package taskassign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newtest1.DataOwner;

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

	private Map<Long, VerifyBlock> activeQueue;//数据块队列,Integer是指优先级。
	private List<Long> priorityList ; 		//优先级顺序队列。
	
	/**
	 * 采用单列模式获得数据块集合。
	 * @return
	 * @author: YYB
	 * @Time: 下午2:38:45
	 */
	public Map<Long, VerifyBlock> getActiveQueue()
	{
		if (this.activeQueue == null)
		{
			this.activeQueue = new HashMap<>();
		}
		return this.activeQueue;
	}
	
	/**
	 * 采用单列模式获得优先级有序的索引集合。
	 * @return
	 * @author: YYB
	 * @Time: 下午2:39:07
	 */
	public List<Long> getPriorityList()
	{
		if (this.priorityList == null)
		{
			this.priorityList = new ArrayList<>();
		}
		return this.priorityList;
	}

	public List<VerifyBlock> assignTask(DataOwner owner , long time)//time是用户owner的截止时间。
	{
		
		return null;
	}
	
	
	
}

