package com.eolinker.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.dto.PartnerListDTO;
import com.eolinker.mapper.ConnDatabaseMapper;
import com.eolinker.mapper.DatabaseMapper;
import com.eolinker.mapper.DatabasePartnerMapper;
import com.eolinker.mapper.MessageMapper;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.Database;
import com.eolinker.pojo.Message;
import com.eolinker.service.DatabasePartnerService;
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
@Transactional
public class DatabasePartnerServiceImpl implements DatabasePartnerService
{

	@Autowired
	private DatabaseMapper databaseMapper;

	@Autowired
	private DatabasePartnerMapper databasePartnerMapper;

	@Autowired
	private ConnDatabaseMapper connDatabaseMapper;

	@Autowired
	private MessageMapper messageMapper;

	/**
	 * 获取成员类型
	 */
	@Override
	public int getUserType(int dbID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		ConnDatabase connDatabase = new ConnDatabase();
		connDatabase.setUserID(userID);
		connDatabase.setDbID(dbID);

		ConnDatabase userType = connDatabaseMapper.getDatabaseUserType(connDatabase);
		if (userType == null || userType.getUserType() < 0)
		{
			return -1;
		}
		else
		{
			return userType.getUserType();
		}
	}

	/**
	 * 邀请成员
	 */
	@Override
	public int invitePartner(int dbID, int inviteUserID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = this.connDatabaseMapper.checkDatabasePermission(dbID, userID);
		if (databaseID != null && databaseID > 0)
		{
			Database databaseInfo = this.databaseMapper.getDatabaseInfo(dbID);

			String summary = "您已被邀请加入数据库：" + databaseInfo.getDbName() + "，开始您的高效协作之旅吧！";
			String msg = "<p>您好！亲爱的用户：</p><p>您已经被加入数据库：<b style='color:#4caf50'>" + databaseInfo.getDbName()
					+ "</b>，现在你可以参与数据字典的开发协作工作。</p>"
					+ "<p>如果您在使用的过程中遇到任何问题，欢迎前往<a href='http://blog.eolinker.com/#/bbs/'><b style='color:#4caf50'>交流社区</b></a>反馈意见，谢谢！。</p>";

			ConnDatabase connDatabase = new ConnDatabase();
			connDatabase.setInviteUserID(userID);
			connDatabase.setUserID(inviteUserID);
			connDatabase.setDbID(dbID);

			// 邀请协作人员
			int affectedRow = this.databasePartnerMapper.invitePartner(connDatabase);
			if (affectedRow > 0 && connDatabase.getConnID() > 0)
			{

				Message message = new Message();
				message.setFromUserID(userID);
				message.setToUserID(inviteUserID);
				message.setMsgType(1);
				message.setSummary(summary);
				message.setMsg(msg);

				this.messageMapper.sendMessage(message);
				return connDatabase.getConnID();
			}
			else
				return -1;
		}
		else
			return -1;
	}

	/**
	 * 检测成员是否存在
	 */
	@Override
	public int checkIsInvited(int dbID, String userName)
	{
		Integer connID = this.databasePartnerMapper.checkIsInvited(dbID, userName);
		if (connID != null && connID > 0)
			return connID;
		else
			return -1;
	}

	/**
	 * 移除成员
	 */
	@Override
	public int removePartner(int dbID, int connID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = this.connDatabaseMapper.checkDatabasePermission(dbID, userID);
		if (databaseID != null && databaseID > 0)
		{
			Database databaseInfo = this.databaseMapper.getDatabaseInfo(dbID);

			String summary = "您已被移除出数据库：" + databaseInfo.getDbName();
			String msg = "<p>您好！亲爱的用户：</p><p>您已经被移除出数据库：<b style='color:#4caf50'>" + databaseInfo.getDbName()
					+ "</b>。</p>"
					+ "<p>如果您在使用的过程中遇到任何问题，欢迎前往<a href='http://blog.eolinker.com/#/bbs/'><b style='color:#4caf50'>交流社区</b></a>反馈意见，谢谢！。</p>";

			Integer remotePartnerID = this.databasePartnerMapper.getUserID(connID);
			if (this.databasePartnerMapper.removePartner(dbID, connID) > 0)
			{

				Message message = new Message();
				message.setFromUserID(0);
				message.setToUserID(remotePartnerID);
				message.setMsgType(1);
				message.setSummary(summary);
				message.setMsg(msg);

				if (this.messageMapper.sendMessage(message) > 0)
					return 1;
				else
					return -1;
			}
			else
				return -1;
		}
		else
			return -1;
	}

	/**
	 * 获取成员列表
	 */
	@Override
	public List<PartnerListDTO> getPartnerList(int dbID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = this.connDatabaseMapper.checkDatabasePermission(dbID, userID);
		if (databaseID != null && databaseID > 0)
		{
			List<PartnerListDTO> partnerList = this.databasePartnerMapper.getPartnerList(dbID);

			for (PartnerListDTO dto : partnerList)
			{
				if (dto.getUserID() == userID)
					dto.setIsNow(1);
				else
					dto.setIsNow(0);
				dto.setUserID(null);
			}

			return partnerList;
		}
		else
			return null;
	}

	/**
	 * 退出数据库
	 */
	@Override
	public int quitPartner(int dbID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer databaseID = this.connDatabaseMapper.checkDatabasePermission(dbID, userID);
		if (databaseID != null && databaseID > 0)
		{
			int affectedRow = this.databasePartnerMapper.quitPartner(dbID, userID);

			if (affectedRow > 0)
				return 1;
			else
				return -1;
		}
		else
			return -1;
	}

	/**
	 * 修改成员备注
	 */
	@Override
	public int editPartnerNickName(int dbID, int connID, String nickName)
	{
		ConnDatabase connDatabase = new ConnDatabase();
		connDatabase.setDbID(dbID);
		connDatabase.setConnID(connID);
		connDatabase.setPartnerNickName(nickName);

		int affectedRow = this.databasePartnerMapper.editPartnerNickName(connDatabase);
		return (affectedRow > 0) ? 1 : -1;
	}

	/**
	 * 修改成员权限类型
	 */
	@Override
	public int editPartnerType(int dbID, int connID, int userType)
	{
		ConnDatabase connDatabase = new ConnDatabase();
		connDatabase.setDbID(dbID);
		connDatabase.setConnID(connID);
		connDatabase.setUserType(userType);

		int affectedRow = this.databasePartnerMapper.editPartnerType(connDatabase);
		return (affectedRow > 0) ? 1 : -1;
	}

}
