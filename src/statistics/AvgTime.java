package statistics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月1日  Time: 下午3:28:39   Locate:149
 * <br/>fileName: AvgTime.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class AvgTime implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final double a = 1;	//分配比例，按照时间确定
	public static final double AVGTIME = 89.2;

	public static final int CONSTANT = 809;;
	public static final double expansion = 0.5;
//	/**
//	 * 还没写完，实在不想写了，干点别的吧！
//	 * @param queue
//	 * @param owner
//	 * @param time
//	 * @return
//	 * @author: YYB
//	 * @Time: 下午4:31:53
//	 */
//	public List<VerifyBlock> getTask(ActiveQueue queue , DataOwner owner , long time)
//	{
//		List<VerifyBlock> task = new ArrayList<>();
//		int num = (int) (time / AVGTIME);
//		//需找前num个的
//		List<Long> subList = queue.getPriorityList().subList(0, num-1);
//		Map map = new HashMap<>();
//		return null;
//	}
	
	public static long getTheoryTime(List<VerifyBlock> task , long start)
	{
		long theoryTime = Long.MAX_VALUE;
		for (int i = 0 ; i < task.size() ; i++){
			long tmp = task.get(i).getTime() - start;
			if (theoryTime > tmp)
			{
				theoryTime = tmp;
			}
		}
		return theoryTime;
	}
}

