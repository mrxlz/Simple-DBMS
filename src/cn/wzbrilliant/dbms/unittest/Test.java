package cn.wzbrilliant.dbms.unittest;

import java.io.IOException;

import cn.wzbrilliant.dbms.exception.InsertException;

public class Test {

	@org.junit.Test
	public void test() throws InsertException {
		System.out.println("hehehhe");
		try {
			Runtime.getRuntime().exec("cmd /c start javac "); 
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
