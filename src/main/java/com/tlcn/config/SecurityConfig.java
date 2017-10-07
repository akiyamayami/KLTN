package com.tlcn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.tlcn.security.CustomAccessDeniedHandler;
import com.tlcn.security.MyLogoutSuccessHandler;
import com.tlcn.security.MySimpleUrlAuthenticationSuccessHandler;
import com.tlcn.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private UserService userDetailsService;
	
	@Autowired
	private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }
	
	@Override
    protected void configure(final HttpSecurity http) throws Exception {
		http
		.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login","/change-password*","/accessDenied","/hackerDetected","/foget-password","/change-password").permitAll()
			.antMatchers("/find-cars","/check-stt-cars").hasAuthority("CHECK_OR_FIND_CARS")
			.antMatchers("/create-car","/create-car-x").hasAuthority("ADD_CAR")
			.antMatchers("/change-car*","/change-car/*/").hasAuthority("CHANGE_CAR")
			.antMatchers("/remove-car").hasAuthority("REMOVE_CAR")
			.antMatchers("/list-driver").hasAuthority("VIEW_LIST_DRIVER")
			.antMatchers("/create-driver").hasAuthority("ADD_DRIVER")
			.antMatchers("/change-driver*").hasAuthority("CHANGE_DRIVER")
			.antMatchers("/remove-driver","/remove-driver/**/").hasAuthority("REMOVE_DRIVER")
			.antMatchers("/list-user").hasAuthority("VIEW_LIST_USER")
			.antMatchers("/change-your-password","/edit-profile").hasAuthority("CHANGE_PROFILE")
			.antMatchers("/add-new-user").hasAuthority("ADD_USER")
			.antMatchers("/edit-user*").hasAuthority("CHANGE_USER")
			.antMatchers("/remove-user*").hasAuthority("REMOVE_USER")
			.antMatchers("/").hasAuthority("FIND_PROPOSAL")
			.antMatchers("/confirm-proposal").hasAuthority("CONFIRM_PROPOSAL")
			.antMatchers("/change-proposal").hasAuthority("CHANGE_PROPOSAL")
			.antMatchers("/create-proposal","/cancel-proposal").hasAuthority("CREATE_PROPOSAL")
			.antMatchers("/check-stt-cars","/find-cars").hasAuthority("CHECK_OR_FIND_CARS")
			.antMatchers("/update-password*").hasAuthority("CHANGE_PASSWORD")
			.antMatchers("/invalidSession*").anonymous()
		.and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/")
			.failureUrl("/login?error=true")
			.successHandler(mySimpleUrlAuthenticationSuccessHandler)
			.permitAll()
        .and()
		.logout()
			.logoutUrl("/j_spring_security_logout")
			.logoutSuccessUrl("/login?logSucc=true")
	        .invalidateHttpSession(false)
	        .deleteCookies("JSESSIONID")
	        .logoutSuccessHandler(myLogoutSuccessHandler)
        .permitAll()
        .and()
		.sessionManagement()
	        .invalidSessionUrl("/invalidSession")
	        .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
            .sessionFixation().none()
        .and()
        .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
	}
	
	
	@Bean
    public PasswordEncoder encoder() {
	        return new BCryptPasswordEncoder(11);
    }
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
	    return new CustomAccessDeniedHandler();
	}
	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
