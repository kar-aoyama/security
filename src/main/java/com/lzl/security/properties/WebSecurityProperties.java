package com.lzl.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lzl
 * @ClassName WebSecurityProperties
 * @date: 2021/4/12 下午5:57
 * @Description:
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "web.security")
public class WebSecurityProperties {

    private String logPage;

    private String logout;

    private String loginUrl;

    private String[] noAuthorityUrls;

    private String[] authorityUrls;

    private boolean isIgnoreOtherUrl;

    private String targetUrl;

    private String targetUrlParameter;
}
