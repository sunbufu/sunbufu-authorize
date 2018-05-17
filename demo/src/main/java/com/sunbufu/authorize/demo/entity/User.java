package com.sunbufu.authorize.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private String userName;
    private String passWord;

    private Set<String> access;

}
