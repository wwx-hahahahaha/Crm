package com.rose.workbench.service;

import com.rose.vo.vo;
import com.rose.workbench.domain.activity;

import java.util.List;
import java.util.Map;

public interface activityService {
    boolean SavaActivity(activity activity);

    vo getActivity(Map<String, Object> map);

    boolean delete(String[] ids);

    activity selectActivityByid(String id);
   boolean update(activity activity);
}
