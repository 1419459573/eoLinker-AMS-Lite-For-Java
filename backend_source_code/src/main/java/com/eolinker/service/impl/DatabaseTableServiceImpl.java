package com.eolinker.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.mapper.ConnDatabaseMapper;
import com.eolinker.mapper.DatabaseMapper;
import com.eolinker.mapper.DatabaseTableMapper;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.DatabaseTable;
import com.eolinker.service.DatabaseTableService;
/**
 * 数据库表[业务处理层]
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
public class DatabaseTableServiceImpl implements DatabaseTableService
{

	@Autowired
	private DatabaseTableMapper databaseTableMapper;

	@Autowired
	private ConnDatabaseMapper connDatabaseTableMapper;

	@Autowired
	private DatabaseMapper databaseMapper;

	/**
	 * 添加表
	 */
	@Override
	public int addTable(Integer dbID, String tableName, String tableDesc)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = connDatabaseTableMapper.checkDatabasePermission(Integer.valueOf(dbID), userID);

		if (databaseID != null && databaseID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(Integer.valueOf(dbID));

			DatabaseTable databaseTable = new DatabaseTable();
			databaseTable.setDbID(databaseID);
			databaseTable.setTableName(tableName);
			databaseTable.setTableDescription(tableDesc);

			int affectedRow = this.databaseTableMapper.addTable(databaseTable);
			if (affectedRow > 0)
				return databaseTable.getTableID();
			else
				return 0;
		}
		else
			return 0;
	}

	/**
	 * 检测是否对表有操作权限
	 */
	@Override
	public Integer checkTablePermission(int tableID, int userID)
	{
		Integer dbID = this.databaseTableMapper.checkTablePermission(tableID, userID);
		if (dbID == null || dbID <= 0)
		{
			return 0;
		}
		else
			return dbID;
	}

	/**
	 * 删除表
	 */
	@Override
	public int deleteTable(int tableID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = this.databaseTableMapper.checkTablePermission(tableID, userID);
		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			int affectedRow = this.databaseTableMapper.deleteTable(tableID);
			if (affectedRow > 0)
				return 1;
			else
				return -1;
		}
		else
		{
			return -1;
		}
	}

	/**
	 * 获取用户类型
	 */
	@Override
	public int getUserType(int tableID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = this.databaseTableMapper.checkTablePermission(tableID, userID);
		if (dbID == null || dbID < 1)
			return -1;

		ConnDatabase connDatabase = new ConnDatabase();
		connDatabase.setUserID(userID);
		connDatabase.setDbID(dbID);

		ConnDatabase databaseUserType = this.connDatabaseTableMapper.getDatabaseUserType(connDatabase);
		if (databaseUserType == null)
			return -1;
		else
			return databaseUserType.getUserID();
	}

	/**
	 * 获取表列表
	 */
	@Override
	public List<DatabaseTable> getTable(int dbID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = this.connDatabaseTableMapper.checkDatabasePermission(dbID, userID);

		if (databaseID != null && databaseID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);
			List<DatabaseTable> tableList = this.databaseTableMapper.getTable(dbID);
			return tableList;
		}
		else
			return null;
	}

	/**
	 * 修改表
	 */
	@Override
	public int editTable(int tableID, String tableName, String tableDesc)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer dbID = this.databaseTableMapper.checkTablePermission(tableID, userID);

		if (dbID != null && dbID > 0)
		{
			this.databaseMapper.updateDatabaseUpdateTime(dbID);

			DatabaseTable databaseTable = new DatabaseTable();
			databaseTable.setTableID(tableID);
			databaseTable.setTableName(tableName);
			databaseTable.setTableDescription(tableDesc);

			int affectedRow = this.databaseTableMapper.editTable(databaseTable);
			return (affectedRow > 0) ? 1 : -1;
		}
		else
			return -1;
	}

}
