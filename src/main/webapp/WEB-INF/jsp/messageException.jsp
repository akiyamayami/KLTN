<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${x.message}</title>
<script type="text/javascript">
	function goBack() {
	  window.history.back()
	}
</script>
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
</head>
<body>
<t:header>
	<t:narbar></t:narbar>
</t:header>
<div class="container">
	<div id="main-region1">
        <div class="form">
        	<div class="MessageBox">
        		<h1>${x.message}</h1>
        		<h3>Click <a a href="#" onclick="goBack()">here</a> to return to the previous page.</h3>
        		<img src="static/img/errors">
        		<h1 style="font-weight: bold;margin-top: 0px;">${x.code}</h1>
        	</div>
        </div>
    </div>
</div>
<t:footer></t:footer>
</body>
</html>