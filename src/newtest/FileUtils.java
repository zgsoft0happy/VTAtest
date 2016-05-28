package newtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月27日  Time: 下午4:51:09   Locate:149
 * <br/>fileName: FileUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：主要是对文件的一些操作。
 */

public class FileUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * 获得文件所包含的数据块数量
	 * @param filename
	 * @return
	 * @author: YYB
	 * @Time: 下午5:04:12
	 */
	public static int getBlockNumOfFile(String filename)
	{
		File file = new File(filename);
		long fileLength = file.length();
		int result = (int) (fileLength / BaseParams.blockSize);
		int remain = (int) (fileLength % BaseParams.blockSize);
		return remain > 0 ? result + 1 : result;
	}
	
	public static Element[] getBlockDatasFromFile(String filename , Field Zn)
	{
		int num = getBlockNumOfFile(filename);
		Element[] data = new Element[num];
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filename, "r");
			byte[] buffer = new byte[BaseParams.blockSize];
			for (int i = 0 ; i < num - 1 ; i++)
			{
				raf.read(buffer);
				data[i] = Zn.newElementFromBytes(buffer);
			}
			int remain = raf.read(buffer);
			if(remain < BaseParams.blockSize)
			{
				for ( int i = remain ; i < BaseParams.blockSize ; i++)
				{
					buffer[i] = 0;
				}
			}
			data[num - 1] = Zn.newElementFromBytes(buffer);
			System.out.println("获取文件分块数据成功！");
			return data;
		} catch (IOException e) {
			System.out.println("获得文件的块数据时没有找到文件或者其他IO异常");
			e.printStackTrace();
		}finally {
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static BigInteger[] genTagsOfFile(String filename , Users user)
	{
		Pairing pairing = user.getMyPairing();
		Field Zn = pairing.getZr();
		Element[] blocks = getBlockDatasFromFile(filename, Zn);
		
		//下面是为文件的数据块产生标签过程
		BigInteger sk = user.getMySk();
		BigInteger gen1 = user.getMyGen1();
		BigInteger[] tags = new BigInteger[blocks.length];
		for (int i = 0 ; i < tags.length ; i++)
		{
			tags[i] = new BigInteger(user.getMyG1().newElementFromBytes(gen1.toByteArray())
					.pow(new BigInteger(blocks[i].toBytes()))
					.pow(sk).toBytes());
			if(i % 30 == 0)
			{
				System.out.println("30的倍数，休息一下，你放心！");
			}
		}
		return tags;
	}
	
}

