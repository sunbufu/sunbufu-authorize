# sunbufu-authorize
=====================================


![](https://img.shields.io/badge/language-java-orange.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.sunbufu/authorize/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.sunbufu/authorize)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/author-sunbufu-blue.svg)](https://blog.csdn.net/sunbufu)

注解式权限管理框架

[详细说明](https://blog.csdn.net/sunbufu/article/details/80379844)

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
implement authorize method
```
@Override
public boolean authorize(String[] access, HttpSession session) {
    //do your authorize logic
}
```
implement authorizeFail method
```
@Override
public void authorizeFail(HttpServletRequest request, HttpServletResponse response) {
    //do something
}
```

### 1.3 add `@Access` to requestMapping
```
@Access("manage")
@RequestMapping("index")
public String index() {
    return "this is index page";
}
```