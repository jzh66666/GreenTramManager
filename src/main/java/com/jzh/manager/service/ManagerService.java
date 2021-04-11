package com.jzh.manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.manager.mapper.ManagerMapper;

/**
 * manager模块M层
 * @author JZH
 *
 */
@Service("managerService")
public class ManagerService {
	
	@Autowired
	private ManagerMapper managerMapper;
	
	/**
	 * 根据token查询root
	 * @param token
	 * @return
	 */
	public Map<String, Object> queryRootByToken(String token) {
		return managerMapper.queryRootByToken(token);
	}
	
	/**
	 * 查询首页头数据
	 * @return
	 */
	public Map<String, Object> queryData() {
		return managerMapper.queryData();
	}
	
	/**
	 * 柱状图信息
	 * @param l
	 * @param m
	 * @return
	 */
	public Map<String, Object> queryZhu(long l, long m) {
		return managerMapper.queryZhu(l,m);
	}
	
	/**
	 * 查询饼状图信息
	 * @return
	 */
	public Map<String, Object> queryBingData() {
		return managerMapper.queryBData();
	}
	
	/**
	 * 查询最近加入的会员
	 * @return
	 */
	public List<Map<String, Object>> queryUsers() {
		return managerMapper.queryUsers();
	}
	
	/**
	 * 查询最近订单
	 * @return
	 */
	public List<Map<String, Object>> queryOrder() {
		return managerMapper.queryOrder();
	}
}
