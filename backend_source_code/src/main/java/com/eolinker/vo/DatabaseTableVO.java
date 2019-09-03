package com.eolinker.vo;

public class DatabaseTableVO {

	private int tableID;
	private String tableName;
	private String tableDesc;
	private DatabaseTableFieldVO[] fieldList;
	
	public int getTableID() {
		return tableID;
	}
	public void setTableID(int tableID) {
		this.tableID = tableID;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public DatabaseTableFieldVO[] getFieldList() {
		return fieldList;
	}
	public void setFieldList(DatabaseTableFieldVO[] fieldList) {
		this.fieldList = fieldList;
	}
	
	
	
}
