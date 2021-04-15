package com.jzh.goods.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 商品中心mapper
 * @author wangjin
 *
 */

@Repository("goodsMapper")
public interface GoodsMapper {
	//查询token
	@Select("select * from `rootlogin` left join `root` on root.id=rootlogin.rid where rootlogin.token=#{0}")
	Map<String, Object> queryRootByToken(String token);
	
	//查询所有
	@Select("select * from `goods` where id like #{0} and goodname like #{1} and display=0")
	List<Map<String, Object>> queryAllGoods(String id, String name);

	//删除
	@Update("update `goods` set display=1 where id=#{0}")
	void edit(String id);
	
	@Insert("insert into `goods`(goodname,goodimg,price,createtime) values(#{0},#{1},#{2},#{3})")
	void add(String name, String path, String price, String time);
	
	@Select("select * from goods where display=0 and id=#{0}")
	Map<String, Object> queryOneById(String id);
	
	@Update("update `goods` set goodname=#{0},goodimg=#{1},price=#{2} where id=#{3}")
	void update(String name, String path, String price, String id);
	
	@Update("update `goods` set goodname=#{0},price=#{1} where id=#{2}")
	void update2(String name, String price, String id);

	@Select("select * from `goods` where goods.id like #{0} and goods.goodname like #{1}")
	List<Map<String, Object>> querySp(String id, String goodname);
	
	

}
