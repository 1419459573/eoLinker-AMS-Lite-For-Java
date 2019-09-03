package com.eolinker.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eolinker.service.MockService;
/**
 * mock请求控制器
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
@RequestMapping("/Mock")
public class MockController
{
	@Resource
	private MockService mockService;

	/**
	 * 简易mock请求
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/simple", produces = { "text/html; charset=UTF-8" })
	public String simple(HttpServletRequest request, HttpServletResponse response, Integer projectID, String resultType,
			String uri)
	{
		response.setHeader("Access-Control-Allow-Headers",
				"x-requested-with,content-type,x-custom-header,Accept,Authorization,other_header,x-csrf-token");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,PATCH,OPTIONS");
		response.setHeader("Access-Control-Allow-Origin", "*");
		resultType = resultType != null ? resultType : "success";
		Integer requstType = 5;
		switch (request.getMethod())
		{
			case "POST":
				requstType = 0;
				break;
			case "GET":
				requstType = 1;
				break;
			case "PUT":
				requstType = 2;
				break;
			case "DELETE":
				requstType = 3;
				break;
			case "HEAD":
				requstType = 4;
				break;
			case "PATCH":
				requstType = 6;
				break;
			default:
				requstType = 5;
				break;
		}
		if (!resultType.equals("success") && !resultType.equals("failure"))
		{
			return "error result type.";
		}
		else
		{
			String result = mockService.simple(projectID, uri, requstType, resultType);
			if (result != null && !result.equals("") && result.length() > 0)
			{
				return result;
			}
			else
			{
				return "sorry,this api without the mock data.";
			}
		}
	}

	/**
	 * 高级mock请求
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mock", produces = { "application/json; charset=UTF-8" })
	public String mock(HttpServletRequest request, HttpServletResponse response, Integer projectID, String uri)
	{
		response.setHeader("Access-Control-Allow-Headers",
				"x-requested-with,content-type,x-custom-header,Accept,Authorization,other_header,x-csrf-token");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,PATCH,OPTIONS");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Integer requstType = 5;
		switch (request.getMethod())
		{
			case "POST":
				requstType = 0;
				break;
			case "GET":
				requstType = 1;
				break;
			case "PUT":
				requstType = 2;
				break;
			case "DELETE":
				requstType = 3;
				break;
			case "HEAD":
				requstType = 4;
				break;
			case "PATCH":
				requstType = 6;
				break;
			default:
				requstType = 5;
				break;
		}
		String result = mockService.getMockResult(projectID, uri, requstType);
		if (result != null && !result.equals("") && result.length() > 0)
		{
			return result;
		}
		else
		{
			return "sorry,this api without the mock data.";
		}
	}
}
