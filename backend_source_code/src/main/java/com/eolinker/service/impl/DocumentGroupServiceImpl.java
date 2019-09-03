package com.eolinker.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.eolinker.service.DocumentGroupService;
/**
 * 数据库成员[业务处理层]
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
public class DocumentGroupServiceImpl implements DocumentGroupService
{

	@Autowired
	private DocumentGroupMapper documentGroupMapper;

	@Autowired
	private PartnerMapper partnerMapper;

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 获取成员类型
	 */
	@Override
	public int getUserType(int groupID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(groupID, userID);
		if (projectID == null)
			return -1;

		Partner userType = this.partnerMapper.getProjectUserType(userID, projectID);
		if (userType == null)
			return -1;
		else
			return userType.getUserType();
	}

	/**
	 * 添加分组
	 */
	@Override
	public int addGroup(DocumentGroup documentGroup) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, documentGroup.getProjectID());
		if (project == null)
			return -1;

		if (documentGroup.getParentGroupID() == null)
		{
			Integer affectedRow = this.documentGroupMapper.addDocumentGroup(documentGroup);

			if (affectedRow < 1)
				throw new RuntimeException("addDocumentGroup error");
			else
			{
				this.projectMapper.updateProjectUpdateTime(documentGroup.getProjectID(), null);

				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(documentGroup.getProjectID());
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
				projectOperationLog.setOpTargetID(documentGroup.getGroupID());
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
				projectOperationLog.setOpDesc("添加项目文档分组:" + documentGroup.getGroupName());

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return documentGroup.getGroupID();
			}
		}
		else
		{
			int affectedRow = this.documentGroupMapper.addChildGroup(documentGroup);
			if (affectedRow < 1)
				throw new RuntimeException("addChildGroup error");
			else
			{
				Integer projectID = this.documentGroupMapper.checkGroupPermission(documentGroup.getParentGroupID(),
						userID);
				if (projectID == null || projectID < 1)
					throw new RuntimeException("addChildGroup error");

				this.projectMapper.updateProjectUpdateTime(documentGroup.getProjectID(), null);

				String parentGroupName = this.documentGroupMapper.getGroupName(documentGroup.getParentGroupID());

				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(documentGroup.getProjectID());
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
				projectOperationLog.setOpTargetID(documentGroup.getGroupID());
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
				projectOperationLog.setOpDesc("添加项目文档子分组" + parentGroupName + ">>" + documentGroup.getGroupName());

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return documentGroup.getGroupID();
			}
		}

	}

	/**
	 * 删除分组
	 */
	@Override
	public int deleteGroup(int groupID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(groupID, userID);
		if (projectID == null)
			return -1;

		String groupName = this.documentGroupMapper.getGroupName(groupID);

		int affectedRow = this.documentGroupMapper.deleteParentalGroup(groupID);
		if (affectedRow < 1)
			throw new RuntimeException("deleteParentalGroup error");
		else
		{
			documentGroupMapper.deleteChildrenGroup(groupID);
			documentGroupMapper.deleteGroupDocument(groupID);
		}
		ProjectOperationLog projectOperationLog = new ProjectOperationLog();
		projectOperationLog.setOpProjectID(projectID);
		projectOperationLog.setOpUerID(userID);
		projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
		projectOperationLog.setOpTargetID(groupID);
		projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
		projectOperationLog.setOpDesc("删除项目文档分组:" + groupName);

		this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

		return 1;
	}

	/**
	 * 获取分组列表
	 */
	@Override
	public Map<String, Object> getGroupList(int projectID) throws RuntimeException
	{
		Map<String, Object> groupList = new HashMap<String, Object>();

		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return null;

		List<Object> resultList = new ArrayList<Object>();
		List<DocumentGroup> parentList = this.documentGroupMapper.getParentalGroup(projectID);
		if (parentList != null && !parentList.isEmpty())
		{
			for (DocumentGroup group : parentList)
			{
				List<DocumentGroup> childList = this.documentGroupMapper.getChildrenGroup(projectID,
						group.getGroupID());
				Map<String, Object> tempMap = new HashMap<String, Object>();
				
				tempMap.put("groupID", group.getGroupID());
				tempMap.put("groupName", group.getGroupName());
				tempMap.put("isChild", group.getIsChild());
				List<Object> resultList1 = new ArrayList<Object>();
				for(DocumentGroup documentGroup : childList)
				{
					List<DocumentGroup> childList1 = this.documentGroupMapper.getChildrenGroup(projectID,
							documentGroup.getGroupID());
					Map<String, Object> tempMap1 = new HashMap<String, Object>();
					
					tempMap1.put("groupID", documentGroup.getGroupID());
					tempMap1.put("groupName", documentGroup.getGroupName());
					tempMap1.put("isChild", documentGroup.getIsChild());
					tempMap1.put("childGroupList", childList1);
					resultList1.add(tempMap1);
				}
				tempMap.put("childGroupList", resultList1);
				resultList.add(tempMap);
			}
		}
		groupList.put("groupList", resultList);
		groupList.put("groupOrder", this.documentGroupMapper.getOrderList(projectID));

		return groupList;
	}

	/**
	 * 修改分组
	 */
	@Override
	public int editGroup(DocumentGroup documentGroup) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.documentGroupMapper.checkGroupPermission(documentGroup.getGroupID(), userID);
		if (projectID == null || projectID < 1)
			return -1;
		if (documentGroup.getParentGroupID() != null)
		{
			projectID = this.documentGroupMapper.checkGroupPermission(documentGroup.getParentGroupID(), userID);
			if (projectID == null || projectID < 1)
				return -1;
		}

		this.projectMapper.updateProjectUpdateTime(projectID, null);

		int affectedRow = -1;
		if (documentGroup.getParentGroupID() == null || documentGroup.getParentGroupID() < 1)
		{
			affectedRow = this.documentGroupMapper.editParentalGroup(documentGroup);
		}
		else
		{
			affectedRow = this.documentGroupMapper.editChildrenGroup(documentGroup);
		}

		if (affectedRow < 1)
			throw new RuntimeException("editGroup error");
		else
		{
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
			projectOperationLog.setOpTargetID(documentGroup.getGroupID());
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpDesc("修改项目文档分组:" + documentGroup.getGroupName());

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
		}

		return 1;
	}

	/**
	 * 对分组进行排序
	 */
	@Override
	public int sortGroup(int projectID, String orderList) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return -1;
		else
		{
			int affectedRow = this.documentGroupMapper.updateGroupOrder(projectID, orderList);
			if (affectedRow < 1)
				throw new RuntimeException("sortGroup error");
			else
			{
				this.projectMapper.updateProjectUpdateTime(projectID, null);

				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(projectID);
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
				projectOperationLog.setOpTargetID(projectID);
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
				projectOperationLog.setOpDesc("修改项目文档分组排序");

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return 1;
			}
		}
	}

	/**
	 * 导出分组
	 */
	@Override
	public String exportGroup(HttpServletRequest request, int groupID) throws RuntimeException, IOException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);
		String userName = (String) requestAttributes.getAttribute("userName", RequestAttributes.SCOPE_SESSION);

		Map<String, Object> data = new HashMap<String, Object>();

		Integer projectID = this.documentGroupMapper.checkGroupPermission(groupID, userID);
		if (projectID == null || projectID < 1)
			return null;

		DocumentGroup parentGroup = this.documentGroupMapper.getGroupData(projectID, groupID);
		List<Document> pageList = this.documentGroupMapper.getDocumentData(projectID, groupID);

		List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
		if (parentGroup != null)
		{
			List<DocumentGroup> childrenGroup = this.documentGroupMapper.getChildrenGroupData(projectID, groupID);
			if (childrenGroup != null && !childrenGroup.isEmpty())
			{
				for (DocumentGroup group : childrenGroup)
				{
					List<Document> documentData = this.documentGroupMapper.getDocumentData(projectID,
							group.getGroupID());
					Map<String, Object> tempMap = new HashMap<String, Object>();
					List<Map<String, Object>> childrenList1 = new ArrayList<Map<String, Object>>();
					List<DocumentGroup> childrenGroup1 = this.documentGroupMapper.getChildrenGroupData(projectID, group.getGroupID());
					if (childrenGroup1 != null && !childrenGroup1.isEmpty())
					{
						for (DocumentGroup group1 : childrenGroup1)
						{
							List<Document> documentData1 = this.documentGroupMapper.getDocumentData(projectID,
									group1.getGroupID());
							Map<String, Object> tempMap1 = new HashMap<String, Object>();

							tempMap1.put("pageList", documentData1);
							tempMap1.put("groupName", group1.getGroupName());
							tempMap1.put("isChild", group1.getIsChild());
							childrenList1.add(tempMap1);
						}
					}

					tempMap.put("pageList", documentData);
					tempMap.put("groupName", group.getGroupName());
					tempMap.put("isChild", group.getIsChild());
					tempMap.put("childGroupList", childrenList1);
					childrenList.add(tempMap);
				}
			}
		}

		data.put("pageList", pageList);
		data.put("groupName", parentGroup.getGroupName());
		data.put("isChild", parentGroup.getIsChild());
		data.put("childGroupList", childrenList);

		String documentInfo = JSON.toJSONString(data);
		String fileName = null;
		if(documentInfo != null && !documentInfo.equals(""))
		{
			File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!classPath.exists())
				classPath = new File("");
			File dir = new File(classPath.getAbsolutePath(), "dump");
			if (!dir.exists() || !dir.isDirectory())
				dir.mkdirs();
			String path = dir.getAbsolutePath();
			fileName = "/eoLinker_document_group_export_" + userName + "_" + new Date().getTime() + ".export";
			File file = new File(path + fileName);
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(documentInfo);
			writer.flush();
			writer.close();

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
			projectOperationLog.setOpDesc("导出文档分组：" + this.documentGroupMapper.getGroupName(groupID));

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

			return request.getContextPath() + "/dump" + fileName;
		}
		return fileName;
		
	}

	/**
	 * 导入分组
	 */
	@Override
	public int importGroup(int projectID, String data) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		JSONObject parseObject = JSON.parseObject(data);
		String groupName = parseObject.getString("groupName");
		List<Document> pageList = JSON.parseArray(parseObject.getString("pageList"), Document.class);
		JSONArray childGroupList = JSON.parseArray(parseObject.getString("childGroupList"));

		Partner member = this.partnerMapper.getProjectUserType(userID, projectID);
		if (member == null)
			return -1;

		// 插入分组
		DocumentGroup documentGroup = new DocumentGroup();
		documentGroup.setProjectID(projectID);
		documentGroup.setGroupName(groupName);
		int affectedRow = this.documentGroupMapper.addDocumentGroup(documentGroup);
		if (affectedRow < 1)
			throw new RuntimeException("addDocumentGroup error");

		int groupID = documentGroup.getGroupID();

		// 插入文档
		if (pageList != null && !pageList.isEmpty())
		{
			for (Document document : pageList)
			{

				document.setGroupID(groupID);
				document.setProjectID(projectID);
				document.setUserID(userID);

				affectedRow = this.documentMapper.addDocument(document);
				if (affectedRow < 1)
					throw new RuntimeException("addDocument error");
			}
		}

		if (childGroupList != null && !childGroupList.isEmpty())
		{
			int parentGroupID = groupID;
			Iterator<Object> iterator = childGroupList.iterator();
			while (iterator.hasNext())
			{
				JSONObject jsonObject = (JSONObject) iterator.next();
				String childGroupName = jsonObject.getString("groupName");
				JSONArray childGroupList1 = JSON.parseArray(jsonObject.getString("childGroupList"));
				// 插入子分组
				DocumentGroup childDocumentGroup = new DocumentGroup();
				childDocumentGroup.setProjectID(projectID);
				childDocumentGroup.setGroupName(childGroupName);
				childDocumentGroup.setParentGroupID(parentGroupID);
				childDocumentGroup.setIsChild(jsonObject.getInteger("isChild"));

				affectedRow = this.documentGroupMapper.addChildGroup(childDocumentGroup);
				if (affectedRow < 1)
					throw new RuntimeException("addChildGroup error");

				int childGroupID = childDocumentGroup.getGroupID();
				List<Document> childPageList = JSON.parseArray(jsonObject.getString("pageList"), Document.class);
				// 插入子分组文档
				for (Document childDoc : childPageList)
				{

					childDoc.setGroupID(childGroupID);
					childDoc.setProjectID(projectID);
					childDoc.setUserID(userID);

					affectedRow = this.documentMapper.addDocument(childDoc);
					if (affectedRow < 1)
						throw new RuntimeException("addDocument error");
				}
				if (childGroupList1 != null && !childGroupList1.isEmpty())
				{
					int parentGroupID1 = childGroupID;
					Iterator<Object> iterator1 = childGroupList1.iterator();
					while (iterator1.hasNext())
					{
						JSONObject jsonObject1 = (JSONObject) iterator1.next();
						String childGroupName1 = jsonObject1.getString("groupName");
						// 插入子分组
						DocumentGroup childDocumentGroup1 = new DocumentGroup();
						childDocumentGroup1.setProjectID(projectID);
						childDocumentGroup1.setGroupName(childGroupName1);
						childDocumentGroup1.setParentGroupID(parentGroupID1);
						childDocumentGroup1.setIsChild(jsonObject1.getInteger("isChild"));

						affectedRow = this.documentGroupMapper.addChildGroup(childDocumentGroup1);
						if (affectedRow < 1)
							throw new RuntimeException("addChildGroup error");

						int childGroupID1 = childDocumentGroup1.getGroupID();
						List<Document> childPageList1 = JSON.parseArray(jsonObject1.getString("pageList"), Document.class);
						// 插入子分组文档
						for (Document childDoc : childPageList1)
						{

							childDoc.setGroupID(childGroupID1);
							childDoc.setProjectID(projectID);
							childDoc.setUserID(userID);

							affectedRow = this.documentMapper.addDocument(childDoc);
							if (affectedRow < 1)
								throw new RuntimeException("addDocument error");
						}
					}
				}
			}
		}

		ProjectOperationLog projectOperationLog = new ProjectOperationLog();
		projectOperationLog.setOpProjectID(projectID);
		projectOperationLog.setOpUerID(userID);
		projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT_DOCUMENT_GROUP);
		projectOperationLog.setOpTargetID(projectID);
		projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
		projectOperationLog.setOpDesc("导入文档分组：" + groupName);

		this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

		return 1;
	}

}
