package com.sunbufu.authorize.authorizecore.intercept;

import com.sunbufu.authorize.authorizecore.util.ArrayUtil;
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
public class AccessInterceptor implements HandlerInterceptor {

    private IAuthorizeService authorizeService;

    private ArrayUtil arrayUtil;

    public AccessInterceptor(IAuthorizeService authorizeService, ArrayUtil arrayUtil) {
        this.authorizeService = authorizeService;
        this.arrayUtil = arrayUtil;
    }

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
            if (access == null || access.length <= 0) {
                return true;
            }
            //权限认证
            if (!authorizeService.authorize(access, request.getSession())) {
                //权限认证失败处理
                authorizeService.authorizeFail(request, response);
            } else {
                authorizeService.authorizeSuccess(request, response);
            }
        } else {
            log.debug("handler = [{}]", handler);
        }
        return true;
    }

}
