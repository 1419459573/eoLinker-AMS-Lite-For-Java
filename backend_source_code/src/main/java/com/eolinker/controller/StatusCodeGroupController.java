package com.eolinker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.pojo.Partner;
import com.eolinker.pojo.StatusCodeGroup;
import com.eolinker.service.ProjectService;
import com.eolinker.service.StatusCodeGroupService;
/**
 * 状态码分组控制器
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
@RequestMapping("/StatusCodeGroup")
public class StatusCodeGroupController
{

	@Autowired
	private StatusCodeGroupService statusCodeGroupService;

	@Autowired
	private ProjectService projectService;

	/**
	 * 添加分组
	 * 
	 * @param groupName
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addGroup")
	public Map<String, Object> addGroup(StatusCodeGroup statusCodeGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (statusCodeGroup.getProjectID() == null
				|| !String.valueOf(statusCodeGroup.getProjectID()).matches("^[0-9]{1,11}$"))
			map.put("statusCode", "180005");
		else if (statusCodeGroup.getGroupName() == null || statusCodeGroup.getGroupName().length() < 1
				|| statusCodeGroup.getGroupName().length() > 32)
			map.put("statusCode", "180004");
		else
		{
			int result = this.statusCodeGroupService.addGroup(statusCodeGroup);

			if (result > 0)
			{
				map.put("statusCode", "000000");
				map.put("groupID", result);
			}
			else
				map.put("statusCode", "180002");
		}
		return map;
	}

	/**
	 * 删除分组
	 * 
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteGroup")
	public Map<String, Object> deleteGroup(@RequestParam("groupID") String groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!groupID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "180003");
			else
			{
				int userType = this.statusCodeGroupService.getUserType(Integer.parseInt(groupID));
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				int result = this.statusCodeGroupService.deleteGroup(Integer.parseInt(groupID));
				map.put("statusCode", (result > 0) ? "000000" : "180006");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180006");
			return map;
		}

		return map;
	}

	/**
	 * 获取分组列表
	 * 
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getGroupList")
	public Map<String, Object> getGroupList(@RequestParam("projectID") String projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "180003");
			else
			{
				map = this.statusCodeGroupService.getGroupList(Integer.parseInt(projectID));

				if (map == null || map.isEmpty())
				{
					map = new HashMap<String, Object>();
					map.put("statusCode", "180001");
				}
				else
				{
					map.put("statusCode", "000000");
				}
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180001");
			return map;
		}

		return map;
	}

	/**
	 * 修改分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editGroup")
	public Map<String, Object> editGroup(StatusCodeGroup statusCodeGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (statusCodeGroup.getGroupID() == null
				|| !String.valueOf(statusCodeGroup.getGroupID()).matches("^[0-9]{1,11}$")
				|| (statusCodeGroup.getParentGroupID() != null
						&& !String.valueOf(statusCodeGroup.getParentGroupID()).matches("^[0-9]{1,11}$")))
			map.put("statusCode", "180003");
		else if (statusCodeGroup.getGroupName() == null || statusCodeGroup.getGroupName().length() < 1
				|| statusCodeGroup.getGroupName().length() > 32)
			map.put("statusCode", "180004");
		else if (statusCodeGroup.getGroupID() == statusCodeGroup.getParentGroupID())
			map.put("statusCode", "180009");
		else
		{
			int userType = this.statusCodeGroupService.getUserType(statusCodeGroup.getGroupID());
			if (userType < 0 || userType > 2)
			{
				map.put("statusCode", "120007");
				return map;
			}

			int result = this.statusCodeGroupService.editGroup(statusCodeGroup);
			map.put("statusCode", (result > 0) ? "000000" : "180007");
		}

		return map;
	}

	/**
	 * 修改状态码分组列表排序
	 * 
	 * @param projectID
	 * @param orderList
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sortGroup")
	public Map<String, Object> sortGroup(HttpSession session, @RequestParam("projectID") String projectID,
			@RequestParam("orderNumber") String orderNumber)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "180005");
			else if (orderNumber.length() <= 0)
				map.put("statusCode", "180008");
			else
			{
				int userID = (int) session.getAttribute("userID");

				Partner member = this.projectService.getProjectUserType(userID, Integer.parseInt(projectID));
				if (member == null || member.getUserType() < 0 || member.getUserType() > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				int result = this.statusCodeGroupService.sortGroup(Integer.parseInt(projectID), orderNumber);
				map.put("statusCode", (result > 0) ? "000000" : "180000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180005");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180000");
			return map;
		}

		return map;
	}

	/**
	 * 导出分组
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportGroup")
	public Map<String, Object> exportGroup(HttpServletRequest request, @RequestParam("groupID") String groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!groupID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "180003");
			else
			{
				int userType = this.statusCodeGroupService.getUserType(Integer.parseInt(groupID));
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
				}
				else
				{
					String fileName = this.statusCodeGroupService.exportGroup(request, Integer.parseInt(groupID));
					if (fileName != null)
					{
						map.put("statusCode", "000000");
						map.put("fileName", fileName);
					}
					else
						map.put("statusCode", "180000");
				}
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180000");
			return map;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180000");
			return map;
		}

		return map;
	}

	/**
	 * 导入分组
	 * 
	 * @param session
	 * @param projectID
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importGroup")
	public Map<String, Object> importGroup(HttpSession session, @RequestParam("projectID") String projectID,
			@RequestParam("data") String data)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		int userID = (int) session.getAttribute("userID");
		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "180007");
			else if (data.isEmpty())
				map.put("statusCode", "180005");
			else
			{
				Partner userType = this.projectService.getProjectUserType(userID, Integer.valueOf(projectID));
				if (userType == null || userType.getUserType() < 0 || userType.getUserType() > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				int result = this.statusCodeGroupService.importGroup(Integer.parseInt(projectID), data);
				map.put("statusCode", (result > 0) ? "000000" : "180000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "180007");
			return map;
		}
		return map;
	}

}
