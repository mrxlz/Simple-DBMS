package cn.wzbrilliant.dbms.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import com.linuxense.javadbf.DBFField;

public class TableInfo {

	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 列信息 key-value 列名-列信息(类型，长度)
	 */
	private HashMap<String, String[]> colInfo;
	/**
	 * 表中所有记录集合 key-value 主键-记录
	 */
	private HashMap<Object, Object> record;

	private Class<?> entityClass = null;

	public TableInfo() {
		super();
	}

	public TableInfo(String tableName, HashMap<String, String[]> colInfo,
			HashMap<Object, Object> record) {
		super();
		this.tableName = tableName;
		this.colInfo = colInfo;
		this.record = record;
		if (colInfo == null)
			this.colInfo = new HashMap<String, String[]>();
		if (record == null)
			this.record = new HashMap<Object, Object>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public HashMap<String, String[]> getColInfo() {
		return colInfo;
	}

	public void setColInfo(HashMap<String, String[]> colInfo) {
		this.colInfo = colInfo;
	}

	public void addColInfo(DBFField field) {
		String dataType = null;
		String fieldLength;

		fieldLength = String.valueOf(field.getFieldLength());

		switch (String.valueOf((char) field.getDataType())) {
		case "C":
			dataType = "char";
			break;
		case "N":
			dataType = "int";
			break;
		}

		colInfo.put(field.getName(), new String[] { dataType, fieldLength });
	}

	public HashMap<Object, Object> getAllRecords() {
		return record;
	}

	public void setRecord(HashMap<Object, Object> record) {
		this.record = record;
	}

	public void addRecord(String key, Object realRecord) {
		this.record.put(key, realRecord);
	}

	public void removeRecord(Object record) {
		try {
			Method method = record.getClass().getDeclaredMethod("getKey");
			Object key = method.invoke(record);
			this.record.remove(key);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取记录中是记录对象类型
	 * 
	 * @return
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public int getRecordSize() {
		return record.size();
	}

	public void showRecords() {

		System.out.println("+---------------------------------------------------------------+");
		System.out.println("|\t\t\t" + tableName + "\t\t\t\t\t|");
		System.out.println("+---------------------------------------------------------------+");
		for (Entry entry : colInfo.entrySet()) {
			String[] col = (String[]) entry.getValue();
			System.out.print("|"+"\t"+entry.getKey()+"\t" );
		}
		System.out.println("|");
		try {
			for (Entry entry : record.entrySet()) {
				System.out.println("+---------------------------------------------------------------+");
				Object aRecord = entry.getValue();
				Method show=aRecord.getClass().getMethod("showRecord");
				show.invoke(aRecord);
			}
			System.out.println("+---------------------------------------------------------------+");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	
	public void showStructure(){
		System.out.println("+---------------------------------------+");
		System.out.println("|\tField\t|\tType\t\t|");
		for (Entry entry : colInfo.entrySet()) {
			String[] col = (String[]) entry.getValue();
			System.out.println("+---------------------------------------+");
			System.out.println("|\t"+entry.getKey()+"\t|\t"+col[0]+"("+col[1]+")\t|");
		}
		System.out.println("+---------------------------------------+");
	}

}
