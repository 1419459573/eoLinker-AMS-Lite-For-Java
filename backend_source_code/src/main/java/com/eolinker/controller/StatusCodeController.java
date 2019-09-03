package com.eolinker.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.eolinker.dto.CodeListDTO;
import com.eolinker.pojo.StatusCode;
import com.eolinker.service.ProjectService;
import com.eolinker.service.StatusCodeGroupService;
import com.eolinker.service.StatusCodeService;
/**
 * 状态码控制器
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
@RequestMapping("/StatusCode")
public class StatusCodeController
{

	@Autowired
	private StatusCodeService statusCodeService;

	@Autowired
	private StatusCodeGroupService statusCodeGroupService;
	
	@Resource
	private ProjectService projectService;

	/**
	 * 添加状态码
	 * 
	 * @param statusCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addCode")
	public Map<String, Object> addCode(StatusCode statusCode, String codeDesc)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (statusCode.getGroupID() == null || !String.valueOf(statusCode.getGroupID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190002");
			else if (statusCode.getCode() == null || statusCode.getCode().length() < 1
					|| statusCode.getCode().length() > 255)
				map.put("statusCode", "190008");
			else if (codeDesc == null || codeDesc.length() < 1 || codeDesc.length() > 255)
				map.put("statusCode", "190003");
			else
			{
				int userType = this.statusCodeGroupService.getUserType(statusCode.getGroupID());
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				statusCode.setCodeDescription(codeDesc);
				int result = this.statusCodeService.addCode(statusCode);
				if (result > 0)
				{
					map.put("statusCode", "000000");
					map.put("codeID", result);
				}
				else
					map.put("statusCode", "190004");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190004");
			return map;
		}
		return map;
	}

	/**
	 * 批量删除状态码
	 * 
	 * @param codeID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteCode")
	public Map<String, Object> deleteCode(@RequestParam("codeID") String codeIDs)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> codeIDlist = new ArrayList<Integer>();

		try
		{
			List<String> parseArray = JSON.parseArray(codeIDs, String.class);
			for (String codeID : parseArray)
			{
				if (!codeID.matches("^[0-9]{1,11}$"))
				{
					map.put("statusCode", "190003");
					return map;
				}
				else
					codeIDlist.add(Integer.parseInt(codeID));
			}

			int result = this.statusCodeService.deleteBatchCode(codeIDlist);
			map.put("statusCode", (result > 0) ? "000000" : "190000");

		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190000");
			return map;
		}
		return map;
	}

	/**
	 * 获取状态码列表
	 * 
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCodeList")
	public Map<String, Object> getCodeList(@RequestParam("groupID") String groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!groupID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190002");
			else
			{
				List<CodeListDTO> codeList = this.statusCodeService.getCodeList(Integer.parseInt(groupID));

				if (codeList != null)
				{
					map.put("statusCode", "000000");
					map.put("codeList", codeList);
				}
				else
					map.put("statusCode", "190001");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190002");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190001");
			return map;
		}

		return map;
	}

	/**
	 * 获取所有状态码列表
	 * 
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllCodeList")
	public Map<String, Object> getAllCodeList(@RequestParam("projectID") String projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190007");
			else
			{
				List<CodeListDTO> codeList = this.statusCodeService.getAllCodeList(Integer.parseInt(projectID));

				if (codeList != null)
				{
					map.put("statusCode", "000000");
					map.put("codeList", codeList);
				}
				else
					map.put("statusCode", "190001");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190007");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 修改状态码
	 * 
	 * @param statusCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editCode")
	public Map<String, Object> editCode(StatusCode statusCode, String codeDesc)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (statusCode.getCodeID() == null || !String.valueOf(statusCode.getCodeID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190005");
			else if (statusCode.getGroupID() == null
					|| !String.valueOf(statusCode.getGroupID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190002");
			else if (statusCode.getCode() == null || statusCode.getCode().length() < 1
					|| statusCode.getCode().length() > 255)
				map.put("statusCode", "190008");
			else if (codeDesc == null || codeDesc.length() < 1 || codeDesc.length() > 255)
				map.put("statusCode", "190003");
			else
			{
				int userType = this.statusCodeService.getUserType(statusCode.getCodeID());
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				statusCode.setCodeDescription(codeDesc);
				int result = this.statusCodeService.editCode(statusCode);
				map.put("statusCode", (result > 0) ? "000000" : "190009");
			}
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190009");
			return map;
		}
	}

	/**
	 * 搜索状态码
	 * 
	 * @param projectID
	 * @param tips
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchStatusCode")
	public Map<String, Object> searchStatusCode(@RequestParam("projectID") String projectID,
			@RequestParam("tips") String tips)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190007");
			else if (tips.length() < 1 || tips.length() > 255)
				map.put("statusCode", "190008");
			else
			{
				List<CodeListDTO> resultList = this.statusCodeService.searchStatusCode(Integer.valueOf(projectID),
						tips);
				if (resultList != null)
				{
					map.put("statusCode", "000000");
					map.put("codeList", resultList);
				}
				else
					map.put("statusCode", "190001");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190007");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "190001");
			return map;
		}

		return map;
	}

	/**
	 * 获取状态码数量
	 * 
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getStatusCodeNum")
	public Map<String, Object> getStatusCodeNum(@RequestParam("projectID") String projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "190007");
			else
			{
				int count = this.statusCodeService.getStatusCodeNum(Integer.parseInt(projectID));
				if (count < 0)
					map.put("statusCode", "190010");
				else
				{
					map.put("statusCode", "000000");
					map.put("num", count);
				}
			}
		}
		catch (NumberFormatException e)
		{
			map.put("statusCode", "190007");
			e.printStackTrace();
			return map;
		}

		return map;
	}
	
	/**
	 * 导入excel表
	 * 
	 * @param request
	 * @param project
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addStatusCodeByExcel", method = RequestMethod.POST)
	public Map<String, Object> addStatusCodeByExcel(HttpServletRequest request,HttpServletResponse response, Integer groupID, @RequestParam("excel") MultipartFile file)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(file == null)
		{
			map.put("statusCode", "190005");
			return map;
		}
        String name=file.getOriginalFilename();
        long size=file.getSize();
        if(name==null || ("").equals(name) && size==0){
        	map.put("statusCode", "190005");
        	return map;
        }     
		HttpSession session = request.getSession(true);
		Integer userID = (Integer) session.getAttribute("userID");
		Integer userType = statusCodeGroupService.getUserType(groupID);
		if (userType > 2)
		{
			map.put("statusCode", "100002");
		}
		else
		{
			InputStream inputStream = null;
			try
			{
				inputStream = file.getInputStream();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				map.put("statusCode", "190006");
			}   
			boolean result = statusCodeService.addStatusCodeByExcel(groupID, userID, inputStream);
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
