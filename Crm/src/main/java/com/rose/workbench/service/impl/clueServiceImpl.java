package com.rose.workbench.service.impl;

import com.rose.utils.SqlSessionUtils;
import com.rose.utils.UUIDUtil;
import com.rose.workbench.domain.Clue;
import com.rose.workbench.domain.activity;
import com.rose.workbench.mapper.ClueActivityRelationMapper;
import com.rose.workbench.mapper.ClueMapper;
import com.rose.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class clueServiceImpl implements ClueService {
    private ClueMapper clueMapper= SqlSessionUtils.getSessiom().getMapper(ClueMapper.class);
    private ClueActivityRelationMapper CARMapper=SqlSessionUtils.getSessiom().getMapper(ClueActivityRelationMapper.class);
    @Override
    public void SaveClue(Clue clue) {
        clueMapper.SaveClue(clue);
    }

    @Override
    public List<Clue> selectClue(Map<String, Object> map) {
        List<Clue> list=clueMapper.selectClue(map);
        return list;
    }

    @Override
    public Clue selectClueById(String id) {
        Clue clue=clueMapper.selectClueById(id);
        return clue;
    }

    @Override
    public boolean selectCAR(String cid,String aid) {
        Integer i=CARMapper.deleteCAR(cid,aid);
        if (i!=1){
            return false;
        }
        return true;
    }

    @Override
    public List<activity> selectActivityNId(String cid) {
        List<activity> list=clueMapper.selectActivityNId(cid);
        return list;
    }

    @Override
    public int SaveActivityRelation(String[] ids, String cid) {
        int count =0;
        for (String id : ids) {
            String uid= UUIDUtil.getUUID();
            count+=CARMapper.SaveActivityRelation(uid,id,cid);
        }
        return count;
    }


}
