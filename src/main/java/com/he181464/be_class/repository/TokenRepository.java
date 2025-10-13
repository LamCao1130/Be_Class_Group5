package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);


}
