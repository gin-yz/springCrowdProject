package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    //接收ajax请求，返回模糊查询相关Role信息
    @ResponseBody
    @RequestMapping(value = "/role/get/page/info.json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        //调用RoleService的getPageInfo方法
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
        //请求封装到ResultEntity
        //注意，这里直接返回到是正确响应的情况，如果发生了异常，那么会抛出异常，需要在@ControllerAdvice中定义相关异常的处理，
        //使得也是返回的ajax信息
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("/role/to/page.html")
    public String toRolePage() {
        return "role-page";
    }

    //保存ajax请求过来的role
    @ResponseBody
    @RequestMapping(value = "/role/save.json", method = RequestMethod.POST)
    public ResultEntity<String> addRole(@RequestParam("name") String roleName) {
        roleService.addRole(roleName);
        return ResultEntity.successWithoutData();
    }

    //更新角色name
    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role) {

        roleService.updateRole(role);

        return ResultEntity.successWithoutData();
    }

    //请求使用json封装，用@RequestBody
    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResultEntity<String> removeByRoleIdAarry(@RequestBody List<Integer> roleIdList) {

        roleService.removeRole(roleIdList);

        return ResultEntity.successWithoutData();
    }
}
