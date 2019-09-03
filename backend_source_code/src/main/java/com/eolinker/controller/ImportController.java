package com.eolinker.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.eolinker.service.ImportService;
import com.eolinker.service.ProjectService;
/**
 * 导入项目控制器
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
@RequestMapping("/Import")
public class ImportController
{
	@Resource
	private ImportService importService;
	@Resource
	private ProjectService projectService;

	/**
	 * 导入eolinker项目
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importEoapi")
	public Map<String, Object> importEoapi(HttpServletRequest request, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null)
		{
			map.put("statusCode", "310004");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			boolean result = importService.importEoapi(data, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "310000");
			}
		}
		return map;
	}
	
	/**
	 * 导入postman项目
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importPostMan")
	public Map<String, Object> importPostMan(HttpServletRequest request, String data, Integer version)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null)
		{
			map.put("statusCode", "310004");
		} 
		else if(version == null || (version != 1 && version != 2))
		{
			map.put("statusCode", "310002");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			boolean result = false;
			if(version ==1)
			{
				result = importService.importPostmanV1(data, userID);
			}
			else
			{
				result = importService.importPostmanV2(data, userID);
			}
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "310000");
			}
		}
		return map;
	}
	
	/**
	 * 导入swagger项目
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importSwagger")
	public Map<String, Object> importSwagger(HttpServletRequest request, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null)
		{
			map.put("statusCode", "310004");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			boolean result = importService.importSwagger(data, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "310000");
			}
		}
		return map;
	}
	
	/**
	 * 导入rap项目
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importRAP")
	public Map<String, Object> importRAP(HttpServletRequest request, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null)
		{
			map.put("statusCode", "310004");
		}
		else
		{
			JSONObject projectData = JSONObject.parseObject(data);
			if(projectData == null)
			{
				map.put("statusCode", "310001");
				return map;
			}
			else
			{
				if(projectData.get("modelJSON") == null)
				{
					map.put("statusCode", "310001");
					return map;
				}
				else
				{
					String modelJSON = projectData.getString("modelJSON");
					JSONObject jsonObject = JSONObject.parseObject(modelJSON);
					if(jsonObject == null)
					{
						map.put("statusCode", "310003");
						return map;
					}
					HttpSession session = request.getSession(true);
					Integer userID = (Integer) session.getAttribute("userID");
					boolean result = importService.importRAP(modelJSON, userID);
					if (result)
					{
						map.put("statusCode", "000000");
					}
					else
					{
						map.put("statusCode", "310000");
					}
				}
			}
			
		}
		return map;
	}
	
	/**
	 * 导入rap项目
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importDHC")
	public Map<String, Object> importDHC(HttpServletRequest request, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null)
		{
			map.put("statusCode", "310004");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			boolean result = importService.importEoapi(data, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "310000");
			}
		}
		return map;
	}
}
