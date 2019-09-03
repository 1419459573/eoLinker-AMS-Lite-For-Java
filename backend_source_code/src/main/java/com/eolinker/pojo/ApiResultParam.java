package com.eolinker.pojo;
/**
 * 接口返回参数
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
public class ApiResultParam 
{
	private Integer paramID;//返回参数ID
	private String paramName;//返回参数描述
	private String paramKey;//返回参数名称
	private Integer apiID;//接口ID
	private Integer paramNotNull;//是否必含
	private Integer paramType;//参数类型
	private String paramValue;//参数值
	public Integer getParamID()
	{
		return paramID;
	}
	public void setParamID(Integer paramID)
	{
		this.paramID = paramID;
	}
	public String getParamName()
	{
		return paramName;
	}
	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}
	public String getParamKey()
	{
		return paramKey;
	}
	public void setParamKey(String paramKey)
	{
		this.paramKey = paramKey;
	}
	public Integer getApiID()
	{
		return apiID;
	}
	public void setApiID(Integer apiID)
	{
		this.apiID = apiID;
	}
	public Integer getParamNotNull()
	{
		return paramNotNull;
	}
	public void setParamNotNull(Integer paramNotNull)
	{
		this.paramNotNull = paramNotNull;
	}
	public Integer getParamType()
	{
		return paramType;
	}
	public void setParamType(Integer paramType)
	{
		this.paramType = paramType;
	}
	public String getParamValue()
	{
		return paramValue;
	}
	public void setParamValue(String paramValue)
	{
		this.paramValue = paramValue;
	}
}
