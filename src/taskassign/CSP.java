package taskassign;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.unisa.dia.gas.jpbc.Element;
import newtest1.CloudServerProvider;
import newtest1.DataOwner;
import newtest1.JdbcUtils;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月2日  Time: 下午7:41:40   Locate:149
 * <br/>fileName: CSP.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class CSP implements Serializable {

	public static final long serialVersionUID = 1L;
	
	
	/**
	 * 太复杂了。
	 * @param task
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午9:22:47
	 */
	public Map<Integer, Element[]> genProof(Map<Integer, List<VerifyBlock>> task , Map<Integer, Map> challenge)
	{
//		DataOwner owner = JdbcUtils.getOwnerFromDB(2);
		CloudServerProvider csp = new CloudServerProvider();
//		csp.genProof(filename, owner, challenge)
		Map<Integer, Element[]> proof = new HashMap<>();
		Iterator<Integer> it = task.keySet().iterator();
		for( ; it.hasNext() ; )
		{
//			Map<Integer, Element[]> map = new HashMap<>();	//证据集
			Integer index = it.next();
			List<VerifyBlock> list = task.get(index);	//获得所包含的单一用户的数据列表。
			Map<VerifyBlock, Element> chal = challenge.get(index);	//针对同一用户的挑战。
			Iterator<VerifyBlock> newIt = chal.keySet().iterator();
			String filename = null;
			Map<Integer , Element> newChal = new HashMap<>();
			for(;it.hasNext();)
			{
				VerifyBlock block = newIt.next();
				if(filename == null)
				{
					filename = block.getFilename();
				}
				newChal.put(new Integer(block.getIndex()), chal.get(block));
			}
			DataOwner owner = JdbcUtils.getOwnerFromDB(chal.keySet().iterator().next().getOwnerId());
			Element[] xiaoProof = csp.genProof(filename, owner, newChal);
			proof.put(index, xiaoProof);
		}
		return proof;
	}
}

