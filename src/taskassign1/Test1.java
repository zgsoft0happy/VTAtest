package taskassign1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import newtest1.BaseParams;
import newtest1.DataOwner;
import newtest1.FileUtils;
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
			if (block.getDeadline().getTime() < end) {
				count++;
			}
			// System.out.println("什么情况！");
		}
		System.out.println("错失率为：" + (count * 1.0) / challenge.size());
	}

	public static void test2() {
		Map<DataOwner, String> filenames = new HashMap<>();
		double rate = 0.2;
		String filename = "Test1.rar";
		DataOwner owner = JdbcUtils.getOwnerFromDB(2);
		List<VerifyBlock> lists = new ArrayList<>();
		lists = VerifyUtils.addNewRequirement(lists, VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(3);
		lists = VerifyUtils.addNewRequirement(lists, VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(5);
		lists = VerifyUtils.addNewRequirement(lists, VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		filename = "test1.txt";
		owner = JdbcUtils.getOwnerFromDB(7);
		lists = VerifyUtils.addNewRequirement(lists, VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);
		owner = JdbcUtils.getOwnerFromDB(11);
		lists = VerifyUtils.addNewRequirement(lists, VerifyUtils.genRequirement(filename, rate, owner));
//		filenames.put(owner, filename);

		Iterator<VerifyBlock> it = lists.iterator();
		while(it.hasNext())
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		System.out.println("===================================================");
		
		PriorityUtils.setPriority(lists);
		it = lists.iterator();
		while(it.hasNext())
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		
		System.out.println("=====================================================");
		
		lists = PriorityUtils.prioritySort(lists);
		it = lists.iterator();
		while(it.hasNext())
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		
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
		it = lists.iterator();
		int value = 0;
		for(;it.hasNext();)
		{
			value += it.next().getValue();
		}
		System.out.println("一共积累的价值是：" + value);
	}
	
	public static void test3()
	{
		boolean flag = true;
		double rate = 0.3;
		List<VerifyBlock> list = new ArrayList<>();
		int value = 0;
		String filename = "Test1.rar";
		DataOwner owner1 = JdbcUtils.getOwnerFromDB(2);
		list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
		for(int i = 0 ; i < 100 ; i++)
		{
			list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
		}
//		filenames.put(owner, filename);
		owner1 = JdbcUtils.getOwnerFromDB(3);
		list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
//		filenames.put(owner, filename);
		owner1 = JdbcUtils.getOwnerFromDB(5);
		list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
//		filenames.put(owner, filename);
		filename = "test1.txt";
		owner1 = JdbcUtils.getOwnerFromDB(7);
		list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
//		filenames.put(owner, filename);
		owner1 = JdbcUtils.getOwnerFromDB(11);
		list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner1));
		long start = System.currentTimeMillis();
		long timeNum = 0;
		for (int i = 0 ; i < 100 ; i++)
		{
			long partStart = System.currentTimeMillis();
			int ownerId = (i % 12) + 1;
			DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
//			if (flag)
//			{
//				filename = "test"
//			}
			VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner));
//			PriorityUtils.setPriority(list);	//按照价值和时间
			PriorityUtils.setPriority1(list);	//按照时间
			PriorityUtils.prioritySort(list);
			int num = (int) (FileUtils.getBlocksNumOfFile(filename) * rate);
			List<VerifyBlock> task = list.subList(0, num);
			Map<DataOwner, List<VerifyBlock>> taskMap = VerifyUtils.getFilenames(task);
			Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(taskMap);
			Map<DataOwner, Proof> proof = VerifyUtils.batchProof(taskMap, challenge);
			Map<DataOwner, Boolean> result = VerifyUtils.batchVerify(challenge, proof);
			Iterator<VerifyBlock> it = task.iterator();
//			for (; it.hasNext() ; )
//			{
//				value += it.next().getValue();
//			}
			long partEnd = System.currentTimeMillis();
			timeNum += (partEnd - partStart);
			int count = 0;
			for (; it.hasNext() ; )
			{
				if (it.next().getDeadline().getTime() < partEnd)
				{
					count++;							//计算任务错失个数。
				}
			}
			System.out.println("此次的错失率为：" + (count * 1.0 / task.size()));
			
			list.removeAll(task);
			list = VerifyUtils.addNewRequirement(list, VerifyUtils.genRequirement(filename, rate, owner));
			System.out.println("此时用时：" + timeNum);
//			System.out.println("此时的价值积累是：" + value);
//			JdbcUtils.savetimeAndValue(timeNum, value);
//			JdbcUtils.savetimeAndValue1(timeNum, value);
			
		}
		long end = System.currentTimeMillis();
		System.out.println("校验完成，一共用时：" + (end - start) + "，价值积累：" + value);
	}
	
	/**
	 * 测试错失率。
	 * @author: YYB
	 * @Time: 下午3:23:25
	 */
	public static void test4()
	{
		//1.先获得一个用户。
		DataOwner owner = JdbcUtils.getOwnerFromDB(3);
		//2.等待分配任务的队列。
		List<VerifyBlock> queue = new ArrayList<>();
		
		//3.给队列中添加一些初始任务。
		String filename = "Test1.rar";
		double rate = 0.4;
		//设置开始时间
//		long start = System.currentTimeMillis();
		Date start = new Date();
		queue = VerifyUtils.addNewRequirement(queue, VerifyUtils.genRequirement(filename, rate, owner));
		System.out.println("queue的大小：" + queue.size());
		Iterator<VerifyBlock> it = queue.iterator();
		for(;it.hasNext();)
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		queue = PriorityUtils.setPriority1(queue);
		System.out.println("==========改造后============");
		it = queue.iterator();
		for(;it.hasNext();)
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		queue = PriorityUtils.prioritySort(queue);
		System.out.println("=============排序后========");
		it = queue.iterator();
		for(;it.hasNext();)
		{
			System.out.println("优先级：" + it.next().getPriority());
		}
		System.out.println("======下面是执行时间======");
		it = queue.iterator();
//		long end = System.currentTimeMillis();
		Date end = new Date();
		System.out.println("执行时间：" + (end.getTime() - start.getTime()));
		for(;it.hasNext();)
		{
			System.out.println("截止时间是否未错失：" + (it.next().getDeadline().after(end)));
		}
		List<VerifyBlock> task = VerifyUtils.assignTask(queue);
		System.out.println("queue的长度：" + queue.size());
		System.out.println("task的长度：" + task.size());
		queue.removeAll(task);
		queue = PriorityUtils.prioritySort(queue);
		task = VerifyUtils.assignTask(queue);
//		System.out.println(task.size());
	}
	
	/**
	 * 测试校验一定量的数据块的时间。
	 * 
	 * @author: YYB
	 * @Time: 下午8:19:48
	 */
	public static void test5()
	{
		DataOwner owner = JdbcUtils.getOwnerFromDB(3);
		String filename = "Test1.rar";
		double rate = 0.3;
		List<VerifyBlock> task = VerifyUtils.genRequirement(filename, rate, owner);
		task = PriorityUtils.prioritySort(task);
		owner = JdbcUtils.getOwnerFromDB(2);
		task = VerifyUtils.addNewRequirement(task, VerifyUtils.genRequirement(filename, rate, owner));
		task = task.subList(0, (int) (task.size() * 0.8));
		long start = System.currentTimeMillis();
		Map<DataOwner, List<VerifyBlock>> newTask = VerifyUtils.getFilenames(task);
		Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(newTask);
		Map<DataOwner, Proof> proof = VerifyUtils.batchProof(newTask, challenge);
		Map<DataOwner, Boolean> result = VerifyUtils.batchVerify(challenge, proof);
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("校验数量：" + task.size() + "\n校验时间：" + time);
		JdbcUtils.saveNumAndTime(task.size(), time , rate);
	}

	public static void test6()
	{
		DataOwner owner = JdbcUtils.getOwnerFromDB(1);
		String filename = "Test1.rar";
		double rate = 0.2;
		List<VerifyBlock> task = VerifyUtils.genRequirement(filename, rate, owner);
		task = PriorityUtils.setPriority(task);
		task = PriorityUtils.prioritySort(task);
//		owner = JdbcUtils.getOwnerFromDB(2);
//		task = VerifyUtils.addNewRequirement(task, VerifyUtils.genRequirement(filename, rate, owner));
//		task = PriorityUtils.setPriority(task);
//		task = PriorityUtils.prioritySort(task);
//		owner = JdbcUtils.getOwnerFromDB(7);
//		task = VerifyUtils.addNewRequirement(task, VerifyUtils.genRequirement(filename, rate, owner));
//		task = PriorityUtils.setPriority(task);
//		task = PriorityUtils.prioritySort(task);
		for(int i = 2 ; i < 12 ; i++){
			owner = JdbcUtils.getOwnerFromDB(i);
			task = VerifyUtils.addNewRequirement(task, VerifyUtils.genRequirement(filename, rate, owner));
			task = PriorityUtils.setPriority(task);
			task = PriorityUtils.prioritySort(task);
//			System.out.println(i);
		}
		
		task = task.subList(0, (int) (task.size() * 0.8));
//		for (int i = 0 ; i < task.size() ; i++)
//		{
//			System.out.println("优先级：" + task.get(i).getPriority());
//		}
		long theoryTime = (long) (AvgTime.AVGTIME * task.size() + 608);
		long start = System.currentTimeMillis();
//		long theoryTime = AvgTime.getTheoryTime(task, start);
		Map<DataOwner, List<VerifyBlock>> newTask = VerifyUtils.getFilenames(task);
		Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(newTask);
		Map<DataOwner, Proof> proof = VerifyUtils.batchProof(newTask, challenge);
		Map<DataOwner, Boolean> results = VerifyUtils.batchVerify(challenge, proof);
		long end = System.currentTimeMillis();
		long time = end - start;
		byte result = (byte) ((time < theoryTime) ? 1 : 0);
		int value = 0;
		int missedCount = 0;
		System.out.println("end:" + end);
		for(int i = 0; i< task.size();i++)
		{
			value += task.get(i).getValue();
//			System.out.println(task.get(i).getDeadline().getTime());
			boolean n = task.get(i).getDeadline().getTime() < end;
//			System.out.println(n);
			if (n)
			{
				missedCount++;
			}
		}
		System.out.println("校验数量：" + task.size() + "\t校验时间：" + time + 
				"\t理论校验时间：" + theoryTime + "\t错失率：" + (missedCount*1.0/task.size()));
		JdbcUtils.saveNumAndTime3(task.size(), time, theoryTime, result, AvgTime.a, value, missedCount*1.0/task.size());
//		JdbcUtils.saveNumAndTime2(task.size(), time , theoryTime , result , AvgTime.a ,value , AvgTime.a);
//		JdbcUtils.saveNumAndTime(task.size(), time , rate);
	}
	
	public static void test7()
	{
		DataOwner owner = null;
		String filename = "Test1.rar";
		double rate = 0.5;
		List<VerifyBlock> task = new ArrayList<>();
		for (int i = 1 ; i <= 12 ; i++)
		{
			owner = JdbcUtils.getOwnerFromDB(i);
			VerifyUtils.addNewRequement1(task, VerifyUtils.genRequirement(filename, rate, owner));
		}
		double taskRate = 0.6;	//分配任务的比例
		Date start = new Date();
		task = VerifyUtils.setTime(task , start , taskRate ,AvgTime.expansion );
//		System.out.println("任务量：" + task.size());
//		task = PriorityUtils.setPriority(task);
		task = PriorityUtils.setPriority1(task);
		task = PriorityUtils.prioritySort(task);
		task = task.subList(0, (int)(task.size() * taskRate));
//		System.out.println("任务量：" + task.size());
		
		Map<DataOwner, List<VerifyBlock>> newTask = VerifyUtils.getFilenames(task);
		Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(newTask);
		Map<DataOwner, Proof> proof = VerifyUtils.batchProof(newTask, challenge);
		Map<DataOwner, Boolean> results = VerifyUtils.batchVerify(challenge, proof);
		
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		long theoryTime = (long) (AvgTime.AVGTIME * task.size() + AvgTime.CONSTANT);
		byte result = (byte) (time < theoryTime ? 1 : 0);
		int count = 0;
		int value = 0;
		for (int i = 0 ; i < task.size() ; i++)
		{
			if (task.get(i).getDeadline().before(end))
			{
				count++;
			}
			value += task.get(i).getValue();
		}
//		System.out.println(count);
//		System.out.println("start: \t" +start);
//		System.out.println("end: \t" + end);
//		System.out.println(end.getTime() - start.getTime());
		
		
		System.out.println("校验数量：" + task.size() + "\t校验时间：" + time + 
				"\t理论校验时间：" + theoryTime + "\t错失率：" + (count*1.0/task.size()));
//		JdbcUtils.saveNumAndTime(task.size(), time , rate);
		JdbcUtils.saveNumAndTime32(task.size(), time, theoryTime, result, 
				AvgTime.expansion, value, count*1.0 /task.size());
	}
	public static void test8()
	{
		DataOwner owner = null;
		String filename = "Test1.rar";
		double rate = 0.5;
		List<VerifyBlock> task = new ArrayList<>();
		for (int i = 1 ; i <= 12 ; i++)
		{
			owner = JdbcUtils.getOwnerFromDB(i);
			VerifyUtils.addNewRequement1(task, VerifyUtils.genRequirement(filename, rate, owner));
		}
		double taskRate = 0.6;	//分配任务的比例
		Date start = new Date();
		task = VerifyUtils.setTime(task , start , taskRate ,AvgTime.expansion );
//		System.out.println("任务量：" + task.size());
		task = PriorityUtils.setPriority(task);
//		task = PriorityUtils.setPriority1(task);
		task = PriorityUtils.prioritySort(task);
		task = task.subList(0, (int)(task.size() * taskRate));
//		System.out.println("任务量：" + task.size());
		
		Map<DataOwner, List<VerifyBlock>> newTask = VerifyUtils.getFilenames(task);
		Map<DataOwner, Map> challenge = VerifyUtils.batchChallenge(newTask);
		Map<DataOwner, Proof> proof = VerifyUtils.batchProof(newTask, challenge);
		Map<DataOwner, Boolean> results = VerifyUtils.batchVerify(challenge, proof);
		
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		long theoryTime = (long) (AvgTime.AVGTIME * task.size() + AvgTime.CONSTANT);
		byte result = (byte) (time < theoryTime ? 1 : 0);
		int count = 0;
		int value = 0;
		for (int i = 0 ; i < task.size() ; i++)
		{
			if (task.get(i).getDeadline().before(end))
			{
				count++;
			}
			value += task.get(i).getValue();
		}
//		System.out.println(count);
//		System.out.println("start: \t" +start);
//		System.out.println("end: \t" + end);
//		System.out.println(end.getTime() - start.getTime());
		
		
		System.out.println("校验数量：" + task.size() + "\t校验时间：" + time + 
				"\t理论校验时间：" + theoryTime + "\t错失率：" + (count*1.0/task.size()));
//		JdbcUtils.saveNumAndTime(task.size(), time , rate);
		JdbcUtils.saveNumAndTime3(task.size(), time, theoryTime, result, 
				AvgTime.expansion, value, count*1.0 /task.size());
	}
	
	public static void main(String[] args) {
		// test1();
		long start = System.currentTimeMillis();
//
//		test3();
//
//		//
//		// for(int i = 0 ; i < 10 ; i++)
//		// {
//		// test();
//		// }
//		// System.out.println("\n");
//		// test();
//		test4();
		
//		test6();
		
//		for(int i = 0 ; i < 2 ; i++)
//		{
//			System.out.println("第" + i + "轮");
//			test6();
//		}
		
		for(int i = 0 ; i < 20 ; i++)
		{
			System.out.println("第" + i + "轮");
			test7();
		}
		for(int i = 0 ; i < 20 ; i++)
		{
			System.out.println("第" + i + "轮");
			test8();
		}
		
//		test7();
		long end = System.currentTimeMillis();
		System.out.println("一共用时：" + (end - start));
		
	}
}
