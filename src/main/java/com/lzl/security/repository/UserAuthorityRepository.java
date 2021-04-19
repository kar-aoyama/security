package com.lzl.security.repository;

import com.lzl.security.entity.UserAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author lzl
 * @ClassName UserAuthorityRepository
 * @date: 2021/4/19 下午2:29
 * @Description:
 */
@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, String> {

    Optional<UserAuthorityEntity> findByAccountIdAndActionUrl(String userId, String actionUrl);

    List<UserAuthorityEntity> findByAccountIdAndMethod(String userId, String method);
}
