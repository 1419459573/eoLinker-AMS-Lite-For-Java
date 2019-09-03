package com.eolinker.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.mapper.ConnDatabaseMapper;
import com.eolinker.mapper.DatabaseMapper;
import com.eolinker.mapper.DatabaseTableFieldMapper;
import com.eolinker.mapper.DatabaseTableMapper;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.Database;
import com.eolinker.pojo.DatabaseTable;
import com.eolinker.pojo.DatabaseTableField;
import com.eolinker.service.DatabaseService;
import com.eolinker.util.RegexMatch;
/**
 * 数据库成员[业务处理层]
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
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DatabaseServiceImpl implements DatabaseService
{

	@Autowired
	private DatabaseMapper databaseMapper;

	@Autowired
	private DatabaseTableMapper databaseTableMapper;

	@Autowired
	private DatabaseTableFieldMapper databaseTableFieldMapper;

	@Autowired
	private ConnDatabaseMapper connDatabaseMapper;

	/**
	 * 获取成员权限类型
	 */
	@Override
	public ConnDatabase getUserType(int dbID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		ConnDatabase connDatabase = new ConnDatabase();
		connDatabase.setDbID(dbID);
		connDatabase.setUserID(userID);

		ConnDatabase currentConnDatabase = connDatabaseMapper.getDatabaseUserType(connDatabase);

		return currentConnDatabase;
	}

	/**
	 * 添加数据库
	 */
	@Override
	public Integer addDatabase(String dbName, double dbVersion)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Database database = new Database();
		database.setDbName(dbName);
		database.setDbVersion(dbVersion);

		Integer affectedRow = databaseMapper.addDatabase(database);
		if (affectedRow != null)
		{
			affectedRow = this.connDatabaseMapper.addDatabaseConnection(database.getDbID(), userID);
		}

		return (affectedRow != 0) ? database.getDbID() : null;

	}

	/**
	 * 删除数据库
	 */
	@Override
	public Integer deleteDatabase(int dbID)
	{
		Integer affectedRow = this.databaseMapper.deleteDatabase(dbID);
		return (affectedRow != null) ? affectedRow : null;
	}

	/**
	 * 获取数据库
	 */
	@Override
	public List<Map<String, Object>> getDatabaseList()
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		List<Map<String, Object>> databases = this.databaseMapper.getDatabaseList(userID);
		if (databases != null && !databases.isEmpty())
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Map<String, Object> database : databases)
			{
				 String updateTime = dateFormat.format(database.get("dbUpdateTime"));
				 database.put("dbUpdateTime", updateTime);
			}
		}
		return databases;
	}

	/**
	 * 修改数据库
	 */
	@Override
	public Integer editDatabase(int dbID, String dbName, Double dbVersion)
	{
		Database database = new Database();
		database.setDbID(dbID);
		database.setDbName(dbName);
		database.setDbVersion(dbVersion);
		int affectedRow = this.databaseMapper.editDatabase(database);
		return (affectedRow != 0) ? affectedRow : null;
	}

	/**
	 * 导入数据库
	 */
	@Override
	public int importDatabase(String dbName, List<Map<String, Object>> tableList)
	{
		Database database = new Database();
		database.setDbName(dbName);
		database.setDbVersion(1.0);
		database.setDbType(0);
		if(databaseMapper.addDatabase(database) < 1)
			throw new RuntimeException("addDatabase error");
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		if(connDatabaseMapper.addDatabaseConnection(database.getDbID(), userID) < 1)
			throw new RuntimeException("addDatabase error");
		
		for(Map<String, Object> table : tableList)
		{
			
			String tableName = (String) table.get("tableName");
			tableName = tableName.replaceAll("`", "");
			DatabaseTable newTable = new DatabaseTable();
			newTable.setDbID(database.getDbID());
			newTable.setTableName(tableName);
			int affectedRow = this.databaseTableMapper.addTable(newTable);
			if (affectedRow < 1)
				throw new RuntimeException("addTable error");

			int tableID = newTable.getTableID();
			// 将各字段信息分割成一行一个
			RegexMatch tableFields = new RegexMatch("\\s{2,5}`(.+?)`\\s(.+?)\\s{0,1}(.+?),",
					table.get("tableField").toString());
			for (String field : tableFields.getList())
			{
				// 提取字段名
				RegexMatch fieldNameMatch = new RegexMatch("`(.*?)`", field);
				String fieldName = fieldNameMatch.getList().get(0).replaceAll("`", "");

				// 提取字段长度
				RegexMatch fieldLengthMatch = new RegexMatch("[0-9]{1,10}", field);
				String fieldLength = "0";
				if (!fieldLengthMatch.getList().isEmpty())
				{
					fieldLength = fieldLengthMatch.getList().get(0);
				}

				// 提取字段类型
				String fieldType = null;
				if (!"0".equals(fieldLength))
				{
					RegexMatch fieldTypeMatch = new RegexMatch("`\\s(.*?)\\(", field);
					fieldType = fieldTypeMatch.getList().get(0).replaceAll("[\\s\\(`]", "");
				}
				else
				{
					RegexMatch fieldTypeMatch = new RegexMatch("`\\s{1,10}(.+?)(\\s|,)", field);
					fieldType = fieldTypeMatch.getList().get(0).replaceAll("[\\s`,]", "");
				}

				// 提取isNotNull
				RegexMatch isNotNullMatch = new RegexMatch("NOT NULL", field);
				int isNotNull = 0;
				if (!isNotNullMatch.getList().isEmpty() && isNotNullMatch.getList().get(0) != null)
					isNotNull = 1;
				else
					isNotNull = 0;
				
				//提取描述
				
				int isPrimaryKey = 0;
				JSONArray primaryKeyArray = (JSONArray) JSONArray.toJSON(table.get("primaryKey"));
				if(primaryKeyArray != null && !primaryKeyArray.isEmpty())
				{
					for(Object object : primaryKeyArray)
					{
						String pk = String.valueOf(object);
						if (fieldName.equalsIgnoreCase(pk))
						{
							isPrimaryKey = 1;
							break;
						}
					}
				}
				DatabaseTableField newField = new DatabaseTableField();
				newField.setTableID(tableID);
				newField.setFieldName(fieldName);
				newField.setFieldLength(fieldLength);
				newField.setFieldType(fieldType);
				newField.setIsNotNull(isNotNull);
				newField.setIsPrimaryKey(isPrimaryKey);

				affectedRow = this.databaseTableFieldMapper.addField(newField);
				if (affectedRow < 1)
					throw new RuntimeException("addField error");
			}
		}
		return 1;
	}

	/**
	 * 数据表导出成为json格式
	 */
	@Override
	public String exportDatabase(HttpServletRequest request, int dbID) throws Exception
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		String fileName = null;

		if (this.connDatabaseMapper.checkDatabasePermission(dbID, userID) != null)
		{
			HttpSession session = request.getSession(true);
			Map<String, Object> data = new HashMap<>();
			Database database = this.databaseMapper.getDatabaseInfo(dbID);
			if(database != null)
			{
				Map<String, Object> databaseInfo = new HashMap<>();
				databaseInfo.put("databaseName", database.getDbName());
				databaseInfo.put("databaseVersion", database.getDbVersion());
				databaseInfo.put("databaseType", database.getDbType());
				data.put("databaseInfo", databaseInfo);
				List<DatabaseTable> databaseTableList = databaseTableMapper.getTable(dbID);
				List<Map<String, Object>> databaseTables = new ArrayList<>();
				if(databaseTableList != null && !databaseTableList.isEmpty())
				{
					for(DatabaseTable databaseTable : databaseTableList)
					{
						Map<String, Object> table = new HashMap<>();
						table.put("tableName", databaseTable.getTableName());
						table.put("tableDesc", databaseTable.getTableDescription());
						table.put("fieldList", databaseTableFieldMapper.getField(databaseTable.getTableID()));
						databaseTables.add(table);
					}
				}
				data.put("tableList", databaseTables);
				File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
				if (!classPath.exists())
					classPath = new File("");
				File dir = new File(classPath.getAbsolutePath(), "dump");
				if (!dir.exists() || !dir.isDirectory())
					dir.mkdirs();
				String path = dir.getAbsolutePath();
				fileName = "/eolinker_export_mysql_" + session.getAttribute("userName") + "_" + System.currentTimeMillis()
						+ ".export";
				File file = new File(path + fileName);
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				JSONObject json = (JSONObject) JSONObject.toJSON(data);
				writer.write(json.toString());
				writer.flush();
				writer.close();
				return request.getContextPath() + "/dump" + fileName;
			}
			
		}
		return fileName;
	}

	/**
	 * 导入数据字典界面数据库
	 */
	@Override
	public Integer importDatabseByJson(String data) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		JSONObject databaseData = JSONObject.parseObject(data);

		if (databaseData != null && !databaseData.isEmpty())
		{
			JSONObject databaseInfo = databaseData.getJSONObject("databaseInfo");
			Database database = new Database();
			database.setDbName(databaseInfo.getString("databaseName"));
			database.setDbVersion(databaseInfo.getDouble("databaseVersion"));
			int dbType = databaseInfo.getInteger("databaseType") != null ? databaseInfo.getInteger("databaseType") : 0;
			database.setDbType(dbType);
			// 创建新数据库
			int affectedRow = this.databaseMapper.addDatabase(database);
			if (affectedRow < 1)
				throw new RuntimeException("insert database error");
			// 生成数据库与用户的联系
			affectedRow = this.connDatabaseMapper.addDatabaseConnection(database.getDbID(), userID);
			if (affectedRow < 1)
				throw new RuntimeException("insert conn error");

			JSONArray tableList = databaseData.getJSONArray("tableList");
			if (tableList != null && !tableList.isEmpty())
			{
				for (Iterator<Object> iterator = tableList.iterator(); iterator.hasNext();)
				{
					JSONObject tableData = (JSONObject) iterator.next();
					DatabaseTable databaseTable = new DatabaseTable();
					databaseTable.setDbID(database.getDbID());
					databaseTable.setTableName(tableData.getString("tableName"));
					databaseTable.setTableDescription(tableData.getString("tableDesc"));
					// 生成数据库表
					affectedRow = this.databaseTableMapper.addTable(databaseTable);
					if (affectedRow < 1)
						throw new RuntimeException("insert table error");
					JSONArray fieldList = tableData.getJSONArray("fieldList");
					if (fieldList != null && !fieldList.isEmpty())
					{
						for (Iterator<Object> iterator2 = fieldList.iterator(); iterator2.hasNext();)
						{
							JSONObject fieldData = (JSONObject) iterator2.next();
							DatabaseTableField databaseTableField = new DatabaseTableField();
							databaseTableField.setIsNotNull(fieldData.getInteger("isNotNull"));
							databaseTableField.setFieldName(fieldData.getString("fieldName"));
							databaseTableField.setFieldType(fieldData.getString("fieldType"));
							databaseTableField.setFieldLength(fieldData.getString("fieldLength"));
							databaseTableField.setIsPrimaryKey(fieldData.getInteger("isPrimaryKey"));
							databaseTableField.setFieldDescription(fieldData.getString("fieldDesc"));
							databaseTableField.setDefaultValue(fieldData.getString("defaultValue"));
							databaseTableField.setTableID(databaseTable.getTableID());
							// 生成字段表
							affectedRow = this.databaseTableFieldMapper.addField(databaseTableField);
							if (affectedRow < 1)
								throw new RuntimeException("insert field error");
						}
					}
				}
			}
			return 1;

		}
		return 0;
	}

	/**
	 * 获取数据库信息
	 */
	@Override
	public Map<String, Object> getDatabase(Integer dbID, Integer userID)
	{
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> database = databaseMapper.getDatabase(dbID, userID);
		if(database != null && !database.isEmpty())
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dbUpdateTime = dateFormat.format(database.get("dbUpdateTime"));
			database.put("dbUpdateTime", dbUpdateTime);
			result.put("databaseInfo", database);
		}
		result.put("tableCount", databaseTableMapper.getTableCount(dbID));
		result.put("fieldCount", databaseTableFieldMapper.getFieldCount(dbID));
		return result;
	}

	/**
	 * 导入oracle SQL文件
	 */
	@Override
	public int importOracleDatabase(String fileName, List<Map<String, Object>> tableList)
	{
		// TODO Auto-generated method stub
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		Database database = new Database();
		database.setDbName(fileName);
		database.setDbVersion(1.0);
		database.setDbType(1);
		if(databaseMapper.addDatabase(database) < 1)
			throw new RuntimeException("addDatabase error");
		if(connDatabaseMapper.addDatabaseConnection(database.getDbID(), userID) < 1)
			throw new RuntimeException("addDatabase error");
		for(Map<String, Object> table : tableList)
		{
			
			String tableName = (String) table.get("tableName");
			String tableDesc = (String) table.get("tableComment");
			DatabaseTable newTable = new DatabaseTable();
			newTable.setDbID(database.getDbID());
			newTable.setTableName(tableName);
			newTable.setTableDescription(tableDesc);
			int affectedRow = this.databaseTableMapper.addTable(newTable);
			if (affectedRow < 1)
				throw new RuntimeException("addTable error");

			int tableID = newTable.getTableID();
			String[] tableFields = table.get("tableField").toString().split(",");
			for (String field : tableFields)
			{
				// 提取字段名
				RegexMatch fieldNameMatch = new RegexMatch("\"(.*?)\"", field);
				String fieldName = fieldNameMatch.getList().get(0).replaceAll("\"", "");
				// 提取字段长度
				RegexMatch fieldLengthMatch = new RegexMatch("\\([0-9]{1,10}", field);
				String fieldLength = "0";
				if (!fieldLengthMatch.getList().isEmpty())
				{
					fieldLength = fieldLengthMatch.getList().get(0).substring(1);
				}
				// 提取字段类型
				String fieldType = null;
				if (!"0".equals(fieldLength))
				{
					RegexMatch fieldTypeMatch = new RegexMatch("\"\\s(.*?)\\(", field);
					fieldType = fieldTypeMatch.getList().get(0).replaceAll("[\\s\\(\"]", "");
				}
				else
				{
					RegexMatch fieldTypeMatch = new RegexMatch("\"\\s{1,10}(.+?)(\\s|,)", field);
					fieldType = fieldTypeMatch.getList().get(0).replaceAll("[\\s\",]", "");
				}
				// 提取isNotNull
				RegexMatch isNotNullMatch = new RegexMatch("NOT NULL", field);
				int isNotNull = 0;
				if (!isNotNullMatch.getList().isEmpty() && isNotNullMatch.getList().get(0) != null)
					isNotNull = 1;
				else
					isNotNull = 0;
				
				//提取描述
				String fieldDesc = "";
				JSONArray columnCommentArray = (JSONArray) JSONArray.toJSON(table.get("columnComment"));
				if(columnCommentArray != null && !columnCommentArray.isEmpty())
				{
					for(Object object : columnCommentArray)
					{
						String pk = String.valueOf(object);
						if (pk.contains("\""+fieldName+"\""))
						{
							fieldDesc = pk.substring(pk.indexOf('\'') + 1, pk.lastIndexOf('\''));
							break;
						}
					}
				}
				//提取主键
				int isPrimaryKey = 0;
				JSONArray primaryKeyArray = (JSONArray) JSONArray.toJSON(table.get("primaryKey"));
				if(primaryKeyArray != null && !primaryKeyArray.isEmpty())
				{
					for(Object object : primaryKeyArray)
					{
						String pk = String.valueOf(object);
						if (pk.contains("ADD PRIMARY KEY (\""+fieldName+"\");"))
						{
							isPrimaryKey = 1;
							break;
						}
					}
				}
				DatabaseTableField newField = new DatabaseTableField();
				newField.setTableID(tableID);
				newField.setFieldName(fieldName);
				newField.setFieldLength(fieldLength);
				newField.setFieldType(fieldType);
				newField.setIsNotNull(isNotNull);
				newField.setIsPrimaryKey(isPrimaryKey);
				newField.setFieldDescription(fieldDesc);
				affectedRow = this.databaseTableFieldMapper.addField(newField);
				if (affectedRow < 1)
					throw new RuntimeException("addField error");
			}
		}
		return 1;
	}

}
