package com.eolinker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.pojo.ApiGroup;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.service.DatabaseService;
import com.eolinker.util.RegexMatch;

/**
 * 数据库管理控制器
 * 
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 *         eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 *         如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 *         eoLinker AMS开源版的开源协议遵循GPL
 *         V3，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 *         官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 *         使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 *         用户讨论QQ群：707530721
 */
@Controller
@RequestMapping("/Database")
public class DatabaseController
{

	@Autowired
	private DatabaseService databaseService;

	/**
	 * 添加数据库
	 * 
	 * @param dbName
	 * @param dbVersion
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addDatabase")
	public Map<String, Object> addDatabase(@RequestParam("dbName") String dbName,
			@RequestParam(value = "dbVersion", defaultValue = "1.0") String dbVersion)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		int nameLen = dbName.length();

		if (!(nameLen >= 1 && nameLen <= 32))
		{
			map.put("statusCode", "220001");
			return map;
		}
		else
		{
			try
			{
				Integer dbID = databaseService.addDatabase(dbName, Double.valueOf(dbVersion));
				if (dbID != null)
				{
					map.put("statusCode", "000000");
					map.put("dbID", dbID);
				}
				else
					map.put("statusCode", "220003");
			}
			catch (NumberFormatException e)
			{
				map.put("statusCode", "220002");
				return map;
			}
		}

		return map;
	}

	/**
	 * 删除数据库
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDatabase")
	public Map<String, Object> deleteDatabase(@RequestParam("dbID") Integer dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (dbID == null || dbID <= 0)
		{
			map.put("statusCode", "220004");
			return map;
		}
		else
		{
			ConnDatabase connDatabase = databaseService.getUserType(dbID);
			if (connDatabase == null)
			{
				map.put("statusCode", "120007");
				return map;
			}
			else if (connDatabase.getUserType() < 0 || connDatabase.getUserType() > 1)
			{
				map.put("statusCode", "120007");
				return map;
			}
			Integer result = databaseService.deleteDatabase(dbID);
			if (result != null)
				map.put("statusCode", "000000");
			else
				map.put("statusCode", "220005");
		}

		return map;
	}

	/**
	 * 获取数据库列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDatabaseList")
	public Map<String, Object> getDatabaseList()
	{
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> databases = this.databaseService.getDatabaseList();

		if (databases != null)
		{
			map.put("statusCode", "000000");
			map.put("databaseList", databases);
		}
		else
			map.put("statusCode", "220006");

		return map;
	}
	
	/**
	 * 获取数据库信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDatabase")
	public Map<String, Object> getDatabase(HttpServletRequest request, Integer dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (dbID == null || dbID <= 0)
		{
			map.put("statusCode", "220004");
			return map;
		}
		else
		{
			ConnDatabase connDatabase = databaseService.getUserType(dbID);
			if (connDatabase == null || connDatabase.getUserType() < 0 || connDatabase.getUserType() > 3)
			{
				map.put("statusCode", "120007");
				return map;
			}
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Map<String, Object> databases = this.databaseService.getDatabase(dbID, userID);
			if (databases != null)
			{
				databases.put("statusCode", "000000");
				return databases;
			}
			else
				map.put("statusCode", "220006");
		}
		return map;
	}


	/**
	 * 修改数据库
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editDatabase")
	public Map<String, Object> editDatabase(@RequestParam(value = "dbID", required = true) String dbID,
			@RequestParam(value = "dbName", required = false) String dbName,
			@RequestParam(value = "dbVersion", required = false) Double dbVersion)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			ConnDatabase connDatabase = this.databaseService.getUserType(Integer.parseInt(dbID));
			if (connDatabase == null || connDatabase.getUserType() < 0 || connDatabase.getUserType() > 2)
			{
				map.put("statusCode", "120007");
				return map;
			}

			if (!dbID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "220004");
			else if (dbName != null && !(dbName.length() >= 1 && dbName.length() <= 32))
				map.put("statusCode", "220001");
			else
			{
				Integer result = this.databaseService.editDatabase(Integer.valueOf(dbID), dbName, dbVersion);

				if (result != null)
					map.put("statusCode", "000000");
				else
					map.put("statusCode", "220007");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220002");
			return map;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220001");
			return map;
		}

		return map;
	}

	/**
	 * 导出数据库
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportDatabase")
	public Map<String, Object> exportDatabase(HttpServletRequest request, @RequestParam("dbID") String dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		ConnDatabase connDatabase = this.databaseService.getUserType(Integer.parseInt(dbID));
		if (connDatabase == null || connDatabase.getUserType() < 0 || connDatabase.getUserType() > 2)
		{
			map.put("statusCode", "120007");
			return map;
		}

		if (!dbID.matches("^[0-9]{1,11}$"))
			map.put("statusCode", "220004");
		else
		{
			try
			{
				String fileName = this.databaseService.exportDatabase(request, Integer.parseInt(dbID));
				if (fileName != null)
				{
					map.put("statusCode", "000000");
					map.put("fileName", fileName);
				}
				else
				{
					map.put("statusCode", "220000");
				}

			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
				map.put("statusCode", "220000");
				return map;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				map.put("statusCode", "220000");
				return map;
			}
		}

		return map;
	}

	/**
	 * 导入SQL格式数据表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importDatabase")
	public Map<String, Object> importDatabase(String fileName,
			@RequestParam("dumpSql") String dumpSql)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
				// 正则匹配出所有创建表的语句块
				RegexMatch sqlMatch = new RegexMatch("CREATE.*?TABLE[\\s\\S]+?;", dumpSql);
				List<String> sqlList = sqlMatch.getList();
				List<Map<String, Object>> tableList = new ArrayList<>();
				for (String tableSql : sqlList)
				{
					// 正则提取表名 index为0
					RegexMatch tableNameMatch = new RegexMatch("`(.*?)`", tableSql);

					// 截取表的字段信息
					String tableField = tableSql.substring(tableSql.indexOf('(') + 1, tableSql.lastIndexOf(')'));

					// 正则提取主键
					RegexMatch pkFieldMatch = new RegexMatch("PRIMARY KEY \\(.*?\\)", tableField);
					List<String> pkList = null;
					if (pkFieldMatch.getList() != null && !pkFieldMatch.getList().isEmpty())
					{
						String pkField = pkFieldMatch.getList().get(0);
						RegexMatch pkMatch = new RegexMatch("`.*?`", pkField);
						pkList = new ArrayList<String>();
						for (String pk : pkMatch.getList())
						{
							pkList.add(pk.replaceAll("`", ""));
						}
					}

					Map<String, Object> tableMap = new HashMap<String, Object>();
					tableMap.put("tableName", tableNameMatch.getList().get(0));
					tableMap.put("tableField", tableField.replaceAll("utf8|utf16|(DEFAULT\\s{0,2}'.+?')", ""));
					tableMap.put("primaryKey", pkList);
					tableList.add(tableMap);
				}
				if (tableList != null && !tableList.isEmpty())
				{
					int result = this.databaseService.importDatabase(fileName, tableList);
					if (result > 0)
						map.put("statusCode", "000000");
					else
						map.put("statusCode", "220008");
				}
				else
					map.put("statusCode", "220009");
			
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230001");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220008");
			return map;
		}
		return map;
	}

	/**
	 * 导入数据库
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importDatabseByJson")
	public Map<String, Object> importDatabseByJson(String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (data == null)
		{
			map.put("statusCode", "220010");
		}
		else
		{
			try
			{
				Integer result = this.databaseService.importDatabseByJson(data);

				if (result != null && result == 1)
					map.put("statusCode", "000000");
				else
					map.put("statusCode", "220000");
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
				map.put("statusCode", "220000");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				map.put("statusCode", "220000");
			}
		}
		return map;
	}
	
	/**
	 * 导入SQL格式数据表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importOracleDatabase")
	public Map<String, Object> importOracleDatabase(String fileName,
			@RequestParam("dumpSql") String dumpSql)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
				// 正则匹配出所有创建表的语句块
				RegexMatch sqlMatch = new RegexMatch("CREATE.*?TABLE[\\s\\S]+?;", dumpSql);
				List<String> sqlList = sqlMatch.getList();
				List<Map<String, Object>> tableList = new ArrayList<>();
				for (String tableSql : sqlList)
				{
					// 正则提取表名 index为0
					RegexMatch tableNameMatch = new RegexMatch("(\\.\"(.*?)\")", tableSql);
					String str =  tableNameMatch.getList().get(0);
					String tableName = str.substring(2, str.length()-1);
					RegexMatch tableCommentMatch = new RegexMatch("COMMENT ON TABLE.*?"+tableName+".*?\\'(.*?)\\'.*?;", dumpSql);
					List<String> comment = tableCommentMatch.getList();
					if(comment != null && comment.size() > 0)
					{
						comment = new RegexMatch("(\'(.*?)\')", comment.get(0)).getList();
						if(comment != null && comment.size() > 0)
						{
							str= comment.get(0);
						}
					}
					String tableComment = str.substring(1, str.length()-1);
					RegexMatch columnCommentMatch = new RegexMatch("COMMENT ON COLUMN.*?"+tableName+".*?\\'(.*?)\\'.*?;", dumpSql);
					List<String> columnComment = columnCommentMatch.getList();
					RegexMatch columnNullMatch = new RegexMatch("ALTER TABLE \".*?\"\\..*?"+tableName+".*?NOT NULL.*?\\);", dumpSql);
					List<String> columnNull = columnNullMatch.getList();
					RegexMatch primaryKeyMatch = new RegexMatch("ALTER TABLE \".*?\"\\..*?"+tableName+".*?PRIMARY KEY.*?\\);", dumpSql);
					List<String> primaryKey = primaryKeyMatch.getList();
					String tableField = tableSql.substring(tableSql.indexOf('(') + 1, tableSql.lastIndexOf(')'));
					Map<String, Object> tableMap = new HashMap<String, Object>();
					tableMap.put("tableName", tableName);
					tableMap.put("tableField", tableField.replaceAll("utf8|utf16|(DEFAULT\\s{0,2}'.+?')", ""));
					tableMap.put("primaryKey", primaryKey);
					tableMap.put("tableComment", tableComment);
					tableMap.put("columnComment", columnComment);
					tableMap.put("columnNull", columnNull);
					tableList.add(tableMap);
				}
				if (tableList != null && !tableList.isEmpty())
				{
					int result = this.databaseService.importOracleDatabase(fileName, tableList);
					if (result > 0)
						map.put("statusCode", "000000");
					else
						map.put("statusCode", "220008");
				}
				else
					map.put("statusCode", "220009");
			
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230001");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220008");
			return map;
		}
		return map;
	}

}
