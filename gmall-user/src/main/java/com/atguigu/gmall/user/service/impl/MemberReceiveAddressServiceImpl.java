package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.user.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
@Service
public class MemberReceiveAddressServiceImpl implements MemberReceiveAddressService {

    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;

    @Override
    public List<UmsMemberReceiveAddress> selectAddressByMemberId(Long memberId) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId",memberId);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        return addressList;
    }
}
