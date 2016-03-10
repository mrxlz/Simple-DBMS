package cn.wzbrilliant.dbms.unittest;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test() {
		String str="    (adivhb )   adnka\njn,.s";
		
		//去除换行符
		String regex="[\n+]|\\,+";
		System.out.println(str);
		str=str.replaceAll(regex, "");
		System.out.println(str+"\n");
		
		//去除空白符,(
		regex="\\s+|[\\s]*[\\(+][\\s]*|[\\s]*[\\)+][\\s]*|\\,+";
//		System.out.println(str.split(regex).length);
		str="insert into Student (\n\n\nid,name) values(001,\n'Tom');";
		String[] splitStr=str.split(regex);
		for(String s:splitStr){
			System.out.println(s);
		}
		
		System.out.println();
		
		String sql1="abdefg";
		System.out.println(sql1.substring(1, 3));
		System.out.println();
		
		regex="[\n+]|\\,+";
		String sql="insert into Student (\n\n\nid,name) values(001,\n'Tom');";
		
		sql="update Student set name='Tom',age=33 where id='002';";
		sql=sql.replaceAll(regex, " ");
		System.out.println(sql);
		System.out.println();
		String[] splitSql=sql.split("\\s+");
		for (String s:splitSql){
			System.out.println("开始"+s);
		}
		System.out.println();
		sql="delete * from Student \nwhere id='002'";
		splitSql=sql.split(regex);
		for (String s:splitSql){
			System.out.println("开始"+s);
		}
		
		System.out.println();
		sql="count='3'";
		regex="=";
		splitSql=sql.split(regex);
		for (String s:splitSql){
			System.out.println("开始"+s);
		}
		
	
	}

}
