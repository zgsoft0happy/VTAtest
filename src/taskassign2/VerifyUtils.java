package taskassign2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.ChangedCharSetException;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import newtest1.DataOwner;
import newtest1.FileUtils;
import taskassign1.VerifyBlock;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月24日  Time: 上午11:16:23   Locate:149
 * <br/>fileName: VerifyUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：这是校验过程的工具，包括给定任务List<VerifyBlock> task，然后根据任务进行
 * 挑战生成，证据生成，和验证。并记录到数据库中。
 */

public class VerifyUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 根据任务和分配的校验者产生对应的挑战。
	 * @param task
	 * @param verifier
	 * @return
	 * @author: YYB
	 * @Time: 下午2:32:43
	 */
	public static Challenge genChallenge(Task task , DataOwner verifier)
	{
		Challenge challenge = new Challenge();
		
		challenge.fileName = task.fileName;		//1
		challenge.owner = task.owner;			//2
		challenge.task = task;					//3
		challenge.verifier = verifier;			//4
		Field Zn = challenge.owner.getPairing().getZr();
		Map<VerifyBlock, Element> chal = new HashMap<>();
		for(int i = 0 ; i < task.blocks.size(); i++)
		{
			chal.put(task.blocks.get(i), Zn.newRandomElement());
		}
		challenge.chal = chal;
		
		return challenge;						//5
	}
	
	/**
	 * 根据校验者的挑战来产生校验证据。
	 * @param challenge
	 * @return
	 * @author: YYB
	 * @Time: 下午2:53:24
	 */
	public static Proof genProof(Challenge challenge)
	{
		Proof proof = new Proof();
		proof.task = challenge.task;			//1
		proof.verifier = challenge.verifier;	//2
		proof.challenge = challenge;			//3
		
		int size = challenge.chal.size();
		Field G1 = challenge.owner.getPairing().getG1();
		Field G2 = challenge.owner.getPairing().getG2();
		Field Zn = challenge.owner.getPairing().getZr();
		
		Element gen1 = challenge.owner.getPublicMeta().getGen1();
		Element gen2 = challenge.owner.getPublicMeta().getGen2();
		Element Pk = challenge.owner.getPublicMeta().getPk();
		
		//从标签文件中获得标签。
		Element[] tags = FileUtils
				.getTagsFromTagFile(proof.task.fileName, proof.challenge.owner);
		//从数据源文件中获得分块数据值
		Element[] blocks = FileUtils
				.getBlockDataFromFile(proof.task.fileName,
						proof.challenge.owner.getPairing().getZr());
		
		//生命标签和数据证据
		Element TP = G1.newOneElement().duplicate();
		Element MP = Zn.newZeroElement().duplicate();
		Element DP = Zn.newOneElement().duplicate();
		Pairing pairing = challenge.owner.getPairing();

		VerifyBlock block = null;
		int index = 0;
		Element tag = null;;
		Element vi = null;
		//产生标签证据
		for(int i = 0 ; i < size ; i++)
		{
			block = proof.task.blocks.get(i);
			index = block.getIndex();
			tag = tags[index].duplicate();
			vi = challenge.chal.get(block).duplicate();
			TP = TP.getImmutable().mul(tag.getImmutable().powZn(vi.getImmutable()));
		}
		proof.TP = TP;								//4
		Element blockData = null;
		for(int i = 0 ; i < size ; i++){
			block = proof.task.blocks.get(i);
			index = block.getIndex();
			blockData = blocks[index].duplicate();
			vi = challenge.chal.get(block).duplicate();
			MP = MP.getImmutable().add(vi.getImmutable().mul(blockData.getImmutable()));
		}
		DP = pairing.pairing(gen1.getImmutable(), Pk.getImmutable()).getImmutable().powZn(MP.getImmutable());
		proof.DP = DP;
		return proof;
	}
	
	/**
	 * 根据证据和挑战验证完整性
	 * @param challenge
	 * @param proof
	 * @return
	 * @author: YYB
	 * @Time: 上午10:20:06
	 */
	public static boolean verify(Challenge challenge , Proof proof)
	{
		DataOwner owner = challenge.owner;
		Field G1 = owner.getPairing().getG1();
		Element oneOfG1 = G1.newOneElement().duplicate();
		Element gen2 = owner.getPublicMeta().getGen2().duplicate();
		Element Pk = owner.getPublicMeta().getPk().duplicate();
		
		Pairing pairing = owner.getPairing();
		
		Element DP = proof.DP.duplicate();
		Element TP = proof.TP.duplicate();
		
		Element left = DP.getImmutable()
				.mul(pairing.pairing(oneOfG1.getImmutable(), Pk.getImmutable())).duplicate();
		Element right = pairing.pairing(TP.getImmutable(), gen2.getImmutable()).duplicate();
		
		System.out.println("left: " + left);
		System.out.println("right: " + right);
		boolean result = left.isEqual(right);
		System.out.println("是否完整：" + result);
		return result;
	}
}

