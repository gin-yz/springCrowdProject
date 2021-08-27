package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAllAuth() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        List<Integer> roleIdLsit = map.get("roleId");
        Integer roleId = roleIdLsit.get(0);
        List<Integer> authIdList = map.get("authIdArray");

        //删除掉表中InnerAdminRole为roleId的所有列
        authMapper.deleteInnerRoleAuthByRoleId(roleId);

        //插入新的Role和Auth
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertInnerRoleAuth(roleId, authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
