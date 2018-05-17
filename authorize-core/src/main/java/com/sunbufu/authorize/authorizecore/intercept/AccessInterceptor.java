package com.sunbufu.authorize.authorizecore.intercept;

import com.sunbufu.authorize.authorizecore.ArrayUtil;
import com.sunbufu.authorize.authorizecore.annotation.Access;
import com.sunbufu.authorize.authorizecore.service.IAuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 *
 * @author sunbufu
 */
@Slf4j
@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IAuthorizeService authorizeService;

    @Autowired
    private ArrayUtil arrayUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //配置错误
        if (authorizeService == null) {
            log.warn("authorizeService bean 注入失败");
            return true;
        }
        // 获取注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            log.debug("handlerMethod = [{}]", handlerMethod);
            //获取controller权限注解
            Access controllerAccess = handlerMethod.getBean().getClass().getAnnotation(Access.class);
            //获取方法权限注解
            Access methodAccess = handlerMethod.getMethodAnnotation(Access.class);
            //合并权限
            String[] access = arrayUtil.concat(controllerAccess == null ? null : controllerAccess.value(), methodAccess == null ? null : methodAccess.value());
            log.debug("access = [{}]", access);
            //不需要权限
            if (methodAccess == null || methodAccess.value() == null || methodAccess.value().length <= 0) {
                return true;
            }
            //权限认证
            if (!authorizeService.authorize(methodAccess.value(), request.getSession())) {
                //权限认证失败处理
                authorizeService.authorizeFail(response);
            }
        } else {
            log.debug("handler = [{}]", handler);
        }
        return true;
    }

}