所有用户的配置文件统一定为："user" + user.getUserId() + "param.properties";
所有用户的私钥文件统一定为："user" + user.getUserId() + ".sk";
所有用户的公钥文件统一定为："user" + user.getUserId() + ".pk";

而所有生成的文件分别保存在三个文件夹中，这三个文件夹的位置在类BaseParam中设置。

由于数据库的连接只涉及到用户（暂时），所以把所有有关数据库的操作放在了UsersReg类中

由于JPBC 的Element是不可序列化的，本工程只是把其转化成BigInteger类型后保存的。
一般情况下，Element和BigInteger之间的转换是通过byte[]数组完成，即：
BigInteger big;
Element e;
big = new BigInteger(e.toBytes());
e = field.newElementFromBytes(big.toByteArray());
//////
