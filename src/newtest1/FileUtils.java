package newtest1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import newtest1.BaseParams;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年5月28日  Time: 下午7:58:37   Locate:149
 * <br/>fileName: FileUtils.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class FileUtils implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static int getBlocksNumOfFile(String filename)
	{
		File file = new File(filename);
		long fileLength = file.length();
		int result = (int) (fileLength / BaseParams.blockSize);
		int remain = (int) (fileLength % BaseParams.blockSize);
		return remain > 0 ? result + 1 : result;	
	}
	
	public static Element[] getBlockDataFromFile(String filename , Field Zn)
	{
		int num = getBlocksNumOfFile(filename);
		Element[] blocks = new Element[num];
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filename, "r");
			byte[] buffer = new byte[BaseParams.blockSize];
			for (int i = 0 ; i < num - 1 ; i++)
			{
				raf.read(buffer);
				blocks[i] = Zn.newElementFromHash(buffer , 0 , BaseParams.blockSize).getImmutable();
			}
			int remain = raf.read(buffer);
			if(remain < BaseParams.blockSize)
			{
				for ( int i = remain ; i < BaseParams.blockSize ; i++)
				{
					buffer[i] = 0;
				}
			}
			blocks[num - 1] = Zn.newElementFromHash(buffer , 0 , BaseParams.blockSize).getImmutable();
			System.out.println("获取文件分块数据成功！");
			return blocks;
		} catch (IOException e) {
			System.out.println("获得数据块数据时失败");
			e.printStackTrace();
		}finally {
			try {
				raf.close();
			} catch (IOException e) {
				System.out.println("获得数据块数据时失败，关闭时失败");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Element[] getTagsFromTagFile(String filename , DataOwner owner)
	{
		Field Zn = owner.getPairing().getG1();		//标签的数值属于群G1中的元素。
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(BaseParams.tagPath 
					+ owner.getOwnerId() + filename + ".tag"));
			int num = getBlocksNumOfFile(filename);			//这里修改一下，如果数量不对。。。。
			Element[] tags = new Element[num];
			BigInteger tag = null;
			for (int i = 0 ; i < num ; i++)
			{
				tag = (BigInteger) ois.readObject();
				tags[i] = Zn.newElementFromBytes(tag.toByteArray()).getImmutable();
			}
			System.out.println("成功读取标签！");
			return tags;
		} catch (IOException e) {
			System.out.println("从标签文件中读取标签失败！");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("没有找到对应对象");
		}
		return null;
	}
	
}

