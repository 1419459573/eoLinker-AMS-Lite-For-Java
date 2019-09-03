package com.eolinker.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.pojo.*;
import com.eolinker.service.*;
/**
 * 接口分组控制器
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
@RequestMapping("/Group")
public class ApiGroupController
{

	@Resource
	ApiGroupService apiGroupService;
	@Resource
	ProjectService projectService;

	/**
	 * 添加接口分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public Map<String, Object> addApiGroup(HttpServletRequest request, ApiGroup apiGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (apiGroup.getProjectID() == null || apiGroup.getProjectID() <= 0)
		{
			map.put("statusCode", "150001");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, apiGroup.getProjectID());
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				apiGroup.setUserID(userID);
				boolean result = apiGroupService.addApiGroup(apiGroup);
				if (result)
				{
					map.put("statusCode", "000000");
					map.put("groupID", apiGroup.getGroupID());
				}
				else
				{
					map.put("statusCode", "150000");
				}
			}
		}
		return map;
	}

	/**
	 * 删除分组
	 * 
	 * @param request
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
	public Map<String, Object> deleteGroup(HttpServletRequest request, Integer groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupID == null || groupID <= 0)
		{
			map.put("statusCode", "150002");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Integer projectID = apiGroupService.checkGroupPermission(groupID, userID);
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				boolean result = apiGroupService.deleteGroup(projectID, groupID, userID);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "150000");
				}
			}
		}
		return map;
	}

	/**
	 * 获取接口列表
	 * 
	 * @param request
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGroupList", method = RequestMethod.POST)
	public Map<String, Object> getGroupList(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (projectID == null || projectID <= 0)
		{
			map.put("statusCode", "150001");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				List<Map<String, Object>> result = apiGroupService.getGroupList(projectID);
				String groupOrder = apiGroupService.getGroupOrderList(projectID);
				if (result != null && !result.isEmpty())
				{
					map.put("statusCode", "000000");
					map.put("groupList", result);
					map.put("groupOrder", groupOrder);
				}
				else
				{
					map.put("groupList", "150000");
				}
			}

		}
		return map;
	}

	/**
	 * 修改接口分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editGroup", method = RequestMethod.POST)
	public Map<String, Object> editGroup(HttpServletRequest request, ApiGroup apiGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (apiGroup.getGroupID() <= 0)
		{
			map.put("statusCode", "150002");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Integer projectID = apiGroupService.checkGroupPermission(apiGroup.getGroupID(), userID);
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				apiGroup.setUserID(userID);
				apiGroup.setProjectID(projectID);
				boolean result = apiGroupService.editGroup(apiGroup);
				if (result)
				{
					map.put("statusCode", "000000");
					map.put("groupID", apiGroup.getGroupID());
				}
				else
				{
					map.put("statusCode", "150000");
				}
			}
		}
		return map;
	}

	/**
	 * 修改接口分组排序
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sortGroup", method = RequestMethod.POST)
	public Map<String, Object> sortGroup(HttpServletRequest request, Integer projectID, String orderList)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (projectID == null || projectID <= 0)
		{
			map.put("statusCode", "150001");
		}
		else if (orderList == null || orderList.equals("") || orderList.length() <= 0)
		{
			map.put("statusCode", "150003");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				boolean result = apiGroupService.sortGroup(projectID, userID, orderList);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "150000");
				}
			}
		}
		return map;
	}

	/**
	 * 导出接口分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportGroup", method = RequestMethod.POST)
	public Map<String, Object> exportGroup(HttpServletRequest request, Integer groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupID == null || groupID <= 0)
		{
			map.put("statusCode", "150002");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Integer projectID = apiGroupService.checkGroupPermission(groupID, userID);
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				Map<String, Object> result = apiGroupService.exportGroup(projectID, groupID, userID);
				if (result != null && !result.isEmpty())
				{
					try
					{
						File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
						if (!classPath.exists())
							classPath = new File("");
						File dir = new File(classPath.getAbsolutePath(), "dump");
						if (!dir.exists() || !dir.isDirectory())
							dir.mkdirs();
						String path = dir.getAbsolutePath();
						String fileName = "/eoLinker_api_export_" + session.getAttribute("userName") + "_"
								+ System.currentTimeMillis() + ".export";
						File file = new File(path + fileName);
						file.createNewFile();
						FileWriter fileWriter = new FileWriter(file);
						JSONObject json = (JSONObject) JSONObject.toJSON(result);
						fileWriter.write(json.toString());
						fileWriter.close();
						map.put("fileName", request.getContextPath() + "/dump" + fileName);
						map.put("statusCode", "000000");
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						map.put("statusCode", "150000");
					}
				}
				else
				{
					map.put("statusCode", "150000");
				}
			}
		}
		return map;
	}

	/**
	 * 导入接口分组
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importGroup", method = RequestMethod.POST)
	public Map<String, Object> importGroup(HttpServletRequest request, Integer projectID, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (data == null || data == "")
		{
			map.put("statusCode", "150005");
		}
		else if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = apiGroupService.importGroup(projectID, userID, data);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "150000");
			}
		}
		return map;
	}

}
