package newtest1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月28日  Time: 下午7:06:06   Locate:149
 * <br/>fileName: DataOwner.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class DataOwner implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private int ownerId;
	private String username;
	private String password;
	private int score;
	private String remark;
	
	private PublicMeta publicMeta;

	private Pairing pairing;
	
	/**
	 * 根据用户Id，用户名，密码，积分，备注信息产生用户。主要用于从数据库或的用户。
	 * @param ownerId
	 * @param username
	 * @param password
	 * @param score
	 * @param remark
	 * @Time : 下午8:03:27
	 */
	public DataOwner(int ownerId, String username, String password, int score, String remark) {
		super();
		this.ownerId = ownerId;
		this.username = username;
		this.password = password;
		this.score = score;
		this.remark = remark;
	}

	/**
	 * 根据用户名，密码，积分，备注信息产生用户。
	 * @param username
	 * @param password
	 * @param score
	 * @param remark
	 * @Time : 下午8:04:26
	 */
	public DataOwner(String username, String password, int score, String remark) {
		super();
		this.username = username;
		this.password = password;
		this.score = score;
		this.remark = remark;
	}

	public DataOwner() {
		super();
	}

	/**
	 * 获得版本序列号。
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 获得用户ID。
	 * @return the ownerId
	 */
	public int getOwnerId() {
		if (this.ownerId == 0)
		{
			System.out.println("用户没有ID，请从数据库从新获得用户！");;
		}
		return ownerId;
	}

	/**
	 * 获得用户名。
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 获得用户密码。
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 获得用户积分。
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 获得备注信息。
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 获得用户的双曲映射。
	 * @return
	 * @author: YYB
	 * @Time: 下午8:06:16
	 */
	public Pairing getPairing()
	{
		if (this.pairing != null)
		{
			return this.pairing;
		}
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath 
				+ this.getOwnerId() + "param.properties");
		return pairing;
	}
	/**
	 * 获得用户的公开信息。
	 * @return the publicMeta
	 */
	public PublicMeta getPublicMeta() {
		if (this.publicMeta != null)
		{
			return this.publicMeta;
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(BaseParams.pkPath
					+ this.getOwnerId() + ".pk"));
			BigInteger gen1 = (BigInteger) ois.readObject();
			BigInteger gen2 = (BigInteger) ois.readObject();
			BigInteger publicKey = (BigInteger)ois.readObject();
			Pairing pairing = getPairing();
			Element g1 = pairing.getG1().newElementFromBytes(gen1.toByteArray()).getImmutable();
			Element g2 = pairing.getG2().newElementFromBytes(gen2.toByteArray()).getImmutable();
			//公钥先这样吧，不知道到底属于G2和是Zn
			//经测试却是应该属于G2。
			Element pk = pairing.getG2().newElementFromBytes(publicKey.toByteArray()).getImmutable();	
			
//			System.out.println("恢复时：");
//			System.out.println("g1:" + g1);
//			System.out.println("g2:" + g2);
//			System.out.println("pk:" + pk);
			
			this.publicMeta = new PublicMeta(g1, g2, pk);
			return this.publicMeta;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("从文件中获得用户公开信息时失败！");
			e.printStackTrace();
		}finally{
			if (ois != null)
			{
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 如果找不到密钥文件说明操作者不是在用户本地，即其无权获得密钥信息
	 * @return
	 * @author: YYB
	 * @Time: 下午3:47:14
	 */
	public Element getMySk()
	{
		//密钥要求的安全等级比较高，所以需要从文件中读取
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(BaseParams.skPath 
					+ this.getOwnerId() + ".sk"));
			BigInteger secretKey = (BigInteger) ois.readObject();
			Element sk = getPairing().getZr()
					.newElementFromBytes(secretKey.toByteArray()).getImmutable();
			System.out.println("成功读取密钥！");
			return sk;
		} catch (FileNotFoundException e) {
			System.out.println("未能找到密钥文件，你应该不是数据拥有者，无权获得密钥！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取密钥文件时失败！IO异常！");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("读取密钥时未找到该类型的对应！");
			e.printStackTrace();
		}finally {
			if (ois != null)
			{
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
//	public Field getZn() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataOwner [ownerId=" + ownerId + ", username=" + username + ", password=" + password + ", score="
				+ score + ", remark=" + remark + ", publicMeta=" + publicMeta + "]";
	}

	//============================================================================
	
	/**
	 * 根据用户Id为用户生成安全参数文件，公开信息和密钥信息，并保存到文件中，以便以后使用。
	 * 但一般情况下不使用。
	 * @param owner
	 * @return 是否成功完成
	 * @author: YYB
	 * @Time: 下午1:30:41
	 */
	public static boolean genParamFileAndSave(DataOwner owner)
	{
		FileWriter fw = null;
		//=======产生安全参数文件==============
		PairingParametersGenerator<PairingParameters> paramsGenerator =
				new TypeACurveGenerator(512, 1024);
		PairingParameters params = paramsGenerator.generate();
		try {
			fw = new FileWriter(BaseParams.paramPath + owner.getOwnerId() + "param.properties");
			fw.write(params.toString());
			System.out.println("生成原始参数成功，并成功保存！");
//			return true;
		} catch (IOException e) {
			System.out.println("生成原始参数文件失败！");
			e.printStackTrace();
		}finally {
			if (fw != null)
			{
				try {
					fw.close();
				} catch (IOException e) {
					System.out.println("生成原始参数后，关闭写入流失败！");
					e.printStackTrace();
				}
			}
		}
		
		
		//=================产生公开信息和密钥信息=============
		
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath 
				+ owner.getOwnerId() + "param.properties");
		Element g1 = pairing.getG1().newRandomElement().getImmutable();
		Element g2 = pairing.getG2().newRandomElement().getImmutable();
		Element sk = pairing.getZr().newRandomElement().getImmutable();
		Element pk = g2.duplicate().powZn(sk);
		
//		System.out.println("生成时：");
//		System.out.println("g1:" + g1);
//		System.out.println("g2:" + g2);
//		System.out.println("pk:" + pk);
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(BaseParams.pkPath 
					+ owner.getOwnerId() + ".pk"));
			
			BigInteger gen1 = new BigInteger(g1.toBytes());
			BigInteger gen2 = new BigInteger(g2.toBytes());
			BigInteger secretKey = new BigInteger(sk.toBytes());
			BigInteger publicKey = new BigInteger(pk.toBytes());
			
			oos.writeObject(gen1);			//保存生成元g1
			oos.writeObject(gen2);			//保存生成元g2
			oos.writeObject(publicKey);			//保存公钥
			
			oos = new ObjectOutputStream(new FileOutputStream(BaseParams.skPath 
					+ owner.getOwnerId() + ".sk"));
			oos.writeObject(secretKey);			//保存密钥

		} catch (IOException e) {
			System.out.println("保存密钥和公开信息失败！");
			e.printStackTrace();
		}finally {
			if (oos != null)
			{
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println("关闭保存密钥公开信息流时出现IO异常！");
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 数据所有者才可以生成文件的标签。生成过程主要是根据自己参数中的g1生成元和密钥sk。
	 * @param filename
	 * @return
	 * @author: YYB
	 * @Time: 下午4:37:53
	 */
	public  Element[] getTagsOfFileFromFile(String filename)
	{
		int num = FileUtils.getBlocksNumOfFile(filename);
		System.out.println("一共有" + num + "个数据块");
		Element[] blocks = FileUtils.getBlockDataFromFile(filename, getPairing().getZr());
		Element g1 = getPublicMeta().getGen1();
		Element sk = getMySk();
		System.out.println("数据块数是否还想等？" + (num == blocks.length));
		Element[] tags = new Element[num];
		for (int i = 0 ; i < num ; i++)
		{
			tags[i] = g1.duplicate().powZn(blocks[i].duplicate()).powZn(sk.duplicate());
		}
		return tags;
	}
	
	/**
	 * 生成文件标签，并保存到文件中。
	 * @param filename
	 * @return
	 * @author: YYB
	 * @Time: 下午5:13:57
	 */
	public boolean saveTagsToTagFile(String filename)
	{
		ObjectOutputStream oos = null;
		Element[] tags = getTagsOfFileFromFile(filename);
		try {
			oos = new ObjectOutputStream(new FileOutputStream(BaseParams.tagPath
					+ this.getOwnerId() + filename + ".tag"));
			BigInteger tag = null;
			for (int i = 0 ; i < tags.length ; i++)
			{
				tag = new BigInteger(tags[i].toBytes());
				oos.writeObject(tag);
			}
			System.out.println("生成保存文件" + filename + "的标签，并保存！");
			return true;
		} catch (IOException e) {
			System.out.println("保存标签失败！");
			e.printStackTrace();
		}
		return false;
	}
	
	//产生挑战
	/**
	 * 产生挑战，通过用户的信息，和文件信息。
	 * @param filename
	 * @param owner
	 * @return
	 * @author: YYB
	 * @Time: 下午5:19:50
	 */
	public Map<Integer, Element> challenge(String filename , DataOwner owner)
	{
		Map<Integer, Element> challenge = new HashMap<>();
		Field Zn = owner.getPairing().getZr();
		int num = FileUtils.getBlocksNumOfFile(filename);
		Element chal = null;
		for (int i = 0; i < num ; i++) {
			chal = Zn.newRandomElement().getImmutable();
			challenge.put(new Integer(i), chal);
		}
		return challenge;
	}
	
	
	
	/**
	 * 根据用户的公开信息，校验者的挑战信息，和CSP产生的校验证据计算数据的完整性。
	 * @param challenge
	 * @param proof
	 * @param owner
	 * @return
	 * @author: YYB
	 * @Time: 下午8:08:29
	 */
	public boolean verify(Map<Integer, Element> challenge , Element[] proof , DataOwner owner)
	{
		Element TP = proof[0];
		Element DP = proof[1];
		Pairing pairing = owner.getPairing();
		Element left = DP.duplicate().mulZn(pairing.pairing(pairing.getG1().newOneElement().getImmutable(),
				owner.getPublicMeta().getPk()));
		Element right = pairing.pairing(TP, owner.getPublicMeta().getGen2().duplicate());
		return left.equals(right);
	}
	
	//==========================下面是针对概率性校验（抽样校验）==================================
	//产生随机挑战
	/**
	 * 随机挑选n块数据块，并对每一数据库产生随机挑战数据。
	 * @param n
	 * @param filename
	 * @param owner
	 * @return
	 * @author: YYB
	 * @Time: 下午8:30:34
	 */
	public Map<Integer, Element> challenge(int n , String filename , DataOwner owner)
	{
		int num = FileUtils.getBlocksNumOfFile(filename);
		Field Zn = owner.getPairing().getZr();
		Random random = new Random();
		Element v = null;
		Map<Integer, Element> challenge = new HashMap<>();
		for (int i = 0 ; i < n ; i++)
		{
			challenge.put(new Integer(random.nextInt(num)), Zn.newRandomElement().getImmutable());
		}
		return challenge;
	}
	
	
	
	
	
	
	
	//=================测试区======================================
	public static void test()
	{
		long start = System.currentTimeMillis();;
		DataOwner owner = JdbcUtils.getOwnerFromDB(1);
//		genParamFileAndSave(owner);
//		System.out.println(owner.getPublicMeta());
		Element g1 = owner.getPublicMeta().getGen1();
		Field Zn = owner.getPairing().getZr();
		Element sk = owner.getMySk();
		String filename = "test";
//		Element[] tags = owner.getTagsOfFileFromFile(filename);
//		owner.saveTagsToTagFile(filename);
//		System.out.println("打印标签：");
//		for (int i = 0 ; i < tags.length ; i++)
//		{
//			System.out.println("标签" + i + ": " + tags[i]);
//		}
//		
//		Element[] newTags = FileUtils.getTagsFromTagFile(filename, owner);
//		System.out.println("打印新标签：");
//		for (int i = 0 ; i < tags.length ; i++)
//		{
//			System.out.println("标签" + i + ": " + newTags[i]);
//		}
//		
//		System.out.println("看看标签是否变了？");
//		for (int i = 0 ; i < tags.length ; i++)
//		{
//			System.out.println("标签" + i + "是否相等: " + (newTags[i].equals(tags[i])));
//		}
//		
		//产生挑战
		Map<Integer, Element> challenge = owner.challenge(42,filename, owner);
		Element[] proof = CloudServerProvider.getInstance().genProProof(filename, owner, challenge);
		boolean result = owner.verify(challenge, proof, owner);
		System.out.println("校验结果：" + result);
		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}
	
	
	public static void main(String[] args) {
		for(int i = 0 ; i < 5 ; i++)
		{
			test();
		}
	}
	
}

