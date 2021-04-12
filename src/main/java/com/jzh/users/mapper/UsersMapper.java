package com.jzh.users.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 会员模块Dao
 * @author JZH
 *
 */
@Repository("usersMapper")
public interface UsersMapper {

	@Select("select * from user where phone like #{0} and display=0")
	List<Map<String, Object>> queryAllUserByPhone(String phone);
	
	@Select("select * from user where id=#{0} and display=0")
	Map<String, Object> queryUserById(String id);
	
	@Update("update user set display=1 where id=#{0}")
	void delById(String id);

}
