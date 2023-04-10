package com.rose.workbench.service;

import com.rose.vo.vo;
import com.rose.workbench.domain.activity;
import com.rose.workbench.domain.activity_remark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean SavaActivity(activity activity);

    vo getActivity(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String,Object> selectActivityByid(String id);
   boolean update(activity activity);

    activity selectActivity(String id);

     List<activity_remark> getRemark(String id);

    boolean deleteRemark(String id);

    Map<String, Object> SavaAndSelectRemark(activity_remark remarks);

    boolean updateRemark(activity_remark remark1);

    List<activity> sele(String id);

    List<activity> selectActivityLikeNotById(String cid, String text);

    List<activity> selectActivityLikeById(String name);
}
