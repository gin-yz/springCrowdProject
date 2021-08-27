package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Student;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.List;

//@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @RequestMapping(value = "/test/world.html", method = RequestMethod.GET)
    public String testHandlerMapping() {
        return "redirect:target.html";
    }

    @RequestMapping(value = "/test/target.html", method = RequestMethod.GET)
    public String testHandlerMapping2(ModelMap modelMap) {
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);
        return "target";
    }

    @RequestMapping(value = "/test/index.html", method = RequestMethod.GET)
    public String testHandlerMapping3(HttpServletRequest request) {
//        int i = 1/0;
        //制造一个空指针异常，测试一下定义全局异常处理是否有用
//        String a = null;
//        a.length();
        boolean b = CrowdUtil.judgeRequestType(request);
        logger.info("请求是否为Ajax："+String.valueOf(b));
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/send/array/one.html",method = RequestMethod.POST)
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> list){
        for (Integer integer : list) {
            logger.debug(integer.toString());
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/send/array/three.html",method = RequestMethod.POST)
    public String testReceiveArrayThree(@RequestBody List<Integer> list){
        for (Integer integer : list) {
            logger.debug(integer.toString());
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/send/compose/object.json",method = RequestMethod.POST)
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request){
        logger.info(student.toString());
        boolean b = CrowdUtil.judgeRequestType(request);
        logger.info("请求是否为Ajax："+String.valueOf(b));
        //制造一个空指针异常，测试一下定义全局异常处理是否有用
        String a = null;
        a.length();
        return ResultEntity.successWithData(student);
    }


}
