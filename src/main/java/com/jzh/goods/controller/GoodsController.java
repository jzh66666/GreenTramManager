package com.jzh.goods.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.swing.CellEditor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.IPMT;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
		/**
		 * excel导出
		 * @throws UnsupportedEncodingException 
		 */
		@RequestMapping("excel")
		public Map<String, Object> excel(String token,String id,String goodname,HttpServletResponse response) throws UnsupportedEncodingException{
			//验证token
			Map<String, Object>json=new HashMap<String, Object>();
			if (token==null||token.equals("")) {
				json.put("code", -2);
				json.put("msg", "非法请求");
			}
			Map<String, Object>root=goodsService.queryRootByToken(token);
			if (root==null) {
				json.put("code", -2);
				json.put("msg", "登陆失效");
			}
			json.put("code", 200);
			json.put("msg", "登陆成功");
			//固定写法这里写导出Excel的名字设置下载在浏览器端，等用户下载
			String fileName="shangpin.xls";
			response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("UTF-8"),"UTF-8"));
			response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");//设置头
			response.setDateHeader("Expires", 0);//设置
			//获取数据库查询到的所有数据
			List<Map<String, Object>> list=goodsService.querySP(id,goodname);
			//讲查询结果带回到页面，回显函数使用
			Map<String, Object>map=new HashMap<String, Object>();
			//创建导出表格的对象
			Workbook wb=new HSSFWorkbook();
			//创建表
			Sheet sheet=wb.createSheet("shangpin1");
			//获取表的第一行元素也就是0行
			Row row=sheet.createRow(0);
			//创建存放列的数组
			Cell[]cell=new HSSFCell[5];
			for (int i = 0; i < cell.length; i++) {
				//把每一列存放到数组中
				cell[i]=row.createCell(i);
			}
			//这个是写的标题头
			//给第0行第一列元素赋值
			cell[0].setCellValue("id");
			//给第0行第一列元素赋值
			cell[1].setCellValue("商品名称");
			//给第0行第一列元素赋值
			cell[2].setCellValue("上架日期");
			//给第0行第一列元素赋值
			cell[3].setCellValue("价格");
			
			try {
				//循环获取从数据库中的集合每个pojo对象的数据
				for (int i = 0; i < list.size(); i++) {
					//查询的每个对象的
					Map<String, Object> map2=list.get(i);
					//设置夭插入的行为i+1(就是标题的下一行)
					Row row1=sheet.createRow(i+1);
					//创建存放列的数组
					Cell[]cell2=new HSSFCell[5];
					for (int j = 0; j < cell.length; j++) {
						//把每一列放到数组中
						cell2[j]=row1.createCell(j);
					}
					cell2[0].setCellValue(i+1);
					cell2[1].setCellValue(map2.get("goodname").toString());
					cell2[2].setCellValue(DateUtils.MillToHourAndMin(map2.get("createtime").toString()));
					cell2[3].setCellValue(map2.get("price").toString());
					
				}
				//输出到下载人的电脑上
				wb.write(response.getOutputStream());
				//刷新
				response.getOutputStream().flush();
				//关闭
				response.getOutputStream().close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (wb!=null) {
						//关闭流
						wb.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return map;
			
		}
		
}
