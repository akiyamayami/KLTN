$(document).ready(function(){
	var html = "<tr id='loading-icon'><td style='padding: 0px;text-align: center;'><img src='https://image.ibb.co/edo8s5/Spinner_2.gif' alt='Spinner' border='0' /></td></tr>";
    var y = 1;
    $(window).scroll(function() {
    	if($(window).scrollTop() + $(window).height() > $(document).height() - 10) {
    		if(y == 1){
    			y = 0;
    			$(".list-notify").append(html);
    			$.ajax({
    				type: "POST",
    				data :{ numberNotify : $(".list-notify .item-notify2").length },
    				url : "show-all-notify",
    				success: function(response){
    					$("#loading-icon").remove();
    					if(response != null && response != "" ){
    						 $(".list-notify").append(response);
    						 y = 1;
    					}
    				},
    				error: function(xhr, textStatus, errorThrown){
    					$("#loading-icon").remove();
    	    	        alert("Có lỗi xảy ra xui lỏng thử lại");
    	    	    }
    			});
    		}
    	}
    }); 
});