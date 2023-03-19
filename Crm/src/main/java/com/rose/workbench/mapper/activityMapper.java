package com.rose.workbench.mapper;

import com.rose.workbench.domain.activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface activityMapper {
    int savaActivity(activity activity);

    List<activity> getActivity(Map<String,Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int deleteActivity(@Param("ids")String[] id);



    int updateActivity(activity activity);

    activity seleActitityByid(String id);

    activity seleActitityByidTwo(String id);
}
