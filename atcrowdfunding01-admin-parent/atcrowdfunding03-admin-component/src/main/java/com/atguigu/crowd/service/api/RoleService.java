package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    Integer addRole(String roleName);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRoleList(Integer adminId);

    List<Role> getUnAssignedRoleList(Integer adminId);

    List<Role> getAssignedRole(Integer adminId);
}
