package com.rose.vo;

import java.util.List;

public class vo<T> {
    private Integer total;
    private List<T> list;


    public vo() {
    }

    public vo(Integer total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    /**
     * 获取
     * @return total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 设置
     * @param total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 获取
     * @return list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    public String toString() {
        return "vo{total = " + total + ", list = " + list + "}";
    }
}
