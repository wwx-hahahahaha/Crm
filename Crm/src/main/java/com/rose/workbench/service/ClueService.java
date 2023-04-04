package com.rose.workbench.service;

import com.rose.workbench.domain.Clue;
import com.rose.workbench.domain.activity;

import java.util.List;
import java.util.Map;

public interface ClueService {
    void SaveClue(Clue clue);


    List<Clue> selectClue(Map<String, Object> map);

    Clue selectClueById(String id);

    boolean selectCAR(String cid,String aid);

    List<activity> selectActivityNId(String cid);

    int SaveActivityRelation(String[] ids, String cid);


}
