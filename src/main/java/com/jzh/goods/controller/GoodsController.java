package com.jzh.goods.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.goods.service.GoodsService;

/**
 * 商品中心c层
 * @author wangjin
 *
 */
@Controller//标注c层
@RequestMapping("/goods")
@ResponseBody
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	/**
	 * 	查询
	 * @param token
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/query")
	public Map<String, Object> query( String token,String id,String name){
		//json容器	
		Map< String, Object> json=new HashMap();
		if (token ==null|| token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		Map<String, Object> root=goodsService.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
			return json;
		}
		
		
		List<Map<String, Object>> data=new ArrayList();		
		List<Map<String, Object>> list=goodsService.queryAllGoods(id,name);
		for (Map<String, Object> map : list) {
			Map<String, Object> map2=new HashMap();
			map2.put("id", map.get("id"));
			map2.put("goodname", map.get("goodname"));
			map2.put("goodimg", map.get("goodimg"));
			map2.put("createtime", map.get("createtime"));
			map2.put("price", map.get("price"));
			data.add(map2);
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("data", data);
		return json;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/edit")
	public Map<String, Object> edit(String token,String id){
		Map<String, Object> json=new HashMap();
		// 判空
				if (token == null || token.equals("")) {
					json.put("code", -2);
					json.put("msg", "非法请求");
					return json;
				}
				Map<String, Object>root=goodsService.queryRootByToken(token);
				if (root==null) {
					json.put("code", -2);
					json.put("msg", "登陆失效");
					return json;
				}
				json.put("code", 200);
				json.put("msg", "请求成功");
				goodsService.edit(id);
				return json;
	}
	
	//进入添加页面
		@RequestMapping("/addpage")
		public Map<String, Object> addpage ( String token){
			Map<String, Object> json=new HashMap();
			
			// 判空
			if (token == null || token.equals("")) {
				json.put("code", -2);
				json.put("msg", "非法请求");
				return json;
			}
			Map<String, Object>root=goodsService.queryRootByToken(token);
			if (root==null) {
				json.put("code", -2);
				json.put("msg", "登陆失效");
				return json;
			}
			json.put("code", 200);
			json.put("msg", "请求成功");
			return json;
		}
		
		
		//进入修改页面
		@RequestMapping("/updatepage")
		public Map<String, Object> updatepage ( String token){
			Map<String, Object> json=new HashMap();
			
			// 判空
			if (token == null || token.equals("")) {
				json.put("code", -2);
				json.put("msg", "非法请求");
				return json;
			}
			Map<String, Object>root=goodsService.queryRootByToken(token);
			if (root==null) {
				json.put("code", -2);
				json.put("msg", "登陆失效");
				return json;
			}
			json.put("code", 200);
			json.put("msg", "请求成功");
			return json;
		}
}
