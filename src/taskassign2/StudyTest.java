package taskassign2;

import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import newtest1.DataOwner;
import newtest1.JdbcUtils;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 下午3:01:21   Locate:149
 * <br/>fileName: StudyTest.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：主要是有一些功能不确定，然后再这里测试。
 */

public class StudyTest implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 测试Field中的one和zero
	 * 
	 * @author: YYB
	 * @Time: 下午3:34:24
	 */
	public static void test()
	{
		DataOwner owner = JdbcUtils.getOwnerFromDB(2);
		Field Zn = owner.getPairing().getZr();
		Element one = Zn.newOneElement();
		Element rand = Zn.newRandomElement();
		System.out.println(one);
		System.out.println(rand);
		Element newRand = rand.mul(one);
		System.out.println(newRand);
		System.out.println(newRand.isEqual(rand));
		
		Element zero = Zn.newZeroElement();
		System.out.println(zero);
		Element newRand1 = rand.mul(zero);
		System.out.println(newRand1);
	}
	
	public static void main(String[] args) {
		test();
	}
}

