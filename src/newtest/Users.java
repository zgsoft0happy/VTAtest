package newtest;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

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
		return publicMeta;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Users [userId=" + userId + ", username=" + username + ", password=" + password + ", score=" + score
				+ ", remark=" + remark + ", publicMeta=" + publicMeta + "]";
	}

	//===================用户生成安全参数并保存的区域=======================
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
			Pairing pairing = PairingFactory.getPairing(BaseParams.paramPath + this.getUserId() + "param.properties");
			
			Field G1 = pairing.getG1();
			Field G2 = pairing.getG2();
			Field Zn = pairing.getZr();					
			
			BigInteger gen1 = new BigInteger(G1.newRandomElement().toBytes());		//群G1的生成元
			BigInteger gen2 = new BigInteger(G2.newRandomElement().toBytes());		//群G2的生成元
			
			BigInteger sk = new BigInteger(Zn.newRandomElement().toBytes());	//密钥
			BigInteger pk = new BigInteger(G2.newElementFromBytes(gen2.toByteArray()).pow(sk).toBytes());//公钥
			
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
	
	//=====================测试区==================
	public static void test()
	{
		Users user = UsersRegister.getUserFronDBById(10);
		System.out.println(user);
//		user.genParamAndSave();
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
	
	public static void main(String[] args) {
		test();
	}
}

