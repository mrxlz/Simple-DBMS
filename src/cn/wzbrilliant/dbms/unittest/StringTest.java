package cn.wzbrilliant.dbms.unittest;

import java.util.HashMap;
import java.util.Map.Entry;

public class StringTest {
	public static void main(String[] args) {
		HashMap<String, StringBuffer> map=new HashMap<String, StringBuffer>();
		StringBuffer s = null;
		map.put("aaa", new StringBuffer("llllll"));
//		for(Entry<String, StringBuffer> entry:map.entrySet()){
//			s=entry.getValue();
//		}
//		s.append("sssssss");
//		
//		System.out.println(map.get("aaa"));
		map.put("sgg", new StringBuffer("dgjng"));
		System.out.println(map.size());
		map.clear();
		System.out.println(map.size());
	}
}
