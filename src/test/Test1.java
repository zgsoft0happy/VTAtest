package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月26日  Time: 上午10:11:54   Locate:149
 * <br/>fileName: Test1.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class Test1 {

	public static void main(String[] args) {
//		test1();
//		
//		test2();
		test3();
		
	}

	public static void test3()
	{
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing("param.properties");
		
		Field G1 = pairing.getG1();
		Field G2 = pairing.getG2();
		Field GT = pairing.getGT();
		Field Zn = pairing.getZr();
		
		BigInteger big1 = null;
		BigInteger big2 = null;
		BigInteger bigout = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("test.txt"));
			big1 = (BigInteger) ois.readObject();
			big2 = (BigInteger) ois.readObject();
			bigout = (BigInteger) ois.readObject();
			System.out.println("读取完毕");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("读取异常");
			e.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(big1);
		Element in4 = G1.newElementFromBytes(big1.toByteArray());
		System.out.println(in4);
		System.out.println(big2);
		Element in5 = G2.newElementFromBytes(big2.toByteArray());
		System.out.println(in5);
		
		Element out = GT.newElementFromBytes(bigout.toByteArray());
		Element newOut = pairing.pairing(in4, in5);
		System.out.println(newOut);
		System.out.println(newOut.isEqual(out));
	}
	
	public static void test2() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing("param.properties");
		
		Field G1 = pairing.getG1();
		Field G2 = pairing.getG2();
		Field GT = pairing.getGT();
		
		
		Element in1 = G1.newRandomElement();
		System.out.println(in1);
		BigInteger big1 = new BigInteger(in1.toBytes());
		System.out.println(big1);
		
		Element in2 = G2.newRandomElement();
		BigInteger big2 = new BigInteger(in2.toBytes());
//		
		Element in3 = G1.newElementFromBytes(big1.toByteArray());
		System.out.println(in3);
		System.out.println("in3==in1?"+in3.isEqual(in1));
		Element out = pairing.pairing(in1, in2);
		BigInteger outbig = new BigInteger(out.toBytes());
//		
//		System.out.println(out);
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
			oos.writeObject(big1);
			oos.writeObject(big2);
			oos.writeObject(outbig);
			System.out.println("写入成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void test1() {
		System.out.println("1");
		PairingParametersGenerator paramsGen = new TypeACurveGenerator(512, 1024);
		PairingParameters params = paramsGen.generate();
		FileWriter fw = null;
		try {
			fw = new FileWriter("param.properties");
			fw.write(params.toString());
			System.out.println("完成");
		} catch (IOException e) {
			System.out.println("IO异常");
			e.printStackTrace();
		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

