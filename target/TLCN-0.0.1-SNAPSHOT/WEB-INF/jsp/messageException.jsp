<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${x.error}</title>
<script type="text/javascript">
	function goBack() {
	  window.history.back()
	}
</script>
</head>
<body>
	<h1>Error : ${x.error}</h1>
	<h2>Code : ${x.code} ${x.mescode}</h2>
	<h3>Message : ${x.message	}</h3>
	<h3>Click <a a href="#" onclick="goBack()">here</a> to return to the previous page.</h3>
</body>
</html>