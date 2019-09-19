package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/28
 * @describe
 */
public interface PmsProductSaleAttrMapper  extends Mapper<PmsProductSaleAttr> {

    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("spuId") Long spuId,@Param("skuId") Long skuId);

    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(@Param("spuId") Long spuId);
}
