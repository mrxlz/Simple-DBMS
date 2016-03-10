package cn.wzbrilliant.dbms.unittest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

public class ReadFileTest {
	@Test
	public void test() {

		File file = new File("./Student.dbf");

		InputStream in = null;

		try {
//			if (!file.exists()) {
//				file.createNewFile();
//			}
			in = new FileInputStream(file);
			DBFReader reader = new DBFReader(in);
			System.out.println(reader.getFieldCount());
			for (int i = 0; i < reader.getFieldCount(); i++){
				System.out.println("name:"+reader.getField(i).getName()
										+" length:"+reader.getField(i).getFieldLength());
			}
			System.out.println("RecordCount:"+reader.getRecordCount());
			Object[] record;
			while((record = reader.nextRecord())!=null){
				for(Object obj:record){
					System.out.println(obj.toString());
				}
			}
			
//			System.out.println(String.valueOf((char)reader.getField(0).getDataType()).equals("C"));
			
//			System.out.println('C');
//			switch(String.valueOf((char)reader.getField(0).getDataType())){
//			case "C":
//				System.out.println("in case C....");
//				break;
//			case "N":
//				System.out.println("in case N....");
//				break;
//			}

		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		} catch (DBFException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
