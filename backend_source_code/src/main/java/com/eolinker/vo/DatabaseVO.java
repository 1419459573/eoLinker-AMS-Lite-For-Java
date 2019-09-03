package com.eolinker.vo;

public class DatabaseVO {

	private DatabaseInfoVO databaseInfo;
	private DatabaseTableVO[] tableList;
	
	public DatabaseInfoVO getDatabaseInfo() {
		return databaseInfo;
	}
	public void setDatabaseInfo(DatabaseInfoVO databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
	public DatabaseTableVO[] getTableList() {
		return tableList;
	}
	public void setTableList(DatabaseTableVO[] tableList) {
		this.tableList = tableList;
	}
	
	
}
