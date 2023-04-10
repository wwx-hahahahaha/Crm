package com.rose.workbench.service;

import com.rose.settings.domain.user;
import com.rose.workbench.domain.Contacts;
import com.rose.workbench.domain.Tran;
import com.rose.workbench.domain.TranHistory;
import com.rose.workbench.domain.activity;

import java.util.List;

public interface TranService {
    List<user> add();

    List<activity> activityLike(String name);

    List<Contacts> contactLike(String name);

    boolean SaveTran(Tran tran, String kname, String createBy);

    List<Tran> getTran();

   Tran getTranByid(String id);

    List<TranHistory> getTranHistory(String id);

    boolean update(TranHistory tranHistory);
}
