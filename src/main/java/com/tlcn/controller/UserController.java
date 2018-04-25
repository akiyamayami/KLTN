package com.tlcn.controller;

import com.tlcn.dto.ModelPassword;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.dto.ModelUser;
import com.tlcn.error.UserNotFoundException;
import com.tlcn.model.Role;
import com.tlcn.model.User;
import com.tlcn.runnable.SendEmail;
import com.tlcn.service.*;
import com.tlcn.validator.EditProfileValidator;
import com.tlcn.validator.ModelPasswordValidator;
import com.tlcn.validator.ModelUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@Transactional
public class UserController {
	
	private final UserService userService;

    @Qualifier("messageSource")
	private MessageSource messages;

	private final CalendarService calendarService;
	 
	
	private final UserSecurityService userSecurityService;
    
	private final NotifyEventService notifyEventService;
	
	
	private final RoleService roleService;
	
	private final ModelPasswordValidator modelPasswordValidator;
	
	private final ModelUserValidator modelUserValidator;
	
	private final NotifyService notifyService;

	private final EditProfileValidator editProfileValidator;

	@Autowired
    public UserController(UserService userService, CalendarService calendarService, UserSecurityService userSecurityService, NotifyEventService notifyEventService, RoleService roleService, ModelPasswordValidator modelPasswordValidator, ModelUserValidator modelUserValidator, NotifyService notifyService, EditProfileValidator editProfileValidator) {
		super();
        this.userService = userService;
        this.calendarService = calendarService;
        this.userSecurityService = userSecurityService;
        this.notifyEventService = notifyEventService;
        this.roleService = roleService;
        this.modelPasswordValidator = modelPasswordValidator;
        this.modelUserValidator = modelUserValidator;
        this.notifyService = notifyService;
        this.editProfileValidator = editProfileValidator;
    }

	@RequestMapping(value="/list-user", method = RequestMethod.GET)
	public String manageUser(Model model) {
		List<User> listUser = userService.findAll();
		listUser.remove(getUser());
		model.addAttribute("MODE", "MODE_MANAGE_USER");
		model.addAttribute("listuser", listUser);
		showCalendarAndNotify(model,null,null);
		return "userManager";
	}
	
	
	
	
	@RequestMapping(value="/edit-profile", method = RequestMethod.GET)
	public String EditProfile(Model model) {
		User user = userService.findOne(getUser().getEmail());
		model.addAttribute("MODE", "MODE_EDIT_PROFILE");
		model.addAttribute("User", userService.converToShow(user));
		model.addAttribute("role", roleService.findOne(user.getRoleUser().getRoleID()));
		showCalendarAndNotify(model,null,null);
		return "userManager";
	}
	
	@RequestMapping(value="/edit-profile", method = RequestMethod.POST)
	public String EditProfilePOST(Model model,@Valid @ModelAttribute("User") ModelUser modelUser,
			BindingResult result, HttpServletRequest request) {
		System.out.println("email user validate is" + modelUser.getEmail());
		editProfileValidator.validate(modelUser, result);
		User user = userService.findOne(getUser().getEmail());
		if(result.hasErrors()){
			model.addAttribute("MODE", "MODE_EDIT_PROFILE");
			model.addAttribute("User", modelUser);
			model.addAttribute("role", roleService.findOne(user.getRoleUser().getRoleID()));
			showCalendarAndNotify(model,null,null);
			return "userManager";
		}
		userService.changeProfile(modelUser, user, request);
		return "redirect:/";
	}
	
	@RequestMapping(value="/change-your-password", method = RequestMethod.GET)
	public String changePasswordUser(Model model) {
		model.addAttribute("MODE", "MODE_CHANGE_PASSWORD");
		model.addAttribute("ModelPassword", new ModelPassword());
		showCalendarAndNotify(model,null,null);
		return "userManager";
	}
	
	@RequestMapping(value="/change-your-password", method = RequestMethod.POST)
	public String changePasswordUserPOST(Model model,@Valid @ModelAttribute("ModelPassword") ModelPassword password,
    		BindingResult result) {
		modelPasswordValidator.validate(password, result);
		if(result.hasErrors()){
			model.addAttribute("MODE", "MODE_CHANGE_PASSWORD");
			model.addAttribute("ModelPassword", password);
			showCalendarAndNotify(model,null,null);
			return "userManager";
		}
		User user = userService.findOne(getUser().getEmail());
		userService.checkPasswodAndUpdate(user, password.getPassword());
		return "redirect:/";
	}
	@RequestMapping(value="/edit-user/{emailUser}/", method = RequestMethod.GET)
	public String viewUser(@PathVariable("emailUser") String email, HttpSession session){
		User user = userService.findOne(email);
		if(user == null){
			throw new UserNotFoundException();
		}
		if(getUser().getEmail().equals(email)){
			return "redirect:/edit-profile";
		}
		session.setAttribute("emailUser", email);
		return "redirect:/edit-user";
	}
	
	
	@RequestMapping(value="/edit-user", method = RequestMethod.GET)
	public String EditUser(Model model, HttpSession session) {
		String email = (String) session.getAttribute("emailUser");
		User user = userService.findOne(email);
		model.addAttribute("MODE", "MODE_CHANGE_USER");
		model.addAttribute("User", userService.converToShow(user));
		List<Role> roles = new ArrayList<>();
		if(checkUserhasAuthority("CHANGE_USER")){
			roles = roleService.findAll();
			model.addAttribute("listrole", roles);
		}else{
			roles.add(user.getRoleUser());
			model.addAttribute("listrole", roles);
		}
		showCalendarAndNotify(model,null,null);
		return "userManager";
	}
	@RequestMapping(value="/remove-user/{emailUser}/", method = RequestMethod.GET)
	public String removeUserRedirect(HttpSession session, @PathVariable("emailUser") String emailUser) {
		session.setAttribute("emailUserDelete", emailUser);
		return "redirect:/remove-user";
	}
	@RequestMapping(value="/remove-user", method = RequestMethod.GET)
	public String removeUser(Model model, HttpSession session) {
		String email = (String) session.getAttribute("emailUserDelete");
		User user = userService.findOne(email);
		if(getUser().getEmail().equals(email))
		{
			model.addAttribute("messageEro", "You cant delete yoursefl");
			List<User> listUser = userService.findAll();
			listUser.remove(getUser());
			model.addAttribute("MODE", "MODE_MANAGE_USER");
			model.addAttribute("listuser", listUser);
			showCalendarAndNotify(model,null,null);
			return "userManager";
		}
		userService.remove(user);
		return "redirect:/list-user";
	}
	
