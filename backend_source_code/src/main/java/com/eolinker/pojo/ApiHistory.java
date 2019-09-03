package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 接口历史
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
public class ApiHistory
{
	private Integer projectID;//项目ID
	private Integer groupID;//分组ID
	private Integer apiID;//接口ID
	private String historyJson;//历史数据
	private String updateDesc;//更新描述
	private Integer updateUserID;//更新人员ID
	private Timestamp updateTime;//更新时间
	private Integer isNow;//是否当前版本

	public Integer getProjectID()
	{
		return projectID;
	}

	public void setProjectID(Integer projectID)
	{
		this.projectID = projectID;
	}

	public Integer getGroupID()
	{
		return groupID;
	}

	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	public Integer getApiID()
	{
		return apiID;
	}

	public void setApiID(Integer apiID)
	{
		this.apiID = apiID;
	}

	public String getHistoryJson()
	{
		return historyJson;
	}

	public void setHistoryJson(String historyJson)
	{
		this.historyJson = historyJson;
	}

	public String getUpdateDesc()
	{
		return updateDesc;
	}

	public void setUpdateDesc(String updateDesc)
	{
		this.updateDesc = updateDesc;
	}

	public Integer getUpdateUserID()
	{
		return updateUserID;
	}

	public void setUpdateUserID(Integer updateUserID)
	{
		this.updateUserID = updateUserID;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	public Integer getIsNow()
	{
		return isNow;
	}

	public void setIsNow(Integer isNow)
	{
		this.isNow = isNow;
	}
}
