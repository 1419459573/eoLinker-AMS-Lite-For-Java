package com.eolinker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eolinker.config.Config;
/**
 * 网站配置控制器
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
@RequestMapping("Setting")
public class SettingController
{
	@Resource
	private Config config;

	/**
	 * 获取网站语言配置
	 * 
	 * @param request
	 * @param project
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getLanguage", method = RequestMethod.POST)
	public Map<String, Object> getLanguage(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (config.getLanguage() != null && !config.getLanguage().equals(""))
		{
			map.put("language", config.getLanguage());
		}
		else
		{
			map.put("language", "zh-cn");
		}
		map.put("statusCode", "000000");
		return map;
	}
	
	/**
	 * 获取网站语言配置
	 * 
	 * @param request
	 * @param project
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getRegitsterSetting", method = RequestMethod.POST)
	public Map<String, Object> getRegitsterSetting(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(config.getAllowRegister() != null && config.getAllowRegister().equals("true"))
		{
			map.put("allowRegister", 1);
		}
		else
		{
			map.put("allowRegister", 0);
		}
		map.put("statusCode", "000000");
		return map;
	}
}
