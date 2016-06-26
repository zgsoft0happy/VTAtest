package taskassign2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import newtest1.DataOwner;
import newtest1.FileUtils;
import newtest1.JdbcUtils;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 下午4:33:31   Locate:149
 * <br/>fileName: TaskUtils1.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这个主要是别人的EDF等算法的任务分配相关。
 */

public class TaskUtils1 implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final int MAXTIME = 1000;
	public static final long EXPSCOPE = 30;
	public static final long CONSTANT = 10000;
	
	public static final int QUEUESIZE = 4;
	
	
	public static List<Task> taskqueue = new ArrayList<>();
	public static List<DataOwner> verifierqueue = new ArrayList<>();
	
	/**
	 * 根据用户（数据拥有者）和数据文件名以及挑战的比例产生校验需求，即任务。
	 * @param owner
	 * @param fileName
	 * @param rate
	 * @return
	 * @author: YYB
	 * @Time: 下午5:32:34
	 */
	public static Task genTaskFromOwner(DataOwner owner , String fileName, double rate)
	{
		Task task = new Task();
		task.owner = owner;			//1
		task.fileName = fileName;						//2
		int num = FileUtils.getBlocksNumOfFile(fileName);
		int size = (int) (num * rate);
		long time = Long.MAX_VALUE;
		int value = 0;
		List<VerifyBlock> blocks= new ArrayList<>();
		Random rand = new Random();
		for (int i = 0 ; i < size ; i++)
		{
			long t = rand.nextInt(MAXTIME) * EXPSCOPE + CONSTANT;
			int val = rand.nextInt(20);		//价值
			value += val;
			Date deadline = new Date(System.currentTimeMillis() + t);
			if (time > t)
			{
				time = t;
			}
			int index = rand.nextInt(num);
			double priority = 0.0;
			VerifyBlock block = new VerifyBlock(fileName, index, t, deadline, val, owner, priority);
			blocks.add(block);
		}
		task.blocks = blocks;					//3
		task.deadLine = new Date(System.currentTimeMillis() + time);	//4
		task.value = value;				//5
		return task;
	}
	
	/**
	 * 向任务队列中添加新的任务
	 * @param task
	 * @return
	 * @author: YYB
	 * @Time: 下午5:32:15
	 */
	public static List<Task> addTask(Task task)
	{
		taskqueue.add(task);
		verifierqueue.add(task.owner);
		return taskqueue;
	}
	
	/**
	 * 依据优先级对任务队列中的任务进行优先级排序
	 * @param taskqueue
	 * @return
	 * @author: YYB
	 * @Time: 下午5:31:43
	 */
	public static List<Task> sortTask()
	{
		List<Task> newQueue = new ArrayList<>();
		newQueue.add(taskqueue.get(0));
		outer : for(int i = 1 ; i < taskqueue.size();i++)
		{
			Task task = taskqueue.get(i);
			for(int j = 0 ; j < newQueue.size() ; j++)
			{
				if (task.priority >= newQueue.get(j).priority)
				{
					newQueue.add(j, task);;
					continue outer;
				}
			}
			newQueue.add(task);
		}
		taskqueue = newQueue;
		return taskqueue;
	}
	
	/**
	 * 分配任务，从任务队列中得到优先级最高的任务
	 * @param owner
	 * @return
	 * @author: YYB
	 * @Time: 下午5:31:04
	 */
	public static Task assignTask(DataOwner owner)
	{
//		TaskUtils1.setPriority();		//时间
//		TaskUtils1.setPriority1();		//价值
		TaskUtils1.setPriority2();
		TaskUtils1.sortTask();
		Task task = taskqueue.get(0);
		taskqueue.remove(task);
		return task;
	}
	
	
	/**
	 * 设置优先级，剔除已经超时的,按照时间
	 * @return
	 * @author: YYB
	 * @Time: 上午11:07:09
	 */
	public static List<Task> setPriority()
	{
		long now = System.currentTimeMillis();
		for (int i = 0 ; i < taskqueue.size() ; i++)
		{
			long timedeta = taskqueue.get(i).deadLine.getTime()- now;
			if (timedeta > 0)
			{
				taskqueue.get(i).priority =	10.0/Math.log10(timedeta);
			}
			else
			{
				JdbcUtils.insertEDFResult(taskqueue.get(i), true,0,0,0);
				taskqueue.remove(i);
				i--;
				int ownerId = new Random().nextInt(11) + 1;
				DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
				String fileName = "Test1.rar";
				double rate = Math.random();
				Task task = genTaskFromOwner(owner, fileName, rate);
				taskqueue.add(task);
			}
		}
		return taskqueue;
	}
	
	/**
	 * 设置优先级，剔除已经超时的,按照价值
	 * @return
	 * @author: YYB
	 * @Time: 上午11:07:09
	 */
	public static List<Task> setPriority1()
	{
		long now = System.currentTimeMillis();
		for (int i = 0 ; i < taskqueue.size() ; i++)
		{
			long timedeta = taskqueue.get(i).deadLine.getTime()- now;
			if (timedeta > 0)
			{
				taskqueue.get(i).priority =
						taskqueue.get(i).value/(taskqueue.get(i).blocks.size()+1);
			}
			else
			{
				JdbcUtils.insertHVFResult(taskqueue.get(i), true,0,0,0);
				taskqueue.remove(i);
				i--;
				int ownerId = new Random().nextInt(11) + 1;
				DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
				String fileName = "Test1.rar";
				double rate = Math.random();
				Task task = genTaskFromOwner(owner, fileName, rate);
				taskqueue.add(task);
			}
		}
		return taskqueue;
	}
	
	/**
	 * 设置优先级，剔除已经超时的,按照价值和时间的折衷，权重相等
	 * @return
	 * @author: YYB
	 * @Time: 上午11:07:09
	 */
	public static List<Task> setPriority2()
	{
		long now = System.currentTimeMillis();
		for (int i = 0 ; i < taskqueue.size() ; i++)
		{
			long timedeta = taskqueue.get(i).deadLine.getTime()- now;
			if (timedeta > 0)
			{
				taskqueue.get(i).priority =
						taskqueue.get(i).value/2*(taskqueue.get(i).blocks.size()+1)
						+ 10.0/Math.log10(timedeta);
			}
			else
			{
				JdbcUtils.insertDPAResult(taskqueue.get(i), true,0,0,0);
				taskqueue.remove(i);
				i--;
				int ownerId = new Random().nextInt(11) + 1;
				DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
				String fileName = "Test1.rar";
				double rate = Math.random();
				Task task = genTaskFromOwner(owner, fileName, rate);
				taskqueue.add(task);
			}
		}
		return taskqueue;
	}
}

