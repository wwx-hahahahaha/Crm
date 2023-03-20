package com.rose.workbench.service.impl;

import com.rose.settings.domain.user;
import com.rose.settings.mapper.userMapper;
import com.rose.utils.SqlSessionUtils;
import com.rose.vo.vo;
import com.rose.workbench.domain.activity;
import com.rose.workbench.domain.activity_remark;
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

    //查询user表和activity表，以便将数据绑定到修改表单中
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

//    查询activity表获取市场活动名称
    @Override
    public activity selectActivity(String id) {
        activity activity=mapper.seleActitityByidTwo(id);
        return activity;
    }

    //查询评论
    @Override
    public List<activity_remark> getRemark(String id) {
        List<activity_remark> remark=remarkMapper.getRemark(id);
        return remark;
    }

    @Override
    public boolean deleteRemark(String id) {
       int i= remarkMapper.deleteRemarkByid(id);
       if (i>0){
           return true;
       }
        return false;
    }

    //添加评论并查询出来
    @Override
    public Map<String, Object> SavaAndSelectRemark(activity_remark remarks) {
        int i=remarkMapper.SavaRemark(remarks);
        Map<String,Object>map=new HashMap<>();
        if (i>0){
            activity_remark remark=remarkMapper.selectRemark(remarks.getId());
            map.put("bo",true);
            map.put("list",remark);
        }
        return map;
    }

    @Override
    public boolean updateRemark(activity_remark remark1) {
        int i=remarkMapper.updateRermark(remark1);
        System.out.println("我是真的服了"+i);
        if (i>0){
            return true;
        }
        return false;
    }


}
