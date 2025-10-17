package com.he181464.be_class.service;

import com.he181464.be_class.entity.Token;

public interface TokenService {

    Token saveRefreshToken(String username, String rToken);
}
