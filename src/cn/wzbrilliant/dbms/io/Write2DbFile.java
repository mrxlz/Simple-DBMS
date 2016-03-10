package cn.wzbrilliant.dbms.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import cn.wzbrilliant.dbms.core.TableInfo;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

public class Write2DbFile {

	TableInfo table = null;

	public Write2DbFile() {
		super();
	}

//	/**
//	 * 创建表table的.dbf表文件
//	 * 
//	 * @param table
//	 * @return 
//	 */
//	public OutputStream createDbFile(File file,TableInfo table) {
//		OutputStream fos = null;
//		this.table=table;
//		try {
//			file.createNewFile();
//
//			HashMap<String, String[]> columns = table.getColInfo();
//
//			DBFField[] fields = new DBFField[columns.size()];
//
//			Object[] colName = columns.keySet().toArray();
//			Object[] columnsInfo = columns.values().toArray();
//
//			int length;
//			char dataType = 0;
//
//			for (int i = 0; i < fields.length; i++) {
//				fields[i] = new DBFField();
//				fields[i].setName((String) colName[i]);
//				String[] colInfo = (String[]) columnsInfo[i];
//
//				switch (colInfo[0]) {
//				case "int":
//				case "double":
//					dataType = 'N';
//					break;
//				case "char":
//					dataType = 'C';
//					break;
//				}
//				length = Integer.parseInt(colInfo[1]);
//				fields[i].setDataType((byte) dataType);
//				fields[i].setFieldLength(length);
//				
//				System.out.println((char)dataType+length);
//				
//			}
//
//			fos = new FileOutputStream(file);
//			DBFWriter writer = new DBFWriter(file);
//			writer.setFields(fields);
//			----------------------------------------------------------------
//			fields[0] = new DBFField();
//			fields[0].setName("sno");
//			fields[0].setDataType(DBFField.FIELD_TYPE_C);
//			fields[0].setFieldLength(10);
//			
//			fields[1] = new DBFField();
//			fields[1].setName("sname");
//			fields[1].setDataType(DBFField.FIELD_TYPE_C);
//			fields[1].setFieldLength(10);
//
//			fields[2] = new DBFField();
//			fields[2].setName("subject");
//			fields[2].setDataType(DBFField.FIELD_TYPE_C);
//			fields[2].setFieldLength(15);
//
//			fields[3] = new DBFField();
//			fields[3].setName("classes");
//			fields[3].setDataType(DBFField.FIELD_TYPE_C);
//			fields[3].setFieldLength(15);
//
//			writer.setFields(fields);
			
//			writer.write(fos);
//			----------------------------------------------------------------------------------
			
			
//			System.out.println("创建文件。。");
//			return fos;
//
//		} catch (DBFException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return fos;
//	}

	/**
	 * 将table表中记录写到已有的.dbf表文件中
	 * 
	 * @param table
	 */
	public void write2File(TableInfo table) {

		OutputStream fos = null;
		DBFWriter writer = null;
		Object[] recordValues = null;
		HashMap<String, String[]> columns = table.getColInfo();
		HashMap<Object, Object> recordMap = table.getAllRecords();
		File dbfFile = new File("./" + table.getTableName() + ".dbf");

		Object[] colName = columns.keySet().toArray();
		Object[] columnsInfo = columns.values().toArray();
		DBFField[] dbFields = new DBFField[columns.size()];
		
		try {
			int length;
			char dataType = 0;
			dbfFile.createNewFile();

			for (int i = 0; i < dbFields.length; i++) {
				dbFields[i] = new DBFField();
				dbFields[i].setName((String) colName[i]);
				String[] colInfo = (String[]) columnsInfo[i];

				switch (colInfo[0]) {
				case "int":
				case "double":
					dataType = 'N';
					break;
				case "char":
					dataType = 'C';
					break;
				}
				length = Integer.parseInt(colInfo[1]);
				dbFields[i].setDataType((byte) dataType);
				dbFields[i].setFieldLength(length);
				
			}
			fos = new FileOutputStream(dbfFile);
			writer = new DBFWriter(dbfFile);
			writer.setFields(dbFields);
			
			for (Object record : recordMap.values()) {
				Field[] fields = record.getClass().getDeclaredFields();
				recordValues = new Object[fields.length];
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					recordValues[i] = fields[i].get(record);
				}
				writer.addRecord(recordValues);
			}
			
			writer.write(fos);

		} catch (DBFException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
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
