package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
public interface MemberService {

    List<UmsMember> getAllUsers();

    UmsMember login(UmsMember umsMember);

    List<UmsMemberReceiveAddress> getUserReceiveAddress(Long memberId);

}
