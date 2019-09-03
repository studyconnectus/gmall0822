package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/27
 * @describe
 */
public interface BaseAttrService {

    List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo attrInfo);

    List<PmsBaseAttrValue> getAttrValueList(Long attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
