package com.jzh.log.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("logMapper")
public interface LogMapper {
	@Select("select * from  `rootlogin` left join `root` on root.id=rootlogin.rid where rootlogin.token=#{0}")
	Map<String, Object> queryAllByToken(String token);

	@Select("select * from `root`")
	List<Map<String, Object>> queryAllLog();
}
