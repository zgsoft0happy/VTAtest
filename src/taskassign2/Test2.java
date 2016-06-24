package taskassign2;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import newtest1.DataOwner;
import newtest1.JdbcUtils;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 下午5:24:08   Locate:149
 * <br/>fileName: Test2.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class Test2 implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static void test()
	{
		Random rand = new Random();
		DataOwner owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		String fileName = "Test1.rar";
		double rate = 0.3;
		Task task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		for (int i = 0 ; i < 20 ; i++)
		{
			owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
			task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
			TaskUtils1.addTask(task);
			System.out.println(TaskUtils1.taskqueue.size());
		}
		TaskUtils1.sortTask();
		
//		for(int i = 0 ; i < TaskUtils1.taskqueue.size() ; i++)
//		{
//			System.out.println(TaskUtils1.taskqueue.get(i));
//		}
		
		for (int i = 0 ; i < 4 ; i++)
		{
			task = TaskUtils1.assignTask(owner);
			
			System.out.println(task + "ha优先级：" + task.priority);
			
			for (int j = 0 ; j < TaskUtils1.taskqueue.size();j++)
			{
				System.out.println(TaskUtils1.taskqueue.get(j) + "优先级：" 
						+ TaskUtils1.taskqueue.get(j).priority);
			}
			System.out.println();
		}
	}
	
	
	public static void test1()
	{
		Random rand = new Random();
		DataOwner owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		String fileName = "Test1.rar";
		double rate = 0.3;
		Task task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
		task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
		TaskUtils1.addTask(task);
		System.out.println(TaskUtils1.taskqueue.size());
		for (int i = 0 ; i < 20 ; i++)
		{
			owner = JdbcUtils.getOwnerFromDB(rand.nextInt(12) + 1);
			task = TaskUtils1.genTaskFromOwner(owner, fileName, rate);
			TaskUtils1.addTask(task);
			System.out.println(TaskUtils1.taskqueue.size());
		}
		TaskUtils1.sortTask();
		
//		for(int i = 0 ; i < TaskUtils1.taskqueue.size() ; i++)
//		{
//			System.out.println(TaskUtils1.taskqueue.get(i));
//		}
		
		for (int i = 0 ; i < 4 ; i++)
		{
			task = TaskUtils1.assignTask(owner);
			
			System.out.println(task + "ha优先级：" + task.priority);
			
			int ownerId = rand.nextInt(12) + 1;
			DataOwner verifier = JdbcUtils.getOwnerFromDB(ownerId);
			Challenge challenge = VerifyUtils.genChallenge(task, verifier);
			Proof proof = VerifyUtils.genProof(challenge);
			boolean result = VerifyUtils.verify(challenge, proof);
			System.out.println("是否错失：" + new Date().after(task.deadLine));
			
//			for (int j = 0 ; j < TaskUtils1.taskqueue.size();j++)
//			{
//				System.out.println(TaskUtils1.taskqueue.get(j) + "优先级：" 
//						+ TaskUtils1.taskqueue.get(j).priority);
//			}
//			System.out.println();
		}
	}
	public static void main(String[] args) {
		test1();
	}
}

