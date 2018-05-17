package com.sunbufu.authorize.demo.service.impl;

import com.sunbufu.authorize.demo.entity.User;
import com.sunbufu.authorize.demo.service.IUserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户管理服务
 *
 * @author sunbufu
 */
@Service("authorizeService")
public class UserService implements IUserService {

    private HashMap<String, User> users = new HashMap();

    public UserService() {
        Set<String> access = new HashSet<>();
        access.add("manager");
        access.add("admin");
        users.put("sunbufu", new User("sunbufu", "1234", access));
    }

    @Override
    public boolean authorize(String[] access, HttpSession session) {
        User user = (User) session.getAttribute(ACCESS_SESSION_KEY);
        if (user != null && user.getAccess() != null && !user.getAccess().isEmpty()) {
            for (String a : access) {
                if (user.getAccess().contains(a)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void authorizeFail(HttpServletResponse response) {
        try {
            response.sendRedirect("err?msg=your account have not enought authority");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User logIn(String userName, String passWord, HttpSession session) {
        User user = users.get(userName);
        session.setAttribute(ACCESS_SESSION_KEY, user);
        return user;
    }

    @Override
    public User logOff(String userName, HttpSession session) {
        User user = (User) session.getAttribute(ACCESS_SESSION_KEY);
        session.removeAttribute(ACCESS_SESSION_KEY);
        return user;
    }
}
