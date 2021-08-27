package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    //得到整个菜单树，返回根结点
    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        //根节点
        Menu root = null;
        //创建临时暂存map
        Map<Integer, Menu> tmpMap = new HashMap<>();

        //从数据库中拿出所有的菜单
        List<Menu> allMenus = menuService.getAll();

        //将其放入map
        for (Menu menu : allMenus) {
            tmpMap.put(menu.getId(), menu);
        }
        //遍历，将所有menu和其父menu相关联
        for (Menu menu : allMenus) {
            Integer parentMenuId = menu.getPid();
            if (parentMenuId == null) {
                root = menu;
                continue;
            }

            Menu parentMenu = tmpMap.get(parentMenuId);
            parentMenu.getChildren().add(menu);

        }

        return ResultEntity.successWithData(root);
    }

    @RequestMapping("/menu/to/page.html")
    public String menuPage(){
        return "menu-page";
    }

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {

        menuService.removeMenu(id);

        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {

        menuService.updateMenu(menu);

        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu) {

        // Thread.sleep(2000);

        menuService.saveMenu(menu);

        return ResultEntity.successWithoutData();
    }

}
