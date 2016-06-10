package taskassign1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月5日  Time: 下午2:33:13   Locate:149
 * <br/>fileName: PriorityUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class PriorityUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 按照价值和紧迫度设置优先级等级。
	 * @param list
	 * @return
	 * @author: YYB
	 * @Time: 下午2:34:45
	 */
	public static List<VerifyBlock> setPriority(List<VerifyBlock> list)
	{
		for(int i = 0 ; i < list.size() ; i++)
		{
			long time = list.get(i).getTime();
			int value = list.get(i).getValue();
			double priority = Math.pow(Math.log10(time) , AssignParams.deadlineWight) 
					* Math.pow(value, AssignParams.valueWight);
			list.get(i).setPriority(priority);
		}
		return list;
	}
	/**
	 * 按照紧迫度设置优先级。
	 * @param list
	 * @return
	 * @author: YYB
	 * @Time: 下午7:41:59
	 */
	public static List<VerifyBlock> setPriority1(List<VerifyBlock> list)
	{
		for(int i = 0 ; i < list.size() ; i++)
		{
			long time = list.get(i).getTime();
			int value = list.get(i).getValue();
			double priority = Math.pow(Math.log10(time) , AssignParams.deadlineWight) 
					* 1;
			list.get(i).setPriority(priority);
		}
		return list;
	}
	
	/**
	 * 按照优先级给队列排队。
	 * @param list
	 * @return
	 * @author: YYB
	 * @Time: 下午6:19:34
	 */
	public static List<VerifyBlock> prioritySort(List<VerifyBlock> list)
	{
		List<VerifyBlock> newList = new ArrayList<>();
		for(int i = 0 ; i < list.size() ; i++)
		{
			if(list.get(i).getDeadline().getTime() <= System.currentTimeMillis())
			{
				list.remove(i);
			}
		}
		newList.add(list.get(0));
		for (int i = 1 ; i < list.size() ; i++)
		{
			int j = 0;
			for (j = 0 ; j < i ; j++)
			{
				if (list.get(i).getPriority() > newList.get(j).getPriority())
				{
					newList.add(j, list.get(i));
					break;
				}
			}
			if (j == i)
			{
				newList.add(j, list.get(i));
			}
		}
		list = newList;
		return list;
	}
}

