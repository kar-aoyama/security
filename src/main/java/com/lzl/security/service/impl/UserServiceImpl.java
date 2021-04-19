package com.lzl.security.service.impl;

import com.lzl.security.entity.UserEntity;
import com.lzl.security.repository.UserRepository;
import com.lzl.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lzl
 * @ClassName UserviceImpl
 * @date: 2021/4/14 下午4:49
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
