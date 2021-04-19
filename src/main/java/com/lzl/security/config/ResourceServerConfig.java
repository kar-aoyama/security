package com.lzl.security.config;

import com.lzl.security.common.GlobalException;
import com.lzl.security.filter.MyAuthenticationFilter;
import com.lzl.security.handler.ExpressionHandler;
import com.lzl.security.response.CommonResponse;
import com.lzl.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.logout.LogoutFilter;


/**
 * @author lzl
 * @ClassName ResourceServerConfig
 * @date: 2021/4/14 下午2:53
 * @Description:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    private RemoteTokenServices remoteTokenServices;

    @Autowired
    ExpressionHandler expressionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("oauth2");
        resources.authenticationEntryPoint(
                //处理未登录或者token有问题的请求
                (request, response, authException) -> response.sendRedirect("/login.html")
        ).accessDeniedHandler((request, response, accessDeniedException) -> {
            //处理权限不够的请求
            GlobalException exception =
                    GlobalException.newInstance("API_ACCESS_DENIED", "用户没有权限访问");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream()
                    .write(
                            CommonResponse.exceptionInstance(exception)
                                    .toJson()
                                    .getBytes());
            //注入表达式对象
        }).expressionHandler(expressionHandler);
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest()
                .access("#verifyPermissions.hasPermission(request,authentication)")
                .and()
                .csrf()
                .disable()
                .cors()
                .disable();

        http.addFilterBefore(new MyAuthenticationFilter(), LogoutFilter.class);
    }
}
