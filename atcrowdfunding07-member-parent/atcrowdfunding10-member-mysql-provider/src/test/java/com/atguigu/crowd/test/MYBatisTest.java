package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.mapper.AddressDao;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.OrderProjectDao;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @RunWith(SpringRunner.class)的作用
 * Springboot的@RunWith(SpringRunner.class)
 * 注解的意义在于Test测试类要使用注入的类，比如@Autowired注入的类，
 * 有了@RunWith(SpringRunner.class)这些类才能实例化到spring容器中，自动注入才能生效，
 * 不然直接一个NullPointerExecption
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MYBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OrderProjectDao orderProjectDao;


//    private Logger logger = LoggerFactory.getLogger(MYBatisTest.class);

    @Test
    public void testMapper() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String source = "123123";

        String encode = passwordEncoder.encode(source);

        MemberPO memberPO = new MemberPO(null, "jack", encode, "杰克", "jack@qq.com", 1, 1, "杰克", "123123", 2);

        memberPOMapper.insert(memberPO);
    }

    @Test
    public void testMapper1(){
        int i = memberPOMapper.countByExample(new MemberPOExample());
        System.out.println(i);
    }

    @Test
    public void testMapper2(){
        OrderProjectVO orderProjectVO = orderProjectDao.selectOrderProjectVO(3);
        System.out.println(orderProjectVO);
    }

}
