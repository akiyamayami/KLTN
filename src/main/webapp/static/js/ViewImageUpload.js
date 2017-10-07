$(document).ready(function(){
	var avatar = "static/img/user/" + $("#email-user").val() + ".jpg";
    if ($("#email-user").val() != '' && $("#email-user").val() != null) {
	    $('.fileinput').addClass('fileinput-exists').removeClass('fileinput-new');
	    $('.fileinput-filename').html(avatar);
	    $(".fileinput-preview").append("<img src="+avatar+">");
	}
});