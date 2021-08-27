package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    //插入管理员账户
    void saveAdmin(Admin admin);

    //get所有管理员
    List<Admin> getAll();

    //管理员登陆的时候进行的对比
    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    //根据关键字查询管理员信息，并分页
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    //根据用户id删除用户
    void remove(Integer adminId);

    //根据id查找管理员账户
    Admin getAdminById(Integer id);

    //修改管理员账户属性
    void update(Admin admin);

    //将AdminId对应的role进行保存
    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    //通过LoginAcct(用户名唯一)找到Admin
    Admin getAdminByLoginAcct(String username);
}
