package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/27
 * @describe
 */
public interface BaseAttrService {

    List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo attrInfo);

    PmsBaseAttrInfo getAttrValueList(Long attrId);
}
