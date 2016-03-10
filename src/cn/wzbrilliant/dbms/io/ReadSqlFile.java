package cn.wzbrilliant.dbms.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.wzbrilliant.dbms.core.SqlStatement;
import cn.wzbrilliant.dbms.exception.DeleteException;
import cn.wzbrilliant.dbms.exception.InsertException;
import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.exception.UpdateException;

public class ReadSqlFile {
	public static String[] readSql(File sqlFile) {

		BufferedReader in = null;
		String sql;
		int ch;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile),"gb2312"));
			SqlStatement statement;
			stringBuilder.delete(0, stringBuilder.length());
			while ((ch = in.read()) != -1) {
				if ((char) ch == ';') {
					stringBuilder.append('\n');
					sql = stringBuilder.toString().trim();
					statement = SqlStatement.splitSql(sql);
					statement.checkSyntax();
					statement.executeSql();
					stringBuilder.delete(0, stringBuilder.length());
				}else{
					stringBuilder.append((char)ch);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.toString());
		} catch (UpdateException e) {
			System.err.println(e.toString());
		} catch (DeleteException e) {
			System.err.println(e.toString());
		} catch (InsertException e) {
			System.err.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
