package taskassign2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import newtest1.DataOwner;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月23日  Time: 下午8:03:26   Locate:149
 * <br/>fileName: Task.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这是EDF，DPA，HVF算法的任务类型，其主要是来自于同一用户的同一文件
 * 的数据校验请求。
 */

public class Task implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 任务重的数据块对应的文件名
	 */
	public String fileName;
	
	/**
	 * 数据所有者
	 */
	public DataOwner owner;
	
	/**
	 * 任务重所包含的数据块索引
	 */
//	public List<Integer> Indexs;
	
	/**
	 * 任务包含的数据块
	 */
	public List<VerifyBlock> blocks;
	
	/**
	 * 任务中所有数据块的综合为任务的价值
	 */
	public int value;
	
	/**
	 * 任务的最终截至时间
	 */
	public Date deadLine;
	
	/**
	 * 任务的优先级
	 */
	public double priority;
}

