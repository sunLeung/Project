package utils;

import bean.UserBean;

public class TransferUtils {
	/**
	 * 创建订单ID<br>
	 * 注：有32位长度传输的时候使用，因为生成订单ID长度是20-30位。<br>
	 * 因为渠道商传参只能是数字和字母，所以生成订单ID的时候预留‘Z’，作为数据分隔符
	 * @param serverId
	 * @param loginId
	 * @param uuid
	 * @param unionID
	 * @return
	 */
	public static String createOrderId(Integer serverId, Integer loginId,
			long uuid,int unionID) {
		StringBuilder sb = new StringBuilder();
		sb.append(_10to61(serverId)).append("Z");
		sb.append(_10to61(loginId)).append("Z");
		sb.append(_10to61(uuid)).append("Z");
		sb.append(_10to61(unionID)).append("Z");
		sb.append(_10to61(System.currentTimeMillis()));
		return sb.toString();
	}

	/**
	 * 解析<br>
	 * keys：<br>
	 * sid 服务器ID<br>
	 * lid 账户ID<br>
	 * uuid 角色ID<br>
	 * unionid 渠道ID<br>
	 * timestamp 创建订单的时间<br>
	 * @param orderId
	 * @return
	 */
	public static UserBean decode(String orderId){
		String[] strs=orderId.split("Z");
		if(strs.length!=5){
			return null;
		}
		UserBean ret=new UserBean();
		long sid= _61to10(strs[0]);
		if(sid<10){
			ret.setServerid("0"+sid);
		}else{
			ret.setServerid(sid+"");
		}
		ret.setLoginid(Integer.valueOf(_61to10(strs[1])+""));;
		ret.setUuid(_61to10(strs[2]));
		ret.setUnionid(_61to10(strs[3])+"");
		ret.setPayTime(_61to10(strs[4]));
		return ret;
	}

	public static String _10to61(long i) {
		if(i == 0){
			return "0";
		}
		StringBuilder buffer = new StringBuilder();
		while (i > 0) {
			buffer.insert(0, _10to61_(i % 61));
			i /= 61;
		}
		return buffer.toString();
	}

	private static char _10to61_(long i) {
		if (i >= 0 && i <= 9) {
			i = (i + 48);
		} else if (i >= 10 && i <= 35) {
			i = (i + 87);
		} else if (i >= 36 && i <= 60) {
			i = (i + 29);
		}
		return (char) (i);
	}

	public static long _61to10(String s) {
		long i = 0;
		for (char c : s.toCharArray()) {
			if (i > 0) {
				i *= 61;
			}
			i += _61to10_(c);
		}
		return i;
	}

	/**
	 * 将单位的0-9a-zA-Y按顺序转换成数字<br>
	 * Z保留用作字符串分隔
	 */
	private static int _61to10_(char c) {
		int t = 0;
		if (c >= '0' && c <= '9') {
			t = c - 48;
		} else if (c >= 'a' && c <= 'z') {
			t = c - 87;
		} else if (c >= 'A' && c <= 'Y') {
			t = c - 29;
		}
		return t;
	}
}
