package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {
        //1.查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRoleList(adminId);

        //2.查询未分配角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRoleList(adminId);

        //将查询的结果放入模型，本质上是request.setAttribute("attributeName",object);
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);

        return "assign-role";
    }

    @RequestMapping(value = "/assign/do/role/assign.html",method = RequestMethod.POST)
    public String doAssignRolePage(
        @RequestParam("adminId") Integer adminId,
        @RequestParam("pageNum") Integer pageNum,
        @RequestParam("keyword") String keyword,

        // 我们允许用户在页面上取消所有已分配角色再提交表单，所以可以不提供roleIdList请求参数
        // 设置required=false表示这个请求参数不是必须的
        @RequestParam(value="roleIdList", required=false) List<Integer> roleIdList
    ){
        //为什么是adminService呢，因为inner_admin_role是中间表，按照开发的经验没有对应的实体java类，所以随机选一个
        adminService.saveAdminRoleRelationship(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

//    @RequestMapping(value = "/assgin/get/all/auth.json",method = RequestMethod.POST)
//    public void getAllAuth(HttpServletResponse response) throws IOException {
//        List<Auth> allAuth = authService.getAllAuth();
//
//        Gson gson = new Gson();
//        String json = gson.toJson(ResultEntity.successWithData(allAuth));
//        response.getWriter().write(json);
//    }

    @ResponseBody
    @RequestMapping(value = "/assgin/get/all/auth.json",method = RequestMethod.POST)
    public ResultEntity<List<Auth>> getAllAuth(HttpServletResponse response) throws IOException {
        List<Auth> allAuth = authService.getAllAuth();
        return ResultEntity.successWithData(allAuth);
    }

    @ResponseBody
    @PostMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId") Integer roleId
    ){
        List<Integer> authList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authList);
    }

    @ResponseBody
    @RequestMapping(value = "/assign/do/role/assign/auth.json",method = RequestMethod.POST)
    public ResultEntity<String> saveRoleAuthRelathinship(
//            @RequestBody String requestBody
            @RequestBody Map<String,List<Integer>> map
    ){
//        Gson gson = new Gson();
//        Map map = gson.fromJson(requestBody, Map.class);
        authService.saveRoleAuthRelationship(map);

        return ResultEntity.successWithoutData();
    }
}
