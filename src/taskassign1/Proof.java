package taskassign1;

import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月3日  Time: 下午3:06:06   Locate:149
 * <br/>fileName: Proof.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class Proof implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private Element TP ;
	private Element DP ;
	public Proof(Element tP, Element dP) {
		super();
		TP = tP;
		DP = dP;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the tP
	 */
	public Element getTP() {
		return TP;
	}
	/**
	 * @return the dP
	 */
	public Element getDP() {
		return DP;
	}
	
	
}

