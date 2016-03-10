package cn.wzbrilliant.dbms.unittest;

import java.lang.reflect.Field;

public class ReflectTest {
	public static void main(String[] args) {
//		Object obj=new Student(null, null, null, null, null);
//		Class<Student> cl=(Class<Student>) obj.getClass();
//		System.out.println(cl.getName());
//		String s=obj.getClass().toString();
//		System.out.println("name:"+s);
		Object obj=new Teacher("Tom","male");
		Field[] fields=obj.getClass().getDeclaredFields();
		
		for(Field field:fields){
			try {
				field.setAccessible(true);
				System.out.println(field.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
