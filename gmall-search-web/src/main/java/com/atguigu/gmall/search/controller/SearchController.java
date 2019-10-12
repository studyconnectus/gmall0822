package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.RequireLogin;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.BaseAttrService;
import com.atguigu.gmall.service.PmsSearchService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liumw
 * @date 2019/9/27
 * @describe
 */
@Controller
public class SearchController {

    @Reference
    PmsSearchService searchService;

    @Reference
    BaseAttrService baseAttrService;

    @RequestMapping("/toTrade")
    @RequireLogin(loginSuccess = true)
    public String toTrade(HttpServletRequest request){
        String memberId = (String)request.getAttribute("memberId");
        String nickName = (String)request.getAttribute("nickName");
        return "toTrade";
    }

    @RequestMapping("/index")
    @RequireLogin(loginSuccess = false)
    public String index() {
        return "index";
    }

    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam, Model model) {
        List<PmsSearchSkuInfo> searchSkuInfos = searchService.searchPmsSkuInfoList(pmsSearchParam);
        Set<String> collect = searchSkuInfos.stream()
                .map(x -> x.getSkuAttrValueList())
                .flatMap(Collection::stream)
                .map(x -> x.getValueId().toString())
                .collect(Collectors.toSet());
        List<PmsBaseAttrInfo> attrValueListByValueId = baseAttrService.getAttrValueListByValueId(collect);
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        //将已经选择的属性从属性列表里面删除
        String[] delValueId = pmsSearchParam.getValueId();
        if (delValueId != null && delValueId.length > 0) {
            Iterator<PmsBaseAttrInfo> iterator = attrValueListByValueId.iterator();
            while (iterator.hasNext()) {
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue value : attrValueList) {
                    Long valueId = value.getId();
                    for (String v : delValueId) {
                        if (Objects.equals(valueId.toString(), v)) {
                            PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                            pmsSearchCrumb.setValueId(v);
                            pmsSearchCrumb.setValueName(value.getValueName());
                            pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, v));
                            pmsSearchCrumbs.add(pmsSearchCrumb);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(pmsSearchParam.getKeyword())) {
            model.addAttribute("keyword", pmsSearchParam.getKeyword());
        }


        model.addAttribute("attrValueSelectedList", pmsSearchCrumbs);

        model.addAttribute("urlParam", getUrlParam(pmsSearchParam));
        model.addAttribute("skuLsInfoList", searchSkuInfos);
        model.addAttribute("attrList", attrValueListByValueId);
        return "list";
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam, String... delValueId) {

        String keyword = pmsSearchParam.getKeyword();
        Long catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueId = pmsSearchParam.getValueId();
        String urlParam = "";
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if (catalog3Id != null) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (valueId == null) {
            return urlParam;
        }
        for (int i = 0; i < valueId.length; i++) {
            if (delValueId != null && delValueId.length > 0 && Objects.equals(valueId[i], delValueId[0])) {
                continue;
            }
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "valueId=" + valueId[i];
        }

        return urlParam;
    }
}
