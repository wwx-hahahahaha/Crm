package com.rose.settings.service;

import com.rose.settings.domain.dicType;
import com.rose.settings.domain.dicValue;

import java.util.List;
import java.util.Map;

public interface dicService {
    List<dicType> sele();

    Map<String,List<dicValue>> selevalue();
}
