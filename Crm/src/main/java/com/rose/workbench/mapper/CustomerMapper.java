package com.rose.workbench.mapper;

import com.rose.workbench.domain.Customer;

import java.util.List;

public interface CustomerMapper {

    Customer selectByCompany(String company);

    int SaveCustomer(Customer cust);

    List<String> getName(String name);

    Customer selectByname(String kname);
}
