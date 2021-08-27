package com.atguigu.crowd.entity.po;

import java.io.Serializable;

/**
 * (OrderProject)实体类
 *
 * @author makejava
 * @since 2021-08-22 16:05:30
 */
public class OrderProjectPO implements Serializable {
    private static final long serialVersionUID = -98576638307943226L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 发起人
     */
    private String launchName;
    /**
     * 回报内容
     */
    private String returnContent;
    /**
     * 回报数量
     */
    private Integer returnCount;
    /**
     * 支持单价
     */
    private Integer supportPrice;
    /**
     * 配送费用
     */
    private Integer freight;
    /**
     * 订单表的主键
     */
    private Integer orderId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLaunchName() {
        return launchName;
    }

    public void setLaunchName(String launchName) {
        this.launchName = launchName;
    }

    public String getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(String returnContent) {
        this.returnContent = returnContent;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public Integer getSupportPrice() {
        return supportPrice;
    }

    public void setSupportPrice(Integer supportPrice) {
        this.supportPrice = supportPrice;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

}
