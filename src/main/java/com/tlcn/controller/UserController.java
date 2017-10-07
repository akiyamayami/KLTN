package com.tlcn.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tlcn.dto.ModelCalendar;
import com.tlcn.dto.ModelPassword;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.dto.ModelUser;
import com.tlcn.error.UserNotFoundException;
import com.tlcn.model.Role;
import com.tlcn.model.User;
import com.tlcn.runnable.SendEmail;
import com.tlcn.service.NotifyEventService;
import com.tlcn.service.NotifyService;
import com.tlcn.service.RoleService;
import com.tlcn.service.UserSecurityService;
import com.tlcn.service.UserService;
import com.tlcn.validator.EditProfileValidator;
import com.tlcn.validator.ModelPasswordValidator;
import com.tlcn.validator.ModelUserValidator;

@Controller
@Transactional
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messages;
	 
	
	@Autowired
    private UserSecurityService userSecurityService;
    
	@Autowired
	private NotifyEventService notifyEventService;
	
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ModelPasswordValidator modelPasswordValidator;
	
	@Autowired
	private ModelUserValidator modelUserValidator;
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired 
	private EditProfileValidator editProfileValidator;
	public UserController() {
		super();
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
	public String viewUser(Model model, @PathVariable("emailUser") String email, HttpSession session){
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
	public String removeUserRedirect(Model model, HttpSession session, @PathVariable("emailUser") String emailUser) {
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
	public String EditUserPOST(Model model, @ModelAttribute("User") ModelUser modelUser
			, HttpSession session) {
		String email = (String) session.getAttribute("emailUser");
		User usernow = getUser();
		if(usernow.getEmail().equals(email)){
			return "redirect:/edit-profile";
		}
		User user = userService.findOne(email);
		System.out.println("roleid = " + modelUser.getRoleID());
		System.out.println("roleid = " + user.getRoleUser().getRoleID());
		Role role = roleService.findOne(modelUser.getRoleID());
		System.out.println(role != null);
		System.out.println(user.getRoleUser().getRoleID() != role.getRoleID());
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
		if(email == null || email == "" || token == "" || token == null){
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
		model.addAttribute("calendar", createCalendar(month,year));
	}
	public String createCalendar(String month, String year){
		System.out.println(month+"xx" + year);
		Calendar now = Calendar.getInstance();
		int y,m;
		if(month == null || year == null)
		{
			m = now.get(Calendar.MONTH);
			y = now.get(Calendar.YEAR);
			now.set(y, m, 1);
		}else{
			now.clear();
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
			now.set(y, m, 1);
		}
		String html = new ModelCalendar().createCalendar(y, m, now);
		return html;
	}
	public UserDetails getUserLogin() {
		return (UserDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	public User getUser(){
		return userService.findOne(getUserLogin().getUsername());
	}
	
	// check user has Authority
	public boolean checkUserhasAuthority(String Authority) {
		UserDetails x ;
		SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(Authority));
	}
}
