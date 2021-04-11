package com.jzh.manager.controller;

import java.text.ParseException;
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
import com.jzh.utils.date.DateUtils;

/**
 * 首页模块
 * @author JZH
 *
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	
	/**
	 * 首页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/head")
	public Map<String, Object> head(HttpServletRequest request){
		//json
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			//接值
			String token = request.getParameter("token");
			//判断
			if(token == null || token.equals("")){
				json.put("code", "-2");
				json.put("msg", "非法请求");
				return json;
			}
			//查询token是否存在
			Map<String, Object> root = managerService.queryRootByToken(token);
			System.out.println("root----->"+root);
			if(root == null){
				json.put("code", "-2");
				json.put("msg", "登录失效");
				return json;
			}
			json.put("code", 200);
			json.put("msg", "请求成功");
			//rootinfo
			Map<String, Object> rootinfo = new HashMap<String, Object>();
			rootinfo.put("rootname", root.get("rootname").toString());//rootname
			json.put("rootinfo", rootinfo);
			
			//4个信息
			Map<String, Object> headData = managerService.queryData(); 
			json.put("allmoney", "￥"+headData.get("allmoney").toString());
			json.put("alluser", headData.get("alluser"));
			json.put("allorder", headData.get("allorder"));
			json.put("allgoods", headData.get("allgoods"));
			
			//echarts
			Map<String, Object> zhu = new HashMap<String, Object>();//zhu
			List<String> date = new ArrayList<String>();//date
			List<String> num = new ArrayList<String>();//num
			//获取今天0点
			String nowTemp = DateUtils.MillToYYMMDD(System.currentTimeMillis()+"");
			String now = nowTemp+" 00:00:00";
			System.out.println(now);
			long zero = DateUtils.dateToMill(now);//现在0点
			long after = zero-604800000;//7天前
			for(long i = 0 ; i < 7 ; i++){
				//每天的信息
				Map<String, Object> data = managerService.queryZhu(after+(86400000*i),after+(86400000*(i+1)));
				date.add(DateUtils.MillToYYMMDD(after+(86400000*i)+""));//日期
				num.add(data.get("num")+"");//订单数量
			}
			zhu.put("date", date);
			zhu.put("num", num);
			json.put("zhu", zhu);
			
			//饼状图
			Map<String, Object> bing = new HashMap<String, Object>();//bing
			Map<String, Object> bData = managerService.queryBingData();
			bing.put("baiyin", bData.get("baiyin"));
			bing.put("huangjin", bData.get("huangjin"));
			bing.put("zuanshi", bData.get("zuanshi"));
			json.put("bing", bing);
			
			//最近加入的会员
			List<Map<String, Object>> users = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> list = managerService.queryUsers();
			for (Map<String, Object> map : list) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("id", map.get("id").toString());
				map2.put("phone", map.get("phone").toString());
				map2.put("createtime", DateUtils.MillToHourAndMin(map.get("createtime").toString()));
				//type
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
			
			
			//order
			List<Map<String, Object>> order = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> orderTemp = managerService.queryOrder();
			
			for (Map<String, Object> map : orderTemp) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("id", map.get("id").toString());
				map2.put("createtime", DateUtils.MillToHourAndMin(map.get("createtime").toString()));
				map2.put("price", "￥"+map.get("price").toString());
				map2.put("phone", map.get("phone").toString());
				order.add(map2);
			}
			json.put("order", order);
			
		} catch (Exception e) {
			e.printStackTrace();
			json.clear();
			json.put("code", -1);
			json.put("msg", "请求异常");
			return json;
		}
		
		return json;
	}
	
	
	
	
}
