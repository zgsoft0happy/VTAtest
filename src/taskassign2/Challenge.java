package taskassign2;

import java.io.Serializable;
import java.util.Map;

import it.unisa.dia.gas.jpbc.Element;
import newtest1.DataOwner;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 下午1:52:01   Locate:149
 * <br/>fileName: Challenge.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这是对应于EDF , DPA , HVF算法的挑战
 */

public class Challenge implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 对应文件名
	 */
	public String fileName;
	
	/**
	 * 对应的数据所有者
	 */
	public DataOwner owner;
	
	/**
	 * 对应的数据校验者，即挑战者
	 */
	public DataOwner verifier;
	
	/**
	 *  对应的任务
	 */
	public Task task;
	
	/**
	 * 对应产生的挑战
	 */
	public Map<VerifyBlock, Element> chal;
}

