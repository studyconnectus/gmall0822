package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.service.BaseAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/27
 * @describe
 */
@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private BaseAttrService baseAttrService;

    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(Long catalog3Id){
        return baseAttrService.attrInfoList(catalog3Id);
    }

    @RequestMapping("/saveAttrInfo")
    public void saveAttrInfo(@RequestBody PmsBaseAttrInfo attrInfo){
        baseAttrService.saveAttrInfo(attrInfo);
    }

    @RequestMapping("/getAttrValueList")
    public PmsBaseAttrInfo getAttrValueList(Long attrId){
        return baseAttrService.getAttrValueList(attrId);
    }
}
