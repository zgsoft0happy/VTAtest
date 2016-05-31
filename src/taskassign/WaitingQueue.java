package taskassign;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newtest1.DataOwner;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月31日  Time: 上午9:45:45   Locate:149
 * <br/>fileName: WaitingQueue.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：任务调度的等待队列。
 */

public class WaitingQueue implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private  Map<DataOwner, List<VerifyBlock>> waitingBlocks;	//等待队列里的待校验数据块！
	
	/**
	 * 等待队列在调度器中只有一个，所有采用单列模式操作等待校验数据块。
	 * @return
	 * @author: YYB
	 * @Time: 上午10:44:13
	 */
	public Map<DataOwner, List<VerifyBlock>> getWaitingBlock(){
		if (this.waitingBlocks != null)
		{
			return this.waitingBlocks;
		}
		Map<DataOwner, List<VerifyBlock>> newwaitingBlocks = new HashMap<>();
		return newwaitingBlocks;
	}
	
	
	/**
	 * 当用户发布最新的需求时，将所有的需求校验的数据块一同添加到等待执行队列。
	 * @param owner	发布校验请求的用户。
	 * @param blockList	用户发布的等待校验的数据块集合。
	 * @author: YYB
	 * @Time: 上午10:47:53
	 */
	public void addWaitingBlocks(DataOwner owner , List<VerifyBlock> blockList)
	{
		this.getWaitingBlock().put(owner, blockList);
	}
	
	/**
	 * 当有需要从等待队列中调度到执行队列时，将对应的数据块从等待队列中删除。
	 * 删除时，如果该用户的本次请求的数据块已经没有了，则删除用户，否则将还未删除的
	 * 数据块集再次加入等待队列。
	 * @param owner
	 * @param blockList
	 * @throws Exception
	 * @author: YYB
	 * @Time: 上午11:12:26
	 */
	public void removeWaitingBlocks(DataOwner owner , List<VerifyBlock> blockList) throws Exception
	{
		List list = getWaitingBlock().get(owner);		//首先获得用户在等待队列中的数据块集
		getWaitingBlock().remove(owner);				//将用户所有等待队列的数据块集从等待队列中移除。
		if (list.containsAll(blockList))				//如果等待队列中的数据块结合包括要删除的集合
		{												
			list.removeAll(blockList);					//删除将要删除的数据块。
		}else
		{
			System.out.println("有不存在的数据块被删除");
			throw new Exception();		//抛出异常
		}
		if (list.size() > 0)							//如果还有剩余，则重新添加到等待校验的队列中。
		{
			getWaitingBlock().put(owner, list);
		}
	}
}

