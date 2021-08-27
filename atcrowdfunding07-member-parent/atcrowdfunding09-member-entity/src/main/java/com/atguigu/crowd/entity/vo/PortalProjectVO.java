package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: JinSheng
 * @date: 2021/08/20 5:32 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    private Integer projectId;
    private String projectName;
    private String headerPicturePath;
    private Integer money;
    private String deployDate;
    private Integer percentage;
    private Integer supporter;
}
