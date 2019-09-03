package com.eolinker.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eolinker.pojo.Partner;
import com.eolinker.service.BackupService;
import com.eolinker.service.ProjectService;
/**
 * 备份项目控制器
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
@RequestMapping("Backup")
public class BackupController
{
	@Resource
	private ProjectService projectService;
	@Resource
	private BackupService backupService;

	/**
	 * 备份项目数据到eolinker官网
	 * 
	 * @param userName
	 * @param dbID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/backupProject")
	public Map<String, Object> backupProject(HttpServletRequest request, String userCall, String userPassword,
			Integer projectID, String verifyCode)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (userCall == null)
		{
			map.put("statusCode", "310001");
		}
		else if (userPassword == null || !userPassword.matches("^[0-9a-zA-Z]{32}$"))
		{
			map.put("statusCode", "310002");
		}
		else if (projectID == null)
		{
			map.put("statusCode", "310003");
		}
		else if (verifyCode == null || !verifyCode.matches("^[0-9a-zA-Z]{32}$"))
		{
			map.put("statusCode", "310004");
		}
		else
		{
			HttpSession session = request.getSession(true);
			Integer userID = (Integer) session.getAttribute("userID");
			Partner partner = projectService.getProjectUserType(userID, projectID);
			if (partner == null || partner.getUserType() < 0 || partner.getUserType() > 1)
			{
				map.put("statusCode", "120007");
			}
			else
			{
				Map<String, Object> data = projectService.exportProjectData(projectID, userID);
				int result = backupService.backupProject(userID, userCall, userPassword, projectID, verifyCode, data);
				switch (result)
				{
					case 0:
						map.put("statusCode", "000000");
						break;
					case -1:
						map.put("msg", "用户没有写权限");
						map.put("statusCode", "310005");
						break;
					case -2:
						map.put("msg", "发送登录请求失败");
						map.put("statusCode", "310006");
						break;
					case -3:
						map.put("msg", "登录账号非法");
						map.put("statusCode", "310007");
						break;
					case -4:
						map.put("msg", "账号不存在或密码错误");
						map.put("statusCode", "310008");
						break;
					case -5:
						map.put("msg", "未知登录错误");
						map.put("statusCode", "310009");
						break;
					case -6:
						map.put("msg", "备份项目失败");
						map.put("statusCode", "310010");
						break;
					default:
						map.put("msg", "备份项目失败");
						map.put("statusCode", "310000");
						break;
				}
			}
		}
		return map;
	}
}
