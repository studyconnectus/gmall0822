package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.user.bean.UmsMember;
import com.atguigu.gmall.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/getAllUsers")
    @ResponseBody
    public List<UmsMember> getAllUsers(){
        List<UmsMember> umsMembers = memberService.getAllUsers();
        return umsMembers;
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "hello world";
    }
}
