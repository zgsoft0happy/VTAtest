package taskassign1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import newtest1.DataOwner;
import newtest1.FileUtils;
import statistics.AvgTime;
import taskassign.Verifier;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月3日  Time: 下午1:49:30   Locate:149
 * <br/>fileName: VerifyUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class VerifyUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	
	/**
	 * 从文件中获得校验请求。
	 * @param filename
	 * @param rate
	 * @param owner
	 * @return
	 * @author: YYB
	 * @Time: 下午8:11:46
	 */
	public static List<VerifyBlock> genRequirement(String filename , double rate , DataOwner owner)
	{
		List<VerifyBlock> list = new ArrayList<>();
		int num = FileUtils.getBlocksNumOfFile(filename);
		int n = (int) (num * rate);
		Random random = new Random();
		long time;
		int value ; 
		double priority;
		VerifyBlock block = null;
		int index;
		long avg = (long) (AvgTime.AVGTIME * n) + 608; 
//		System.out.println("avg: " + avg);
		for (int i = 0 ; i < n ; i++)
		{
			index = random.nextInt(num);
			time = random.nextInt((int)avg) + avg/2 ;
//			System.out.println("time:" + time);
			Date deadline = new Date(System.currentTimeMillis() + time);
			value = random.nextInt(20);
			priority = random.nextDouble();
			block = new VerifyBlock(filename, index,time , deadline, value, owner, priority);
			list.add(block);
		}
		return list;
	}
	
	
	public static List<VerifyBlock> setTime(List<VerifyBlock> task , Date start ,
			double rate , double expansion)
	{
		int num = (int) (task.size() * rate);
		List<VerifyBlock> newTask = new ArrayList<>();
		Random rand = new Random();
		int avg = (int) ((AvgTime.AVGTIME * num + AvgTime.CONSTANT) * expansion);
		for(int i = 0 ; i < num ; i++)
		{
			VerifyBlock block = task.get(i);
			long time = rand.nextInt(avg) + avg / 2;
			block.setTime(time);
			block.setDeadline(new Date(start.getTime() + time));
			newTask.add(block);
		}
		return newTask;
	}
	
	/**
	 * 单用户校验的挑战
	 * @param filename
	 * @param owner
	 * @param rate
	 * @return
	 * @author: YYB
	 * @Time: 下午4:37:17
	 */
	public static Map<VerifyBlock, Element> genChallenge(List<VerifyBlock> list , DataOwner owner)
	{
//		List<VerifyBlock> list = genRequirement(filename, rate, owner);
		Field Zn = owner.getPairing().getZr();
		Map<VerifyBlock, Element> challenge = new HashMap<>();
		for (int i = 0 ; i < list.size() ; i++)
		{
			VerifyBlock block = list.get(i);
			Element v = Zn.newRandomElement().getImmutable();
			challenge.put(block, v);
		}
//		System.out.println("成功生成对文件" + list.get(0).getFilename() + "的挑战！");
		return challenge;
	}
	
	/**
	 * 来自同一个用户的校验任务的证据。
	 * @param owner
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午6:20:48
	 */
	public static Proof genProof(DataOwner owner , Map<VerifyBlock, Element> challenge)
	{
		int size = challenge.size();
		Iterator<VerifyBlock> it = challenge.keySet().iterator();
		String filename = it.next().getFilename();
		it = challenge.keySet().iterator();
		Element[] tags = FileUtils.getTagsFromTagFile(filename, owner);
		
		//下面是产生证据。
		Pairing pairing = owner.getPairing();
		//下面是标签证据产生。
		Element TP = pairing.getG1().newOneElement();
		for(;it.hasNext();)
		{
			VerifyBlock block = it.next();
			int index = block.getIndex();
			Element tag = tags[index];
			Element v = challenge.get(block);
			TP = TP.mul(tag.duplicate().powZn(v.duplicate()));
		}
//		System.out.println("TP:" + TP);		//标签证据。
		//下面是数据证据的产生。
		Field Zn = pairing.getZr();
		Element[] blocks = FileUtils.getBlockDataFromFile(filename, Zn);
		it = challenge.keySet().iterator();
		Element MP = Zn.newZeroElement();
		for (;it.hasNext();)
		{
			VerifyBlock block = it.next();
			int index = block.getIndex();
			Element data = blocks[index];
//			System.out.println(data);
			Element v = challenge.get(block);
			MP = MP.add(v.duplicate().mul(data.duplicate()));
		}
//		System.out.println("MP:" + MP);
		Element gen1 = owner.getPublicMeta().getGen1().getImmutable();
		Element pk = owner.getPublicMeta().getPk().getImmutable();
		Element DP = pairing.pairing(gen1, pk).duplicate().powZn(MP.duplicate()).getImmutable();
//		System.out.println("DP:" + DP);		//数据证据。
		
		
		Proof proof = new Proof(TP, DP);
		return proof;
	}
	
	/**
	 * 单个用户需求的验证
	 * @param owner
	 * @param challenge
	 * @param proof
	 * @return
	 * @author: YYB
	 * @Time: 下午8:18:37
	 */
	public static boolean verify(DataOwner owner , Map<VerifyBlock, Element> challenge , Proof proof)
	{
		Element DP = proof.getDP().getImmutable();
		Element TP = proof.getTP().getImmutable();
		Element pk = owner.getPublicMeta().getPk();
		Element one = owner.getPairing().getG1().newOneElement().getImmutable();
		Element gen2 = owner.getPublicMeta().getGen2().getImmutable();
		Element left = DP.duplicate().mul(owner.getPairing().pairing(one, pk).duplicate()).getImmutable();
		Element right = owner.getPairing().pairing(TP, gen2);
		System.out.println("left: \t" + left);
		System.out.println("right: \t" + right);
		System.out.println("是否校验通过：" + left.equals(right));
		return left.equals(right);
	}
	
	
	//下面是批量的步骤
	
	public static Map<DataOwner , List<VerifyBlock>> getFilenames(List<VerifyBlock> task)
	{
		Iterator<VerifyBlock> it = task.iterator();
		Map<DataOwner, List<VerifyBlock>> result = new HashMap<>();
		VerifyBlock block = null;
		DataOwner owner = null;
		String filename = null;
		for ( ; it.hasNext() ; )
		{
			block = it.next();
			owner = block.getOwner();
			if(result.get(owner) == null)
			{
				List<VerifyBlock> list = new ArrayList<>();
				list.add(block);
				result.put(owner, list);
			}
			else
			{
				result.get(owner).add(block);
			}
		}
		return result;
	}
	
	/**
	 * 批量产生挑战。
	 * @param filenames
	 * @param rate
	 * @return
	 * @author: YYB
	 * @Time: 下午7:02:07
	 */
	public static Map<DataOwner, Map> batchChallenge(Map<DataOwner, //String> filenames 
			List<VerifyBlock>> lists)
	{
//		Iterator<DataOwner> owners = filenames.keySet().iterator();
		Iterator<DataOwner> owners = lists.keySet().iterator();
		Map<DataOwner, Map> challenge = new HashMap<>();
		Map<VerifyBlock, Element> chal = new HashMap<>();
		for (;owners.hasNext();)
		{
			DataOwner owner = owners.next();
//			String filename = filenames.get(owner);
//			chal = genChallenge(filename, owner, rate);
			List<VerifyBlock> blocks = lists.get(owner);
			chal = genChallenge(blocks, owner);
			challenge.put(owner, chal);
		}
		return challenge;
	}
	
	/**
	 * 批量产生证据。
	 * @param filenames
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午7:02:20
	 */
	public static Map<DataOwner, Proof> batchProof(Map<DataOwner, //String> filenames 
			List<VerifyBlock>> lists,Map<DataOwner, Map> challenge)
	{
		Map<DataOwner, Proof> result = new HashMap<>();
		Iterator<DataOwner> owners = challenge.keySet().iterator();
		for(; owners.hasNext() ; )
		{
			DataOwner owner = owners.next();
//			List<VerifyBlock> list = lists.get(owner);
			Map<VerifyBlock, Element> chal = challenge.get(owner);
			Proof proof = genProof(owner, chal);
			result.put(owner, proof);
		}
		return result;
	}
	
	public static Map<DataOwner, Boolean> batchVerify(/*Map<DataOwner, //String> filenames 
			List<VerifyBlock>> lists,*/Map<DataOwner, Map> challenge , Map<DataOwner, Proof> proof)
	{
		Iterator<DataOwner> owners = proof.keySet().iterator();
		Map<DataOwner, Boolean> result = new HashMap<>();
		for( ; owners.hasNext() ;)
		{
			DataOwner owner = owners.next();
			Map<VerifyBlock, Element> chal = challenge.get(owner);
			Proof p = proof.get(owner);
			boolean r = verify(owner, chal, p);
			result.put(owner, r);
		}
		return result;
	}
	
	/**
	 * 添加校验任务。
	 * @param list
	 * @param newRequirement
	 * @return
	 * @author: YYB
	 * @Time: 下午2:02:24
	 */
	public static List<VerifyBlock> addNewRequirement(List<VerifyBlock> list ,
			List<VerifyBlock> newRequirement)
	{
		List<VerifyBlock> newList = PriorityUtils.prioritySort(newRequirement);
				/*new ArrayList<>();
		newList.add(newRequirement.get(0));
		for(int i = 1 ; i < newRequirement.size() ; i++)
		{
			int j = 0;
			for (j = 0 ; j < newList.size() ; j++)
			{
				if(newRequirement.get(i).getPriority() > newList.get(j).getPriority())
				{
					newList.add(j, newRequirement.get(i));
					break;
				}
			}
			if (j==i)
			{
				newList.add(j , newRequirement.get(i));
			}
		}*/
		int i = 0;
		int j = 0;
		List<VerifyBlock> result = new ArrayList<>();
		while(i < newList.size() && j < list.size())
		{
			if(newList.get(i).getPriority() > list.get(j).getPriority())
			{
				result.add(newList.get(i));
				i++;
			}
			else
			{
				result.add(list.get(j));
				j++;
			}
		}
		if(i < newList.size())
		{
			for(; i < newList.size();i++)
			{
				result.add(newList.get(i));
			}
		}
		if(j < list.size())
		{
			for(; j < list.size(); j++)
			{
				result.add(list.get(j));
			}
		}
		return result;
	}
	
	public static List<VerifyBlock> addNewRequement1(List<VerifyBlock> task ,
			List<VerifyBlock> newRequement)
	{
		task.addAll(newRequement);
		return task;
	}
	
