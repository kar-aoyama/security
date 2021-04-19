package com.lzl.security.config.authorization;

import cn.hutool.core.lang.Assert;
import com.lzl.security.handler.UserDetailHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * @author lzl
 * @ClassName AuthorizationConfig
 * @date: 2021/4/14 上午11:19
 * @Description:
 */
@Slf4j
@Configuration
//启用授权服务器
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    UserDetailHandler userDetailHandler;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //允许验证资源服务器访问公钥端点（/oauth/token_key）
                //允许验证用户访问令牌解析端点（/oauth/check_token）
                .tokenKeyAccess("isAuthenticated()").checkTokenAccess("isAuthenticated()")
                // 允许客户端发送表单来进行权限认证来获取令牌
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //持久化到数据库
        clients.withClientDetails(clientDetails());
       /* clients.jdbc(dataSource);
        clients.inMemory()
                .withClient("order-client")
                .secret(passwordEncoder.encode("order-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)                .scopes("all")
                .and()
                .withClient("user-client")
                .secret(passwordEncoder.encode("user-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");*/

    }

    //调用方法也只会调用一次
    @Bean
    public ClientDetailsService clientDetails() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    //jks 文件生成 https://blog.csdn.net/tonydz0523/article/details/106676135
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //配置认证管理器
                .authenticationManager(authenticationManager)
                //定义异常转换返回处理
                .exceptionTranslator(e -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))
                //设置用户验证服务
                .userDetailsService(userDetailHandler)
                //指定 token 的存储方式
                .tokenStore(jwtTokenStore())
                //token转换方式
                .accessTokenConverter(jwtAccessTokenConverter());
        DefaultTokenServices tokenServices
                = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("lzl-jwt.jks"),
                "lzl0502".toCharArray())
                .getKeyPair("lzl-jwt");
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

  /*  @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        Assert.state(dataSource != null, "DataSource must be provided");
        return new JdbcAuthorizationCodeServices(dataSource);
    }*/
}
