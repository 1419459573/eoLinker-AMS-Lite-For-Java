package com.eolinker.pojo;
/**
 * 自动化测试单例
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
public class AutomatedTestCaseSingle
{
	private Integer connID;//关联ID
	private Integer caseID;//用例ID
	private String caseData;//用例数据
	private String caseCode;//用例代码
	private String statusCode;//用例状态码
	private Integer matchType;//匹配类型
	private String matchRule;//匹配规则
	private String apiURI;//接口URI
	private String apiName;//接口名称
	private Integer apiRequestType;//请求类型
	private Integer orderNumber;//排序

	public Integer getConnID()
	{
		return connID;
	}

	public void setConnID(Integer connID)
	{
		this.connID = connID;
	}

	public Integer getCaseID()
	{
		return caseID;
	}

	public void setCaseID(Integer caseID)
	{
		this.caseID = caseID;
	}

	public String getCaseData()
	{
		return caseData;
	}

	public void setCaseData(String caseData)
	{
		this.caseData = caseData;
	}

	public String getCaseCode()
	{
		return caseCode;
	}

	public void setCaseCode(String caseCode)
	{
		this.caseCode = caseCode;
	}

	public String getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	public Integer getMatchType()
	{
		return matchType;
	}

	public void setMatchType(Integer matchType)
	{
		this.matchType = matchType;
	}

	public String getMatchRule()
	{
		return matchRule;
	}

	public void setMatchRule(String matchRule)
	{
		this.matchRule = matchRule;
	}

	public String getApiURI()
	{
		return apiURI;
	}

	public void setApiURI(String apiURI)
	{
		this.apiURI = apiURI;
	}

	public Integer getApiRequestType()
	{
		return apiRequestType;
	}

	public void setApiRequestType(Integer apiRequestType)
	{
		this.apiRequestType = apiRequestType;
	}

	public Integer getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public String getApiName()
	{
		return apiName;
	}

	public void setApiName(String apiName)
	{
		this.apiName = apiName;
	}
}
