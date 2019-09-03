package com.eolinker.pojo;
/**
 * 数据库表字段
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 * eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 * 如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 * eoLinker AMS开源版的开源协议遵循GPL V3，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 * 官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 * 使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 * 用户讨论QQ群：707530721
 */
public class DatabaseTableField
{

	private Integer fieldID;//字段ID
	private String fieldName;//字段名称
	private String fieldType;//字段类型
	private String fieldLength;//字段长度
	private Integer isNotNull;//必填
	private Integer isPrimaryKey;//是否为主键
	private String fieldDescription;//字段描述
	private Integer tableID;//数据库表ID
	private String defaultValue;//默认值

	public Integer getFieldID()
	{
		return fieldID;
	}

	public void setFieldID(Integer fieldID)
	{
		this.fieldID = fieldID;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getFieldType()
	{
		return fieldType;
	}

	public void setFieldType(String fieldType)
	{
		this.fieldType = fieldType;
	}

	public String getFieldLength()
	{
		return fieldLength;
	}

	public void setFieldLength(String fieldLength)
	{
		this.fieldLength = fieldLength;
	}

	public Integer getIsNotNull()
	{
		return isNotNull;
	}

	public void setIsNotNull(Integer isNotNull)
	{
		this.isNotNull = isNotNull;
	}

	public Integer getIsPrimaryKey()
	{
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(Integer isPrimaryKey)
	{
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getFieldDescription()
	{
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription)
	{
		this.fieldDescription = fieldDescription;
	}

	public Integer getTableID()
	{
		return tableID;
	}

	public void setTableID(Integer tableID)
	{
		this.tableID = tableID;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

}
