package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liumw
 * @date 2019/9/3
 * @describe
 */
public interface PmsBaseAttrInfoMapper extends Mapper<PmsSkuInfo> {

    List<PmsBaseAttrInfo> selectBaseAttrValueListById(@Param("valueIdStr") String valueIdStr);
}
