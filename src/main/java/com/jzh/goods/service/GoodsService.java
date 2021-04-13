package com.jzh.goods.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.goods.mapper.GoodsMapper;


/**
 * 商品中心mc
 * @author wangjin
 *
 */
@Service("goodsService")
public class GoodsService {
	
	@Autowired
	private GoodsMapper goodsMapper;

	public Map<String, Object> queryRootByToken(String token) {
		
		return goodsMapper.queryRootByToken(token);
	}

	public List<Map<String, Object>> queryAllGoods(String id, String name) {
		if (id==null||id.equals("")) {
			id="%%";
		}
		else {
			id= "%"+id+"%";
		}
		
		if (name==null || name.equals("")) {
			name="%%";
		}
		else {
			name="%"+name+"%";
		}
		return goodsMapper.queryAllGoods(id,name);
	}

	public void edit(String id) {
		goodsMapper.edit(id);
	}
}
