package com.tlcn.service;

import java.util.Arrays;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tlcn.dao.PasswordResetTokenRespository;
import com.tlcn.model.PasswordResetToken;
import com.tlcn.model.User;

@Service
public class UserSecurityService {
	@Autowired
    private PasswordResetTokenRespository passwordResetTokenRespository;

    public String validatePasswordResetToken(String email, String token) {
        PasswordResetToken passToken = passwordResetTokenRespository.findByToken(token);
        if ((passToken == null) || (!passToken.getUser().getEmail().equals(email))) {
        	 System.out.println("invalidToken");
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
        	 System.out.println("expired");
            return "expired";
        }
        System.out.println("Give autho");
        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, 
        		null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD")));
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
        return null;
    }
}
