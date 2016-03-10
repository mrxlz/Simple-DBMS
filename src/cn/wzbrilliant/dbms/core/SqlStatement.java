package cn.wzbrilliant.dbms.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.wzbrilliant.dbms.exception.DeleteException;
import cn.wzbrilliant.dbms.exception.InsertException;
import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.exception.UpdateException;
import cn.wzbrilliant.dbms.util.TableUtils;

/**
 * sql语句抽象类
 * 
 * @author ice
 *
 */
public abstract class SqlStatement {

	protected String sql;
	protected String tableName = null;
	protected String[] splitedSql = null;

	public SqlStatement(String sql) {
		super();
		this.sql = sql;
	}

	public SqlStatement(String sql, String[] splitedSql) {
		super();
		this.sql = sql;
		this.splitedSql = splitedSql;
	}

	public String getSql() {
		return sql;
	}

	/**
	 * 检查sql语法
	 * 
	 * @return
	 * @throws SyntaxException 
	 */
	public abstract boolean checkSyntax() throws SyntaxException;

	/**
	 * 执行sql语句
	 * @throws UpdateException 
	 * @throws SyntaxException 
	 * @throws DeleteException 
	 * @throws InsertException 
	 */
	public abstract void executeSql() throws UpdateException, SyntaxException, DeleteException, InsertException;

	/**
	 * 切割sql语句
	 * 
	 * @return 相应sql语句的对象
	 * @throws SyntaxException 不属于insert、update、delete语句
	 *             
	 */
	public static SqlStatement splitSql(String sql) throws SyntaxException {
		String regex;
		SqlStatement statement;
		sql = sql.toLowerCase();
		// 所有空白符以及逗号，括号
		regex = "\\s+|[\\s]*[\\(+][\\s]*|[\\s]*[\\)+][\\s]*|,+";
		sql = sql.replaceAll(regex, " ");
		// 按空格分割语句
		regex = "\\s+";
		String[] splitedSql = sql.split(regex);

		switch (splitedSql[0].toLowerCase()) {
		case "insert":
			statement = new Insert(sql, splitedSql);
			break;
		case "update":
			statement = new Update(sql, splitedSql);
			break;
		case "delete":
			statement = new Delete(sql, splitedSql);
			break;
		default:
			throw new SyntaxException("sql语法错误");
		}
		return statement;
	}

	/**
	 * 查找符合where条件的记录
	 * 
	 * @param whereCondition where条件
	 * 
	 * @return 符合条件的对象的集合
	 * @throws SyntaxException 
	 */
	public List<Object> findRecord(String[] whereCondition) throws SyntaxException {
		/*
		 * 查找or的索引，查询每一个or条件的结果集，求并集
		 * 每两个or之间and关键字，加至list列表里，判断位置正误(前后是否与and或or关键字相邻)，若正确，则添加前后字符串为筛选条件
		 */
		List<Object> record=new ArrayList<Object>();
		List<Integer> orIndex = new ArrayList<Integer>();
		List<Integer> andIndex = new ArrayList<Integer>();
		HashMap<String, String> conditions= new HashMap<String, String>();
		orIndex.add(-1);
		for (int i = 0; i < whereCondition.length; i++) {
			if ("or".equalsIgnoreCase(whereCondition[i])) {
				if (i == whereCondition.length - 1)
					throw new SyntaxException("where条件语法错误");
				orIndex.add(i);
			}else if("and".equalsIgnoreCase(whereCondition[i])){
				if (i == whereCondition.length - 1)
					throw new SyntaxException("where条件语法错误");
				andIndex.add(i);
			}
		}
		orIndex.add(whereCondition.length);
		andIndex.add(whereCondition.length);
		String[] splitedCondition = null;
		for (int i = 0; i < orIndex.size()-1; i++) {
			for (int j = orIndex.get(i) + 1; j < orIndex.get(i + 1); j++) {
				if ("and".equalsIgnoreCase(whereCondition[j])) {
					if ((andIndex.contains(j - 1)
							|| andIndex.contains(j + 1))
							|| orIndex.contains(j - 1)
							|| orIndex.contains(j + 1)) {
						throw new SyntaxException("where条件语法错误");
					}
				} else {
					if (!whereCondition[j].contains("=")
							|| (!(andIndex.contains(j - 1) 
									|| orIndex.contains(j - 1)) 
									&& !(andIndex.contains(j + 1)
											|| orIndex.contains(j + 1)))) {
						throw new SyntaxException("where条件语法错误");
					}
					splitedCondition=whereCondition[j].split("=");
					if(splitedCondition.length!=2 || 
							!(splitedCondition[1].startsWith("'") && splitedCondition[1].endsWith("'"))){
						throw new SyntaxException("where条件语法错误");
					}
					splitedCondition[1]=splitedCondition[1].substring(1, splitedCondition[1].length()-1);
					conditions.put(splitedCondition[0], splitedCondition[1]);
				}
			}
			
			
			TableInfo table=TableUtils.getTable(tableName);
			HashMap<Object, Object> allRecords=table.getAllRecords();
			
			//遍历所有记录
			for(Entry<Object, Object> entry:allRecords.entrySet()){
				Object recordEntity=entry.getValue();
				Field[] fields=recordEntity.getClass().getDeclaredFields();
				boolean isMatch=true;
				//遍历所有条件
				for(Entry<String,String> conEntry:conditions.entrySet()){
					
//					System.out.println(conEntry.getKey()+":"+conEntry.getValue());
					
					boolean hasField=false;
					String fieldName=(String) conEntry.getKey();
					//遍历对象字段值
					for(Field field:fields){
						field.setAccessible(true);
						if(field.getName().equalsIgnoreCase(fieldName)){
							hasField=true;
							try {
								if(!field.get(recordEntity).equals(conEntry.getValue())){
									isMatch=false;
								}
							} catch (IllegalArgumentException
									| IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
					if(!hasField){
						isMatch=false;
						throw new SyntaxException("where条件语法错误 ,表中不存在某字段");
					}
				}
				if(isMatch){
					record.add(recordEntity);
				}
			}
			conditions.clear();
		}
		return record;
	}
}
