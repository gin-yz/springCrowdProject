package com.atguigu.crowd.entity.po;

import java.io.Serializable;

/**
 * (Address)实体类
 *
 * @author makejava
 * @since 2021-08-22 16:03:41
 */
public class AddressPO implements Serializable {
    private static final long serialVersionUID = 362677531596220494L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 收件人
     */
    private String receiveName;
    /**
     * 手机号
     */
    private String phoneNum;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 用户 id
     */
    private Integer memberId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

}
