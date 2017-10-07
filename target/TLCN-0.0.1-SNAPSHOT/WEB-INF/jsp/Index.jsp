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
<title>Home</title>
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/main.css" rel="stylesheet" />
<link href="static/css/font-awesome.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-timepicker.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-datepicker3.min.css" rel="stylesheet" />

<script type="text/javascript" src="static/js/jquery.min.js"></script>

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
						<c:choose>
							<c:when test='${MODE == "MODE_FIND_PROPOSAL" || MODE == "MODE_FIND_MY_PROPOSAL"}'>
								<c:if test="${MODE == 'MODE_FIND_PROPOSAL'}">
									<div class="title-content">
										<div class="row">Tìm đề nghị</div>
									</div>
								</c:if>
								<c:if test="${MODE == 'MODE_FIND_MY_PROPOSAL'}">
									<div class="title-content">
										<div class="row">
											Đề nghị của tôi
											<div class="col-sm-1" style="float: right; margin-top: 3px;">
												<a href="/create-proposal" data-toggle="tooltip" data-placement="top"
													title="Tạo đề nghị"><i class="fa fa-plus fa-lg"
													aria-hidden="true"></i></a>
											</div>
										</div>
									</div>
								</c:if>
								<div class="title-content2">
									<form:form method="post" modelAttribute="filter-model"
										action="/" class="form-inline form-filer-data">
										<div class="form-group">
											<label class="control-label">Ngày :</label>
											<form:input path="datecreate" type="text"
												class="form-control date-picker2" />
										</div>
										<div class="form-group">
											<label class="control-label">Loại :</label>
											<form:select path="type" class="form-control set-height-25">
												<form:option value="0">Tất cả</form:option>
												<form:option value="1">Tạo</form:option>
												<form:option value="2">Chỉnh sửa</form:option>
												<form:option value="3">Hủy</form:option>
											</form:select>
										</div>
										<div class="form-group">
											<label class="control-label">Tình trạng :</label>
											<form:select path="stt" class="form-control set-height-25">
												<form:option value="-1">Tất cả</form:option>
												<form:option value="1">Đã duyệt</form:option>
												<form:option value="0">Chưa duyệt</form:option>
											</form:select>
										</div>
										<div style="float: right;">
											<button type="submit" class="btn btn-default set-height-25">Lọc</button>
											<a id="cancel-filter" class="myClickableThingy"> <i
												class="fa fa-times fa-lg" aria-hidden="true"></i>
											</a>
										</div>
									</form:form>
								</div>
								<table cellpadding="0" cellspacing="0"
									class="table table-striped table-content"
									style="border-bottom: 1px solid #e3e3e3; margin-bottom: 0px;">
									<thead>
										<tr>
											<th style="width: 5%;">STT</th>
											<th style="width: 16%;">Tên</th>
											<th style="width: 20%;">Thời gian sử dụng</th>
											<th style="width: 10%;">Ngày tạo</th>
											<th style="width: 10%;">Loại</th>
											<th style="width: 10%;">Tình Trạng</th>
											<th style="width: 7%;" class="last-item-table">Chi tiết</th>
											<c:if test="${MODE == 'MODE_FIND_MY_PROPOSAL'}">
												<th style="width: 5%;" class="last-item-table">Sửa</th>
												<th style="width: 5%;" class="last-item-table">Hủy</th>
											</c:if>
											<c:if test="${MODE == 'MODE_FIND_PROPOSAL'}">
												<th style="width: 5%;" class="last-item-table">Xem</th>
											</c:if>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${listProposal}" var="list" varStatus="stt">
											<tr>
												<td>${stt.index + 1 }</td>
												<td><c:out value="${list.name}" /></td>
												<td><c:out value="${list.usefromdate}" /> đến <c:out value="${list.usetodate}" /></td>
												<td><c:out value="${list.userregister.dateregister}" /></td>
												<td><c:out value="${list.type.name}" /></td>
												<td><c:out value="${list.stt.name}"/> </td>
												<td class="last-item-table"><a class="myClickableThingy"
													data-toggle="tooltip" data-placement="right"
													data-html="true" title='<c:out value="${list.detail}"/>'>
														<i class="fa fa-info-circle fa-lg" aria-hidden="true"></i>
												</a></td>

												<c:if test="${MODE == 'MODE_FIND_MY_PROPOSAL'}">
													<td class="last-item-table">
														<a href="/change-proposal-${list.proposalID}"> 
															<i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>
														</a>
													</td>
													<td class="last-item-table">
														<c:if test="${ !list.expired && list.type.typeID != 3}">
															<a href="/cancel-proposal-${list.proposalID}"> 
																<i class="fa fa-trash-o fa-lg" aria-hidden="true"></i>
															</a>
														</c:if>
													</td>
												</c:if>
												<c:if test="${MODE == 'MODE_FIND_PROPOSAL'}">
													<th style="width: 5%;" class="last-item-table">
														<a href="/confirm-proposal-${list.proposalID}">
															<i class="fa fa-pencil" aria-hidden="true"></i>
														</a>
														
													</th>
												</c:if>

											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="row">
									<div class="col-sm-12">
										<nav class="pull-right">
										<ul class="pagination justify-content-end">
											<c:choose>
												<c:when test="${pagination.current != 1}">
													<li class="page-item"><a class="page-link"
													href="?page=${pagination.pre}" tabindex="-1">Previous</a></li>
												</c:when>
												<c:otherwise>
													<li class="page-item disabled"><a class="page-link"
													href="#" tabindex="-1">Previous</a></li>
												</c:otherwise>
											</c:choose>
											
											<li class="page-item"><a class="page-link" href="#">1</a></li>
											<li class="page-item"><a class="page-link" href="#">2</a></li>
											<li class="page-item"><a class="page-link" href="#">3</a></li>
											<li class="page-item"><a class="page-link" href="#">Next</a>
											</li>
										</ul>
										</nav>
									</div>

								</div>
							</c:when>
							<c:when test='${MODE == "MODE_CREATE_PROPOSAL" || MODE == "MODE_CHANGE_PROPOSAL"}'>
								<c:if test='${MODE == "MODE_CREATE_PROPOSAL"}'>
									<div class="title-content">Tạo Đề nghị</div>
								</c:if>
								<c:if test='${MODE == "MODE_CHANGE_PROPOSAL"}'>
									<div class="title-content">Chỉnh sữa đề nghị</div>
								</c:if>
								<form:form method="post" modelAttribute="Proposal"
									action="${typeForm}" class="form-horizontal"
									enctype="multipart/form-data">
									<c:if test="${not empty message}">
										<div class="form-group">
											<div style="color: red; font-size: 15px; text-align: center;">
												<c:out value="${message}" />
											</div>
										</div>
									</c:if>
									<c:if test="${not empty error}">
										<div class="form-group">
											<div style="color: red; font-size: 15px; text-align: center;">
												<c:out value="${error}" />
											</div>
										</div>
									</c:if>
									
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
										<label class="control-label col-sm-3">Nơi đến</label>
										<div class="col-sm-7">
											<form:input path="destination" type="text"
												class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="destination" />
											</div>
										</div>
										
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Địa điểm đón</label>
										<div class="col-sm-7">
											<form:input path="pickuplocation" type="text"
												class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="pickuplocation" />
											</div>
										</div>
										
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian đón</label>
										<div class="col-sm-7">
											<div class="input-group bootstrap-timepicker timepicker">
												<form:input path="pickuptime" id="pickuptime" type="text"
													class="form-control input-small" />
												<span class="input-group-addon"> <i
													class="glyphicon glyphicon-time"></i>
												</span>
											</div>
											<div class="has-error">
												<form:errors class="control-label" path="pickuptime" />
											</div>
										</div>
										
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Chi Tiết</label>
										<div class="col-sm-7">
											<form:textarea path="detail" id="detail" class="form-control"
												cols="30" rows="10"></form:textarea>
											<div class="has-error">
												<form:errors class="control-label" path="detail" />
											</div>
										</div>
										
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Xe</label>
										<div class="col-sm-7">
											<form:select id="choicecar" path="carID"
												class="btn btn-choose">
												<c:forEach items="${carsAvailble}" var="listcar">
													<form:option value="${listcar.carID}">
														<c:out value="${listcar.licenseplate}" /> -
														<c:out value="${listcar.type}" /> -
														<c:out value="${listcar.seats}" /> Chỗ
													</form:option>
												</c:forEach>
											</form:select>
											<i class="fa fa-info-circle fa-lg icon-show-detail myClickableThingy"
												aria-hidden="true"></i>
											<div class="has-error">
												<form:errors class="control-label" path="carID" />
											</div>
										</div>
										<div class="col-sm-offset-3 col-sm-7">
											<div >
												<c:forEach items="${carsAvailble}" var="listcar">
													<div class="col-sm-12 detail-car"
														id='detail-car-<c:out value="${listcar.carID}"/>'>
														<div class="col-sm-2">
															<img
																src='static/img/user/<c:out value="${listcar.driver.email}"/>.jpg'
																width="70px" height="80px">
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6">
																<p>
																	<strong>Tên :</strong>
																</p>
																<p>
																	<strong>Email :</strong>
																</p>
																<p>
																	<strong>Ngày sinh :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.name}" />
																	/p>
																<p>
																	<c:out value="${listcar.driver.email}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.birthday}" />
																</p>
															</div>
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6 padding-zero">
																<p>
																	<strong>SĐT :</strong>
																</p>
																<p>
																	<strong>Kinh nghiệm :</strong>
																</p>
																<p>
																	<strong>Bằng Lái :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.phone}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.experience}" />
																	năm
																</p>
																<p>
																	<c:out value="${listcar.driver.license}" />
																</p>
															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian sử
											dụng</label>
										<div class="col-sm-9">
											<div class="col-sm-2 padding-zero">
												<form:input path="usefromdate" id="usefromdate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input path="usefromtime" id="usefromtime" type="text"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
											<label class="control-label col-sm-1"
												style="text-align: center;">-</label>
											<div class="col-sm-2 padding-zero">
												<form:input id="usetodate" path="usetodate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input type="text" path="usetotime" id="usetotime"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
											<div class="col-sm-1">&nbsp;</div>
											<div class="has-error">
												<form:errors class="control-label" path="usefromdate" />
											</div>
											<div class="has-error">
												<form:errors class="control-label" path="usefromtime" />
											</div>
											<div class="has-error">
												<form:errors class="control-label" path="usetodate" />
											</div>
											<div class="has-error">
												<form:errors class="control-label" path="usetotime" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tệp đính kèm</label>
										<div class="col-sm-5">
											<form:input path="file" type="file" class="form-control" />
											<div class="has-error">
												<form:errors class="control-label" path="file" />
											</div>
										</div>
										<c:if
											test='${MODE == "MODE_CHANGE_PROPOSAL" && Proposal.fileexist == true}'>
											<a href="/static/file/${Proposal.proposalID}.pdf"
												class="control-label col-sm-3" style="text-align: left;"
												download>File</a>
										</c:if>
									</div>
									<div class="form-group">
										<c:if test='${MODE == "MODE_CREATE_PROPOSAL" }'>
											<div class="col-sm-offset-5 col-sm-3">
												<button type="submit" class="btn btn-default">Submit</button>
												<a href="/" class="btn btn-default">Trở về</a>
											</div>
										</c:if>
										<c:if test='${MODE == "MODE_CHANGE_PROPOSAL"}'>
											<div class="col-sm-offset-5 col-sm-4">
												<button type="submit" name="type" value="change"
													class="btn btn-default">Chỉnh sữa</button>
												<a href="/cancel-proposal" type="submit"
													class="btn btn-default">Hủy</a> <a href="/"
													class="btn btn-default">Trở về</a>
											</div>
										</c:if>
									</div>
								</form:form>
							</c:when>
							<c:when test='${MODE == "MODE_CONFIRM_PROPOSAL"}'>
								<div class="title-content">Xem đề nghị</div>
								<form:form method="post" modelAttribute="Proposal"
									action="${typeForm}" class="form-horizontal"
									enctype="multipart/form-data">
									<div class="form-group">
										<label class="control-label col-sm-3">Tên</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="name" type="text" class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Nơi đến</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="destination" type="text"
												class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Địa điểm đón</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="pickuplocation" type="text"
												class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian đón</label>
										<div class="col-sm-7">
											<div class="input-group bootstrap-timepicker timepicker">
												<form:input disabled="true" path="pickuptime" id="pickuptime" type="text"
													class="form-control input-small" />
												<span class="input-group-addon"> <i
													class="glyphicon glyphicon-time"></i>
												</span>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Chi Tiết</label>
										<div class="col-sm-7">
											<form:textarea disabled="true" path="detail" id="detail" class="form-control"
												cols="30" rows="10"></form:textarea>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Xe</label>
										<div class="col-sm-7">
											<form:select disabled="true" id="choicecar" path="carID"
												class="btn btn-choose">
												<c:forEach items="${carsAvailble}" var="listcar">
													<option value='<c:out value="${listcar.carID}"/>'>
														<c:out value="${listcar.licenseplate}" /> -
														<c:out value="${listcar.type}" /> -
														<c:out value="${listcar.seats}" /> Chỗ
													</option>
												</c:forEach>
											</form:select>
											<i class="fa fa-info-circle fa-lg icon-show-deital myClickableThingy"
												aria-hidden="true"></i>
										</div>
										<div class="col-sm-offset-3 col-sm-7">
											<div >
												<c:forEach items="${carsAvailble}" var="listcar">
													<div class="col-sm-12 detail-car"
														id='detail-car-<c:out value="${listcar.carID}"/>'>
														<div class="col-sm-2">
															<img
																src='static/img/user/<c:out value="${listcar.driver.email}"/>.jpg'
																width="70px" height="80px">
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6">
																<p>
																	<strong>Tên :</strong>
																</p>
																<p>
																	<strong>Email :</strong>
																</p>
																<p>
																	<strong>Ngày sinh :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.name}" />
																	/p>
																<p>
																	<c:out value="${listcar.driver.email}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.birthday}" />
																</p>
															</div>
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6 padding-zero">
																<p>
																	<strong>SĐT :</strong>
																</p>
																<p>
																	<strong>Kinh nghiệm :</strong>
																</p>
																<p>
																	<strong>Bằng Lái :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.phone}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.experience}" />
																	năm
																</p>
																<p>
																	<c:out value="${listcar.driver.license}" />
																</p>
															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian sử
											dụng</label>
										<div class="col-sm-9">
											<div class="col-sm-2 padding-zero">
												<form:input disabled="true" path="usefromdate" id="usefromdate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input disabled="true" path="usefromtime" id="usefromtime" type="text"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
											<label class="control-label col-sm-1"
												style="text-align: center;">-</label>
											<div class="col-sm-2 padding-zero">
												<form:input disabled="true" id="usetodate" path="usetodate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input disabled="true" type="text" path="usetotime" id="usetotime"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tệp đính kèm</label>
										<c:choose>
											<c:when test='${Proposal.fileexist == true}'>
												<a href="/static/file/${Proposal.proposalID}.pdf"
												class="control-label col-sm-3" style="text-align: left;"
												download>File</a>
											</c:when>
											<c:otherwise>
												<label class="control-label">Không có tệp đính kèm</label>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-4">
											<c:if test="${Proposal.stt.sttproposalID == 0}">
												<button type="submit" name="type" value="confirm"
												class="btn btn-default">Duyệt</button>
											</c:if>
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>
							</c:when>
							<c:when test="${MODE == 'MODE_PROPOSAL_EXPIRED'}">
								<form:form method="post" modelAttribute="Proposal"
									action="${typeForm}" class="form-horizontal"
									enctype="multipart/form-data">
									<c:if test="${not empty message}">
										<div class="form-group">
											<div style="color: red; font-size: 15px; text-align: center;">
												<c:out value="${message}" />
											</div>
										</div>
									</c:if>
									<div class="form-group">
										<label class="control-label col-sm-3">Tên</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="name" type="text" class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Nơi đến</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="destination" type="text"
												class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Địa điểm đón</label>
										<div class="col-sm-7">
											<form:input disabled="true" path="pickuplocation" type="text"
												class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian đón</label>
										<div class="col-sm-7">
											<div class="input-group bootstrap-timepicker timepicker">
												<form:input disabled="true" path="pickuptime" id="pickuptime" type="text"
													class="form-control input-small" />
												<span class="input-group-addon"> <i
													class="glyphicon glyphicon-time"></i>
												</span>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Chi Tiết</label>
										<div class="col-sm-7">
											<form:textarea disabled="true" path="detail" id="detail" class="form-control"
												cols="30" rows="10"></form:textarea>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Xe</label>
										<div class="col-sm-7">
											<form:select disabled="true" id="choicecar" path="carID"
												class="btn btn-choose">
												<c:forEach items="${carsAvailble}" var="listcar">
													<option value='<c:out value="${listcar.carID}"/>'>
														<c:out value="${listcar.licenseplate}" /> -
														<c:out value="${listcar.type}" /> -
														<c:out value="${listcar.seats}" /> Chỗ
													</option>
												</c:forEach>
											</form:select>
											<i class="fa fa-info-circle fa-lg icon-show-deital myClickableThingy"
												aria-hidden="true"></i>
										</div>
										<div class="col-sm-offset-3 col-sm-7">
											<div>
												<c:forEach items="${carsAvailble}" var="listcar">
													<div class="col-sm-12 detail-car"
														id='detail-car-<c:out value="${listcar.carID}"/>'>
														<div class="col-sm-2">
															<img
																src='static/img/user/<c:out value="${listcar.driver.email}"/>.jpg'
																width="70px" height="80px">
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6">
																<p>
																	<strong>Tên :</strong>
																</p>
																<p>
																	<strong>Email :</strong>
																</p>
																<p>
																	<strong>Ngày sinh :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.name}" />
																	/p>
																<p>
																	<c:out value="${listcar.driver.email}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.birthday}" />
																</p>
															</div>
														</div>
														<div class="col-sm-5 padding-zero">
															<div class="col-sm-6 padding-zero">
																<p>
																	<strong>SĐT :</strong>
																</p>
																<p>
																	<strong>Kinh nghiệm :</strong>
																</p>
																<p>
																	<strong>Bằng Lái :</strong>
																</p>
															</div>
															<div class="col-sm-6 padding-zero">
																<p>
																	<c:out value="${listcar.driver.phone}" />
																</p>
																<p>
																	<c:out value="${listcar.driver.experience}" />
																	năm
																</p>
																<p>
																	<c:out value="${listcar.driver.license}" />
																</p>
															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Thời gian sử
											dụng</label>
										<div class="col-sm-9">
											<div class="col-sm-2 padding-zero">
												<form:input disabled="true" path="usefromdate" id="usefromdate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input disabled="true" path="usefromtime" id="usefromtime" type="text"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
											<label class="control-label col-sm-1"
												style="text-align: center;">-</label>
											<div class="col-sm-2 padding-zero">
												<form:input disabled="true" id="usetodate" path="usetodate" type="text"
													class="form-control date-picker" />
											</div>
											<div class="col-sm-3 padding-right-zero">
												<div class="input-group bootstrap-timepicker timepicker">
													<form:input disabled="true" type="text" path="usetotime" id="usetotime"
														class="form-control input-small time2" />
													<span class="input-group-addon"> <i
														class="glyphicon glyphicon-time"></i>
													</span>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-sm-3">Tệp đính kèm</label>
										<c:choose>
											<c:when test='${Proposal.fileexist == true}'>
												<a href="/static/file/${Proposal.proposalID}.pdf"
												class="control-label col-sm-3" style="text-align: left;"
												download>File</a>
											</c:when>
											<c:otherwise>
												<label class="control-label">Không có tệp đính kèm</label>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-2">
											<a href="/" class="btn btn-default">Trở về</a>
										</div>
									</div>
								</form:form>
							</c:when>
							<c:when test="${MODE == 'MODE_SHOW_ALL_NOTIFY' }">
								<script type="text/javascript" src="static/js/loading-icon.js"></script>
								<style type="text/css">
									.list-notify .item-notify2 div {
										color: #364294;
										font-size: 14px;
									}
									
									.list-notify .item-notify2 p {
										font-size: 13px;
									}
									
									.list-notify a:hover {
										text-decoration: none;
									}
									.list-notify td {
										padding-bottom : 0px;
									}
								</style>
								<div class="title-content">
									<div class="row">Thông báo của bạn</div>
								</div>
								<table class="table table-hover">
									<tbody class="list-notify">
										<sec:authorize access="hasAuthority('CONFIRM_PROPOSAL')">
											<c:forEach items="${listNotify}" var="notifys">
												<tr>
													<td>
														<div class="item-notify2">
															<a href="confirm-proposal-${notifys.proposalID}">
																${notifys.message} ${notifys.time}
																</p>
															</a>
														</div>
													</td>
												</tr>
											</c:forEach>
										</sec:authorize>
										<sec:authorize access="hasAuthority('CHANGE_PROPOSAL')">
											<c:forEach items="${listNotify}" var="notifys">
												<tr>
													<td>
														<div class="item-notify2">
															<a href="change-proposal-${notifys.proposalID}">
																${notifys.message} ${notifys.time}
																</p>
															</a>
														</div>
													</td>
												</tr>
											</c:forEach>
										</sec:authorize>
									</tbody>
								</table>
							</c:when>
						</c:choose>
					</div>
				</div>
			<t:notifyandcalendar></t:notifyandcalendar>
		</div>
	</div>
	</div>
	<t:footer></t:footer>
	
	<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="static/js/bootstrap-timepicker.min.js"></script>
	<script type="text/javascript" src="static/js/bootstrap.min.js"></script>
	
	<script type="text/javascript" src="static/js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="static/js/validate.js"></script>
	<script type="text/javascript" src="static/js/main.js"></script>
</body>
</html>