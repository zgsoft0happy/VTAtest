package taskassign;
import java.io.Serializable;

import newtest1.DataOwner;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月30日  Time: 下午4:04:50   Locate:149
 * <br/>fileName: Verifier.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：校验者，主要是校验者的角色，用户加入校验组之后，即为校验者。
 */

public class Verifier implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private DataOwner verifier;		//校验者，用户。
	private int remainNum;			//需要校验的数据块
	private long remainTime;		//接受自己校验数据的截止时时间之前剩余的时间。
}

