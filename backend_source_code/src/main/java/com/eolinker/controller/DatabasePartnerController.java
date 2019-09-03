package com.eolinker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.dto.PartnerListDTO;
import com.eolinker.pojo.ConnDatabase;
import com.eolinker.pojo.User;
import com.eolinker.service.DatabasePartnerService;
import com.eolinker.service.DatabaseService;
import com.eolinker.service.UserService;
/**
 * 数据库协作管理控制器
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
@RequestMapping("/DatabasePartner")
public class DatabasePartnerController
{

	@Autowired
	private DatabasePartnerService databasePartnerService;

	@Autowired
	private DatabaseService databaseService;

	@Autowired
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
	public Map<String, Object> getPartnerInfo(@RequestParam("userName") String userName, @RequestParam("dbID") int dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		if (userName == null || !userName.matches("^([a-zA-Z][0-9a-zA-Z_]{3,59})$"))
			map.put("statusCode", "250001");
		else
		{
			User user = userService.getUserByUserName(userName);
			if (user != null)
			{
				int result = this.databasePartnerService.checkIsInvited(dbID, userName);
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
	public Map<String, Object> invitePartner(@RequestParam("userName") String userName, @RequestParam("dbID") int dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		ConnDatabase userType = databaseService.getUserType(dbID);
		if (userType == null || userType.getUserType() < 0 || userType.getUserType() > 1)
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
				int result = this.databasePartnerService.checkIsInvited(dbID, userName);
				if (result > 0)
					map.put("statusCode", "250007");
				else
				{
					result = this.databasePartnerService.invitePartner(dbID, user.getUserID());
					if (result > 0)
					{
						map.put("statusCode", "000000");
						map.put("connID", result);
					}
					else
						map.put("statusCode", "250003");
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
	public Map<String, Object> removePartner(@RequestParam("dbID") int dbID, @RequestParam("connID") int connID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		ConnDatabase userType = databaseService.getUserType(dbID);
		if (userType == null || userType.getUserType() < 0 || userType.getUserType() > 1)
		{
			map.put("statusCode", "120007");
			return map;
		}

		int result = this.databasePartnerService.removePartner(dbID, connID);
		if (result > 0)
			map.put("statusCode", "000000");
		else
			map.put("statusCode", "250004");

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
	public Map<String, Object> getPartnerList(@RequestParam("dbID") int dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		List<PartnerListDTO> partnerList = this.databasePartnerService.getPartnerList(dbID);
		if (partnerList != null)
		{
			map.put("statusCode", "000000");
			map.put("partnerList", partnerList);
		}
		else
			map.put("statusCode", "250005");

		return map;
	}

	/**
	 * 退出协作数据字典
	 * 
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/quitPartner")
	public Map<String, Object> quitPartner(@RequestParam("dbID") int dbID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		int result = this.databasePartnerService.quitPartner(dbID);
		if (result > 0)
			map.put("statusCode", "000000");
		else
			map.put("statusCode", "250006");
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
	public Map<String, Object> editPartnerNickName(@RequestParam("dbID") String dbID,
			@RequestParam("connID") String connID, @RequestParam("nickName") String nickName)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (dbID == null || connID == null || !dbID.matches("^[0-9]{1,11}$") || !connID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "250003");
			else if (nickName == null || nickName.length() < 1 || nickName.length() > 32)
				map.put("statusCode", "250004");
			else
			{
				int result = this.databasePartnerService.editPartnerNickName(Integer.valueOf(dbID),
						Integer.valueOf(connID), nickName);
				map.put("statusCode", (result > 0) ? "000000" : "250000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "250003");
			return map;
		}
		return map;
	}

	/**
	 * 修改协作成员的类型
	 * 
	 * @param dbID
	 * @param connID
	 * @param userType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPartnerType")
	public Map<String, Object> editPartnerType(@RequestParam("dbID") String dbID, @RequestParam("connID") String connID,
			@RequestParam("userType") String userType)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (dbID == null || connID == null || !dbID.matches("^[0-9]{1,11}$") || !connID.matches("^[0-9]{1,11}$"))
			{
				map.put("statusCode", "250003"); // 关联ID格式非法
				return map;
			}
			else if (userType == null || !userType.matches("^[1-3]{1}$"))
			{
				map.put("statusCode", "250005"); // 用户类型格式非法
				return map;
			}

			ConnDatabase type = this.databaseService.getUserType(Integer.parseInt(dbID));
			if (type == null || type.getUserType() < 0 || type.getUserType() > 1)
				map.put("statusCode", "120007");
			else
			{
				int result = this.databasePartnerService.editPartnerType(Integer.parseInt(dbID),
						Integer.parseInt(connID), Integer.parseInt(userType));
				map.put("statusCode", (result > 0) ? "000000" : "250000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "250003");
			return map;
		}

		return map;
	}

}
