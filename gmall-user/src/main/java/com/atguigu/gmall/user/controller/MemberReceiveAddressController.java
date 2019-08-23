package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.user.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.user.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/23
 * @describe
 */
@Controller
public class MemberReceiveAddressController {


    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    @RequestMapping("/getAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getAddressByMemberId(Long memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = memberReceiveAddressService.selectAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }
}
