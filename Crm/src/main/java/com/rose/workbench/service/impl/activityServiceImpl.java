package com.rose.workbench.service.impl;

import com.rose.settings.domain.user;
import com.rose.settings.mapper.userMapper;
import com.rose.utils.SqlSessionUtils;
import com.rose.vo.vo;
import com.rose.workbench.domain.activity;
import com.rose.workbench.mapper.activityMapper;
import com.rose.workbench.mapper.activityRemarkMapper;
import com.rose.workbench.service.activityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activityServiceImpl implements activityService {
    private activityMapper mapper=SqlSessionUtils.getSessiom().getMapper(activityMapper.class);
    private activityRemarkMapper remarkMapper=SqlSessionUtils.getSessiom().getMapper(activityRemarkMapper.class);

    private userMapper user =SqlSessionUtils.getSessiom().getMapper(userMapper.class);
    //添加activity(市场活动)方法
    @Override
    public boolean SavaActivity(activity activity) {
        int i = mapper.savaActivity(activity);
        if (i==0){
            return false;
        }
        return true;
    }

    //查询activity方法
    @Override
    public vo getActivity(Map<String, Object> map) {
        int i=mapper.getTotalByCondition(map);
        List<activity>list=mapper.getActivity(map);
        vo<activity> vos=new vo<>();
        vos.setTotal(i);
        vos.setList(list);
        return vos;
    }

    //批量删除方法
    @Override
    public boolean delete(String[] ids) {
        int count=remarkMapper.selectCount(ids);
        if (count!=0){
            int deleteNums = remarkMapper.deleteActivityRemork(ids);
            if (count!=deleteNums){
                return false;
            }
        }

        int i = mapper.deleteActivity(ids);
        if (i!= ids.length){
            return false;
        }
        return true;
    }

    @Override
    public Map<String,Object> selectActivityByid(String id) {

        activity activity=mapper.seleActitityByid(id);
        List<user> seleone = user.seleone();
        Map<String,Object>map=new HashMap<>();
        map.put("a",activity);
        map.put("user",seleone);
        return map;
    }


    //修改和查询activity
    @Override
    public boolean update(activity activity) {
        int i = mapper.updateActivity(activity);
        System.out.println("数字是"+i);
        if (i!=1){
            return false;
        }
        return true;
    }

    @Override
    public activity selectActivity(String id) {
        activity activity=mapper.seleActitityByidTwo(id);
        return activity;
    }


}
