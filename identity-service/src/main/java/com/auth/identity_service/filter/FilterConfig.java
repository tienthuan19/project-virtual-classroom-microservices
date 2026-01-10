package com.auth.identity_service.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    public FilterRegistrationBean<LogFilter> myFilter() {
        FilterRegistrationBean<LogFilter> regBean = new FilterRegistrationBean<>();
            
        regBean.setFilter(new LogFilter());
        regBean.addUrlPatterns("/*");
        regBean.setOrder(1);
            
        return regBean;    
    }
}

