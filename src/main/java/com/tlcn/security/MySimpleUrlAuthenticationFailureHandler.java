package com.tlcn.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		setDefaultFailureUrl("/login?error=true");
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		String targetUrl = "/login";
		if (response.isCommitted()) {
			System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
}
