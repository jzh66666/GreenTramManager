package com.jzh.goods.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jzh.goods.service.GoodsService;
import com.jzh.utils.date.DateUtils;
import com.jzh.utils.qiniu.PutFile;

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
		//rootinfo
		Map<String, Object> rootinfo = new HashMap<>();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("rootinfo", rootinfo);
		
		List<Map<String, Object>> data=new ArrayList();		
		List<Map<String, Object>> list=goodsService.queryAllGoods(id,name);
		for (Map<String, Object> map : list) {
			Map<String, Object> map2=new HashMap();
			map2.put("id", map.get("id"));
			map2.put("goodname", map.get("goodname"));
			map2.put("goodimg", map.get("goodimg"));
			map2.put("createtime",DateUtils.MillToHourAndMin(map.get("createtime").toString()) );
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
	@RequestMapping("/del")
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
			//rootinfo
			Map<String, Object> rootinfo = new HashMap<>();
			rootinfo.put("rootname", root.get("rootname"));
			json.put("rootinfo", rootinfo);
			return json;
		}
		
		/**
		 * 添加
		 * @return
		 * @throws IOException 
		 */
		@RequestMapping("/add")
		public Map<String, Object> add(MultipartFile goodimg , String token,String name,String price) throws IOException{
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
			if(goodimg == null || goodimg.equals("")){
				json.put("code", -1);
				json.put("msg", "请选择图片");
				return json;
			}
			if(name == null || name.equals("")){
				json.put("code", -1);
				json.put("msg", "请输入商品名称");
				return json;
			}
			if(price == null || price.equals("")){
				json.put("code", -1);
				json.put("msg", "请输入商品价格");
				return json;
			}
			
			json.put("code", 200);
			json.put("msg", "请求成功");
			//上传图片到七牛云
			String path = PutFile.Putimgs(goodimg.getInputStream(), goodimg.getContentType());
			//存入数据库
			goodsService.add(name,price,path,System.currentTimeMillis()+"");
			return json;
		}
		
		
		/**
		 * 进入修改页面
		 * @param token
		 * @param id
		 * @return
		 */
		@RequestMapping("/editpage")
		public Map<String, Object> updatepage (String token,String id){
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
			Map<String, Object> rootinfo = new HashMap<>();
			rootinfo.put("rootname", root.get("rootname"));
			json.put("rootinfo", rootinfo);
			
			Map<String, Object> good = goodsService.queryGoodById(id);
			if(good == null){
				json.put("code", -3);
				json.put("msg", "获取失败");
				return json;
			}
			
			json.put("code", 200);
			json.put("msg", "请求成功");
			json.put("good", good);
			return json;
		}
		
		/**
		 * 添加
		 * @return
		 * @throws IOException 
		 */
		@RequestMapping("/edit")
		public Map<String, Object> edit(String id,MultipartFile goodimg , String token,String name,String price) throws IOException{
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
			
			if(id == null || id.equals("")){
				json.put("code", -2);
				json.put("msg", "请求异常");
				return json;
			}
			if(name == null || name.equals("")){
				json.put("code", -1);
				json.put("msg", "请输入商品名称");
				return json;
			}
			if(price == null || price.equals("")){
				json.put("code", -1);
				json.put("msg", "请输入商品价格");
				return json;
			}
			json.put("code", 200);
			json.put("msg", "请求成功");
			if(goodimg == null || goodimg.equals("")){
				goodsService.update2(name,price,id);
				return json;
			}
			//上传图片到七牛云
			String path = PutFile.Putimgs(goodimg.getInputStream(), goodimg.getContentType());
			//存入数据库
			goodsService.update(name,price,path,id);
			
			return json;
			
		}
		
}
