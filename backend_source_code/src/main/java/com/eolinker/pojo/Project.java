package com.eolinker.pojo;

import java.sql.Timestamp;
import java.util.List;
/**
 * 项目
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
public class Project
{
	private Integer projectID;//项目ID
	private Integer projectType;//项目类型
	private String projectName;//项目名称
	private Timestamp projectUpdateTime;//项目更新时间
	private String projectVersion;//项目版本
	private List<Partner> partners;//项目成员
	private Integer userType;
	
	public Integer getProjectID()
	{
		return projectID;
	}
	
	public void setProjectID(Integer projectID)
	{
		this.projectID = projectID;
	}
	
	public Integer getProjectType()
	{
		return projectType;
	}
	
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	
	public String getProjectName()
	{
		return projectName;
	}
	
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	
	public Timestamp getProjectUpdateTime()
	{
		return projectUpdateTime;
	}
	
	public void setProjectUpdateTime(Timestamp projectUpdateTime)
	{
		this.projectUpdateTime = projectUpdateTime;
	}
	
	public String getProjectVersion()
	{
		return projectVersion;
	}
	
	public void setProjectVersion(String projectVersion)
	{
		this.projectVersion = projectVersion;
	}
	
	public List<Partner> getProjectMembers()
	{
		return partners;
	}
	
	public void setProjectMembers(List<Partner> partners)
	{
		this.partners = partners;
	}
	
	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}
}
