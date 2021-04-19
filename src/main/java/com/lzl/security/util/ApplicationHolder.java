package com.lzl.security.util;

import com.lzl.security.annotation.WhetherLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lzl
 * @ClassName ApplicationHolder
 * @date: 2021/4/12 下午6:08
 * @Description:
 */
@Slf4j
@Component
public class ApplicationHolder implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    public static Set<String> getNoAuthorityUrls() {
        return doGetHandlerMappingUrls(false);
    }

    public static Set<String> getAuthorityUrls() {
        return doGetHandlerMappingUrls(true);
    }

    private static Set<String> doGetHandlerMappingUrls(boolean isLogin) {
        Map<String, RequestMappingHandlerMapping> handlerMappings = getHandlerMappings();
        Set<String> set = new HashSet<>();
        for (RequestMappingHandlerMapping handlerMapping : handlerMappings.values()) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                RequestMappingInfo requestMappingInfo = entry.getKey();
                HandlerMethod handlerMethod = entry.getValue();
                if (handlerMethod.hasMethodAnnotation(WhetherLogin.class)
                        || handlerMethod.getBeanType().isAnnotationPresent(WhetherLogin.class)) {
                    Method method = handlerMethod.getMethod();
                    WhetherLogin methodWhetherLogin = method.getAnnotation(WhetherLogin.class);

                    if (Objects.isNull(methodWhetherLogin)  && isLogin){
                        continue;
                    }

                    //没有注解按照无需权限分配
                    if (Objects.isNull(methodWhetherLogin)) {
                        set.addAll(requestMappingInfo.getPatternsCondition().getPatterns());
                        continue;
                    }

                    if (isLogin && methodWhetherLogin.login()) {
                        //获取需要权限url
                        Set<String> authoritySet = requestMappingInfo.getPatternsCondition().getPatterns();
                        set.addAll(authoritySet);
                        continue;
                    }

                    if (!isLogin && !methodWhetherLogin.login()) {
                        //获取无需权限url
                        Set<String> noAuthoritySet = requestMappingInfo.getPatternsCondition().getPatterns();
                        set.addAll(noAuthoritySet);
                    }
                }
            }
        }
        return set;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationHolder.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Map<String, RequestMappingHandlerMapping> getHandlerMappings() {
        return applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
    }
}
