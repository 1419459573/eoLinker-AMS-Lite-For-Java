package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 接口bean
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
public class Api
{
	private Integer apiID;//接口ID
	private String apiName;//接口名称
	private String apiURI;//接口URI
	private Integer apiProtocol;//请求协议
	private String apiFailureMock;//失败示例
	private String apiSuccessMock;//成功示例
	private Integer apiRequestType;//请求类型
	private Integer apiSuccessMockType;//mock成功类型
	private Integer apiFailureMockType;//mock失败类型
	private Integer apiStatus;//接口状态
	private Timestamp apiUpdateTime;//接口更新时间
	private Integer groupID;//分组ID
	private Integer projectID;//项目ID
	private Integer starred;//星标状态
	private Integer removed;//是否被移到回收站
	private Timestamp removeTime;//移到回收站的时间
	private Integer apiNoteType;//接口详细说明文本类型
	private String apiNoteRaw;//详细说明富文本
	private String apiNote;//详细说明Markdown
	private Integer apiRequestParamType;//接口请求类型
	private String apiRequestRaw;//接口源数据
	private Integer updateUserID;//更新人员ID
	private String mockRule;//mock规则
	private String mockResult;//mock数据
	private String mockConfig;//mock配置
	private String apiSuccessStatusCode;//成功状态码
	private String apiFailureStatusCode;//失败状态码
	private String beforeInject;//前置注入
	private String afterInject;//后置注入

	public Integer getApiID()
	{
		return apiID;
	}

	public void setApiID(Integer apiID)
	{
		this.apiID = apiID;
	}

	public String getApiName()
	{
		return apiName;
	}

	public void setApiName(String apiName)
	{
		this.apiName = apiName;
	}

	public String getApiURI()
	{
		return apiURI;
	}

	public void setApiURI(String apiURI)
	{
		this.apiURI = apiURI;
	}

	public Integer getApiProtocol()
	{
		return apiProtocol;
	}

	public void setApiProtocol(Integer apiProtocol)
	{
		this.apiProtocol = apiProtocol;
	}

	public String getApiFailureMock()
	{
		return apiFailureMock;
	}

	public void setApiFailureMock(String apiFailureMock)
	{
		this.apiFailureMock = apiFailureMock;
	}

	public String getApiSuccessMock()
	{
		return apiSuccessMock;
	}

	public void setApiSuccessMock(String apiSuccessMock)
	{
		this.apiSuccessMock = apiSuccessMock;
	}

	public Integer getApiRequestType()
	{
		return apiRequestType;
	}

	public void setApiRequestType(Integer apiRequestType)
	{
		this.apiRequestType = apiRequestType;
	}

	public Integer getApiSuccessMockType()
	{
		return apiSuccessMockType;
	}

	public void setApiSuccessMockType(Integer apiSuccessMockType)
	{
		this.apiSuccessMockType = apiSuccessMockType;
	}

	public Integer getApiFailureMockType()
	{
		return apiFailureMockType;
	}

	public void setApiFailureMockType(Integer apiFailureMockType)
	{
		this.apiFailureMockType = apiFailureMockType;
	}

	public Integer getApiStatus()
	{
		return apiStatus;
	}

	public void setApiStatus(Integer apiStatus)
	{
		this.apiStatus = apiStatus;
	}

	public Timestamp getApiUpdateTime()
	{
		return apiUpdateTime;
	}

	public void setApiUpdateTime(Timestamp apiUpdateTime)
	{
		this.apiUpdateTime = apiUpdateTime;
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

	public Integer getStarred()
	{
		return starred;
	}

	public void setStarred(Integer starred)
	{
		this.starred = starred;
	}

	public Integer getRemoved()
	{
		return removed;
	}

	public void setRemoved(Integer removed)
	{
		this.removed = removed;
	}

	public Timestamp getRemoveTime()
	{
		return removeTime;
	}

	public void setRemoveTime(Timestamp removeTime)
	{
		this.removeTime = removeTime;
	}

	public Integer getApiNoteType()
	{
		return apiNoteType;
	}

	public void setApiNoteType(Integer apiNoteType)
	{
		this.apiNoteType = apiNoteType;
	}

	public String getApiNoteRaw()
	{
		return apiNoteRaw;
	}

	public void setApiNoteRaw(String apiNoteRaw)
	{
		this.apiNoteRaw = apiNoteRaw;
	}

	public String getApiNote()
	{
		return apiNote;
	}

	public void setApiNote(String apiNote)
	{
		this.apiNote = apiNote;
	}

	public Integer getApiRequestParamType()
	{
		return apiRequestParamType;
	}

	public void setApiRequestParamType(Integer apiRequestParamType)
	{
		this.apiRequestParamType = apiRequestParamType;
	}

	public String getApiRequestRaw()
	{
		return apiRequestRaw;
	}

	public void setApiRequestRaw(String apiRequestRaw)
	{
		this.apiRequestRaw = apiRequestRaw;
	}

	public Integer getUpdateUserID()
	{
		return updateUserID;
	}

	public void setUpdateUserID(Integer updateUserID)
	{
		this.updateUserID = updateUserID;
	}

	public String getMockRule()
	{
		return mockRule;
	}

	public void setMockRule(String mockRule)
	{
		this.mockRule = mockRule;
	}

	public String getMockResult()
	{
		return mockResult;
	}

	public void setMockResult(String mockResult)
	{
		this.mockResult = mockResult;
	}

	public String getMockConfig()
	{
		return mockConfig;
	}

	public void setMockConfig(String mockConfig)
	{
		this.mockConfig = mockConfig;
	}

	public String getApiSuccessStatusCode() {
		return apiSuccessStatusCode;
	}

	public void setApiSuccessStatusCode(String apiSuccessStatusCode) {
		this.apiSuccessStatusCode = apiSuccessStatusCode;
	}

	public String getApiFailureStatusCode() {
		return apiFailureStatusCode;
	}

	public void setApiFailureStatusCode(String apiFailureStatusCode) {
		this.apiFailureStatusCode = apiFailureStatusCode;
	}

	public String getBeforeInject() {
		return beforeInject;
	}

	public void setBeforeInject(String beforeInject) {
		this.beforeInject = beforeInject;
	}

	public String getAfterInject() {
		return afterInject;
	}

	public void setAfterInject(String afterInject) {
		this.afterInject = afterInject;
	}

}
