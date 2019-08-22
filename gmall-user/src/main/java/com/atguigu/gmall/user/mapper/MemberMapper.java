package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.user.bean.UmsMember;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
public interface MemberMapper {
    List<UmsMember> selectAllUsers();
}
