package com.example.authorizedemo;

import com.sunbufu.authorize.authorizecore.service.IAuthorizeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户管理
 *
 * @author sunbufu
 */
@Service
public class AuthorizeServiceImpl implements IAuthorizeService {

    private static final String USER_SESSION_KEY = "USER";

    @Data
    @AllArgsConstructor
    class User {
        String userName;
        String passWord;
        Set<String> access;
    }

    /**模拟数据库中的用户信息*/
    private Map<String, User> users;

    @PostConstruct
    public void initUsers() {
        users = new HashMap<>();
        Set<String> access = new HashSet<>();
        access.add("manager");
        User user = new User("sunbufu", "1234", access);
    }

    /**
     * 登录方法
     *
     * @param userName
     * @param passWord
     * @param session
     * @return
     */
    public User logIn(String userName, String passWord, HttpSession session) {
        User user = users.get(userName);
        if (!user.getPassWord().equals(passWord)) {
            return null;
        }
        session.setAttribute(USER_SESSION_KEY, user);
        return user;
    }

    @Override
    public boolean authorize(String[] access, HttpSession session) {
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        if (user != null && user.getAccess() != null && user.getAccess().isEmpty()) {
            for (String requestAccess : access) {
                if (user.getAccess().contains(requestAccess)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void authorizeFail(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("authorizeError?message=your account have not enought authority");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
