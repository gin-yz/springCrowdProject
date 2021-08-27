package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    int countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin findByUserNameAndUserPswd(@Param("loginAcct") String loginAcct, @Param("userPswd") String userPswd);

    //通过关键词查找管理员
    List<Admin> selectAdminByKeyword(String keyword);

    //删除掉id为adminId的全部inner_admin_role表的记录
    void deleteOLdRelationship(@Param("adminId") Integer adminId);

    //往inner_admin_role表插入roleIdList中对应的role
    void insertNewRelationship(@Param("adminId") Integer adminId, @Param("roleIdList") List<Integer> roleIdList);

    Admin selectAdminByLoginAcct(@Param("loginAcct") String username);
}