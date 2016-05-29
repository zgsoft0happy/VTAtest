package newtest1;

import java.io.Serializable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月28日  Time: 下午7:10:55   Locate:149
 * <br/>fileName: PublicMeta.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class PublicMeta implements Serializable {

	public static final long serialVersionUID = 1L;
	private Element gen1;
	private Element gen2;
//	private Pairing pairing;
	private Element pk;
	public PublicMeta(Element gen1, Element gen2, Element pk) {
		super();
		this.gen1 = gen1;
		this.gen2 = gen2;
		this.pk = pk;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the gen1
	 */
	public Element getGen1() {
		return gen1;
	}
	/**
	 * @return the gen2
	 */
	public Element getGen2() {
		return gen2;
	}
	/**
	 * @return the pk
	 */
	public Element getPk() {
		return pk;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PublicMeta [gen1=" + gen1 + ", gen2=" + gen2 + ", pk=" + pk + "]";
	}
	
	
}

