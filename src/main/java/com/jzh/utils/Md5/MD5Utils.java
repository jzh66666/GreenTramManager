package com.jzh.utils.Md5;
/**
 * @desc   MD5工具类
 * @author JZH
 * @time   2020-12-20
 */


public class MD5Utils {
	//MD5
	private static  md5;
	/**
	 * 初始化MD5对象
	 */
	static{
		md5 = new Mademd5();
	}
	/**
	 * 将字符串md5加密
	 * @param message
	 * @return
	 */
	public static String makeMD5(String message){
		return md5.toMd5(message);
	} 
}
