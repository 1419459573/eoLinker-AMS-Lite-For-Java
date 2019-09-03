package com.eolinker.pojo;
/**
 * 数据库协作人员
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
public class ConnDatabase
{

	private int connID;//关联ID
	private int dbID;//数据库ID
	private int userID;//协作成员ID
	private int userType;//成员权限类型
	private int inviteUserID;//邀请者ID
	private String partnerNickName;//备注

	public int getConnID()
	{
		return connID;
	}

	public void setConnID(int connID)
	{
		this.connID = connID;
	}

	public int getDbID()
	{
		return dbID;
	}

	public void setDbID(int dbID)
	{
		this.dbID = dbID;
	}

	public int getUserID()
	{
		return userID;
	}

	public void setUserID(int userID)
	{
		this.userID = userID;
	}

	public int getUserType()
	{
		return userType;
	}

	public void setUserType(int userType)
	{
		this.userType = userType;
	}

	public int getInviteUserID()
	{
		return inviteUserID;
	}

	public void setInviteUserID(int inviteUserID)
	{
		this.inviteUserID = inviteUserID;
	}

	public String getPartnerNickName()
	{
		return partnerNickName;
	}

	public void setPartnerNickName(String partnerNickName)
	{
		this.partnerNickName = partnerNickName;
	}

}
