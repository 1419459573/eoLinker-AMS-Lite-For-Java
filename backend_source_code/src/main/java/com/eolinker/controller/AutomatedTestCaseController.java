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
import com.eolinker.pojo.AutomatedTestCase;
import com.eolinker.pojo.Partner;
import com.eolinker.service.AutomatedTestCaseService;
import com.eolinker.service.ProjectService;
/**
 * 自动化测试用例控制器
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
@RequestMapping("/AutomatedTestCase")
public class AutomatedTestCaseController
{
	@Resource
	private ProjectService projectService;
	@Resource
	private AutomatedTestCaseService automatedTestCaseService;

	/**
	 * 添加用例
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addTestCase", method = RequestMethod.POST)
	public Map<String, Object> addTestCase(HttpServletRequest request, AutomatedTestCase automatedTestCase)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer projectID = automatedTestCaseService.checkGroupPermission(automatedTestCase.getGroupID(), userID);
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			automatedTestCase.setUserID(userID);
			automatedTestCase.setCaseType(0);
			Integer caseID = automatedTestCaseService.addTestCase(automatedTestCase);
			if (caseID != null && caseID > 0)
			{
				map.put("statusCode", "000000");
				map.put("caseID", caseID);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}

	/**
	 * 修改用例
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editTestCase", method = RequestMethod.POST)
	public Map<String, Object> editTestCase(HttpServletRequest request, AutomatedTestCase automatedTestCase)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer projectID = automatedTestCaseService.checkGroupPermission(automatedTestCase.getGroupID(), userID);
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			automatedTestCase.setUserID(userID);
			automatedTestCase.setCaseType(0);
			boolean result = automatedTestCaseService.editTestCase(automatedTestCase);
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
	 * 获取用例列表
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTestCaseList", method = RequestMethod.POST)
	public Map<String, Object> getTestCaseList(HttpServletRequest request, Integer projectID, Integer groupID)
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
			List<Map<String, Object>> result = automatedTestCaseService.getTestCaseList(projectID, groupID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("caseList", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}

	/**
	 * 获取用例列表
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTestCaseInfo", method = RequestMethod.POST)
	public Map<String, Object> getTestCaseInfo(HttpServletRequest request, Integer projectID, Integer caseID)
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
			Map<String, Object> result = automatedTestCaseService.getTestCaseInfo(projectID, caseID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("caseInfo", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}

	/**
	 * 删除用例
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteTestCase", method = RequestMethod.POST)
	public Map<String, Object> deleteTestCase(HttpServletRequest request, Integer projectID, String caseID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = automatedTestCaseService.deleteTestCase(projectID, caseID, userID);
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
	 * 搜索用例
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/searchTestCase", method = RequestMethod.POST)
	public Map<String, Object> searchTestCase(HttpServletRequest request, String tips, Integer projectID)
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
			List<Map<String, Object>> result = automatedTestCaseService.searchTestCase(projectID, tips);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("caseList", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}

	/**
	 * 获取用例数据
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTestCaseDataList", method = RequestMethod.POST)
	public Map<String, Object> getTestCaseDataList(HttpServletRequest request, Integer projectID, Integer groupID)
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
			List<Map<String, Object>> result = automatedTestCaseService.getTestCaseDataList(projectID, groupID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("caseList", result);
			}
			else
			{
				map.put("statusCode", "860000");
			}
		}
		return map;
	}
}