	@RequestMapping(value="/edit-user", method = RequestMethod.POST)
	public String EditUserPOST(@ModelAttribute("User") ModelUser modelUser
            , HttpSession session) {
		String email = (String) session.getAttribute("emailUser");
		User usernow = getUser();
		if(usernow.getEmail().equals(email)){
			return "redirect:/edit-profile";
		}
		User user = userService.findOne(email);
		Role role = roleService.findOne(modelUser.getRoleID());
		if(role != null && user.getRoleUser().getRoleID() != modelUser.getRoleID())
		{	System.out.println("set role");
			user.setRoleUser(role);
			userService.save(user);
		}
		return "redirect:/list-user";
	}
	
	@RequestMapping(value="/add-new-user", method = RequestMethod.GET)
	public String addUser(Model model) {
		model.addAttribute("MODE", "MODE_ADD_USER");
		model.addAttribute("User", new ModelUser());
		model.addAttribute("listrole", roleService.findAll());
		showCalendarAndNotify(model,null,null);
		return "userManager";
	}
	
	@RequestMapping(value="/add-new-user", method = RequestMethod.POST)
	public String addUserPOST(Model model,@Valid @ModelAttribute("User") ModelUser modelUser,BindingResult result,
			HttpServletRequest request) {
		modelUserValidator.validate(modelUser, result);
		if(result.hasErrors()){
			model.addAttribute("MODE", "MODE_ADD_USER");
			model.addAttribute("User", modelUser);
			model.addAttribute("listrole", roleService.findAll());
			showCalendarAndNotify(model,null,null);
			return "userManager";
		}
		
		userService.convertAndSave(modelUser, request);
		return "redirect:/list-user";
	}
	
	
	
	@RequestMapping(value="/foget-password", method = RequestMethod.GET)
	public String forgetPassword(Model model) {
		model.addAttribute("MODE", "FOGET_PASSWORD");
		return "resetPassword";
	}
	
	@RequestMapping(value="/foget-password", method = RequestMethod.POST)
	public String forgetPasswordPOST(HttpServletRequest request,Model model, 
			@RequestParam("email") String userEmail) {
		User user = userService.findOne(userEmail);
		if(user == null){
			throw new UserNotFoundException();
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		String message = notifyService.genarateMessageResetTokenEmail(request, 
		request.getLocale(), token, user);
		List<User> listuser = new ArrayList<>();
		listuser.add(user);
		Thread nThread = new Thread(new SendEmail(listuser,message,"Reset Password"));
		nThread.start();
		model.addAttribute("messages", messages.getMessage("message.resetPasswordEmail", null, Locale.ENGLISH));
		return "Login";
	}
	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public String showChangePasswordPage(Model model, @RequestParam("email") String email, 
    		@RequestParam("token") String token){
		if(StringUtils.isEmpty(email) || StringUtils.isEmpty(token)){
			return "redirect:/login";
		}
		System.out.println("email = " + email + ", token = " + token);
		String result = userSecurityService.validatePasswordResetToken(email, token);
		System.out.println(result);
		if (result != null) {
            model.addAttribute("message", 
            		messages.getMessage("auth.message." + result, null, Locale.ENGLISH));
            return "redirect:/login";
        }
		return "redirect:/update-password";
	}
	
	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
    public String updatePassword(Model model){
		model.addAttribute("MODE", "CHANGE_PASSWORD");
		model.addAttribute("ModelPassword", new ModelPassword());
		return "resetPassword";
	}
	
	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
    public String updatePasswordPOST(Model model,
    		@Valid @ModelAttribute("ModelPassword") ModelPassword password,
    		BindingResult result){
		modelPasswordValidator.validate(password, result);
		if(result.hasErrors()){
			model.addAttribute("MODE", "CHANGE_PASSWORD");
			model.addAttribute("ModelPassword", password);
			return "resetPassword";
		}
		if(!userService.checkPasswodAndUpdate((User)  
				SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal(), password.getPassword()))
		{
			// show erros old and new passoword equal
			model.addAttribute("MODE", "CHANGE_PASSWORD");
			model.addAttribute("ModelPassword", password);
			return "resetPassword";
		}
		// remove permission change password of user
		SecurityContextHolder.getContext().setAuthentication(null);
		model.addAttribute("messages", "Update password success");
		return "Login";
	}
	public void showCalendarAndNotify(Model model, String month, String year){
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(getUser());
		if(listNotify != null && listNotify.size() < 5)
			model.addAttribute("listNotify",listNotify);
		else
			model.addAttribute("listNotify", notifyEventService.getListNotifyNewest(getUser()).subList(0, 5));
		model.addAttribute("calendar", calendarService.createCalendar(month,year));
	}
	public UserDetails getUserLogin() {
		return (UserDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	public User getUser(){
		return userService.findOne(getUserLogin().getUsername());
	}
	
	// check user has Authority
	public boolean checkUserhasAuthority(String Authority) {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(Authority));
	}
}
