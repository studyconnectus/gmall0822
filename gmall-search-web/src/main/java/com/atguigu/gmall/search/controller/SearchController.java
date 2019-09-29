package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.service.PmsSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author liumw
 * @date 2019/9/27
 * @describe
 */
@Controller
public class SearchController {

    @Reference
    PmsSearchService searchService;


    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam, Model model){
        List<PmsSearchSkuInfo> searchSkuInfos = searchService.searchPmsSkuInfoList(pmsSearchParam);
        model.addAttribute("skuLsInfoList",searchSkuInfos);
        return "list";
    }
}
