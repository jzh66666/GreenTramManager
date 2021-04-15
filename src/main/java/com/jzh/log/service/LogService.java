package com.jzh.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jzh.log.mapper.LogMapper;

/**
 *  m层
 * @author wangjin
 *
 */
@Service("/logService")
public class LogService {

	@Autowired
	private LogMapper logMapper;

	public Map<String, Object> queryAllByToken(String token) {
		return logMapper.queryAllByToken(token);
	}

	public List<Map<String, Object>> queryAllLog() {
		
		return logMapper.queryAllLog();
	}
}
