package com.eolinker.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.PartnerMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.mapper.StatusCodeGroupMapper;
import com.eolinker.mapper.StatusCodeMapper;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.pojo.StatusCode;
import com.eolinker.pojo.StatusCodeGroup;
import com.eolinker.service.StatusCodeGroupService;
/**
 * 状态码分组[业务处理层]
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
public class StatusCodeGroupServiceImpl implements StatusCodeGroupService
{

	@Autowired
	private StatusCodeGroupMapper statusCodeGroupMapper;

	@Autowired
	private StatusCodeMapper statusCodeMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private PartnerMapper partnerMapper;

	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 获取用户权限类型
	 */
	@Override
	public int getUserType(int groupID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(groupID, userID);
		if (projectID == null || projectID < 1)
			return -1;

		Partner projectUserType = this.partnerMapper.getProjectUserType(userID, projectID);
		if (projectUserType != null)
			return projectUserType.getUserType();
		else
			return -1;
	}

	/**
	 * 添加分组
	 */
	@Override
	public int addGroup(StatusCodeGroup statusCodeGroup) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, statusCodeGroup.getProjectID());
		if (project == null)
			return -1;
		else
		{
			this.projectMapper.updateProjectUpdateTime(statusCodeGroup.getProjectID(), null);
			if (statusCodeGroup.getParentGroupID() == null || statusCodeGroup.getParentGroupID() < 0)
			{
				int affectedRow = this.statusCodeGroupMapper.addGroup(statusCodeGroup);

				if (affectedRow > 0)
				{
					ProjectOperationLog projectOperationLog = new ProjectOperationLog();
					projectOperationLog.setOpProjectID(statusCodeGroup.getProjectID());
					projectOperationLog.setOpUerID(userID);
					projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
					projectOperationLog.setOpTargetID(statusCodeGroup.getGroupID());
					projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
					projectOperationLog.setOpDesc("新增状态码分组:" + statusCodeGroup.getGroupName());

					this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

					return statusCodeGroup.getGroupID();
				}
				else
					throw new RuntimeException("addGroup error");
			}
			else
			{
				int affectedRow = this.statusCodeGroupMapper.addChildGroup(statusCodeGroup);

				if (affectedRow > 0)
				{
					String paregroupName = this.statusCodeGroupMapper.getGroupName(statusCodeGroup.getParentGroupID());

					ProjectOperationLog projectOperationLog = new ProjectOperationLog();
					projectOperationLog.setOpProjectID(statusCodeGroup.getProjectID());
					projectOperationLog.setOpUerID(userID);
					projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
					projectOperationLog.setOpTargetID(statusCodeGroup.getGroupID());
					projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
					projectOperationLog.setOpDesc("新增状态码子分组:" + paregroupName + ">>" + statusCodeGroup.getGroupName());

					this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

					return statusCodeGroup.getGroupID();
				}
				else
					throw new RuntimeException("addChildGroup error");
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

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(groupID, userID);
		if (projectID == null || projectID < 0)
			return -1;

		String groupName = this.statusCodeGroupMapper.getGroupName(groupID);
		int affectedRow = this.statusCodeGroupMapper.deleteGroup(groupID);

		if (affectedRow < 1)
			throw new RuntimeException("deleteGroup");
		else
		{
			this.projectMapper.updateProjectUpdateTime(projectID, null);

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpDesc("删除状态码分组:" + groupName);

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

			return 1;
		}

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
		List<StatusCodeGroup> parentList = this.statusCodeGroupMapper.getParentList(projectID);
		if (parentList != null && !parentList.isEmpty())
		{
			for (StatusCodeGroup group : parentList)
			{
				List<StatusCodeGroup> childList = this.statusCodeGroupMapper.getChildList(projectID,
						group.getGroupID());
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("groupID", group.getGroupID());
				tempMap.put("groupName", group.getGroupName());
				tempMap.put("isChild", group.getIsChild());
				List<Object> resultList1 = new ArrayList<Object>();
				for(StatusCodeGroup statusCodeGroup : childList)
				{
					List<StatusCodeGroup> childList1 = this.statusCodeGroupMapper.getChildList(projectID,
							statusCodeGroup.getGroupID());
					Map<String, Object> tempMap1 = new HashMap<String, Object>();
					tempMap1.put("groupID", statusCodeGroup.getGroupID());
					tempMap1.put("groupName", statusCodeGroup.getGroupName());
					tempMap1.put("isChild", statusCodeGroup.getIsChild());
					tempMap1.put("childGroupList", childList1);
					resultList1.add(tempMap1);
				}
				tempMap.put("childGroupList", resultList1);
				resultList.add(tempMap);
			}
		}

		groupList.put("groupList", resultList);
		groupList.put("groupOrder", this.statusCodeGroupMapper.getGroupOrder(projectID));

		return groupList;
	}

	/**
	 * 修改分组
	 */
	@Override
	public int editGroup(StatusCodeGroup statusCodeGroup) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(statusCodeGroup.getGroupID(),
				userID);
		if (projectID == null || projectID < 1)
			return -1;

		if (statusCodeGroup.getParentGroupID() != null)
		{
			projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(statusCodeGroup.getParentGroupID(),
					userID);
			if (projectID == null || projectID < 1)
				return -1;
		}

		this.projectMapper.updateProjectUpdateTime(projectID, null);

		int affectedRow = -1;
		if (statusCodeGroup.getParentGroupID() == null || statusCodeGroup.getParentGroupID() < 1)
		{
			affectedRow = this.statusCodeGroupMapper.editParentalGroup(statusCodeGroup);
		}
		else
		{
			affectedRow = this.statusCodeGroupMapper.editChildGroup(statusCodeGroup);
		}

		if (affectedRow < 1)
			throw new RuntimeException("editGroup error");
		else
		{
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
			projectOperationLog.setOpTargetID(statusCodeGroup.getGroupID());
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpDesc("修改状态码分组:" + statusCodeGroup.getGroupName());

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
			int affectedRow = this.statusCodeGroupMapper.sortGroup(projectID, orderList);
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
				projectOperationLog.setOpDesc("修改状态码分组排序");

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

		Map<String, Object> data = new HashMap<String, Object>();

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(groupID, userID);
		if (projectID == null || projectID < 1)
			return null;

		StatusCodeGroup parentGroup = this.statusCodeGroupMapper.getGroupData(projectID, groupID);
		List<StatusCode> statusCodeList = this.statusCodeGroupMapper.getStatusCodeData(groupID);

		List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
		if (parentGroup != null)
		{
			List<StatusCodeGroup> childrenGroup = this.statusCodeGroupMapper.getChildGroupData(groupID, projectID);
			if (childrenGroup != null && !childrenGroup.isEmpty())
			{
				for (StatusCodeGroup group : childrenGroup)
				{
					List<StatusCode> statusCodeData = this.statusCodeGroupMapper.getStatusCodeData(group.getGroupID());
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("statusCodeList", statusCodeData);
					tempMap.put("groupName", group.getGroupName());
					tempMap.put("isChild", group.getIsChild());
					List<StatusCodeGroup> childrenGroup1 = this.statusCodeGroupMapper.getChildGroupData(group.getGroupID(), projectID);
					List<Map<String, Object>> childrenList1 = new ArrayList<Map<String, Object>>();
					if (childrenGroup1 != null && !childrenGroup1.isEmpty())
					{
						for (StatusCodeGroup group1 : childrenGroup1)
						{
							List<StatusCode> statusCodeData1 = this.statusCodeGroupMapper.getStatusCodeData(group1.getGroupID());
							Map<String, Object> tempMap1 = new HashMap<String, Object>();
							tempMap1.put("statusCodeList", statusCodeData1);
							tempMap1.put("groupName", group1.getGroupName());
							tempMap1.put("isChild", group1.getIsChild());
							childrenList1.add(tempMap1);
						}
					}
					 tempMap.put("childGroupList", childrenList1);
					childrenList.add(tempMap);
				}
			}
		}

		data.put("statusCodeList", statusCodeList);
		data.put("groupName", parentGroup.getGroupName());
		data.put("isChild", parentGroup.getIsChild());
		data.put("childGroupList", childrenList);

		String statusCodeInfo = JSON.toJSONString(data);
		String fileName = null;
		if(statusCodeInfo != null && !statusCodeInfo.equals(""))
		{
			HttpSession session = request.getSession(true);
			File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!classPath.exists())
				classPath = new File("");
			File dir = new File(classPath.getAbsolutePath(), "dump");
			if (!dir.exists() || !dir.isDirectory())
				dir.mkdirs();
			String path = dir.getAbsolutePath();
			fileName = "/eoLinker_statusCode_group_export_" + session.getAttribute("userName") + "_"
					+ System.currentTimeMillis() + ".export";
			File file = new File(path + fileName);
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(statusCodeInfo);
			writer.flush();
			writer.close();

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
			projectOperationLog.setOpDesc("导出状态码分组：" + this.statusCodeGroupMapper.getGroupName(groupID));
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
		List<StatusCode> statusCodeList = JSON.parseArray(parseObject.getString("statusCodeList"), StatusCode.class);
		JSONArray childGroupList = JSON.parseArray(parseObject.getString("childGroupList"));

		Partner member = this.partnerMapper.getProjectUserType(userID, projectID);
		if (member == null)
			return -1;

		StatusCodeGroup statusCodeGroup = new StatusCodeGroup();
		statusCodeGroup.setProjectID(projectID);
		statusCodeGroup.setGroupName(groupName);
		int affectedRow = this.statusCodeGroupMapper.addGroup(statusCodeGroup);
		if (affectedRow < 1)
			throw new RuntimeException("addGroup error");

		int groupID = statusCodeGroup.getGroupID();

		// 插入文档
		if (statusCodeList != null && !statusCodeList.isEmpty())
		{
			for (StatusCode statusCode : statusCodeList)
			{
				statusCode.setGroupID(groupID);
				affectedRow = this.statusCodeMapper.addCode(statusCode);

				if (affectedRow < 1)
					throw new RuntimeException("addCode error");
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

				StatusCodeGroup childStatusCodeGroup = new StatusCodeGroup();
				childStatusCodeGroup.setProjectID(projectID);
				childStatusCodeGroup.setGroupName(childGroupName);
				childStatusCodeGroup.setParentGroupID(parentGroupID);
				childStatusCodeGroup.setIsChild(jsonObject.getInteger("isChild"));

				affectedRow = this.statusCodeGroupMapper.addChildGroup(childStatusCodeGroup);
				if (affectedRow < 1)
					throw new RuntimeException("addChildGroup error");

				int childGroupID = childStatusCodeGroup.getGroupID();
				List<StatusCode> childStatusCodeList = JSON.parseArray(jsonObject.getString("statusCodeList"),
						StatusCode.class);

				for (StatusCode childStatusCode : childStatusCodeList)
				{
					childStatusCode.setGroupID(childGroupID);

					affectedRow = this.statusCodeMapper.addCode(childStatusCode);
					if (affectedRow < 1)
						throw new RuntimeException("addCode error");
				}
				JSONArray childGroupList1 = JSON.parseArray(jsonObject.getString("childGroupList"));
				if (childGroupList1 != null && !childGroupList1.isEmpty())
				{
					int parentGroupID1 = childGroupID;
					Iterator<Object> iterator1 = childGroupList1.iterator();
					while (iterator1.hasNext())
					{
						JSONObject jsonObject1 = (JSONObject) iterator1.next();
						String childGroupName1 = jsonObject1.getString("groupName");

						StatusCodeGroup childStatusCodeGroup1 = new StatusCodeGroup();
						childStatusCodeGroup1.setProjectID(projectID);
						childStatusCodeGroup1.setGroupName(childGroupName1);
						childStatusCodeGroup1.setParentGroupID(parentGroupID1);
						childStatusCodeGroup1.setIsChild(jsonObject1.getInteger("isChild"));

						affectedRow = this.statusCodeGroupMapper.addChildGroup(childStatusCodeGroup1);
						if (affectedRow < 1)
							throw new RuntimeException("addChildGroup error");

						int childGroupID1 = childStatusCodeGroup1.getGroupID();
						List<StatusCode> childStatusCodeList1 = JSON.parseArray(jsonObject1.getString("statusCodeList"),
								StatusCode.class);

						for (StatusCode childStatusCode : childStatusCodeList1)
						{
							childStatusCode.setGroupID(childGroupID1);

							affectedRow = this.statusCodeMapper.addCode(childStatusCode);
							if (affectedRow < 1)
								throw new RuntimeException("addCode error");
						}
					}
				}
			}
		}

		ProjectOperationLog projectOperationLog = new ProjectOperationLog();
		projectOperationLog.setOpProjectID(projectID);
		projectOperationLog.setOpUerID(userID);
		projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE_GROUP);
		projectOperationLog.setOpTargetID(projectID);
		projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
		projectOperationLog.setOpDesc("导入状态码分组：" + groupName);

		this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

		return 1;
	}

}
