<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<header id="header">
    <div class="container">
        <div class="row">
            <div id="logo" class="col-sm-1">
                <img src="static/img/logo_spkt.jpg" height="98.7px" width="77.7px" />
            </div>
            <div id="name-ute" class="col-sm-5">
                <h5>TRƯỜNG ĐẠI HỌC</h5>
                <h4>SƯ PHẠM KỸ THUẬT TP. HỒ CHÍ MINH</h4>
                <h6>PHÒNG THIẾT BỊ - VẬT TƯ</h6>
            </div>
            <div id="search" class="col-sm-5">
                <div id="search-bar">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="Search for...">
                        <span class="input-group-btn">
        <button id="btn-submit" class="btn btn-primary glyphicon glyphicon-search"></button>
      </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="narbar" id="narbar">
        <div class="container">
            <div class="row">
            	<jsp:doBody/>
            </div>
        </div>
    </div>
</header>