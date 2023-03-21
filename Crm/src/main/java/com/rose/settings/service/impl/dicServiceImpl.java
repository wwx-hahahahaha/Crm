package com.rose.settings.service.impl;

import com.rose.settings.domain.dicType;
import com.rose.settings.domain.dicValue;
import com.rose.settings.mapper.dictypeMapper;
import com.rose.settings.mapper.dicvalueMapper;
import com.rose.settings.service.dicService;
import com.rose.utils.SqlSessionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dicServiceImpl implements dicService {
    private dictypeMapper typeMapper= SqlSessionUtils.getSessiom().getMapper(dictypeMapper.class);
    private dicvalueMapper valueMapper=SqlSessionUtils.getSessiom().getMapper(dicvalueMapper.class);


    @Override
    public List<dicType> sele() {
        List<dicType> list=typeMapper.sele();
        return list;
    }

    @Override
    public Map<String,List<dicValue>> selevalue() {
        Map<String,List<dicValue>> map=new HashMap<>();
        List<dicType> type=typeMapper.sele();
        for (dicType dicType : type) {
            List<dicValue> list=valueMapper.sele(dicType.getCode());
            map.put(dicType.getCode(),list);
        }
        return map;
    }
}
