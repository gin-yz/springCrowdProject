package com.atguigu.crowd.springSecurityConfig;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 考虑到User对象中仅仅包含账号和密码，为了能够获取到原始的Admin对象，专门创建这个类对User类进行扩展
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    //原始的admin对象，也就是对应数据库中的admin对象的bean
    private Admin originalAdmin;

    /**
     * 构造方法
     *
     * @param originalAdmin 传入原始的Admin对象
     * @param authorities   创建角色、权限信息的集合
     */
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        // 调用父类构造器
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
        // 将原始Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    // 对外提供的获取原始Admin对象的getXxx()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
