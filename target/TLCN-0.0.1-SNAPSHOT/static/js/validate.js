$(document).ready(function(){
	
	
	/*function getDate(date, time){
		var startDate = date.split("-");
		var startTime = time.split(":");
		startDate = new Date(startDate[2], startDate[1] - 1, startDate[0],startTime[0], startTime[1],0,0);
		return startDate;
	}
	// validate form proposal
	$.validator.addMethod("greaterThan", function(value, element, params) {
		var p = [];
		p[0] = $("#usetodate").val();
		p[1] = $("#usetotime").val();
		p[2] = $("#usefromdate").val();
		p[3] = $("#usefromtime").val()
		
		console.log(value + "," + p[0] + "," + p[1] + "," + p[2]);
		console.log(getDate(value,p[0]).getTime() > getDate(p[1],p[2]).getTime());
        if (!/Invalid|NaN/.test(new Date(value)))
            return new Date(value) > new Date($(params).val());
        return isNaN(value) && isNaN($(params).val()) 
            || (Number(value) > Number($(params).val())); 
		return getDate(p[0],p[1]).getTime() > getDate(p[2],p[3]).getTime();  
    },'Ngày kết thúc phải lớn hơn ngày bắt đầu');*/
    $.validator.addMethod("pdfOnly",function(value,element,params){
    	if(value == null || value == "")
    		return true;
        var ext = value.split('.').pop();
        if(ext == "pdf")
            return true;
        return false;
    },"Chỉ được chọn file .pdf");
    $.validator.addMethod('filesize', function(value, element, param) {
        // param = size (en bytes) 
        // element = element to validate (<input>)
        // value = value of the element (file name)
        return this.optional(element) || (element.files[0].size <= 10240000) 
    },"File quá lớn xin chọn file < 10MB");
    $("#Proposal").validate({
        rules: {
            name: "required",
            detail: "required",
            usetodate: { 
                required : true,
            },
            usefromdate: {
                required : true,
            },
            usetotime: {
                required : true,
            },
            usetofromtime: {
                required : true,
            },
            file :{
                pdfOnly: true,
                filesize : 10240000,
            }
        }
    });
    //
    $("#Car").validate({
    	rules: {
    		licenseplate: "required",
    		type : "required",
    		seats : {
    			required: true,
    			digits: true,
    		}
        }
    });
    $('#form-create-car').validate({
    	rules: {
    		licenseplate: "required",
    		type : "required",
    		seats : {
    			required: true,
    			digits: true,
    		},
    		emailDriver: "required",
        }
    });
});