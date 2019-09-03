package com.eolinker.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.service.MessageService;
/**
 * 消息管理控制器
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
@Controller
@RequestMapping("/Message")
public class MessageController
{

	@Autowired
	private MessageService messageService;

	/**
	 * 获取消息列表
	 * 
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMessageList")
	public Map<String, Object> getMessageList(@RequestParam(value = "page", defaultValue = "1") String page)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (StringUtils.isNumeric(page))
			{
				map = this.messageService.getMessageList(Integer.parseInt(page));
				if (map != null)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map = new HashMap<String, Object>();
					map.put("statusCode", "260001");
				}
			}
			else
			{
				map.put("statusCode", "260001");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "260001");
			return map;
		}
		return map;
	}

	/**
	 * 查阅消息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/readMessage")
	public Map<String, Object> readMessage(@RequestParam("msgID") String msgID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (msgID == null || !msgID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "260004");
			else
			{
				int result = this.messageService.readMessage(Integer.parseInt(msgID));
				map.put("statusCode", (result > 0) ? "000000" : "260002");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "260004");
			return map;
		}

		return map;
	}

	/**
	 * 删除消息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delMessage")
	public Map<String, Object> delMessage(@RequestParam("msgID") String msgID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (msgID == null || !msgID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "260004");
			else
			{
				int result = this.messageService.delMessage(Integer.parseInt(msgID));
				map.put("statusCode", (result > 0) ? "000000" : "260005");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "260004");
			return map;
		}

		return map;
	}

	/**
	 * 清空消息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cleanMessage")
	public Map<String, Object> cleanMessage()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		int result = this.messageService.cleanMessage();
		map.put("statusCode", (result > 0) ? "000000" : "260001");
		return map;
	}

	/**
	 * 获取未读消息数量
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUnreadMessageNum")
	public Map<String, Object> getUnreadMessageNum()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		int result = this.messageService.getUnreadMessageNum();
		if (result > 0)
		{
			map.put("statusCode", "000000");
			map.put("unreadMsgNum", result);
		}
		else
			map.put("statusCode", "260001");
		return map;
	}

}
