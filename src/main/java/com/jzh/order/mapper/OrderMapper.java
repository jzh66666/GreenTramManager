package com.jzh.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 订单模块mapper
 * @author JZH
 *
 */
@Repository("orderMapper")
public interface OrderMapper {
	
	@Select("select * from `order` left join `goods` on order.gid=goods.id where order.phone like #{0} and order.display=0")
	List<Map<String, Object>> queryAllOrder(String phone);
	
	@Update("update `order` set display=1 where id=#{0}")
	void delById(String id);
	
	@Select("select * from `order` where id=#{0} and display=0")
	Map<String, Object> queryOneById(String id);

	@Select("select * from `goods` where display=0")
	List<Map<String, Object>> queryAllGoods();
	
	@Select("select * from `user` where phone=#{0} and display=0")
	Map<String, Object> queryUserByPhone(String phone);
	
	@Select("select * from `goods` where id=#{0}")
	Map<String, Object> querGoodById(String gid);
	
	@Insert("insert into `order`(gid,phone,price,count,createtime) values(#{0},#{1},#{3},#{2},#{4})")
	void add(int id, String phone, int c, double p, String time);
	
	@Insert("insert into `user`(phone,type,count,createtime) values(#{0},#{1},#{2},#{3})")
	void addUser(String phone, int type, String count, String time);

	@Update("update `user` set count=count+#{0},display=0 where phone=#{1}")
	void updateCount(int parseInt, String phone);
	
	@Update("update `user` set type=#{1} where phone=#{0}")
	void updateUserType(String phone, int type);
	
	@Select("select * from `user` where phone=#{0}")
	Map<String, Object> queryUserByPhone2(String phone);
	
	

	
	
	
}
