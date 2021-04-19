package com.lzl.security.repository;

import com.lzl.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author lzl
 * @ClassName UserReporits
 * @date: 2021/4/12 下午5:33
 * @Description:
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByAccountId(String accountId);
}
