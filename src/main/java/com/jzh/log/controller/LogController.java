package com.jzh.log.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.log.service.LogService;
import com.jzh.utils.date.DateUtils;

@Controller
@RequestMapping("/log")
@ResponseBody
public class LogController {
	@Autowired
	private LogService logService;
	
	/**
	 * 查询 log
	 * @param token
	 * @return
	 */
	@RequestMapping("/query")
	public Map<String, Object> query(String token){
		Map<String, Object> json=new HashMap<String, Object>();
		if (token==null||token.equals("")) {
			json.put("conde", -2);
			json.put("msg", "非法请求");
		}
		Map<String, Object> root=logService.queryAllByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
		}
		List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list=logService.queryAllLog();
		for (Map<String, Object> map : list) {
			Map<String, Object> map2=new HashMap<String, Object>();
			map2.put("rootname", map.get("rootname"));
			
			map2.put("rootlogtime", DateUtils.CurrentYMDHSMTime());
			data.add(map2);
		}
		json.put("code", 200);
		json.put("msg", "登陆成功");
		json.put("data", data);
		return json;
	}

}
