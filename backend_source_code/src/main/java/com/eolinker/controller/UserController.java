package com.eolinker.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.eolinker.service.MessageService;
import com.eolinker.service.UserService;
/**
 * 用户控制器
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
@RequestMapping("/User")
public class UserController
{
	@Resource
	private UserService userService;
	@Resource
	private MessageService messageService;

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		session.invalidate();
		map.put("statusCode", "000000");
		return JSON.toJSONString(map);
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request, String oldPassword, String newPassword)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (oldPassword == null || !newPassword.matches("^[0-9a-zA-Z]{32}$") || newPassword == null
				|| !oldPassword.matches("^[0-9a-zA-Z]{32}$"))
		{
			// 密码非法
			map.put("statusCode", "120001");
		}
		else if (oldPassword.equals(newPassword))
		{
			// 密码相同
			map.put("statusCode", "120002");
		}
		else
		{
			HttpSession session = request.getSession(true);
			String userName = (String) session.getAttribute("userName");
			boolean result = userService.changePassword(userName, oldPassword, newPassword);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "120000");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 修改昵称
	 * 
	 * @param request
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeNickName", method = RequestMethod.POST)
	public String changeNickName(HttpServletRequest request, String nickName)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (nickName == null || nickName.length() > 20)
		{
			// 昵称格式非法
			map.put("statusCode", "120003");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			boolean result = userService.changeNickName(userID, nickName);
			if (result)
			{
				session.setAttribute("userNickName", nickName);
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "120000");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public String getUserInfo(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("userID") != null)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("userID", (Integer) session.getAttribute("userID"));
			result.put("userName", (String) session.getAttribute("userName"));
			result.put("userNickName", (String) session.getAttribute("userNickName"));
			result.put("unreadMsgNum", messageService.getUnreadMessageNum((Integer) session.getAttribute("userID")));
			map.put("userInfo", result);
			map.put("statusCode", "000000");
		}
		else
		{
			map.put("statusCode", "120000");
		}

		return JSON.toJSONString(map);
	}

}
