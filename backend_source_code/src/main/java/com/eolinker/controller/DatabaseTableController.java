package com.eolinker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.DatabaseTable;
import com.eolinker.service.DatabaseService;
import com.eolinker.service.DatabaseTableService;
/**
 * 数据库表控制器
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
@Controller
@RequestMapping("/DatabaseTable")
public class DatabaseTableController
{

	@Autowired
	private DatabaseTableService databaseTableService;

	@Autowired
	private DatabaseService databaseService;

	/**
	 * 添加数据表
	 * 
	 * @param dbID
	 * @param tableName
	 * @param tableDescription
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addTable")
	public Map<String, Object> addTable(@RequestParam(value = "dbID", required = true) String dbID,
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "tableDescription", required = true) String tableDescription)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			int nameLen = tableName.length();

			if (!dbID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230001"); // 数据库ID格式非法
			else if (!(nameLen >= 1 && nameLen <= 255))
				map.put("statusCode", "230002"); // 表名长度非法
			else
			{
				ConnDatabase connDatabase = this.databaseService.getUserType(Integer.valueOf(dbID));
				if (connDatabase == null || connDatabase.getUserType() < 0 || connDatabase.getUserType() > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}

				int result = this.databaseTableService.addTable(Integer.valueOf(dbID), tableName, tableDescription);

				if (result != 0)
				{
					map.put("statusCode", "000000");
					map.put("tableID", result);
				}
				else
				{
					map.put("statusCode", "230004");
				}
			}
		}
		catch (NumberFormatException e)
		{
			map.put("statusCode", "230001");
			e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 删除数据表
	 * 
	 * @param tableID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTable")
	public Map<String, Object> deleteTable(@RequestParam("tableID") String tableID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!tableID.matches("^[0-9]{1,11}$"))
			{
				map.put("statusCode", "230005");
				return map;
			}

			int userType = this.databaseTableService.getUserType(Integer.valueOf(tableID));
			if (userType < 0 || userType > 2)
			{
				map.put("statusCode", "120007");
				return map;
			}
			else
			{
				int result = this.databaseTableService.deleteTable(Integer.valueOf(tableID));
				if (result < 0)
					map.put("statusCode", "230006");
				else
					map.put("statusCode", "000000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230005");
			return map;
		}

		return map;
	}

	/**
	 * 获取数据表列表
	 * 
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTable")
	public Map<String, Object> getTable(@RequestParam("dbID") String dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!dbID.matches("^[0-9]{1,11}$"))
			{
				map.put("statusCode", "230001");
			}
			else
			{
				List<DatabaseTable> tableList = this.databaseTableService.getTable(Integer.valueOf(dbID));
				if (tableList != null && !tableList.isEmpty())
				{
					map.put("statusCode", "000000");
					map.put("tableList", tableList);
				}
				else
					map.put("statusCode", "230007");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230001");
			return map;
		}
		return map;
	}

	/**
	 * 修改数据表
	 * 
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editTable")
	public Map<String, Object> editTable(@RequestParam(value = "tableID", required = true) String tableID,
			@RequestParam(value = "tableName", required = false) String tableName,
			@RequestParam(value = "tableDescription", required = false) String tableDesc)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!tableID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230005");
			else if (tableName != null && !(tableName.length() > 1 && tableName.length() < 255))
				map.put("statusCode", "230002");
			else
			{
				int result = this.databaseTableService.editTable(Integer.valueOf(tableID), tableName, tableDesc);
				if (result > 0)
					map.put("statusCode", "000000");
				else
					map.put("statusCode", "230008");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230005");
			return map;
		}
		return map;
	}

}
