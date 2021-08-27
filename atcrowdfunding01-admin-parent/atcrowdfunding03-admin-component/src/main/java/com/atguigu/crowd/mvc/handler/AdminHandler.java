package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.springSecurityConfig.SecurityAdmin;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    //管理员登陆，使用SpringSecurity之后这个方法就废了
    @RequestMapping(value = "/admin/do/login.html", method = RequestMethod.POST)
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ) {
        // 调用Service方法执行登录检查
        // 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        // 将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        // 登陆成功后转发到主页面，而不是直接返回主页面，不然到话每次刷新都会提交表单
        // 重定向指的是，这个写的地址是让浏览器重新再找这个路径，所以这个要写浏览器转发到地址
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/")
    public String loginPage() {
        return "admin-main";
    }

    //管理员退出登陆
    @RequestMapping(value = "/admin/do/logout.html", method = RequestMethod.GET)
    public String doLogout(HttpSession session) {
        //强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }


    //查询信息，需要分页，在mybatis的配置文件中需要指定PageHelper过滤器
    @PreAuthorize("hasRole('经理') OR hasAuthority('user')")
    @RequestMapping(value = "/admin/get/page.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String getPageInfo(
            // 使用@RequestParam注解的defaultValue属性，指定默认值，在请求中没有携带对应参数时使用默认值
            // keyword默认值使用空字符串，和SQL语句配合实现两种情况适配
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            // pageNum默认值使用1
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            // pageSize默认值使用5
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            //查询结果需要封装，返回给页面
            ModelMap modelMap
    ) {
        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        // 将PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    //保存用户
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String saveAdminAccount(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    //跳转到修改页面
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ) {
        // 1.根据adminId查询Admin对象
        Admin admin = adminService.getAdminById(adminId);

        // 2.将Admin对象存入模型
        modelMap.addAttribute("admin", admin);
        //modelMap中封装了request，也就是说会把请求的pageNum和keyword转发到admin-edit.jsp
        //在admin-edit.jsp会将pageNum和keyword置hidden，同表单一起提交到/admin/update.html
        return "admin-edit";
    }

    //修改账户属性
    @RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword
    ) {
        //修改账户
        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }


    //删除账户，使用restful的风格
    @RequestMapping("admin/remove/{adminId}/{pageNum}/{keyword}.html")
    //增加权限控制，只有有user:delete权限的用户才能删除
    @PreAuthorize("hasAuthority('user:delete')")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword,
            HttpSession session
    ) {
        //加入了springsecurity中应该这样得到Admin对象
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityAdmin principal = (SecurityAdmin) auth.getPrincipal();
        Admin admin = principal.getOriginalAdmin();
//        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        if (admin.getId().equals(adminId)) {
            throw new RuntimeException("请不要删除自己");
        }
        // 执行删除
        adminService.remove(adminId);
        // 页面跳转：回到分页页面

        // 尝试方案1：直接转发到admin-page.jsp会无法显示分页数据
        // return "admin-page";

        // 尝试方案2：转发到/admin/get/page.html地址，一旦刷新页面会重复执行删除浪费性能
        // return "forward:/admin/get/page.html";

        // 尝试方案3：重定向到/admin/get/page.html地址
        // 同时为了保持原本所在的页面和查询关键词再附加pageNum和keyword两个请求参数
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    //先执行，再判断返回的值的条件，可以直接写逻辑，用处不大
    @ResponseBody
    @PostAuthorize("returnObject.data.loginAcct == principal.username")
    @RequestMapping("/admin/test/post/filter.json")
    public ResultEntity<Admin> getAdminById() {

        Admin admin = new Admin();

        admin.setLoginAcct("adminOperator");

        return ResultEntity.successWithData(admin);
    }

    //判断请求的参数，是将请求的数字中偶数留下了，可以直接写逻辑，用处不大
    @PreFilter(value = "filterObject%2==0")
    @ResponseBody
    @RequestMapping("/admin/test/pre/filter")
    public ResultEntity<List<Integer>> saveList(@RequestBody List<Integer> valueList) {
        return ResultEntity.successWithData(valueList);
    }

}
