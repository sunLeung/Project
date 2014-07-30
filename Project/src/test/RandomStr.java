package test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomUtils;

public class RandomStr {
	/**
	   * 产生随机字符串
	   * */
	private static Random randGen = ThreadLocalRandom.current();
	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	public static final String randomString(int length) {
	         if (length < 1) {
	             return null;
	         }
	         char [] randBuffer = new char[length];
	         for (int i=0; i<randBuffer.length; i++) {
	             randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
	          //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
	         }
	         return new String(randBuffer);
	}
	
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			System.out.println(RandomUtils.nextInt(0, 3));
		}
	}
}
