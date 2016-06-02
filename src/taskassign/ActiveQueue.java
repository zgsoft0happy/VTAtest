package taskassign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newtest1.DataOwner;
import statistics.AvgTime;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月1日  Time: 下午1:52:40   Locate:149
 * <br/>fileName: ActiveQueue.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：处于等待分配的数据块队列，已经按照优先级分配好的队列。
 * 根据优先级，排队插入，使用List的add(index,element)方法插入元素，其中elemnet是在
 * 校验队列中对应数据块的索引值。
 */

public class ActiveQueue implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private List<VerifyBlock> activeQueue = new ArrayList<>();
	
	public List<VerifyBlock> getActiveQueue()
	{
		if (this.activeQueue == null)
		{
			this.activeQueue = new ArrayList<>();
		}		
		return this.activeQueue;
	}
	
	/**
	 * 数据块从等待队列加入激活队列时，按照优先级直接排序。
	 * @param block
	 * @return
	 * @author: YYB
	 * @Time: 下午1:57:41
	 */
	public List<VerifyBlock> add(VerifyBlock block)
	{
		int num = this.activeQueue.size();
		for (int i = 0 ; i < num ; i++)
		{
			if(block.getPriority() > this.activeQueue.get(i).getPriority())
			{
				this.activeQueue.add(i, block);
				return this.activeQueue;
			}
		}
		this.activeQueue.add(num , block);
		return this.activeQueue;
	}
	
	/**
	 * 任务分配，最大化分配数量。
	 * @param owner
	 * @param time
	 * @return
	 * @author: YYB
	 * @Time: 下午3:30:11
	 */
	public Map<Integer, List<VerifyBlock>> assignTask(DataOwner owner , long time)
	{
		int num = (int) (time / AvgTime.AVGTIME);
		long newTime = time;
		List<VerifyBlock> result = null;
		for (int i = 0 ; i < num ; i++)
		{
			if (this.activeQueue.get(i).getTime() < newTime)
			{
				newTime = this.activeQueue.get(i).getTime();
				num = (int) (newTime / AvgTime.AVGTIME);
				if (num < i)
				{
					result = this.activeQueue.subList(0, i-1);
					this.activeQueue.removeAll(result);
//					return result;
				}
				if (num == i)
				{
					result = this.activeQueue.subList(0, i);
					this.activeQueue.removeAll(result);
//					return result;
				}
			}
		}
		//前边是最大化分配任务。后边是将任务中包含的数据块按照用户ID分类。
		
		Map<Integer, List<VerifyBlock>> map = new HashMap<>();
		Integer ownerId = null;
		for (int i = 0 ; i < result.size() ; i++)
		{
			ownerId = result.get(i).getOwnerId();
			if (map.containsKey(ownerId))
			{
				map.get(ownerId).add(result.get(i));
			}else
			{
				List<VerifyBlock> list = new ArrayList<>();
				ownerId = result.get(i).getOwnerId();
				list.add(result.get(i));
				map.put(ownerId, list);
			}
		}
		return map;
	}
	
//	private Map<Long, VerifyBlock> activeQueue;//数据块队列,Integer是指优先级。
//	private List<Long> priorityList ; 		//优先级顺序队列。
//	
//	/**
//	 * 采用单列模式获得数据块集合。
//	 * @return
//	 * @author: YYB
//	 * @Time: 下午2:38:45
//	 */
//	public Map<Long, VerifyBlock> getActiveQueue()
//	{
//		if (this.activeQueue == null)
//		{
//			this.activeQueue = new HashMap<>();
//		}
//		return this.activeQueue;
//	}
//	
//	/**
//	 * 采用单列模式获得优先级有序的索引集合。
//	 * @return
//	 * @author: YYB
//	 * @Time: 下午2:39:07
//	 */
//	public List<Long> getPriorityList()
//	{
//		if (this.priorityList == null)
//		{
//			this.priorityList = new ArrayList<>();
//		}
//		return this.priorityList;
//	}
//
//	public List<VerifyBlock> assignTask(DataOwner owner , long time)//time是用户owner的截止时间。
//	{
//		
//		return null;
//	}
//	
//	
	
}

