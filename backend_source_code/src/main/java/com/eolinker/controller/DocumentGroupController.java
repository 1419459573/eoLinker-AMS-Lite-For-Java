package com.eolinker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.pojo.DocumentGroup;
import com.eolinker.pojo.Partner;
import com.eolinker.service.DocumentGroupService;
import com.eolinker.service.ProjectService;
/**
 * 项目文档分组控制器
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
@RequestMapping("/DocumentGroup")
public class DocumentGroupController
{

	@Autowired
	private DocumentGroupService documentGroupService;

	@Autowired
	private ProjectService projectService;

	/**
	 * 添加文档分组
	 * 
	 * @param documentGroup
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addGroup")
	public Map<String, Object> addGroup(DocumentGroup documentGroup, HttpSession session)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (documentGroup.getProjectID() == null
					|| !String.valueOf(documentGroup.getProjectID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "220005");
			else if (documentGroup.getGroupName() == null || documentGroup.getGroupName().length() < 1
					|| documentGroup.getGroupName().length() > 32)
				map.put("statusCode", "220001");
			else if (documentGroup.getParentGroupID() != null
					&& !String.valueOf(documentGroup.getParentGroupID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "220002");
			else
			{
				int userID = (int) session.getAttribute("userID");
				Partner member = this.projectService.getProjectUserType(userID, documentGroup.getProjectID());
				if (member == null || member.getUserType() < 0 || member.getUserType() > 2)
					map.put("statusCode", "120007");
				else
				{
					int result = this.documentGroupService.addGroup(documentGroup);
					if (result > 0)
					{
						map.put("statusCode", "000000");
						map.put("groupID", result);
					}
					else
						map.put("statusCode", "220000");
				}
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
			return map;
		}
		return map;
	}

	/**
	 * 删除文档分组
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
				map.put("statusCode", "220003");
			else
			{
				int userType = this.documentGroupService.getUserType(Integer.parseInt(groupID));
				if (userType < 0 || userType > 2)
					map.put("statusCode", "120007");
				else
				{
					int result = this.documentGroupService.deleteGroup(Integer.parseInt(groupID));
					map.put("statusCode", (result > 0) ? "000000" : "220000");
				}
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
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
				map.put("statusCode", "220005");
			else
			{
				map = this.documentGroupService.getGroupList(Integer.parseInt(projectID));

				if (map == null || map.isEmpty())
				{
					map = new HashMap<String, Object>();
					map.put("statusCode", "220000");
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
			map.put("statusCode", "220005");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
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
	public Map<String, Object> editGroup(DocumentGroup documentGroup)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (documentGroup.getGroupID() == null || !String.valueOf(documentGroup.getGroupID()).matches("^[0-9]{1,11}$")
				|| (documentGroup.getParentGroupID() != null
						&& !String.valueOf(documentGroup.getParentGroupID()).matches("^[0-9]{1,11}$")))
			map.put("statusCode", "220003");
		else if (documentGroup.getGroupName() == null || documentGroup.getGroupName().length() < 1
				|| documentGroup.getGroupName().length() > 32)
			map.put("statusCode", "220001");
		else if (documentGroup.getGroupID() == documentGroup.getParentGroupID())
			map.put("statusCode", "220002");
		else
		{
			int userType = this.documentGroupService.getUserType(documentGroup.getGroupID());
			if (userType < 0 || userType > 2)
			{
				map.put("statusCode", "120007");
				return map;
			}

			int result = this.documentGroupService.editGroup(documentGroup);
			map.put("statusCode", (result > 0) ? "000000" : "220000");
		}

		return map;
	}

	/**
	 * 修改文档分组列表排序
	 * 
	 * @param projectID
	 * @param orderList
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sortDocumentGroup")
	public Map<String, Object> sortDocumentGroup(HttpSession session, @RequestParam("projectID") String projectID,
			@RequestParam("orderList") String orderList)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "220005");
			else if (orderList.length() <= 0)
				map.put("statusCode", "220004");
			else
			{
				int userID = (int) session.getAttribute("userID");

				Partner member = this.projectService.getProjectUserType(userID, Integer.parseInt(projectID));
				if (member == null || member.getUserType() < 0 || member.getUserType() > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}

				int result = this.documentGroupService.sortGroup(Integer.parseInt(projectID), orderList);
				map.put("statusCode", (result > 0) ? "000000" : "220000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220005");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
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
				map.put("statusCode", "220003");
			else
			{
				int userType = this.documentGroupService.getUserType(Integer.parseInt(groupID));
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
				}
				else
				{
					String fileName = this.documentGroupService.exportGroup(request, Integer.parseInt(groupID));
					if (fileName != null)
					{
						map.put("statusCode", "000000");
						map.put("fileName", fileName);
					}
					else
						map.put("statusCode", "220000");
				}
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
			return map;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220000");
			return map;
		}

		return map;
	}

	/**
	 * 导入分组
	 * 
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
				map.put("statusCode", "220007");
			else if (data.isEmpty())
				map.put("statusCode", "220005");
			else
			{
				Partner userType = this.projectService.getProjectUserType(userID, Integer.valueOf(projectID));
				if (userType == null || userType.getUserType() < 0 || userType.getUserType() > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				int result = this.documentGroupService.importGroup(Integer.parseInt(projectID), data);
				map.put("statusCode", (result > 0) ? "000000" : "220000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "220007");
			return map;
		}
		return map;
	}

}
