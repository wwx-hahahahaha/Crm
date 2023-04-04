package com.rose.workbench.mapper;


import org.apache.ibatis.annotations.Param;

public interface ClueActivityRelationMapper {


    Integer deleteCAR(@Param("cid")String cid,@Param("aid")String aid);


    int SaveActivityRelation(@Param("uid") String uid,@Param("id") String id,@Param("cid") String cid);

}
