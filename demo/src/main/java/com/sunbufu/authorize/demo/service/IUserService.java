package com.sunbufu.authorize.demo.service;

import com.sunbufu.authorize.authorizecore.service.IAuthorizeService;
import com.sunbufu.authorize.demo.entity.User;

import javax.servlet.http.HttpSession;

/**
 * 用户管理
 *
 * @author sunbufu
 */
public interface IUserService extends IAuthorizeService {

    String ACCESS_SESSION_KEY = "USER";

    User logIn(String userName, String passWord, HttpSession session);

    User logOff(String userName, HttpSession session);

}
