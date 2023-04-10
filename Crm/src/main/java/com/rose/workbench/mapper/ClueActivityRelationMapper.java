package com.rose.workbench.mapper;


import com.rose.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationMapper {


    List<ClueActivityRelation> selectByCid(String cid);

    Integer deleteCAR(@Param("cid")String cid, @Param("aid")String aid);


    int SaveActivityRelation(@Param("uid") String uid,@Param("id") String id,@Param("cid") String cid);

    int delete(String id);
}
