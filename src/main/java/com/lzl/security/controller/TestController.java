package com.lzl.security.controller;

import com.lzl.security.annotation.WhetherLogin;
import com.lzl.security.entity.UserEntity;
import com.lzl.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzl
 * @ClassName TestController
 * @date: 2021/4/13 下午3:42
 * @Description:
 */
@RestController
@WhetherLogin(login = true)
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @WhetherLogin(login = true)
    @GetMapping("aaa")
    public String aaa() {
        return "error";
    }

    @PostMapping("aaa")
    public String abc() {
        return "ac";
    }

    @WhetherLogin
    @GetMapping("bbb")
    public String bbb() {
        return "success";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserEntity usersEntity) {
        String passWord = usersEntity.getPassWord();
        String encodePassWord = bCryptPasswordEncoder.encode(passWord);
        usersEntity.setPassWord(encodePassWord);
        userService.saveUser(usersEntity);
        return "success";
    }

    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);
    }
}
