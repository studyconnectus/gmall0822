package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;

import java.util.List;

/**
 * @author liumw
 * @date 2019/9/29
 * @describe
 */
public interface PmsSearchService {

    List<PmsSearchSkuInfo> searchPmsSkuInfoList(PmsSearchParam searchParam);
}
