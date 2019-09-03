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

import com.alibaba.fastjson.JSONArray;
import com.eolinker.pojo.Api;
import com.eolinker.pojo.Partner;
import com.eolinker.service.*;

/**
 * 接口控制器
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
@RequestMapping("/Api")
public class ApiController
{
	@Resource
	private ApiService apiService;
	@Resource
	private ProjectService projectService;
	@Resource
	private ApiGroupService apiGroupService;

	/**
	 * 添加接口
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addApi", method = RequestMethod.POST)
	public Map<String, Object> addApi(HttpServletRequest request, Api api,
			@RequestParam(value = "apiHeader", required = false) String apiHeader,
			@RequestParam(value = "apiRequestParam", required = false) String apiRequestParam,
			@RequestParam(value = "apiResultParam", required = false) String apiResultParam)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer projectID = apiGroupService.checkGroupPermission(api.getGroupID(), userID);
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			api.setUpdateUserID(userID);
			api.setProjectID(projectID);
			Integer apiID = apiService.addApi(api, apiHeader, apiRequestParam, apiResultParam);
			if (apiID > 0)
			{
				map.put("statusCode", "000000");
				map.put("apiID", apiID);
				map.put("groupID", api.getGroupID());
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 修改接口
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editApi", method = RequestMethod.POST)
	public Map<String, Object> editApi(HttpServletRequest request, Api api,
			@RequestParam(value = "apiHeader", required = false) String apiHeader,
			@RequestParam(value = "apiRequestParam", required = false) String apiRequestParam,
			@RequestParam(value = "apiResultParam", required = false) String apiResultParam,
			@RequestParam(value = "updateDesc", required = false) String updateDesc)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer projectID = apiGroupService.checkGroupPermission(api.getGroupID(), userID);
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			api.setUpdateUserID(userID);
			api.setProjectID(projectID);
			boolean result = apiService.editApi(api, apiHeader, apiRequestParam, apiResultParam, updateDesc);
			if (result)
			{
				map.put("statusCode", "000000");
				map.put("apiID", api.getApiID());
				map.put("groupID", api.getGroupID());
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 批量删除api,将其移入回收站
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/removeApi", method = RequestMethod.POST)
	public Map<String, Object> removeApi(HttpServletRequest request, String apiID, Integer projectID)
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
			boolean result = apiService.removeApi(projectID, apiID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 批量恢复接口
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/recoverApi", method = RequestMethod.POST)
	public Map<String, Object> recoverApi(HttpServletRequest request, String apiID, Integer groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

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
			boolean result = apiService.recoverApi(projectID, groupID, apiID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 切断删除接口
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteApi", method = RequestMethod.POST)
	public Map<String, Object> deleteApi(HttpServletRequest request, String apiID, Integer projectID)
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
			boolean result = apiService.deleteApi(projectID, apiID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 清空回收站
	 * 
	 * @param request
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cleanRecyclingStation", method = RequestMethod.POST)
	public Map<String, Object> cleanRecyclingStation(HttpServletRequest request, Integer projectID)
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
			boolean result = apiService.cleanRecyclingStation(projectID, userID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取回收站接口列表
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRecyclingStationApiList", method = RequestMethod.POST)
	public Map<String, Object> getRecyclingStationApiList(HttpServletRequest request, Integer projectID,
			Integer orderBy, Integer asc)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = apiService.getRecyclingStationApiList(projectID, orderBy, asc);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiList", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取接口详情
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApi", method = RequestMethod.POST)
	public Map<String, Object> getApi(HttpServletRequest request, Integer apiID, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Map<String, Object> result = apiService.getApi(projectID, apiID, request);
			System.out.print(result);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiInfo", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取分组接口列表
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApiList", method = RequestMethod.POST)
	public Map<String, Object> getApiList(HttpServletRequest request, Integer projectID, Integer groupID,
			Integer orderBy, Integer asc)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = apiService.getApiList(projectID, groupID, orderBy, asc);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiList", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取项目接口列表
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllApiList", method = RequestMethod.POST)
	public Map<String, Object> getAllApiList(HttpServletRequest request, Integer projectID, Integer orderBy,
			Integer asc)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = apiService.getApiList(projectID, 0, orderBy, asc);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiList", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取接口详情
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/searchApi", method = RequestMethod.POST)
	public Map<String, Object> searchApi(HttpServletRequest request, Integer projectID, String tips)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = apiService.searchApi(projectID, tips);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiList", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 修改接口星标状态
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStar", method = RequestMethod.POST)
	public Map<String, Object> updateStar(HttpServletRequest request, Integer projectID, Integer apiID, Integer starred)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = apiService.updateStar(projectID, apiID, userID, starred);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 添加接口星标
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addStar", method = RequestMethod.POST)
	public Map<String, Object> addStar(HttpServletRequest request, Integer projectID, Integer apiID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Integer starred = 1;
			boolean result = apiService.updateStar(projectID, apiID, userID, starred);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 去除接口星标
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/removeStar", method = RequestMethod.POST)
	public Map<String, Object> removeStar(HttpServletRequest request, Integer projectID, Integer apiID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Integer starred = 0;
			boolean result = apiService.updateStar(projectID, apiID, userID, starred);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取接口mock数据
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApiMockData", method = RequestMethod.POST)
	public Map<String, Object> getApiMockData(HttpServletRequest request, Integer projectID, Integer apiID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			Map<String, Object> result = apiService.getApiMockData(projectID, apiID, request);
			if (result != null && !result.isEmpty())
			{
				result.put("statusCode", "000000");
				return result;
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 修改接口mock数据
	 * 
	 * @param request
	 * @param apiID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editApiMockData", method = RequestMethod.POST)
	public Map<String, Object> editApiMockData(HttpServletRequest request, Integer projectID, Integer apiID,
			String mockRule, String mockResult, String mockConfig)
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
			boolean result = apiService.editApiMockData(projectID, apiID, mockRule, mockResult, mockConfig);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 获取接口版本历史列表
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApiHistoryList", method = RequestMethod.POST)
	public Map<String, Object> getApiHistoryList(HttpServletRequest request, Integer projectID, Integer apiID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			List<Map<String, Object>> result = apiService.getApiHistoryList(projectID, apiID);
			if (result != null && !result.isEmpty())
			{
				map.put("statusCode", "000000");
				map.put("apiHistoryList", result);
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 删除接口历史版本
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteApiHistory", method = RequestMethod.POST)
	public Map<String, Object> deleteApiHistory(HttpServletRequest request, Integer projectID, Integer apiID,
			Integer apiHistoryID)
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
			boolean result = apiService.deleteApiHistory(projectID, apiID, userID, apiHistoryID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 切换接口历史版本
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toggleApiHistory", method = RequestMethod.POST)
	public Map<String, Object> toggleApiHistory(HttpServletRequest request, Integer projectID, Integer apiID,
			Integer apiHistoryID)
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
			boolean result = apiService.toggleApiHistory(projectID, apiID, userID, apiHistoryID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 修改接口分组
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeApiGroup", method = RequestMethod.POST)
	public Map<String, Object> changeApiGroup(HttpServletRequest request, Integer projectID, String apiID,
			Integer groupID)
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
			boolean result = apiService.changeApiGroup(projectID, apiID, userID, groupID);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}

	/**
	 * 导出接口
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportApi", method = RequestMethod.POST)
	public Map<String, Object> exportApi(HttpServletRequest request, Integer projectID, String apiID)
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
			List<Map<String, Object>> result = apiService.exportApi(projectID, apiID, userID);
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
					JSONArray json = (JSONArray) JSONArray.toJSON(result);
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
		return map;
	}

	/**
	 * 导入接口
	 * 
	 * @param request
	 * @param projectID
	 * @param orderBy
	 * @param asc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importApi", method = RequestMethod.POST)
	public Map<String, Object> importApi(HttpServletRequest request, Integer projectID, String data, Integer groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (data == null || data == "")
		{
			map.put("statusCode", "160006");
		}
		else if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			boolean result = apiService.importApi(projectID, groupID, userID, data);
			if (result)
			{
				map.put("statusCode", "000000");
			}
			else
			{
				map.put("statusCode", "160000");
			}
		}
		return map;
	}
}
