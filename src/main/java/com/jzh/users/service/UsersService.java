package com.jzh.users.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.users.mapper.UsersMapper;

/**
 * 会员模块M层
 * @author JZH
 *
 */
@Service("usersService")
public class UsersService {
	
	@Autowired
	private UsersMapper usersMapper;
	
	/**
	 * 查询所有用户
	 * @param phone 
	 * @return
	 */
	public List<Map<String, Object>> queryAllUser(String phone) {
		if(phone == null || phone.equals("")){
			phone="%%";
		}
		else{
			phone="%"+phone+"%";
		}
		return usersMapper.queryAllUserByPhone(phone);
	}
	
	/**
	 * 根据id查询user
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryUserById(String id) {
		return usersMapper.queryUserById(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delById(String id) {
		usersMapper.delById(id);
	}
}
