package com.eolinker.controller;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.config.Config;
import com.eolinker.service.InstallService;

@Controller
@RequestMapping("/Install")
public class InstallController
{

	@Resource
	private InstallService installService;
	@Resource
	private Config config;

	/**
	 * 检测是否已经安装
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkConfig", method = RequestMethod.POST)
	public Map<String, Object> checkConfig(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<>();
		if (config.getVersion() != null && !config.getVersion().equals(""))
		{
			map.put("statusCode", "000000");
		}
		else
		{
			map.put("statusCode", "200003");
		}
		return map;
	}

	/**
	 * 检测是否已经安装
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkoutEnv", method = RequestMethod.POST)
	public Map<String, Object> checkoutEnv(HttpServletRequest request, String dbURL, String dbName, String dbUser,
			String dbPassword)
	{
		Map<String, Object> map = new HashMap<>();
		if (dbURL == null || dbURL.equals("") || dbURL.length() <= 0)
		{
			map.put("statusCode", "200004");
		}
		else if (dbName == null || dbName.equals("") || dbName.length() <= 0)
		{
			map.put("statusCode", "200005");
		}
		else if (dbUser == null || dbUser.equals("") || dbUser.length() <= 0)
		{
			map.put("statusCode", "200006");
		}
		else
		{
			Map<String, Object> result = installService.checkoutEnv(dbURL, dbName, dbUser, dbPassword);
			if (result != null)
			{
				map.put("envStatus", result);
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "200000");
			}
		}
		return map;
	}
	
	/**
	 * 安装
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public Map<String, Object> start(HttpServletRequest request, String dbURL, String dbName, String dbUser,
			String dbPassword, String websiteName, String language)
	{
		Map<String, Object> map = new HashMap<>();
		if (dbURL == null || dbURL.equals("") || dbURL.length() <= 0)
		{
			map.put("statusCode", "200004");
		}
		else if (dbName == null || dbName.equals("") || dbName.length() <= 0)
		{
			map.put("statusCode", "200005");
		}
		else if (dbUser == null || dbUser.equals("") || dbUser.length() <= 0)
		{
			map.put("statusCode", "200006");
		}
		else
		{
			boolean result = installService.start(dbURL, dbName, dbUser, dbPassword, websiteName, language);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "200000");
			}
		}
		return map;
	}
}
