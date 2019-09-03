package com.eolinker.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.eolinker.pojo.AutomatedTestCaseGroup;
import com.eolinker.pojo.AutomatedTestCaseSingle;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.service.AutomatedTestCaseGroupService;

/**
 * 自动化测试用例分组[业务处理层]
 * 
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 *         eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 *         如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 *         eoLinker
 *         AMS开源版的开源协议遵循GPL V3，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 *         官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 *         使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 *         用户讨论QQ群：707530721
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "java.lang.Exception")
public class AutomatedTestCaseGroupServiceImpl implements AutomatedTestCaseGroupService
{

	@Autowired
	AutomatedTestCaseGroupMapper automatedTestCaseGroupMapper;
	@Autowired
	AutomatedTestCaseMapper automatedTestCaseMapper;
	@Autowired
	AutomatedTestCaseSingleMapper automatedTestCaseSingleMapper;
	@Autowired
	ProjectOperationLogMapper projectOperationLogMapper;
	@Autowired
	ProjectMapper projectMapper;

	/**
	 * 添加分组
	 */
	@Override
	public boolean addGroup(AutomatedTestCaseGroup automatedTestCaseGroup, Integer userID)
	{
		// TODO Auto-generated method stub
		if (automatedTestCaseGroup.getParentGroupID() == null)
		{
			automatedTestCaseGroup.setParentGroupID(0);
			automatedTestCaseGroup.setIsChild(0);
		}
		int result = automatedTestCaseGroupMapper.addGroup(automatedTestCaseGroup);
		if (result > 0)
		{
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(automatedTestCaseGroup.getProjectID(), nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(automatedTestCaseGroup.getProjectID());
			projectOperationLog.setOpDesc("添加自动化分组 '" + automatedTestCaseGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(automatedTestCaseGroup.getGroupID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 删除分组
	 */
	@Override
	public boolean deleteGroup(Integer projectID, Integer groupID, Integer userID)
	{
		// TODO Auto-generated method stub
		List<Integer> groupIDS = new ArrayList<Integer>();
		groupIDS.add(groupID);
		List<Map<String, Object>> childGroupList = automatedTestCaseGroupMapper.getChildGroupList(projectID, groupID);
		for (Map<String, Object> group : childGroupList)
		{
			groupIDS.add(new Integer(group.get("groupID").toString()));
			List<Map<String, Object>> childList = automatedTestCaseGroupMapper.getChildGroupList(projectID, new Integer(group.get("groupID").toString()));
			for (Map<String, Object> map : childList)
			{
				groupIDS.add(new Integer(map.get("groupID").toString()));
			}
		}
		automatedTestCaseSingleMapper.deleteTestCaseSingleByGroupID(groupIDS);
		automatedTestCaseMapper.deleteTestCaseByGroupID(groupIDS);
		AutomatedTestCaseGroup automatedTestCaseGroup = automatedTestCaseGroupMapper.getGroupByID(groupID);
		int result = automatedTestCaseGroupMapper.deleteGroup(groupIDS, projectID);
		if (result > 0)
		{
			// 添加操作记录
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("删除自动化用例分组  '" + automatedTestCaseGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
		{
			throw new RuntimeException("deleteGroup error");
		}
	}

	/**
	 * 获取分组列表
	 */
	@Override
	public List<Map<String, Object>> getGroupList(Integer projectID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> groupList = automatedTestCaseGroupMapper.getParentGroupList(projectID);
		for (Map<String, Object> automatedTestCaseGroup : groupList)
		{
			List<Map<String, Object>> childGroupList = automatedTestCaseGroupMapper.getChildGroupList(projectID,
					new Integer(automatedTestCaseGroup.get("groupID").toString()));
			for (Map<String, Object> group : childGroupList)
			{
				group.put("childGroupList", automatedTestCaseGroupMapper.getChildGroupList(projectID,
						new Integer(group.get("groupID").toString())));
			}
			automatedTestCaseGroup.put("childGroupList", childGroupList);
		}
		return groupList;
	}

	/**
	 * 获取分组排序
	 */
	@Override
	public String getGroupOrderList(Integer projectID)
	{
		// TODO Auto-generated method stub
		return automatedTestCaseGroupMapper.getGroupOrderList(projectID);
	}

	/**
	 * 修改分组
	 */
	@Override
	public boolean editGroup(AutomatedTestCaseGroup automatedTestCaseGroup, Integer userID)
	{
		// TODO Auto-generated method stub
		int result = automatedTestCaseGroupMapper.editGroup(automatedTestCaseGroup);
		if (result > 0)
		{
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(automatedTestCaseGroup.getProjectID(), nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(automatedTestCaseGroup.getProjectID());
			projectOperationLog.setOpDesc("修改自动化分组  '" + automatedTestCaseGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(automatedTestCaseGroup.getGroupID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 对分组进行排序
	 */
	@Override
	public boolean sortGroup(Integer projectID, Integer userID, String orderList)
	{
		// TODO Auto-generated method stub
		int result = automatedTestCaseGroupMapper.sortGroup(projectID, orderList);
		if (result > 0)
		{
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("修改项目自动化分组排序");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 导出分组
	 */
	@Override
	public Map<String, Object> exportGroup(Integer projectID, Integer userID, Integer groupID)
	{
		// TODO Auto-generated method stub
		AutomatedTestCaseGroup automatedTestCaseGroup = automatedTestCaseGroupMapper.getGroupByID(groupID);
		Map<String, Object> result = new HashMap<String, Object>();
		if (automatedTestCaseGroup != null)
		{
			result.put("groupName", automatedTestCaseGroup.getGroupName());
			List<Map<String, Object>> caseList = automatedTestCaseMapper.getTestCaseListByGroupID(projectID, groupID);
			if (caseList != null && !caseList.isEmpty())
			{
				for (Map<String, Object> testCase : caseList)
				{
					testCase.put("caseSingleList",
							automatedTestCaseSingleMapper.getSingleCaseList((Integer) testCase.get("caseID")));
				}
				result.put("caseList", caseList);
			}
			List<Map<String, Object>> data = automatedTestCaseGroupMapper.getChildGroupList(projectID,
					automatedTestCaseGroup.getGroupID());
			List<Map<String, Object>> childGroupList = new ArrayList<Map<String, Object>>();
			if (data != null && !data.isEmpty())
			{
				int i = 0;
				for (Map<String, Object> childGroup : data)
				{
					Map<String, Object> group = new HashMap<String, Object>();
					group.put("groupName", childGroup.get("groupName"));
					List<Map<String, Object>> caseList1 = automatedTestCaseMapper.getTestCaseListByGroupID(projectID,
							new Integer(childGroup.get("groupID").toString()));
					if (caseList1 != null && !caseList1.isEmpty())
					{
						for (Map<String, Object> testCase : caseList1)
						{
							testCase.put("caseSingleList",
									automatedTestCaseSingleMapper.getSingleCaseList((Integer) testCase.get("caseID")));
						}
						group.put("caseList", caseList1);
					}
					List<Map<String, Object>> data1 = automatedTestCaseGroupMapper.getChildGroupList(projectID,
							new Integer(childGroup.get("groupID").toString()));
					List<Map<String, Object>> childGroupList1 = new ArrayList<Map<String, Object>>();
					if (data1 != null && !data1.isEmpty())
					{
						int k = 0;
						for (Map<String, Object> childGroup1 : data1)
						{
							Map<String, Object> group1 = new HashMap<String, Object>();
							group1.put("groupName", childGroup1.get("groupName"));
							List<Map<String, Object>> caseList11 = automatedTestCaseMapper.getTestCaseListByGroupID(projectID,
									new Integer(childGroup1.get("groupID").toString()));
							if (caseList11 != null && !caseList11.isEmpty())
							{
								for (Map<String, Object> testCase : caseList11)
								{
									testCase.put("caseSingleList",
											automatedTestCaseSingleMapper.getSingleCaseList((Integer) testCase.get("caseID")));
								}
								group1.put("caseList", caseList11);
							}
							childGroupList1.add(k, group1);
							k++;
						}
						group.put("childGroupList", childGroupList1);
					}
					childGroupList.add(i, group);
					i++;
				}
				result.put("childGroupList", childGroupList);
			}
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("导出用例分组'" + automatedTestCaseGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
		}
		return result;
	}

	/**
	 * 导入分组
	 */
	@Override
	public boolean importGroup(Integer projectID, Integer userID, String data)
	{
		// TODO Auto-generated method stub
		JSONObject group = JSONObject.parseObject(data);
		if (data != null && !data.isEmpty())
		{
			AutomatedTestCaseGroup automatedTestCaseGroup = new AutomatedTestCaseGroup();
			automatedTestCaseGroup.setGroupName(group.getString("groupName"));
			automatedTestCaseGroup.setIsChild(0);
			automatedTestCaseGroup.setProjectID(projectID);
			automatedTestCaseGroup.setParentGroupID(0);
			if (automatedTestCaseGroupMapper.addGroup(automatedTestCaseGroup) < 1)
				throw new RuntimeException("addGroup error");
			JSONArray caseList = group.getJSONArray("caseList");
			if (caseList != null && !caseList.isEmpty())
			{
				for (Iterator<Object> iterator = caseList.iterator(); iterator.hasNext();)
				{
					JSONObject testCase = (JSONObject) iterator.next();
					AutomatedTestCase automatedTestCase = new AutomatedTestCase();
					automatedTestCase.setProjectID(projectID);
					automatedTestCase.setUserID(userID);
					automatedTestCase.setCaseName(testCase.getString("caseName"));
					automatedTestCase.setCaseDesc(testCase.getString("caseDesc"));
					Date date = new Date();
					Timestamp updateTime = new Timestamp(date.getTime());
					automatedTestCase.setCreateTime(updateTime);
					automatedTestCase.setUpdateTime(updateTime);
					automatedTestCase.setCaseType(testCase.getInteger("caseType"));
					automatedTestCase.setGroupID(automatedTestCaseGroup.getGroupID());
					if (automatedTestCaseMapper.addTestCase(automatedTestCase) < 1)
						throw new RuntimeException("addTestCase error");
					JSONArray singleCaseList = testCase.getJSONArray("caseSingleList");
					if (singleCaseList != null && !singleCaseList.isEmpty())
					{
						for (Iterator<Object> iterator1 = singleCaseList.iterator(); iterator1.hasNext();)
						{
							JSONObject singleCase = (JSONObject) iterator1.next();
							Pattern pattern = Pattern.compile("<response\\[(\\d+)\\]");
							Matcher matcher = pattern.matcher(singleCase.getString("caseData"));
							String caseData = singleCase.getString("caseData");
							int i = 0;
							while (matcher.find())
							{
								Pattern pattern1 = Pattern.compile("\\d+");
								Matcher matcher1 = pattern1.matcher(matcher.group(i));
								if (matcher1.find())
								{
									for (Iterator<Object> iterator11 = singleCaseList.iterator(); iterator11.hasNext();)
									{
										JSONObject singleCase1 = (JSONObject) iterator11.next();
										if (singleCase1.get("connID") != null && singleCase1.getString("connID").equals(matcher1.group()))
										{
											Integer connID = automatedTestCaseSingleMapper.getConnID(
													singleCase1.getString("apiName"), singleCase1.getString("apiURI"),
													automatedTestCase.getCaseID());
											caseData = caseData.replace("<response[" + matcher1.group(),
													"<response[" + connID);
										}
									}
								}
								i++;
							}
							AutomatedTestCaseSingle automatedTestCaseSingle = new AutomatedTestCaseSingle();
							automatedTestCaseSingle.setCaseID(automatedTestCase.getCaseID());
							automatedTestCaseSingle.setCaseData(caseData);
							automatedTestCaseSingle.setCaseCode(singleCase.getString("caseCode"));
							automatedTestCaseSingle.setStatusCode(singleCase.getString("statusCode"));
							automatedTestCaseSingle.setMatchType(singleCase.getInteger("matchType"));
							automatedTestCaseSingle.setMatchRule(singleCase.getString("matchRule"));
							automatedTestCaseSingle.setApiName(singleCase.getString("apiName"));
							automatedTestCaseSingle.setApiURI(singleCase.getString("apiURI"));
							automatedTestCaseSingle.setApiRequestType(singleCase.getInteger("apiRequestType"));
							automatedTestCaseSingle.setOrderNumber(singleCase.getInteger("orderNumber"));
							if (automatedTestCaseSingleMapper.addSingleTestCase(automatedTestCaseSingle) < 1)
								throw new RuntimeException("addSingleTestCase error");
						}
					}
				}
			}
			JSONArray childGroupList = group.getJSONArray("childGroupList");
			if (childGroupList != null && !childGroupList.isEmpty())
			{
				for (Iterator<Object> iterator = childGroupList.iterator(); iterator.hasNext();)
				{
					JSONObject childGroup = (JSONObject) iterator.next();
					AutomatedTestCaseGroup automatedTestCaseGroup1 = new AutomatedTestCaseGroup();
					automatedTestCaseGroup1.setGroupName(childGroup.getString("groupName"));
					automatedTestCaseGroup1.setIsChild(1);
					automatedTestCaseGroup1.setProjectID(projectID);
					automatedTestCaseGroup1.setParentGroupID(automatedTestCaseGroup.getGroupID());
					if (automatedTestCaseGroupMapper.addGroup(automatedTestCaseGroup1) < 1)
						throw new RuntimeException("addGroup error");
					JSONArray caseList1 = childGroup.getJSONArray("caseList");
					if (caseList1 != null && !caseList1.isEmpty())
					{
						for (Iterator<Object> iterator1 = caseList1.iterator(); iterator1.hasNext();)
						{
							JSONObject testCase = (JSONObject) iterator1.next();
							AutomatedTestCase automatedTestCase = new AutomatedTestCase();
							automatedTestCase.setProjectID(projectID);
							automatedTestCase.setUserID(userID);
							automatedTestCase.setCaseName(testCase.getString("caseName"));
							automatedTestCase.setCaseDesc(testCase.getString("caseDesc"));
							Date date = new Date();
							Timestamp updateTime = new Timestamp(date.getTime());
							automatedTestCase.setCreateTime(updateTime);
							automatedTestCase.setUpdateTime(updateTime);
							automatedTestCase.setCaseType(testCase.getInteger("caseType"));
							automatedTestCase.setGroupID(automatedTestCaseGroup1.getGroupID());
							if (automatedTestCaseMapper.addTestCase(automatedTestCase) < 1)
								throw new RuntimeException("addTestCase error");
							JSONArray singleCaseList = testCase.getJSONArray("caseSingleList");
							if (singleCaseList != null && !singleCaseList.isEmpty())
							{
								for (Iterator<Object> iterator11 = singleCaseList.iterator(); iterator11.hasNext();)
								{
									JSONObject singleCase = (JSONObject) iterator11.next();
									Pattern pattern = Pattern.compile("<response\\[(\\d+)\\]");
									Matcher matcher = pattern.matcher(singleCase.getString("caseData"));
									String caseData = singleCase.getString("caseData");
									while (matcher.find())
									{
										Pattern pattern1 = Pattern.compile("\\d+");
										Matcher matcher1 = pattern1.matcher(matcher.group());
										if (matcher1.find())
										{
											for (Iterator<Object> iterator111 = singleCaseList.iterator(); iterator111
													.hasNext();)
											{
												JSONObject singleCase1 = (JSONObject) iterator111.next();
												if (singleCase1.get("connID") != null && singleCase1.getString("connID").equals(matcher1.group()))
												{
													Integer connID = automatedTestCaseSingleMapper.getConnID(
															singleCase1.getString("apiName"),
															singleCase1.getString("apiURI"),
															automatedTestCase.getCaseID());
													caseData = caseData.replace("<response[" + matcher1.group(),
															"<response[" + connID);
												}
											}
										}
									}
									AutomatedTestCaseSingle automatedTestCaseSingle = new AutomatedTestCaseSingle();
									automatedTestCaseSingle.setCaseID(automatedTestCase.getCaseID());
									automatedTestCaseSingle.setCaseData(singleCase.getString("caseData"));
									automatedTestCaseSingle.setCaseCode(singleCase.getString("caseCode"));
									automatedTestCaseSingle.setStatusCode(singleCase.getString("statusCode"));
									automatedTestCaseSingle.setMatchType(singleCase.getInteger("matchType"));
									automatedTestCaseSingle.setMatchRule(singleCase.getString("matchRule"));
									automatedTestCaseSingle.setApiName(singleCase.getString("apiName"));
									automatedTestCaseSingle.setApiURI(singleCase.getString("apiURI"));
									automatedTestCaseSingle.setApiRequestType(singleCase.getInteger("apiRequestType"));
									automatedTestCaseSingle.setOrderNumber(singleCase.getInteger("orderNumber"));
									if (automatedTestCaseSingleMapper.addSingleTestCase(automatedTestCaseSingle) < 1)
										throw new RuntimeException("addSingleTestCase error");
								}
							}
						}
					}
					JSONArray childGroupList1 = childGroup.getJSONArray("childGroupList");
					if (childGroupList1 != null && !childGroupList1.isEmpty())
					{
						for (Iterator<Object> iterator1 = childGroupList1.iterator(); iterator1.hasNext();)
						{
							JSONObject childGroup1 = (JSONObject) iterator1.next();
							AutomatedTestCaseGroup automatedTestCaseGroup11 = new AutomatedTestCaseGroup();
							automatedTestCaseGroup11.setGroupName(childGroup1.getString("groupName"));
							automatedTestCaseGroup11.setIsChild(2);
							automatedTestCaseGroup11.setProjectID(projectID);
							automatedTestCaseGroup11.setParentGroupID(automatedTestCaseGroup1.getGroupID());
							if (automatedTestCaseGroupMapper.addGroup(automatedTestCaseGroup11) < 1)
								throw new RuntimeException("addGroup error");
							JSONArray caseList11 = childGroup1.getJSONArray("caseList");
							if (caseList11 != null && !caseList11.isEmpty())
							{
								for (Iterator<Object> iterator11 = caseList11.iterator(); iterator11.hasNext();)
								{
									JSONObject testCase = (JSONObject) iterator11.next();
									AutomatedTestCase automatedTestCase = new AutomatedTestCase();
									automatedTestCase.setProjectID(projectID);
									automatedTestCase.setUserID(userID);
									automatedTestCase.setCaseName(testCase.getString("caseName"));
									automatedTestCase.setCaseDesc(testCase.getString("caseDesc"));
									Date date = new Date();
									Timestamp updateTime = new Timestamp(date.getTime());
									automatedTestCase.setCreateTime(updateTime);
									automatedTestCase.setUpdateTime(updateTime);
									automatedTestCase.setCaseType(testCase.getInteger("caseType"));
									automatedTestCase.setGroupID(automatedTestCaseGroup11.getGroupID());
									if (automatedTestCaseMapper.addTestCase(automatedTestCase) < 1)
										throw new RuntimeException("addTestCase error");
									JSONArray singleCaseList = testCase.getJSONArray("caseSingleList");
									if (singleCaseList != null && !singleCaseList.isEmpty())
									{
										for (Iterator<Object> iterator111 = singleCaseList.iterator(); iterator111.hasNext();)
										{
											JSONObject singleCase = (JSONObject) iterator111.next();
											Pattern pattern = Pattern.compile("<response\\[(\\d+)\\]");
											Matcher matcher = pattern.matcher(singleCase.getString("caseData"));
											String caseData = singleCase.getString("caseData");
											while (matcher.find())
											{
												Pattern pattern1 = Pattern.compile("\\d+");
												Matcher matcher1 = pattern1.matcher(matcher.group());
												if (matcher1.find())
												{
													for (Iterator<Object> iterator1111 = singleCaseList.iterator(); iterator1111
															.hasNext();)
													{
														JSONObject singleCase1 = (JSONObject) iterator1111.next();
														if (singleCase1.get("connID") != null && singleCase1.getString("connID").equals(matcher1.group()))
														{
															Integer connID = automatedTestCaseSingleMapper.getConnID(
																	singleCase1.getString("apiName"),
																	singleCase1.getString("apiURI"),
																	automatedTestCase.getCaseID());
															caseData = caseData.replace("<response[" + matcher1.group(),
																	"<response[" + connID);
														}
													}
												}
											}
											AutomatedTestCaseSingle automatedTestCaseSingle = new AutomatedTestCaseSingle();
											automatedTestCaseSingle.setCaseID(automatedTestCase.getCaseID());
											automatedTestCaseSingle.setCaseData(singleCase.getString("caseData"));
											automatedTestCaseSingle.setCaseCode(singleCase.getString("caseCode"));
											automatedTestCaseSingle.setStatusCode(singleCase.getString("statusCode"));
											automatedTestCaseSingle.setMatchType(singleCase.getInteger("matchType"));
											automatedTestCaseSingle.setMatchRule(singleCase.getString("matchRule"));
											automatedTestCaseSingle.setApiName(singleCase.getString("apiName"));
											automatedTestCaseSingle.setApiURI(singleCase.getString("apiURI"));
											automatedTestCaseSingle.setApiRequestType(singleCase.getInteger("apiRequestType"));
											automatedTestCaseSingle.setOrderNumber(singleCase.getInteger("orderNumber"));
											if (automatedTestCaseSingleMapper.addSingleTestCase(automatedTestCaseSingle) < 1)
												throw new RuntimeException("addSingleTestCase error");
										}
									}
								}
							}
						}
					}
				}
			}
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("导入用例分组'" + automatedTestCaseGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE_GROUP);
			projectOperationLog.setOpTargetID(automatedTestCaseGroup.getGroupID());
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

}
