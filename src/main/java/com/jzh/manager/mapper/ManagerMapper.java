package com.jzh.manager.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 首页模块mapper
 * @author JZH
 *
 */
@Repository("managerMapper")
public interface ManagerMapper {
	
	@Select("select * from rootlogin left join root on rootlogin.rid=root.id where rootlogin.token=#{0}")
	Map<String, Object> queryRootByToken(String token);
	
	
	@Select("select * from "
			+ "(select sum(price) allmoney from `order` where display=0 ) a,"
			+ "(select count(*) alluser from `user` where display=0) b,"
			+ "(select count(*) allorder from `order` where display=0) c,"
			+ "(select count(*) allgoods from `goods` where display=0) d")
	Map<String, Object> queryData();

	@Select("select count(*) num from `order` where display=0 and createtime>#{0} and createtime<#{1}")
	Map<String, Object> queryZhu(long l, long m);

	@Select("select * from "
			+ "(select count(*) baiyin from `user` where type=1 and display=0) a,"
			+ "(select count(*) huangjin from `user` where type=2 and display=0) b,"
			+ "(select count(*) zuanshi from `user` where type=3 and display=0) c")
	Map<String, Object> queryBData();

	@Select("select * from `user` where display=0 GROUP BY id desc LIMIT 0,5")
	List<Map<String, Object>> queryUsers();

	@Select("select * from `order` where display=0 GROUP BY id desc LIMIT 0,5")
	List<Map<String, Object>> queryOrder();

}
