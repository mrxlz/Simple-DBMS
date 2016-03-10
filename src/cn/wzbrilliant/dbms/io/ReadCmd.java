package cn.wzbrilliant.dbms.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.wzbrilliant.dbms.core.SqlStatement;
import cn.wzbrilliant.dbms.core.TableInfo;
import cn.wzbrilliant.dbms.exception.DeleteException;
import cn.wzbrilliant.dbms.exception.InsertException;
import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.exception.UpdateException;

public class ReadCmd {

	public static void readSql(TableInfo table) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sql, line;
		StringBuilder stringBuilder = new StringBuilder();

		System.out.println("请输入sql语句：");
		try {
			stringBuilder.delete(0, stringBuilder.length());
			while ((line = in.readLine()) != null) {
				if (line.endsWith(";")) {
					stringBuilder.append(line.substring(0, line.length() - 1));
					break;
				}
				stringBuilder.append(line + '\n');
			}
			sql = stringBuilder.toString();

			SqlStatement statement = SqlStatement.splitSql(sql);

			statement.checkSyntax();

			statement.executeSql();

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
		}

	}

}
