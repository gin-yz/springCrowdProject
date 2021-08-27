package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.entity.RoleExample.Criteria;
import com.atguigu.crowd.exception.AddRoleAlreadyExistException;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        //开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);

        return new PageInfo<>(roleList);
    }

    @Override
    public Integer addRole(String roleName) {
        Role role = new Role(null, roleName);
        try {
            roleMapper.insertWithOnlyRoleName(role);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){
                throw new AddRoleAlreadyExistException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
        return role.getId();
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {

        RoleExample example = new RoleExample();

        Criteria criteria = example.createCriteria();

        //delete from t_role where id in (5,8,12)
        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(example);
    }

    //根据id查询已经分配的角色
    @Override
    public List<Role> getAssignedRoleList(Integer adminId) {
        List<Role> roles = roleMapper.selectAssignedRole(adminId);
        return roles;
    }

    //根据id查询未分配的角色,(自己实现的方法)
    @Override
    public List<Role> getUnAssignedRoleList(Integer adminId) {
        List<Role> roles = roleMapper.selectUnAssignedRole(adminId);
        return roles;
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.selectAssignedRole(adminId);
    }

}
