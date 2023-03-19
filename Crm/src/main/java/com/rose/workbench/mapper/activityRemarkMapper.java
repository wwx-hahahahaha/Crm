package com.rose.workbench.mapper;

import org.apache.ibatis.annotations.Param;

public interface activityRemarkMapper {
    int deleteActivityRemork(@Param("ids") String[] ids);

    int selectCount(@Param("ids")String[] ids);
}
