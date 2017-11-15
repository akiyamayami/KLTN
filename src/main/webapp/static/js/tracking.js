var map;
var c, d;
var path;
var count = false;
var moveCenter = 0;
var marker;
var config = {
	apiKey : "AIzaSyCCSB8kN6eqVVpsSb7_5dN7os9f8YVuEN8",
	authDomain : "testfb-6251e.firebaseapp.com",
	databaseURL : "https://testfb-6251e.firebaseio.com",
	projectId : "testfb-6251e",
	storageBucket : "testfb-6251e.appspot.com",
	messagingSenderId : "300440751235"
};
firebase.initializeApp(config);
var database = firebase.database();
checkAndUpdateProposalID();
function initMap(proposalID, database) {
	map = new google.maps.Map(document.getElementById("map"), {
		zoom : 16,
		center: {lat: 0, lng: -180},
		mapTypeId : "terrain"
	});
	var data = database.ref("proposal/" + proposalID);
	var flightPath = new google.maps.Polyline({
		geodesic : true,
		strokeColor : "#FF0000",
		strokeOpacity : 5.0,
		strokeWeight : 10
	});
	data.on("value", function(snap) {
		c = snap.val();
		if (c != null && !count) {
			var arr = Object.values(c);
			path = flightPath.getPath();
			for ( var key in arr) {
				path.push(new google.maps.LatLng(arr[key].lat, arr[key].lng));
			}
			var lastAdress = arr.pop();
			map.setCenter(new google.maps.LatLng(lastAdress.lat,
							lastAdress.lng));
			flightPath.setMap(map);
			flightPath.setPath(path);
			count = true;
			marker = new google.maps.Marker({
                position: new google.maps.LatLng(lastAdress.lat, lastAdress.lng),
                title:"Your are here"
            });
            marker.setMap(map);
		} else {
			if (c != null) {
				moveCenter++;
				var arr = Object.values(c);
				path = flightPath.getPath();
				d = arr.pop();
				path = flightPath.getPath();
				var myLatlng = new google.maps.LatLng(d.lat, d.lng);
				path.push(myLatlng);
				flightPath.setPath(path);
				if (moveCenter == 5) {
					map.setCenter(new google.maps.LatLng(d.lat, d.lng));
				}
				marker.setPosition(myLatlng);
			}
		}
	});
}
function checkAndUpdateProposalID(){
	$('.id-car').each(function() {
		var carID = $(this).text();
		console.log("carID = " + carID);
		var proposalID = $(this).parent("div").find(".id-proposal").text();
		var datax = database.ref("car/" + carID + "/proposalID");
		datax.once("value", function(snap) {
			var snapData = snap.val();
			console.log("snapData = " + snapData);
			console.log("proposalID = " + proposalID);
			if(proposalID != snapData)
			{
				var datay = database.ref("car/" + carID).update({"proposalID" : parseInt(proposalID)});
				console.log("update success");
			}
		});
	});
}
$(document).ready(function() {

	$(".info-driver").on("click", function() {
		$("#map").show();
		initMap($(this).find(".id-proposal").text(), database);
	});
});
