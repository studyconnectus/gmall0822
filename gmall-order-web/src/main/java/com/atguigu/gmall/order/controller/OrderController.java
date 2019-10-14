package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.RequireLogin;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.MemberService;
import com.atguigu.gmall.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liumw
 * @date 2019/10/14
 * @describe
 */
@Controller
public class OrderController {

    @Reference
    private MemberService memberService;

    @Reference
    private CartService cartService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/toTrade")
    @RequireLogin
    public String toTrade(HttpServletRequest request, HttpServletResponse response, Model model){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        String nickName = (String)request.getAttribute("nickName");

        //获取购物车信息
        List<OmsCartItem> cartList = cartService.getCartList(memberId);
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : cartList) {
            if (omsCartItem.getIsChecked() == 1){
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPrice(omsCartItem.getPrice());
                omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }

        }
        //获取收获地址
        List<UmsMemberReceiveAddress> userReceiveAddress = memberService.getUserReceiveAddress(memberId);
        model.addAttribute("tradeCode",orderService.getTradeCode(memberId));
        model.addAttribute("userAddressList",userReceiveAddress);
        model.addAttribute("orderDetailList",omsOrderItems);
        return "trade";
    }

    @RequestMapping("/submitOrder")
    @RequireLogin
    public String submitOrder(Long addressId,String tradeCode,HttpServletRequest request,HttpServletResponse response){
        //校验交易码
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        String nickName = (String)request.getAttribute("nickName");
        String checkResult = orderService.checkTradeCode(memberId, tradeCode);
        if ("success".equals(checkResult)){
            //校验成功，开始生成订单
            //购物车信息
            return "success";
        }else {
            return "fail";
        }
    }
}
