package cn.wzbrilliant.dbms.bean;

import java.util.HashMap;

/**
 * Student类
 * @author ice
 *
 */
public class Student {
	
//	private Integer age;
	private String sno;
	private String sname;
	private String subject;
	private String classes;
	
	public Student(){
		sno = null;
		sname = null;
//		age = null;
		subject = null;
		classes = null;
	}

	public Student(String sno, String sname, String subject,
			String classes) {
		super();
		this.sno = sno;
		this.sname = sname;
//		this.age = age;
		this.subject = subject;
		this.classes = classes;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

//	public Integer getAge() {
//		return age;
//	}
//
//	public void setAge(Integer age) {
//		this.age = age;
//	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}
	
	public String getKey(){
		return this.sno;
	}
	
	public String getKeyName(){
		return new String("sno");
	}
	
	public static HashMap<String, String[]> getColumnInfo(){
		HashMap<String, String[]> colInfo=new HashMap<String, String[]>();
		colInfo.put("sno", new String[]{"char","15"});
		colInfo.put("sname", new String[]{"char","15"});
//		colInfo.put("age", new String[]{"int","15"});
		colInfo.put("subject", new String[]{"char","15"});
		colInfo.put("classes", new String[]{"char","15"});
		return colInfo;
	}
	
	public void showRecord(){
		System.out.println("|   "+sno+"\t|"+"\t"+sname+"\t"+"|"+"\t"+subject+"\t"+"|"+"\t"+classes+"\t"+"|");
	}
	
}
