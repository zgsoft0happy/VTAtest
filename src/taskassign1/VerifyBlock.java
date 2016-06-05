package taskassign1;

import java.io.Serializable;
import java.util.Date;

import newtest1.DataOwner;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月3日  Time: 下午12:33:44   Locate:149
 * <br/>fileName: VerifyBlock.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class VerifyBlock implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private String filename;	//文件名
	private int index;		//块索引
//	private long time;		//截止时间。
	private Date deadline;		//截止时间
	private int value;		//数据块的价值。
	private DataOwner owner;	//所属用户。
	private double priority;	//优先级
	
	public VerifyBlock() {}
	public VerifyBlock(String filename, int index, /*long time*/ Date deadline, int value, DataOwner owner, double priority) {
		super();
		this.filename = filename;
		this.index = index;
//		this.time = time;
		this.deadline = deadline;
		this.value = value;
		this.owner = owner;
		this.priority = priority;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	/**
	 * @return the owner
	 */
	public DataOwner getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(DataOwner owner) {
		this.owner = owner;
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
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

