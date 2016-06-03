package taskassign1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import newtest1.BaseParams;
import newtest1.DataOwner;
import newtest1.JdbcUtils;
import statistics.AvgTime;

/**
 * <br/>
 * CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a> <br/>
 * Copyright (C), 2016-2017, YYB , Thomas <br/>
 * This program is protected by copyright laws. <br/>
 * Programe Name: <br/>
 * Date: 2016年6月3日 Time: 下午12:31:14 Locate:149 <br/>
 * fileName: Test1.java
 * 
 * @author yyb zgsoft_happy@126.com
 * @version 1.0 description：
 */

public class Test1 implements Serializable {

	public static final long serialVersionUID = 1L;

	// 先生成用户参数。
	public static void test1() {
		// 插入几个用户
		DataOwner owner = null;

		// for(int i = 4 ; i < 13 ; i++)
		// {
		// JdbcUtils.register("owner" + i, "123456");
		// System.out.println("用户owner" + i + "注册成功！");
		// }

		// 为用户生成参数。
		// for (int i = 1 ; i < 13 ; i++)
		// {
		// owner = JdbcUtils.getOwnerFromDB(i);
		// owner.genParamFileAndSave(owner);
		// System.out.println("保存成功！" + i);
		// }

		long start = System.currentTimeMillis();

		// 每一个用户对文件产生标签文件。
		for (int i = 1; i < 13; i++) {
			owner = JdbcUtils.getOwnerFromDB(i);
			String filename = "Test1.rar";
			owner.saveTagsToTagFile(filename);
			System.out.println("用户" + owner.getOwnerId() + " 成功保存" + filename + "的标签。");
			filename = "test1.txt";
			owner.saveTagsToTagFile(filename);
			System.out.println("用户" + owner.getOwnerId() + " 成功保存" + filename + "的标签。");
			System.out.println();
		}
		long end = System.currentTimeMillis();
		System.out.println("一共用时：" + (end - start));
	}

	public static void test() {
		long start = System.currentTimeMillis();
		DataOwner owner1 = JdbcUtils.getOwnerFromDB(1);

		// 下边是产生用户owner1的公开信息，甚至是私钥信息。
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath + owner1.getOwnerId() + "param.properties");
		System.out.println("标识位置1");

		Field G11 = pairing.getG1();
		Field G12 = pairing.getG2();
		Field G1T = pairing.getGT();
		Field Z1n = pairing.getZr();

		Element gen11 = owner1.getPublicMeta().getGen1();
		Element gen12 = owner1.getPublicMeta().getGen2();
		System.out.println("gen11: " + gen11);
		System.out.println("gen12: " + gen12);

		Element pk1 = owner1.getPublicMeta().getPk();
		Element sk1 = owner1.getMySk();
		System.out.println("PK1: " + pk1);
		System.out.println("SK1: " + sk1);

		System.out.println("用户" + owner1.getOwnerId() + "的信息获得成功！");

		// 产生挑战。
		// double rate = 0.3;
		String filename = "Test1.rar";
		double rate1 = BaseParams.RATE;
//		Map<VerifyBlock, Element> challenge = VerifyUtils.genChallenge(filename, owner1, rate1);

		List<VerifyBlock> lists = VerifyUtils.genRequirement(filename, rate1, owner1);
		Map<VerifyBlock, Element> challenge = VerifyUtils.genChallenge(lists, owner1);
		System.out.println("抽取的数据块：" + challenge.size());
		Iterator<VerifyBlock> it = challenge.keySet().iterator();
		Map<Integer, Element> newChallenge = new HashMap<>();
		for (; it.hasNext();) {
			VerifyBlock block = it.next();
			Element chal = challenge.get(block);
			int index = block.getIndex();
			newChallenge.put(index, chal);
			// System.out.println("数据块" + index + "的挑战：" + chal);
		}
		Proof proof = VerifyUtils.genProof(owner1, challenge);
		VerifyUtils.verify(owner1, challenge, proof);
		long end = System.currentTimeMillis();
		int count = 0;
		it = challenge.keySet().iterator();
		for (; it.hasNext();) {
			VerifyBlock block = it.next();
			if (start + block.getTime() >= end) {
				count++;
			}
			// System.out.println("什么情况！");
		}
		System.out.println("未错失率为：" + (count * 1.0) / challenge.size());
	}

	public static void test2() {
		Map<DataOwner, String> filenames = new HashMap<>();
		double rate = 0.2;
		String filename = "Test1.rar";
		DataOwner owner = JdbcUtils.getOwnerFromDB(2);
		List<VerifyBlock> lists = VerifyUtils.genRequirement(filename, rate, owner);
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(3);
		lists.addAll(VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(5);
		lists.addAll(VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		filename = "test1.txt";
		owner = JdbcUtils.getOwnerFromDB(7);
		lists.addAll(VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(11);
		lists.addAll(VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);

		
		Map<DataOwner, List<VerifyBlock>> map = VerifyUtils.getFilenames(lists);
//		double rate = 0.2;
		//挑战
		Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(map);
		Iterator<DataOwner> owners = challenge.keySet().iterator();
		for (; owners.hasNext();) {
			DataOwner newowner = owners.next();
			Map<VerifyBlock, Element> chal = challenge.get(newowner);
			Iterator<VerifyBlock> blocks = chal.keySet().iterator();
			for (; blocks.hasNext();) {
				VerifyBlock block = blocks.next();
				System.out.println("用户" + newowner.getOwnerId() + "的文件" + block.getFilename() 
					+ "的数据块。索引为：" + block.getIndex());
				System.out.println("其对应的有挑战是：" + chal.get(block));
				System.out.println();
			}
			System.out.println("--------------------------------------------------------");
		}
		//证据
		Map<DataOwner, Proof> proof = VerifyUtils.batchProof(map, challenge);
		owners = proof.keySet().iterator();
		for ( ; owners.hasNext() ;)
		{
			DataOwner newOwner = owners.next();
			Proof p = proof.get(newOwner);
			System.out.println("用户" + newOwner.getOwnerId() + "的证据：");
			System.out.println("TP:" + p.getTP() );
			System.out.println("DP:" + p.getDP());
			System.out.println();
		}
		//验证
		Map<DataOwner,Boolean> result = VerifyUtils.batchVerify(challenge, proof);
		owners = result.keySet().iterator();
		for( ; owners.hasNext() ;)
		{
			DataOwner newowner = owners.next();
			Boolean r = result.get(newowner);
			System.out.println("用户" + newowner.getOwnerId() + "的需求的文件校验结果是：" 
					+ result.get(newowner));
		}
	}

	public static void main(String[] args) {
		// test1();
		long start = System.currentTimeMillis();

		test2();

		//
		// for(int i = 0 ; i < 10 ; i++)
		// {
		// test();
		// }
		// System.out.println("\n");
		// test();

		long end = System.currentTimeMillis();
		System.out.println("一共用时：" + (end - start));
	}
}
