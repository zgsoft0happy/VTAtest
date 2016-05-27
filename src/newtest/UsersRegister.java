package newtest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月27日  Time: 下午1:55:04   Locate:149
 * <br/>fileName: UsersRegister.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class UsersRegister implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private static Connection conn;		//数据连接
	
	/**
	 * 获取数据块连接，如果已经存在数据块连接，直接返回，如果没有则创建数据库连接并返回，
	 * 数据块连接的配置文件为mysql.ini
	 * @return
	 * @author: YYB
	 * @Time: 下午2:02:10
	 */
	public static Connection getConn()
	{
		if (conn != null){
			return conn;
		}
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("mysql.ini"));
			String driver = props.getProperty("driver");
			String username = props.getProperty("user");
			String password = props.getProperty("password");
			String url = props.getProperty("url");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("成功获得连接");
			return conn;
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println("呜呜呜！获取数据库连接失败！");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据用户名，用户密码，备注信息，注册用户
	 * @param username
	 * @param password
	 * @param remark
	 * @return
	 * @author: YYB
	 * @Time: 下午2:14:28
	 */
	public static boolean register(String username , String password , String remark)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into users(username , password , remark) values('" 
					+ username + "','" + password + "','" + remark + "')";
			int num = stmt.executeUpdate(sql);
			System.out.println("成功注册用户" + num + "名！");	
			return true;
		} catch (SQLException e) {
			System.out.println("注册用户失败，数据块异常！");
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 根据用户名，用户密码注册用户
	 * @param username
	 * @param password
	 * @return
	 * @author: YYB
	 * @Time: 下午2:16:43
	 */
	public static boolean register(String username ,String password)
	{
		return register(username , password , "");
	}
	/**
	 * 根据用户本身进行注册，为防止用户已经存在，后续要加上是否已经存在数据库的判断
	 * @param user
	 * @return
	 * @author: YYB
	 * @Time: 下午2:18:31
	 */
	public static boolean register(Users user)
	{
		return register(user.getUsername() , user.getPassword(),user.getRemark());
	}
	
	/**
	 * 根据用户ID从数据库获取用户
	 * @param userId
	 * @return
	 * @author: YYB
	 * @Time: 下午2:43:26
	 */
	public static Users getUserFronDBById(int userId)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "select * from users where userId = " + userId;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				return new Users(rs.getInt("userId") , rs.getString("username") , rs.getString("password")
						,rs.getInt("score") , rs.getString("remark"));
			}
		} catch (SQLException e) {
			System.out.println("从数据库获取用户失败！");
			e.printStackTrace();
		}
		return null;
	}
	
	
	//=====================================下边是测试区===========================
	/**
	 * 测试数数据库连接
	 * 
	 * @author: YYB
	 * @Time: 下午2:03:54
	 */
	public static void test()
	{
		System.out.println(getConn());
	}
	
	public static void test1()
	{
		String username = "东华";
		String password = "123456";
		String remark = "空";
		System.out.println("是否注册成功：" + register(username, password, remark));
		
	}
	
	public static void main(String[] args) {
		test();
		test1();
	}
}

