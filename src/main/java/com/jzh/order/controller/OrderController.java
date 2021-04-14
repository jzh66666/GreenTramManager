package com.jzh.order.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.manager.mapper.ManagerMapper;
import com.jzh.order.service.OrderService;
import com.jzh.utils.date.DateUtils;

/**
 * 订单模块c层
 * @author JZH
 *
 */
@Controller
@RequestMapping("/order")
@ResponseBody
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ManagerMapper managerMapper;
	
	/**
	 * 查询
	 * @return
	 */
	@RequestMapping("/query")
	public Map<String, Object> query(String token,String phone){
		//json
		Map<String, Object> json = new HashMap<String, Object>();
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token是否正确
		Map<String, Object> root = managerMapper.queryRootByToken(token);
		if(root == null){
			json.put("code", -2);
			json.put("msg", "登录失效");
			return json;
		}
		
		//给phone赋值,用于sql语句
		if(phone == null || phone.equals("")){
			phone = "%%";
		}
		else{
			phone = "%"+phone+"%";
		}
		
		//取值
		//rootinfo
		Map<String, Object> rootinfo = new HashMap<>();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("rootinfo", rootinfo);
		
		//orders
		List<Map<String, Object>> orders = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> temp = orderService.queryAllOrder(phone);
		for (Map<String, Object> map : temp) {
			Map<String, Object> map2 = new HashMap<>();
			map2.put("id", map.get("id"));
			map2.put("gname", map.get("goodname"));
			map2.put("gimg", map.get("goodimg"));
			map2.put("createtime", DateUtils.MillToHourAndMin(map.get("createtime").toString()));
			map2.put("phone", map.get("phone"));
			map2.put("price", "￥"+map.get("price").toString());
			map2.put("count", map.get("count"));
			orders.add(map2);
		}
		json.put("orders", orders);
		return json;
		
	}
	
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/del")
	public Map<String, Object> del(String token,String id){
		//json
		Map<String, Object> json = new HashMap<>();
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token是否正确
		Map<String, Object> root = managerMapper.queryRootByToken(token);
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
		Map<String, Object> order = orderService.queryOrderById(id);
		if(order == null){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//删除
		orderService.delById(id);
		json.put("code", 200);
		json.put("msg", "请求成功");
		return json;
	}
	
	/**
	 * 进入添加页面
	 * @return
	 */
	@RequestMapping("/addpage")
	public Map<String, Object> addpage(String token){
		//json
		Map<String, Object> json = new HashMap<>();
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token是否正确
		Map<String, Object> root = managerMapper.queryRootByToken(token);
		if(root == null){
			json.put("code", -2);
			json.put("msg", "登录失效");
			return json;
		}
		//取值
		//rootinfo
		Map<String, Object> rootinfo = new HashMap<>();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("rootinfo", rootinfo);
		
		//获取电动车列表
		List<Map<String, Object>> goods = new ArrayList<>();
		List<Map<String, Object>> teMaps = orderService.queryAllGoods();
		for (Map<String, Object> map : teMaps) {
			Map<String, Object> map2 = new HashMap<>();
			map2.put("id", map.get("id"));
			map2.put("goodname", map.get("goodname"));
			goods.add(map2);
		}
		json.put("goods", goods);
		return json;
		
	}
	
	
	/**
	 * 价格计算
	 * @return
	 */
	@RequestMapping("/count")
	public Map<String, Object> count(String token,String gid,String phone,String count){
		//json
		Map<String, Object> json = new HashMap<>();
		try {
			//判空
			if(token == null || token.equals("")){
				json.put("code", -2);
				json.put("msg", "非法请求");
				return json;
			}
			//判断token是否正确
			Map<String, Object> root = managerMapper.queryRootByToken(token);
			if(root == null){
				json.put("code", -2);
				json.put("msg", "登录失效");
				return json;
			}
			double zkeKou = 1;//折扣
			double price = 0;
			if(count == null || count.equals("")){
				count="0";
			}
			
			//根据会员获取折扣
			Map<String, Object> user = orderService.queryUserByPhone(phone);
			if(user != null){
				if(user.get("type").toString().equals("1")){//白银会员
					zkeKou = 0.95;
				}
				else if(user.get("type").toString().equals("2")){//钻石会员
					zkeKou = 0.9;
				}
				else{
					zkeKou = 0.8;
				}
			}
			//商品价格
			Map<String, Object> good = orderService.queryGoodById(gid);
			if(good != null){
				price = Double.parseDouble(good.get("price").toString());
			}
			//计算
			double allprice = Double.parseDouble(((price*zkeKou)*Integer.parseInt(count))+"");
			
			json.put("code", 200);
			json.put("msg", "请求成功");
			Map<String, Object> data = new HashMap<>();
			data.put("price", price);
			data.put("cut", zkeKou==1?"暂无折扣":zkeKou);
			data.put("allprice", allprice);
			json.put("data", data);
		} catch (NumberFormatException e) {
			json.put("code", -1);
			json.put("msg", "请输入正确数量");
			return json;
		}
		return json;
	}
	
	/**
	 * 下单
	 * @return
	 */
	@RequestMapping("/add")
	public Map<String, Object> add(String token,String gid,String phone,String count,String price){
		//json
		Map<String, Object> json = new HashMap<>();
		//判空
		if(token == null || token.equals("")){
			json.put("code", -2);
			json.put("msg", "非法请求");
			return json;
		}
		//判断token是否正确
		Map<String, Object> root = managerMapper.queryRootByToken(token);
		if(root == null){
			json.put("code", -2);
			json.put("msg", "登录失效");
			return json;
		}
		if(gid == null || gid.equals("")){
			json.put("code", -1);
			json.put("msg", "请选择商品");
			return json;
		}
		if(phone == null || phone.equals("")){
			json.put("code", -1);
			json.put("msg", "请输入顾客手机号");
			return json;
		}
		if(count == null || count.equals("")){
			json.put("code", -1);
			json.put("msg", "请输入购买数量");
			return json;
		}
		if(price == null || price.equals("")){
			json.put("code", -1);
			json.put("msg", "请求异常，请尝试刷新");
			return json;
		}
		
		
		
		//下单
		try {
			orderService.add(gid,phone,count,price);
			Map<String, Object> user = orderService.queryUserByPhone2(phone);
			if(user == null){
				int type = 0;
				int temp = Integer.parseInt(count);
				if(temp == 1){
					type = 1;
				}
				else if(temp >= 5 ){
					type = 3;
				}
				else if(temp >= 2 ){
					type = 2;
				}
				
				//添加会员
				orderService.addUser(phone,type,count,System.currentTimeMillis()+"");
			}
			else{
				//添加数量
				orderService.updateCount(phone,Integer.parseInt(count));
				//再次获取
				Map<String, Object> user2 = orderService.queryUserByPhone(phone);
				int countTemp = Integer.parseInt(user2.get("count").toString());
				int type = 0;
				if(countTemp == 1){
					type = 1;
				}
				else if(countTemp >= 5 ){
					type = 3;
				}
				else if(countTemp >= 2 ){
					type = 2;
				}
				//更新会员状态
				orderService.updateType(phone,type);
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "请输入正确数量");
			return json;
		}
		json.put("code", 200);
		json.put("msg", "请求成功");
		return json;
	}
	
//	/**
//	 * 下载Excel
//	 * @return
//	 * @throws IOException 
//	 */
//	public Object downloadExcel(String phone,HttpServletResponse response) throws IOException{
//		ExcelUtils excel = new ExcelUtils();
//		List<Map<String, Object>> list_examinee = orderService.queryData(phone);
//		//2.写入excel中
//		//2.1初始化poi的核心类，产生一个工作簿，并创建一个sheet，且命名
//		HSSFWorkbook workbook=new HSSFWorkbook();
//		String sheetname="考生的详细信息";
//		HSSFSheet sheet = workbook.createSheet(sheetname);
//		
//		//2.2设定表头
//		String[] tableTop = { "订单id", "商品名称", "联系方式", "交易金额","性别","提问","答案","专业","身份证号","日期" };
//		String[] columnName = { "examinee_id","classs_id","examinee_name","examinee_pass","examinee_sex","examinee_question","examinee_answer","examinee_specialty","examinee_identity","examinee_time"};
//	    //创建表头
//		HSSFRow row = sheet.createRow(0);//创建第一行
//		for(int i =0;i<tableTop.length;i++)
//		{
//	     row.createCell(i).setCellValue(tableTop[i]);		
//		}
//		
//		//2.3从第二行开始向表格中添加数据
//		for(int i =0;i<list_examinee.size();i++)
//		{
//	     HSSFRow row2 = sheet.createRow(i+1);
//	 	 sheet.autoSizeColumn(i, true);//poi自带的解决表格中的数据自动适配宽度（对中文不太好使）
//		 Map<String, Object> map = list_examinee.get(i);//取出list_examinee中的map,其实就是数据库表中的一行数据	
//		
//		  for(int k=0;k<columnName.length;k++)
//		  {
//			  row2.createCell(k).setCellValue((String)map.get(columnName[k]));
//		  }
//		}
//		excel.setColumnAutoAdapter(sheet, list_examinee.size());
//        //通过流写入到工作簿中
//        OutputStream out = response.getOutputStream();
//        workbook.write(out);
//        out.close();
//	
//		
//	}
//	
	
}
