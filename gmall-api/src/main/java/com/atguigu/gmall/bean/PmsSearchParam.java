package com.atguigu.gmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author liumw
 * @date 2019/9/29
 * @describe
 */
public class PmsSearchParam implements Serializable {
    private Long catalog3Id;

    private String keyword;

    private String[] valueId;

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String[] getValueId() {
        return valueId;
    }

    public void setValueId(String[] valueId) {
        this.valueId = valueId;
    }
}
