package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登陆成功后放入session域的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginVO implements Serializable {
	
	private Integer id;
	
    private String username;
	
	private String email;
	
}