package com.atguigu.gmall.user.service;

import com.atguigu.gmall.user.bean.UmsMember;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
public interface MemberService {
    List<UmsMember> getAllUsers();
}
