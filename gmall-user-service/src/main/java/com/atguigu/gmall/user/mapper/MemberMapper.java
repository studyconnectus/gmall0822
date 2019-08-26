package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
public interface MemberMapper extends Mapper<UmsMember> {
    List<UmsMember> selectAllUsers();
}
