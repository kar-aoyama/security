package com.lzl.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lzl
 * @ClassName UserInfo
 * @date: 2021/4/12 下午5:24
 * @Description:
 */
@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "password")
    private String passWord;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "mobile_no")
    private String mobileNo;

}
