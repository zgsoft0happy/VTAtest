package newtest1;

import java.io.Serializable;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月27日  Time: 下午3:20:14   Locate:149
 * <br/>fileName: BaseParams.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class BaseParams implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final int K = 1024;		//文件的长度单位
	public static final int blockSize = K * 32;	//文件块的长度
	
	public static final String paramPath = "params\\user";	//安全参数保存路径
	public static final String pkPath = "pk\\user";			//公开信息保存路径
	public static final String skPath = "sk\\user";			//密钥信息保存路径
	public static final String tagPath = "tag\\tag";			//密钥信息保存路径
}

