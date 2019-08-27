package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.service.BaseAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id) {
        PmsBaseAttrInfo baseAttrInfo = new PmsBaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        return attrInfoMapper.select(baseAttrInfo);
    }

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo attrInfo) {
        attrInfoMapper.insert(attrInfo);
        attrInfo.getAttrValueList().forEach(
                x->{
                    x.setAttrId(attrInfo.getId());
                    attrValueMapper.insert(x);
                }
        );
    }

    @Override
    public PmsBaseAttrInfo getAttrValueList(Long attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",attrId);
        List<PmsBaseAttrValue> valueList = attrValueMapper.selectByExample(example);
        PmsBaseAttrInfo baseAttrInfo = attrInfoMapper.selectByPrimaryKey(attrId);
        baseAttrInfo.setAttrValueList(valueList);
        return baseAttrInfo;
    }
}
