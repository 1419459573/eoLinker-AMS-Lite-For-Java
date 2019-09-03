package com.eolinker.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.mapper.MessageMapper;
import com.eolinker.service.MessageService;
import com.eolinker.util.Arithmetic;
/**
 * 消息[业务处理层]
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
public class MessageServiceImpl implements MessageService
{

	@Autowired
	private MessageMapper messageMapper;

	/**
	 * 获取消息列表
	 */
	@Override
	public Map<String, Object> getMessageList(int page)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		List<Map<String, Object>> messageList = messageMapper.getMessageList((page - 1) * 15, userID);
		if (messageList != null && !messageList.isEmpty())
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> message : messageList)
			{
				String msgSendTime = dateFormat.format(message.get("msgSendTime"));
				message.put("msgSendTime", msgSendTime);
			}
			int msgCount = messageMapper.getMessageListCount(userID);
			map.put("messageList", messageList);
			map.put("msgCount", msgCount);
			map.put("pageCount", Math.ceil(Arithmetic.div(msgCount, 15, 3)));
			map.put("pageNow", page);
		}
		else
			return null;

		return map;
	}

	/**
	 * 读消息
	 */
	@Override
	public int readMessage(int msgID)
	{
		return this.messageMapper.readMessage(msgID);
	}

	/**
	 * 删除消息
	 */
	@Override
	public int delMessage(int msgID)
	{
		return this.messageMapper.delMessage(msgID);
	}

	/**
	 * 清除消息
	 */
	@Override
	public int cleanMessage()
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		return this.messageMapper.cleanMessage(userID);
	}

	/**
	 * 获取未读消息数量
	 */
	@Override
	public int getUnreadMessageNum()
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		return this.messageMapper.getUnreadMessageNum(userID);
	}

	/**
	 * 获取未读消息数量
	 */
	@Override
	public int getUnreadMessageNum(Integer userID)
	{
		// TODO Auto-generated method stub
		return messageMapper.getUnreadMessageNum(userID);
	}

}
