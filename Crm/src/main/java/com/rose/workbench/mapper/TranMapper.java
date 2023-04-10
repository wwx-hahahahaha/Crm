package com.rose.workbench.mapper;

import com.rose.workbench.domain.Tran;

import java.util.List;

public interface TranMapper {

    int Save(Tran tran);

    List<Tran> getTran();

    Tran getTranByid(String id);

    int update(Tran tran);
}
