package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: JinSheng
 * @date: 2021/08/13 11:10 AM
 */
@Slf4j
@Controller
@EnableConfigurationProperties(ShortMessageProperties.class)
public class MemberHandler {
    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySqlRemoteService;

    /**
     * 登陆
     */
    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session) {

        // 1.调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity =
                mySqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

            return "member-login";

        }

        MemberPO memberPO = resultEntity.getData();

        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

            return "member-login";
        }

        // 2.比较密码
        String userpswdDataBase = memberPO.getUserpswd();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

        if (!matcheResult) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 3.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        //注意，这个必须在zuul中配置zuul:add-host-header: true，不然的话会定位到本机ip:本微服务port/auth/member/to/center/page，
        //而不是网关的ip：网关的port/auth/member/to/center/page,或者直接暴力一点，定位到网关的url。
        //return "redirect:http://www.cjs.com/auth/member/to/center/page";
        return "redirect:/auth/member/to/center/page";
    }

    /**
     * 注销
     */
    @GetMapping("/auth/member/logout")
    public String logout(HttpSession session) {
        //将session失效
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 发送验证码
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        // 1.发送验证码到phoneNum手机
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(),
                phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());

        // 2.判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            //如果发送成功，将结果存入redis中
            //从上一步操作的结果中获取随机生成的验证码
            String code = sendMessageResultEntity.getData();
            String redisPhoneKey = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            int timeOut = 15;
            //调用redis的微服务，存入redis
            ResultEntity<String> rediResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(redisPhoneKey, code, timeOut, TimeUnit.MINUTES);
            //判断结果
            if (ResultEntity.SUCCESS.equals(rediResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return rediResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }
    }

    /**
     * 注册
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        //从redis中取出验证码
        String phoneNum = memberVO.getPhoneNum();
        String redisPhoneKey = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        if (phoneNum == null) {
            return "member-reg";
        }
        ResultEntity<String> redisResultEntity = null;
        try {
            redisResultEntity = redisRemoteService.getRedisStringValueByKeyRemote(redisPhoneKey);
        } catch (Exception e) {
            //如果是客户端调用时网络出错
            String clientException = "com.netflix.client.ClientException";
            if (e.getMessage().startsWith(clientException)) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, e.getMessage());
                return "member-reg";
            }
        }

        //判断是否请求redis微服务有效
        String redisResult = redisResultEntity.getResult();
        if (ResultEntity.FAILED.equals(redisResult)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, redisResultEntity.getMessage());
            return "member-reg";
        }
        //判断表单提交的验证码是否有效
        String submitCode = memberVO.getCode();
        if (submitCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        //同传过来对验证码进行比对
        String redisCode = redisResultEntity.getData();
        //如果不一致，则不能删，因为有可能填错了
        if (!Objects.equals(redisCode, submitCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }

        // 如果验证码一致，则从Redis删除
        try {
            redisRemoteService.removeRedisKeyRemote(redisPhoneKey);
        } catch (Exception e) {
            //每一个都用try/catch太麻烦，可以直接定义全局的异常处理
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, e.getMessage());
            return "member-reg";
        }

        // 执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();

        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);

        memberVO.setUserpswd(userpswdAfterEncode);

        // 执行保存
        // ①创建空的MemberPO对象
        MemberPO memberPO = new MemberPO();

        // ②复制属性
        BeanUtils.copyProperties(memberVO, memberPO);

        // ③调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySqlRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:/auth/member/to/login/page";
    }
}
