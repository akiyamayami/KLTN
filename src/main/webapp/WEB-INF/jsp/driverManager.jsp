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
							<c:when test='${MODE == "MODE_FIND_DRIVER"}'>
								<div class="title-content">
									<div class="row">
										Quản lý tài xế
										<sec:authorize access="hasAuthority('ADD_DRIVER')">
											<div class="col-sm-1" style="float: right; margin-top: 3px;">
											<a href="/create-driver" data-toggle="tooltip"
												data-placement="top" title="Thêm tài xế mới"><i
												class="fa fa-plus fa-lg" aria-hidden="true"></i></a>
											</div>
										</sec:authorize>
									</div>
								</div>
								<table cellpadding="0" cellspacing="0"
									class="table table-striped table-content">
									<thead>
										<tr>
											<th>STT</th>
											<th>Tên</th>
											<th>Email</th>
											<th>Tình trạng</th>
											<sec:authorize access="hasAuthority('CHANGE_DRIVER')">
											<th style="width: 6%;">Xem</th>
											</sec:authorize>
											<sec:authorize access="hasAuthority('REMOVE_DRIVER')">
												<th style="width: 6%;">Xóa</th>
											</sec:authorize>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listdrivers}" var="driver" varStatus="stt" >
											<tr>
												<td>${stt.index + 1}</td>
												<td><c:out value="${driver.name}" /></td>
												<td><c:out value="${driver.email}" /></td>
												<td><c:out value="${driver.sttdriver.name}" /></td>
												<sec:authorize access="hasAuthority('CHANGE_DRIVER')">
													<td ><a href="/change-driver/${driver.email}/"> <i
															class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>
													</a></td>
												</sec:authorize>
												<sec:authorize access="hasAuthority('REMOVE_DRIVER')">
													<c:if test="${driver.sttdriver.sttdriverID != 3 }">
														<td ><a href="/remove-driver/${driver.email}/"> <i
															class="fa fa-trash fa-lg" aria-hidden="true"></i>
														</a></td>
													</c:if>
													
												</sec:authorize>
												
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:when>

							<c:when test="${MODE == 'MODE_CREATE_DRIVER' || MODE == 'MODE_CHANGE_DRIVER'}">
								<c:if test="${MODE == 'MODE_CHANGE_DRIVER'}">
									<div class="title-content">Chỉnh sữa Tài Xế</div>
								</c:if>
								<c:if test="${MODE == 'MODE_CREATE_DRIVER'}">
									<div class="title-content">Thêm Tài Xế</div>
								</c:if>
								<form:form method="post" modelAttribute="Driver"
									action="${typeForm}" class="form-horizontal"
									 enctype="multipart/form-data">
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
										<label class="control-label col-sm-3">Email</label>
										<div class="col-sm-7">
											<form:input path="email" type="email" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="email" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ngày Sinh</label>
										<div class="col-sm-7">
											<form:input path="birthday" type="text"
												class="form-control date-picker" />
											<div class="has-error">
												<form:errors class="control-label" path="birthday" />
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
										<label class="control-label col-sm-3">Địa chỉ</label>
										<div class="col-sm-7">
											<form:input path="address" type="text" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="address" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Kinh nghiệm</label>
										<div class="col-sm-7">
											<form:select path="experience"
												class="btn btn-choose">
												<form:option value="1">1</form:option>
												<form:option value="2">2</form:option>
												<form:option value="3">3</form:option>
												<form:option value="4">4</form:option>
											</form:select>
											<label class="control-label">Năm</label>
											<div class="has-error">
												<form:errors class="control-label" path="experience" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Bằng lái</label>
										<div class="col-sm-7">
											<form:select path="license"
												class="btn btn-choose">
												<form:option value="A1">A1</form:option>
												<form:option value="A2">A2</form:option>
												<form:option value="A3">A3</form:option>
												<form:option value="A4">A4</form:option>
												<form:option value="B1">B1</form:option>
												<form:option value="B2">B2</form:option>
												<form:option value="C">C</form:option>
												<form:option value="D">D</form:option>
												<form:option value="E">E</form:option>
												<form:option value="F">F</form:option>
												<form:option value="FC">FC</form:option>
											</form:select>
											<div class="has-error">
												<form:errors class="control-label" path="license" />
											</div>
										</div>

									</div>
									<c:if test="${MODE == 'MODE_CHANGE_DRIVER'}">
										<div class="form-group">
				                            <label class="control-label col-sm-3">Tình trạng</label>
				                            <div class="col-sm-7">
				                                <form:select path="sttdriverID" class="btn btn-choose">
				                                	<c:forEach items="${listSttDriver}" var="stt">
				                                		<form:option value="${stt.sttdriverID}">
				                                			<c:out value="${stt.name}"></c:out>
														</form:option>
				                                	</c:forEach>
				                                </form:select>
				                                <div class="has-error">
													<form:errors class="control-label" path="sttdriverID" />
												</div>
				                            </div>
				                        </div>
									</c:if>
									
									<div class="form-group">
										<label class="control-label col-sm-3">Xe</label>
										<div class="col-sm-7">
											<table cellpadding="0" cellspacing="0"
												class="table table-bordered">
												<thead>
													<tr>
														<th style="width: 10%;">ID</th>
														<th style="width: 15%">Số xe</th>
														<th>Loại</th>
														<th style="width: 25%;">Số chỗ ngồi</th>
														<th>Xóa</th>
													</tr>
												</thead>
												<tbody id="detail-car-add">
													<c:forEach items="${Driver.listcar}" var="car"
														varStatus="status">
														<tr>
															<td><form:input
																	path="listcar[${status.index}].carID" type="text"
																	readonly="true" style="border:none;" size="1" /></td>
															<td><form:input
																	path="listcar[${status.index}].licenseplate"
																	type="text" readonly="true" style="border:none;"
																	size="20" /></td>
															<td><form:input path="listcar[${status.index}].type"
																	type="text" readonly="true" style="border:none;"
																	size="2" /></td>
															<td><form:input
																	path="listcar[${status.index}].seats" type="text" readonly="true" style="border:none;"
																	size="1"/> Chỗ</td>
															<td><a class="remove-car myClickableThingy"> <i
																	class="fa fa-trash fa-lg" aria-hidden="true"></i>
															</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
											<a class="myClickableThingy" data-toggle="modal" data-target="#myModal">
												Thêm xe 
											</a>
											<div class="has-error">
												<form:errors class="control-label" path="listcar" />
											</div>
										</div>

									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Ảnh</label>
										<div class="col-sm-7" style="padding-top:7px;">
											<div class="fileinput fileinput-new" data-provides="fileinput">
												<div class="fileinput-preview thumbnail"
													data-trigger="fileinput"
													style="width: 200px; height: 150px;"></div>
												<div>
													<span class="btn btn-default btn-file">
													<span class="fileinput-new">Select image</span>
													<span class="fileinput-exists">Change</span>
													<form:input type="file" path="file"/></span> 
													<a class="btn btn-default fileinput-exists myClickableThingy"
														data-dismiss="fileinput">Remove</a>
												</div>
											</div>
											<input type="hidden" id="email-user" value="<c:out value='${Driver.email}'/>" />
											<div class="has-error">
												<form:errors class="control-label" path="file" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<c:if test="${MODE == 'MODE_CHANGE_DRIVER'}">
											<div class="col-sm-offset-5 col-sm-5">
												
												<button type="submit" class="btn btn-default">Chỉnh sửa</button>
												<sec:authorize access="hasAuthority('REMOVE_DRIVER')">
													<a href="/remove-driver/<c:out value='${Driver.email}'/>/" class="btn btn-default">Xóa</a>
												</sec:authorize>
												<a href="/list-driver" class="btn btn-default">Trở về</a>
											</div>
										</c:if>
										<c:if test="${MODE == 'MODE_CREATE_DRIVER'}">
											<div class="col-sm-offset-5 col-sm-3">
												<button type="submit" class="btn btn-default">Thêm</button>
												<a href="/list-driver" class="btn btn-default">Trở về</a>
											</div>
										</c:if>
									</div>
								</form:form>
								<!-- Modal add new car -->
								<div class="modal fade" id="myModal" role="dialog">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<h4 class="modal-title">Thêm Xe</h4>
											</div>
											<div class="modal-body">
												<div class="">
													<ul class="nav nav-tabs nav-justified">
														<li class="active"><a href="#available"
															data-toggle="tab">Có sẵn</a></li>
														<sec:authorize access="hasAuthority('ADD_CAR')">
															<li><a href="#new" data-toggle="tab">Mới</a></li>
														</sec:authorize>
													</ul>
												</div>
												<div class="tab-content">
													<div class="tab-pane fade in active" id="available">
														<table cellpadding="0" cellspacing="0"
															class="table table-bordered table-content">
															<thead>
																<tr>
																	<th>ID</th>
																	<th>Số xe</th>
																	<th>Loại</th>
																	<th>Số chỗ ngồi</th>
																	<th></th>
																</tr>
															</thead>
															<tbody id="detail-car-free">
																<c:forEach items="${ListCarFree}" var="carfree">
																	<tr>
																		<td><c:out value="${carfree.carID}" /></td>
																		<td><c:out value="${carfree.licenseplate}" /></td>
																		<td><c:out value="${carfree.type}" /></td>
																		<td><c:out value="${carfree.seats}" /> Chỗ</td>
																		<td><a class="choose-car myClickableThingy"> <i
																				class="fa fa-square-o fa-lg" aria-hidden="true"></i>
																		</a></td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
														<div class="modal-footer">
															<button id="add-car" type="button"
																class="btn btn-default">Thêm</button>
														</div>
													</div>
													<sec:authorize access="hasAuthority('ADD_CAR')">
													<div class="tab-pane fade" id="new">
														<form:form action="/create-car" id="form-create-car"
															method="POST" modelAttribute="Car">
															<div class="form-group">
																<label>Số xe :</label>
																<form:input path="licenseplate" type="text"
																	class="form-control" />
															</div>
															<div class="form-group">
																<label>Loại xe :</label>
																<form:input path="type" type="text" class="form-control" />
															</div>
															<div class="form-group">
																<label>Số chỗ ngồi :</label>
																<form:input path="seats" type="number" min="1"
																	class="form-control" />
															</div>
															<div class="modal-footer">
																<button type="submit" id="btn-submit-form-create-car"
																	class="btn btn-default">Thêm</button>
															</div>
														</form:form>
													</div>
													</sec:authorize>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- end modal add new car -->
								
							</c:when>

						</c:choose>
					</div>
				</div>
			<t:notifyandcalendar></t:notifyandcalendar>
		</div>
	</div>
	</div>
	<t:footer></t:footer>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-timepicker/0.5.2/js/bootstrap-timepicker.min.js"></script>
	
	
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/jquery.validate.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.min.js"></script>
	
	<script type="text/javascript" src="static/js/main.js"></script>
	<script type="text/javascript" src="static/js/caranddriver.js"></script>
	<script type="text/javascript" src="static/js/validate.js"></script>
	<script type="text/javascript" src="static/js/ViewImageUpload.js"></script>
</body>
</html>