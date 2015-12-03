package com.xbb.la.modellibrary.bean;

import java.io.Serializable;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 13:23
 * 描述：员工信息
 */

public class Employee implements Serializable{
    private String id;
    private String name;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
