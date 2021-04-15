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
	
	/**
	 * 添加
	 * @param name
	 * @param price
	 * @param path
	 * @param string
	 */
	public void add(String name, String price, String path, String time) {
		goodsMapper.add(name,path,price,time);
	}
	
	/**
	 * 根据id查询商品
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryGoodById(String id) {
		return goodsMapper.queryOneById(id);
	}
	
	/**
	 * 更新
	 * @param name
	 * @param price
	 * @param path
	 * @param id 
	 * @param string
	 */
	public void update(String name, String price, String path, String id) {
		goodsMapper.update(name,path,price,id);
	}
	
	/**
	 * 更新
	 * @param name
	 * @param price
	 * @param id
	 */
	public void update2(String name, String price, String id) {
		goodsMapper.update2(name,price,id);
	}

	public List<Map<String, Object>> querySP(String id, String goodname) {
		if (id==null||id.equals("")) {
			id="%%";
		}else {
			id="%"+id+"%";
		}
		if (goodname==null||goodname.equals("")) {
			goodname="%%";
		}else {
			goodname="%"+goodname+"%";
		}
		return goodsMapper.querySp(id,goodname);
	}
}
