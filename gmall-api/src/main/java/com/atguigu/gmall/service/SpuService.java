package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liumw
 * @date 2019/8/28
 * @describe
 */
public interface SpuService {

    List<PmsProductInfo> pmsSpuList(Long catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(Long spuId);

    List<PmsProductImage> spuImageList(Long spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(Long spuId,Long skuId);

    Map<String, String> selectSkuSaleAttrValueListBySpu(Long spuId);
}
