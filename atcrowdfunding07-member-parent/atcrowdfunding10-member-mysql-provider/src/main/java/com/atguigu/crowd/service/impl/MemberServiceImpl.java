package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
// 在类上使用@Transactional(readOnly = true)针对查询操作设置事务属性，对这个类对所有方法都有效，除非特殊指定
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {
        MemberPO memberPO = memberPOMapper.selectMemberPOByLoginAccount(loginacct);
        return memberPO;
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class,
            readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
