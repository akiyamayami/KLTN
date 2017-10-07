<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Car Manager</title>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/main.css" rel="stylesheet" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/css/bootstrap-datepicker3.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/css/jasny-bootstrap.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-timepicker/0.5.2/css/bootstrap-timepicker.min.css" rel="stylesheet" />



</head>
<body>
	<t:header>
		<t:narbar></t:narbar>
	</t:header>
	<div class="container">
		<div id="main-region">
			<div class="row">
				<div class="col-sm-9">
					<c:if test="${not empty messagesSuc}">
						<h1 id="error" class="alert alert-danger">
							${messagesSuc}
						</h1>
					</c:if>
					<c:if test="${not empty messagesEro}">
						<h1 id="error" class="alert alert-success">
							${messagesEro}
						</h1>
					</c:if>
					<div class="main-content">
						<c:choose>
							<c:when test='${MODE == "MODE_MANAGE_USER"}'>
								<div class="title-content">
									<div class="row">
										Quản lý Người dùng
										<sec:authorize access="hasAuthority('ADD_USER')">
										<div class="col-sm-1" style="float: right; margin-top: 3px;">
											<a href="/add-new-user" data-toggle="tooltip"
												data-placement="top" title="Thêm người dùng mới"><i
												class="fa fa-plus fa-lg" aria-hidden="true"></i></a>
										</div>
										</sec:authorize>
									</div>
								</div>
								<table cellpadding="0" cellspacing="0"
									class="table table-striped table-content">
									<thead>
										<tr>
											<th style="width: 10%;">STT</th>
											<th>Tên</th>
											<th>Email</th>
											<th>Role</th>
											<th style="width: 10%;">Chi tiết</th>
											<sec:authorize access="hasAuthority('REMOVE_USER')">
												<th style="width: 6%;">Xóa</th>
											</sec:authorize>
											
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listuser}" var="user" varStatus="stt">
											<tr>
												<td>${stt.index + 1}</td>
												<td><c:out value="${user.name}" /></td>
												<td><c:out value="${user.email}" /></td>
												<td><c:out value="${user.roleUser.name}" /></td>
												<sec:authorize access="hasAuthority('CHANGE_USER')">
													<td><a href="/edit-user/${user.email}/"> <i
															class="fa fa-info-circle fa-lg" aria-hidden="true"></i>
													</a></td>
												</sec:authorize>
												<sec:authorize access="hasAuthority('REMOVE_USER')">
													<td><a href="/remove-user/${user.email}/"> <i
														class="fa fa-trash fa-lg" aria-hidden="true"></i>
													</a></td>
												</sec:authorize>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:when>

							<c:when test="${MODE == 'MODE_ADD_USER'}">
								<div class="title-content">Thêm người dùng</div>
								<form:form modelAttribute="User" class="form-horizontal"
									action="/add-new-user" method="post" enctype="multipart/form-data">
									<div class="form-group">
										<label class="control-label col-sm-3">Email</label>
										<div class="col-sm-7">
											<form:input path="email" type="email" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="email" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tên</label>
										<div class="col-sm-7">
											<form:input path="name" type="text" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="name" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">SDT</label>
										<div class="col-sm-7">
											<form:input path="phone" type="number" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="phone" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ngày sinh</label>
										<div class="col-sm-7">
											<form:input type="text" path="birthday"
												class="date-picker form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="birthday" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Role</label>
										<div class="col-sm-7">
											<form:select path="roleID" class="form-control">
												<c:forEach items="${listrole}" var="role">
													<form:option value="${role.roleID }">
														<c:out value="${role.name}" />
													</form:option>
												</c:forEach>
											</form:select>
											<div class="has-error">
												<form:errors class="control-label" path="roleID" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ảnh</label>
										<div class="col-sm-7" style="padding-top: 7px;">
											<div class="fileinput fileinput-new"
												data-provides="fileinput">
												<div class="fileinput-preview thumbnail"
													data-trigger="fileinput"
													style="width: 200px; height: 150px;"></div>
												<div>
													<span class="btn btn-default btn-file"> <span
														class="fileinput-new">Select image</span> <span
														class="fileinput-exists">Change</span> <form:input
															type="file" path="file" /></span> <a
														class="btn btn-default fileinput-exists myClickableThingy"
														data-dismiss="fileinput">Remove</a>
												</div>
											</div>
											<input type="hidden" id="email-user"
												value="<c:out value='${User.email}'/>" />
											<div class="has-error">
												<form:errors class="control-label" path="file" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-3">
											<button type="submit" class="btn btn-default">Thêm</button>
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>

							</c:when>
							<c:when test="${MODE == 'MODE_CHANGE_USER'}">
								<div class="title-content">Thông tin người dùng</div>
								<form:form modelAttribute="User" class="form-horizontal"
									action="/edit-user" method="post" enctype="multipart/form-data">
									<div class="form-group">
										<label class="control-label col-sm-3">Email</label>
										<div class="col-sm-7">
											<form:input path="email" type="email" disabled="true"
												class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="email" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tên</label>
										<div class="col-sm-7">
											<form:input path="name" disabled="true" type="text" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="name" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3" >SDT</label>
										<div class="col-sm-7">
											<form:input path="phone" type="number" disabled="true" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="phone" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ngày sinh</label>
										<div class="col-sm-7">
											<form:input type="text" disabled="true" path="birthday"
												class="date-picker form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="birthday" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Role</label>
										<div class="col-sm-7">
											<form:select path="roleID" class="form-control">
												<c:forEach items="${listrole}" var="role">
													<form:option value="${role.roleID }">
														<c:out value="${role.name}" />
													</form:option>
												</c:forEach>
											</form:select>
											<div class="has-error">
												<form:errors class="control-label" path="roleID" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ảnh</label>
										<div class="col-sm-7" style="padding-top: 7px;">
											<div class="fileinput fileinput-new"
												data-provides="fileinput">
												<div class="fileinput-preview thumbnail"
													data-trigger="fileinput"
													style="width: 200px; height: 150px;"></div>
												<div>
													<span class="btn btn-default btn-file"> <span
														class="fileinput-new">Select image</span> <span
														class="fileinput-exists">Change</span> <form:input
															type="file" path="file" disabled="true" /></span> <a
														class="btn btn-default fileinput-exists myClickableThingy"
														data-dismiss="fileinput">Remove</a>
												</div>
											</div>
											<input type="hidden" id="email-user"
												value="<c:out value='${User.email}'/>" />
											<div class="has-error">
												<form:errors class="control-label" path="file" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-3">
											<sec:authorize access="hasAuthority('CHANGE_USER')">
												<button type="submit" class="btn btn-default">Chỉnh
													sữa</button>
											</sec:authorize>
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>

							</c:when>
							<c:when test="${MODE == 'MODE_CHANGE_PASSWORD'}">
								<div class="title-content">Đổi mật khẩu</div>
								<form:form action="/change-your-password"
									modelAttribute="ModelPassword" method="POST"
									class="form-horizontal">
									<div class="form-group">
										<label class="control-label col-sm-3">Password</label>
										<div class="col-sm-7">
											<form:input type="password" class="form-control"
												path='password' placeholder="Enter password" />
											<div class="has-error">
												<form:errors class="control-label" path="password" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Confirm
											password</label>
										<div class="col-sm-7">
											<form:input type="password" class="form-control"
												path='passwordconfirm' placeholder="Enter confirm password" />
											<div id="globalError" class="col-sm-12"
												style="display: none; color: red;"></div>
											<div class="has-error">
												<form:errors class="control-label" path="password" />
											</div>
										</div>
									</div>
									<div style="display: none;text-algin:center;" id="hint" class="form-group">
										<div class="col-sm-offset-4 col-sm-4 ">
											<div>
												Hint: <a href="https://goo.gl/iHyHHP">Click me</a>
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-3">
											<button type="submit" class="btn btn-default">Update Password</button>
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>
							</c:when>
							<c:otherwise>
								<div class="title-content">Thông tin cá nhân</div>
								<form:form modelAttribute="User" class="form-horizontal"
									action="/edit-profile" method="post" enctype="multipart/form-data">
									<div class="form-group">
										<label class="control-label col-sm-3">Email</label>
										<div class="col-sm-7">
											<form:input path="email" type="email" readonly="true"
												class="form-control" />

											<div class="has-error">
												<form:errors class="control-label" path="email" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tên</label>
										<div class="col-sm-7">
											<form:input path="name" type="text" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="name" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">SDT</label>
										<div class="col-sm-7">
											<form:input path="phone" type="number" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="phone" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ngày sinh</label>
										<div class="col-sm-7">
											<form:input type="text" path="birthday"
												class="date-picker form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="birthday" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Role</label>
										<div class="col-sm-7">
											<form:select path="roleID" class="form-control">
												<form:option value="${role.roleID }">
													<c:out value="${role.name}" />
												</form:option>
											</form:select>
											<div class="has-error">
												<form:errors class="control-label" path="roleID" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ảnh</label>
										<div class="col-sm-7" style="padding-top: 7px;">
											<div class="fileinput fileinput-new"
												data-provides="fileinput">
												<div class="fileinput-preview thumbnail"
													data-trigger="fileinput"
													style="width: 200px; height: 150px;"></div>
												<div>
													<span class="btn btn-default btn-file"> 
													<span class="fileinput-new">Select image</span> 
													<span class="fileinput-exists">Change</span> 
													<form:input type="file" path="file" /></span> <a
														class="btn btn-default fileinput-exists myClickableThingy"
														data-dismiss="fileinput">Remove</a>
												</div>
											</div>
											<input type="hidden" id="email-user"
												value="<c:out value='${User.email}'/>" />
											<div class="has-error">
												<form:errors class="control-label" path="file" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-3">
											<button type="submit" class="btn btn-default">Chỉnh
												sữa</button>
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<t:notifyandcalendar></t:notifyandcalendar>
			</div>
		</div>
	</div>
	<t:footer></t:footer>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pwstrength-bootstrap/2.1.1/pwstrength-bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-timepicker/0.5.2/js/bootstrap-timepicker.min.js"></script>
	
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.min.js"></script>
	<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/jquery.validate.min.js"></script>
	<script type="text/javascript" src="static/js/ViewImageUpload.js"></script>
	<script type="text/javascript" src="static/js/main.js"></script>
	<script type="text/javascript" src="static/js/validate.js"></script>
	
</body>
</html>