package taskassign;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import newtest1.CloudServerProvider;
import newtest1.DataOwner;
import newtest1.JdbcUtils;

/**
 * <br/>CSDN主页：<a href="http://my.csdn.net/y1193329479">CSDN主页</a>
 * <br/>Copyright (C), 2016-2017, YYB , Thomas
 * <br/>This program is protected by copyright laws.
 * <br/>Programe Name:
 * <br/>Date: 2016年6月2日  Time: 下午7:41:40   Locate:149
 * <br/>fileName: CSP.java
 * @author yyb zgsoft_happy@126.com
 * @version 1.0
 * description：
 */

public class CSP implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public Map<Integer, Map> genProof(Map<Integer, List<VerifyBlock>> task , Map<Integer, Map> challenge)
	{
		DataOwner owner = JdbcUtils.getOwnerFromDB(2);
		CloudServerProvider csp = new CloudServerProvider();
//		csp.genProof(filename, owner, challenge)
		return null;
	}
}

