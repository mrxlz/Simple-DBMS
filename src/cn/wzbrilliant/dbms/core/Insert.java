package cn.wzbrilliant.dbms.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import cn.wzbrilliant.dbms.exception.InsertException;
import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.util.TableUtils;

/**
 * 执行插入语句相关操作
 * 
 * @author ice
 *
 */
public class Insert extends SqlStatement {

	private String columns[];
	private String values[];

	public Insert(String sql) {
		super(sql);
	}

	public Insert(String sql, String[] splitedSql) {
		super(sql, splitedSql);
	}

	@Override
	public boolean checkSyntax() throws SyntaxException {
		tableName = splitedSql[2];
		int tableIndex = 2, valueIndex = -1;
		int fieldNum = -1, valueNum = -1;

		for (int i = 0; i < splitedSql.length; i++) {
			if ("values".equalsIgnoreCase(splitedSql[i])) {
				valueIndex = i;
				break;
			}
		}
		fieldNum = valueIndex - tableIndex - 1;
		valueNum = splitedSql.length - valueIndex - 1;
		if (!"into".equalsIgnoreCase(splitedSql[1]) || tableName == null
				|| valueIndex == -1
				|| ((fieldNum != 0) && (fieldNum != valueNum)) || fieldNum < 0
				|| valueNum <= 0) {
			throw new SyntaxException("insert 语句语法错误");
		}

		if (fieldNum > 0) {
			columns = new String[fieldNum];
			for (int i = tableIndex + 1,j=0; i < valueIndex; i++,j++) {
				columns[j] = splitedSql[i];
			}
		} else {
			columns = null;
		}

		values = new String[valueNum];
		for (int j = valueIndex + 1,i=0; j < splitedSql.length; j++,i++) {
			values[i] = splitedSql[j];
		}
		return true;
	}

	@Override
	public void executeSql() throws InsertException, SyntaxException {
		/*
		 * if columns==null 
		 * 		判断value个数 if valueNum == 记录的属性数 
		 * 						cloumns<<-反射获取插入实体对象的列名
		 * 					else Exeption
		 * 按列将值赋给实体对象
		 * if primary key != null
		 * 		插入记录
		 */

		TableInfo table = TableUtils.getTable(tableName);
		HashMap<Object, Object> allRecords = table.getAllRecords();
		Class<?> recordEntity = table.getEntityClass();
		Field[] fields = recordEntity.getDeclaredFields();
		
		if (columns == null) {
			if (values.length != fields.length)
				throw new InsertException("insert 语句语法错误，插入值与表字段不符合");
			
			Set<String> keySet=table.getColInfo().keySet();
			String[] colNames=new String[keySet.size()];
			int count=0;
			for(String s:keySet){
				colNames[count++]=s;
			}
			columns = new String[colNames.length];
			for (int i = 0; i < colNames.length; i++) {
				columns[i] = colNames[i];
			}
		}

		boolean haveCol;
		for (String colName : columns) {
			haveCol = false;
			for (Field field : fields) {
				if (field.getName().equalsIgnoreCase(colName))
					haveCol = true;
			}
			if (!haveCol) {
				throw new InsertException("insert 语句语法错误，不存在" + colName + "字段");
			}
		}

		//反射创建实体对象，并插入表中
		try {
			Object record = recordEntity.newInstance();

			for (int i = 0; i < columns.length; i++) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(columns[i])) {
						field.setAccessible(true);
						if(field.getType()==String.class){
							if(!(values[i].startsWith("'") && values[i].endsWith("'")))
								throw new InsertException("insert 语句语法错误:"+values[i]);
							values[i]=values[i].substring(1, values[i].length()-1);
						}
						if(field.getType()==Integer.class)
							field.set(record, Integer.parseInt(values[i]));
						else
							field.set(record, values[i]);
					}
				}
			}

			Method method = recordEntity.getMethod("getKey");
			Object key = method.invoke(record);
			if (key == null)
				throw new InsertException("insert 语句错误,主键不能为null");
			if(allRecords.containsKey(key))
				throw new InsertException("insert 语句错误,主键不能重复");
			allRecords.put(key, record);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		System.out.println("\n插入成功！");
	}

}
