package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.*;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.mapper.MenuMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//在类上进行标记必要的注解，Spring整合Junit，只有打了这个后，@Test才有效
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml","classpath:spring-persist-security.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AuthService authService;

    //事务的测试，注意看日志
    @Test
    public void testTx() {
        Admin admin = new Admin(null, "jerry", "123456", "杰瑞", "jerry@qq.com",null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testLog() {

        // 1.获取Logger对象，这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        // 2.根据不同日志级别打印日志
        logger.debug("Hello I am Debug level!!!");
        logger.debug("Hello I am Debug level!!!");
        logger.debug("Hello I am Debug level!!!");

        logger.info("Info level!!!");
        logger.info("Info level!!!");
        logger.info("Info level!!!");

        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");
        logger.warn("Warn level!!!");

        logger.error("Error level!!!");
        logger.error("Error level!!!");
        logger.error("Error level!!!");
    }

    @Test
    public void testTX(){
        Admin admin = new Admin(null, "jerry", "123456", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testConnection() throws SQLException {
        Admin admin = adminMapper.selectByPrimaryKey(1);

        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        logger.debug(admin.toString());
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "tom", "123123", "汤姆", "tom@qq.com", null);
        int count = adminMapper.insert(admin);

        // 如果在实际开发中，所有想查看数值的地方都使用sysout方式打印，会给项目上线运行带来问题！
        // sysout本质上是一个IO操作，通常IO的操作是比较消耗性能的。如果项目中sysout很多，那么对性能的影响就比较大了。
        // 即使上线前专门花时间删除代码中的sysout，也很可能有遗漏，而且非常麻烦。
        // 而如果使用日志系统，那么通过日志级别就可以批量的控制信息的打印。
        System.out.println("受影响的行数="+count);
    }

    @Test
    public void testAdminLogin(){
        Date date = new Date();
        IntStream.range(1,100).mapToObj(i->
            new Admin(null, "admin" + i, String.valueOf(i), "admin" + i, "admin" + i + "@foxmail.com", String.valueOf(date.getTime()))
        ).forEach(obj->adminService.saveAdmin(obj));

    }

    @Test
    public void testRoleMapper(){
        IntStream.range(1,104)
                .mapToObj(i->new Role(null,"role"+i))
                .forEach(obj-> roleMapper.insert(obj));
    }

    @Test
    public void testRoleInsert(){
        Integer id = roleService.addRole("cjsdsg");
        System.out.println(id);
    }

    @Test
    public void menuTest(){
        //根节点
        Menu root = null;
        //创建临时暂存map
        Map<Integer, Menu> tmpMap = new HashMap<>();

        //从数据库中拿出所有的菜单
        List<Menu> allMenus = menuService.getAll();

        //将其放入map
        for (Menu menu : allMenus) {
            tmpMap.put(menu.getId(), menu);
        }
        //遍历，将所有menu和其父menu相关联
        for (Menu menu : allMenus) {
            Integer parentMenuId = menu.getPid();
            if (parentMenuId == null) {
                root = menu;
                continue;
            }

            Menu parentMenu = tmpMap.get(parentMenuId);
            parentMenu.getChildren().add(menu);

        }
    }

    @Test
    public void roleTest(){
//        List<Role> unAssignedRoleList = roleService.getUnAssignedRoleList(1);
//        for (Role role : unAssignedRoleList) {
//            System.out.println(role);
//        }
        List<Role> assignedRoleList = roleService.getAssignedRoleList(1);

        for (Role role : assignedRoleList) {
            System.out.println(role);
        }

    }

    @Test
    public void assignTest(){
        adminMapper.insertNewRelationship(1,IntStream.range(1,10).boxed().collect(Collectors.toList()));
    }

    @Test
    public void authTest(){
        List<String> auths = authMapper.selectAssignedAuthNameByAdminId(1);

        for (String auth : auths) {
            System.out.println(auth);
        }
    }

    @Test
    public void adminTest2(){
        Admin tom = adminService.getAdminByLoginAcct("tom");
        System.out.println(tom);
    }

}
