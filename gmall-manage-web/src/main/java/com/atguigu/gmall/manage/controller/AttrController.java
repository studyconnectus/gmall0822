package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manage.utils.PmsUploadUtils;
import com.atguigu.gmall.service.BaseAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile file){

        String imageUrl = PmsUploadUtils.uploadImage(file);
        return imageUrl;
    }

    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(Long catalog3Id){
        return baseAttrService.attrInfoList(catalog3Id);
    }

    @RequestMapping("/saveAttrInfo")
    public void saveAttrInfo(@RequestBody PmsBaseAttrInfo attrInfo){
        baseAttrService.saveAttrInfo(attrInfo);
    }

    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId){
        return baseAttrService.getAttrValueList(attrId);
    }

    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        return baseAttrService.baseSaleAttrList();
    }

}
