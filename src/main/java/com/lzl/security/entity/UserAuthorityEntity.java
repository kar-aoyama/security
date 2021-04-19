package com.lzl.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author lzl
 * @ClassName UserAuthorityEntity
 * @date: 2021/4/19 下午2:29
 * @Description:
 */
@Data
@Entity
@Table(name = "user_authority")
public class UserAuthorityEntity {

    @Id
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "method")
    private String method;

    @Column(name = "create_time")
    private Timestamp createTime;
}
