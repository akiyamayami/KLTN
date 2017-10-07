package com.tlcn.dao;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.PasswordResetToken;
import com.tlcn.model.User;

public interface PasswordResetTokenRespository extends CrudRepository<PasswordResetToken, Long>{

	PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from passwordresettoken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
    
    @Modifying
    @Query("delete from passwordresettoken t where t.user = ?1")
    public void deleteAllTokenOfUser(User user);
}
