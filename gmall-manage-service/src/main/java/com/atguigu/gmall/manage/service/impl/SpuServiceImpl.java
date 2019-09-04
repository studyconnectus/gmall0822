package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/28
 * @describe
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private PmsProductInfoMapper productInfoMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> pmsSpuList(Long catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = productInfoMapper.select(pmsProductInfo);
        return pmsProductInfos;
    }

    @Override
    @Transactional
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        if (pmsProductInfo.getId() == null){
            productInfoMapper.insert(pmsProductInfo);
            if (pmsProductInfo.getSpuImageList() != null){
                pmsProductInfo.getSpuImageList().forEach(x->{
                    x.setProductId(pmsProductInfo.getId());
                    pmsProductImageMapper.insert(x);
                });
            }

            if (pmsProductInfo.getSpuSaleAttrList() != null){
                pmsProductInfo.getSpuSaleAttrList().forEach(x-> {
                    x.setProductId(pmsProductInfo.getId());
                    pmsProductSaleAttrMapper.insert(x);
                    x.getSpuSaleAttrValueList().forEach(y->{
                        y.setProductId(pmsProductInfo.getId());
                        y.setSaleAttrId(x.getSaleAttrId());
                        pmsProductSaleAttrValueMapper.insert(y);
                    });
                });
            }
        }else {
            productInfoMapper.updateByPrimaryKeySelective(pmsProductInfo);
        }
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(Long spuId) {
        PmsProductSaleAttr saleAttr = new PmsProductSaleAttr();
        saleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> saleAttrList = pmsProductSaleAttrMapper.select(saleAttr);
        for (PmsProductSaleAttr productSaleAttr : saleAttrList) {
            PmsProductSaleAttrValue saleAttrValue = new PmsProductSaleAttrValue();
            saleAttrValue.setProductId(spuId);
            saleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> select = pmsProductSaleAttrValueMapper.select(saleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(select);
        }
        return saleAttrList;
    }

    @Override
    public List<PmsProductImage> spuImageList(Long spuId) {

        PmsProductImage image = new PmsProductImage();
        image.setProductId(spuId);
        return pmsProductImageMapper.select(image);
    }
}
