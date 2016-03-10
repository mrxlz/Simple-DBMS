package cn.wzbrilliant.dbms.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.wzbrilliant.dbms.exception.DeleteException;
import cn.wzbrilliant.dbms.exception.SyntaxException;
import cn.wzbrilliant.dbms.util.TableUtils;

/**
 * 执行删除语句相关操作
 * 
 * @author ice
 *
 */
public class Delete extends SqlStatement {

	private boolean haveWhere = false;
	private String[] whereCondition = null;

	public Delete(String sql) {
		super(sql);
	}

	public Delete(String sql, String[] splitedSql) {
		super(sql, splitedSql);
	}

	@Override
	public boolean checkSyntax() throws SyntaxException {
		tableName=splitedSql[2];
		int whereIndex = -1, whereConditionNum = -1;

		for (int i = 0; i < splitedSql.length; i++) {
			if ("where".equalsIgnoreCase(splitedSql[i])) {
				whereIndex = i;
				break;
			}
		}

		if (whereIndex > 0) {
			if (whereIndex == splitedSql.length - 1
					|| !"from".equalsIgnoreCase(splitedSql[1])) {
				throw new SyntaxException("delete 语句语法错误");
			}
			haveWhere = true;
		}

		if (haveWhere) {
			whereConditionNum = splitedSql.length - whereIndex - 1;
			whereCondition = new String[whereConditionNum];
			for (int i = whereIndex + 1, j = 0; i < splitedSql.length; i++, j++) {
				whereCondition[j]=splitedSql[i];
			}
		}

		return true;
	}

	@Override
	public void executeSql() throws DeleteException, SyntaxException {

		TableInfo table=TableUtils.getTable(tableName);
		
		List<Object> records;
		HashMap<Object, Object> allRecord;
		
		if(whereCondition!=null)
			records=this.findRecord(whereCondition);
		else{
			allRecord=table.getAllRecords();
			records=new ArrayList<Object>();
			for(Entry entry:allRecord.entrySet()){
				records.add(entry.getValue());
			}
			
		}
		for(Object record:records){
			table.removeRecord(record);
		}
		System.out.println("删除成功！");
	}

}
