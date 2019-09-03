package com.eolinker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.User;
import com.eolinker.service.PartnerService;
import com.eolinker.service.ProjectService;
import com.eolinker.service.UserService;
/**
 * 项目协作管理控制器
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
@RequestMapping("/Partner")
public class PartnerController
{
	@Resource
	private PartnerService partnerService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserService userService;

	/**
	 * 获取人员信息
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPartnerInfo")
	public Map<String, Object> getPartnerInfo(HttpServletRequest request, String userName, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (userName == null || !userName.matches("^([a-zA-Z][0-9a-zA-Z_]{3,59})$"))
			map.put("statusCode", "250001");
		else
		{
			User user = userService.getUserByUserName(userName);
			if (user != null)
			{
				int result = partnerService.checkIsInvited(projectID, userName);
				Map<String, Object> userInfo = new HashMap<String, Object>();
				userInfo.put("userName", userName);
				userInfo.put("userNickName", user.getUserNickName());
				if (result > 0)
				{
					userInfo.put("isInvited", 1);
					map.put("statusCode", "250007");
				}
				else
				{
					userInfo.put("isInvited", 0);
					map.put("statusCode", "000000");
				}
				map.put("userInfo", userInfo);
			}
			else
				map.put("statusCode", "250002");
		}
		return map;
	}

	/**
	 * 邀请协作人员
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/invitePartner")
	public Map<String, Object> invitePartner(HttpServletRequest request, String userName, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner partner = projectService.getProjectUserType(userID, projectID);
		if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 1)
		{
			map.put("statusCode", "120007");
			return map;
		}
		if (userName == null || !userName.matches("^([a-zA-Z][0-9a-zA-Z_]{3,59})$"))
			map.put("statusCode", "250001");
		else
		{
			User user = userService.getUserByUserName(userName);
			if (user != null)
			{
				int result = partnerService.checkIsInvited(projectID, userName);
				if (result > 0)
					map.put("statusCode", "250007");
				else
				{
					result = partnerService.invitePartner(projectID, userID, user.getUserID(), user.getUserName());
					if (result > 0)
					{
						map.put("statusCode", "000000");
						map.put("connID", result);
					}
					else
						map.put("statusCode", "250000");
				}
			}
			else
				map.put("statusCode", "250002");
		}
		return map;
	}

	/**
	 * 移除协作人员
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/removePartner")
	public Map<String, Object> removePartner(HttpServletRequest request, Integer projectID, Integer connID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner Partner = projectService.getProjectUserType(userID, projectID);
		if (Partner == null || Partner.getUserType() < 0 || Partner.getUserType() > 1)
		{
			map.put("statusCode", "120007");
			return map;
		}
		boolean result = partnerService.removePartner(projectID, connID, userID);
		if (result)
			map.put("statusCode", "000000");
		else
			map.put("statusCode", "250000");

		return map;
	}

	/**
	 * 获取协作人员列表
	 * 
	 * @param dbID
	 * @param connID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPartnerList")
	public Map<String, Object> getPartnerList(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner Partner = projectService.getProjectUserType(userID, projectID);
		if (Partner == null || Partner.getUserType() < 0 || Partner.getUserType() > 3)
		{
			map.put("statusCode", "120007");
			return map;
		}
		List<Map<String, Object>> partnerList = partnerService.getPartnerList(projectID, userID);
		if (partnerList != null)
		{
			map.put("statusCode", "000000");
			map.put("partnerList", partnerList);
		}
		else
			map.put("statusCode", "250000");

		return map;
	}

	/**
	 * 退出协作项目
	 * 
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/quitPartner")
	public Map<String, Object> quitPartner(HttpServletRequest request, Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		String userName = (String) session.getAttribute("userName");
		Partner Partner = projectService.getProjectUserType(userID, projectID);
		if (Partner == null || Partner.getUserType() < 0 || Partner.getUserType() > 3)
		{
			map.put("statusCode", "120007");
			return map;
		}
		boolean result = partnerService.quitPartner(projectID, userID, userName);
		if (result)
			map.put("statusCode", "000000");
		else
			map.put("statusCode", "250000");
		return map;
	}

	/**
	 * 修改协作成员的昵称
	 * 
	 * @param dbID
	 * @param connID
	 * @param nickName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPartnerNickName")
	public Map<String, Object> editPartnerNickName(HttpServletRequest request, Integer projectID, Integer connID,
			String nickName)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner Partner = projectService.getProjectUserType(userID, projectID);
		if (Partner == null || Partner.getUserType() < 0 || Partner.getUserType() > 3)
		{
			map.put("statusCode", "120007");
		}
		else if (nickName == null || nickName.length() < 1 || nickName.length() > 32)
		{
			map.put("statusCode", "250004");
		}
		else
		{
			boolean result = partnerService.editPartnerNickName(projectID, connID, userID, nickName);
			if (result)
				map.put("statusCode", "000000");
			else
				map.put("statusCode", "250000");
		}

		return map;
	}

	/**
	 * 修改协作成员的权限
	 * 
	 * @param dbID
	 * @param connID
	 * @param nickName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPartnerType")
	public Map<String, Object> editPartnerType(HttpServletRequest request, Integer projectID, Integer connID,
			Integer userType)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Partner Partner = projectService.getProjectUserType(userID, projectID);
		if (Partner == null || Partner.getUserType() < 0 || Partner.getUserType() > 1)
		{
			map.put("statusCode", "120007");
		}
		else
		{
			boolean result = partnerService.editPartnerType(projectID, connID, userID, userType);
			if (result)
				map.put("statusCode", "000000");
			else
				map.put("statusCode", "250000");
		}

		return map;
	}

}
