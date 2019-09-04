package com.atguigu.gmall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liumw
 * @date 2019/9/4
 * @describe
 */
@Controller
public class ItemController {

    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("hello","hello thymeleaf!!");
        return "index";
    }
}
