package com.eolinker.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eolinker.mapper.MockMapper;
import com.eolinker.service.MockService;

/**
 * mock[业务处理层]
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
@Service
public class MockServiceImpl implements MockService
{
	@Autowired
	private MockMapper mockMapper;

	/**
	 * 获取简易mock
	 */
	@Override
	public String simple(Integer projectID, String uri, Integer requstType, String resultType)
	{
		// TODO Auto-generated method stub
		String result = "";
		if (resultType.equals("success"))
		{
			result = mockMapper.getSuccessResult(projectID, uri, requstType);
			if (result == null || result.equals("") || result.length() <= 0)
			{
				Map<String, Object> data = getRestfulMock(projectID, uri, requstType);
				if (data != null && !data.isEmpty())
				{
					result = (String) data.get("apiSuccessMock");
				}
			}
		}
		else
		{
			result = mockMapper.getFailureResult(projectID, uri, requstType);
			if (result == null || result.equals("") || result.length() <= 0)
			{
				Map<String, Object> data = getRestfulMock(projectID, uri, requstType);
				if (data != null && !data.isEmpty())
				{
					result = (String) data.get("apiFailureMock");
				}
			}
		}
		return result;
	}

	/**
	 * 获取restful数据
	 * @param projectID
	 * @param uri
	 * @param requstType
	 * @return
	 */
	private Map<String, Object> getRestfulMock(Integer projectID, String uri, Integer requstType)
	{
		List<Map<String, Object>> data = mockMapper.getRestfulMock(projectID, requstType);
		if (data != null && !data.isEmpty())
		{
			for (Map<String, Object> api : data)
			{
				String str = api.get("apiURI").toString().replaceAll("\\{[^\\/]+\\}", "[^/]+");
				str = str.replace("amp;", "");
				str = str.replaceAll("/:[^\\/]+/", "[^/]+");
				str = str.replaceAll("//", "\\/");
				str = str.replace("/\\?/", "\\?");
				str = "^" + str + "$";
				uri = uri.replace("amp;", "");
				Pattern r = Pattern.compile(str);
				Matcher m = r.matcher(uri);
				if (m.find())
				{
					return api;
				}
			}
		}
		return null;
	}

	/**
	 * 获取mock结果
	 */
	@Override
	public String getMockResult(Integer projectID, String uri, Integer requstType)
	{
		// TODO Auto-generated method stub
		String result = mockMapper.getMockResult(projectID, uri, requstType);
		if (result == null || result.equals("") || result.length() <= 0)
		{
			Map<String, Object> data = getRestfulMock(projectID, uri, requstType);
			if (data != null && !data.isEmpty())
			{
				result = (String) data.get("mockResult");
			}
		}
		return result;
	}

}
