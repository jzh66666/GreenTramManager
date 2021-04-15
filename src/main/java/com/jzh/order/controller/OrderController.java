package com.jzh.order.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.swing.event.CellEditorListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jzh.manager.mapper.ManagerMapper;
import com.jzh.order.mapper.OrderMapper;
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
		Map<String, Object> rootinfo = new HashMap();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("rootinfo", rootinfo);
		
		//orders
		List<Map<String, Object>> orders = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> temp = orderService.queryAllOrder(phone);
		for (Map<String, Object> map : temp) {
			Map<String, Object> map2 = new HashMap();
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
		Map<String, Object> json = new HashMap();
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
		Map<String, Object> json = new HashMap();
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
		Map<String, Object> rootinfo = new HashMap();
		rootinfo.put("rootname", root.get("rootname"));
		json.put("code", 200);
		json.put("msg", "请求成功");
		json.put("rootinfo", rootinfo);
		
		//获取电动车列表
		List<Map<String, Object>> goods = new ArrayList();
		List<Map<String, Object>> teMaps = orderService.queryAllGoods();
		for (Map<String, Object> map : teMaps) {
			Map<String, Object> map2 = new HashMap();
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
		Map<String, Object> json = new HashMap();
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
			Map<String, Object> data = new HashMap();
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
		Map<String, Object> json = new HashMap();
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
	
	
	/**
	 * Excel导出
	 * @return 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("excel")
	public Map<String, Object> Excel(String token,String phone,HttpServletResponse respon) throws UnsupportedEncodingException {
		//验证token
		Map<String, Object> json=new HashMap<String, Object>();
		if (token==null||token.equals("")) {
			json.put("code", -2);
			json.put("msg", "非法请求");
		}
		Map<String, Object> root=managerMapper.queryRootByToken(token);
		if (root==null) {
			json.put("code", -2);
			json.put("msg", "登陆失效");
		}
		json.put("code", 200);
		json.put("msg", "登陆成功");
	
		//固定写法 这里写导出Excel的名字设置下载在浏览器端 ，等用户下载
		String fileName="dingdan.xls";
		respon.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("UTF-8"),"UTF-8"));
		respon.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");
		respon.setHeader("Cache-Control", "no-cache");//设置头
		respon.setDateHeader("Expires", 0);//设置
		//获取数据库查询到的所有数据
		List<Map<String, Object>>list=orderService.queryPhone(phone);
		System.out.println(list.size());
		//讲查询结果带回到页面，回显函数使用
		Map<String, Object> map=new HashMap<String, Object>();
		//创建导出表格的对象
		Workbook wb=new HSSFWorkbook();
		//	创建表
		Sheet sheet=wb.createSheet("dingdan1");
		//获取表的第一行元素也就是0行
		Row row=sheet.createRow(0);
		//创建存放列的数组
		Cell[]cell=new HSSFCell[7];
		for(int i=0;i<cell.length;i++) {
			//把每一列存放到数组中
			cell[i]=row.createCell(i);
		}
		//这个是写的标题头
		//给第0行第一列元素赋值
		cell[0].setCellValue("id");
		//给第0行第二列元素赋值
		cell[1].setCellValue("商品");
		//给第0行第三列元素赋值
		cell[2].setCellValue("交易日期");
		//给第0行第四列元素赋值
		cell[3].setCellValue("顾客联系方式");
		//给第0行第五列元素赋值
		cell[4].setCellValue("交易金额");
		//给第0行第六列元素赋值
		cell[5].setCellValue("交易数量");
		
		try {
			//循环获取从数据库中的集合每个pojo对象的数据
			for (int i = 0; i <list.size(); i++) {
				//查询的每个对象的数据
				Map<String, Object> map2=list.get(i);
				//设置要插入的行为i+1（就是标题的下一行）
				Row row1=sheet.createRow(i+1);
				//创建存放列的数组
				Cell[]cell2=new HSSFCell[7];
				for (int j = 0; j < cell.length; j++) {
					//把每一列放到数组中
					cell2[j]=row1.createCell(j);
				}
				cell2[0].setCellValue(i+1);
				cell2[1].setCellValue(map2.get("goodname").toString());
				cell2[2].setCellValue(DateUtils.MillToHourAndMin(map2.get("createtime").toString()));
				cell2[3].setCellValue(map2.get("phone").toString());
				cell2[4].setCellValue(map2.get("price").toString());
				cell2[5].setCellValue(map2.get("count").toString());
			}
			//输出到下载人的电脑上
			wb.write(respon.getOutputStream());
			//刷新
			respon.getOutputStream().flush();
			//关闭
			respon.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
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
