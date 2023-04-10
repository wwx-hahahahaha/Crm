package com.rose.workbench.mapper;

import com.rose.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryMapper {

    int save(TranHistory tranHistory);

    List<TranHistory> getTranHistory(String id);
}
