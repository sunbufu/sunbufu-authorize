# sunbufu-authorize
=====================================


![](https://img.shields.io/badge/language-java-orange.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.sunbufu/authorize/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.sunbufu/authorize)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/author-sunbufu-blue.svg)](https://blog.csdn.net/sunbufu)

注解式权限认证

## 1 how to use

### 1.1 add dependency to pom.xml
```
<dependency>
    <groupId>com.github.sunbufu</groupId>
    <artifactId>authorize-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

### 1.2 implement `com.sunbufu.authorize.authorizecore.service.IAuthorizeService`
```
@Service
public class AuthorizeServiceImpl implements IAuthorizeService {

    public static final String USER_SESSION_KEY = "USER";

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
    public void setUsers() {
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
```

### 1.3 add requestMapping
```
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
```