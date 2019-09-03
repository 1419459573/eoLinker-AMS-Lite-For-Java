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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.Partner;
import com.eolinker.service.ProjectService;
/**
 * 项目控制器
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
@RequestMapping("/Project")
public class ProjectController
{
	@Resource
	private ProjectService projectService;

	/**
	 * 新建项目
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addProject", method = RequestMethod.POST)
	public Map<String, Object> addProject(HttpServletRequest request, Project project)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (project.getProjectName() == null || project.getProjectName().length() < 1 || project.getProjectName().length() > 32)
		{
			map.put("statusCode", "130001");
		}
		else if (project.getProjectType() == null ||project.getProjectType() < 0 || project.getProjectType() > 3)
		{
			map.put("statusCode", "130002");
		}
		else
		{
			HttpSession session =  request.getSession(true);
			Map<String, Object> projectInfo = projectService.addProject(project, session);
			if (projectInfo != null)
			{
				map.put("statusCode", "000000");
				map.put("projectInfo", projectInfo);
			}
			else
			{
				map.put("status", "130000");
			}
		}
		return map;
	}

	/**
	 * 删除项目
	 * @param request
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public Map<String, Object> deleteProject(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() != 0)
		{
			map.put("status", "100002");
		}
		else
		{
			int result = projectService.deleteProject(projectID);
			if (result > 0)
				map.put("statusCode", "000000");
			else
				map.put("statusCode", "130000");
		}
		return map;
	}

	/**
	 * 获取项目列表
	 * @param request
	 * @param projectType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
	public Map<String, Object> getProjectList(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "-1") Integer projectType)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session =  request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		List<Map<String, Object>> result = projectService.getProjectList(userID, projectType);
		if (result != null)
		{
			map.put("statusCode", "000000");
			map.put("projectList", result);
		}
		else
			map.put("statusCode", "130000");
		return map;
	}
	
	/**
	 * 修改项目
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editProject", method = RequestMethod.POST)
	public Map<String, Object> editProject(HttpServletRequest request, Project project)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(project.getProjectID() == null || project.getProjectID() < 1)
		{
			map.put("statusCode", "130003");
		}
		if (project.getProjectName() == null || project.getProjectName().length() < 1 || project.getProjectName().length() > 32)
		{
			map.put("statusCode", "130001");
		}
		else if (project.getProjectType() == null ||project.getProjectType() < 0 || project.getProjectType() > 3)
		{
			map.put("statusCode", "130002");
		}
		else
		{
			HttpSession session =  request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, project.getProjectID());
			if (partner == null || partner.getUserType() <0 || partner.getUserType() > 1)
			{
				map.put("status", "100002");
			}
			boolean result = projectService.editProject(project);
			if (result)
			{
				map.put("statusCode", "000000");

			}
			else
			{
				map.put("status", "130000");
			}
		}
		return map;
	}
	
	/**
	 * 获取项目信息
	 * @param request
	 * @param projectType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getProject", method = RequestMethod.POST)
	public Map<String, Object> getProject(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(projectID < 1)
		{
			map.put("statusCode", "130003");
		}
		else
		{
			HttpSession session =  request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null)
			{
				map.put("status", "100002");
			}
			else
			{
				Map<String, Object> result = projectService.getProject(userID, projectID);
				if (result != null)
				{
					result.put("importURL",  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/AutoGenerate/importApi");
					result.put("statusCode", "000000");
					return result;
				}
				else
					map.put("statusCode", "130000");
			}
		}
		return map;
	}
	
	/**
	 * 获取项目列表
	 * @param request
	 * @param projectType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getProjectLogList", method = RequestMethod.POST)
	public Map<String, Object> getProjectLogList(HttpServletRequest request, Integer projectID, @RequestParam(value="page",required=false,defaultValue="1")Integer page, @RequestParam(value="pageSize",required=false,defaultValue="15")Integer pageSize)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(projectID < 1)
		{
			map.put("statusCode", "130003");
		}
		else
		{
			HttpSession session =  request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null)
			{
				map.put("status", "100002");
			}
			else
			{
				List<Map<String, Object>> result = projectService.getProjectLogList(projectID, page, pageSize);
				int logCount = projectService.getProjectLogCount(projectID, 7);
				if (result != null && !result.isEmpty())
				{
				    map.put("statusCode", "000000");
				    map.put("logCount", logCount);
					map.put("logList", result);
				}
				else
					map.put("statusCode", "130000");
			}
		}
		return map;
	}
	
	/**
	 * 获取接口数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApiNum", method = RequestMethod.POST)
	public String getApiNum(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(projectID < 1)
		{
			map.put("statusCode", "130003");
		}
		else
		{
			Integer apiNum = projectService.getApiNum(projectID);
			map.put("num", apiNum);
			map.put("statusCode", "000000");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 导出项目数据为json
	 * 
	 * @param request
	 * @param projectType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dumpProject", method = RequestMethod.POST)
	public Map<String, Object> exportProjectData(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (projectID == null || projectID < 1)
		{
			map.put("statusCode", "140001");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
			{
				map.put("status", "100002");
			}
			else
			{
				Map<String, Object> result = projectService.exportProjectData(projectID, userID);
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
						String fileName = "/eoLinker_dump_" + session.getAttribute("userName") + "_"
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
						map.put("statusCode", "140000");
					}
				}
				else
				{
					map.put("statusCode", "140000");
				}
			}
		}
		return map;
	}
}
