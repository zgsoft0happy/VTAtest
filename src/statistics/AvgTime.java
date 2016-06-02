package statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newtest1.DataOwner;
import taskassign.ActiveQueue;
import taskassign.VerifyBlock;

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
	
	public static final double a = 0.8;	//分配比例，按照时间确定
	public static final int AVGTIME = (int) (100 * a);
	
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
}

