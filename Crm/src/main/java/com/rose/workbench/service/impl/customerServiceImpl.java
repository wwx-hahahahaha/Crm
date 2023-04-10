package com.rose.workbench.service.impl;

import com.rose.utils.SqlSessionUtils;
import com.rose.workbench.mapper.CustomerMapper;
import com.rose.workbench.service.CustomerService;

import java.util.List;

public class customerServiceImpl implements CustomerService {
    private CustomerMapper customerMapper= SqlSessionUtils.getSessiom().getMapper(CustomerMapper.class);
    @Override
    public List<String> getCustomerName(String name) {
        List<String> name1=customerMapper.getName(name);
        return name1;
    }
}
