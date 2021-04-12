package com.jzh.staff.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.staff.mapper.StaffMapper;

/**
 * 员工模块，m层
 * @author wangjin
 *
 */
@Service("staffService")
public class StaffService {

	
	@Autowired
	private StaffMapper staffMapper;

	/**
	 * 根据token查询root
	 * @param token
	 * @return
	 */
	public Map<String, Object> queryRootByToken(String token) {
		
		return  staffMapper.queryRootByToken(token);
	}
/**
 * 获取所有员工的数据
 * @return
 */
	public List<Map<String, Object>> queryAllStaff() {
		
		return staffMapper.queryAllStaff();
	}
}
