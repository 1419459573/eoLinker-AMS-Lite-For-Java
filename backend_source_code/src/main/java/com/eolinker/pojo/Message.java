package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 消息
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
public class Message
{
	private Integer msgID;//消息ID
	private Integer toUserID;//接收者ID
	private Integer fromUserID;//发送者ID
	private Timestamp msgSendTime;//发送时间
	private Integer msgType;//短信类型
	private String summary;//标题
	private String msg;//内容
	private Integer isRead;//是否已读
	private String otherMsg;//其他内容
	
	public Integer getMsgID()
	{
		return msgID;
	}
	
	public void setMsgID(Integer msgID)
	{
		this.msgID = msgID;
	}
	
	public Integer getToUserID()
	{
		return toUserID;
	}
	
	public void setToUserID(Integer toUserID)
	{
		this.toUserID = toUserID;
	}
	
	public Integer getFromUserID()
	{
		return fromUserID;
	}
	
	public void setFromUserID(Integer fromUserID)
	{
		this.fromUserID = fromUserID;
	}
	
	public Timestamp getMsgSendTime()
	{
		return msgSendTime;
	}
	
	public void setMsgSendTime(Timestamp msgSendTime)
	{
		this.msgSendTime = msgSendTime;
	}
	
	public Integer getMsgType()
	{
		return msgType;
	}
	
	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	
	public Integer getIsRead()
	{
		return isRead;
	}
	
	public void setIsRead(Integer isRead)
	{
		this.isRead = isRead;
	}
	
	public String getOtherMsg()
	{
		return otherMsg;
	}
	
	public void setOtherMsg(String otherMsg)
	{
		this.otherMsg = otherMsg;
	}
}
