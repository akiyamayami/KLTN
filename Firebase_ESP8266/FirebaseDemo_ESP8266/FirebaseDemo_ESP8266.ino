#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>
#include <ArduinoJson.h>


#define FIREBASE_HOST "testfb-6251e.firebaseio.com"
#define FIREBASE_AUTH "9Xbvqil0hbtX4MG9QOw5ATlkkEeEceL17AzxzxbM"
#define WIFI_SSID "11_01" // Cô đổi thành tên wifi của cô
#define WIFI_PASSWORD "1234567890" // đây là pass wifi

//#define WIFI_SSID "xxx"
//#define WIFI_PASSWORD "12345678"

static void smartdelay(unsigned long ms);
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
float lng, lat;
unsigned long age;
int proposalID;
TinyGPS gps;
SoftwareSerial ss(2, 3);
String address;
bool flag;
void setup() {
  Serial.begin(115200);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  proposalID = Firebase.getInt("car/1/proposalID");
}

char latConvert[32], lngConvert[32];
int proposal;
void loop() {
  gps.f_get_position(&lat, &lng, &age);
  dtostrf(lat, 8, 6, latConvert);
  dtostrf(lng, 9, 6, lngConvert);
  root["lat"] = latConvert;
  root["lng"] = lngConvert;
  address = "proposal/" + String(proposalID);
  Serial.println(address);
  Firebase.push(address, root);
  if (Firebase.failed()) {
    Serial.println(Firebase.error());
    return;
  }
  Serial.println("push");
  smartdelay(800);
  proposal = Firebase.getInt("car/1/proposalID");
  if(proposalID != proposal){
    Serial.println("Change proposalID:");
    Serial.print(proposalID + " -> " + proposal);
    proposalID = proposal;
  }
  delay(200);
}
static void smartdelay(unsigned long ms) {
  unsigned long start = millis();
  do
  {
    while (ss.available())
    {
      gps.encode(ss.read());
    }
  } while (millis() - start < ms);
}
