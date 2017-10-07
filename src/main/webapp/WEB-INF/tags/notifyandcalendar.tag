<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="col-sm-3">
	<div class="block">
		<div class="content">
			<div class="minicalendar">
				<div class="title-calendar">Calendar</div>
				${calendar}
			</div>
		</div>
	</div>
	<c:if test="${MODE != 'MODE_SHOW_ALL_NOTIFY'}">
		<div class="block">
			<div class="mininotify">
				<div class="title-notify">Notification</div>
				<div class="list-notify">
					<c:forEach items="${listNotify}" var="notifys">
						<div class="item-notify">
							<sec:authorize access="hasAuthority('CONFIRM_PROPOSAL')">
								<a href="confirm-proposal-${notifys.proposalID}">
									${notifys.message} ${notifys.time}
									</p>
								</a>
							</sec:authorize>
							<sec:authorize access="hasAuthority('CHANGE_PROPOSAL')">
								<a href="change-proposal-${notifys.proposalID}">
									${notifys.message} ${notifys.time}
									</p>
								</a>
							</sec:authorize>
						</div>
					</c:forEach>
				</div>
				<a href="/show-all-notify">
					<div class="show-all-notify">Xem tất cả</div>
				</a>
			</div>
		</div>
	</c:if>
</div>