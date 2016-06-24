package taskassign2;

import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import newtest1.DataOwner;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 上午11:32:00   Locate:149
 * <br/>fileName: fileData.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这是数据文件对应的分块数据元素
 */

public class fileData implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * 对应的文件名。
	 */
	public String fileName;
	
	/**
	 * 文件对应的用户。
	 */
	public DataOwner owner;
	
	/**
	 * 数据文件对应的数据块在域里边的数据。
	 */
	public Element[] blocks;
	
	/**
	 * 数据块对应的域。
	 */
	public Field Zn;
}

