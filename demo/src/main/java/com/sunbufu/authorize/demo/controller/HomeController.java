package com.sunbufu.authorize.demo.controller;

import com.sunbufu.authorize.authorizecore.annotation.Access;
import com.sunbufu.authorize.demo.entity.User;
import com.sunbufu.authorize.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Access("test")
@RestController
public class HomeController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public String index() {
        return "this is index page";
    }

    @Access("manager")
    @RequestMapping("manage")
    public String manage() {
        return "this is manage page";
    }

    @RequestMapping("logIn")
    public String logIn(HttpSession session) {
        User user = userService.logIn("sunbufu", "1234", session);
        return user.getUserName() + " logIn success";
    }

    @RequestMapping("logOff")
    public String logOff(HttpSession session) {
        User user = userService.logOff("sunbufu", session);
        return user.getUserName() + " logOff success";
    }

    @RequestMapping("err")
    public String error(String msg) {
        return msg;
    }

}
