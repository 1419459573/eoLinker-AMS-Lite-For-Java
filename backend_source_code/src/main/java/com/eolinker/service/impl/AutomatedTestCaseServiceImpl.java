package com.eolinker.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.mapper.AutomatedTestCaseGroupMapper;
import com.eolinker.mapper.AutomatedTestCaseMapper;
import com.eolinker.mapper.AutomatedTestCaseSingleMapper;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.pojo.AutomatedTestCase;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.service.AutomatedTestCaseService;
/**
 * 自动化测试用例[业务处理层]
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
public class AutomatedTestCaseServiceImpl implements AutomatedTestCaseService
{
	@Autowired
	private AutomatedTestCaseGroupMapper automtedTestCaseGroupMapper;
	@Autowired
	private AutomatedTestCaseSingleMapper automatedTestCaseSingleMapper;
	@Autowired
	private AutomatedTestCaseMapper automatedTestCaseMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 检查用户对分组是否有操作权限
	 */
	@Override
	public Integer checkGroupPermission(Integer groupID, Integer userID)
	{
		// TODO Auto-generated method stub
		return automtedTestCaseGroupMapper.checkGroupPermission(groupID, userID);
	}

	/**
	 * 添加测试用例
	 */
	@Override
	public Integer addTestCase(AutomatedTestCase automatedTestCase)
	{
		// TODO Auto-generated method stub
		Date date = new Date();
		Timestamp nowTime = new Timestamp(date.getTime());
		automatedTestCase.setCreateTime(nowTime);
		automatedTestCase.setUpdateTime(nowTime);
		if (automatedTestCaseMapper.addTestCase(automatedTestCase) > 0)
		{
			// 添加操作记录
			projectMapper.updateProjectUpdateTime(automatedTestCase.getProjectID(), nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(automatedTestCase.getProjectID());
			projectOperationLog.setOpDesc("新增自动化用例'" + automatedTestCase.getCaseName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE);
			projectOperationLog.setOpTargetID(automatedTestCase.getCaseID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(automatedTestCase.getUserID());
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return automatedTestCase.getCaseID();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 修改用例
	 */
	@Override
	public boolean editTestCase(AutomatedTestCase automatedTestCase)
	{
		// TODO Auto-generated method stub
		Date date = new Date();
		Timestamp nowTime = new Timestamp(date.getTime());
		automatedTestCase.setUpdateTime(nowTime);
		if (automatedTestCaseMapper.editTestCase(automatedTestCase) > 0)
		{
			// 添加操作记录
			projectMapper.updateProjectUpdateTime(automatedTestCase.getProjectID(), nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(automatedTestCase.getProjectID());
			projectOperationLog.setOpDesc("修改自动化用例'" + automatedTestCase.getCaseName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE);
			projectOperationLog.setOpTargetID(automatedTestCase.getCaseID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(automatedTestCase.getUserID());
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 获取用例列表
	 */
	@Override
	public List<Map<String, Object>> getTestCaseList(Integer projectID, Integer groupID)
	{
		// TODO Auto-generated method stub
		List<Integer> groupIDS = new ArrayList<Integer>();
		List<Map<String, Object>> childGroupList = new ArrayList<>();
		if(groupID != null && groupID > 0)
		{
			groupIDS.add(groupID);
			childGroupList = automtedTestCaseGroupMapper.getChildGroupList(projectID,groupID);
		}
		if(childGroupList != null && !childGroupList.isEmpty())
		{
			for (Map<String, Object> group : childGroupList)
			{
				groupIDS.add(new Integer(group.get("groupID").toString()));
				List<Map<String, Object>> childGroupList1 = automtedTestCaseGroupMapper.getChildGroupList(projectID, new Integer(group.get("groupID").toString()));
				if(childGroupList1 != null && !childGroupList1.isEmpty())
				{
					for (Map<String, Object> group1 : childGroupList1)
					{
						groupIDS.add(new Integer(group1.get("groupID").toString()));
					}
				}
			}
		}
		List<Map<String, Object>> caseList = automatedTestCaseMapper.getTestCaseList(projectID, groupIDS);
		if (caseList != null && !caseList.isEmpty())
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> caseData : caseList)
			{
				String updateTime = dateFormat.format(caseData.get("updateTime"));
				caseData.put("updateTime", updateTime);
				Integer topParentGroupID = automtedTestCaseGroupMapper.getParentGroupID(new Integer(caseData.get("parentGroupID").toString()));
				topParentGroupID = topParentGroupID != null ? topParentGroupID : 0;
				caseData.put("topParentGroupID", topParentGroupID);
			}
		}
		return caseList;
	}

	/**
	 * 获取用例详情
	 */
	@Override
	public Map<String, Object> getTestCaseInfo(Integer projectID, Integer caseID)
	{
		// TODO Auto-generated method stub
		return automatedTestCaseMapper.getTestCaseInfo(projectID, caseID);
	}

	/**
	 * 删除用例
	 */
	@Override
	public boolean deleteTestCase(Integer projectID, String caseID, Integer userID)
	{
		// TODO Auto-generated method stub
		String caseName = "";
		Integer result = 0;
		JSONArray jsonArray = JSONArray.parseArray(caseID);
		List<Integer> caseIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				caseIDs.add((Integer) iterator.next());
			}
			caseName = automatedTestCaseMapper.getCaseNameByIDs(caseIDs);
			result = automatedTestCaseMapper.deleteTestCase(projectID, caseIDs);
		}
		if (result > 0)
		{
			automatedTestCaseSingleMapper.batchDeleteSingle(caseIDs);
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("删除用例：'" + caseName + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;

	}

	/**
	 * 搜索用例
	 */
	@Override
	public List<Map<String, Object>> searchTestCase(Integer projectID, String tips)
	{
		// TODO Auto-generated method stub
		return automatedTestCaseMapper.searchTestCase(projectID, tips);
	}

	/**
	 * 获取用例数据
	 */
	@Override
	public List<Map<String, Object>> getTestCaseDataList(Integer projectID, Integer groupID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = automatedTestCaseMapper.getTestCaseDataList(projectID, groupID);
		if (result != null && !result.isEmpty())
		{
			for (Map<String, Object> testCase : result)
			{
				List<Map<String, Object>> singleCaseList = automatedTestCaseSingleMapper
						.getSingleCaseList((Integer) testCase.get("caseID"));
				if (singleCaseList != null && !singleCaseList.isEmpty())
				{
					for (Map<String, Object> singleCase : singleCaseList)
					{
						if ((int) singleCase.get("matchType") == 2 && singleCase.get("matchRule") != null)
						{
							singleCase.put("matchRule", JSONObject.parse((String) singleCase.get("matchRule")));
						}
					}
				}
				testCase.put("singleCaseList", singleCaseList);
			}
		}
		return result;
	}

	/**
	 * 根据用例ID获取项目ID
	 */
	@Override
	public Integer getProjectIDByCaseID(Integer caseID)
	{
		// TODO Auto-generated method stub
		return automatedTestCaseMapper.getProjectIDByCaseID(caseID);
	}

}
