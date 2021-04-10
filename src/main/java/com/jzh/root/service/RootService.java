package com.jzh.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.root.mapper.RootMapper;
import com.jzh.root.pojo.Root;

@Service("rootService")
public class RootService {

	@Autowired
	private RootMapper rootMapper;
	
	/**
	 * 登录
	 * @param rootname
	 * @param password
	 * @return
	 */
	public Root queryRootByNameAndPass(String rootname, String password) {
		return rootMapper.queryByNameAndPass(rootname,password);
	}
	
	/**
	 * 更新token
	 * @param token
	 * @param id
	 */
	public void updateToken(String token, int id) {
		rootMapper.updateToken(token,id,System.currentTimeMillis()+"");
	}
	
}
