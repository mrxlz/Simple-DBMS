package cn.wzbrilliant.dbms.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.wzbrilliant.dbms.bean.Student;
import cn.wzbrilliant.dbms.core.TableInfo;
import cn.wzbrilliant.dbms.exception.TableFieldException;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

public class ReadDbFile {
	private TableInfo table;
	private File dbFile;

	public ReadDbFile(File dbFile) {
		this.dbFile = dbFile;
	}

	/**
	 * 将表结构、表中记录读入table中
	 * @return 读取的表
	 */
	public TableInfo readFile() {
		InputStream in = null;

		String tableName=dbFile.getName().substring(0, dbFile.getName().length()-4);
		table = new TableInfo(tableName, null, null);

		try {
			in = new FileInputStream(dbFile);
			DBFReader reader = new DBFReader(in);

			// 以下代码仅用于读取Student.dbf文件
			// --------------------------------------------------------------
			if (4 != reader.getFieldCount()) {
				throw new TableFieldException("表结构异常");
			}

			// 插入列信息
			for (int index = 0; index < reader.getFieldCount(); index++) {
				table.addColInfo(reader.getField(index));
			}
			
			Object[] record;
			// 插入记录
			while((record = reader.nextRecord())!=null) {
				Student stu = new Student(((String) record[0]).trim(),
						((String) record[1]).trim(),
						((String) record[2]).trim(), ((String) record[3]).trim());

				String key=((String)record[0]).trim();
				table.addRecord(key, stu);
			}
			// --------------------------------------------------------------
		} catch (FileNotFoundException e) {
			System.err.println("无法找到数据库表文件");
		} catch (DBFException e) {
			System.err.println("DBFReader读取文件出错");
		} catch (TableFieldException e) {
			System.err.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return table;
	}

	public TableInfo getTable() {
		return table;
	}

}
