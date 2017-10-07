package com.tlcn.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dao.PasswordResetTokenRespository;
import com.tlcn.dao.UserRespository;
import com.tlcn.dto.ModelUser;
import com.tlcn.model.PasswordResetToken;
import com.tlcn.model.Right;
import com.tlcn.model.Role;
import com.tlcn.model.User;
import com.tlcn.runnable.SendEmail;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRespository userRespository;

	@Autowired
	private RoleService roleService;	
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private PasswordEncoder passworndEcoder;
	
	@Autowired
	private PasswordResetTokenRespository passwordResetTokenRespository;
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User userInfo = userRespository.findOne(username.toLowerCase());
		System.out.println(username);
        System.out.println("UserInfo= " + userInfo);
        
        if (userInfo == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }    
        List<Role> x = new ArrayList<Role>();
        x.add(userInfo.getRoleUser());
        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(userInfo.getEmail(),
                userInfo.getPassword(),getAuthorities(x));
		return userDetails;
	}
	
	private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }
	public void remove(User user){
		passwordResetTokenRespository.deleteAllTokenOfUser(user);
		userRespository.delete(user);
	}
	public boolean checkPasswodAndUpdate(User user, String newpassword){
		if(!passworndEcoder.encode(user.getPassword()).equals(passworndEcoder.encode(newpassword))){
			user.setPassword(passworndEcoder.encode(newpassword));
			userRespository.save(user);
			return true;
		}
		return false;
	}
	public List<User> getListBGMAndPTBVT(){
		return userRespository.getListBGMAndPTBVT();
	}
	public ModelUser converToShow(User user){
		return new ModelUser(user.getEmail(), user.getName(), user.getPhone(), user.getBirthday(), user.getRoleUser().getRoleID());
	}
	
	public void convertAndSave(ModelUser model, HttpServletRequest request){
		User user = new User(model.getEmail(), passworndEcoder.encode("S1mple@password"), model.getName(), model.getPhone(), model.getBirthday(), roleService.findOne(model.getRoleID()));
		String message = notifyService.genaratepassword(request, request.getLocale(), "S1mple@password", user);
		List<User> listuser = new ArrayList<>();
		listuser.add(user);
		Thread nThread = new Thread(new SendEmail(listuser,message,"New Password"));
		nThread.start();
//		notifyService.SendMailSTMP(listuser,message,"New Password");
		userRespository.save(user);
	}
	private final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<String>();
        final List<Right> collection = new ArrayList<Right>();
        for (final Role role : roles) {
            collection.addAll(role.getListRight());
        }
        for (final Right item : collection) {
            privileges.add(item.getName());
        }
        
        return privileges;
    }
	
	public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRespository.save(myToken);
    }
	
	private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
	
	public User findOne(String email){
		return userRespository.findOne(email);
	}
	
	public List<User> findAll(){
		List<User> users = new ArrayList<>();
		for(User user : userRespository.findAll()){
			users.add(user);
		}
		return users;
	}
	
	public void changeProfile(ModelUser model, User user, HttpServletRequest request){
		user.setBirthday(model.getBirthday());
		user.setName(model.getName());
		user.setBirthday(model.getBirthday());
		user.setPhone(model.getPhone());
		MultipartFile file = model.getFile();
		if(file != null && !file.isEmpty()){
			String location = request.getServletContext().getRealPath("static") + "/img/user/";
			String name = file.getOriginalFilename();
			String namefile = user.getEmail() + name.substring(name.lastIndexOf("."),name.length());
			uploadfile(file,location,namefile);
		}
		userRespository.save(user);
		
	}
	
	public void save(User user){
		userRespository.save(user);
	}
	public boolean uploadfile(MultipartFile file, String localtion, String namefile){
		try {
			byte[] bytes;
			bytes = file.getBytes();
			File serverFile = new File(localtion + namefile);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			return true;
		} catch (IOException e) {
			e.getMessage();
			return false;
		}
	}
	
	public UserDetails getUserLogin() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public User GetUser(){
		return userRespository.findOne(getUserLogin().getUsername());
	}
	
	// check user has Authority
	public boolean checkUserhasAuthority(String Authority) {
		System.out.println(getUserLogin().getUsername());
		return getUserLogin().getAuthorities().contains(new SimpleGrantedAuthority(Authority));
	}
}
