package com.lzl.security.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Collection;

/**
 * @author lzl
 * @ClassName UserInfo
 * @date: 2021/4/15 上午10:55
 * @Description:
 */
@Getter
public class UserInfo extends User implements Principal {

    private String id;

    private String accountId;

    private String password;

    private String userName;

    private String mobileNo;


    public UserInfo(
            String id,
            String accountId,
            String password,
            String mobileNo) {
        super(accountId, password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.id = id;
        this.accountId = accountId;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    @Override
    public String getName() {
        return this.accountId;
    }
}
