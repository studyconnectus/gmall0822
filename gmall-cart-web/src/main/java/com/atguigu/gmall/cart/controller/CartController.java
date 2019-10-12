package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.utils.CookieUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author liumw
 * @date 2019/10/9
 * @describe
 */
@Controller
public class CartController {

    @Reference
    private SkuService skuService;

    @Reference
    private CartService cartService;

    @RequestMapping("/addToCart")
    public String addToCart(Long skuId, int quantity, HttpServletRequest request, HttpServletResponse response){
        // 调用商品服务获取商品信息
        PmsSkuInfo skuInfo = skuService.getSkuInfo(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setCreateDate(new Date());
        cartItem.setDeleteStatus(0);
        cartItem.setQuantity(quantity);
        cartItem.setProductSubTitle(skuInfo.getSkuDesc());
        cartItem.setProductSkuId(skuId);
        cartItem.setProductPic(skuInfo.getSkuDefaultImg());
        cartItem.setProductName(skuInfo.getSkuName());
        cartItem.setPrice(skuInfo.getPrice());
        cartItem.setProductAttr("");
        cartItem.setProductBrand("");
        cartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        cartItem.setProductId(skuInfo.getProductId());
        cartItem.setProductSkuCode("111111111");
        cartItem.setModifyDate(new Date());
        //判断用户是否登录
        Long memberId = (Long)request.getAttribute("memberId");
        String nickName = (String)request.getAttribute("nickName");
        if (memberId == null){
            //未登录 -判断cookie里面是否已经有值
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            List<OmsCartItem> omsCartItemList = new ArrayList<>();
            if (StringUtils.isBlank(cartListCookie)){
                //无cookie
                omsCartItemList.add(cartItem);
            }else{
                omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //判断是否已经存在改商品了
                Boolean exist = ifCartExist(cartItem,omsCartItemList);
                if (exist){
                    for (OmsCartItem omsCartItem : omsCartItemList) {
                        if (Objects.equals(omsCartItem.getProductSkuId(),cartItem.getProductSkuId())){
                            omsCartItem.setQuantity(omsCartItem.getQuantity() + cartItem.getQuantity());
                            break;
                        }
                    }
                }else {
                    omsCartItemList.add(cartItem);
                }
            }
            CookieUtils.setCookie(request,response,"cartListCookie", JSON.toJSONString(omsCartItemList),60*60*72,true);
        }else {
            //已经登录
            //获取购物车是否已经存在了当前添加的商品
            cartItem.setMemberId(memberId);
            OmsCartItem omsCartItem = cartService.ifCartExistByUser(memberId,skuId);
            //如果不存在则将购物车添加到数据库中
            if (omsCartItem == null){
                cartService.addCart(cartItem);
            } else {
                //如果存在则将添加商品个数之后做更新操作
                omsCartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                cartService.updateCart(omsCartItem);
            }
            //同步缓存
            cartService.flushCache(memberId);

        }
        return "redirect:/success";
    }

    @RequestMapping("success")
    public String success(){
        return "success";
    }

    @RequestMapping("checkCart")
    public String cartListInner(Integer isChecked,
                                Long productSkuId,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Model model){
        Long memberId = (Long)request.getAttribute("memberId");
        String nickName = (String)request.getAttribute("nickName");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setIsChecked(isChecked);
        omsCartItem.setProductSkuId(productSkuId);
        cartService.checkCart(omsCartItem);
        List<OmsCartItem> cartList = cartService.getCartList(memberId);
        model.addAttribute("cartList",cartList);
        return "cartListInner";
    }

    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, HttpServletResponse response, Model model){
        Long memberId = (Long)request.getAttribute("memberId");
        String nickName = (String)request.getAttribute("nickName");
        List<OmsCartItem> cartItems = null;
        if (memberId != null){
            //用户已经登录-从缓存中获取
            cartItems = cartService.getCartList(memberId);
        }else {
            //用户未登录-从cookie中获取
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)){
                cartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }
        BigDecimal bigDecimal = new BigDecimal(0);
        for (OmsCartItem cartItem : cartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            if (cartItem.getIsChecked() == 1){
                bigDecimal = bigDecimal.add(cartItem.getTotalPrice());
            }
        }
        model.addAttribute("totalprice",bigDecimal);

        model.addAttribute("cartList",cartItems);
        return "cartList";
    }

    private Boolean ifCartExist(OmsCartItem cartItem, List<OmsCartItem> omsCartItemList) {
        return omsCartItemList.stream().anyMatch(item->{
            if (cartItem.getProductSkuId() != null){
                return Objects.equals(cartItem.getProductSkuId(),item.getProductSkuId());
            }else {
                return false;
            }
        });
    }
}
