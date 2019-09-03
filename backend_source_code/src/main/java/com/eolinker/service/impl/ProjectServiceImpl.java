package com.eolinker.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.eolinker.mapper.*;
import com.eolinker.pojo.*;
import com.eolinker.service.ProjectService;
/**
 * 项目[业务处理层]
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
public class ProjectServiceImpl implements ProjectService
{

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ApiGroupMapper apiGroupMapper;
	@Autowired
	private StatusCodeGroupMapper statusCodeGroupMapper;
	@Autowired
	private DocumentGroupMapper documentGroupMapper;
	@Autowired
	private PartnerMapper partnerMapper;
	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;
	@Autowired
	private ApiMapper apiMapper;
	@Autowired
	private ApiCacheMapper apiCacheMapper;
	@Autowired
	private StatusCodeMapper statusCodeMapper;
	@Autowired
	private EnvFrontUriMapper envFrontUriMapper;
	@Autowired
	private EnvHeaderMapper envHeaderMapper;
	@Autowired
	private EnvMapper envMapper;
	@Autowired
	private EnvParamMapper envParamMapper;
	@Autowired
	private EnvParamAdditionalMapper envParamAdditionalMapper;

	/**
	 * 新建项目
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public Map<String, Object> addProject(Project project, HttpSession session)
	{
		// TODO Auto-generated method stub
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		project.setProjectUpdateTime(updateTime);
		Integer resutl = projectMapper.addProject(project);
		Map<String, Object> projectInfo = new HashMap<String, Object>();
		if (resutl != 0)
		{
			Integer projectID = project.getProjectID();
			Integer userID = (Integer) session.getAttribute("userID");
			// 添加用户与项目之间的联系
			Partner partner = new Partner();
			partner.setUserID(userID);
			partner.setProjectID(projectID);
			partner.setUserType(0);
			partnerMapper.addPartner(partner);

			String groupName = "默认分组";

			// 添加接口默认分组
			ApiGroup apiGroup = new ApiGroup();
			apiGroup.setGroupName(groupName);
			apiGroup.setIsChild(0);
			apiGroup.setParentGroupID(0);
			apiGroup.setProjectID(projectID);
			apiGroupMapper.addApiGroup(apiGroup);

			// 添加状态码默认分组
			StatusCodeGroup statusCodeGroup = new StatusCodeGroup();
			statusCodeGroup.setGroupName(groupName);
			statusCodeGroup.setIsChild(0);
			statusCodeGroup.setParentGroupID(0);
			statusCodeGroup.setProjectID(projectID);
			statusCodeGroupMapper.addStatusCodeGroup(statusCodeGroup);

			// 添加项目文档默认分组
			DocumentGroup documentGroup = new DocumentGroup();
			documentGroup.setGroupName(groupName);
			documentGroup.setIsChild(0);
			documentGroup.setIsChild(0);
			documentGroup.setProjectID(projectID);
			documentGroupMapper.addDocumentGroup(documentGroup);

			// 添加操作记录
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("创建项目");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PROJECT);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

			// 返回信息
			projectInfo.put("projectID", projectID);
			projectInfo.put("projectType", project.getProjectType());
			projectInfo.put("projectUpdateTime", time);
			projectInfo.put("projectVersion", project.getProjectVersion());
		}
		return projectInfo;
	}

	/**
	 * 获取用户类型
	 */
	@Override
	public Partner getProjectUserType(Integer userID, Integer projectID)
	{
		// TODO Auto-generated method stub

		return partnerMapper.getProjectUserType(userID, projectID);
	}

	/**
	 * 删除项目
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int deleteProject(Integer projectID)
	{
		// TODO Auto-generated method stub
		int result = projectMapper.deleteProject(projectID);
		if (result > 0)
		{
			partnerMapper.deleteAllMember(projectID);
		}
		return result;
	}

	/**
	 * 获取项目列表
	 */
	@Override
	public List<Map<String, Object>> getProjectList(Integer userID, Integer projectType)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> projectList = projectMapper.getProjectList(userID);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> project : projectList)
		{
			String updateTime = dateFormat.format(project.get("projectUpdateTime"));
			project.put("projectUpdateTime", updateTime);
		}
		return projectList;
	}

	/**
	 * 修改项目
	 */
	@Override
	public boolean editProject(Project project)
	{
		// TODO Auto-generated method stub
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		project.setProjectUpdateTime(updateTime);
		Integer result = projectMapper.updateProject(project);
		if (result > 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取项目信息
	 */
	@Override
	public Map<String, Object> getProject(Integer userID, Integer projectID)
	{
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Project project = projectMapper.getProject(userID, projectID);
		map.put("projectID", project.getProjectID());
		map.put("projectName", project.getProjectName());
		map.put("projectType", project.getProjectType());
		map.put("userType", project.getUserType());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime = dateFormat.format(project.getProjectUpdateTime());
		map.put("projectUpdateTime", updateTime);
		map.put("projectVersion", project.getProjectVersion());
		map.put("apiCount", apiMapper.getApiCount(projectID));
		map.put("statusCodeCount", statusCodeMapper.getStatusCodeCount(projectID));
		map.put("partnerCount", partnerMapper.getPartnerCount(projectID));
		Integer page = 0;
		Integer pageSize = 10;
		Integer dayOffset = 1;
		List<Map<String, Object>> logList = projectOperationLogMapper.getProjectLogList(projectID, page, pageSize,
				dayOffset);
		for (Map<String, Object> log : logList)
		{
			String optime = dateFormat.format(log.get("opTime"));
			log.put("opTime", optime);
		}
		map.put("logList", logList);
		map.put("logCount", projectOperationLogMapper.getLogCount(projectID, dayOffset));
		return map;
	}

	/**
	 * 获取项目日志列表
	 */
	@Override
	public List<Map<String, Object>> getProjectLogList(Integer projectID, Integer page, Integer pageSize)
	{
		// TODO Auto-generated method stub
		Integer dayOffset = 7;
		page = (page - 1) * pageSize;
		List<Map<String, Object>> logList = projectOperationLogMapper.getProjectLogList(projectID, page, pageSize,
				dayOffset);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> log : logList)
		{
			String optime = dateFormat.format(log.get("opTime"));
			log.put("opTime", optime);
		}
		return logList;
	}

	/**
	 * 获取项目日志列表
	 */
	@Override
	public int getProjectLogCount(Integer projectID, int dayOffset)
	{
		// TODO Auto-generated method stub
		return projectOperationLogMapper.getLogCount(projectID, dayOffset);
	}

	/**
	 * 获取接口数量
	 */
	@Override
	public int getApiNum(Integer projectID)
	{
		// TODO Auto-generated method stub
		return apiMapper.getApiCount(projectID);
	}
	
	/**
	 * 导出项目数据
	 */
	@Override
	public Map<String, Object> exportProjectData(Integer projectID, Integer userID)
	{
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> projectInfo = projectMapper.getProjectInfo(projectID);
		if (projectInfo != null && !projectInfo.isEmpty())
		{
			result.put("projectInfo", projectInfo);
		}
		List<Map<String, Object>> apiGroupList = apiGroupMapper.getParentGroupList(projectID);
		if (apiGroupList != null && !apiGroupList.isEmpty())
		{
			for (Map<String, Object> apiGroup : apiGroupList)
			{
				List<ApiCache> apiCacheList = apiCacheMapper.getApiCacheByGroupID(projectID,
						new Integer(apiGroup.get("groupID").toString()));
				if (apiCacheList != null && !apiCacheList.isEmpty())
				{
					List<Map<String, Object>> apiList = new ArrayList<Map<String, Object>>();
					for (ApiCache apiCache : apiCacheList)
					{
						Map<String, Object> apiJson = JSONObject.parseObject((String) apiCache.getApiJson());
						Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
						baseInfo.put("starred", apiCache.getStarred());
						apiJson.put("baseInfo", baseInfo);
						apiList.add(apiJson);
					}
					apiGroup.put("apiList", apiList);
				}
				List<Map<String, Object>> data = apiGroupMapper.getChildGroupList(projectID,
						new Integer(apiGroup.get("groupID").toString()));
				List<Map<String, Object>> childGroupList = new ArrayList<Map<String, Object>>();
				if (data != null && !data.isEmpty())
				{
					for (Map<String, Object> childGroup : data)
					{
						Map<String, Object> group = new HashMap<String, Object>();
						group.put("groupName", childGroup.get("groupName"));
						group.put("isChild", childGroup.get("isChild"));
						List<Map<String, Object>> apiList = new ArrayList<Map<String, Object>>();
						List<ApiCache> apiCaches = apiCacheMapper.getApiCacheByGroupID(projectID,
								new Integer(childGroup.get("groupID").toString()));
						if (apiCaches != null && !apiCaches.isEmpty())
						{
							for (ApiCache apiCache : apiCaches)
							{
								Map<String, Object> apiJson = JSONObject.parseObject((String) apiCache.getApiJson());
								Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
								baseInfo.put("starred", apiCache.getStarred());
								apiJson.put("baseInfo", baseInfo);
								apiList.add(apiJson);
							}
							group.put("apiList", apiList);
						}
						List<Map<String, Object>> data1 = apiGroupMapper.getChildGroupList(projectID,
								new Integer(childGroup.get("groupID").toString()));
						List<Map<String, Object>> childGroupList1 = new ArrayList<Map<String, Object>>();
						if (data1 != null && !data1.isEmpty())
						{
							for (Map<String, Object> childGroup1 : data1)
							{
								Map<String, Object> group1 = new HashMap<String, Object>();
								group1.put("groupName", childGroup1.get("groupName"));
								group1.put("isChild", childGroup1.get("isChild"));
								List<Map<String, Object>> apiList1 = new ArrayList<Map<String, Object>>();
								List<ApiCache> apiCaches1 = apiCacheMapper.getApiCacheByGroupID(projectID,
										new Integer(childGroup1.get("groupID").toString()));
								if (apiCaches1 != null && !apiCaches1.isEmpty())
								{
									for (ApiCache apiCache : apiCaches1)
									{
										Map<String, Object> apiJson = JSONObject.parseObject((String) apiCache.getApiJson());
										Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
										baseInfo.put("starred", apiCache.getStarred());
										apiJson.put("baseInfo", baseInfo);
										apiList1.add(apiJson);
									}
									group1.put("apiList", apiList1);
								}
								childGroupList1.add(group1);
							}
							group.put("apiGroupChildList", childGroupList1);
						}
						childGroupList.add(group);
					}
					apiGroup.put("apiGroupChildList", childGroupList);
				}
			}
			result.put("apiGroupList", apiGroupList);
		}
		List<StatusCodeGroup> statusCodeGroupList = statusCodeGroupMapper.getParentList(projectID);
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		if (statusCodeGroupList != null && !statusCodeGroupList.isEmpty())
		{
			for (StatusCodeGroup statusCodeGroup : statusCodeGroupList)
			{
				Map<String, Object> data = new HashMap<String, Object>();
				List<StatusCode> statusCodeList = statusCodeGroupMapper.getStatusCodeData(statusCodeGroup.getGroupID());
				List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
				List<StatusCodeGroup> childrenGroup = statusCodeGroupMapper
						.getChildGroupData(statusCodeGroup.getGroupID(), projectID);
				if (childrenGroup != null && !childrenGroup.isEmpty())
				{
					for (StatusCodeGroup group : childrenGroup)
					{
						List<StatusCode> statusCodeData = this.statusCodeGroupMapper
								.getStatusCodeData(group.getGroupID());
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("statusCodeList", statusCodeData);
						tempMap.put("groupName", group.getGroupName());
						tempMap.put("isChild", group.getIsChild());
						List<Map<String, Object>> childrenList1 = new ArrayList<Map<String, Object>>();
						List<StatusCodeGroup> childrenGroup1 = statusCodeGroupMapper
								.getChildGroupData(statusCodeGroup.getGroupID(), projectID);
						if (childrenGroup1 != null && !childrenGroup1.isEmpty())
						{
							for (StatusCodeGroup group1 : childrenGroup1)
							{
								List<StatusCode> statusCodeData1 = this.statusCodeGroupMapper
										.getStatusCodeData(group1.getGroupID());
								Map<String, Object> tempMap1 = new HashMap<String, Object>();
								tempMap1.put("statusCodeList", statusCodeData1);
								tempMap1.put("groupName", group1.getGroupName());
								tempMap1.put("isChild", group1.getIsChild());
								childrenList1.add(tempMap1);
							}
						}
						tempMap.put("statusCodeGroupChildList", childrenGroup1);
						childrenList.add(tempMap);
					}
				}
				data.put("statusCodeList", statusCodeList);
				data.put("groupName", statusCodeGroup.getGroupName());
				data.put("isChild", statusCodeGroup.getIsChild());
				data.put("statusCodeGroupChildList", childrenList);
				groupList.add(data);
			}
			result.put("statusCodeGroupList", groupList);
		}
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		List<Env> envList = envMapper.getEnvList(projectID);
		if (envList != null && !envList.isEmpty())
		{
			for (Env env : envList)
			{
				EnvFrontUri envFrontUri = envFrontUriMapper.getEnvFrontUri(env.getEnvID());
				List<EnvHeader> envHeaderList = envHeaderMapper.getEnvHeaderList(env.getEnvID());
				List<EnvParam> envParamList = envParamMapper.getEnvParamList(env.getEnvID());
				List<EnvParamAdditional> envParamAdditional = envParamAdditionalMapper
						.getEnvParamAdditional(env.getEnvID());

				Map<String, Object> partialMap = new HashMap<String, Object>();
				partialMap.put("envID", env.getEnvID());
				partialMap.put("envName", env.getEnvName());
				partialMap.put("frontURI", envFrontUri);
				partialMap.put("headerList", envHeaderList);
				partialMap.put("paramList", envParamList);
				partialMap.put("additionalParamList", envParamAdditional);
				data.add(partialMap);
			}
			result.put("env", data);
		}
		List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
		List<DocumentGroup> documentGroupList = this.documentGroupMapper.getParentalGroup(projectID);
		if (documentGroupList != null && !documentGroupList.isEmpty())
		{
			for (DocumentGroup documentGroup : documentGroupList)
			{
				Map<String, Object> data11 = new HashMap<String, Object>();
				List<Document> pageList = this.documentGroupMapper.getDocumentData(projectID,
						documentGroup.getGroupID());
				List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
				List<DocumentGroup> childrenGroup = this.documentGroupMapper.getChildrenGroupData(projectID,
						documentGroup.getGroupID());
				if (childrenGroup != null && !childrenGroup.isEmpty())
				{
					for (DocumentGroup group : childrenGroup)
					{
						List<Document> documentData = this.documentGroupMapper.getDocumentData(projectID,
								group.getGroupID());
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("pageList", documentData);
						tempMap.put("groupName", group.getGroupName());
						tempMap.put("isChild", group.getIsChild());
						List<Map<String, Object>> childrenList1 = new ArrayList<Map<String, Object>>();
						List<DocumentGroup> childrenGroup1 = this.documentGroupMapper.getChildrenGroupData(projectID,
								documentGroup.getGroupID());
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
						tempMap.put("pageGroupChildList", childrenList1);
						childrenList1.add(tempMap);
					}
				}
				data11.put("pageList", pageList);
				data11.put("groupName", documentGroup.getGroupName());
				data11.put("isChild", documentGroup.getIsChild());
				data11.put("pageGroupChildList", childrenList);
				data1.add(data11);
			}
			result.put("pageGroupList", data1);
		}
		return result;
	}

}
