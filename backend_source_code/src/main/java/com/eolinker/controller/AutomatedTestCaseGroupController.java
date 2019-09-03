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
import com.eolinker.pojo.AutomatedTestCaseGroup;
import com.eolinker.pojo.Partner;
import com.eolinker.service.AutomatedTestCaseGroupService;
import com.eolinker.service.ProjectService;
/**
 * 自动化测试用例分组控制器
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
@RequestMapping("/AutomatedTestCaseGroup")
public class AutomatedTestCaseGroupController
{

	@Resource
	private ProjectService projectService;
	@Resource
	private AutomatedTestCaseGroupService automatedTestCaseGroupService;

	/**
	 * 添加用例分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public Map<String, Object> addGroup(HttpServletRequest request, AutomatedTestCaseGroup automatedTestCaseGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (automatedTestCaseGroup.getGroupName() == null || automatedTestCaseGroup.getGroupName().length() < 1
				|| automatedTestCaseGroup.getGroupName().length() > 32)
		{
			map.put("statusCode", "850001");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, automatedTestCaseGroup.getProjectID());
			if (partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				boolean result = automatedTestCaseGroupService.addGroup(automatedTestCaseGroup, userID);
				if (result)
				{
					map.put("statusCode", "000000");
					map.put("groupID", automatedTestCaseGroup.getGroupID());
				}
				else
				{
					map.put("statusCode", "850000");
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
	public Map<String, Object> deleteGroup(HttpServletRequest request, Integer groupID, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupID == null || groupID <= 0)
		{
			map.put("statusCode", "850002");
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
				boolean result = automatedTestCaseGroupService.deleteGroup(projectID, groupID, userID);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "850000");
				}
			}
		}
		return map;
	}

	/**
	 * 获取分组列表
	 * 
	 * @param request
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGroupList", method = RequestMethod.POST)
	public Map<String, Object> getGroupList(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 3)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = automatedTestCaseGroupService.getGroupList(projectID);
			String groupOrder = automatedTestCaseGroupService.getGroupOrderList(projectID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("groupList", result);
				map.put("groupOrder", groupOrder);
			}
			else
			{
				map.put("statusCode", "850000");
			}
		}

		return map;
	}

	/**
	 * 修改用例分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editGroup", method = RequestMethod.POST)
	public Map<String, Object> editGroup(HttpServletRequest request, AutomatedTestCaseGroup automatedTestCaseGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (automatedTestCaseGroup.getProjectID() == null || automatedTestCaseGroup.getProjectID() < 0)
		{
			map.put("statusCode", "850001");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, automatedTestCaseGroup.getProjectID());
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("statusCode", "100002");
			}
			else
			{
				boolean result = automatedTestCaseGroupService.editGroup(automatedTestCaseGroup, userID);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "850000");
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
		if (orderList == null || orderList.equals("") || orderList.length() <= 0)
		{
			map.put("statusCode", "850004");
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
				boolean result = automatedTestCaseGroupService.sortGroup(projectID, userID, orderList);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "160000");
				}
			}
		}
		return map;
	}

	/**
	 * 导出分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportGroup", method = RequestMethod.POST)
	public Map<String, Object> exportGroup(HttpServletRequest request, Integer projectID, Integer groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupID == null || groupID <= 0)
		{
			map.put("statusCode", "850003");
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
				Map<String, Object> result = automatedTestCaseGroupService.exportGroup(projectID, userID, groupID);
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
						String fileName = "/eolinker_test_case_group_dump_" + session.getAttribute("userName") + "_"
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
						map.put("statusCode", "160000");
					}
				}
				else
				{
					map.put("statusCode", "160000");
				}
			}
		}
		return map;
	}

	/**
	 * 导入分组
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importGroup", method = RequestMethod.POST)
	public Map<String, Object> importGroup(HttpServletRequest request, Integer projectID, String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null || data.equals("") || data.length() <= 0)
		{
			map.put("statusCode", "850003");
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
				boolean result = automatedTestCaseGroupService.importGroup(projectID, userID, data);
				if (result)
				{
					map.put("statusCode", "000000");
				}
				else
				{
					map.put("statusCode", "160000");
				}
			}
		}
		return map;
	}
}
