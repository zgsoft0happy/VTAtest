package test;

import java.io.Serializable;
import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月26日  Time: 下午4:01:41   Locate:149
 * <br/>fileName: PublicMessage.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class PublicMessage implements Serializable {

	private Users user;			//用户
	private BigInteger publicKey;	//对应用户的公钥
//	private Field G1;			//群G1
//	private Field G2;			//群G2
//	private Field GT;			//群GT
//	private Field Zn;			//域Zn
	private BigInteger gen1;		//群G1的生成元
	private BigInteger gen2;		//群G2的生成元
	
	
	public PublicMessage(Users user, BigInteger publicKey, BigInteger gen1, BigInteger gen2) {
		super();
		this.user = user;
		this.publicKey = publicKey;
		this.gen1 = gen1;
		this.gen2 = gen2;
	}
	
	
	
}

