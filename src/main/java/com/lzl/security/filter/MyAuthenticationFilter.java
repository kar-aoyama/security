package com.lzl.security.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author lzl
 * @ClassName MyAuthenticationFilter
 * @date: 2021/4/19 下午3:53
 * @Description:
 */
public class MyAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (token.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            token = StrUtil.trim(StrUtil.subAfter(token, AUTHORIZATION_HEADER_PREFIX, false));
        }
        token = StrUtil.isBlank(token) ? StrUtil.EMPTY : token;
        System.out.println(token);
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        System.out.println(attributeNames.hasMoreElements());
        Iterator<String> stringIterator = attributeNames.asIterator();
        while (stringIterator.hasNext()) {
            String name = stringIterator.next();
            Object value = session.getAttribute(name);
            System.out.println(name + " : " + value);
        }
        filterChain.doFilter(request, response);
    }


}
