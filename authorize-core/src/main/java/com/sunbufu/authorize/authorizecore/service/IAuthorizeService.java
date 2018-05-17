package com.sunbufu.authorize.authorizecore.service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 权限处理
 *
 * @author sunbufu
 */
public interface IAuthorizeService {

    /**
     * 权限鉴定方法
     *
     * @param access  该方法需要的权限（Access注解的value，任意权限满足，即可通过）
     * @param session 该用户的session
     * @return
     */
    boolean authorize(String[] access, HttpSession session);

    /**
     * 鉴权失败时调用
     *
     * @param response
     */
    void authorizeFail(HttpServletResponse response);

}
