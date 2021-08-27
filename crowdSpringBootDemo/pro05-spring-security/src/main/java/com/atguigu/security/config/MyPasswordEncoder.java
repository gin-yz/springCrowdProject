package com.atguigu.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Component
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return privateEncode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 1.对明文密码进行加密
        String formPassword = privateEncode(rawPassword);

        // 2.声明数据库密码
        String databasePassword = encodedPassword;

        // 3.比较
        return Objects.equals(formPassword, databasePassword);
    }

    private String privateEncode(CharSequence rawPassword) {
        try {
            //创建加密对象
            String algorithm = "MD5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            //获取rawPassword字节数组
            byte[] input = ((String) rawPassword).getBytes();

            //加密
            byte[] ouput = messageDigest.digest(input);

            //转换为16进制对应字符
            String enconded = new BigInteger(1, ouput).toString(16).toUpperCase();

            return enconded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        MyPasswordEncoder myPasswordEncoder = new MyPasswordEncoder();
//
//        String privateEncode = myPasswordEncoder.privateEncode("123123");
//
//        System.out.println(privateEncode);
//    }
}
