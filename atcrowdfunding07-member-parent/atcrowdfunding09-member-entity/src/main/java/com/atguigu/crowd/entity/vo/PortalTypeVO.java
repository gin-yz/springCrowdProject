package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: JinSheng
 * @date: 2021/08/20 5:31 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalTypeVO {
    private Integer id;
    private String name;
    private String remark;

    private List<PortalProjectVO> portalProjectVOList;
}
