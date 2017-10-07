$(document).ready(function(){
    var x = 1;
    var y = 1;
    var currentLocation = window.location;
    $("[data-toggle=tooltip]").tooltip();
    $(".detail-car").hide();
    $("#choicecar").change(function(){
    	$(".detail-car").slideUp("slow");
    	x = 1;
    });
    $(".icon-show-detail").click(function(){
    	var carID = $("#choicecar").val();
    	var detaildiverID = "#detail-car-" + carID;
        if(x == 1){
            $(detaildiverID).slideDown("slow");
            x = 0;
        }else{
        	$(".detail-car").slideUp("slow");
        	x = 1;
        }
    });
    // date time picker
    $(".date-picker2").datepicker({
        format: "dd-mm-yyyy"
    });
    $(".date-picker").datepicker({
        format: "dd-mm-yyyy"
    });
    $(".date-picker").each(function(){
    	if($(this).val() == "" || $(this).val() == null){
    		$(this).datepicker("update",setcurrentday());
    	}
    });
    $(".input-small").timepicker({
    	showMeridian:false,
	    minuteStep:30
    })
   
    
    //Show hide info driver
    $(".driver-info").hide();
    $('.show-deital-driver').click(function(){
        $(".driver-info").hide();
        var row_index = $(this).closest("tr").index();
        var idinfo = "#driver-info-"+row_index;
        $(idinfo).slideDown("slow");
    });
    $("#cancel-filter").hide();
    if($("#datecreate").val() != null){
    	checkfilterornot();
    }
    $("#type").change(function(){
    	checkfilterornot();
    });
	$("#stt").change(function(){
		checkfilterornot();
	});
    $("#cancel-filter").click(function(){
    	$("#datecreate").val("");
    	$("#type").val("0");
    	$("#stt").val("-1");
    });
});
function checkfilterornot(){
	var date = $("#datecreate").val();
	var type = $("#type").val();
	var stt = $("#stt").val();
	if(date != ""  || type != "0" || stt != "-1"){
		$("#cancel-filter").show();
	}
	else{
		$("#cancel-filter").hide();
	}
}
function setcurrentday(){
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();

    if(dd<10) {
        dd = '0'+dd
    } 

    if(mm<10) {
        mm = '0'+mm
    } 
    return dd + "-" + mm + "-" + yyyy;
}