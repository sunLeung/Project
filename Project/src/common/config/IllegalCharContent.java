package common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.logger.Logger;
import common.logger.LoggerManger;

public class IllegalCharContent {
	private static Logger log=LoggerManger.getLogger();
	
	public static List<String> illegalCharContent=new ArrayList<String>();
	
	public static void init(){
		log.info("Star init illegalCharset data.");
		String filePath=Config.CONFIG_DIR + File.separator + "illegal-character.xx";
		FileInputStream fis=null;
		BufferedReader br=null;
		try {
			fis=new FileInputStream(filePath);
			br=new BufferedReader(new InputStreamReader(fis,"utf-8"));
			String temp="";
			while((temp=br.readLine())!=null){
				temp=temp.trim();
				if(!temp.startsWith("#")){
					illegalCharContent.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null)br.close();
				if(fis!=null)fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		log.info("Init illegalCharset data complete.");
	}
	
	public static boolean hasIllegalCharMatch(String word){
		for(String str:illegalCharContent){
			if(str.equals(word)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasIllegalCharLike(String word){
		for(String str:illegalCharContent){
			if(word.contains(str)){
				return true;
			}
		}
		return false;
	}
}
