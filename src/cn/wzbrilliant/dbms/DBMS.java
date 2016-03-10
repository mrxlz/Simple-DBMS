package cn.wzbrilliant.dbms;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.wzbrilliant.dbms.bean.Student;
import cn.wzbrilliant.dbms.core.TableInfo;
import cn.wzbrilliant.dbms.io.ReadCmd;
import cn.wzbrilliant.dbms.io.ReadDbFile;
import cn.wzbrilliant.dbms.io.ReadSqlFile;
import cn.wzbrilliant.dbms.io.Write2DbFile;
import cn.wzbrilliant.dbms.util.TableUtils;

public class DBMS {
	
	public static void main(String[] args) {

		
		File dbfFile=new File("./student.dbf");
		TableInfo table;
		
		if(dbfFile.exists()){
			table=new ReadDbFile(dbfFile).readFile();
		}else{
			table=new TableInfo("Student", Student.getColumnInfo(), null);
		}
		table.setEntityClass(Student.class);
		TableUtils.addTable(table);
		
		String option;
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			System.out.println("+-----------------------------------------------------------------------+");
			System.out.println("|\t\t\t\t\t\t\t\t\t|");
			System.out.println("|\t\t请选择所要执行的命令\t\t\t\t\t|");
			System.out.println("|\t\t\t\t\t\t\t\t\t|");
			System.out.println("|\t\t1.sql语句：insert、update或delete语句(通过命令方式)\t|");
			System.out.println("|\t\t2.sql语句：insert、update或delete语句(通过程序方式)\t|");
			System.out.println("|\t\t3.查看表中所有记录\t\t\t\t\t|");
			System.out.println("|\t\t4.查看表结构信息\t\t\t\t\t|");
			System.out.println("|\t\t5.退出\t\t\t\t\t\t\t|");
			System.out.println("|\t\t\t\t\t\t\t\t\t|");
			System.out.println("+-----------------------------------------------------------------------+");
			System.out.println("请输入选项：");
			try {
				option = in.readLine();
				switch(option){
				case "1":
					ReadCmd.readSql(table);
					break;
				case "2":
					File sqlFile=new File("./db.sql");
					if(sqlFile.exists())
						ReadSqlFile.readSql(sqlFile);
					else
						System.out.println("sql语句文件不存在，请新建db.sql文件");
					break;
				case "3":
					table.showRecords();
					break;
				case "4":
					table.showStructure();
					break;
				case "5":
					Write2DbFile writer=new Write2DbFile();
					writer.write2File(table);
					System.out.println("程序结束...");
					return ;
				default:
					System.out.println("输入选项错误，请重新输入");
					break;
				}
				System.out.println("按回车键继续...");
				option = in.readLine();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
