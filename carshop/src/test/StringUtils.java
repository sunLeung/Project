package test;

import java.io.UnsupportedEncodingException;

public class StringUtils {

	// private static Log logger = LogFactory.getLog(StringUtil.class);

	// 国标码和区位码转换常量
	private static int GB_SP_DIFF = 160;
	// 存放国标一级汉字不同读音的起始区位码
	private static int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594,
			2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390,
			4558, 4684, 4925, 5249, 5600 };

	// 存放国标一级汉字不同读音的起始区位码对应读音
	private static char[] firstLetter = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J','K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z' };

	public static char convert(String ch) {
		byte[] bytes = new byte[2];
		try {
			bytes = ch.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		char result = '-';
		int secPosValue = 0;
		int i;
		for (i = 0; i < bytes.length; i++) {
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for (i = 0; i < 23; i++) {
			if (secPosValue >= secPosValueList[i]
					&& secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(secPosValueList.length);
		System.out.println(firstLetter.length);
		System.out.println(convert("梁宇新"));
		System.out.println(convert("宇新"));
		System.out.println(convert("新"));
	}
}