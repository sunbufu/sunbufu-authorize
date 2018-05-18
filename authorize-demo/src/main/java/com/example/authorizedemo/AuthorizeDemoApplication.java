package com.example.authorizedemo;

import com.sunbufu.authorize.authorizecore.annotation.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@SpringBootApplication
public class AuthorizeDemoApplication {

    @Autowired
    private AuthorizeServiceImpl authorizeService;

    /**
     * 测试权限
     *
     * @return
     */
    @Access("manage")
    @RequestMapping("index")
    public String index() {
        return "this is index page";
    }

    /**
     * 登录
     *
     * @param session
     * @return
     */
    @RequestMapping("logIn")
    public String logIn(HttpSession session) {
        AuthorizeServiceImpl.User user = authorizeService.logIn("sunbufu", "1234", session);
        if (user != null) {
            return "success userName=[" + user.userName + "]";
        } else {
            return "fail";
        }
    }

    /**
     * 权限不足
     *
     * @param message
     * @param response
     * @return
     */
    @RequestMapping("authorizeError")
    public String authorizeError(String message, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return message;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeDemoApplication.class, args);
    }

}
