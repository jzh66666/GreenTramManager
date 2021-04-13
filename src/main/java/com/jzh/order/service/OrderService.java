package com.jzh.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.order.mapper.OrderMapper;

/**
 * 订单模块M层
 * @author JZH
 *
 */
@Service("orderService")
public class OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 查询所有订单
	 * @param phone 
	 * @return
	 */
	public List<Map<String, Object>> queryAllOrder(String phone) {
		return orderMapper.queryAllOrder(phone);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delById(String id) {
		orderMapper.delById(id);
	}
	
	/**
	 * 根据id查询订单
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryOrderById(String id) {
		return orderMapper.queryOneById(id);
	}
	
	/**
	 * 查询所有电车
	 * @return
	 */
	public List<Map<String, Object>> queryAllGoods() {
		return orderMapper.queryAllGoods();
	}

	/**
	 * 根据phone查询会员
	 * @param phone 
	 * @return
	 */
	public Map<String, Object> queryUserByPhone(String phone) {
		return orderMapper.queryUserByPhone(phone);
	}
	
	/**
	 * 根据id查询商品
	 * @param gid
	 * @return
	 */
	public Map<String, Object> queryGoodById(String gid) {
		return orderMapper.querGoodById(gid);
	}
	
	/**
	 * 添加订单
	 * @param gid
	 * @param phone
	 * @param count
	 * @param price
	 */
	public void add(String gid, String phone, String count, String price) {
		int id = Integer.parseInt(gid);
		double p = Double.parseDouble(price);
		int c = Integer.parseInt(count);
		orderMapper.add(id,phone,c,p,System.currentTimeMillis()+"");
	}
	
	/**
	 * 添加会员
	 * @param phone
	 * @param type
	 * @param count
	 * @param string
	 */
	public void addUser(String phone, int type, String count, String time) {
		orderMapper.addUser(phone,type,count,time);
	}
	
	/**
	 * 添加数量
	 * @param phone
	 * @param parseInt
	 */
	public void updateCount(String phone, int parseInt) {
		orderMapper.updateCount(parseInt,phone);
	}
	
	/**
	 * 更新会员等级
	 * @param phone
	 * @param type
	 */
	public void updateType(String phone, int type) {
		orderMapper.updateUserType(phone,type);
	}
	
	/**
	 * 根据phone查询user
	 * @param phone
	 * @return
	 */
	public Map<String, Object> queryUserByPhone2(String phone) {
		return orderMapper.queryUserByPhone2(phone);
	}
	
	
}
