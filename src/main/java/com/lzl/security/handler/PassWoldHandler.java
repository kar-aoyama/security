package com.lzl.security.handler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author lzl
 * @ClassName PassworldHandler
 * @date: 2021/4/15 上午10:17
 * @Description:
 */
public class PassWoldHandler implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString());
    }
}
