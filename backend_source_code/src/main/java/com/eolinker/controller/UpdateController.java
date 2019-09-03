package com.eolinker.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.eolinker.config.Config;
import com.eolinker.service.UpdateService;
import com.eolinker.util.Proxy;

@Controller
@RequestMapping("/Update")
public class UpdateController
{

	@Resource
	private UpdateService updateService;
	@Resource
	private Config config;

	/**
	 * 检查更新
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUpdate", method = RequestMethod.POST)
	public Map<String, Object> checkUpdate(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (config.getAllowUpdate() != null && config.getAllowUpdate().equals("true"))
		{
			Proxy proxy = new Proxy();
			String completeURL = "https://api.eolinker.com/openSource/JavaVersionUpdate/checkout";
			Map<String, Object> result = proxy.proxyToDesURL("GET", completeURL, null, null);

			JSONObject testResult = (JSONObject) JSONObject.toJSON(result.get("testResult"));
			JSONObject body = JSONObject.parseObject(testResult.getString("body"));
			if (body != null && body.getString("version").compareTo(config.getVersion()) > 0)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "320002");
			}
		}
		else
		{
			map.put("statusCode", "320004");
		}
		return map;
	}

	/**
	 * 自动更新
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/autoUpdate", method = RequestMethod.POST)
	public Map<String, Object> autoUpdate(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (config.getAllowUpdate() != null && config.getAllowUpdate().equals("true"))
		{
			Proxy proxy = new Proxy();
			String completeURL = "https://api.eolinker.com/openSource/JavaVersionUpdate/checkout";
			Map<String, Object> result = proxy.proxyToDesURL("GET", completeURL, null, null);
			JSONObject testResult = (JSONObject) JSONObject.toJSON(result.get("testResult"));
			JSONObject body = JSONObject.parseObject(testResult.getString("body"));
			if (body != null && body.getString("version").compareTo(config.getVersion()) > 0)
			{
				String updateUrl = body.getString("updateUrl");
				boolean data = updateService.autoUpdate(updateUrl);
				if (data)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "320003");
				}
			}
			else
			{
				map.put("statusCode", "320002");
			}
		}
		else
		{
			map.put("statusCode", "320004");
		}
		return map;
	}

	/**
	 * 自动更新
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/manualUpdate", method = RequestMethod.POST)
	public Map<String, Object> manualUpdate(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (config.getAllowUpdate() != null && config.getAllowUpdate().equals("true"))
		{
			boolean data = updateService.manualUpdate();
			if (data)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "320003");
			}

		}
		else
		{
			map.put("statusCode", "320004");
		}
		return map;
	}

}
