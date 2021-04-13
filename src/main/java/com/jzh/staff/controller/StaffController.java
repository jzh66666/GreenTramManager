package com.jzh.staff.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.staff.service.StaffService;

/**
 * 员工模块 c层 
 * @author wangjin
 *
 */
@Controller //标注c层
@RequestMapping("/staff")
@ResponseBody
public class StaffController {
	
	@Autowired
	private StaffService staffService;
	
	
	/**
	 * 查询员工
	 * @return
	 */
	@RequestMapping("/query")
	public Map<String, Object> query( String token){
		//json容器
		Map<String, Object> json=new HashMap();
		//判空
		if (token ==null|| token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object> root=staffService.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
			return json;
		}
		List<Map<String, Object>> data=new ArrayList();
		List<Map<String, Object>> list=staffService.queryAllStaff();
		for (Map<String, Object> map : list) {
			Map<String, Object> map2=new HashMap();
			map2.put("id", map.get("id"));
			map2.put("name", map.get("name"));
			map2.put("price", map.get("gongzi"));	
			data.add(map2);
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		Map<String, Object> rootinfo=new HashMap();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("rootinfo", rootinfo);
		json.put("data", data);
		return json;
	}
	
	/**
	 * 添加页面
	 * @param token
	 * @return
	 */
	@RequestMapping("/addpage")
	public Map<String, Object> addpage ( String token){
		Map<String, Object> json=new HashMap();
		
		// 判空
		if (token == null || token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object>root=staffService.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
			return json;
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		Map<String, Object> rootinfo=new HashMap();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("rootinfo", rootinfo);
		return json;
	}
	/**
	 * 员工添加
	 * @return
	 */
	@RequestMapping("/add")
	public Map<String, Object> add(String token, String name, String gongzi) {
		Map<String, Object> json = new HashMap();
		
		// 判空
		if (token == null || token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object>root=staffService.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
			return json;
		}
		if (name==null||name.equals("")) {
			json.put("code", -1);
			json.put("msg", "请输入姓名");
			return json;
		}
		if (gongzi==null||gongzi.equals("")) {
			json.put("code", -1);
			json.put("msg", "请输入工资");
			return json;
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		
		try {
			staffService.addstaff(name,gongzi);
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "请输入正确数值");
			return json;
		}
		
		return json;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/del")
	public Map<String, Object>edit(String token,String id){
		Map<String, Object> json=new HashMap();
		
		
		// 判空
		if (token == null || token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object>root=staffService.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
			return json;
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		staffService.edit(id);
		return json;
		
	}

}
