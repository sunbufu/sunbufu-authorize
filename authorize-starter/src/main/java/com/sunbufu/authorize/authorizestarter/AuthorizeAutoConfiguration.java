package com.sunbufu.authorize.authorizestarter;

import com.sunbufu.authorize.authorizecore.intercept.AccessInterceptor;
import com.sunbufu.authorize.authorizecore.service.IAuthorizeService;
import com.sunbufu.authorize.authorizecore.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自动配置拦截器
 *
 * @author sunbufu
 */
@Configuration
public class AuthorizeAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private IAuthorizeService authorizeService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor());
    }

    public ArrayUtil arrayUtil() {
        return new ArrayUtil();
    }

    public AccessInterceptor accessInterceptor() {
        return new AccessInterceptor(authorizeService, arrayUtil());
    }

}
