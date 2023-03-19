package com.rose.settings.service.impl;

import com.rose.exception.LoginExption;
import com.rose.settings.domain.user;
import com.rose.settings.mapper.userMapper;
import com.rose.settings.service.userServlce;
import com.rose.utils.DateTimeUtil;
import com.rose.utils.SqlSessionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userServiceImpl implements userServlce {

    private userMapper userMapper = SqlSessionUtils.getSessiom().getMapper(userMapper.class);

    @Override
    public user seleuser(String name, String pwd, String id) throws LoginExption {
        //调用Mybatis mapper接口方法根据用户名和密码查询对象
        user user = userMapper.seleuser(name,pwd);
        if (user == null) {
            //如果查到的对象为空的话,定义异常
            throw new LoginExption("账号或密码错误");
        }

        //user登录到期时间
        String expireTime = user.getExpireTime();
        //现在时间
        String time = DateTimeUtil.getSysTime();
        int i = expireTime.compareTo(time);
        System.out.println(i);
        if (i < 0) {//如果当前时间和登录到期时间相比小于0则定义异常
            throw new LoginExption("登录失效过期");
        }

        if ("0".equals(user.getLockState())) {//如果user getLockState对象中的状态为0 定义异常
            throw new LoginExption("账号已锁定");
        }

        String id1 = user.getAllowIps();
        System.out.println(id1);
        if (!id1.contains(id)) {
            //如果user对象的允许登录的ip地址不包括本机ip地址,则定义异常
            throw new LoginExption("ip地址不符合");
        }

        return user;
    }

    @Override
    public List<user> seleone() {
        return userMapper.seleone();
    }


}
