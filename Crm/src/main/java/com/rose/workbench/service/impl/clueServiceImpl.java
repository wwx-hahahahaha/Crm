package com.rose.workbench.service.impl;

import com.rose.settings.domain.user;
import com.rose.settings.mapper.userMapper;
import com.rose.utils.DateTimeUtil;
import com.rose.utils.SqlSessionUtils;
import com.rose.utils.UUIDUtil;
import com.rose.workbench.domain.*;
import com.rose.workbench.mapper.*;
import com.rose.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class clueServiceImpl implements ClueService {
    //线索相关表
    private ClueMapper clueMapper= SqlSessionUtils.getSessiom().getMapper(ClueMapper.class);
    private ClueActivityRelationMapper CARMapper=SqlSessionUtils.getSessiom().getMapper(ClueActivityRelationMapper.class);
    private ClueRemarkMapper clueRemarkMapper =SqlSessionUtils.getSessiom().getMapper(ClueRemarkMapper.class);

    //客户相关表
    private CustomerMapper customerMapper=SqlSessionUtils.getSessiom().getMapper(CustomerMapper.class);
    private CustomerRemarkMapper customerRemarkMapper=SqlSessionUtils.getSessiom().getMapper(CustomerRemarkMapper.class);

    //联系人相关表
    private ContactsMapper contactsMapper= SqlSessionUtils.getSessiom().getMapper(ContactsMapper.class);
    private ContactsActivityRelationMapper contactsActivityRelationMapper=SqlSessionUtils.getSessiom().getMapper(ContactsActivityRelationMapper.class);
    private ContactsRemarkMapper contactsRemarkMapper=SqlSessionUtils.getSessiom().getMapper(ContactsRemarkMapper.class);

    //交易相关表
    private TranMapper tranMapper=SqlSessionUtils.getSessiom().getMapper(TranMapper.class);
    private TranHistoryMapper tranHistoryMapper=SqlSessionUtils.getSessiom().getMapper(TranHistoryMapper.class);
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

    @Override
    public boolean convert(Tran tran, String cid, String createBy) {
        boolean bo=true;
        //根据cid取出线索表的相关信息
        Clue clue = clueMapper.selectClueById(cid);
        String sysTime = DateTimeUtil.getSysTime();
        String company = clue.getCompany();
        //先根据公司名查询客户，判断是否存在
        Customer customer =customerMapper.selectByCompany(company);
        Customer cust=new Customer();
        if (customer==null){//如果不存在，手动添加
            cust.setId(UUIDUtil.getUUID());
            cust.setName(company);
            cust.setDescription(clue.getDescription());
            cust.setOwner(clue.getOwner());
            cust.setAddress(clue.getAddress());
            cust.setCreateTime(sysTime);
            cust.setCreateBy(createBy);
            cust.setContactSummary(clue.getContactSummary());
            cust.setWebsite(clue.getWebsite());
            cust.setPhone(clue.getPhone());
            cust.setNextContactTime(clue.getNextContactTime());
            int count=customerMapper.SaveCustomer(cust);
            if (count!=-1){
                bo=false;
            }
        }

        //添加联系人相关数据
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(cust.getId());
        contacts.setCreateTime(sysTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        int count1=contactsMapper.SaveContacts(contacts);
        if (count1!=1){
            bo=false;
        }

//        线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarks=clueRemarkMapper.selectBycid(cid);
        for (ClueRemark clueRemark : clueRemarks) {
            //取出备注信息（主要转换到客户备注和联系人备注上）
            String content = clueRemark.getNoteContent();

            CustomerRemark cp=new CustomerRemark();
            cp.setId(UUIDUtil.getUUID());
            cp.setCustomerId(cust.getId());
            cp.setNoteContent(content);
            cp.setCreateTime(sysTime);
            cp.setCreateBy(createBy);
            cp.setEditBy("0");
            int count2=customerRemarkMapper.save(cp);
            if (count2!=1){
                bo=false;
            }



            ContactsRemark cr=new ContactsRemark();
            cr.setId(UUIDUtil.getUUID());
            cr.setContactsId(contacts.getId());
            cr.setNoteContent(content);
            cr.setCreateTime(sysTime);
            cr.setCreateBy(createBy);
            cr.setEditBy("0");
            int count3=contactsRemarkMapper.save(cr);
            if (count3!=1){
                bo=false;
            }
        }

        // “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelations=CARMapper.selectByCid(cid);
        for (ClueActivityRelation relation : clueActivityRelations) {
            String id = relation.getActivityId();
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(id);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            int count5=contactsActivityRelationMapper.Save(contactsActivityRelation);
            if (count5!=1){
                bo=false;
            }
        }

        if (tran!=null){
            tran.setSource(clue.getSource());
            String owner = clue.getOwner();
            userMapper userMapper=SqlSessionUtils.getSessiom().getMapper(com.rose.settings.mapper.userMapper.class);
            user user=userMapper.selectOwner(owner);
            tran.setOwner(user.getId());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(cust.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            //添加交易
            int count=tranMapper.Save(tran);
            if (count!=1){
                bo=false;
            }
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setCreateTime(sysTime);
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());

            int count3=tranHistoryMapper.save(tranHistory);
            if (count3!=1){
                bo=false;
            }
        }

        //删除线索备注
        for (ClueRemark clueRemark : clueRemarks){
            int count=clueRemarkMapper.delete(clueRemark.getId());
            if (count!=1){
                bo=false;
            }
        }

        //删除线索和市场活动的关系
        for (ClueActivityRelation relation : clueActivityRelations) {
            int count=CARMapper.delete(relation.getId());
            if (count!=1){
                bo=false;
            }
        }

        //删除线索
        int count=clueMapper.delete(cid);
        if (count!=1){
            bo=false;
        }
        return bo;
    }


}
