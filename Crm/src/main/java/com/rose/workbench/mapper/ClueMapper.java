package com.rose.workbench.mapper;


import com.rose.workbench.domain.Clue;
import com.rose.workbench.domain.activity;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface ClueMapper {


    void SaveClue(Clue clue);

    List<Clue> selectClue(Map<String, Object> map);

    Clue selectClueById(String id);

    List<activity> selectActivityNId(String cid);

    int delete(String cid);
}

