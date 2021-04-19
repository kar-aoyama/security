package com.lzl.security.service;

import com.lzl.security.entity.UserAuthorityEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author lzl
 * @ClassName UserAuthoritySetvice
 * @date: 2021/4/19 下午2:34
 * @Description:
 */
public interface UserAuthorityService {

    UserAuthorityEntity findByAccountIdAndActionUrl(String userId, String actionUrl);

    List<UserAuthorityEntity> findByAccountIdAndMethod(String userId,String method);
}
