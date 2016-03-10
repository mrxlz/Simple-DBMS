package cn.wzbrilliant.dbms.unittest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

public class Write2FileTest {

	@Test
	public void test() {

		File file = new File("./student.dbf");

		OutputStream fos = null;

		try {
			if (!file.exists())
				file.createNewFile();

			fos = new FileOutputStream(file);
			DBFWriter writer = new DBFWriter(file);
			DBFField[] fields = new DBFField[4];

			fields[0] = new DBFField();
			fields[0].setName("sno");
			fields[0].setDataType(DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(10);
			
			fields[1] = new DBFField();
			fields[1].setName("sname");
			fields[1].setDataType(DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(10);

//			fields[2] = new DBFField();
//			fields[2].setName("age");
//			fields[2].setDataType(DBFField.FIELD_TYPE_N);
//			fields[2].setFieldLength(3);

			fields[2] = new DBFField();
			fields[2].setName("subject");
			fields[2].setDataType(DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(15);

			fields[3] = new DBFField();
			fields[3].setName("classes");
			fields[3].setDataType(DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(15);

			writer.setFields(fields);
			Object[] rowData=new Object[4];
			rowData[0]="001";
			rowData[1]="Tom";
			rowData[2]="CS";
			rowData[3]="13-2";
			
			writer.addRecord(rowData);
			
			writer.write(fos);

		} catch (DBFException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
