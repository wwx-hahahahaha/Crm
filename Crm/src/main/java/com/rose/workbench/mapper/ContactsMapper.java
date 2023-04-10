package com.rose.workbench.mapper;

import com.rose.workbench.domain.Contacts;

import java.util.List;

public interface ContactsMapper {

    int SaveContacts(Contacts contacts);

    List<Contacts> selectLikeByName(String name);

}
