package com.atguigu.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.RequireLogin;
import com.atguigu.gmall.utils.CookieUtils;
import com.atguigu.gmall.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liumw
 * @date 2019/10/11
 * @describe
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println(request.getRequestURL());

        HandlerMethod handle = (HandlerMethod)handler;
        RequireLogin annotation = handle.getMethodAnnotation(RequireLogin.class);
        if (annotation == null){
            return true;
        }
        String token = "";
        String oldToken = CookieUtils.getCookieValue(request,"oldToken",true);
        if (StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)){
            token = newToken;
        }
        boolean loginSuccess = annotation.loginSuccess();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
        }
        //调用认证中心查询是否已经登录
        String successJson = HttpClientUtil.doGet("http://127.0.0.1:8085/verify?token=" + token + "&currentIp=" + ip );
        HashMap<String,String> result = JSON.parseObject(successJson, HashMap.class);

        if (loginSuccess){
            //如果token为空---转发到登录页面
            if (StringUtils.isBlank(token) || !"success".equals(result.get("status"))){
                response.sendRedirect("http://127.0.0.1:8085/index?returnUrl=" + request.getRequestURL());
                return false;
            }else {
                //验证通过
                request.setAttribute("memberId",result.get("memberId"));
                request.setAttribute("nickName",result.get("zhangsan"));
                CookieUtils.setCookie(request,response,"oldToken",token,60 * 60 *2,true);
            }
        }else{
            if ("success".equals(result.get("status"))){
                //校验成功
                request.setAttribute("memberId",result.get("memberId"));
                request.setAttribute("nickName",result.get("zhangsan"));
                CookieUtils.setCookie(request,response,"oldToken",token,60 * 60 *2,true);
            }
        }


        return true;
    }
}