//	public static void 
	public static List<VerifyBlock> assignTask(List<VerifyBlock> queue)
	{
		List<VerifyBlock> task = new ArrayList<>();
		int size = queue.size();
		Date now = new Date();
//		System.out.println("now " + now);
		PriorityUtils.prioritySort(queue);
		for(int i = 0 ; i < queue.size() ; i++)
		{
//			System.out.println(queue.get(i).getDeadline().getTime() - now.getTime());
			if(!now.before(queue.get(i).getDeadline()))
			{				
				queue.remove(i);
				i--;
			}
		}
		int preIndex = 0;
		int nowIndex = 0;
		long maxTime = queue.get(0).getDeadline().getTime() - now.getTime();
		long time = 0;
		while (nowIndex < queue.size())
		{
			nowIndex++;
			if(maxTime > queue.get(nowIndex).getDeadline().getTime() - now.getTime())
			{
				maxTime = queue.get(nowIndex).getDeadline().getTime() - now.getTime();
			}
			if (maxTime / AvgTime.AVGTIME >= (nowIndex + 1))
			{
				preIndex = nowIndex;
			}else 
			{
				break;
			}
		}
		task = queue.subList(0, preIndex);
//		queue.removeAll(task);
//		System.out.println(task.size());
		return task;
	}
}

