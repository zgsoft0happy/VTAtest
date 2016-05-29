package newtest1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月29日  Time: 下午8:02:16   Locate:149
 * <br/>fileName: CloudServerProvider.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：云服务器。
 */

public class CloudServerProvider implements Serializable {

	private static CloudServerProvider CSP;
	public static CloudServerProvider getInstance()
	{
		if (CSP == null)
		{
			return CSP = new CloudServerProvider();
		}
		return CSP;
	}


	//根据挑战产生证据
	/**
	 * CSP 产生数据的验证证据
	 * @param filename
	 * @param owner
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午5:32:42
	 */
	public Element[] genProof(String filename , DataOwner owner , Map<Integer, Element> challenge)
	{
		Element[] tags = FileUtils.getTagsFromTagFile(filename, owner);
		Element TP = owner.getPairing().getG1().newOneElement().getImmutable();
		int num = challenge.size();
		for (int i = 0 ; i < tags.length ; i++)
		{
			TP = TP.mul(tags[i].duplicate().powZn(challenge.get(new Integer(i)).duplicate()));
		}
		Element MP = owner.getPairing().getZr().newZeroElement().getImmutable();
		Element[] blocks = FileUtils.getBlockDataFromFile(filename, owner.getPairing().getZr());
		for (int i = 0 ; i < blocks.length ; i++)
		{
			MP = MP.add(challenge.get(new Integer(i)).duplicate().mulZn(blocks[i].duplicate()));
		}
		Element DP = (owner.getPairing().pairing(owner.getPublicMeta().getGen1(),
				owner.getPublicMeta().getPk())).duplicate().powZn(MP);
		return new Element[]{TP , DP};
	}
	
	

//	//==========================下面是针对概率性校验（抽样校验）==================================
	/**
	 * CSP 产生数据的验证证据
	 * @param filename
	 * @param owner
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午5:32:42
	 */
	public Element[] genProProof(String filename , DataOwner owner , Map<Integer, Element> challenge)
	{
		Element[] tags = FileUtils.getTagsFromTagFile(filename, owner);
		System.out.println("一共有" + tags.length + "个标签/数据块");
		Element TP = owner.getPairing().getG1().newOneElement().getImmutable();
		int num = challenge.size();
		Iterator it = challenge.keySet().iterator();
		Integer[] index = new Integer[num];
		int i = 0;
		while (it.hasNext())
		{
			index[i++] = (Integer) it.next();
		}
		for (i = 0 ; i < num ; i++)
		{
			TP = TP.mul(tags[(int)index[i]].duplicate().powZn(challenge.get(index[i]).duplicate()));
		}
		Element MP = owner.getPairing().getZr().newZeroElement().getImmutable();
		Element[] blocks = FileUtils.getBlockDataFromFile(filename, owner.getPairing().getZr());
		for (i = 0 ; i < num ; i++)
		{
			MP = MP.add(challenge.get(index[i]).duplicate().mulZn(blocks[(int)index[i]].duplicate()));
		}
		Element DP = (owner.getPairing().pairing(owner.getPublicMeta().getGen1(),
				owner.getPublicMeta().getPk())).duplicate().powZn(MP);
		return new Element[]{TP , DP};
	}
	
	
	public void test(Map<Integer, String> map)
	{
		Iterator it = map.keySet().iterator();
		Integer tmp = null;
		while (it.hasNext())
		{
			tmp = (Integer) it.next();
			System.out.println(tmp);
		}
	}
	public static void main(String[] args) {
		CloudServerProvider CSP = CloudServerProvider.getInstance();
		Map<Integer, String> map = new HashMap<>();
		Integer tmp = null;
		Random random = new Random();
		String s = null;
		for (int i = 0 ; i < 20 ; i++)
		{
			tmp = random.nextInt(300);
			s = "hah : " + i;
			map.put(tmp, s);
			System.out.println(tmp + "\t" + s);
		}
		CSP.test(map);
	}
}

