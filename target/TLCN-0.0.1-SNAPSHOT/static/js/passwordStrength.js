$(document).ready(function() {
	$(":password").keyup(function() {
		if ($("#password").val() != $("#passwordconfirm").val()) {
			$("#globalError").show().html("Password not match");
		} else {
			$("#globalError").html("").hide();
		}
	});

	options = {
		common : {
			minChar : 8
		},
		ui : {
			showVerdictsInsideProgressBar : true,
			showErrors : true,
			errorMessages : {
				wordLength : "Your password is too short(min 8 char)",
				wordNotEmail : "Do not use your email as your password",
				wordSequences : "Your password contains sequences",
				wordLowercase : "Use lower case characters",
				wordUppercase : "Use upper case characters",
				wordOneNumber : "Use numbers"
			}
		}
	};
	$("#password").pwstrength(options);
	$(function() {
		function show_popup() {
			$("#hint").slideDown();
		}
		;
		window.setTimeout(show_popup, 10000); // 10 seconds
	});
});