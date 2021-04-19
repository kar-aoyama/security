package com.lzl.security.service.impl;

import com.lzl.security.entity.UserAuthorityEntity;
import com.lzl.security.repository.UserAuthorityRepository;
import com.lzl.security.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author lzl
 * @ClassName UserAuthorityServiceImpl
 * @date: 2021/4/19 下午2:36
 * @Description:
 */
@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    UserAuthorityRepository userAuthorityRepository;


    @Override
    public UserAuthorityEntity findByAccountIdAndActionUrl(String userId, String actionUrl) {
        Optional<UserAuthorityEntity> optional = userAuthorityRepository.findByAccountIdAndActionUrl(userId, actionUrl);
        return optional.orElse(null);
    }

    @Override
    public List<UserAuthorityEntity> findByAccountIdAndMethod(String userId, String method) {
        return userAuthorityRepository.findByAccountIdAndMethod(userId,method);
    }
}
