package com.snail.ssip.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snail.util.StringUtils;

@Controller
@RequestMapping("/test")
public class TestCtrl {
	
//	private static Logger LOG = Logger.getLogger("MailLog");
	
	private String rootMenusStr = "[{"
			+ "menuId:'1',"
			+ "menuName:'测试系统1',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'',"
			+ "leaf:false,"
			+ "menuIndex:1"
			+ "},{"
			+ "menuId:'2',"
			+ "menuName:'测试系统2',"
			+ "menuDesc:'这是第二个测试系统',"
			+ "icon:'',"
			+ "leaf:false,"
			+ "menuIndex:2"
			+ "},{"
			+ "menuId:'3',"
			+ "menuName:'测试系统3',"
			+ "menuDesc:'这是第三个测试系统',"
			+ "icon:'',"
			+ "leaf:false,"
			+ "menuIndex:3"
			+ "}]";

	private String menusStr = "[{"
			+ "menuId:'11',"
			+ "menuName:'测试系统11',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_node.png',"
			+ "leaf:false,"
			+ "menuIndex:1"
			+ "},{"
			+ "menuId:'12',"
			+ "menuName:'测试系统12',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_node.png',"
			+ "leaf:false,"
			+ "menuIndex:2"
			+ "},{"
			+ "menuId:'13',"
			+ "menuName:'测试系统13',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_node.png',"
			+ "leaf:false,"
			+ "menuIndex:3"
			+ "}]";
	private String menusStr1 = "[{"
			+ "menuId:'111',"
			+ "menuName:'测试系统111',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_leaf.png',"
			+ "leaf:true,"
			+ "url:'/pages/testPanel',"
			+ "menuIndex:1"
			+ "},{"
			+ "menuId:'112',"
			+ "menuName:'测试系统112',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_leaf.png',"
			+ "leaf:true,"
			+ "url:'/pages/testPanel',"
			+ "menuIndex:2"
			+ "},{"
			+ "menuId:'113',"
			+ "menuName:'测试系统113',"
			+ "menuDesc:'这是第一个测试系统',"
			+ "icon:'default_tree_leaf.png',"
			+ "url:'/pages/test_testPanel',"
			+ "leaf:true,"
			+ "menuIndex:3"
			+ "}]";
	
	@RequestMapping("test")
	@ResponseBody
	public String test() {
		return "hello!";
	}

	@RequestMapping("getMenus")
	@ResponseBody
	public String getMenus(String menuId) {
		if(StringUtils.isBlank(menuId) || menuId.equalsIgnoreCase("root")){
			return rootMenusStr;
		}
		if(menuId.equalsIgnoreCase("1")){
			return menusStr;
		}
		if(menuId.equalsIgnoreCase("11")){
			return menusStr1;
		}
		return "";
	}
}
