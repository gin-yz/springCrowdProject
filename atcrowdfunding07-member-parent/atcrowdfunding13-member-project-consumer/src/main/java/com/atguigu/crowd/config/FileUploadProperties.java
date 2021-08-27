package com.atguigu.crowd.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

/**
 * @author: JinSheng
 * @date: 2021/08/18 7:15 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("file.upload")
public class FileUploadProperties {
    private String orginalPath;
    private String innerPath;
    private String url;

    //之后发
    @PostConstruct
    public void postDo() throws FileNotFoundException {
        String localPath = ResourceUtils.getURL("classpath:").getPath();
        orginalPath = localPath + orginalPath;
    }
//
//    public FileUploadProperties(String orginalPath, String innerPath, String url) {
//        this.orginalPath = orginalPath;
//        this.innerPath = innerPath;
//        this.url = url;
//    }
//
//    public String getOrginalPath() {
//        return orginalPath;
//    }
//
//    public void setOrginalPath(String orginalPath) {
//        this.orginalPath = orginalPath;
//    }
//
//    public String getInnerPath() {
//        return innerPath;
//    }
//
//    public void setInnerPath(String innerPath) {
//        this.innerPath = innerPath;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
}
