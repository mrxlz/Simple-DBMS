package cn.wzbrilliant.dbms.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.exception.UpdateException;
import cn.wzbrilliant.dbms.util.TableUtils;

/**
 * 执行更新语句相关操作
 * 
 * @author ice
 *
 */
public class Update extends SqlStatement {

	private boolean haveWhere = false;
	private String[] whereCondition = null;
	private String[] updateCol; // must have
	private String[] updateValue; // must have

	public Update(String sql) {
		super(sql);
	}

	public Update(String sql, String[] splitedSql) {
		super(sql, splitedSql);
	}

	@Override
	public boolean checkSyntax() throws SyntaxException {
		tableName = splitedSql[1];
		int updateColNum, whereConditionNum = -1;
		int setIndex = 2, whereIndex = -1;

		for (int i = 0; i < splitedSql.length; i++) {
			if ("where".equalsIgnoreCase(splitedSql[i])) {
				whereIndex = i;
				break;
			}
		}

		if (whereIndex > 0) {
			if (whereIndex == splitedSql.length - 1
					|| (whereIndex - setIndex) <= 1
					|| !"set".equalsIgnoreCase(splitedSql[setIndex])) {
				throw new SyntaxException("update 语句语法错误");
			}
			haveWhere = true;
		}

		if (haveWhere) {
			updateColNum = whereIndex - setIndex - 1;
			whereConditionNum = splitedSql.length - whereIndex - 1;
		} else {
			updateColNum = splitedSql.length - setIndex - 1;
		}

		updateCol = new String[updateColNum];
		updateValue = new String[updateColNum];
		for (int i = setIndex + 1, j = 0; i < setIndex + updateColNum + 1; i++, j++) {
			if (!splitedSql[i].contains("=")) {
				throw new SyntaxException("update 语句语法错误");
			}
			String[] updateContent = splitedSql[i].split("=");
			if (updateContent.length != 2) {
				throw new SyntaxException("update 语句语法错误");
			}
			updateCol[j] = updateContent[0];
			updateValue[j] = updateContent[1];
		}

		if (haveWhere) {
			whereCondition = new String[whereConditionNum];
			for (int i = whereIndex + 1, j = 0; i < splitedSql.length; i++, j++) {
				whereCondition[j] = splitedSql[i];
			}
		}

		return true;
	}

	@Override
	public void executeSql() throws UpdateException, SyntaxException {
		TableInfo table = TableUtils.getTable(tableName);
		
		
		List<Object> records;
		HashMap<Object, Object> allRecord;
		HashMap<Object, Object> allRecords = table.getAllRecords();
		if(whereCondition!=null){
			records = this.findRecord(whereCondition);
		}
		else{
			allRecord=table.getAllRecords();
			records=new ArrayList<Object>();
			for(Entry entry:allRecord.entrySet()){
				records.add(entry.getValue());
			}
		}
		boolean haveField=false;
		for (Object record : records) {
			Field[] fields = record.getClass().getDeclaredFields();
			for (int i = 0; i < updateCol.length; i++) {
				haveField=false;
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.getName().equalsIgnoreCase(updateCol[i])) {
						try {
							haveField=true;
//							System.out.println(updateValue[i]);
							if (!(updateValue[i].startsWith("'") && updateValue[i].endsWith("'"))) {
								throw new UpdateException("update 语句语法错误");
							}
							Method method = record.getClass().getMethod("getKey");
							String key = (String) method.invoke(record);
							method=record.getClass().getMethod("getKeyName");
							String keyName=(String)method.invoke(record);
							if (field.getName().equalsIgnoreCase(keyName)  && (updateValue[i].length()==0 || "null".equalsIgnoreCase(updateValue[i])))
								throw new UpdateException("update 语句错误,主键不能为null");
							if (field.getName().equalsIgnoreCase(keyName) && allRecords.containsKey(updateValue[i]))
								throw new UpdateException("update 语句错误,主键不能重复");
							
							field.set(record, updateValue[i].substring(1, updateValue[i].length()-1));
							if(field.getName().equalsIgnoreCase(keyName)){
								for(Entry entry:allRecords.entrySet()){
									Object entryKey=entry.getKey();
									if(entryKey.equals(key)){
										entryKey=updateValue[i];
										break;
									}
								}
							}
						} catch (IllegalArgumentException
								| IllegalAccessException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				if(!haveField){
					throw new UpdateException("不含有要更新的某字段");
				}
			}
		}
		System.out.println("更新成功");
	}

}
