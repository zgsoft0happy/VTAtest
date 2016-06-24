package taskassign2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import newtest1.DataOwner;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月23日  Time: 下午7:55:28   Locate:149
 * <br/>fileName: Test1.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class Test1 implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static List<VerifyBlock>	blockQueue = new ArrayList<>();
	public static List<Task> taskQueue = new ArrayList<>();
	public static List<DataOwner> owner = new ArrayList<>();
	
	
	
	public static void test(){
		
	}
	
	public static void main(String[] args) {
		test();
	}
}

