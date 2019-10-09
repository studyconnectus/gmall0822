package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.BaseAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author liumw
 * @date 2019/8/27
 * @describe
 */
@Service
public class BaseAttrServiceImpl implements BaseAttrService {

    @Autowired
    private BaseAttrInfoMapper attrInfoMapper;

    @Autowired
    private BaseAttrValueMapper attrValueMapper;

    @Autowired
    private PmsBaseSaleAttrMapper saleAttrMapper;

    @Autowired
    private PmsBaseAttrInfoMapper baseAttrInfoMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id) {
        PmsBaseAttrInfo baseAttrInfo = new PmsBaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> attrInfos = attrInfoMapper.select(baseAttrInfo);
        for (PmsBaseAttrInfo attrInfo : attrInfos) {
            PmsBaseAttrValue value = new PmsBaseAttrValue();
            value.setAttrId(attrInfo.getId());
            List<PmsBaseAttrValue> select = attrValueMapper.select(value);
            attrInfo.setAttrValueList(select);
        }
        return attrInfos;
    }

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo attrInfo) {
        if (attrInfo.getId() == null){
            attrInfoMapper.insert(attrInfo);
            attrInfo.getAttrValueList().forEach(
                    x->{
                        x.setAttrId(attrInfo.getId());
                        attrValueMapper.insert(x);
                    }
            );
        }else {
            attrInfoMapper.updateByPrimaryKeySelective(attrInfo);
            attrInfo.getAttrValueList().forEach(
                    x->{
                        attrValueMapper.updateByPrimaryKeySelective(x);
                    }
            );
        }

    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",attrId);
        List<PmsBaseAttrValue> valueList = attrValueMapper.selectByExample(example);
        return valueList;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return saleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet, ",");
        List<PmsBaseAttrInfo> pmsSkuInfos = baseAttrInfoMapper.selectBaseAttrValueListById(valueIdStr);
        return pmsSkuInfos;
    }


}
