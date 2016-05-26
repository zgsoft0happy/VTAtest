package test;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.FileLockInterruptionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

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
 * <br/>Date: 2016年5月26日  Time: 下午2:34:02   Locate:149
 * <br/>fileName: UsersReg.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：注册新用户
 */

public class UsersReg implements Serializable {

	private static Connection conn;
	
	/**
	 * 获得数据库连接
	 * @return
	 * @author: YYB
	 * @Time: 下午3:21:02
	 */
	public static Connection getConnection()
	{
		Properties props = new Properties();
		try {
			props.load(new FileReader("mysql.ini"));
			String driver = props.getProperty("driver");
			String user = props.getProperty("user");
			String password = props.getProperty("password");
			String url = props.getProperty("url");
			if (conn == null)
			{
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);
			}
			return conn;
		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void register(String username , String password , String remark)
	{
		Connection conn = getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement("insert into users(username , password, remark) values(?,?,?)");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			if(remark==null)
			{
				remark = "";
			}
			pstmt.setString(3, remark);
			System.out.println("zhe;;i");
			int num = pstmt.executeUpdate();
			System.out.println("成功插入" + num + "行！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 下面是根据用户ID产生用户的安全参数文件，针对某一用户的安全文件的命名规则是userId+"param.properties"
	 * @param user
	 * @return 是否成功产生并保存安全参数到文件中
	 * @author: YYB
	 * @Time: 下午3:36:34
	 */
	public static boolean genParamFileAndSave(Users user)
	{
		PairingParametersGenerator<PairingParameters> paramGenerator=
				new TypeACurveGenerator(512, 1024);
		PairingParameters params = paramGenerator.generate();
		FileWriter fw = null;
		try {
			fw = new FileWriter("user"+user.getUserId() + "param.properties");
			fw.write(params.toString());
			return true;
		} catch (IOException e) {
			System.out.println("产生参数和保存时失败！");
			e.printStackTrace();
		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				System.out.println("关闭安全参数文件写入流异常！");
				e.printStackTrace();
			}
		}
		return false;
	}
	

	/**
	 * 根据用户ID从数据库中获取用户
	 * @param userId
	 * @return 返回一个各个参数和数据库一致的用户
	 * @author: YYB
	 * @Time: 下午3:56:50
	 */
	public static Users getUserFromDB(int userId){
		try {
			PreparedStatement pstmt = getConnection().prepareStatement("select * from users where userId = ?");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				String username = rs.getString("username");
				String password = rs.getString("password");
				int score = rs.getInt("score");
				String remark = rs.getString("remark");
				return new Users(userId, username, password, score, remark);
			}
		} catch (SQLException e) {
			//从数据库获取用户失败
			System.out.println("从数据库获取用户失败");
			e.printStackTrace();
		}
		System.out.println("未从数据库查到此用户！");
		return null;
	}
	
	public static boolean savePKToFile(Users user , PublicMessage publicMes)
	{
//		BigInteger sk = new BigInteger(secretKey.toBytes());
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("user" + user.getUserId() + ".pk"));
			oos.writeObject(publicMes);
			System.out.println("保存" + user.getUsername() + "的公钥等公开信息成功");
//			oos = new ObjectOutputStream(new FileOutputStream("user" + user.getUserId() + ".sk"));
//			oos.writeObject(sk);
//			System.out.println("保存" + user.getUsername() + "的密钥成功");
		} catch (IOException e) {
			System.out.println("保存公钥到文件失败！");
			e.printStackTrace();
		}finally {
			try {
				oos.close();
			} catch (IOException e) {
				System.out.println("关闭保存公钥对象输出流失败！");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//设置用户自己的公钥密钥对到文件
	public static boolean setUserPkAndSk(Users user)
	{
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing = PairingFactory.getPairing("user" + user.getUserId() + "param.properties");
		
		Field G1 = pairing.getG1();
		Field G2 = pairing.getG2();
		Field Zn = pairing.getZr();
		
		Element g1 = G1.newRandomElement();
		Element g2 = G2.newRandomElement();
		
		//转换成大整数保存
		BigInteger gen1 = new BigInteger(g1.toBytes());
		BigInteger gen2 = new BigInteger(g2.toBytes());
		
		Element sk = Zn.newRandomElement();
		Element pk = g2.pow(gen1);
		
		//转换成大整数
		BigInteger secretKey = new BigInteger(sk.toBytes());
		BigInteger publicKey = new BigInteger(pk.toBytes());
		
		PublicMessage publicMessage = new PublicMessage(user, publicKey, gen1, gen2);
		
		//将公开信息和密钥写入文件
		ObjectOutputStream oos = null;
		try {
			//保存公开信息
			oos = new ObjectOutputStream(new FileOutputStream("user" + user.getUserId() + ".pk"));
			oos.writeObject(publicMessage);
			System.out.println("成功写入并保存公共信息");
			//保存密钥信息
			oos = new ObjectOutputStream(new FileOutputStream("user" + user.getUserId() + ".sk"));
			oos.writeObject(secretKey);
			System.out.println("成功写入并保存密钥信息");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static void main(String[] args)
	{
//		System.out.println(new UsersReg().getConnection());
//		register("YY", "123456", "12");
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		
		System.out.println(getUserFromDB(2));
		Users user = getUserFromDB(2);
		System.out.println(genParamFileAndSave(user));

		Pairing pairing = PairingFactory.getPairing("user" + user.getUserId() + "param.properties");
		Element publicKey = pairing.getG1().newRandomElement();
		setUserPkAndSk(user);
//		savePKToFile(user, publicKey);
	}
}

