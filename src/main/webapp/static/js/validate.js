$(document).ready(
		function() {

			function getDate(date, time) {
				var startDate = date.split("-");
				var startTime = time.split(":");
				startDate = new Date(startDate[2], startDate[1] - 1,
						startDate[0], startTime[0], startTime[1], 0, 0);
				return startDate;
			}
			// validate form proposa
			$.validator.addMethod("lessThanUseTime", function(value, element,
					params) {

				if (value == null || value == ""
						|| $("#usefromtime").val() == null
						|| $("#usefromtime").val() == "")
					return false;
				var timeunsefliter = $("#usefromtime").val().split(":");
				var timepickupfilter = value.split(":");
				var timeuse = new Date(0, 0, 0, timeunsefliter[0],
						timeunsefliter[1], 0, 0);
				var timepickup = new Date(0, 0, 0, timepickupfilter[0],
						timepickupfilter[1], 0, 0);
				if (timepickup.getTime() > timeuse.getTime())
					return false;
				return true;
			}, 'Ngày kết thúc phải lớn hơn ngày bắt đầu');
			$.validator.addMethod("greaterThan", function(value, element,
					params) {
				var p = [];
				p[0] = $("#usetodate").val();
				p[1] = $("#usetotime").val();
				p[2] = $("#usefromdate").val();
				p[3] = $("#usefromtime").val();

				console.log(value + "," + p[0] + "," + p[1] + "," + p[2]);
				console
						.log(getDate(value, p[0]).getTime() > getDate(p[1],
								p[2]).getTime());
				return getDate(p[0], p[1]).getTime() > getDate(p[2], p[3])
						.getTime();
			}, 'Ngày kết thúc phải lớn hơn ngày bắt đầu');
			$.validator.addMethod("pdfOnly", function(value, element, params) {
				if (value == null || value == "")
					return true;
				var ext = value.split('.').pop();
				if (ext == "pdf")
					return true;
				return false;
			}, "Chỉ được chọn file .pdf");
			$.validator.addMethod("jpgOnly", function(value, element, params) {
				if (value == null || value == "")
					return true;
				var ext = value.split('.').pop();
				if (ext == "jpg")
					return true;
				return false;
			}, "Chỉ được chọn file .jpg");
			$.validator.addMethod("lessThanNow", function(value, element,
					params) {
				if (value == null || value == "")
					return false;
				var now = new Date();
				var date = new Date(value);
				if (date.getTime() > now.getTime())
					return false;
				return true;
			}, "Ngày sinh không thể lớn hơn hôm nay");
			$.validator.addMethod('filesize10',
					function(value, element, param) {
						// param = size (en bytes)
						// element = element to validate (<input>)
						// value = value of the element (file name)
						console.log(element.files[0].size);
						return element.files[0].size < 10240000;
					}, "File quá lớn xin chọn file < 10MB");
			$.validator.addMethod('filesize1', function(value, element, param) {
				// param = size (en bytes)
				// element = element to validate (<input>)
				// value = value of the element (file name)
				console.log(element.files[0].size);
				return element.files[0].size < 1024000;
			}, "File quá lớn xin chọn file < 1MB");
			$("#Driver").validate({
				rules : {
					name : "required",
					email : {
						required : true,
						email : true,
					},
					birthday : {
						required : true,
						lessThanNow : true,
					},
					phone : "required",
					address : "required",
					experience : "required",
					license : "required",
					file : {
						filesize1 : true,
						jpgOnly : true,
					}
				}
			});
			$("#Proposal").validate({
				rules : {
					name : "required",
					detail : "required",
					usetodate : {
						required : true,
						greaterThan : true,
					},
					usefromdate : {
						required : true,
					},
					usetotime : {
						required : true,
					},
					usetofromtime : {
						required : true,
					},
					file : {
						pdfOnly : true,
						filesize10 : true,
					},
					destination : "required",
					pickuplocation : "required",
					pickuptime : {
						required : true,
						lessThanUseTime : true,
					},
					detail : "required",
					carID : "required",
				}
			});
			//
			$("#Car").validate({
				rules : {
					licenseplate : "required",
					type : "required",
					seats : {
						required : true,
						digits : true,
					}
				}
			});
			$('#form-create-car').validate({
				rules : {
					licenseplate : "required",
					type : "required",
					seats : {
						required : true,
						digits : true,
					},
					emailDriver : "required",
				}
			});
			$("#User").validate({
				rules : {
					email : {
						required : true,
						email : true,
					},
					name : "required",
					phone : {
						required : true,
						digits : true,
					},
					birthday : {
						required : true,
						lessThanNow : true,
					},
					roleID : {
						required : true,
					},
					file : {
						filesize1 : true,
						jpgOnly : true,
					},
				}
			});

		});