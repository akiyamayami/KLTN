

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>
#include "third-party/arduino-json-5.6.7/include/ArduinoJson.h"



#define FIREBASE_HOST "testfb-6251e.firebaseio.com"
#define FIREBASE_AUTH "9Xbvqil0hbtX4MG9QOw5ATlkkEeEceL17AzxzxbM"
#define WIFI_SSID "TraiTơ"
#define WIFI_PASSWORD "anhkhongbiet"

static void smartdelay(unsigned long ms);
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
float lng, lat;
unsigned long age;
TinyGPS gps;
SoftwareSerial ss(2, 3);

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

}

char latConvert[32], lngConvert[32];
void loop() {
  gps.f_get_position(&lat, &lng, &age);
  dtostrf(lat, 8, 6, latConvert);
  dtostrf(lng, 9, 6, lngConvert);
  root["lat"] = latConvert;
  root["lng"] = lngConvert;
  root["time"] = 1351824120;
  Firebase.push("proposal/41", root);
  if (Firebase.failed()) {
    Serial.print("pushing /logs failed:");
    Serial.println(Firebase.error());
    return;
  }
  Serial.println("push");
  smartdelay(1000);
}
static void smartdelay(unsigned long ms) {
  unsigned long start = millis();
  do
  {
    while (ss.available())
      gps.encode(ss.read());
  } while (millis() - start < ms);
}
