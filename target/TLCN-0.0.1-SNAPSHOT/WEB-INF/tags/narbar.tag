<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="col-sm-8">
	<div class="dropdown col-sm-6 padding-zero">
		<button class="btn-dropdown-page" style="float:left;" OnClick=" location.href='/'">Trang
			chủ</button>
		<div class="dropdown col-sm-2 padding-zero">
			<button class="btn-dropdown-page dropdown-toggle" type="button"
				data-toggle="dropdown">
				Xe <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="/check-stt-cars">Kiểm tra</a></li>
				<li><a href="/find-cars">Tra cứu</a></li>
				<sec:authorize access="hasAuthority('ADD_CAR')">
					<li><a href="/create-car">Thêm xe mới</a></li>
				</sec:authorize>
			</ul>
		</div>
		<sec:authorize access="hasAuthority('VIEW_LIST_DRIVER')">
		<div class="dropdown col-sm-3 padding-zero">
			<button class="btn-dropdown-page dropdown-toggle" type="button"
				data-toggle="dropdown">
				Tài xế <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="/list-driver">Danh sách tài xế</a></li>
				<sec:authorize access="hasAuthority('ADD_DRIVER')">
					<li><a href="/create-driver">Thêm tài xế</a></li>
				</sec:authorize>
			</ul>
		</div>
		</sec:authorize>
		<sec:authorize access="hasAuthority('VIEW_LIST_USER')">
		<div class="dropdown col-sm-3 padding-zero">
			<button class="btn-dropdown-page dropdown-toggle" type="button"
				data-toggle="dropdown">
				User <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="/list-user">Danh sách user</a></li>
				<sec:authorize access="hasAuthority('ADD_USER')">
					<li><a href="/add-new-user">Thêm user</a></li>
				</sec:authorize>
			</ul>
		</div>
		</sec:authorize>
	</div>
</div>
<div class="col-sm-4">
	<div class="info-user">
		<button class="btn-dropdown">
			<span class="info"><img src="static/img/user/<sec:authentication property="principal.username" />.jpg"
				height="27px" width="23px" /></span> <span class="info"><sec:authentication property="principal.username" /></span>
			<span class="caret info"></span>
		</button>
		<div class="dropdown-content">
			<a href="/edit-profile">Thay đổi thông tin</a>
			<a href="/change-your-password">Đổi mật khẩu</a>
			<a href="/j_spring_security_logout">Đăng Xuất</a>
		</div>
	</div>
</div>