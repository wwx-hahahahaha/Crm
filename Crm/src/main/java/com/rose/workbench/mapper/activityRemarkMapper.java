package com.rose.workbench.mapper;

import com.rose.workbench.domain.activity_remark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface activityRemarkMapper {
    int deleteActivityRemork(@Param("ids") String[] ids);

    int selectCount(@Param("ids")String[] ids);

     List<activity_remark> getRemark(String id);

    int deleteRemarkByid(String id);

    int SavaRemark(activity_remark remarks);

    activity_remark selectRemark(String id);

    int updateRermark(activity_remark remark1);
}
