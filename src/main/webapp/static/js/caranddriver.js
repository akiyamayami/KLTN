$(document).ready(function(){
	var count = $("#detail-car-add tr").length;
	$(document).on('click','.choose-car',function(){
        $(this).find("i").toggleClass('fa-square-o fa-check-square-o');
    });
    $("#add-car").click(function(){
        $(".fa-check-square-o").each(function(){
            $("#detail-car-add").append(createcardetail($(this).closest("tr"),"add",count));
            $(this).closest("tr").remove();
            count += 1;
        });
            
        $('#myModal').modal('toggle');
    });
    $(document).on('click','.remove-car',function(){
        $("#detail-car-free").append(createcardetail($(this).closest("tr"),"remove",count));
        $(this).closest("tr").remove();
    });
    
    $('#form-create-car').submit(function(e){
    	e.preventDefault();
		if($(this).valid()){
			var dataform = $(this).serialize();
	        $.ajax({
	        	type : "POST",
	        	data : dataform,
	        	url : "create-car-x",
	    	    success: function(response){
	    	       if(response != "errors"){
	    	    	   $("#detail-car-add").append(response);
	    	    	   $('#myModal').modal('toggle');
	    	    	   $(this).trigger("reset");
	    	    	   alert("Thêm thành công");
	    	       }
	    	       else{
	    	    	   alert("Có lỗi xảy ra xui lỏng thử lại");
	    	       }
	    	    },
	    	    error: function(xhr, textStatus, errorThrown){
	    	        alert("Có lỗi xảy ra xui lỏng thử lại");
	    	    }
	        });
		}
	});
});

function createcardetail(x,type,count){
    var html = "<tr>";
    var child = "";
	if (type == "add") {
		var i = 1;
		child = "td:nth-child(" + i + ")";
		html += "<td><input id='listcar"+ count +".carID' name='listcar["+ count +"].carID' type='text' readonly='true' value='"
				+ x.find(child).text() + "' style='border:none;' size='1'/></td>";
		i++;
		child = "td:nth-child(" + i + ")";
		html += "<td><input id='listcar"+ count +".licenseplate' name='listcar["+ count +"].licenseplate' type='text' readonly='true' value='"
				+ x.find(child).text() + "' style='border:none;' size='6'/></td>";
		i++;
		child = "td:nth-child(" + i + ")";
		html += "<td><input id='listcar"+ count +".type' name='listcar["+ count +"].type' type='text' readonly='true' value='"
				+ x.find(child).text() + "' style='border:none;' size='2'/></td>";
		i++;
		child = "td:nth-child(" + i + ")";
		html += "<td><input id='listcar"+ count +".seats' name='listcar["+ count +"].seats' type='text' readonly='true' value='"
				+ x.find(child).text().slice(0,x.find(child).text().length - 3) + "' style='border:none;' size='1'/>Chỗ</td>";
		i++;
        html += "<td><a href='#' class='remove-car'><i class='fa fa-trash fa-lg' aria-hidden='true'></i></a></td>";
    }
    else{
    	for(var i = 1 ; i < 4 ; i++){
            child = "td:nth-child("+i+") input";
            html += "<td>" + x.find(child).val() +"</td>";
        }
        child = "td:nth-child("+4+") input";
        html += "<td>" + x.find(child).val() +" Chỗ</td>";
        html += "<td><a href='#' class='choose-car'><i class='fa fa-square-o fa-lg' aria-hidden='true'></i></a></td>";
    }
    html += "</tr>";
    return html;
}