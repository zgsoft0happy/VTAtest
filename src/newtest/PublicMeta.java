package newtest;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月27日  Time: 下午1:50:30   Locate:149
 * <br/>fileName: PublicMeta.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：用户的公开数据，包括群G1，G2的生成元，以及公钥
 */

public class PublicMeta implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private BigInteger gen1;		//群G1的生成元
	private BigInteger gen2;		//群G2的生成元
	private BigInteger pk;			//用户对应的公钥
	public PublicMeta(BigInteger gen1, BigInteger gen2, BigInteger pk) {
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
	public BigInteger getGen1() {
		return gen1;
	}
	/**
	 * @return the gen2
	 */
	public BigInteger getGen2() {
		return gen2;
	}
	/**
	 * @return the pk
	 */
	public BigInteger getPk() {
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

