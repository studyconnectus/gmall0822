package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsSkuInfo;

import java.util.List;

/**
 * @author liumw
 * @date 2019/9/3
 * @describe
 */
public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuInfoFromDB(Long skuId);

    PmsSkuInfo getSkuInfo(Long skuId);

    List<PmsSkuInfo> getSkuInfoList(Long catelog3Id);
}
