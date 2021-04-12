package com.jzh.root.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.root.pojo.Root;
import com.jzh.root.service.RootService;

/**
 * rootController
 * @author JZH
 *
 */
@Controller
@RequestMapping("/root")
public class RootController {
	
	@Autowired
	private RootService rootService;
	
	/**
	 * 登录模块
	 * @param rootname
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request,String rootname,String password){
		System.out.println("login----->");
		//存放json
		Map<String, Object> json = new HashMap<String, Object>();
		//判空
		if(rootname == null || rootname.equals("")){
			json.put("code", -1);
			json.put("msg", "请输入用户名");
			return json;
		}
		if(password == null || password.equals("")){
			json.put("code", -1);
			json.put("msg", "请输入密码");
			return json;
		}
		
		Root root = rootService.queryRootByNameAndPass(rootname,password);
		if(root == null){
			json.put("code", -1);
			json.put("msg", "用户名或密码错误");
			return json;
		}
		
		
		//更新token
		String token = DigestUtils.md5Hex(rootname+password+request.getSession().getId());
		rootService.updateToken(token,root.getId());
		
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("token",token);
		System.out.println(token);
		return json;
	}
	
}
