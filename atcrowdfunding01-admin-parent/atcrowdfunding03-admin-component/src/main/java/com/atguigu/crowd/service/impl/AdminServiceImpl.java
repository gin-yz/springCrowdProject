package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    //插入管理员账户
    @Override
    public void saveAdmin(Admin admin) {
        //密码加密
        String userPswd = admin.getUserPswd();
//        userPswd = CrowdUtil.md5(userPswd);
        userPswd = passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);
        //生成插入日期
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名=" + e.getClass().getName());
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
        //故意抛个异常，看测试的日志中是否有回滚
//        throw new RuntimeException();
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        String md5Pwd = CrowdUtil.md5(userPswd);
        Admin admin = adminMapper.findByUserNameAndUserPswd(loginAcct, md5Pwd);
        if (admin == null) throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        //另外一种思路
//        // 1.根据登录账号查询Admin对象
//        // ①创建AdminExample对象
//        AdminExample adminExample = new AdminExample();
//
//        // ②创建Criteria对象
//        Criteria criteria = adminExample.createCriteria();
//
//        // ③在Criteria对象中封装查询条件
//        criteria.andLoginAcctEqualTo(loginAcct);
//
//        // ④调用AdminMapper的方法执行查询
//        List<Admin> list = adminMapper.selectByExample(adminExample);
//
//        // 2.判断Admin对象是否为null
//        if(list == null || list.size() == 0) {
//            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
//        }
//
//        if(list.size() > 1) {
//            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
//        }
//
//        Admin admin = list.get(0);
//
//        // 3.如果Admin对象为null则抛出异常
//        if(admin == null) {
//            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
//        }
//
//        // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
//        String userPswdDB = admin.getUserPswd();
//
//        // 5.将表单提交的明文密码进行加密
//        String userPswdForm = CrowdUtil.md5(userPswd);
//
//        // 6.对密码进行比较
//        if(!Objects.equals(userPswdDB, userPswdForm)) {
//            // 7.如果比较结果是不一致则抛出异常
//            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
//        }
//
//        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用PageHelper的静态方法开启分页功能
        // 这里充分体现了PageHelper的“非侵入式”设计：原本要做的查询不必有任何修改
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行查询，注意这个不是返回所有匹配的Admin，而是当前设置PageNum和PageSize对应的,
        // 因为在配置mybatis的时候，已经将分页拦截器插件植入进去了,每一次调用mybatis，mybatis的拦截器都要起作用
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        if (admin == null) throw new RuntimeException("账户不存在");
        return admin;
    }

    @Override
    public void update(Admin admin) {
        // “Selective”表示有选择的更新，对于null值的字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("异常全类名=" + e.getClass().getName());

            if (e instanceof DuplicateKeyException) {
                throw new RuntimeException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {

        // 旧数据如下：
        // adminId	roleId
        // 1		1（要删除）
        // 1		2（要删除）
        // 1		3
        // 1		4
        // 1		5
        // 新数据如下：
        // adminId	roleId
        // 1		3（本来就有）
        // 1		4（本来就有）
        // 1		5（本来就有）
        // 1		6（新）
        // 1		7（新）
        // 为了简化操作：先根据adminId删除旧的数据，再根据roleIdList保存全部新的数据

        //先删除掉id为adminId的全部inner_admin_role表的记录
        adminMapper.deleteOLdRelationship(adminId);

        //再往inner_admin_role表插入roleIdList中对应的role
        if (adminId != null && roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
        //纯粹是测试一下异常的捕获
//        throw new NullPointerException("空指针异常");
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        Admin admin = adminMapper.selectAdminByLoginAcct(username);
        return admin;
    }
}
