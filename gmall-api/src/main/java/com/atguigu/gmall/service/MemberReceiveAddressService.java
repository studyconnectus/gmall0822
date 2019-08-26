package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
public interface MemberReceiveAddressService {
    List<UmsMemberReceiveAddress> selectAddressByMemberId(Long memberId);
}
