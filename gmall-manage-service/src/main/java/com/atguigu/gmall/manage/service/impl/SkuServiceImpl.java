package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liumw
 * @date 2019/9/3
 * @describe
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper skuInfoMapper;

    @Autowired
    private PmsSkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private PmsSkuImageMapper skuImageMapper;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        skuInfoMapper.insert(pmsSkuInfo);
        pmsSkuInfo.getSkuAttrValueList().forEach(x->{
            x.setSkuId(pmsSkuInfo.getId());
            skuAttrValueMapper.insert(x);
        });
        pmsSkuInfo.getSkuSaleAttrValueList().forEach(x->{
            x.setSkuId(pmsSkuInfo.getId());
            skuSaleAttrValueMapper.insert(x);
        });
        pmsSkuInfo.getSkuImageList().forEach(x->{
            x.setSkuId(pmsSkuInfo.getId());
            skuImageMapper.insert(x);
        });
    }
}
