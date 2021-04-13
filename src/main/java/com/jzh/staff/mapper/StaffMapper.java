package com.jzh.staff.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

	@Insert("insert into `staff`(name,gongzi,createtime) values(#{0},#{1},#{2})")
	void addstaff(String name, String gongzi, String string);

	@Update("update `staff` set display=1 where id=#{0}")
	void edit(String id);

}
