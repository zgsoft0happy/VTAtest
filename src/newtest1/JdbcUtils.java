package newtest1;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import taskassign1.AssignParams;
import taskassign2.Task;



/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月29日  Time: 下午1:33:48   Locate:149
 * <br/>fileName: JdbcUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：主要是操作数据库，数据库主要保存的是用户的身份信息。
 */

public class JdbcUtils implements Serializable {

	public static final long serialVersioUID = 1L;
	
	private static Connection conn;
	
	/**
	 * 获得一个数据库连接，单例模式。
	 * @return
	 * @author: YYB
	 * @Time: 下午2:22:52
	 */
	public static Connection getConn()
	{
		if (conn != null)
		{
			return conn;
		}
		Properties props = new Properties();
		try {
			props.load(new FileReader("mysql.ini"));
			String driver = props.getProperty("driver");
			String user = props.getProperty("user");
			String password = props.getProperty("password");
			String url = props.getProperty("url");
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
//			System.out.println("成功获得数据库连接");
			return conn;
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println("获取数据库连接失败！");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过用户名，用户密码，备注信息注册新的用户（数据拥有者）。
	 * @param username
	 * @param password
	 * @param remark
	 * @return
	 * @author: YYB
	 * @Time: 下午2:23:14
	 */
	public static boolean register(String username , String password , String remark)
	{
		String sql = "insert into dataowner(username , password , remark) values(?,?,?)";
		try {
			PreparedStatement pstmt = getConn().prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, remark);
			int result = pstmt.executeUpdate();
			System.out.println("成功注册" + result + "个人");
		} catch (SQLException e) {
			System.out.println("注册失败！");
			e.printStackTrace();
		}
		return false;		
	}
	/**
	 * 通过用户名，用户密码注册新的用户（数据拥有者）。
	 * @param username
	 * @param password
	 * @param remark
	 * @return
	 * @author: YYB
	 * @Time: 下午2:23:14
	 */
	public static boolean register(String username , String password)
	{
		String remark = "";
		return register(username, password,remark);
	}
	
	/**
	 * 从数据库中根据用户Id获得数据所有者。
	 * @param ownerId
	 * @return
	 * @author: YYB
	 * @Time: 下午2:39:41
	 */
	public static DataOwner getOwnerFromDB(int ownerId)
	{
		String sql = "select * from dataowner where id = ?";
		try {
			PreparedStatement pstmt = getConn().prepareStatement(sql);
			pstmt.setInt(1, ownerId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				DataOwner dataOwner = new DataOwner(rs.getInt("id"),
						rs.getString("username"), rs.getString("password"),
						rs.getInt("score"), rs.getString("remark"));
//				System.out.println("成功从数据库获得用户并返回！");
				return dataOwner;
			}
			System.out.println("数据库没有此用户！");
		} catch (SQLException e) {
			System.out.println("从数据获取用户时出现SQL异常！");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//==================价值和时间的记录到数据库===============
	public static boolean savetimeAndValue(long time , int value)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_value(time , value) values('" + time + "','" + value +"')";
			int num = stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("插入时间和价值时出现错误！");
			e.printStackTrace();
		}
		return false;
	}
	public static boolean savetimeAndValue1(long time , int value)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_value(time1 , value1) values('" + time + "','" + value +"')";
			int num = stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("插入时间和价值时出现错误！");
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 将校验的数量和校验时间保存到数据库。
	 * @param num
	 * @param time
	 * @return
	 * @author: YYB
	 * @Time: 下午7:15:27
	 */
	public static boolean saveNumAndTime(int num , long time , double flag)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_num_time(num , time , flag) "
					+ "values('" + num + "','" + time + "','" + flag + "')";
			int result = stmt.executeUpdate(sql);
//			System.out.println("插入" + result + "行。");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 将校验的数量和校验时间保存到数据库，以及是否满足时间。
	 * @param num
	 * @param time
	 * @return
	 * @author: YYB
	 * @Time: 下午8:17:47
	 */
	public static boolean saveNumAndTime2(int num , long time ,	long theoryTime ,
			byte result , double rate ,int value , double flag)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_num_time2(num , time , theoryTime , result , rate ,"
					+ " value , flag, deadlinewight , valuewight) values('" + num + "','" + 
					time + "','" + theoryTime +	"','" + result + "','" + rate + "','" + 
					value + "','" + flag + "','" +
					AssignParams.deadlineWight + "','" + AssignParams.valueWight +"')";
			int r = stmt.executeUpdate(sql);
//			System.out.println("插入" + r + "行。");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean saveNumAndTime3(int num , long time ,	long theoryTime ,
			byte result , double rate ,int value , double missed)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_num_time_rate(num , time , theoryTime ,  rate ,"
					+ " value , missed ,result , deadlinewight , valuewight) values('" + 
					num + "','" + time + "','" + theoryTime + "','" + rate + "','" + 
					value + "','" + missed + "','" + result + "','" + 
					AssignParams.deadlineWight + "','" + AssignParams.valueWight +"')";
			int r = stmt.executeUpdate(sql);
//			System.out.println("插入" + r + "行。");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public static boolean saveNumAndTime32(int num , long time ,	long theoryTime ,
			byte result , double rate ,int value , double missed)
	{
		try {
			Statement stmt = getConn().createStatement();
			String sql = "insert into t_num_time_rate2(num , time , theoryTime ,  rate ,"
					+ " value , missed ,result , deadlinewight , valuewight) values('" + 
					num + "','" + time + "','" + theoryTime + "','" + rate + "','" + 
					value + "','" + missed + "','" + result + "','" + 
					AssignParams.deadlineWight + "','" + AssignParams.valueWight +"')";
			int r = stmt.executeUpdate(sql);
//			System.out.println("插入" + r + "行。");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public static void insertEDFResult(Task task , boolean result , 
			long tatoltime , int tatolbolocknum , int tatolvalue)
	{
		int num = task.blocks.size();
		int value = task.value;
		String result1 = "0";
		if (result)
		{
			value = 0;
			result1 = "1";
		}
		String sql = "insert into EDF_table(num , value , result,"
				+ "tatoltime , tatolblocknum , tatolvalue) values('"
				+ num + "','" + value + "','" + result1 
				+ "','" + tatoltime + "','" + tatolbolocknum + "','"
				+ tatolvalue + "')";
		try {
			Statement stmt = getConn().createStatement();
			int n = stmt.executeUpdate(sql);
			System.out.println("成功插入EDF_table中" + n + "条记录！");
		} catch (SQLException e) {
			System.out.println("插入EDF_table时出现错误！");
			e.printStackTrace();
		}
	}
	
	
	public static void insertHVFResult(Task task , boolean result , 
			long tatoltime , int tatolbolocknum , int tatolvalue)
	{
		int num = task.blocks.size();
		int value = task.value;
		String result1 = "0";
		if (result)
		{
			value = 0;
			result1 = "1";
		}
		String sql = "insert into HVF_table(num , value , result,"
				+ "tatoltime , tatolblocknum , tatolvalue) values('"
				+ num + "','" + value + "','" + result1 
				+ "','" + tatoltime + "','" + tatolbolocknum + "','"
				+ tatolvalue + "')";
		try {
			Statement stmt = getConn().createStatement();
			int n = stmt.executeUpdate(sql);
			System.out.println("成功插入HVF_table中" + n + "条记录！");
		} catch (SQLException e) {
			System.out.println("插入HVF_table时出现错误！");
			e.printStackTrace();
		}
	}
	
	public static void insertDPAResult(Task task , boolean result , 
			long tatoltime , int tatolbolocknum , int tatolvalue)
	{
		int num = task.blocks.size();
		int value = task.value;
		String result1 = "0";
		if (result)
		{
			value = 0;
			result1 = "1";
		}
		String sql = "insert into DPA_table(num , value , result,"
				+ "tatoltime , tatolblocknum , tatolvalue) values('"
				+ num + "','" + value + "','" + result1 
				+ "','" + tatoltime + "','" + tatolbolocknum + "','"
				+ tatolvalue + "')";
		try {
			Statement stmt = getConn().createStatement();
			int n = stmt.executeUpdate(sql);
			System.out.println("成功插入DPA_table中" + n + "条记录！");
		} catch (SQLException e) {
			System.out.println("插入DPA_table时出现错误！");
			e.printStackTrace();
		}
	}
	
	//===============================================================================
	public static void test()
	{
		System.out.println(getConn());
		register("yyy", "123456", "代码注册");
		System.out.println(getOwnerFromDB(2));
	}
	public static void main(String[] args) {
		test();
	}

}

