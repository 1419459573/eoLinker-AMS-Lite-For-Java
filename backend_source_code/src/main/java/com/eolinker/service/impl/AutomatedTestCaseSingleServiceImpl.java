package com.eolinker.service.impl;

import java.sql.Timestamp;
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
import com.eolinker.mapper.ApiMapper;
import com.eolinker.mapper.AutomatedTestCaseSingleMapper;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.pojo.AutomatedTestCaseSingle;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.service.AutomatedTestCaseSingleService;
/**
 * 自动化测试用例单例[业务处理层]
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
public class AutomatedTestCaseSingleServiceImpl implements AutomatedTestCaseSingleService
{

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;
	@Autowired
	private AutomatedTestCaseSingleMapper automatedTestCaseSingleMapper;
	@Autowired
	private ApiMapper apiMapper;

	/**
	 * 新增单例
	 */
	@Override
	public Integer addSingleTestCase(AutomatedTestCaseSingle automatedTestCaseSingle,
			Integer projectID, Integer userID)
	{
		// TODO Auto-generated method stub
		if(automatedTestCaseSingle.getOrderNumber() != null && automatedTestCaseSingle.getOrderNumber() > 0)
		{
			automatedTestCaseSingleMapper.updateOrderNumber(automatedTestCaseSingle.getCaseID(), automatedTestCaseSingle.getOrderNumber());
		}
		else
		{
			Integer max_order_number = automatedTestCaseSingleMapper.getMaxOrderNumber(automatedTestCaseSingle.getCaseID());
			automatedTestCaseSingle.setOrderNumber(max_order_number != null ?  max_order_number + 1 : 1);
		}
		if (automatedTestCaseSingleMapper.addSingleTestCase(automatedTestCaseSingle) > 0)
		{
			// 添加操作记录
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("新增自动化用例单例'" + automatedTestCaseSingle.getApiName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE);
			projectOperationLog.setOpTargetID(automatedTestCaseSingle.getConnID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return automatedTestCaseSingle.getConnID();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 修改单例
	 */
	@Override
	public boolean editSingleTestCase(AutomatedTestCaseSingle automatedTestCaseSingle, Integer projectID,
			Integer userID)
	{
		// TODO Auto-generated method stub
		if (automatedTestCaseSingleMapper.editSingleTestCase(automatedTestCaseSingle) > 0)
		{
			// 添加操作记录
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("修改自动化用例单例'" + automatedTestCaseSingle.getApiName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE);
			projectOperationLog.setOpTargetID(automatedTestCaseSingle.getConnID());
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取单例列表
	 */
	@Override
	public List<Map<String, Object>> getSingleTestCaseList(Integer projectID, Integer caseID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> singleCaseList = new ArrayList<Map<String, Object>>();
		if (caseID != null && caseID > 0)
		{
			singleCaseList = automatedTestCaseSingleMapper.getSingleCaseList(caseID);
		}
		else
		{
			singleCaseList = automatedTestCaseSingleMapper.getAllSingleCase(projectID);
		}
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
		return singleCaseList;
	}

	/**
	 * 获取单例详情
	 */
	@Override
	public Map<String, Object> getSingleTestCaseInfo(Integer projectID, Integer connID)
	{
		// TODO Auto-generated method stub
		Map<String, Object> singleCase = automatedTestCaseSingleMapper.getSingleTestCaseInfo(projectID, connID);
		if (singleCase != null && !singleCase.isEmpty())
		{
			if ((int) singleCase.get("matchType") == 2 && singleCase.get("matchRule") != null)
			{
				singleCase.put("matchRule", JSONObject.parse((String) singleCase.get("matchRule")));
			}
		}
		return singleCase;
	}

	/**
	 * 删除单例
	 */
	@Override
	public boolean deleteSingleTestCase(Integer projectID, String connID, Integer userID)
	{
		// TODO Auto-generated method stub
		String apiName = "";
		JSONArray jsonArray = JSONArray.parseArray(connID);
		List<Integer> connIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				connIDs.add((Integer) iterator.next());
			}
			apiName = automatedTestCaseSingleMapper.getApiNameByIDs(connIDs);
			if (automatedTestCaseSingleMapper.deleteSingleTestCase(projectID, connIDs) < 1)
				throw new RuntimeException("deleteSingleTestCase");
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("删除自动化用例单例'" + apiName + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_AUTOMATED_TEST_CASE);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取接口列表
	 */
	@Override
	public List<Map<String, Object>> getApiList(Integer projectID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> apiList = apiMapper.getAllApi(projectID);
		for (Map<String, Object> api : apiList)
		{
			JSONObject apiJson = JSONObject.parseObject((String) api.get("apiJson"));
			api.put("headerInfo", apiJson.get("headerInfo"));
			api.put("requestInfo", apiJson.get("requestInfo"));
			api.put("resultInfo", apiJson.get("resultInfo"));
			api.remove("apiJson");
		}
		return apiList;
	}

}
