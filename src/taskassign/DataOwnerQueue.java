package taskassign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import newtest1.DataOwner;
import newtest1.JdbcUtils;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月2日  Time: 下午3:31:48   Locate:149
 * <br/>fileName: DataOwnerQueue.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class DataOwnerQueue implements Serializable {

	public static final long serialVersionUID = 1L;	

	private List<Verifier> verifiers = new ArrayList<>();		//校验者队列。
	
	/**
	 * 向校验者队列中添加新的校验者。
	 * @param verifier
	 * @return
	 * @author: YYB
	 * @Time: 下午3:46:53
	 */
	public List<Verifier> addVerifier(Verifier verifier)
	{
		for (int i = 0 ; i < this.verifiers.size() ; i++)
		{
			if(verifier.getRemainTime() < this.verifiers.get(i).getRemainTime())
			{
				this.verifiers.add(i , verifier);
			}
		}
		return this.verifiers;
	}
	
	/**
	 * 在校验者队列中获得优先级最高的校验者。
	 * @return
	 * @author: YYB
	 * @Time: 下午3:56:07
	 */
	public Verifier getVerifier()
	{
		Verifier verifier = this.verifiers.get(0);
		System.out.println("将要分配任务的校验者：" + verifier);
		this.verifiers.remove(0);
		System.out.println("现在队列中优先级最高的校验者：" + this.verifiers.get(0));
		return verifier;
	}
	
	/**
	 * 批量处理的挑战生成。
	 * @param task
	 * @return
	 * @author: YYB
	 * @Time: 下午4:55:08
	 */
	public Map<Integer, Map> genChallenge(Map<Integer, List<VerifyBlock>> task)
	{
		Map<Integer, Map> challenge = new HashMap<>();
//		List ownerIds = task.keySet();
		Iterator<Integer> it = challenge.keySet().iterator();
		for ( ; it.hasNext() ; )
		{
			Integer ownerId = it.next();
			DataOwner owner = JdbcUtils.getOwnerFromDB(ownerId);
//			DataOwner owner = JdbcUtils.getOwnerFromDB(task.)
			List<VerifyBlock> xiaoList = (List<VerifyBlock>) task.get(ownerId);
			Field Zn = owner.getPairing().getZr();
			Map<VerifyBlock, Element> ownerIdChal = this.challenge(xiaoList, Zn);
			challenge.put(ownerId, ownerIdChal);
		}
		
		return challenge;
	}
	
	/**
	 * 任务中来自于统一用户的数据块的挑战。
	 * @param xiaoList
	 * @param Zn
	 * @return
	 * @author: YYB
	 * @Time: 下午5:46:06
	 */
	public Map<VerifyBlock, Element> challenge(List<VerifyBlock> xiaoList , Field Zn)
	{
		Element rand = null;
		Map<VerifyBlock,Element> challenge = new HashMap<>();
		for (int i = 0 ; i < xiaoList.size() ; i++)
		{
			rand = Zn.newRandomElement().getImmutable();
			challenge.put(xiaoList.get(i), rand);
		}
		return challenge;
	}
	
//	public boolean verify(List<VerifyBlock> task)
//	{
//		
//		return false;
//	}
}

