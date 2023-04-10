package com.rose.workbench.mapper;

import com.rose.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {

    List<ClueRemark> selectBycid(String cid);

    int delete(String id);
}
