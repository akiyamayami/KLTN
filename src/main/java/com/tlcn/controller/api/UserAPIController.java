package com.tlcn.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlcn.config.SecurityConfig;
import com.tlcn.dto.ModelLogin;
import com.tlcn.model.User;
import com.tlcn.service.UserService;

@RestController
public class UserAPIController {
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityConfig secConfig;
	
	public UserAPIController() {
		super();
	}
	
	@RequestMapping(value = "/api/v1/login", method = RequestMethod.POST)
	public ResponseEntity<String> LoginAPI(@RequestBody ModelLogin user)
	{
		System.out.println(secConfig.encoder().encode(user.getPassword()));
		return (userService.checkLogin(user)) ? new ResponseEntity<String>(HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	@RequestMapping(value = "/api/v1/test", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getSomeThing()
	{
		return "1";
	}
	
}
