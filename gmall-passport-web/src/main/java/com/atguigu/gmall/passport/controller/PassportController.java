package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.passport.utils.JwtUtil;
import com.atguigu.gmall.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liumw
 * @date 2019/10/11
 * @describe
 */
@Controller
public class PassportController {

    @Reference
    private MemberService memberService;

    @RequestMapping("/verify")
    @ResponseBody
    public String index(String token,String currentIp){
        Map<String,String> map = new HashMap<>();

        Map<String, Object> result = JwtUtil.decode(token, "gmall0822", currentIp);
        if (result !=null){
            map.put("status","success");
            map.put("memberId",(String)result.get("memberId"));
            map.put("nickName",(String)result.get("nickName"));
        }else {
            map.put("status","fail");
        }

        return JSON.toJSONString(map);
    }

    @RequestMapping("/index")
    public String index(String returnUrl, Model model){
        model.addAttribute("returnUrl",returnUrl);
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request,UmsMember umsMember){
        UmsMember umsMember1 = memberService.login(umsMember);
        if (umsMember1 != null){
            //登录成功,开始生成token
            String token = getTokenStr(request,umsMember1);
            return token;
        }
        return "fail";
    }

    private String getTokenStr(HttpServletRequest request,UmsMember umsMember1) {
        String key = "gmall0822";
        Map<String,Object> param = new HashMap<>();
        param.put("memberId",umsMember1.getId());
        param.put("nickName",umsMember1.getNickname());

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
        }
        /*String time = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        String salt = ip + time;*/
        String encode = JwtUtil.encode(key, param, ip);
        return encode;

    }
}
