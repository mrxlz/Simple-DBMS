package cn.wzbrilliant.dbms.util;

import java.util.ArrayList;

import cn.wzbrilliant.dbms.core.TableInfo;
import cn.wzbrilliant.dbms.exception.SyntaxException;

public class TableUtils {
	
	static{
		tables = new ArrayList<TableInfo>();
	}
	
	private static ArrayList<TableInfo> tables;

	public TableUtils() {

	}

	public static void addTable(TableInfo table) {
		tables.add(table);
	}

	public static TableInfo getTable(String tableName) throws SyntaxException {
		for (TableInfo table : tables) {
			if (tableName.equalsIgnoreCase(table.getTableName())) {
				return table;
			}
		}
		throw new SyntaxException("不存在该表");
	}
	
	
	
}
