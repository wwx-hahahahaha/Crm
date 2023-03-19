package com.rose.settings.mapper;

import com.rose.settings.domain.user;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface userMapper {
    user seleuser(@Param("name")String name,@Param("password")String pwd);

    @Select("select * from tbl_user ")
    List<user>seleone();

}
