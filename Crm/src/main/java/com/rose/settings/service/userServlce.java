package com.rose.settings.service;

import com.rose.exception.LoginExption;
import com.rose.settings.domain.user;

import java.util.List;

public interface userServlce {
    user seleuser(String name,String pwd,String id) throws LoginExption;

    List<user>seleone();
}
