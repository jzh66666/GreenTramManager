package com.jzh.root.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.jzh.root.pojo.Root;

/**
 * root模块mapper
 * @author JZH
 *
 */
@Repository("rootMapper")
public interface RootMapper {
	
	@Select("select * from root where rootname=#{0} and password=#{1}")
	Root queryByNameAndPass(String rootname, String password);
	
	@Update("update rootlogin set token=#{0},createtime=#{2} where id=#{1}")
	void updateToken(String token, int id, String string);
	
	
}
