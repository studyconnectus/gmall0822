package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/27
 * @describe 类目分类service
 */
public interface CatalogService {
    /**
     * 获取一级类目
     * @return
     */
    List<PmsBaseCatalog1> getCatalog1();

    /**
     * 获取二级类目
     * @return
     */
    List<PmsBaseCatalog2> getCatalog2(Long catalog1Id);

    /**
     * 获取三级类目
     * @return
     */
    List<PmsBaseCatalog3> getCatalog3(Long catalog2Id);
}
