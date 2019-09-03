package com.eolinker.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.dto.DocumentDTO;
import com.eolinker.mapper.DocumentGroupMapper;
import com.eolinker.mapper.DocumentMapper;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.PartnerMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.pojo.Document;
import com.eolinker.pojo.DocumentGroup;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.pojo.StatusCodeGroup;
import com.eolinker.service.DocumentService;
/**
 * 项目文档[业务处理层]
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
@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "java.lang.Exception")
public class DocumentServiceImpl implements DocumentService
{

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentGroupMapper documentGroupMapper;

	@Autowired
	private PartnerMapper partnerMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 获取成员类型
	 */
	@Override
	public int getUserType(int documentID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentMapper.checkDocumentPermission(documentID, userID);
		if (projectID == null || projectID < 1)
			return -1;

		Partner projectUserType = this.partnerMapper.getProjectUserType(userID, projectID);
		if (projectUserType != null)
			return projectUserType.getUserType();
		else
			return -1;
	}

	/**
	 * 添加文档
	 */
	@Override
	public int addDocument(Document document) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(document.getGroupID(), userID);
		if (projectID == null || projectID < 1)
			return -1;
		else
		{
			this.projectMapper.updateProjectUpdateTime(projectID, null);
			document.setProjectID(projectID);
			document.setUserID(userID);
			if (this.documentMapper.addDocument(document) < 1)
				throw new RuntimeException("addDocument error");
			else
			{
				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(projectID);
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT);
				projectOperationLog.setOpTargetID(document.getDocumentID());
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
				projectOperationLog.setOpDesc("添加项目文档:" + document.getTitle());

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return document.getDocumentID();
			}
		}
	}

	/**
	 * 修改文档
	 */
	@Override
	public int editDocument(Document document) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(document.getGroupID(), userID);
		if (projectID == null || projectID < 1)
			return -1;
		projectID = this.documentMapper.checkDocumentPermission(document.getDocumentID(), userID);
		if (projectID == null || projectID < 1)
			return -1;

		this.projectMapper.updateProjectUpdateTime(document.getProjectID(), null);

		document.setProjectID(projectID);
		document.setUserID(userID);
		int affectedRow = this.documentMapper.editDocument(document);

		if (affectedRow < 1)
			throw new RuntimeException("editCode error");
		else
		{

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT);
			projectOperationLog.setOpTargetID(document.getDocumentID());
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpDesc("修改项目文档:" + document.getTitle());

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

			return 1;
		}
	}

	/**
	 * 获取文档列表
	 */
	@Override
	public List<DocumentDTO> getDocumentList(int groupID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(groupID, userID);
		if (projectID == null || projectID < 1)
			return null;
		else
		{
			List<Integer> groupIDS = new ArrayList<Integer>();
			groupIDS.add(groupID);
			List<DocumentGroup> childGroupList = documentGroupMapper.getChildrenGroup(projectID,groupID);
			if(childGroupList != null && !childGroupList.isEmpty())
			{
				for (DocumentGroup group : childGroupList)
				{
					groupIDS.add(group.getGroupID());
					List<DocumentGroup> childGroupList1 = documentGroupMapper.getChildrenGroup(projectID, group.getGroupID());
					if(childGroupList1 != null && !childGroupList1.isEmpty())
					{
						for (DocumentGroup group1 : childGroupList1)
						{
							groupIDS.add(group1.getGroupID());
						}
					}
				}
			}
			List<DocumentDTO> documentList = this.documentMapper.getDocumentList(groupIDS);
			return (documentList == null || documentList.isEmpty()) ? null : documentList;
		}

	}

	/**
	 * 获取文档列表
	 */
	@Override
	public List<DocumentDTO> getAllDocumentList(int projectID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return null;
		else
		{
			List<DocumentDTO> documentList = this.documentMapper.getAllDocumentList(projectID);
			return (documentList == null || documentList.isEmpty()) ? null : documentList;
		}
	}

	/**
	 * 搜索文档
	 */
	@Override
	public List<DocumentDTO> searchDocument(int projectID, String tips) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return null;
		else
		{
			List<DocumentDTO> resultList = this.documentMapper.searchDocument(projectID, tips);
			return (resultList == null || resultList.isEmpty()) ? null : resultList;
		}
	}

	/**
	 * 获取文档详情
	 */
	@Override
	public Map<String, Object> getDocument(int documentID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentMapper.checkDocumentPermission(documentID, userID);
		if (projectID == null || projectID < 1)
			return null;
		else
		{
			Map<String, Object> document = documentMapper.getDocument(documentID);
			if (document != null && !document.isEmpty())
			{
				DocumentGroup parentGroupInfo = documentMapper
						.getParentGroupInfo(new Integer(document.get("groupID").toString()));
				Integer topParentGroupID = 0;
				if (parentGroupInfo != null)
				{
					document.put("parentGroupID", parentGroupInfo.getParentGroupID());
					document.put("parentGroupName", parentGroupInfo.getGroupName());
					topParentGroupID = documentGroupMapper.getParentGroupID(parentGroupInfo.getParentGroupID());
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String updateTime = dateFormat.format(document.get("updateTime"));
				document.put("updateTime", updateTime);
				document.put("topParentGroupID", topParentGroupID);
				return document;
			}
			else
				return null;
		}
	}

	/**
	 * 批量删除文档
	 */
	@Override
	public int deleteBatchDocument(List<Integer> documentIDs, int projectID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Partner member = this.partnerMapper.getProjectUserType(userID, projectID);
		if (member == null)
			return -1;
		String documentTitles = this.documentMapper.getDocumentTitle(documentIDs);
		if (this.documentMapper.deleteDocuments(documentIDs, projectID) < 1)
			throw new RuntimeException("deleteDocuments error");
		else
		{
			this.projectMapper.updateProjectUpdateTime(projectID, null);

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTargetID(0);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpDesc("删除项目文档:" + documentTitles);

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return 1;
		}

	}

}
