package com.jzh.users.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.manager.service.ManagerService;
import com.jzh.users.service.UsersService;
import com.jzh.utils.date.DateUtils;

/**
 * 会员模块Controller
 * @author JZH
 *
 */
@Controller
@RequestMapping("/users")
@ResponseBody
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	@Autowired
	private ManagerService ManagerService;
	
	/**
	 * 查询
	 * @return
	 */
	@RequestMapping("/query")
	public Map<String, Object> query(String token , String phone){
		//json
		Map<String, Object> json= new HashMap();
		
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token
		Map<String, Object> root = ManagerService.queryRootByToken(token);
		if(root == null){
			json.put("code", -2);
			json.put("msg", "登录失效");
			return json;
		}
		//rootinfo
		Map<String, Object> rootinfo = new HashMap<String, Object>();
		rootinfo.put("rootname", root.get("rootname"));
		
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("rootinfo", rootinfo);
		
		//取值
		List<Map<String, Object>> list = usersService.queryAllUser(phone);
		List<Map<String, Object>> users = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("id", map.get("id"));
			map2.put("phone", map.get("phone"));
			map2.put("count", map.get("count"));
			map2.put("createtime", DateUtils.MillToHourAndMin(map.get("createtime").toString()));
			String type = map.get("type").toString();
			if(type.equals("1")){
				map2.put("type", "白银会员");
			}
			if(type.equals("2")){
				map2.put("type", "黄金会员");
			}
			if(type.equals("3")){
				map2.put("type", "钻石会员");
			}
			
			users.add(map2);
		}
		
		json.put("users", users);
		
		return json;
	}
	
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/del")
	public Map<String, Object> del(String token,String id){
		System.out.println(id);
		//json
		Map<String, Object> json = new HashMap<String, Object>();
		
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token
		Map<String, Object> root = ManagerService.queryRootByToken(token);
		if(root == null){
			json.put("code", -2);
			json.put("msg", "登录失效");
			return json;
		}
		if(id == null || id.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object> user = usersService.queryUserById(id);
		if(user == null){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断无误
		//删除
		usersService.delById(id);
		json.put("code", 200);
		json.put("msg", "请求成功");
		return json;
	}
	
	
	
}
