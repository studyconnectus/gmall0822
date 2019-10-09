package com.atguigu.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liumw
 * @date 2019/10/9
 * @describe
 */
@Controller
public class CartController {

    @RequestMapping("/addToCart")
    public String addToCart(){
        return "success";
    }
}
