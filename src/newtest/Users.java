package newtest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.ChangedCharSetException;

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
 * <br/>Date: 2016年5月27日  Time: 下午1:46:47   Locate:149
 * <br/>fileName: Users.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class Users implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private int userId;		//用户ID
	private String username;		//用户名
	private String password;		//用户密码
	private int score;				//用户积分
	private String remark; 			//用户备注信息
	
	private PublicMeta publicMeta;	//用户公开数据

	public Users(String username, String password, String remark) {
		super();
		this.username = username;
		this.password = password;
		this.remark = remark;
	}

	public Users(int userId, String username, String password, int score, String remark) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.score = score;
		this.remark = remark;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
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
					+ this.getUserId() + ".pk"));
			PublicMeta pm = (PublicMeta) ois.readObject();
			System.out.println("成功获取用户的公开信息！");
			return pm;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("获取用户的公开信息失败！");
			e.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return publicMeta;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username + ", password="
				+ password + ", score=" + score
				+ ", remark=" + remark + ", publicMeta=" + publicMeta + "]";
	}

	//===================用户生成安全参数并保存的区域=======================
	
	
	
	/**
	 * 获得用户自己的公钥。
	 * @return
	 * @author: YYB
	 * @Time: 下午7:45:38
	 */
	public Element getMyPk()
	{
		return this.getUserPk(this);
	}
	/**
	 * 获取用户user的公钥。
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:44:48
	 */
	public Element getUserPk(Users user)
	{
		return user.getMyG2().newElementFromBytes(user.getPublicMeta().getPk().toByteArray()).getImmutable();
	}
	/**
	 * 获得用户自己的群G2的生成元
	 * @return
	 * @author: YYB
	 * @Time: 下午7:42:45
	 */
	public Element getMyGen2()
	{
		return this.getUserGen2(this);
	}
	/**
	 * 获取用户user的群G2的生成元
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:40:46
	 */
	public Element getUserGen2(Users user)
	{
		return user.getMyG2().newElementFromBytes(user.getPublicMeta().getGen2().toByteArray()).getImmutable();
	}
	/**
	 * 获得用户自己的群G1的生成元
	 * @return
	 * @author: YYB
	 * @Time: 下午7:39:41
	 */
	public Element getMyGen1()
	{
		return this.getUserGen1(this);
	}
	/**
	 * 获取用户user的群G1的生成元gen1
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:22:42
	 */
	public Element getUserGen1(Users user)
	{
		return user.getMyG1().newElementFromBytes(user.getPublicMeta().getGen1().toByteArray()).getImmutable();
	}
	/**
	 * 获得用户自己的密钥
	 * @return
	 * @author: YYB
	 * @Time: 下午7:20:13
	 */
	public Element getMySk()
	{
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(BaseParams.skPath + this.getUserId() + ".sk"));
			Element sk = this.getMyZn().newElementFromBytes(((BigInteger) ois.readObject()).toByteArray());
			System.out.println("成功获得密钥！惊喜吧！");
			return sk;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("对不起未找到配置文件，可能是你没有权限获得用户的私钥！");
			e.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * 获得用户自己的参数中的域Zn。
	 * @return
	 * @author: YYB
	 * @Time: 下午8:01:42
	 */
	public Field getMyZn()
	{
		return this.getUserZn(this);
	}
	/**
	 * 获取用户user的参数中的域Zn。
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午8:00:54
	 */
	public Field getUserZn(Users user)
	{
		return getUserPairing(user).getZr();
	}
	/**
	 * 获取用户自己的参数中的群GT。
	 * @return
	 * @author: YYB
	 * @Time: 下午7:59:59
	 */
	public Field getMyGT()
	{
		return this.getUserGT(this);
	}
	/**
	 * 获取用户user的参数的群GT。
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:59:04
	 */
	public Field getUserGT(Users user)
	{
		return getUserPairing(user).getGT();
	}
	/**
	 * 获取用户自己的参数中的群G2。
	 * @return
	 * @author: YYB
	 * @Time: 下午7:57:58
	 */
	public Field getMyG2()
	{
		return this.getUserG2(this);
	}
	/**
	 * 获取用户user的参数中的群G2。
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:57:06
	 */
	public Field getUserG2(Users user)
	{
		return getUserPairing(this).getG2();
	}
	/**
	 * 获取用户自己的参数中的群G1。
	 * @return
	 * @author: YYB
	 * @Time: 下午7:53:39
	 */
	public Field getMyG1()
	{
		return this.getUserG1(this);
	}
	/**
	 * 获得用户user的参数中的群G1。
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午7:52:44
	 */
	public Field getUserG1(Users user)
	{
		return getUserPairing(user).getG1();
	}
	
	/**
	 * 获取用户user的Pairing对象
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午6:52:40
	 */
	public Pairing getUserPairing(Users user)
	{
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath
				+ user.getUserId() + "param.properties");
		return pairing;
	}
	
	/**
	 * 获得用户自己的Pairing对象
	 * @return
	 * @author: YYB
	 * @Time: 下午6:53:28
	 */
	public Pairing getMyPairing()
	{
		return getUserPairing(this);
	}
	/**
	 * 生成*param.properties文件，保存起来，以便以后使用
	 * @return
	 * @author: YYB
	 * @Time: 下午6:47:42
	 */
	public boolean genParamAndSave()
	{
		
		//生成参数
		PairingParametersGenerator<PairingParameters> paramsGenerator = new TypeACurveGenerator(512, 1024);
		PairingParameters params = paramsGenerator.generate();
		
		//将参数保存到文件
		FileWriter fw = null;
		try {
			fw = new FileWriter(BaseParams.paramPath + this.getUserId() + "param.properties");
			fw.write(params.toString());
			System.out.println("保存安全参数成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//生成私钥和公开信息并保存
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(BaseParams.skPath
					+ this.getUserId() + ".sk"));
			//通过配置文件获得公钥和密钥信息，并保存
			PairingFactory.getInstance().setUsePBCWhenPossible(true);
			Pairing pairing = this.getMyPairing();
			
			Field G1 = pairing.getG1();
			Field G2 = pairing.getG2();
			Field Zn = pairing.getZr();					
			
			BigInteger gen1 = new BigInteger(G1.newRandomElement().getImmutable().toBytes());		//群G1的生成元
			BigInteger gen2 = new BigInteger(G2.newRandomElement().getImmutable().toBytes());		//群G2的生成元
			
			BigInteger sk = new BigInteger(Zn.newRandomElement().getImmutable().toBytes());	//密钥
			BigInteger pk = new BigInteger(G2.newElementFromBytes(gen2.toByteArray())
					.getImmutable().powZn(Zn.newElementFromBytes(sk.toByteArray()).getImmutable())
					.toBytes());//公钥
			
			this.publicMeta =  new PublicMeta(gen1, gen2, pk);
			
			//开始写入文件
			oos.writeObject(pk);
			System.out.println("写入私钥成功");
			
			oos = new ObjectOutputStream(new FileOutputStream(BaseParams.pkPath + this.getUserId() + ".pk"));
			oos.writeObject(this.publicMeta);
			System.out.println("写入公开信息成功！");
			return true;
		} catch (IOException e) {
			System.out.println("写入公开信息以及私钥信息失败！");
			e.printStackTrace();
		}finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//======================检验者工作区域=============================
	/**
	 * 产生对文件filename的挑战，测试所以是全部，不是抽样
	 * @param filename
	 * @param Zn
	 * @return
	 * @author: YYB
	 * @Time: 下午8:28:38
	 */
	public Map<Integer, Element> genChallenge(String filename, Field Zn)
	{
		Integer key = null;
		Element value = null;
		Map<Integer, Element> challenge = new HashMap<>();
		for (int i = 0 ; i < FileUtils.getBlockNumOfFile(filename) ; i++)
		{
			key = new Integer(i);
			value = Zn.newRandomElement().getImmutable();
			challenge.put(key, value);
		}
		System.out.println("挑战产生成功！");
		return challenge;
	}
	/**
	 * 验证数据证据，得到文件数据是否完整
	 * @param challenge
	 * @param proof
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午9:22:54
	 */
	public boolean verify(Map<Integer, Element> challenge , Element[] proof , Users user)
	{
		Element left = proof[1].duplicate().mulZn(user.getMyPairing().pairing(user.getMyG1()
				.newOneElement().duplicate(),user.getMyPk().duplicate()));
		Element right = user.getMyPairing().pairing(proof[0].duplicate(), 
				user.getMyGen2().duplicate());
		return left.isEqual(right);
	}
	//===========================服务器工作区=======================
	/**
	 * 根据挑战者的挑战数据获取的对应文件的证据
	 * @param challenge
	 * @param filename 
	 * @param user 产生标签文件的用户，不是校验者。
	 * @return
	 * @author: YYB
	 * @Time: 下午9:13:08
	 */
	public Element[] genProof(Map<Integer, Element> challenge , String filename ,Users user)
	{
		Element TP = null;
		Element[] tags = FileUtils.genTagsOfFile(filename, user);
		Element[] blocks = FileUtils.getBlockDatasFromFile(filename, user.getMyZn());
		TP = user.getMyG1().newOneElement().getImmutable().duplicate()
				.powZn(user.getMyZn().newOneElement().getImmutable().duplicate());
		for (int i = 0 ; i < tags.length ; i++){
			TP = TP.mul(tags[i].duplicate().powZn(challenge.get(new Integer(i))).duplicate());
		}
		Element MP = user.getMyZn().newOneElement().getImmutable().duplicate()
				.mul(user.getMyZn().newOneElement().getImmutable().duplicate());
		for (int i = 0 ; i < tags.length ; i++){
			MP = MP.add(blocks[i].duplicate().mulZn(challenge.get(new Integer(i))).duplicate());
		}
		Element DP = user.getMyPairing()
				.pairing(user.getMyGen1().getImmutable(), 
						user.getMyPk().getImmutable()).powZn(MP.getImmutable());
		return new Element[]{TP,DP};
	}
	//=====================测试区==================
	public static void test()
	{
		Users user = UsersRegister.getUserFronDBById(8);
		System.out.println(user);
		user.genParamAndSave();
		String filename = "test.avi";
		int num = FileUtils.getBlockNumOfFile(filename);
		System.out.println("一共有" + num + "块数据块！");
		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath + user.getUserId() + "param.properties");
		Field Zn = pairing.getZr();
		Element[] blocks = FileUtils.getBlockDatasFromFile(filename, Zn);
		for (int i = 0 ; i < blocks.length ; i++)
		{
			System.out.println("数据块" + i + ": " + blocks[i]);
		}
		
		
	}
	public static void test1()
	{
		Users user = UsersRegister.getUserFronDBById(8);
		System.out.println(user);
		String filename = "test";
		Element[] tags = FileUtils.genTagsOfFile(filename, user);
		System.out.println("生成标签成功");
		for ( int i = 0 ; i < tags.length ; i++)
		{
			 System.out.println("标签" + i + ": " + tags[i]);
		}
	}
	public static void test2()
	{
		Users user = UsersRegister.getUserFronDBById(8);
		System.out.println(user);
		String filename = "test";
		Field Zn = user.getMyZn();
		Map challenge = user.genChallenge(filename, Zn);
		System.out.println("获取挑战成功！");
		for(int i = 0 ; i < challenge.size();i++)
		{
			System.out.println("" + i + "-->" + challenge.get(new Integer(i)));
		}
		Element[] proof = user.genProof(challenge, filename, user);
		System.out.println("成功生成证据");
		System.out.println(proof[0] + "\n" + proof[1]);
		System.out.println("下面开始验证");
		System.out.println(user.verify(challenge, proof, user));;
	}
	public static void test3()
	{
		Users user = UsersRegister.getUserFronDBById(8);
		System.out.println("用户：" + user);
		Pairing pairing = user.getMyPairing();
		Element in1 = pairing.getG1().newRandomElement().getImmutable();
		Element in2 = pairing.getG2().newRandomElement().getImmutable();
		
		Element out1 = pairing.pairing(in1, in2);
		Element rand = pairing.getZr().newRandomElement().getImmutable();
		BigInteger big = new BigInteger(rand.toBytes());
		Element out2 = pairing.pairing(in1.powZn(rand), in2);
		Element out3 = pairing.pairing(in1, in2.powZn(rand));
		System.out.println("out2和out3是否相等：" + out2.equals(out3));
		BigInteger big1 = new BigInteger(out2.toBytes());
		BigInteger big2 = new BigInteger(out3.toBytes());
		System.out.println("big1: " + big1);
		System.out.println("big2: " + big2);
		System.out.println("big1==big2?" + big1.equals(big2));
		
	}
	public static void main(String[] args) {
		test2();
//		Users user = UsersRegister.getUserFronDBById(8);
//		System.out.println(user);
//		Pairing pairing2 = user.getMyPairing();
//		PairingFactory.getInstance().setUsePBCWhenPossible(true);
//		Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath + user.getUserId() + "param.properties");
//		System.out.println(pairing.equals(pairing2));
//		PairingFactory.getInstance().setUsePBCWhenPossible(true);
//		Pairing pairing3 = PairingFactory.getPairing(BaseParams.paramPath + user.getUserId() + "param.properties");
//		System.out.println(pairing.equals(pairing3));
//		System.out.println(pairing2.equals(pairing3));
//		System.out.println("==========G1============");
//		Field G11 = pairing.getG1();
//		Field G21 = pairing2.getG1();
//		Field G31 = pairing3.getG1();
//		System.out.println(G11.equals(G21));
//		System.out.println(G11.equals(G31));
//		System.out.println(G21.equals(G31));
//		System.out.println("===========pairing映射=========");
		
	}
}

