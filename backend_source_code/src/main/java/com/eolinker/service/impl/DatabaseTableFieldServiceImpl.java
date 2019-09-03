package com.eolinker.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.mapper.ConnDatabaseMapper;
import com.eolinker.mapper.DatabaseMapper;
import com.eolinker.mapper.DatabaseTableFieldMapper;
import com.eolinker.mapper.DatabaseTableMapper;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.DatabaseTableField;
import com.eolinker.service.DatabaseTableFieldService;
/**
 * 数据库表字段[业务处理层]
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
@Transactional
public class DatabaseTableFieldServiceImpl implements DatabaseTableFieldService
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
	 * 获取成员权限
	 */
	@Override
	public int getUserType(int fieldID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = databaseTableFieldMapper.checkFieldPermission(fieldID, userID);

		if (dbID == null)
			return -1;
		else
		{
			ConnDatabase connDatabase = new ConnDatabase();
			connDatabase.setUserID(userID);
			connDatabase.setDbID(dbID);

			ConnDatabase userType = this.connDatabaseMapper.getDatabaseUserType(connDatabase);
			if (userType == null)
				return -1;
			else
				return userType.getUserType();
		}
	}

	/**
	 * 添加表字段
	 */
	@Override
	public int addField(DatabaseTableField databaseTableField)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = this.databaseTableMapper.checkTablePermission(Integer.valueOf(databaseTableField.getTableID()),
				userID);

		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			int affectedRow = this.databaseTableFieldMapper.addField(databaseTableField);

			if (affectedRow > 0)
				return Integer.valueOf(databaseTableField.getFieldID());
			else
				return -1;
		}
		else
			return -1;
	}

	/**
	 * 删除表字段
	 */
	@Override
	public int deleteField(int fieldID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = this.databaseTableFieldMapper.checkFieldPermission(fieldID, userID);
		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			int affectedRow = this.databaseTableFieldMapper.deleteField(fieldID);

			if (affectedRow > 0)
				return 1;
			else
				return -1;
		}
		else
			return -1;
	}

	/**
	 * 获取字段列表
	 */
	@Override
	public List<DatabaseTableField> getField(int tableID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		Integer dbID = this.databaseTableMapper.checkTablePermission(tableID, userID);

		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			List<DatabaseTableField> fieldList = this.databaseTableFieldMapper.getField(tableID);

			if (fieldList == null || fieldList.isEmpty())
				return null;
			else
				return fieldList;
		}
		else
			return null;
	}

	/**
	 * 修改字段
	 */
	@Override
	public int editField(DatabaseTableField databaseTableField)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		Integer dbID = this.databaseTableFieldMapper
				.checkFieldPermission(Integer.valueOf(databaseTableField.getFieldID()), userID);

		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			int affectedRow = this.databaseTableFieldMapper.editField(databaseTableField);

			return (affectedRow > 0) ? 1 : -1;
		}
		else
			return -1;
	}

}
