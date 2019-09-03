package com.eolinker.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.eolinker.config.Config;
import com.eolinker.pojo.User;
import com.eolinker.service.*;
/**
 * 访客控制器
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 * eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 * 如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 * eoLinker AMS开源版的开源协议遵循Apache License2.0，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 * 官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 * 使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 * 用户讨论QQ群：707530721
 */
@Controller
@RequestMapping("/Guest")
public class GuestController
{
	@Resource
	private UserService userService;
	@Resource
	private Config config;

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, Object> register(HttpServletRequest request, User user)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(config.getAllowRegister() == null || !config.getAllowRegister().equals("true"))
		{
			map.put("statusCode", "130005");
			return map;
		}
		if (user.getUserName() == null || !user.getUserName().matches("^[0-9a-zA-Z][0-9a-zA-Z_]{3,63}$"))
		{
			map.put("statusCode", "110001");
		}
		else if (user.getUserPassword() == null || !user.getUserPassword().matches("^[0-9a-zA-Z]{32}$"))
		{
			map.put("statusCode", "110002");
		}
		else if (user.getUserNickName() != null && user.getUserNickName().length() > 16)
		{
			map.put("statusCode", "110003");
		}
		else
		{
			User u = userService.getUserByUserName(user.getUserName());
			if (u != null)
			{
				map.put("statusCode", "110004");
			}
			else
			{
				Integer userID = userService.addUser(user);
				if (userID != 0)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "110000");
				}
			}
		}
		return map;
	}

	/**
	 * 检查用户名是否存在
	 * @param request
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUserNameExist", method = RequestMethod.POST)
	public String checkUserNameExist(HttpServletRequest request, String userName)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (userName == null || !userName.matches("^[0-9a-zA-Z][0-9a-zA-Z_]{3,63}$"))
		{
			map.put("StatusCode", "130001");
		}
		else
		{
			User user = userService.getUserByUserName(userName);
			if (user == null)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "110000");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(HttpServletRequest request, String loginName, String loginPassword)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (loginName == null || !loginName.matches("^[0-9a-zA-Z][0-9a-zA-Z_]{3,63}$"))
		{
			map.put("statusCode", "110001");
		}
		else if (loginPassword == null || !loginPassword.matches("^[0-9a-zA-Z]{32}$"))
		{
			map.put("statusCode", "110002");
		}
		else
		{
			Map<String, Object> result = userService.login(request, loginName, loginPassword);
			if (result !=  null && !result.isEmpty())
			{
				map.put("userID", result.get("userID"));
				map.put("JSESSIONID", result.get("JSESSIONID"));
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "110000");
			}
		}
		return map;
	}

	/**
	 * 检测是否登录
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	public Map<String, Object> checkLogin(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session =  request.getSession(true);
		if (session.getAttribute("userID") != null)
		{
			map.put("statusCode", "000000");
		}
		else
		{
			map.put("statusCode", "100001");
		}
		return map;
	}
}
