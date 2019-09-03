package com.eolinker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.pojo.DatabaseTableField;
import com.eolinker.service.DatabaseTableFieldService;
/**
 * 数据库表字段控制器
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
@RequestMapping("/DatabaseTableField")
public class DatabaseTableFieldController
{

	@Autowired
	private DatabaseTableFieldService databaseTableFieldService;

	/**
	 * 添加字段
	 * 
	 * @param databaseTableField
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addField")
	public Map<String, Object> addField(DatabaseTableField databaseTableField)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (databaseTableField.getTableID() == null || databaseTableField.getTableID() <= 0)
		{
			map.put("statusCode", "240001");
			return map;
		}
		else if (databaseTableField.getFieldName() == null
				|| (databaseTableField.getFieldName() != null && !(databaseTableField.getFieldName().length() >= 1
						&& databaseTableField.getFieldName().length() <= 255)))
		{
			map.put("statusCode", "240002");
			return map;
		}
		else if (databaseTableField.getFieldType() == null
				|| (databaseTableField.getFieldType() != null && !(databaseTableField.getFieldType().length() >= 1
						&& databaseTableField.getFieldType().length() <= 255)))
		{
			map.put("statusCode", "240003");
			return map;
		}
		else if (databaseTableField.getIsNotNull() == null)
		{
			map.put("statusCode", "240004");
			return map;
		}
		else if (databaseTableField.getIsPrimaryKey() == null)
		{
			map.put("statusCode", "240004");
			return map;
		}
		else
		{
			int result = this.databaseTableFieldService.addField(databaseTableField);
			if (result > 0)
			{
				map.put("statusCode", "000000");
				map.put("fieldID", result);
			}
			else
				map.put("statusCode", "240006");

		}
		return map;
	}

	/**
	 * 删除字段
	 * 
	 * @param fieldID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteField")
	public Map<String, Object> deleteField(@RequestParam("fieldID") String fieldID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (fieldID == null || !fieldID.matches("^[0-9]{1,11}$"))
			{
				map.put("statusCode", "240007");
				return map;
			}

			int userType = this.databaseTableFieldService.getUserType(Integer.valueOf(fieldID));

			if (userType < 0 || userType > 2)
			{
				map.put("statusCode", "120007");
			}
			else
			{
				int result = this.databaseTableFieldService.deleteField(Integer.valueOf(fieldID));
				if (result > 0)
					map.put("statusCode", "000000");
				else
					map.put("statusCode", "240008");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "240007");
			return map;
		}
		return map;
	}

	/**
	 * 获取字段列表
	 * 
	 * @param tableID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getField")
	public Map<String, Object> getField(@RequestParam("tableID") String tableID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (tableID == null || !tableID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "240001");
			else
			{
				List<DatabaseTableField> fieldList = this.databaseTableFieldService.getField(Integer.parseInt(tableID));

				if (fieldList != null)
				{
					map.put("statusCode", "000000");
					map.put("fieldList", fieldList);
				}
				else
					map.put("statusCode", "240009");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "240001");
			return map;
		}
		return map;
	}

	/**
	 * 修改字段
	 * 
	 * @param databaseTableField
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editField")
	public Map<String, Object> editField(DatabaseTableField databaseTableField)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (databaseTableField.getFieldID() == null || databaseTableField.getFieldID() <= 0)
		{
			map.put("statusCode", "240007");
			return map;
		}
		else if (databaseTableField.getFieldName() == null
				|| (databaseTableField.getFieldName() != null && !(databaseTableField.getFieldName().length() >= 1
						&& databaseTableField.getFieldName().length() <= 255)))
		{
			map.put("statusCode", "240002");
			return map;
		}
		else if (databaseTableField.getFieldType() == null
				|| (databaseTableField.getFieldType() != null && !(databaseTableField.getFieldType().length() >= 1
						&& databaseTableField.getFieldType().length() <= 255)))
		{
			map.put("statusCode", "240003");
			return map;
		}
		else if (databaseTableField.getIsNotNull() == null)
		{
			map.put("statusCode", "240004");
			return map;
		}
		else if (databaseTableField.getIsPrimaryKey() == null)
		{
			map.put("statusCode", "240004");
			return map;
		}
		else
		{
			int result = this.databaseTableFieldService.editField(databaseTableField);
			if (result > 0)
				map.put("statusCode", "000000");
			else
				map.put("statusCode", "240010");

		}
		return map;
	}

}
