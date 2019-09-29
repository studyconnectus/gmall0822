package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

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
    @Autowired
    private RedisUtil redisUtil;

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

    @Override
    public PmsSkuInfo getSkuInfoFromDB(Long skuId) {

        PmsSkuInfo pmsSkuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        if (pmsSkuInfo == null){
            return null;
        }

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> select = skuImageMapper.select(pmsSkuImage);
        pmsSkuInfo.setSkuImageList(select);
        return pmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getSkuInfo(Long skuId) {
        Jedis jedis = redisUtil.getJedis();
        String skuKey = "item:" + skuId + ":info";
        String itemJson = jedis.get(skuKey);
        PmsSkuInfo skuInfo = null;
        if (itemJson == null){
            //使用redis的分布式锁防止缓存击穿
            String OK = jedis.set("item:" + skuId + ":lock", "1", "nx", "px", 10*1000);
            if (StringUtils.isNotBlank(OK) && "OK".equals(OK)){
                //设置分布式锁成功，从数据库查询数据
                skuInfo = getSkuInfoFromDB(skuId);
                if (skuInfo == null){
                    return null;
                }
                jedis.set(skuKey, JSON.toJSONString(skuInfo));
            }else {
                //设置分布式锁失败，自旋等待3秒再次进行获取
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuInfo(skuId);
            }
        }else {
            skuInfo = JSON.parseObject(itemJson, PmsSkuInfo.class);
        }
        return skuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuInfoList(Long catelog3Id) {
        PmsSkuInfo skuInfo = new PmsSkuInfo();
        skuInfo.setCatalog3Id(catelog3Id);
        List<PmsSkuInfo> select = skuInfoMapper.select(skuInfo);
        for (PmsSkuInfo pmsSkuInfo : select) {
            PmsSkuAttrValue skuAttrValue = new PmsSkuAttrValue();
            skuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> select1 = skuAttrValueMapper.select(skuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select1);

        }
        return select;
    }


}
