package com.eolinker.vo;

public class DatabaseTableFieldVO {

	private String fieldName;
	private int fieldType;
	private int fieldLength;
	private int isNotNull;
	private int isPrimaryKey;
	private String fieldDesc;
	private String defaultValue;
	
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	public int getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	public int getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(int isNotNull) {
		this.isNotNull = isNotNull;
	}
	public int getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(int isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getFieldDesc() {
		return fieldDesc;
	}
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
