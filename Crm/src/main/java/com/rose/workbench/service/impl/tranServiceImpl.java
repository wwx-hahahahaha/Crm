package com.rose.workbench.service.impl;

import com.rose.settings.domain.user;
import com.rose.settings.mapper.userMapper;
import com.rose.utils.DateTimeUtil;
import com.rose.utils.SqlSessionUtils;
import com.rose.utils.UUIDUtil;
import com.rose.workbench.domain.*;
import com.rose.workbench.mapper.*;
import com.rose.workbench.service.TranService;

import java.util.List;

public class tranServiceImpl implements TranService {
    private TranMapper tranMapper= SqlSessionUtils.getSessiom().getMapper(TranMapper.class);
    //市场活动
    private activityMapper activityMapper=SqlSessionUtils.getSessiom().getMapper(activityMapper.class);
//    用户
    private userMapper userMapper=SqlSessionUtils.getSessiom().getMapper(userMapper.class);
    //联系人
    private ContactsMapper contactsMapper=SqlSessionUtils.getSessiom().getMapper(ContactsMapper.class);
    //客户
    private CustomerMapper customerMapper=SqlSessionUtils.getSessiom().getMapper(CustomerMapper.class);
    //历史交易
    private TranHistoryMapper tranHistoryMapper=SqlSessionUtils.getSessiom().getMapper(TranHistoryMapper.class);
    @Override
    public List<user> add() {
        List<user> list = userMapper.seleone();
        return list;
    }

    @Override
    public List<activity> activityLike(String name) {
        List<activity> list=activityMapper.selectActivityLikeById(name);
        return list;
    }

    @Override
    public List<Contacts> contactLike(String name) {
        List<Contacts> list=contactsMapper.selectLikeByName(name);
        return list;
    }

    @Override
    public boolean SaveTran(Tran tran, String kname, String createBy) {
        String time= DateTimeUtil.getSysTime();
        boolean bo=true;
        Customer customer1 =customerMapper.selectByname(kname);
        if (customer1 == null) {
             customer1=new Customer();
            customer1.setCreateBy(createBy);
            customer1.setCreateTime(time);
            customer1.setId(UUIDUtil.getUUID());
            customer1.setName(kname);
            int count = customerMapper.SaveCustomer(customer1);
            if (count!=1){
                bo=false;
            }
        }

        tran.setCustomerId(customer1.getId());
        int count = tranMapper.Save(tran);
        if (count!=1){
            bo=false;
        }
        return bo;
    }

    @Override
    public List<Tran> getTran() {
        List<Tran> list=tranMapper.getTran();
        return list;
    }

    @Override
    public Tran getTranByid(String id) {
        Tran list=tranMapper.getTranByid(id);
        return list;
    }

    @Override
    public List<TranHistory> getTranHistory(String id) {
        List<TranHistory> list=tranHistoryMapper.getTranHistory(id);
        return list;
    }

    @Override
    public boolean update(TranHistory tranHistory) {
        boolean bo=true;
        int save = tranHistoryMapper.save(tranHistory);
        if (save!=1){
            bo=false;
        }
        Tran tran=new Tran();
        tran.setId(tranHistory.getTranId());
        tran.setStage(tranHistory.getStage());
        tran.setEditBy(tranHistory.getCreateBy());
        tran.setEditTime(tranHistory.getCreateTime());
        int update=tranMapper.update(tran);
        if (update!=1){
            bo=false;
        }
        return bo;
    }
}
