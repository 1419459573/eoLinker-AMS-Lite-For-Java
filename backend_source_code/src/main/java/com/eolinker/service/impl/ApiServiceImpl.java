package com.eolinker.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.mapper.*;
import com.eolinker.mapper.ApiHistoryMapper;
import com.eolinker.pojo.*;
import com.eolinker.service.ApiService;
import com.sun.org.apache.bcel.internal.generic.NEW;
/**
 * 接口业务处理层
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
public class ApiServiceImpl implements ApiService
{

	@Autowired
	ApiMapper apiMapper;
	@Autowired
	ApiGroupMapper apiGroupMapper;
	@Autowired
	ProjectMapper projectMapper;
	@Autowired
	ApiCacheMapper apiCacheMapper;
	@Autowired
	ProjectOperationLogMapper projectOperationLogMapper;
	@Autowired
	ApiHistoryMapper apiHistoryMapper;
	@Autowired
	TestHistoryMapper testHistoryMapper;

	/**
	 * 新增接口
	 */
	@Override
	public Integer addApi(Api api, String apiHeader, String apiRequestParam, String apiResultParam)
	{
		// TODO Auto-generated method stub
		if (api.getApiSuccessMock() == null)
		{
			api.setApiSuccessMock("");
		}
		if (api.getApiFailureMock() == null)
		{
			api.setApiFailureMock("");
		}
		if (api.getApiRequestRaw() == null)
		{
			api.setApiRequestRaw("");
		}
		if (api.getApiNote() == null || api.getApiNote().equals("&lt;p&gt;&lt;br&gt;&lt;/p&gt;"))
		{
			api.setApiNote("");
		}
		if (api.getApiNoteRaw() == null)
		{
			api.setApiNoteRaw("");
		}
		if (api.getStarred() == null)
		{
			api.setStarred(0);
		}
		if (api.getApiNoteType() == null)
		{
			api.setApiNoteType(0);
		}
		if (api.getApiRequestParamType() == null)
		{
			api.setApiRequestParamType(0);
		}
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		api.setApiUpdateTime(updateTime);
		projectMapper.updateProjectUpdateTime(api.getProjectID(), updateTime);
		Map<String, Object> cache = new HashMap<String, Object>();
		Map<String, Object> mockInfo = new HashMap<String, Object>();
		cache.put("baseInfo", api);
		cache.put("headerInfo", JSONArray.parseArray(apiHeader));
		mockInfo.put("mockResult", api.getMockResult());
		mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
		mockInfo.put("mockConfig", api.getMockConfig());
		cache.put("mockInfo", mockInfo);
		cache.put("requestInfo", JSONArray.parseArray(apiRequestParam));
		cache.put("resultInfo", JSONArray.parseArray(apiResultParam));
		Integer result = apiMapper.addApi(api);
		if (result > 0)
		{
			ApiCache apiCache = new ApiCache();
			apiCache.setApiID(api.getApiID());
			apiCache.setApiJson(JSON.toJSONString(cache));
			apiCache.setGroupID(api.getGroupID());
			apiCache.setProjectID(api.getProjectID());
			apiCache.setStarred(api.getStarred());
			apiCache.setUpdateUserID(api.getUpdateUserID());
			if (apiCacheMapper.addApiCache(apiCache) < 1)
				throw new RuntimeException("addApiCache error");
			JSONArray headerList = JSONArray.parseArray(apiHeader);
			if (headerList != null && !headerList.isEmpty())
			{
				for (Iterator<Object> iterator = headerList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiHeader header = new ApiHeader();
					header.setHeaderName(jsonObject.getString("headerName"));
					header.setHeaderValue(jsonObject.getString("headerValue"));
					header.setApiID(api.getApiID());
					if (apiMapper.addApiHeader(header) < 1)
						throw new RuntimeException("addApiHeader error");
				}
			}
			JSONArray requestParamList = JSONArray.parseArray(apiRequestParam);
			if (requestParamList != null && !requestParamList.isEmpty())
			{
				for (Iterator<Object> iterator = requestParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiRequestParam requestParam = new ApiRequestParam();
					requestParam.setApiID(api.getApiID());
					requestParam.setParamName(jsonObject.getString("paramName"));
					requestParam.setParamKey(jsonObject.getString("paramKey"));
					requestParam.setParamValue(jsonObject.getString("paramValue"));
					requestParam.setParamType(jsonObject.getInteger("paramType"));
					requestParam.setParamLimit(jsonObject.getString("paramLimit"));
					requestParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addRequestParam(requestParam) < 1)
						throw new RuntimeException("addRequestParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiRequestValue apiRequestValue = new ApiRequestValue();
						apiRequestValue.setParamID(requestParam.getParamID());
						apiRequestValue.setValue(paramValue.getString("value"));
						apiRequestValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addRequestValue(apiRequestValue) < 1)
							throw new RuntimeException("apiRequestValue error");
					}
				}
			}
			JSONArray resultParamList = JSONArray.parseArray(apiResultParam);
			if (resultParamList != null && !resultParamList.isEmpty())
			{
				for (Iterator<Object> iterator = resultParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiResultParam resultParam = new ApiResultParam();
					resultParam.setApiID(api.getApiID());
					resultParam.setParamName(jsonObject.getString("paramName"));
					resultParam.setParamKey(jsonObject.getString("paramKey"));
					resultParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addResultParam(resultParam) < 1)
						throw new RuntimeException("addResultParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiResultValue apiResultValue = new ApiResultValue();
						apiResultValue.setParamID(resultParam.getParamID());
						apiResultValue.setValue(paramValue.getString("value"));
						apiResultValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addResultValue(apiResultValue) < 1)
							throw new RuntimeException("addResultValue error");
					}
				}
			}
			ApiHistory apiHistory = new ApiHistory();
			apiHistory.setApiID(api.getApiID());
			apiHistory.setGroupID(api.getGroupID());
			apiHistory.setHistoryJson(JSON.toJSONString(cache));
			apiHistory.setIsNow(1);
			apiHistory.setProjectID(api.getProjectID());
			apiHistory.setUpdateDesc("创建API");
			apiHistory.setUpdateTime(updateTime);
			apiHistory.setUpdateUserID(api.getUpdateUserID());
			if (apiHistoryMapper.addApiHistory(apiHistory) < 1)
				throw new RuntimeException("addHistory error");
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(api.getProjectID());
			projectOperationLog.setOpDesc("添加接口  '" + api.getApiName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(api.getApiID());
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(api.getUpdateUserID());
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return api.getApiID();

		}
		return 0;
	}

	/**
	 * 修改接口
	 */
	@Override
	public boolean editApi(Api api, String apiHeader, String apiRequestParam, String apiResultParam, String updateDesc)
	{
		// TODO Auto-generated method stub
		if (api.getApiSuccessMock() == null)
		{
			api.setApiSuccessMock("");
		}
		if (api.getApiFailureMock() == null)
		{
			api.setApiFailureMock("");
		}
		if (api.getApiRequestRaw() == null)
		{
			api.setApiRequestRaw("");
		}
		if (api.getApiNote() == null || api.getApiNote().equals("&lt;p&gt;&lt;br&gt;&lt;/p&gt;"))
		{
			api.setApiNote("");
		}
		if (api.getApiNoteRaw() == null)
		{
			api.setApiNoteRaw("");
		}
		if (api.getStarred() == null)
		{
			api.setStarred(0);
		}
		if (api.getApiNoteType() == null)
		{
			api.setApiNoteType(0);
		}
		if (api.getApiRequestParamType() == null)
		{
			api.setApiRequestParamType(0);
		}
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		api.setApiUpdateTime(updateTime);
		projectMapper.updateProjectUpdateTime(api.getProjectID(), updateTime);
		Map<String, Object> cache = new HashMap<String, Object>();
		Map<String, Object> mockInfo = new HashMap<String, Object>();
		cache.put("baseInfo", api);
		cache.put("headerInfo", JSONArray.parseArray(apiHeader));
		mockInfo.put("mockResult", api.getMockResult());
		mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
		mockInfo.put("mockConfig", api.getMockConfig());
		cache.put("mockInfo", mockInfo);
		cache.put("requestInfo", JSONArray.parseArray(apiRequestParam));
		cache.put("resultInfo", JSONArray.parseArray(apiResultParam));
		Integer result = apiMapper.updateApi(api);
		if (result > 0)
		{
			apiMapper.deleteApiHeader(api.getApiID());
			apiMapper.deleteRequestParam(api.getApiID());
			apiMapper.deleteResultParam(api.getApiID());
			ApiCache apiCache = new ApiCache();
			apiCache.setApiID(api.getApiID());
			apiCache.setApiJson(JSON.toJSONString(cache));
			apiCache.setGroupID(api.getGroupID());
			apiCache.setProjectID(api.getProjectID());
			apiCache.setStarred(api.getStarred());
			apiCache.setUpdateUserID(api.getUpdateUserID());
			if (apiCacheMapper.updateApiCache(apiCache) < 1)
				throw new RuntimeException("updateApiCache error");
			JSONArray headerList = JSONArray.parseArray(apiHeader);
			if (headerList != null && !headerList.isEmpty())
			{
				for (Iterator<Object> iterator = headerList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiHeader header = new ApiHeader();
					header.setHeaderName(jsonObject.getString("headerName"));
					header.setHeaderValue(jsonObject.getString("headerValue"));
					header.setApiID(api.getApiID());
					if (apiMapper.addApiHeader(header) < 1)
						throw new RuntimeException("addApiHeader error");
				}
			}
			JSONArray requestParamList = JSONArray.parseArray(apiRequestParam);
			if (requestParamList != null && !requestParamList.isEmpty())
			{
				for (Iterator<Object> iterator = requestParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiRequestParam requestParam = new ApiRequestParam();
					requestParam.setApiID(api.getApiID());
					requestParam.setParamName(jsonObject.getString("paramName"));
					requestParam.setParamKey(jsonObject.getString("paramKey"));
					requestParam.setParamValue(jsonObject.getString("paramValue"));
					requestParam.setParamType(jsonObject.getInteger("paramType"));
					requestParam.setParamLimit(jsonObject.getString("paramLimit"));
					requestParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addRequestParam(requestParam) < 0)
						throw new RuntimeException("addRequestParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiRequestValue apiRequestValue = new ApiRequestValue();
						apiRequestValue.setParamID(requestParam.getParamID());
						apiRequestValue.setValue(paramValue.getString("value"));
						apiRequestValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addRequestValue(apiRequestValue) < 1)
							throw new RuntimeException("apiRequestValue error");
					}
				}
			}

			JSONArray resultParamList = JSONArray.parseArray(apiResultParam);
			if (resultParamList != null && !resultParamList.isEmpty())
			{
				for (Iterator<Object> iterator = resultParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiResultParam resultParam = new ApiResultParam();
					resultParam.setApiID(api.getApiID());
					resultParam.setParamName(jsonObject.getString("paramName"));
					resultParam.setParamKey(jsonObject.getString("paramKey"));
					resultParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addResultParam(resultParam) < 1)
						throw new RuntimeException("addResultParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiResultValue apiResultValue = new ApiResultValue();
						apiResultValue.setParamID(resultParam.getParamID());
						apiResultValue.setValue(paramValue.getString("value"));
						apiResultValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addResultValue(apiResultValue) < 1)
							throw new RuntimeException("apiResultValue error");
					}
				}
			}
			String desc = "";
			String desc1 = "";
			if (updateDesc == null || updateDesc.length() <= 0)
			{
				desc1 = "修改接口:'" + api.getApiName() + "'";
				desc = "[快速保存]修改接口";
			}
			else
			{
				desc = updateDesc;
				desc1 = "修改接口:'" + api.getApiName() + "',更新描述：" + updateDesc;
			}
			ApiHistory apiHistory = new ApiHistory();
			apiHistory.setApiID(api.getApiID());
			apiHistory.setGroupID(api.getGroupID());
			apiHistory.setHistoryJson(JSON.toJSONString(cache));
			apiHistory.setIsNow(1);
			apiHistory.setProjectID(api.getProjectID());
			apiHistory.setUpdateDesc(desc);
			apiHistory.setUpdateTime(updateTime);
			apiHistory.setUpdateUserID(api.getUpdateUserID());
			apiHistoryMapper.updateApiHistory(api.getApiID());
			if (apiHistoryMapper.addApiHistory(apiHistory) < 1)
				throw new RuntimeException("apiHistory error");
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(api.getProjectID());
			projectOperationLog.setOpDesc(desc1);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(api.getApiID());
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(api.getUpdateUserID());
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;

		}
		return false;
	}

	/**
	 * 移除接口到回收站
	 */
	@Override
	public boolean removeApi(Integer projectID, String apiID, Integer userID)
	{
		// TODO Auto-generated method stub
		Integer result = 0;
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		JSONArray jsonArray = JSONArray.parseArray(apiID);
		List<Integer> apiIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				apiIDs.add((Integer) iterator.next());
			}
			result = apiMapper.removeApi(projectID, apiIDs, updateTime);
		}
		if (result > 0)
		{
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("将接口:'" + apiName + "'移入接口回收站");
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
	 * 从回收站恢复接口
	 */
	@Override
	public boolean recoverApi(Integer projectID, Integer groupID, String apiID, Integer userID)
	{
		// TODO Auto-generated method stub
		Integer result = 0;
		JSONArray jsonArray = JSONArray.parseArray(apiID);
		List<Integer> apiIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				apiIDs.add((Integer) iterator.next());
			}
			result = apiMapper.recoverApi(projectID, groupID, apiIDs);
		}
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		if (result > 0)
		{
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("将接口:'" + apiName + "'从回收站恢复");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 删除接口
	 */
	@Override
	public boolean deleteApi(Integer projectID, String apiID, Integer userID)
	{
		// TODO Auto-generated method stub
		String apiName = "";
		Integer result = 0;
		JSONArray jsonArray = JSONArray.parseArray(apiID);
		List<Integer> apiIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				apiIDs.add((Integer) iterator.next());
			}
			apiName = apiMapper.getApiNameByIDs(apiIDs);
			result = apiMapper.deleteApi(projectID, apiIDs);
		}
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		if (result > 0)
		{
			apiMapper.batchDeleteApiHeader(apiIDs);
			apiMapper.batchDeleteRequestParam(apiIDs);
			apiMapper.batchDeleteResultParam(apiIDs);
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("彻底删除接口：'" + apiName + "'");
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
	 * 清空回收站
	 */
	@Override
	public boolean cleanRecyclingStation(Integer projectID, Integer userID)
	{
		// TODO Auto-generated method stub
		Integer result = apiMapper.cleanRecyclingStation(projectID);
		if (result > 0)
		{
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("清空回收站");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 获取回收站接口列表
	 */
	@Override
	public List<Map<String, Object>> getRecyclingStationApiList(Integer projectID, Integer orderBy, Integer asc)
	{
		// TODO Auto-generated method stub
		String order = "";
		switch (orderBy)
		{
			case 0:
				order = "eo_api.apiName";
				break;
			case 1:
				order = "eo_api.removeTime";
				break;
			case 2:
				order = "eo_api.starred";
				break;
			case 3:
				order = "eo_api.apiID";
				break;
		}
		String by = asc == 0 ? "ASC" : "DESC";
		List<Map<String, Object>> apiList = apiMapper.getRecyclingStationApiList(projectID, order + " " + by);
		if(apiList != null && !apiList.isEmpty())
		{
			for(Map<String, Object> api : apiList)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String removeTime = dateFormat.format(api.get("removeTime"));
				String apiUpdateTime = dateFormat.format(api.get("apiUpdateTime"));
				api.put("removeTime", removeTime);
				api.put("apiUpdateTime", apiUpdateTime);
			}
		}
		return apiList;
	}

	/**
	 * 获取接口详情
	 */
	@Override
	public Map<String, Object> getApi(Integer projectID, Integer apiID, HttpServletRequest request)
	{
		// TODO Auto-generated method stub

		Map<String, Object> result = apiMapper.getApi(projectID, apiID);
		Map<String, Object> apiJson = JSONObject.parseObject(result.get("apiJson").toString());
		if (apiJson != null && !apiJson.isEmpty())
		{
			Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
			String mockCode = "projectID=" + result.get("projectID") + "&uri="
					+ ((JSONObject) apiJson.get("baseInfo")).getString("apiURI");
			baseInfo.put("mockCode", mockCode);
			baseInfo.put("starred", result.get("starred"));
			baseInfo.put("groupID", result.get("groupID"));
			baseInfo.put("parentGroupID", result.get("parentGroupID"));
			baseInfo.put("projectID", result.get("projectID"));
			baseInfo.put("apiID", result.get("apiID"));
			baseInfo.put("successMockURL", request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath() + "/Mock/simple?" + mockCode);
			baseInfo.put("failureMockURL",
					request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath() + "/Mock/simple?resultType=failure&" + mockCode);
			Integer groupID = apiGroupMapper.getTopParentGroupID(new Integer(result.get("parentGroupID").toString()));
			Integer topParentGroupID = groupID != null ? groupID : 0;
			baseInfo.put("topParentGroupID", topParentGroupID);
			apiJson.put("baseInfo", baseInfo);
			Map<String, Object> mockInfo = (JSONObject) apiJson.get("mockInfo");
			mockInfo.put("mockURL", request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath() + "/Mock/mock?" + mockCode);
			apiJson.put("mockInfo", mockInfo);
			List<Map<String, Object>> testHistoryList = testHistoryMapper.getTestHistoryList(projectID, apiID);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> list : testHistoryList)
			{
				String testTime = null;
				try
				{
					testTime = dateFormat.format(dateFormat.parse(list.get("testTime").toString()));
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.put("testTime", testTime);
				list.put("requestInfo", JSONObject.parse(list.get("requestInfo").toString()));
				list.put("resultInfo", JSONObject.parse(list.get("resultInfo").toString()));
			}
			apiJson.put("testHistory", testHistoryList);
		}
		return apiJson;
	}

	/**
	 * 获取接口列表
	 */
	@Override
	public List<Map<String, Object>> getApiList(Integer projectID, Integer groupID, Integer orderBy, Integer asc)
	{
		String order = "";
		if (orderBy == null)
		{
			orderBy = 3;
		}
		switch (orderBy)
		{
			case 0:
				order = "eo_api.apiName";
				break;
			case 1:
				order = "eo_api.apiUpdateTime";
				break;
			case 2:
				order = "eo_api.starred";
				break;
			case 3:
				order = "eo_api.apiID";
				break;
		}
		if (asc == null)
		{
			asc = 0;
		}
		String by = asc == 0 ? "ASC" : "DESC";
		List<Integer> groupIDS = new ArrayList<Integer>();
		List<Map<String, Object>> childGroupList = new ArrayList<>();
		if(groupID != null && groupID > 0)
		{
			groupIDS.add(groupID);
			childGroupList = apiGroupMapper.getChildGroupList(projectID,groupID);
		}
		if(childGroupList != null && !childGroupList.isEmpty())
		{
			for (Map<String, Object> group : childGroupList)
			{
				groupIDS.add(new Integer(group.get("groupID").toString()));
				List<Map<String, Object>> childGroupList1 = apiGroupMapper.getChildGroupList(projectID, new Integer(group.get("groupID").toString()));
				if(childGroupList1 != null && !childGroupList1.isEmpty())
				{
					for (Map<String, Object> group1 : childGroupList1)
					{
						groupIDS.add(new Integer(group1.get("groupID").toString()));
					}
				}
			}
		}
		// TODO Auto-generated method stub
		List<Map<String, Object>> apiList = apiMapper.getApiList(projectID, groupIDS, order + " " + by);
		for (Map<String, Object> api : apiList)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateTime = dateFormat.format(api.get("apiUpdateTime"));
			api.put("apiUpdateTime", updateTime);
		}
		return apiList;
	}

	/**
	 * 搜索接口
	 */
	@Override
	public List<Map<String, Object>> searchApi(Integer projectID, String tips)
	{
		// TODO Auto-generated method stub

		return apiMapper.searchApi(projectID, tips);
	}

	/**
	 * 更新接口星标状态
	 */
	@Override
	public boolean updateStar(Integer projectID, Integer apiID, Integer userID, Integer starred)
	{
		// TODO Auto-generated method stub
		Integer result = apiMapper.updateStar(projectID, apiID, starred);
		if (result > 0)
		{
			apiCacheMapper.updateApiStar(projectID, apiID, starred);
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			List<Integer> apiIDs = new ArrayList<Integer>();
			apiIDs.add(apiID);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			String desc = "";
			if (starred == 1)
			{
				desc = "修改接口'" + apiName + "'添加星标";
			}
			else
			{
				desc = "修改接口'" + apiName + "去除星标";
			}
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc(desc);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 获取接口历史列表
	 */
	@Override
	public List<Map<String, Object>> getApiHistoryList(Integer projectID, Integer apiID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> apiHistoryList = apiHistoryMapper.getApiHistoryList(projectID, apiID, 10);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> apiHistory : apiHistoryList)
		{
			String updateTime = dateFormat.format(apiHistory.get("updateTime"));
			apiHistory.put("updateTime", updateTime);
		}
		return apiHistoryList;
	}

	/**
	 * 删除接口历史
	 */
	@Override
	public boolean deleteApiHistory(Integer projectID, Integer apiID, Integer userID, Integer apiHistoryID)
	{
		// TODO Auto-generated method stub
		Integer result = apiHistoryMapper.deleteApiHistory(projectID, apiID, apiHistoryID);
		if (result > 0)
		{
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			List<Integer> apiIDs = new ArrayList<Integer>();
			apiIDs.add(apiID);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			String desc = "删除接口'" + apiName + "'的历史版本";
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc(desc);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 切换接口版本
	 */
	@Override
	public boolean toggleApiHistory(Integer projectID, Integer apiID, Integer userID, Integer apiHistoryID)
			throws RuntimeException
	{
		// TODO Auto-generated method stub
		ApiHistory apiHistory = apiHistoryMapper.getApiHistory(projectID, apiID, apiHistoryID);
		if (apiHistory == null)
		{
			return false;
		}
		else
		{
			Map<String, Object> apiJson = JSONObject.parseObject(apiHistory.getHistoryJson());
			Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
			if (apiHistoryMapper.updateApiHistoryByApiID(apiID) < 1)
				throw new RuntimeException("updateApiHistoryByApiID error");
			if (apiHistoryMapper.updateApiHistoryByApiHistoryID(apiHistoryID) < 1)
				throw new RuntimeException("updateApiHistoryByApiHistoryID error");
			ApiCache apiCache = new ApiCache();
			apiCache.setApiID(apiID);
			apiCache.setGroupID(apiHistory.getGroupID());
			apiCache.setApiJson(apiHistory.getHistoryJson());
			apiCache.setProjectID(projectID);
			apiCache.setUpdateUserID(userID);
			apiCache.setStarred((Integer) baseInfo.get("starred"));
			if (apiCacheMapper.updateApiCache(apiCache) < 1)
				throw new RuntimeException("updateApiCache error");
			apiMapper.deleteApiHeader(apiID);
			apiMapper.deleteRequestParam(apiID);
			apiMapper.deleteResultParam(apiID);
			Api api = apiMapper.getApiInfo(projectID, apiID);
			api.setApiName((String) baseInfo.get("apiName"));
			api.setApiURI((String) baseInfo.get("apiURI"));
			api.setApiProtocol((Integer) baseInfo.get("apiProtocol"));
			api.setApiSuccessMock((String) baseInfo.get("apiSuccessMock"));
			api.setApiFailureMock((String) baseInfo.get("apiFailureMock"));
			api.setApiRequestType((Integer) baseInfo.get("apiRequestType"));
			api.setApiStatus((Integer) baseInfo.get("apiStatus"));
			api.setStarred((Integer) baseInfo.get("starred"));
			api.setGroupID(apiHistory.getGroupID());
			api.setApiNoteType((Integer) baseInfo.get("apiNoteType"));
			api.setApiNoteRaw((String) baseInfo.get("apiNoteRaw"));
			api.setApiNote((String) baseInfo.get("apiNote"));
			// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date((long)baseInfo.get("apiUpdateTime"));
			// try {
			// date = dateFormat.parse((String)baseInfo.get("apiUpdateTime"));
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			Timestamp updateTime = new Timestamp(date.getTime());
			api.setApiUpdateTime(updateTime);
			api.setApiRequestParamType((Integer) baseInfo.get("apiRequestParamType"));
			api.setApiRequestRaw((String) baseInfo.get("apiRequestRaw"));
			api.setUpdateUserID(userID);
			if (apiMapper.updateApi(api) < 1)
				throw new RuntimeException("updateApi error");
			JSONArray headerList = (JSONArray) apiJson.get("headerInfo");
			if (headerList != null && !headerList.isEmpty())
			{
				for (Iterator<Object> iterator = headerList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiHeader header = new ApiHeader();
					header.setHeaderName(jsonObject.getString("headerName"));
					header.setHeaderValue(jsonObject.getString("headerValue"));
					header.setApiID(api.getApiID());
					if (apiMapper.addApiHeader(header) < 1)
						throw new RuntimeException("addApiHeader error");
				}
			}
			JSONArray requestParamList = (JSONArray) apiJson.get("requestInfo");
			if (requestParamList != null && !requestParamList.isEmpty())
			{
				for (Iterator<Object> iterator = requestParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiRequestParam requestParam = new ApiRequestParam();
					requestParam.setApiID(api.getApiID());
					requestParam.setParamName(jsonObject.getString("paramName"));
					requestParam.setParamKey(jsonObject.getString("paramKey"));
					requestParam.setParamValue(jsonObject.getString("paramValue"));
					requestParam.setParamType(jsonObject.getInteger("paramType"));
					requestParam.setParamLimit(jsonObject.getString("paramLimit"));
					requestParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addRequestParam(requestParam) < 1)
						throw new RuntimeException("addRequestParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiRequestValue apiRequestValue = new ApiRequestValue();
						apiRequestValue.setParamID(requestParam.getParamID());
						apiRequestValue.setValue(paramValue.getString("value"));
						apiRequestValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addRequestValue(apiRequestValue) < 1)
							throw new RuntimeException("apiRequestValue error");
					}
				}
			}
			JSONArray resultParamList = (JSONArray) apiJson.get("resultInfo");
			if (resultParamList != null && !resultParamList.isEmpty())
			{
				for (Iterator<Object> iterator = resultParamList.iterator(); iterator.hasNext();)
				{
					JSONObject jsonObject = (JSONObject) iterator.next();
					ApiResultParam resultParam = new ApiResultParam();
					resultParam.setApiID(api.getApiID());
					resultParam.setParamName(jsonObject.getString("paramName"));
					resultParam.setParamKey(jsonObject.getString("paramKey"));
					resultParam.setParamNotNull(jsonObject.getInteger("paramNotNull"));
					if (apiMapper.addResultParam(resultParam) < 1)
						throw new RuntimeException("addResultParam error");
					JSONArray paramValueList = JSONArray.parseArray(jsonObject.getString("paramValueList"));
					for (Iterator<Object> iterator1 = paramValueList.iterator(); iterator1.hasNext();)
					{
						JSONObject paramValue = (JSONObject) iterator1.next();
						ApiResultValue apiResultValue = new ApiResultValue();
						apiResultValue.setParamID(resultParam.getParamID());
						apiResultValue.setValue(paramValue.getString("value"));
						apiResultValue.setValueDescription(paramValue.getString("valueDescription"));
						if (apiMapper.addResultValue(apiResultValue) < 1)
							throw new RuntimeException("addResultValue error");
					}
				}
			}
			List<Integer> apiIDs = new ArrayList<Integer>();
			apiIDs.add(apiID);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			Date d = new Date();
			Timestamp t = new Timestamp(d.getTime());
			projectMapper.updateProjectUpdateTime(projectID, t);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("切换接口'" + apiName + "'历史版本");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(t);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
	}

	/**
	 * 修改分组
	 */
	@Override
	public boolean changeApiGroup(Integer projectID, String apiID, Integer userID, Integer groupID)
	{
		// TODO Auto-generated method stub
		int result = 0;
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		JSONArray jsonArray = JSONArray.parseArray(apiID);
		List<Integer> apiIDs = new ArrayList<Integer>();
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				apiIDs.add((Integer) iterator.next());
			}
			result = apiMapper.changeApiGroup(projectID, apiIDs, groupID);
		}
		if (result > 0)
		{
			if (apiCacheMapper.changeApiGroup(projectID, apiIDs, groupID) < 1)
				throw new RuntimeException("changeApiGroup error");
			projectMapper.updateProjectUpdateTime(projectID, updateTime);
			String apiName = apiMapper.getApiNameByIDs(apiIDs);
			ApiGroup apiGroup = apiGroupMapper.getGroupByID(groupID);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("将接口'" + apiName + "'移到分组'" + apiGroup.getGroupName() + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 导出接口
	 */
	@Override
	public List<Map<String, Object>> exportApi(Integer projectID, String apiID, Integer userID)
	{
		// TODO Auto-generated method stub
		List<Integer> apiIDs = new ArrayList<Integer>();
		JSONArray jsonArray = JSONArray.parseArray(apiID);
		if (jsonArray != null && !jsonArray.isEmpty())
		{
			for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();)
			{
				apiIDs.add((Integer) iterator.next());
			}
		}
		List<Map<String, Object>> result = apiMapper.getApiData(projectID, apiIDs);
		List<Map<String, Object>> apiList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (Map<String, Object> api : result)
		{
			Map<String, Object> apiJson = JSONObject.parseObject((String) api.get("apiJson"));
			Map<String, Object> baseInfo = (JSONObject) apiJson.get("baseInfo");
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String apiUpdateTime = dateFormat.format(baseInfo.get("apiUpdateTime"));
//			baseInfo.put("apiUpdateTime", apiUpdateTime);
			baseInfo.put("starred", api.get("starred"));
			apiJson.put("baseInfo", baseInfo);
			apiList.add(i, apiJson);
			i++;
		}
		Date date = new Date();
		Timestamp updateTime = new Timestamp(date.getTime());
		String apiName = apiMapper.getApiNameByIDs(apiIDs);
		ProjectOperationLog projectOperationLog = new ProjectOperationLog();
		projectOperationLog.setOpProjectID(projectID);
		projectOperationLog.setOpDesc("导出接口'" + apiName + "'");
		projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
		projectOperationLog.setOpTargetID(projectID);
		projectOperationLog.setOpTime(updateTime);
		projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_OTHERS);
		projectOperationLog.setOpUerID(userID);
		projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
		return apiList;
	}

	/**
	 * 导入接口
	 */
	@Override
	public boolean importApi(Integer projectID, Integer groupID, Integer userID, String data)
	{
		// TODO Auto-generated method stub
		JSONArray apiList = JSONArray.parseArray(data);
		if (apiList != null && !apiList.isEmpty())
		{
			String apiName = "";
			for (Iterator<Object> iterator = apiList.iterator(); iterator.hasNext();)
			{
				JSONObject apiInfo = (JSONObject) iterator.next();
				JSONObject baseInfo = (JSONObject) apiInfo.get("baseInfo");
				JSONObject mockInfo = (JSONObject) apiInfo.get("mockInfo");
				Api api = new Api();
				api.setApiName(baseInfo.getString("apiName"));
				api.setApiURI(baseInfo.getString("apiURI"));
				api.setApiProtocol(baseInfo.getInteger("apiProtocol"));
				api.setApiSuccessMock(baseInfo.getString("apiSuccessMock"));
				api.setApiFailureMock(baseInfo.getString("apiFailureMock"));
				api.setApiRequestType(baseInfo.getInteger("apiRequestType"));
				api.setApiStatus(baseInfo.getInteger("apiStatus"));
				api.setStarred(baseInfo.getInteger("starred"));
				api.setGroupID(groupID);
				api.setProjectID(projectID);
				api.setApiNoteType(baseInfo.getInteger("apiNoteType"));
				api.setApiNoteRaw(baseInfo.getString("apiNoteRaw"));
				api.setApiNote(baseInfo.getString("apiNote"));
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = null;
				try
				{
					date = dateFormat.parse(baseInfo.getString("apiUpdateTime"));
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Timestamp updateTime = new Timestamp(date.getTime());
				api.setApiUpdateTime(updateTime);
				api.setApiRequestParamType(baseInfo.getInteger("apiRequestParamType"));
				api.setApiRequestRaw(baseInfo.getString("apiRequestRaw"));
				api.setUpdateUserID(userID);
				api.setMockConfig(mockInfo.getString("mockConfig"));
				api.setMockRule(mockInfo.getString("mockRule"));
				api.setMockResult(mockInfo.getString("mockResult"));
				if (apiMapper.addApi(api) < 1)
					throw new RuntimeException("addApi error");
				ApiCache apiCache = new ApiCache();
				apiCache.setApiID(api.getApiID());
				apiCache.setApiJson(JSON.toJSONString(apiInfo));
				apiCache.setGroupID(api.getGroupID());
				apiCache.setProjectID(api.getProjectID());
				apiCache.setStarred(api.getStarred());
				apiCache.setUpdateUserID(api.getUpdateUserID());
				if (apiCacheMapper.addApiCache(apiCache) < 1)
					throw new RuntimeException("addApiCache error");
				JSONArray headerList = (JSONArray) apiInfo.get("headerInfo");
				if (headerList != null && !headerList.isEmpty())
				{
					for (Iterator<Object> iterator1 = headerList.iterator(); iterator1.hasNext();)
					{
						JSONObject headerInfo = (JSONObject) iterator1.next();
						ApiHeader header = new ApiHeader();
						header.setHeaderName(headerInfo.getString("headerName"));
						header.setHeaderValue(headerInfo.getString("headerValue"));
						header.setApiID(api.getApiID());
						if (apiMapper.addApiHeader(header) < 1)
							throw new RuntimeException("addApiHeader error");
					}
				}
				JSONArray requestParamList = (JSONArray) apiInfo.get("requestInfo");
				if (requestParamList != null && !requestParamList.isEmpty())
				{
					for (Iterator<Object> iterator1 = requestParamList.iterator(); iterator1.hasNext();)
					{
						JSONObject requestInfo = (JSONObject) iterator1.next();
						ApiRequestParam requestParam = new ApiRequestParam();
						requestParam.setApiID(api.getApiID());
						requestParam.setParamName(requestInfo.getString("paramName"));
						requestParam.setParamKey(requestInfo.getString("paramKey"));
						requestParam.setParamValue(requestInfo.getString("paramValue"));
						requestParam.setParamType(requestInfo.getInteger("paramType"));
						requestParam.setParamLimit(requestInfo.getString("paramLimit"));
						requestParam.setParamNotNull(requestInfo.getInteger("paramNotNull"));
						if (apiMapper.addRequestParam(requestParam) < 1)
							throw new RuntimeException("addRequestParam error");
						JSONArray paramValueList = (JSONArray) requestInfo.get("paramValueList");
						for (Iterator<Object> iterator2 = paramValueList.iterator(); iterator2.hasNext();)
						{
							JSONObject paramValue = (JSONObject) iterator2.next();
							ApiRequestValue apiRequestValue = new ApiRequestValue();
							apiRequestValue.setParamID(requestParam.getParamID());
							apiRequestValue.setValue(paramValue.getString("value"));
							apiRequestValue.setValueDescription(paramValue.getString("valueDescription"));
							if (apiMapper.addRequestValue(apiRequestValue) < 1)
								throw new RuntimeException("apiRequestValue error");
						}
					}
				}
				JSONArray resultParamList = (JSONArray) apiInfo.get("resultInfo");
				if (resultParamList != null && !resultParamList.isEmpty())
				{
					for (Iterator<Object> iterator1 = resultParamList.iterator(); iterator1.hasNext();)
					{
						JSONObject resultInfo = (JSONObject) iterator1.next();
						ApiResultParam resultParam = new ApiResultParam();
						resultParam.setApiID(api.getApiID());
						resultParam.setParamName(resultInfo.getString("paramName"));
						resultParam.setParamKey(resultInfo.getString("paramKey"));
						resultParam.setParamNotNull(resultInfo.getInteger("paramNotNull"));
						if (apiMapper.addResultParam(resultParam) < 1)
							throw new RuntimeException("addResultParam error");
						JSONArray paramValueList = (JSONArray) resultInfo.get("paramValueList");
						for (Iterator<Object> iterator2 = paramValueList.iterator(); iterator2.hasNext();)
						{
							JSONObject paramValue = (JSONObject) iterator2.next();
							ApiResultValue apiResultValue = new ApiResultValue();
							apiResultValue.setParamID(resultParam.getParamID());
							apiResultValue.setValue(paramValue.getString("value"));
							apiResultValue.setValueDescription(paramValue.getString("valueDescription"));
							if (apiMapper.addResultValue(apiResultValue) < 1)
								throw new RuntimeException("addResultValue error");
						}
					}
				}
				apiName += baseInfo.getString("apiName") + ",";
			}
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			apiName = apiName.substring(0, apiName.length() - 1);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("批量导入接口'" + apiName + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_API);
			projectOperationLog.setOpTargetID(projectID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取接口mock数据
	 */
	@Override
	public Map<String, Object> getApiMockData(Integer projectID, Integer apiID, HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		Map<String, Object> data = apiMapper.getApiMockData(projectID, apiID);
		if (data != null && !data.isEmpty())
		{
			if(data.get("mockRule") != null)
			{
				data.put("mockRule", JSONArray.parse(data.get("mockRule").toString()));
			}
			else
			{
				data.put("mockRule", new ArrayList<>());
			}
			if(data.get("mockConfig") != null)
			{
				data.put("mockConfig", JSONObject.parse(data.get("mockConfig").toString()));
			}
			else
			{
				data.put("mockConfig", new ArrayList<>());
			}
			String mockCode = "projectID=" + data.get("projectID") + "&uri=" + data.get("apiURI");
			data.put("mockURL", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/Mock/mock?" + mockCode);
		}
		return data;
	}

	/**
	 * 修改接口mock数据
	 */
	@Override
	public boolean editApiMockData(Integer projectID, Integer apiID, String mockRule, String mockResult,
			String mockConfig)
	{
		// TODO Auto-generated method stub
		if (apiMapper.editApiMockData(projectID, apiID, mockRule, mockResult, mockConfig) > 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取项目ID
	 */
	@Override
	public Integer getProjectID(Integer apiID)
	{
		// TODO Auto-generated method stub
		return apiMapper.getProjectID(apiID);
	}
}
