package com.lzl.security.handler;


import com.lzl.security.entity.UserInfo;
import com.lzl.security.entity.UserEntity;
import com.lzl.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * @author lzl
 * @ClassName UserDetailHandler
 * @date: 2021/4/12 下午5:22
 * @Description:
 */
@Slf4j
public class UserDetailHandler implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserInfo(username);
    }

    private UserInfo getUserInfo(String accountId) {
        Optional<UserEntity> optional = userRepository.findByAccountId(accountId);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("账号不存在");
        }
        return paddingField(optional.get());
    }

    private UserInfo paddingField(UserEntity userEntity) {
        return new UserInfo(userEntity.getId(), userEntity.getAccountId(),
                userEntity.getPassWord(), userEntity.getMobileNo());
    }
}
