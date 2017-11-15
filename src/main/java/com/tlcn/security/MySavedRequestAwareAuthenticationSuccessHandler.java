package com.tlcn.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
@Component
public class MySavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		handle(request,response,authentication);
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			customClearAuthentication(request);
			return;
		}
		String targetUrlParam = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParam != null && StringUtils.hasText(request.getParameter(targetUrlParam)))) {
			requestCache.removeRequest(request, response);
			customClearAuthentication(request);
			return;
		}

		clearAuthenticationAttributes(request);
	}
	public void customClearAuthentication(HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if(session != null)
		{
			session.setMaxInactiveInterval(30*60);
		}
	} 
	
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}
