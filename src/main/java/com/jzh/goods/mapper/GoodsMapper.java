package com.jzh.goods.mapper;

import java.util.List;
import java.util.Map;

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
	@Select("select * from goods")
	List<Map<String, Object>> queryAllGoods(String id, String name);

	//删除
	@Update("update `goods` set display=1 where id=#{0}")
	void edit(String id);
	
	

}
