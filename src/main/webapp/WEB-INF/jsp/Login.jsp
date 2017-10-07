<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setBundle basename="messages" />
<fmt:message key="message.password" var="noPass" />
<fmt:message key="message.email" var="noEmail" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
<link href="static/css/main.css" rel="stylesheet"/>
<style type="text/css">
#main-region1{
    border: 1px #e3e3e3 solid; 
    border-radius:10px;
    height:calc(100vh - 200px);
    margin-top:10px;
    margin-bottom:10px;
        text-align: center;
}
</style>
<script type="text/javascript">
function validate() {
    if (document.f.username.value == "" && document.f.password.value == "") {
        alert("${noEmail} and ${noPass}");
	    document.f.username.focus();
	    return false;
    }
    if (document.f.username.value == "") {
	    alert("${noEmail}");
	    document.f.username.focus();
	    return false;
     }
     if (document.f.password.value == "") {
	    alert("${noPass}");
	    document.f.password.focus();
	    return false;
     }
}
</script>
</head>
<body>
<t:header>
</t:header>
<div class="container">
		<c:if test="${param.logSucc}">
			<h1 id="error" class="alert alert-success">
				<spring:message code="message.logoutSucc"></spring:message>
			</h1>
		</c:if>
		<c:if test="${not empty messages}">
			<h1 id="error" class="alert alert-success">
				${messages}
			</h1>
		</c:if>
		<div id="main-region1">
        <div class="form"> 
	    	<sec:authorize access="isAnonymous()">
	    		<form name='f' action="login" id="form_login" method="POST" class="form-horizontal" onsubmit="return validate();">
                <h1 ><strong>Login</strong></h1>
                <div class="form-group">
                    <label class="control-label col-sm-2 col-sm-offset-3" for="email" >Email:</label>
                    <div class="col-sm-3">
                        <input type="email" class="form-control" name='username' id="email" placeholder="Enter email">
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2 col-sm-offset-3"  for="pwd">Password:</label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" name='password' id="pwd" placeholder="Enter password">
                    </div>
                </div>
                <c:if test="${param.error != null}">
					<p style="color:red;">Sai mật khẩu hoặc tài khoản</p>
				</c:if>
                <div class="form-group"> 
                    <div class="col-sm-12">
                        <button type="submit" class="btn btn-default">Login</button> 
                        <!-- <input class="btn btn-primary" name="submit" type="submit" value="<c:out value="${submit}"/>" /> -->
                    </div>
                </div>
            </form>
	    	</sec:authorize>
            <sec:authorize access="isAuthenticated()">
            	<h1>You Already Login</h1>
            	Click <a href="/">here</a> to back Home.
            </sec:authorize>
        </div>
        <a href="/foget-password">Forget password</a>
    </div>
</div>
<t:footer></t:footer>
</body>
</html>