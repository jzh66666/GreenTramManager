package com.jzh.utils.qiniu;

import java.util.UUID;

public class SendInfo {

	/**
	 * @desc 返回去掉-的uuid
	 * @return
	 */
	public static String GetUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getTimeMillis() {
		return System.currentTimeMillis() + "";
	}

}
