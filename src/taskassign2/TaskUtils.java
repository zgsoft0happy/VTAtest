package taskassign2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import newtest1.DataOwner;
import newtest1.FileUtils;
import newtest1.JdbcUtils;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月14日  Time: 下午1:30:51   Locate:149
 * <br/>fileName: TaskUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class TaskUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final int avgTime = 100;		//平均每一块的校验时间
	public static final int expScope = 500;
	public static final int queueSize = 500;	//队列中保持的尺寸，也就是待分配的数据量。
	public static final String filename = "Test1.rar";
//	public static DataOwner owner
	public static  List<VerifyBlock> queue = new ArrayList<>(500);
	
	//添加数据任务
	public static List<VerifyBlock> addRequire(int n)
	{

		int ownerId = (int) (Math.random() * 10) + 1;
		DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
		int num = FileUtils.getBlocksNumOfFile(filename);
		Field Zn = owner.getPairing().getZr();
		Element[] blocks = FileUtils.getBlockDataFromFile(filename, Zn);
		Random rand = new Random();
		while (n>num)
		{
			int index = rand.nextInt(num);	//块索引
			long time = 0L;	//截止时间到现在的时间差
			Date deadline = null;
			int value = 0;
			double priority = 0.0;
			for (int i = 0 ; i < num ;i++)
			{
				time = (long) ((Math.random() * (i+1) * (queueSize/2) * avgTime)
						+ avgTime * Math.random() * expScope);
				deadline = new Date(System.currentTimeMillis() + time);
				value = (int) (Math.random() * 20);
				VerifyBlock block = new VerifyBlock(filename, index, time, deadline, value, owner, priority);
				queue.add(block);
			}
			n -= num;
//			System.out.println("1");
		}
		for (int i = 0 ; i < n ; i++){
			int index = rand.nextInt(num);	//块索引
			long time = (long) ((Math.random() * n * avgTime)
					+ avgTime * Math.random() * expScope);	//截止时间到现在的时间差
			Date deadline = new Date(System.currentTimeMillis() + time);
			int value = (int) (Math.random() * 20);
			double priority = 0.0;
			VerifyBlock block = new 
					VerifyBlock(filename, index, time, deadline, value, owner, priority);
			queue.add(block);
//			System.out.println("2");
		}
		return queue;
	}
	
	//重置优先级
	//1.按照时间优先级
	public static List<VerifyBlock> reSetPriority1(long currentTime)
	{
		int size = queue.size();
		VerifyBlock block = null;
		long time = 0;
		for (int i = 0 ; i < size ; i++)
		{
			block = queue.get(i);
			time = block.getDeadline().getTime() - currentTime;	//模拟时间
			if (time <= 0)
			{
				queue.remove(i);
				i--;
				continue;
			}
			double timePriority = Math.log10(time);
			block.setPriority(timePriority);
		}
		return queue;
	}
	
	
	
	//优先级排序
	public static List<VerifyBlock> sortPriority()
	{
		List<VerifyBlock> newQueue = new ArrayList<>();
		VerifyBlock block = queue.get(0);
		newQueue.add(block);
		outer : for ( int i = 1; i < queue.size() ; i++)
		{
			block = queue.get(i);
			for(int j = 0 ; j < newQueue.size() ; j++)
			{
				if (block.getPriority() > newQueue.get(j).getPriority())
				{
					newQueue.add(j, block);
					continue outer;
				}
			}
			newQueue.add(block);			
		}
		return queue = newQueue;
	}
	
	
	
	
	
	
	
	
	
	
	
	//测试添加数据任务以及重置优先级
	public static void test()
	{
		addRequire(30);
		System.out.println(queue.size());
		System.out.println("=======打印优先级（前）=======");
		for(int i = 0 ; i < queue.size(); i++)
		{
			System.out.println("数据块" + i + "的优先级是：" + queue.get(i).getPriority());
		}
		reSetPriority1(System.currentTimeMillis());
		System.out.println("=======重置优先级后=======");
		System.out.println(queue.size());
		System.out.println("=======打印优先级=======");
		for(int i = 0 ; i < queue.size(); i++)
		{
			System.out.println("数据块" + i + "的优先级是：" + queue.get(i).getPriority());
		}
	}
	
	//测试优先级排序
	public static void test1()
	{
		addRequire(20);
		System.out.println("队列长度：" + queue.size());
		for (int i = 0 ; i < queue.size() ; i++)
		{
			System.out.println("数据块" + i + "的优先级：" + queue.get(i).getPriority());
		}
		System.out.println("=============开始对队列数据块进行重置优先级===============");
		reSetPriority1(System.currentTimeMillis());
		System.out.println("队列长度：" + queue.size());
		for (int i = 0 ; i < queue.size() ; i++)
		{
			System.out.println("数据块" + i + "的优先级：" + queue.get(i).getPriority());
		}
		System.out.println("=============开始对队列数据块进行优先级排序===============");
		sortPriority();
		for (int i = 0 ; i < queue.size() ; i++)
		{
			System.out.println("数据块" + i + "的优先级：" + queue.get(i).getPriority());
		}
	}
	
	
	public static void test2()
	{
		addRequire(20);
		for(int i = 0 ; i < queue.size(); i++){
			System.out.println(queue.get(i).getIndex());
		}
		Task task = new Task();
		task.blocks = queue;
		task.deadLine = new Date((long) (System.currentTimeMillis() + Math.random() * 100000));
		task.fileName = filename;
		task.owner = queue.get(0).getOwner();
		task.value = 1000;
		DataOwner verifier = JdbcUtils.getOwnerFromDB(3);
		Challenge challenge = VerifyUtils.genChallenge(task, verifier);
		
		System.out.println("任务长度：" + task.blocks.size());
		System.out.println("挑战长度：" + challenge.chal.size());
		for(int i = 0 ; i < task.blocks.size() ; i++)
		{
			System.out.println("数据块值：" + task.blocks.get(i).getIndex());
			System.out.println("挑战值：" + challenge.chal.get(task.blocks.get(i)));
		}
	}
	
	/**
	 * 测试产生证据
	 * 
	 * @author: YYB
	 * @Time: 下午4:22:51
	 */
	public static void test3()
	{
		addRequire(20);
		for(int i = 0 ; i < queue.size(); i++){
			System.out.println(queue.get(i).getIndex());
		}
		Task task = new Task();
		task.blocks = queue;
		task.deadLine = new Date((long) (System.currentTimeMillis() + Math.random() * 100000));
		task.fileName = filename;
		task.owner = queue.get(0).getOwner();
		task.value = 1000;
		DataOwner verifier = JdbcUtils.getOwnerFromDB(3);
		Challenge challenge = VerifyUtils.genChallenge(task, verifier);
		
		System.out.println("任务长度：" + task.blocks.size());
		System.out.println("挑战长度：" + challenge.chal.size());
		for(int i = 0 ; i < task.blocks.size() ; i++)
		{
			System.out.println("数据块值：" + task.blocks.get(i).getIndex());
			System.out.println("挑战值：" + challenge.chal.get(task.blocks.get(i)));
		}
		Proof proof = VerifyUtils.genProof(challenge);
		System.out.println("TP:" + proof.TP);
		System.out.println("DP:" + proof.DP);
	}
	
	public static void test4()
	{
		addRequire(20);
		for(int i = 0 ; i < queue.size(); i++){
			System.out.println(queue.get(i).getIndex());
		}
		Task task = new Task();
		task.blocks = queue;
		task.deadLine = new Date((long) (System.currentTimeMillis() + Math.random() * 100000));
		task.fileName = filename;
		task.owner = queue.get(0).getOwner();
		task.value = 1000;
		DataOwner verifier = JdbcUtils.getOwnerFromDB(3);
		Challenge challenge = VerifyUtils.genChallenge(task, verifier);
		
		System.out.println("任务长度：" + task.blocks.size());
		System.out.println("挑战长度：" + challenge.chal.size());
		for(int i = 0 ; i < task.blocks.size() ; i++)
		{
			System.out.println("数据块值：" + task.blocks.get(i).getIndex());
			System.out.println("挑战值：" + challenge.chal.get(task.blocks.get(i)));
		}
		Proof proof = VerifyUtils.genProof(challenge);
		System.out.println("TP:" + proof.TP);
		System.out.println("DP:" + proof.DP);
		
		boolean result = VerifyUtils.verify(challenge, proof);
		
	}
	
	public static void main(String[] args)
	{
//		test();
//		test2();
//		test3();
		test4();
	}
}

