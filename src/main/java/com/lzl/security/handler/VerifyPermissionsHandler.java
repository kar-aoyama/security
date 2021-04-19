package com.lzl.security.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lzl.security.entity.UserAuthorityEntity;
import com.lzl.security.enums.UrlType;
import com.lzl.security.service.UserAuthorityService;
import com.lzl.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author lzl
 * @ClassName VerifyPermissions
 * @date: 2021/4/14 下午4:14
 * @Description:
 */
@Component
public class VerifyPermissionsHandler {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserAuthorityService userAuthorityService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        return isNoAuthority(request) ? Boolean.TRUE : checkPermissions(request, authentication);
    }

    /**
     * 验证用户是否有权限
     *
     * @param request
     * @param authentication
     * @return
     */
    private boolean checkPermissions(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String userId = authentication.getName();
        List<UserAuthorityEntity> authorityList = userAuthorityService.findByAccountIdAndMethod(userId, method);
        if (CollUtil.isNotEmpty(authorityList)) {
            Optional<UserAuthorityEntity> optional = authorityList.stream()
                    .filter(Objects::nonNull)
                    .filter(
                            e -> antPathMatcher.match(e.getActionUrl(), requestURI)
                                    && StrUtil.equalsIgnoreCase(e.getMethod(), method)
                    )
                    .findAny();
            return optional.isPresent();
        }
        return false;
    }

    /**
     * 判断请求地址是否无需权限
     *
     * @param request
     * @return
     */
    private boolean isNoAuthority(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        BoundHashOperations<String, String, Set<String>> ops = getUrls(UrlType.NO_AUTHORITY);
        Set<String> noAuthrity = ops.get(UrlType.NO_AUTHORITY);
        if (CollUtil.isEmpty(noAuthrity)) {
            return false;
        }
        if (noAuthrity.contains(requestURI)) {
            return true;
        }
        return false;
    }

    public BoundHashOperations<String, String, Set<String>> getUrls(UrlType urlType) {
        return redisTemplate.boundHashOps(urlType.name());
    }

}
