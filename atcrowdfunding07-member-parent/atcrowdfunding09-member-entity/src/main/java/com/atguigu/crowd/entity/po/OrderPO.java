package com.atguigu.crowd.entity.po;

import java.io.Serializable;

/**
 * (Order)实体类
 *
 * @author makejava
 * @since 2021-08-22 16:04:19
 */
public class OrderPO implements Serializable {
    private static final long serialVersionUID = 206634607061238261L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 支付宝流水号
     */
    private String payOrderNum;
    /**
     * 订单金额
     */
    private Double orderAmount;
    /**
     * 是否开发票(0 不开，1 开)
     */
    private Integer invoice;
    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 订单备注
     */
    private String orderRemark;
    /**
     * 收货地址 id
     */
    private String addressId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPayOrderNum() {
        return payOrderNum;
    }

    public void setPayOrderNum(String payOrderNum) {
        this.payOrderNum = payOrderNum;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getInvoice() {
        return invoice;
    }

    public void setInvoice(Integer invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

}
