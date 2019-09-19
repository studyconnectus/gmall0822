package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liumw
 * @date 2019/9/4
 * @describe
 */
@Controller
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;

    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("hello","hello thymeleaf!!");
        return "index";
    }

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable Long skuId,Model model){
        PmsSkuInfo skuInfo = skuService.getSkuInfo(skuId);

        List<PmsProductSaleAttr> saleAttrList = spuService.spuSaleAttrListCheckBySku(skuInfo.getProductId(),skuInfo.getId());
        Map<String,String> skuSaleAttrHash =  spuService.selectSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        model.addAttribute("skuSaleAttrHash", JSON.toJSONString(skuSaleAttrHash));
        model.addAttribute("spuSaleAttrListCheckBySku",saleAttrList);
        model.addAttribute("skuInfo",skuInfo);

        return "item";
    }
}
