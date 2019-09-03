package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 项目文档
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
public class Document
{

	private Integer documentID;// 文档ID
	private Integer groupID;// 文档分组ID
	private Integer projectID;// 项目ID
	private Integer contentType;// 文档类型
	private String contentRaw;// 富文本内容
	private String content;// Markdown内容
	private String title;// 文档标题
	private Timestamp updateTime;// 更新时间
	private Integer userID;// 更新人员ID
	private Integer topParentGroupID;//一级父分组ID

	public Integer getDocumentID()
	{
		return documentID;
	}

	public void setDocumentID(Integer documentID)
	{
		this.documentID = documentID;
	}

	public Integer getGroupID()
	{
		return groupID;
	}

	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	public Integer getProjectID()
	{
		return projectID;
	}

	public void setProjectID(Integer projectID)
	{
		this.projectID = projectID;
	}

	public Integer getContentType()
	{
		return contentType;
	}

	public void setContentType(Integer contentType)
	{
		this.contentType = contentType;
	}

	public String getContentRaw()
	{
		return contentRaw;
	}

	public void setContentRaw(String contentRaw)
	{
		this.contentRaw = contentRaw;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	public Integer getUserID()
	{
		return userID;
	}

	public void setUserID(Integer userID)
	{
		this.userID = userID;
	}

	public Integer getTopParentGroupID()
	{
		return topParentGroupID;
	}

	public void setTopParentGroupID(Integer topParentGroupID)
	{
		this.topParentGroupID = topParentGroupID;
	}

}
