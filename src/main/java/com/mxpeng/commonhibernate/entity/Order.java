package com.mxpeng.commonhibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by tian_ on 2016/12/22.
 */
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    private String id;
    private String goods;
    private String createed;
    private String updateed;
    private String userId;

    public Order(String id, String goods, String createed, String updateed, String userId) {
        this.id = id;
        this.goods = goods;
        this.createed = createed;
        this.updateed = updateed;
        this.userId = userId;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getCreateed() {
        return createed;
    }

    public void setCreateed(String createed) {
        this.createed = createed;
    }

    public String getUpdateed() {
        return updateed;
    }

    public void setUpdateed(String updateed) {
        this.updateed = updateed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", goods='" + goods + '\'' +
                ", createed='" + createed + '\'' +
                ", updateed='" + updateed + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
