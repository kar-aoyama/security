package com.lzl.security.config;

import com.lzl.security.handler.UserDetailHandler;
import com.lzl.security.properties.WebSecurityProperties;
import com.lzl.security.util.ApplicationHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lzl
 * @ClassName WebSecurityConfig
 * @date: 2021/4/12 下午5:18
 * @Description:
 */
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    WebSecurityProperties webSecurityProperties;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(createUserDetailHandler())
                .passwordEncoder(bCryptPasswordEncoder);
        auth.authenticationProvider(createAuthenticationProvider());
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/assets/**", "/*.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginUrl = webSecurityProperties.getLoginUrl();
        String logout = webSecurityProperties.getLogout();
        String logPage = webSecurityProperties.getLogPage();

        String[] noAuthorityUrls = webSecurityProperties.getNoAuthorityUrls();
        String[] authorityUrls = webSecurityProperties.getAuthorityUrls();

        Set<String> noAuthorityUrlSet = ApplicationHolder.getNoAuthorityUrls();
        Set<String> authorityUrlsSet = ApplicationHolder.getAuthorityUrls();
        noAuthorityUrlSet.add(loginUrl);
        noAuthorityUrlSet.add(logPage);
        noAuthorityUrlSet.addAll(Stream.of(noAuthorityUrls).collect(Collectors.toSet()));
        authorityUrlsSet.add(logout);
        authorityUrlsSet.addAll(Stream.of(authorityUrls).collect(Collectors.toSet()));
        http
                .authorizeRequests()
                .antMatchers(authorityUrlsSet.toArray(String[]::new))
                .authenticated()
                .antMatchers(noAuthorityUrlSet.toArray(String[]::new))
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(logPage)
                .and()
                .cors()
                .disable()
                .sessionManagement()
                .maximumSessions(1);
    }

    @Bean("userDetailHandler")
    public UserDetailHandler createUserDetailHandler() {
        return new UserDetailHandler();
    }

    @Bean(name = "daoAuthProvider")
    public DaoAuthenticationProvider createAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(createUserDetailHandler());
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder createBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
