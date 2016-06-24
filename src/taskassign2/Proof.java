package taskassign2;

import java.io.Serializable;
import java.util.List;

import it.unisa.dia.gas.jpbc.Element;
import newtest1.DataOwner;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 下午2:15:49   Locate:149
 * <br/>fileName: Proof.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这是证据类，针对某一个任务和挑战产生的证据。
 */

public class Proof implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 执行任务的校验者
	 */
	public DataOwner verifier;
	
	/**
	 * 证据多对应的任务
	 */
	public Task task;
	
	/**
	 * 证据对应的挑战
	 */
	public Challenge challenge; 
	
	/**
	 * 标签证据
	 */
	public Element TP;
	
	/**
	 * 数据证据
	 */
	public Element DP;
}

