package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class UUID {

	private final static int max_auto_id = 9000000;

	private static final AtomicInteger[] _uuidCount = new AtomicInteger[10];
	
	static {
		for (int i = 0; i < _uuidCount.length; i++)
			_uuidCount[i] = new AtomicInteger(1);
	}

	private static long gen_uuid(int c) {
		long uuid = 0;
		long time = System.currentTimeMillis();
		int count = _uuidCount[c].incrementAndGet();
		time = time / 60000L;// 只取分钟
		uuid = time * 100000000000L + 999888 * 100000000L + c * 10000000L + count;
		if (count > max_auto_id)
			_uuidCount[c].set(0);
		return uuid;
	}

	/**
	 * 创建订单号
	 * @return
	 */
	public static long genOrderid() {
		return gen_uuid(0);
	}

}
