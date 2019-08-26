package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.user.mapper.MemberMapper;
import com.atguigu.gmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<UmsMember> getAllUsers() {
        return memberMapper.selectAll();
    }
}
