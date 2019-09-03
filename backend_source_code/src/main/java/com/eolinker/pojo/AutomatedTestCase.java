package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 自动化测试用例
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
public class AutomatedTestCase
{
	private Integer caseID;//用例ID
	private Integer projectID;//项目ID
	private Integer userID;//更新人员ID
	private String caseName;//用例名称
	private String caseDesc;//用例描述
	private Timestamp createTime;//创建时间
	private Timestamp updateTime;//更新时间
	private Integer groupID;//分组ID
	private Integer caseType;//用例类型
	private String caseCode;//用例代码

	public Integer getCaseID()
	{
		return caseID;
	}

	public void setCaseID(Integer caseID)
	{
		this.caseID = caseID;
	}

	public Integer getProjectID()
	{
		return projectID;
	}

	public void setProjectID(Integer projectID)
	{
		this.projectID = projectID;
	}

	public Integer getUserID()
	{
		return userID;
	}

	public void setUserID(Integer userID)
	{
		this.userID = userID;
	}

	public String getCaseName()
	{
		return caseName;
	}

	public void setCaseName(String caseName)
	{
		this.caseName = caseName;
	}

	public String getCaseDesc()
	{
		return caseDesc;
	}

	public void setCaseDesc(String caseDesc)
	{
		this.caseDesc = caseDesc;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	public Integer getGroupID()
	{
		return groupID;
	}

	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	public Integer getCaseType()
	{
		return caseType;
	}

	public void setCaseType(Integer caseType)
	{
		this.caseType = caseType;
	}

	public String getCaseCode()
	{
		return caseCode;
	}

	public void setCaseCode(String caseCode)
	{
		this.caseCode = caseCode;
	}
	
}
