package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductInfo;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/28
 * @describe
 */
public interface SpuService {

    List<PmsProductInfo> PmsspuList(Long catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);
}
