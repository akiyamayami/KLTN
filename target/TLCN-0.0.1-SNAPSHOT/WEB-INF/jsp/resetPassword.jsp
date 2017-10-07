<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>

<link href="static/css/bootstrap.min.css" rel="stylesheet"/>
<link href="static/css/main.css" rel="stylesheet"/>



<style type="text/css">
#main-region1{
    border: 1px #e3e3e3 solid; 
    border-radius:10px;
    height:calc(100vh - 200px);
    margin-top:10px;
    margin-bottom:10px;
}
</style>
</head>
<body>
	<t:header>
	</t:header>
	<div class="container">
		<div id="main-region1">
			<div class="form">
				<c:choose>
					<c:when test="${MODE == 'FOGET_PASSWORD'}">
						<form action="/foget-password" id="form_login" method="POST"
							class="form-inline">
							<h1 style="margin-bottom: 50pex;">
								<strong>Reset Password</strong>
							</h1>
							<div class="form-group">
								<label for="email">Email:</label> <input type="email"
									class="form-control" name="email" id="email">
							</div>
							<button type="submit" class="btn btn-default">Reset
								password</button>
						</form>

					</c:when>
					<c:when test="${ MODE == 'CHANGE_PASSWORD'}">
						<form:form action="/update-password"
							modelAttribute="ModelPassword" id="form_login" method="POST"
							class="form-horizontal">
							<h1 style="margin-bottom: 30px;">
								<strong>Change Password</strong>
							</h1>
							<div class="form-group">
								<label class="control-label col-sm-2 col-sm-offset-2">Password</label>
								<div class="col-sm-4">
									<form:input type="password" class="form-control"
										path='password' placeholder="Enter password" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2 col-sm-offset-2">Confirm
									password</label>
								<div class="col-sm-4">
									<form:input type="password" class="form-control"
										path='passwordconfirm' placeholder="Enter confirm password" />
									<div id="globalError" class="col-sm-12"
										style="display: none; color: red;"></div>
								</div>

							</div>
							<div style="display: none;" id="hint" class="form-group">
								<div class="col-sm-offset-4 col-sm-4 ">
									<div>
										Hint: <a href="https://goo.gl/iHyHHP">Click me</a>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-12">
									<button type="submit" class="btn btn-default">Update
										Password</button>
								</div>
							</div>
						</form:form>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
	<t:footer></t:footer>
	<script type="text/javascript" src="static/js/jquery.min.js"></script>
	<script type="text/javascript" src="static/js/pwstrength.js"></script>
	<script type="text/javascript" src="static/js/passwordStrength.js"></script>
</body>
</html>