package com.eolinker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eolinker.pojo.AutomatedTestCaseSingle;
import com.eolinker.pojo.Partner;
import com.eolinker.service.AutomatedTestCaseService;
import com.eolinker.service.AutomatedTestCaseSingleService;
import com.eolinker.service.ProjectService;
/**
 * 自动化测试用例单例控制器
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
@RequestMapping("/AutomatedTestCaseSingle")
public class AutomatedTestCaseSingleController
{
	@Resource
	private ProjectService projectService;
	@Resource
	private AutomatedTestCaseService automatedTestCaseService;
	@Resource
	private AutomatedTestCaseSingleService automatedTestCaseSingleService;
	
	/**
	 * 添加单例
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addSingleTestCase", method = RequestMethod.POST)
	public Map<String, Object> addSingleTestCase(HttpServletRequest request, AutomatedTestCaseSingle automatedTestCaseSingle, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer ID = automatedTestCaseService.getProjectIDByCaseID(automatedTestCaseSingle.getCaseID());
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2 || projectID != ID)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Integer connID = automatedTestCaseSingleService.addSingleTestCase(automatedTestCaseSingle, projectID, userID);
			if (connID != null && connID > 0)
			{
				map.put("statusCode", "000000");
				map.put("connID", connID);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
	
	/**
	 * 修改单例
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editSingleTestCase", method = RequestMethod.POST)
	public Map<String, Object> editSingleTestCase(HttpServletRequest request, AutomatedTestCaseSingle automatedTestCaseSingle, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer ID = automatedTestCaseService.getProjectIDByCaseID(automatedTestCaseSingle.getCaseID());
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2 || projectID != ID)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = automatedTestCaseSingleService.editSingleTestCase(automatedTestCaseSingle, projectID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
	
	/**
	 * 获取单例列表
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleTestCaseList", method = RequestMethod.POST)
	public Map<String, Object> getSingleTestCaseList(HttpServletRequest request, Integer projectID, Integer caseID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 3)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = automatedTestCaseSingleService.getSingleTestCaseList(projectID, caseID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("singCaseList", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
	
	/**
	 * 获取单例详情
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleTestCaseInfo", method = RequestMethod.POST)
	public Map<String, Object> getSingleTestCaseInfo(HttpServletRequest request, Integer projectID, Integer connID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 3)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Map<String, Object> result = automatedTestCaseSingleService.getSingleTestCaseInfo(projectID, connID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("singleCaseInfo", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
	
	/**
	 * 删除单例
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSingleTestCase", method = RequestMethod.POST)
	public Map<String, Object> deleteSingleTestCase(HttpServletRequest request, Integer projectID, String connID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = automatedTestCaseSingleService.deleteSingleTestCase(projectID, connID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
	
	/**
	 * 获取已有接口列表
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApiList", method = RequestMethod.POST)
	public Map<String, Object> getApiList(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = automatedTestCaseSingleService.getApiList(projectID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiList", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
}
