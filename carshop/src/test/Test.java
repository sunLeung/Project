package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(int i=0;i<10;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put(i+"", i);
			list.add(map);
		}
		
		System.out.println(list.size());
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=list.get(i);
			result.add(map);
			list.remove(map);
		}
		System.out.println(list.size());
		System.out.println(result.size());
	}
}
