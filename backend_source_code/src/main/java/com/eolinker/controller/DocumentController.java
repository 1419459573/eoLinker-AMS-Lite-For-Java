package com.eolinker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.eolinker.dto.DocumentDTO;
import com.eolinker.pojo.Document;
import com.eolinker.pojo.Partner;
import com.eolinker.service.DocumentGroupService;
import com.eolinker.service.DocumentService;
import com.eolinker.service.ProjectService;
/**
 * 项目文档控制器
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
@RequestMapping("/Document")
public class DocumentController
{

	@Autowired
	private DocumentGroupService documentGroupService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private ProjectService projectService;

	/**
	 * 添加文档
	 * 
	 * @param document
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addDocument")
	public Map<String, Object> addDocument(Document document)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (document.getGroupID() == null || !String.valueOf(document.getGroupID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230001");
			else if (document.getContentType() == null
					|| (document.getContentType() != 1 && document.getContentType() != 0))
				map.put("statusCode", "230002");
			else
			{
				int userType = this.documentGroupService.getUserType(document.getGroupID());
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}

				int result = this.documentService.addDocument(document);
				if (result > 0)
				{
					map.put("statusCode", "000000");
					map.put("documentID", result);
				}
				else
					map.put("statusCode", "230000");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}
		return map;
	}

	/**
	 * 编辑文档
	 * 
	 * @param document
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editDocument")
	public Map<String, Object> editDocument(Document document)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (document.getDocumentID() == null || !String.valueOf(document.getDocumentID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230003");
			else if (document.getGroupID() == null || !String.valueOf(document.getGroupID()).matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230001");
			else
			{
				int userType = this.documentService.getUserType(document.getDocumentID());
				if (userType < 0 || userType > 2)
				{
					map.put("statusCode", "120007");
					return map;
				}
				int result = this.documentService.editDocument(document);
				map.put("statusCode", (result > 0) ? "000000" : "230000");
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}
		return map;
	}

	/**
	 * 获取文档列表
	 * 
	 * @param groupID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDocumentList")
	public Map<String, Object> getDocumentList(@RequestParam("groupID") String groupID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!groupID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230001");
			else
			{
				List<DocumentDTO> documentList = this.documentService.getDocumentList(Integer.parseInt(groupID));

				if (documentList != null)
				{
					map.put("statusCode", "000000");
					map.put("documentList", documentList);
				}
				else
					map.put("statusCode", "230000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230001");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}

		return map;
	}

	/**
	 * 获取所有文档列表
	 * 
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllDocumentList")
	public Map<String, Object> getAllDocumentList(@RequestParam("projectID") String projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230004");
			else
			{
				List<DocumentDTO> documentList = this.documentService.getAllDocumentList(Integer.parseInt(projectID));

				if (documentList != null)
				{
					map.put("statusCode", "000000");
					map.put("documentList", documentList);
				}
				else
					map.put("statusCode", "230000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230004");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}

		return map;
	}

	/**
	 * 搜索文档
	 * 
	 * @param projectID
	 * @param tips
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchDocument")
	public Map<String, Object> searchDocument(@RequestParam("projectID") String projectID,
			@RequestParam("tips") String tips)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230004");
			else if (tips.length() < 1 || tips.length() > 255)
				map.put("statusCode", "230005");
			else
			{
				List<DocumentDTO> resultList = this.documentService.searchDocument(Integer.valueOf(projectID), tips);
				if (resultList != null)
				{
					map.put("statusCode", "000000");
					map.put("documentList", resultList);
				}
				else
					map.put("statusCode", "230000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230004");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}

		return map;
	}

	/**
	 * 获取文档详情
	 * 
	 * @param documentID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDocument")
	public Map<String, Object> getDocument(String documentID)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		try
		{
			if (!documentID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "230003");
			else
			{
				Map<String, Object> documentInfo = documentService.getDocument(Integer.parseInt(documentID));

				if (documentInfo != null && !documentInfo.isEmpty())
				{
					map.put("statusCode", "000000");
					map.put("documentInfo", documentInfo);
				}
				else
					map.put("statusCode", "230000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}
		return map;
	}

	/**
	 * 批量删除文档
	 * 
	 * @param session
	 * @param documentIDs
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDocuments")
	public Map<String, Object> deleteDocuments(HttpSession session, @RequestParam("documentID") String documentID,
			Integer projectID)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> documentIDlist = new ArrayList<Integer>();

		try
		{
			List<String> parseArray = JSON.parseArray(documentID, String.class);

			for (String id : parseArray)
			{

				if (!id.matches("^[0-9]{1,11}$"))
				{
					map.put("statusCode", "230004");
					return map;
				}
				else
					documentIDlist.add(Integer.parseInt(id));
			}

			int userID = (int) session.getAttribute("userID");

			Partner member = this.projectService.getProjectUserType(userID, projectID);
			if (member == null || member.getUserType() < 0 || member.getUserType() > 2)
				map.put("statusCode", "120007");
			else
			{
				int result = this.documentService.deleteBatchDocument(documentIDlist, projectID);
				map.put("statusCode", (result > 0) ? "000000" : "230000");
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230003");
			return map;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			map.put("statusCode", "230000");
			return map;
		}
		return map;
	}

}
