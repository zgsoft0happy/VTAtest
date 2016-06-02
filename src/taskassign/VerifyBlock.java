package taskassign;

import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月30日  Time: 下午3:34:39   Locate:149
 * <br/>fileName: VerifyBlock.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：校验数据块，主要是指定数据块校验时用到的信息。
 */

public class VerifyBlock implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private String filename;	//文件名。
	private int index;			//文件块索引。
	private int worth;			//数据块的价值，用户自己设定。
	private int ownerId;		//请求校验的用户ID
	private long time;			//校验可用时间。毫秒级。
	private double priority;	//数据块的优先级，初始值默认为0。
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @return the worth
	 */
	public int getWorth() {
		return worth;
	}
	/**
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}

	
	
	
	
	
	
	
	
	
	
	
}

