package com.jzh.staff.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 员工模块 mapper
 * @author wangjin
 *
 */
@Repository("staffMapper")
public interface StaffMapper {
	@Select("select * from `rootlogin` left join `root` on root.id=rootlogin.rid where rootlogin.token=#{0}")
	Map<String, Object> queryRootByToken(String token);
	
	
	@Select("select * from `staff` where display=0")
	List<Map<String, Object>> queryAllStaff();

}
