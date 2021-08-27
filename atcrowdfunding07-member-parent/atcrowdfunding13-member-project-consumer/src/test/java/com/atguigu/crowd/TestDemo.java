package com.atguigu.crowd;

import com.atguigu.crowd.config.FileUploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * @author: JinSheng
 * @date: 2021/08/19 11:15 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestDemo {

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Test
    public void testDemo() throws FileNotFoundException {
        String s = fileUploadProperties.getOrginalPath();
        System.out.println(s);
    }
}
