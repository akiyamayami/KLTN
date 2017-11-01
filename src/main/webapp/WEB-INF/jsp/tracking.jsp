<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link href="static/css/main.css" rel="stylesheet" type="text/css" />
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
	$(document).ready(function() {
		$("#map").hide();
	});
</script>
</head>
<body>
	<t:header>
		<t:narbar></t:narbar>
	</t:header>
	<div class="container">
		<div id="main-region">
			<div class="row">
				<div class="col-sm-9">
					<div class="main-content">
						<div id="map"></div>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="block">
						<div class="title-content">Tài xế đang hoạt động</div>
						<c:choose>
							<c:when test="${fn:length(listProposal) == 0}">
								<div style="text-align: center; color: #c6c6c6;">Hiện
									không có tài xế nào đang hoạt động</div>
							</c:when>
							<c:otherwise>
								<div>
									<c:forEach items="${listProposal}" var="proposal">
										<div class="info-driver myClickableThingy">
											<img src="static/img/user/${proposal.car.driver.email}.jpg"
												height="32px" width="32px" /> <span>${proposal.car.driver.name}</span>
											<span class="id-proposal" style="display: none">${proposal.proposalID}</span>
										</div>
									</c:forEach>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
	<t:footer></t:footer>
	<script type="text/javascript"
		src="https://www.gstatic.com/firebasejs/4.5.0/firebase.js"></script>
	<script type="text/javascript" src="static/js/tracking.js"></script>
	<script type="text/javascript"
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA1LZU7U-fPMn6RMgupnjYyQ6gADycg_ow"
		async defer></script>
	

</body>
</html>