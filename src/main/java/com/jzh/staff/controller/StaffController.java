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

}
