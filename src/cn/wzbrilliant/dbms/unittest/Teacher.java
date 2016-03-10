package cn.wzbrilliant.dbms.unittest;

public class Teacher {
	private String name;
	private String sex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Teacher(String name, String sex) {
		super();
		this.name = name;
		this.sex = sex;
	}
}
