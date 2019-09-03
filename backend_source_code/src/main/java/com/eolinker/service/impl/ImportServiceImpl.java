package com.eolinker.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.awt.Stroke;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.Length;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.mapper.ApiCacheMapper;
import com.eolinker.mapper.EnvFrontUriMapper;
import com.eolinker.mapper.EnvHeaderMapper;
import com.eolinker.mapper.EnvMapper;
import com.eolinker.mapper.EnvParamAdditionalMapper;
import com.eolinker.mapper.EnvParamMapper;
import com.eolinker.mapper.ApiGroupMapper;
import com.eolinker.mapper.ApiMapper;
import com.eolinker.mapper.DocumentGroupMapper;
import com.eolinker.mapper.DocumentMapper;
import com.eolinker.mapper.PartnerMapper;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.StatusCodeGroupMapper;
import com.eolinker.mapper.StatusCodeMapper;
import com.eolinker.pojo.Api;
import com.eolinker.pojo.ApiCache;
import com.eolinker.pojo.Env;
import com.eolinker.pojo.EnvFrontUri;
import com.eolinker.pojo.EnvHeader;
import com.eolinker.pojo.EnvParam;
import com.eolinker.pojo.EnvParamAdditional;
import com.eolinker.pojo.ApiGroup;
import com.eolinker.pojo.ApiHeader;
import com.eolinker.pojo.ApiRequestParam;
import com.eolinker.pojo.ApiRequestValue;
import com.eolinker.pojo.ApiResultParam;
import com.eolinker.pojo.ApiResultValue;
import com.eolinker.pojo.Document;
import com.eolinker.pojo.DocumentGroup;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.StatusCode;
import com.eolinker.pojo.StatusCodeGroup;
import com.eolinker.service.ImportService;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 导入项目[业务处理层]
 * 
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 *         eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 *         如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 *         eoLinker AMS开源版的开源协议遵循GPL
 *         V3，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 *         官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 *         使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 *         用户讨论QQ群：707530721
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "java.lang.Exception")
public class ImportServiceImpl implements ImportService
{

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private ApiGroupMapper apiGroupMapper;
	@Autowired
	private PartnerMapper partnerMapper;
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
	@Autowired
	private StatusCodeGroupMapper statusCodeGroupMapper;
	@Autowired
	private DocumentGroupMapper documentGroupMapper;
	@Autowired
	private DocumentMapper documentMapper;

	/**
	 * 导入eolinker项目
	 */
	@Override
	public boolean importEoapi(String data, Integer userID)
	{
		// TODO Auto-generated method stub
		JSONObject projectData = JSONObject.parseObject(data);
		if (projectData != null && !projectData.isEmpty())
		{
			JSONObject projectInfo = JSONObject.parseObject(projectData.getString("projectInfo"));
			Project project = new Project();
			project.setProjectName(projectInfo.getString("projectName"));
			project.setProjectType(projectInfo.getInteger("projectType"));
			project.setProjectVersion(projectInfo.getString("projectVersion"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			Timestamp updateTime = null;
			try
			{
				date = dateFormat.parse(projectInfo.getString("projectUpdateTime"));
				updateTime = new Timestamp(date.getTime());
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				date = new Date();
				updateTime = new Timestamp(date.getTime());
			}
			project.setProjectUpdateTime(updateTime);
			if (projectMapper.addProject(project) < 1)
				throw new RuntimeException("addProject error");
			Partner partner = new Partner();
			partner.setProjectID(project.getProjectID());
			partner.setUserID(userID);
			partner.setUserType(0);
			if (partnerMapper.addPartner(partner) < 1)
				throw new RuntimeException("addPartner error");
			JSONArray apiGroupList = null;
			if (!Boolean.class.isInstance(projectData.get("apiGroupList")))
			{
				apiGroupList = JSONArray.parseArray(projectData.getString("apiGroupList"));
			}
			if (apiGroupList != null && !apiGroupList.isEmpty())
			{
				for (Iterator<Object> iterator = apiGroupList.iterator(); iterator.hasNext();)
				{
					JSONObject groupData = (JSONObject) iterator.next();
					ApiGroup apiGroup = new ApiGroup();
					apiGroup.setGroupName(groupData.getString("groupName"));
					apiGroup.setProjectID(project.getProjectID());
					apiGroup.setIsChild(0);
					apiGroup.setParentGroupID(0);
					if (apiGroupMapper.addApiGroup(apiGroup) < 1)
						throw new RuntimeException("addApiGroup error");
					JSONArray apiList = null;
					if (!Boolean.class.isInstance(groupData.get("apiList")))
					{
						apiList = JSONArray.parseArray(groupData.getString("apiList"));
					}
					if (apiList != null && !apiList.isEmpty())
					{
						for (Iterator<Object> iterator1 = apiList.iterator(); iterator1.hasNext();)
						{
							JSONObject apiInfo = (JSONObject) iterator1.next();
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
							api.setGroupID(apiGroup.getGroupID());
							api.setProjectID(project.getProjectID());
							api.setApiNoteType(baseInfo.getInteger("apiNoteType"));
							api.setApiNoteRaw(baseInfo.getString("apiNoteRaw"));
							api.setApiNote(baseInfo.getString("apiNote"));
							SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date1 = null;
							Timestamp updateTime1 = null;
							try
							{
								date1 = dateFormat1.parse(baseInfo.getString("apiUpdateTime"));
								updateTime1 = new Timestamp(date1.getTime());
							}
							catch (ParseException e)
							{
								// TODO Auto-generated catch block
								date1 = new Date();
								updateTime1 = new Timestamp(date1.getTime());
							}
							api.setApiUpdateTime(updateTime1);
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
							JSONArray headerList = null;
							if (!Boolean.class.isInstance(apiInfo.get("headerInfo")))
							{
								headerList = (JSONArray) apiInfo.get("headerInfo");
							}
							if (headerList != null && !headerList.isEmpty())
							{
								for (Iterator<Object> iterator2 = headerList.iterator(); iterator2.hasNext();)
								{
									JSONObject headerInfo = (JSONObject) iterator2.next();
									ApiHeader header = new ApiHeader();
									header.setHeaderName(headerInfo.getString("headerName"));
									header.setHeaderValue(headerInfo.getString("headerValue"));
									header.setApiID(api.getApiID());
									if (apiMapper.addApiHeader(header) < 1)
										throw new RuntimeException("addApiHeader error");
								}
							}
							JSONArray requestParamList = null;
							if (!Boolean.class.isInstance(apiInfo.get("requestInfo")))
							{
								requestParamList = (JSONArray) apiInfo.get("requestInfo");
							}
							if (requestParamList != null && !requestParamList.isEmpty())
							{
								for (Iterator<Object> iterator2 = requestParamList.iterator(); iterator2.hasNext();)
								{
									JSONObject requestInfo = (JSONObject) iterator2.next();
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
									JSONArray paramValueList = null;
									if (!Boolean.class.isInstance(requestInfo.get("paramValueList")))
									{
										paramValueList = (JSONArray) requestInfo.get("paramValueList");
									}
									for (Iterator<Object> iterator21 = paramValueList.iterator(); iterator21.hasNext();)
									{
										JSONObject paramValue = (JSONObject) iterator21.next();
										ApiRequestValue apiRequestValue = new ApiRequestValue();
										apiRequestValue.setParamID(requestParam.getParamID());
										apiRequestValue.setValue(paramValue.getString("value"));
										apiRequestValue.setValueDescription(paramValue.getString("valueDescription"));
										if (apiMapper.addRequestValue(apiRequestValue) < 1)
											throw new RuntimeException("apiRequestValue error");
									}
								}
							}
							JSONArray resultParamList = null;
							if (!Boolean.class.isInstance(apiInfo.get("resultInfo")))
							{
								resultParamList = (JSONArray) apiInfo.get("resultInfo");
							}
							if (resultParamList != null && !resultParamList.isEmpty())
							{
								for (Iterator<Object> iterator2 = resultParamList.iterator(); iterator2.hasNext();)
								{
									JSONObject resultInfo = (JSONObject) iterator2.next();
									ApiResultParam resultParam = new ApiResultParam();
									resultParam.setApiID(api.getApiID());
									resultParam.setParamName(resultInfo.getString("paramName"));
									resultParam.setParamKey(resultInfo.getString("paramKey"));
									resultParam.setParamNotNull(resultInfo.getInteger("paramNotNull"));
									if (apiMapper.addResultParam(resultParam) < 1)
										throw new RuntimeException("addResultParam error");
									JSONArray paramValueList = null;
									if (!Boolean.class.isInstance(resultInfo.get("paramValueList")))
									{
										paramValueList = (JSONArray) resultInfo.get("paramValueList");
									}
									for (Iterator<Object> iterator21 = paramValueList.iterator(); iterator21.hasNext();)
									{
										JSONObject paramValue = (JSONObject) iterator21.next();
										ApiResultValue apiResultValue = new ApiResultValue();
										apiResultValue.setParamID(resultParam.getParamID());
										apiResultValue.setValue(paramValue.getString("value"));
										apiResultValue.setValueDescription(paramValue.getString("valueDescription"));
										if (apiMapper.addResultValue(apiResultValue) < 1)
											throw new RuntimeException("addResultValue error");
									}
								}
							}
						}
					}
					JSONArray childGroupList = null;
					if (!Boolean.class.isInstance(groupData.get("apiGroupChildList")))
					{
						childGroupList = JSONArray.parseArray(groupData.getString("apiGroupChildList"));
					}
					if (childGroupList != null && !childGroupList.isEmpty())
					{
						for (Iterator<Object> iterator1 = childGroupList.iterator(); iterator1.hasNext();)
						{
							JSONObject childGroupInfo = (JSONObject) iterator1.next();
							ApiGroup apiChildGroup = new ApiGroup();
							apiChildGroup.setGroupName(childGroupInfo.getString("groupName"));
							apiChildGroup.setIsChild(childGroupInfo.getInteger("isChild"));
							apiChildGroup.setProjectID(project.getProjectID());
							apiChildGroup.setParentGroupID(apiGroup.getGroupID());
							if (apiGroupMapper.addApiGroup(apiChildGroup) < 1)
								throw new RuntimeException("addApiChildGroup error");
							JSONArray apiList1 = null;
							if (!Boolean.class.isInstance(childGroupInfo.getString("apiList")))
							{
								apiList1 = JSONArray.parseArray(childGroupInfo.getString("apiList"));
							}
							if (apiList1 != null && !apiList1.isEmpty())
							{
								for (Iterator<Object> iterator11 = apiList1.iterator(); iterator11.hasNext();)
								{
									JSONObject apiInfo = (JSONObject) iterator11.next();
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
									api.setGroupID(apiChildGroup.getGroupID());
									api.setProjectID(project.getProjectID());
									api.setApiNoteType(baseInfo.getInteger("apiNoteType"));
									api.setApiNoteRaw(baseInfo.getString("apiNoteRaw"));
									api.setApiNote(baseInfo.getString("apiNote"));
									SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date date1 = null;
									Timestamp updateTime1 = null;
									try
									{
										date1 = dateFormat1.parse(baseInfo.getString("apiUpdateTime"));
										updateTime1 = new Timestamp(date1.getTime());
									}
									catch (ParseException e)
									{
										// TODO Auto-generated catch block
										date1 = new Date();
										updateTime1 = new Timestamp(date1.getTime());
									}
									api.setApiUpdateTime(updateTime1);
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
									JSONArray headerList = null;
									if (!Boolean.class.isInstance(apiInfo.get("headerInfo")))
									{
										headerList = (JSONArray) apiInfo.get("headerInfo");
									}
									if (headerList != null && !headerList.isEmpty())
									{
										for (Iterator<Object> iterator111 = headerList.iterator(); iterator111
												.hasNext();)
										{
											JSONObject headerInfo = (JSONObject) iterator111.next();
											ApiHeader header = new ApiHeader();
											header.setHeaderName(headerInfo.getString("headerName"));
											header.setHeaderValue(headerInfo.getString("headerValue"));
											header.setApiID(api.getApiID());
											if (apiMapper.addApiHeader(header) < 1)
												throw new RuntimeException("addApiHeader error");
										}
									}
									JSONArray requestParamList = null;
									if (!Boolean.class.isInstance(apiInfo.get("requestInfo")))
									{
										requestParamList = (JSONArray) apiInfo.get("requestInfo");
									}
									if (requestParamList != null && !requestParamList.isEmpty())
									{
										for (Iterator<Object> iterator111 = requestParamList.iterator(); iterator111
												.hasNext();)
										{
											JSONObject requestInfo = (JSONObject) iterator111.next();
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
											JSONArray paramValueList = null;
											if (!Boolean.class.isInstance(requestInfo.get("paramValueList")))
											{
												paramValueList = (JSONArray) requestInfo.get("paramValueList");
											}
											for (Iterator<Object> iterator2 = paramValueList.iterator(); iterator2
													.hasNext();)
											{
												JSONObject paramValue = (JSONObject) iterator2.next();
												ApiRequestValue apiRequestValue = new ApiRequestValue();
												apiRequestValue.setParamID(requestParam.getParamID());
												apiRequestValue.setValue(paramValue.getString("value"));
												apiRequestValue
														.setValueDescription(paramValue.getString("valueDescription"));
												if (apiMapper.addRequestValue(apiRequestValue) < 1)
													throw new RuntimeException("apiRequestValue error");
											}
										}
									}
									JSONArray resultParamList = null;
									if (!Boolean.class.isInstance(apiInfo.get("resultInfo")))
									{
										resultParamList = (JSONArray) apiInfo.get("resultInfo");
									}
									if (resultParamList != null && !resultParamList.isEmpty())
									{
										for (Iterator<Object> iterator111 = resultParamList.iterator(); iterator111
												.hasNext();)
										{
											JSONObject resultInfo = (JSONObject) iterator111.next();
											ApiResultParam resultParam = new ApiResultParam();
											resultParam.setApiID(api.getApiID());
											resultParam.setParamName(resultInfo.getString("paramName"));
											resultParam.setParamKey(resultInfo.getString("paramKey"));
											resultParam.setParamNotNull(resultInfo.getInteger("paramNotNull"));
											if (apiMapper.addResultParam(resultParam) < 1)
												throw new RuntimeException("addResultParam error");
											JSONArray paramValueList = null;
											if (!Boolean.class.isInstance(resultInfo.get("paramValueList")))
											{
												paramValueList = (JSONArray) resultInfo.get("paramValueList");
											}
											for (Iterator<Object> iterator2 = paramValueList.iterator(); iterator2
													.hasNext();)
											{
												JSONObject paramValue = (JSONObject) iterator2.next();
												ApiResultValue apiResultValue = new ApiResultValue();
												apiResultValue.setParamID(resultParam.getParamID());
												apiResultValue.setValue(paramValue.getString("value"));
												apiResultValue
														.setValueDescription(paramValue.getString("valueDescription"));
												if (apiMapper.addResultValue(apiResultValue) < 1)
													throw new RuntimeException("addResultValue error");
											}
										}
									}
								}
							}
							JSONArray childGroupList1 = null;
							if (!Boolean.class.isInstance(childGroupInfo.get("apiGroupChildList")))
							{
								childGroupList1 = JSONArray.parseArray(childGroupInfo.getString("apiGroupChildList"));
							}
							if (childGroupList1 != null && !childGroupList1.isEmpty())
							{
								for (Iterator<Object> iterator11 = childGroupList1.iterator(); iterator11.hasNext();)
								{
									JSONObject childGroupInfo1 = (JSONObject) iterator11.next();
									ApiGroup apiChildGroup1 = new ApiGroup();
									apiChildGroup1.setGroupName(childGroupInfo1.getString("groupName"));
									apiChildGroup1.setIsChild(childGroupInfo1.getInteger("isChild"));
									apiChildGroup1.setProjectID(project.getProjectID());
									apiChildGroup1.setParentGroupID(apiChildGroup.getGroupID());
									if (apiGroupMapper.addApiGroup(apiChildGroup1) < 1)
										throw new RuntimeException("addApiChildGroup error");
									JSONArray apiList11 = null;
									if (!Boolean.class.isInstance(childGroupInfo1.get("apiList")))
									{
										apiList11 = JSONArray.parseArray(childGroupInfo1.getString("apiList"));
									}
									if (apiList11 != null && !apiList11.isEmpty())
									{
										for (Iterator<Object> iterator111 = apiList11.iterator(); iterator111
												.hasNext();)
										{
											JSONObject apiInfo = (JSONObject) iterator111.next();
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
											api.setGroupID(apiChildGroup1.getGroupID());
											api.setProjectID(project.getProjectID());
											api.setApiNoteType(baseInfo.getInteger("apiNoteType"));
											api.setApiNoteRaw(baseInfo.getString("apiNoteRaw"));
											api.setApiNote(baseInfo.getString("apiNote"));
											SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Date date1 = null;
											Timestamp updateTime1 = null;
											try
											{
												date1 = dateFormat1.parse(baseInfo.getString("apiUpdateTime"));
												updateTime1 = new Timestamp(date1.getTime());
											}
											catch (ParseException e)
											{
												// TODO Auto-generated catch block
												date1 = new Date();
												updateTime1 = new Timestamp(date1.getTime());
											}
											api.setApiUpdateTime(updateTime1);
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
											JSONArray headerList = null;
											if (!Boolean.class.isInstance(apiInfo.get("headerInfo")))
											{
												headerList = (JSONArray) apiInfo.get("headerInfo");
											}
											if (headerList != null && !headerList.isEmpty())
											{
												for (Iterator<Object> iterator1111 = headerList.iterator(); iterator1111
														.hasNext();)
												{
													JSONObject headerInfo = (JSONObject) iterator1111.next();
													ApiHeader header = new ApiHeader();
													header.setHeaderName(headerInfo.getString("headerName"));
													header.setHeaderValue(headerInfo.getString("headerValue"));
													header.setApiID(api.getApiID());
													if (apiMapper.addApiHeader(header) < 1)
														throw new RuntimeException("addApiHeader error");
												}
											}
											JSONArray requestParamList = null;
											if (!Boolean.class.isInstance(apiInfo.get("requestInfo")))
											{
												requestParamList = (JSONArray) apiInfo.get("requestInfo");
											}
											if (requestParamList != null && !requestParamList.isEmpty())
											{
												for (Iterator<Object> iterator1111 = requestParamList
														.iterator(); iterator1111.hasNext();)
												{
													JSONObject requestInfo = (JSONObject) iterator1111.next();
													ApiRequestParam requestParam = new ApiRequestParam();
													requestParam.setApiID(api.getApiID());
													requestParam.setParamName(requestInfo.getString("paramName"));
													requestParam.setParamKey(requestInfo.getString("paramKey"));
													requestParam.setParamValue(requestInfo.getString("paramValue"));
													requestParam.setParamType(requestInfo.getInteger("paramType"));
													requestParam.setParamLimit(requestInfo.getString("paramLimit"));
													requestParam
															.setParamNotNull(requestInfo.getInteger("paramNotNull"));
													if (apiMapper.addRequestParam(requestParam) < 1)
														throw new RuntimeException("addRequestParam error");
													JSONArray paramValueList = null;
													if (!Boolean.class.isInstance(requestInfo.get("paramValueList")))
													{
														paramValueList = (JSONArray) requestInfo.get("paramValueList");
													}
													for (Iterator<Object> iterator2 = paramValueList
															.iterator(); iterator2.hasNext();)
													{
														JSONObject paramValue = (JSONObject) iterator2.next();
														ApiRequestValue apiRequestValue = new ApiRequestValue();
														apiRequestValue.setParamID(requestParam.getParamID());
														apiRequestValue.setValue(paramValue.getString("value"));
														apiRequestValue.setValueDescription(
																paramValue.getString("valueDescription"));
														if (apiMapper.addRequestValue(apiRequestValue) < 1)
															throw new RuntimeException("apiRequestValue error");
													}
												}
											}
											JSONArray resultParamList = null;
											if (!Boolean.class.isInstance(apiInfo.get("resultInfo")))
											{
												resultParamList = (JSONArray) apiInfo.get("resultInfo");
											}
											if (resultParamList != null && !resultParamList.isEmpty())
											{
												for (Iterator<Object> iterator1111 = resultParamList
														.iterator(); iterator1111.hasNext();)
												{
													JSONObject resultInfo = (JSONObject) iterator1111.next();
													ApiResultParam resultParam = new ApiResultParam();
													resultParam.setApiID(api.getApiID());
													resultParam.setParamName(resultInfo.getString("paramName"));
													resultParam.setParamKey(resultInfo.getString("paramKey"));
													resultParam.setParamNotNull(resultInfo.getInteger("paramNotNull"));
													if (apiMapper.addResultParam(resultParam) < 1)
														throw new RuntimeException("addResultParam error");
													JSONArray paramValueList = null;
													if (!Boolean.class.isInstance(resultInfo.get("paramValueList")))
													{
														paramValueList = (JSONArray) resultInfo.get("paramValueList");
													}
													for (Iterator<Object> iterator2 = paramValueList
															.iterator(); iterator2.hasNext();)
													{
														JSONObject paramValue = (JSONObject) iterator2.next();
														ApiResultValue apiResultValue = new ApiResultValue();
														apiResultValue.setParamID(resultParam.getParamID());
														apiResultValue.setValue(paramValue.getString("value"));
														apiResultValue.setValueDescription(
																paramValue.getString("valueDescription"));
														if (apiMapper.addResultValue(apiResultValue) < 1)
															throw new RuntimeException("addResultValue error");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			JSONArray statusCodeGroupList = null;
			if (!Boolean.class.isInstance(projectData.get("statusCodeGroupList")))
			{
				statusCodeGroupList = JSONArray.parseArray(projectData.getString("statusCodeGroupList"));
			}
			if (statusCodeGroupList != null && !statusCodeGroupList.isEmpty())
			{
				for (Iterator<Object> iterator1 = statusCodeGroupList.iterator(); iterator1.hasNext();)
				{

					JSONObject statusCodeGroupData = (JSONObject) iterator1.next();
					StatusCodeGroup statusCodeGroup = new StatusCodeGroup();
					statusCodeGroup.setProjectID(project.getProjectID());
					statusCodeGroup.setGroupName(statusCodeGroupData.getString("groupName"));
					if (statusCodeGroupMapper.addGroup(statusCodeGroup) < 1)
						throw new RuntimeException("addStatusGroup error");
					JSONArray statusCodeList = null;
					if (!Boolean.class.isInstance(statusCodeGroupData.get("statusCodeList")))
					{
						statusCodeList = JSONArray.parseArray(statusCodeGroupData.getString("statusCodeList"));
					}
					// 插入文档
					if (statusCodeList != null && !statusCodeList.isEmpty())
					{
						for (Iterator<Object> iterator11 = statusCodeList.iterator(); iterator11.hasNext();)
						{
							JSONObject statusCodeData = (JSONObject) iterator11.next();
							StatusCode statusCode = new StatusCode();
							statusCode.setGroupID(statusCodeGroup.getGroupID());
							statusCode.setCode(statusCodeData.getString("code"));
							statusCode.setCodeDescription(statusCodeData.getString("codeDescription"));
							if (statusCodeMapper.addCode(statusCode) < 1)
								throw new RuntimeException("addCode error");
						}
					}
					JSONArray childGroupList = null;
					if (!Boolean.class.isInstance(statusCodeGroupData.get("statusCodeGroupChildList")))
					{
						childGroupList = JSONArray
								.parseArray(statusCodeGroupData.getString("statusCodeGroupChildList"));
					}
					if (childGroupList != null && !childGroupList.isEmpty())
					{
						for (Iterator<Object> iterator3 = childGroupList.iterator(); iterator3.hasNext();)
						{
							JSONObject groupData = (JSONObject) iterator3.next();
							StatusCodeGroup childStatusCodeGroup = new StatusCodeGroup();
							childStatusCodeGroup.setProjectID(project.getProjectID());
							childStatusCodeGroup.setGroupName(groupData.getString("groupName"));
							childStatusCodeGroup.setParentGroupID(statusCodeGroup.getGroupID());
							childStatusCodeGroup.setIsChild(groupData.getInteger("isChild"));
							if (statusCodeGroupMapper.addChildGroup(childStatusCodeGroup) < 1)
								throw new RuntimeException("addStatusCodeChildGroup error");
							JSONArray statusCodeList1 = null;
							if (!Boolean.class.isInstance(groupData.getString("statusCodeList")))
							{
								statusCodeList1 = JSONArray.parseArray(groupData.getString("statusCodeList"));
							}
							if (statusCodeList1 != null && !statusCodeList1.isEmpty())
							{
								for (Iterator<Object> iterator2 = statusCodeList1.iterator(); iterator2.hasNext();)
								{
									JSONObject statusCodeData = (JSONObject) iterator2.next();
									StatusCode statusCode = new StatusCode();
									statusCode.setGroupID(childStatusCodeGroup.getGroupID());
									statusCode.setCode(statusCodeData.getString("code"));
									statusCode.setCodeDescription(statusCodeData.getString("codeDescription"));
									if (statusCodeMapper.addCode(statusCode) < 1)
										throw new RuntimeException("addCode error");
								}
							}
							JSONArray childGroupList1 = null;
							if (!Boolean.class.isInstance(groupData.get("statusCodeGroupChildList")))
							{
								childGroupList1 = JSONArray.parseArray(groupData.getString("statusCodeGroupChildList"));
							}
							if (childGroupList1 != null && !childGroupList1.isEmpty())
							{
								for (Iterator<Object> iterator31 = childGroupList1.iterator(); iterator31.hasNext();)
								{
									JSONObject groupData1 = (JSONObject) iterator31.next();
									StatusCodeGroup childStatusCodeGroup1 = new StatusCodeGroup();
									childStatusCodeGroup1.setProjectID(project.getProjectID());
									childStatusCodeGroup1.setGroupName(groupData1.getString("groupName"));
									childStatusCodeGroup1.setParentGroupID(childStatusCodeGroup.getGroupID());
									childStatusCodeGroup1.setIsChild(groupData1.getInteger("isChild"));
									if (statusCodeGroupMapper.addChildGroup(childStatusCodeGroup1) < 1)
										throw new RuntimeException("addStatusCodeChildGroup error");
									JSONArray statusCodeList11 = null;
									if (!Boolean.class.isInstance(groupData1.get("statusCodeList")))
									{
										statusCodeList11 = JSONArray.parseArray(groupData1.getString("statusCodeList"));
									}
									if (statusCodeList11 != null && !statusCodeList11.isEmpty())
									{
										for (Iterator<Object> iterator2 = statusCodeList11.iterator(); iterator2
												.hasNext();)
										{
											JSONObject statusCodeData = (JSONObject) iterator2.next();
											StatusCode statusCode = new StatusCode();
											statusCode.setGroupID(childStatusCodeGroup1.getGroupID());
											statusCode.setCode(statusCodeData.getString("code"));
											statusCode.setCodeDescription(statusCodeData.getString("codeDescription"));
											if (statusCodeMapper.addCode(statusCode) < 1)
												throw new RuntimeException("addCode error");
										}
									}

								}

							}

						}

					}

				}
			}
			JSONArray pageGroupList = null;
			if (!Boolean.class.isInstance(projectData.get("pageGroupList")))
			{
				pageGroupList = JSONArray.parseArray(projectData.getString("pageGroupList"));
			}
			if (pageGroupList != null && !pageGroupList.isEmpty())
			{
				for (Iterator<Object> iterator = pageGroupList.iterator(); iterator.hasNext();)
				{
					JSONObject pageGroupData = (JSONObject) iterator.next();
					DocumentGroup documentGroup = new DocumentGroup();
					documentGroup.setGroupName(pageGroupData.getString("groupName"));
					documentGroup.setProjectID(project.getProjectID());
					documentGroup.setIsChild(pageGroupData.getInteger("isChild"));
					if (documentGroupMapper.addDocumentGroup(documentGroup) < 1)
						throw new RuntimeException("addDocumentGroup error");
					JSONArray pageList = null;
					if (!Boolean.class.isInstance(pageGroupData.get("pageList")))
					{
						pageList = JSONArray.parseArray(pageGroupData.getString("pageList"));
					}
					if (pageList != null && !pageList.isEmpty())
					{
						for (Iterator<Object> iterator2 = pageList.iterator(); iterator2.hasNext();)
						{
							JSONObject pageData = (JSONObject) iterator2.next();
							Document document = new Document();
							document.setGroupID(documentGroup.getGroupID());
							document.setProjectID(project.getProjectID());
							document.setContentType(pageData.getInteger("contentType"));
							document.setContentRaw(pageData.getString("contentRaw"));
							document.setContent(pageData.getString("content"));
							document.setTitle(pageData.getString("title"));
							SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date1 = null;
							Timestamp updateTime1 = null;
							try
							{
								date1 = dateFormat1.parse(pageData.getString("updateTime"));
								updateTime1 = new Timestamp(date1.getTime());
							}
							catch (ParseException e)
							{
								// TODO Auto-generated catch block
								date1 = new Date();
								updateTime1 = new Timestamp(date1.getTime());
							}
							document.setUpdateTime(updateTime1);
							document.setUserID(userID);
							if (documentMapper.addDocument(document) < 1)
								throw new RuntimeException("addDocument error");
						}
					}
					JSONArray childGroupList = null;
					if (!Boolean.class.isInstance(pageGroupData.get("pageGroupChildList")))
					{
						childGroupList = JSONArray.parseArray(pageGroupData.getString("pageGroupChildList"));
					}
					for (Iterator<Object> iterator1 = childGroupList.iterator(); iterator1.hasNext();)
					{
						JSONObject pageGroupData1 = (JSONObject) iterator1.next();
						DocumentGroup documentGroup1 = new DocumentGroup();
						documentGroup1.setGroupName(pageGroupData1.getString("groupName"));
						documentGroup1.setProjectID(project.getProjectID());
						documentGroup1.setParentGroupID(documentGroup.getGroupID());
						documentGroup1.setIsChild(pageGroupData1.getInteger("isChild"));
						if (documentGroupMapper.addChildGroup(documentGroup1) < 1)
							throw new RuntimeException("addDocumentGroup error");
						JSONArray pageList1 = null;
						if (!Boolean.class.isInstance(pageGroupData1.get("pageList")))
						{
							pageList1 = JSONArray.parseArray(pageGroupData1.getString("pageList"));
						}
						if (pageList1 != null && !pageList1.isEmpty())
						{
							for (Iterator<Object> iterator2 = pageList1.iterator(); iterator2.hasNext();)
							{
								JSONObject pageData = (JSONObject) iterator2.next();
								Document document = new Document();
								document.setGroupID(documentGroup1.getGroupID());
								document.setProjectID(project.getProjectID());
								document.setContentType(pageData.getInteger("contentType"));
								document.setContentRaw(pageData.getString("contentRaw"));
								document.setContent(pageData.getString("content"));
								document.setTitle(pageData.getString("title"));
								SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date1 = null;
								Timestamp updateTime1 = null;
								try
								{
									date1 = dateFormat1.parse(pageData.getString("updateTime"));
									updateTime1 = new Timestamp(date1.getTime());
								}
								catch (ParseException e)
								{
									// TODO Auto-generated catch block
									date1 = new Date();
									updateTime1 = new Timestamp(date1.getTime());

								}
								document.setUpdateTime(updateTime1);
								document.setUserID(userID);
								if (documentMapper.addDocument(document) < 1)
									throw new RuntimeException("addDocument error");
							}
						}
						JSONArray childGroupList1 = null;
						if (!Boolean.class.isInstance(pageGroupData1.get("pageGroupChildList")))
						{
							childGroupList1 = JSONArray.parseArray(pageGroupData1.getString("pageGroupChildList"));
						}
						for (Iterator<Object> iterator11 = childGroupList1.iterator(); iterator11.hasNext();)
						{
							JSONObject pageGroupData11 = (JSONObject) iterator11.next();
							DocumentGroup documentGroup11 = new DocumentGroup();
							documentGroup11.setGroupName(pageGroupData11.getString("groupName"));
							documentGroup11.setProjectID(project.getProjectID());
							documentGroup11.setParentGroupID(documentGroup1.getGroupID());
							documentGroup11.setIsChild(pageGroupData11.getInteger("isChild"));
							if (documentGroupMapper.addChildGroup(documentGroup11) < 1)
								throw new RuntimeException("addDocumentGroup error");
							JSONArray pageList11 = null;
							if (!Boolean.class.isInstance(pageGroupData11.get("pageList")))
							{
								pageList11 = JSONArray.parseArray(pageGroupData11.getString("pageList"));
							}
							if (pageList11 != null && !pageList11.isEmpty())
							{
								for (Iterator<Object> iterator2 = pageList11.iterator(); iterator2.hasNext();)
								{
									JSONObject pageData = (JSONObject) iterator2.next();
									Document document = new Document();
									document.setGroupID(documentGroup11.getGroupID());
									document.setProjectID(project.getProjectID());
									document.setContentType(pageData.getInteger("contentType"));
									document.setContentRaw(pageData.getString("contentRaw"));
									document.setContent(pageData.getString("content"));
									document.setTitle(pageData.getString("title"));
									SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date date1 = null;
									Timestamp updateTime1 = null;
									try
									{
										date1 = dateFormat1.parse(pageData.getString("updateTime"));
										updateTime1 = new Timestamp(date1.getTime());
									}
									catch (ParseException e)
									{
										// TODO Auto-generated catch block
										date1 = new Date();
										updateTime1 = new Timestamp(date1.getTime());

									}
									document.setUpdateTime(updateTime1);
									document.setUserID(userID);
									if (documentMapper.addDocument(document) < 1)
										throw new RuntimeException("addDocument error");
								}
							}
						}
					}
				}
			}
			JSONArray envList = null;
			if (!Boolean.class.isInstance(projectData.get("env")))
			{
				envList = JSONArray.parseArray(projectData.getString("env"));
			}
			if (envList != null && !envList.isEmpty())
			{
				for (Iterator<Object> iterator = envList.iterator(); iterator.hasNext();)
				{
					JSONObject envData = (JSONObject) iterator.next();
					Env env = new Env();
					env.setEnvName(envData.getString("envName"));
					env.setProjectID(project.getProjectID());
					if (envMapper.addEnv(env) < 1)
						throw new RuntimeException("addEnv error");
					EnvFrontUri envFrontUri = new EnvFrontUri();
					if (envData.get("frontURI") != null && !Boolean.class.isInstance(envData.get("frontURI")))
					{
						JSONObject frontURIData = (JSONObject) envData.get("frontURI");
						envFrontUri.setUri(frontURIData.getString("uri"));
						envFrontUri.setApplyProtocol(frontURIData.getInteger("applyProtocol"));
					}
					else
					{
						envFrontUri.setUri(null);
						envFrontUri.setApplyProtocol(-1);
					}
					envFrontUri.setEnvID(env.getEnvID());
					if (envFrontUriMapper.addEnvFrontUri(envFrontUri) < 1)
						throw new RuntimeException("addEnvFrontUri error");
					JSONArray headerList = null;
					if (!Boolean.class.isInstance(envData.get("headerList")))
					{
						headerList = JSONArray.parseArray(envData.getString("headerList"));
					}
					if (headerList != null && !headerList.isEmpty())
					{
						for (Iterator<Object> iterator2 = headerList.iterator(); iterator2.hasNext();)
						{
							JSONObject headerData = (JSONObject) iterator2.next();
							EnvHeader envHeader = new EnvHeader();
							envHeader.setApplyProtocol(headerData.getInteger("applyProtocol"));
							envHeader.setEnvID(env.getEnvID());
							envHeader.setHeaderName(headerData.getString("headerName"));
							envHeader.setHeaderValue(headerData.getString("headerValue"));
							if (envHeaderMapper.addEnvHeader(envHeader) < 1)
								throw new RuntimeException("addEnvHeader error");
						}
					}
					JSONArray paramList = null;
					if (!Boolean.class.isInstance(envData.get("paramList")))
					{
						paramList = JSONArray.parseArray(envData.getString("paramList"));
					}
					if (paramList != null && !paramList.isEmpty())
					{
						for (Iterator<Object> iterator2 = paramList.iterator(); iterator2.hasNext();)
						{
							JSONObject paramData = (JSONObject) iterator2.next();
							EnvParam envParam = new EnvParam();
							envParam.setEnvID(env.getEnvID());
							envParam.setParamKey(paramData.getString("paramKey"));
							envParam.setParamValue(paramData.getString("paramValue"));
							if (envParamMapper.addEnvParam(envParam) < 1)
								throw new RuntimeException("addEnvParam error");
						}
					}
					JSONArray additionalParamList = null;
					if (!Boolean.class.isInstance(envData.get("additionalParamList")))
					{
						additionalParamList = JSONArray.parseArray(envData.getString("additionalParamList"));
					}
					if (additionalParamList != null && !additionalParamList.isEmpty())
					{
						for (Iterator<Object> iterator2 = additionalParamList.iterator(); iterator2.hasNext();)
						{
							JSONObject paramData = (JSONObject) iterator2.next();
							EnvParamAdditional envParamAdditional = new EnvParamAdditional();
							envParamAdditional.setEnvID(env.getEnvID());
							envParamAdditional.setParamKey(paramData.getString("paramKey"));
							envParamAdditional.setParamValue(paramData.getString("paramValue"));
							if (envParamAdditionalMapper.addEnvParamAdditional(envParamAdditional) < 1)
								throw new RuntimeException("addEnvParamAdditional error");
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean importPostmanV1(String data, Integer userID)
	{
		// TODO Auto-generated method stub
		JSONObject projectData = JSONObject.parseObject(data);
		if (projectData != null && !projectData.isEmpty())
		{
			Project project = new Project();
			project.setProjectType(0);
			project.setProjectVersion("1.0");
			project.setProjectName(projectData.getString("name"));
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			project.setProjectUpdateTime(updateTime);
			if (projectMapper.addProject(project) < 1)
				throw new RuntimeException("addProject error");
			Partner partner = new Partner();
			partner.setProjectID(project.getProjectID());
			partner.setUserID(userID);
			partner.setUserType(0);
			if (partnerMapper.addPartner(partner) < 1)
				throw new RuntimeException("addPartner error");
			List<Map<String, Object>> groupInfoList = new ArrayList<>();
			Map<String, Object> resutl = new HashMap<>();
			resutl.put("groupName", "默认分组");
			resutl.put("folderID", "default");
			groupInfoList.add(resutl);
			JSONArray folders = JSONArray.parseArray(projectData.getString("folders"));
			if (folders != null && !folders.isEmpty())
			{
				for (Iterator<Object> iterator = folders.iterator(); iterator.hasNext();)
				{
					Map<String, Object> map = new HashMap<>();
					JSONObject folderData = (JSONObject) iterator.next();
					map.put("groupName", folderData.get("name"));
					map.put("folderID", folderData.get("id"));
					groupInfoList.add(map);
				}
			}
			if (groupInfoList != null && !groupInfoList.isEmpty())
			{
				for (Map<String, Object> groupInfo : groupInfoList)
				{
					JSONArray requests = JSONArray.parseArray(projectData.getString("requests"));
					ApiGroup apiGroup = new ApiGroup();
					apiGroup.setGroupName((String) groupInfo.get("groupName"));
					apiGroup.setIsChild(0);
					apiGroup.setParentGroupID(0);
					apiGroup.setProjectID(project.getProjectID());
					if (apiGroupMapper.addApiGroup(apiGroup) < 1)
						throw new RuntimeException("addApiGroup error");
					if (requests != null && !requests.isEmpty())
					{
						for (Iterator<Object> iterator = requests.iterator(); iterator.hasNext();)
						{
							Api api = new Api();
							JSONObject requestData = (JSONObject) iterator.next();
							if (requestData.get("folder") == null)
							{
								requestData.put("folder", "default");
							}
							if (!requestData.getString("folder").equals(groupInfo.get("folderID")))
								continue;
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
							api.setApiUpdateTime(updateTime);
							api.setApiName(requestData.getString("name"));
							api.setApiURI(requestData.getString("url"));
							int apiProtocol = requestData.getString("url").contains("https") ? 1 : 0;
							api.setApiProtocol(apiProtocol);
							api.setApiStatus(0);
							switch (requestData.getString("method"))
							{
								case "POST":
									api.setApiRequestType(0);
									break;
								case "GET":
									api.setApiRequestType(1);
									break;
								case "PUT":
									api.setApiRequestType(2);
									break;
								case "DELETE":
									api.setApiRequestType(3);
									break;
								case "HEAD":
									api.setApiRequestType(4);
									break;
								case "OPTIONS":
									api.setApiRequestType(5);
									break;
								case "PATCH":
									api.setApiRequestType(6);
									break;
							}
							api.setGroupID(apiGroup.getGroupID());
							api.setProjectID(project.getProjectID());
							api.setUpdateUserID(userID);
							Integer result = apiMapper.addApi(api);
							if (result > 0)
							{
								List<Map<String, Object>> apiHeader = new ArrayList<>();
								JSONArray headers = requestData.getJSONArray("headerData");
								if (headers != null && !headers.isEmpty())
								{
									for (Iterator<Object> iterator1 = headers.iterator(); iterator1.hasNext();)
									{
										JSONObject headerData = (JSONObject) iterator1.next();
										Map<String, Object> map = new HashMap<>();
										map.put("headerName", headerData.getString("key"));
										map.put("headerValue", headerData.getString("value"));
										apiHeader.add(map);
										ApiHeader head = new ApiHeader();
										head.setHeaderName(headerData.getString("key"));
										head.setHeaderValue(headerData.getString("value"));
										head.setApiID(api.getApiID());
										if (apiMapper.addApiHeader(head) < 1)
											throw new RuntimeException("addApiHeader error");
									}
								}

								List<Map<String, Object>> apiRequestParam = new ArrayList<>();

								JSONArray requestParamList = requestData.getJSONArray("data");
								if (requestParamList != null && !requestParamList.isEmpty())
								{
									for (Iterator<Object> iterator1 = requestParamList.iterator(); iterator1.hasNext();)
									{
										JSONObject object = (JSONObject) iterator1.next();
										Map<String, Object> map = new HashMap<>();
										ApiRequestParam requestParam = new ApiRequestParam();
										requestParam.setApiID(api.getApiID());
										requestParam.setParamName("");
										requestParam.setParamKey(object.getString("key"));
										requestParam.setParamValue(object.getString("value"));
										requestParam.setParamType(object.getString("type") != null ? 0 : 1);
										requestParam.setParamLimit("");
										requestParam.setParamNotNull(object.get("enabled") != null ? 0 : 1);
										if (apiMapper.addRequestParam(requestParam) < 1)
											throw new RuntimeException("addRequestParam error");
										map.put("paramName", "");
										map.put("paramKey", object.getString("key"));
										map.put("paramValue", object.getString("value"));
										map.put("paramType", object.getString("type") != null ? 0 : 1);
										map.put("paramLimit", "");
										map.put("paramNotNull", object.get("enabled") != null ? 0 : 1);
										map.put("paramValueList", new ArrayList<Map<String, Object>>());
										apiRequestParam.add(map);
									}
								}
								Map<String, Object> cache = new HashMap<String, Object>();
								Map<String, Object> mockInfo = new HashMap<String, Object>();
								cache.put("baseInfo", api);
								cache.put("headerInfo", apiHeader);
								mockInfo.put("mockResult", api.getMockResult());
								mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
								mockInfo.put("mockConfig", api.getMockConfig());
								cache.put("mockInfo", mockInfo);
								cache.put("requestInfo", apiRequestParam);
								cache.put("resultInfo", new ArrayList<Map<String, Object>>());
								ApiCache apiCache = new ApiCache();
								apiCache.setApiID(api.getApiID());
								apiCache.setApiJson(JSON.toJSONString(cache));
								apiCache.setGroupID(api.getGroupID());
								apiCache.setProjectID(api.getProjectID());
								apiCache.setStarred(api.getStarred());
								apiCache.setUpdateUserID(api.getUpdateUserID());
								if (apiCacheMapper.addApiCache(apiCache) < 1)
									throw new RuntimeException("addApiCache error");
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean importPostmanV2(String data, Integer userID)
	{
		// TODO Auto-generated method stub
		JSONObject projectData = JSONObject.parseObject(data);
		if (projectData != null && !projectData.isEmpty())
		{
			JSONObject projectInfo = projectData.getJSONObject("info");
			Project project = new Project();
			project.setProjectType(0);
			project.setProjectVersion("1.0");
			project.setProjectName(projectInfo.getString("name"));
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			project.setProjectUpdateTime(updateTime);
			if (projectMapper.addProject(project) < 1)
				throw new RuntimeException("addProject error");
			Partner partner = new Partner();
			partner.setProjectID(project.getProjectID());
			partner.setUserID(userID);
			partner.setUserType(0);
			if (partnerMapper.addPartner(partner) < 1)
				throw new RuntimeException("addPartner error");
			ApiGroup defaultapiGroup = new ApiGroup();
			defaultapiGroup.setGroupName("默认分组");
			defaultapiGroup.setIsChild(0);
			defaultapiGroup.setParentGroupID(0);
			defaultapiGroup.setProjectID(project.getProjectID());
			if (apiGroupMapper.addApiGroup(defaultapiGroup) < 1)
				throw new RuntimeException("addApiGroup error");
			JSONArray items = projectData.getJSONArray("item");
			if (items != null && !items.isEmpty())
			{
				for (Iterator<Object> iterator = items.iterator(); iterator.hasNext();)
				{
					JSONObject item = (JSONObject) iterator.next();
					if (item.get("item") == null)
					{
						Api api = new Api();
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
						api.setApiUpdateTime(updateTime);
						api.setApiName(item.getString("name"));
						JSONObject request = item.getJSONObject("request");
						JSONObject url = null;
						if (request.get("url") != null && !String.class.isInstance(request.get("url")))
						{
							url = request.getJSONObject("url");
						}
						if (request.getString("method").equalsIgnoreCase("GET") && url != null
								&& url.get("raw") != null)
							api.setApiURI(url.getString("raw").split("\\?")[0]);
						else
							api.setApiURI(request.getString("url"));
						int apiProtocol = 0;
						if (url != null && url.get("raw") != null)
							apiProtocol = url.getString("raw").contains("https") ? 1 : 0;
						else
							apiProtocol = request.getString("url").contains("https") ? 1 : 0;
						api.setApiProtocol(apiProtocol);
						api.setApiStatus(0);
						switch (request.getString("method"))
						{
							case "POST":
								api.setApiRequestType(0);
								break;
							case "GET":
								api.setApiRequestType(1);
								break;
							case "PUT":
								api.setApiRequestType(2);
								break;
							case "DELETE":
								api.setApiRequestType(3);
								break;
							case "HEAD":
								api.setApiRequestType(4);
								break;
							case "OPTIONS":
								api.setApiRequestType(5);
								break;
							case "PATCH":
								api.setApiRequestType(6);
								break;
						}
						api.setGroupID(defaultapiGroup.getGroupID());
						api.setProjectID(project.getProjectID());
						api.setUpdateUserID(userID);
						Integer result = apiMapper.addApi(api);
						if (result > 0)
						{
							List<Map<String, Object>> apiHeader = new ArrayList<>();
							JSONArray headers = request.getJSONArray("header");
							if (headers != null && !headers.isEmpty())
							{
								for (Iterator<Object> iterator1 = headers.iterator(); iterator1.hasNext();)
								{
									JSONObject headerData = (JSONObject) iterator1.next();
									Map<String, Object> map = new HashMap<>();
									map.put("headerName", headerData.getString("key"));
									map.put("headerValue", headerData.getString("value"));
									apiHeader.add(map);
									ApiHeader head = new ApiHeader();
									head.setHeaderName(headerData.getString("key"));
									head.setHeaderValue(headerData.getString("value"));
									head.setApiID(api.getApiID());
									if (apiMapper.addApiHeader(head) < 1)
										throw new RuntimeException("addApiHeader error");
								}
							}

							List<Map<String, Object>> apiRequestParam = new ArrayList<>();
							JSONObject body = request.getJSONObject("body");
							if (body != null && !body.isEmpty())
							{
								if (body.get("mode") != null && body.getString("mode").equals("formdata"))
								{
									JSONArray formdata = body.getJSONArray("formdata");
									if (formdata != null && !formdata.isEmpty())
									{
										for (Iterator<Object> iterator2 = formdata.iterator(); iterator2.hasNext();)
										{
											JSONObject object = (JSONObject) iterator2.next();
											Map<String, Object> map = new HashMap<>();
											ApiRequestParam requestParam = new ApiRequestParam();
											requestParam.setApiID(api.getApiID());
											requestParam.setParamName("");
											requestParam.setParamKey(object.getString("key"));
											requestParam.setParamValue(object.getString("value"));
											requestParam.setParamType(object.getString("type") != null ? 0 : 1);
											requestParam.setParamLimit("");
											requestParam.setParamNotNull(object.get("enabled") != null ? 0 : 1);
											if (apiMapper.addRequestParam(requestParam) < 1)
												throw new RuntimeException("addRequestParam error");
											map.put("paramName", "");
											map.put("paramKey", object.getString("key"));
											map.put("paramValue", object.getString("value"));
											map.put("paramType", object.getString("type") != null ? 0 : 1);
											map.put("paramLimit", "");
											map.put("paramNotNull", object.get("enabled") != null ? 0 : 1);
											map.put("paramValueList", new ArrayList<Map<String, Object>>());
											apiRequestParam.add(map);
										}
									}
								}
							}
							if (url != null && request.getString("method").equals("GET") && url.get("raw") != null)
							{
								JSONArray formdata = url.getJSONArray("query");
								if (formdata != null && !formdata.isEmpty())
								{
									for (Iterator<Object> iterator2 = formdata.iterator(); iterator2.hasNext();)
									{
										JSONObject object = (JSONObject) iterator2.next();
										Map<String, Object> map = new HashMap<>();
										ApiRequestParam requestParam = new ApiRequestParam();
										requestParam.setApiID(api.getApiID());
										requestParam.setParamName("");
										requestParam.setParamKey(object.getString("key"));
										requestParam.setParamValue(object.getString("value"));
										requestParam.setParamType(0);
										requestParam.setParamLimit("");
										requestParam.setParamNotNull(object.get("equals") != null ? 0 : 1);
										if (apiMapper.addRequestParam(requestParam) < 1)
											throw new RuntimeException("addRequestParam error");
										map.put("paramName", "");
										map.put("paramKey", object.getString("key"));
										map.put("paramValue", object.getString("value"));
										map.put("paramType", 0);
										map.put("paramLimit", "");
										map.put("paramNotNull", object.get("equals") != null ? 0 : 1);
										map.put("paramValueList", new ArrayList<Map<String, Object>>());
										apiRequestParam.add(map);
									}
								}
							}

							Map<String, Object> cache = new HashMap<String, Object>();
							Map<String, Object> mockInfo = new HashMap<String, Object>();
							cache.put("baseInfo", api);
							cache.put("headerInfo", apiHeader);
							mockInfo.put("mockResult", api.getMockResult());
							mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
							mockInfo.put("mockConfig", api.getMockConfig());
							cache.put("mockInfo", mockInfo);
							cache.put("requestInfo", apiRequestParam);
							cache.put("resultInfo", new ArrayList<Map<String, Object>>());
							ApiCache apiCache = new ApiCache();
							apiCache.setApiID(api.getApiID());
							apiCache.setApiJson(JSON.toJSONString(cache));
							apiCache.setGroupID(api.getGroupID());
							apiCache.setProjectID(api.getProjectID());
							apiCache.setStarred(api.getStarred());
							apiCache.setUpdateUserID(api.getUpdateUserID());
							if (apiCacheMapper.addApiCache(apiCache) < 1)
								throw new RuntimeException("addApiCache error");
						}
					}
					else
					{
						ApiGroup apiGroup = new ApiGroup();
						apiGroup.setGroupName(item.getString("name"));
						apiGroup.setIsChild(0);
						apiGroup.setParentGroupID(0);
						apiGroup.setProjectID(project.getProjectID());
						if (apiGroupMapper.addApiGroup(apiGroup) < 1)
							throw new RuntimeException("addApiGroup error");
						JSONArray apiList = item.getJSONArray("item");
						if (apiList != null && !apiList.isEmpty())
						{
							for (Iterator<Object> iterator2 = apiList.iterator(); iterator2.hasNext();)
							{
								JSONObject apiData = (JSONObject) iterator2.next();
								Api api = new Api();
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
								if (api.getApiNote() == null
										|| api.getApiNote().equals("&lt;p&gt;&lt;br&gt;&lt;/p&gt;"))
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
								api.setApiUpdateTime(updateTime);
								api.setApiName(apiData.getString("name"));
								JSONObject request = apiData.getJSONObject("request");
								if (request == null)
									continue;
								JSONObject url = null;
								if (request.get("url") != null && !String.class.isInstance(request.get("url")))
								{
									url = request.getJSONObject("url");
								}
								if (request.getString("method").equalsIgnoreCase("GET") && url != null
										&& url.get("raw") != null)
									api.setApiURI(url.getString("raw").split("\\?")[0]);
								else
									api.setApiURI(request.getString("url"));
								int apiProtocol = 0;
								if (url != null && url.get("raw") != null)
									apiProtocol = url.getString("raw").contains("https") ? 1 : 0;
								else
									apiProtocol = request.getString("url").contains("https") ? 1 : 0;
								JSONObject body = request.getJSONObject("body");
								api.setApiRequestRaw(body.getString("raw") != null ? body.getString("raw") : "");
								api.setApiProtocol(apiProtocol);
								api.setApiStatus(0);
								if (body.get("mode") != null && body.getString("mode").equals("raw"))
									api.setApiRequestParamType(1);
								else
									api.setApiRequestParamType(0);
								switch (request.getString("method"))
								{
									case "POST":
										api.setApiRequestType(0);
										break;
									case "GET":
										api.setApiRequestType(1);
										break;
									case "PUT":
										api.setApiRequestType(2);
										break;
									case "DELETE":
										api.setApiRequestType(3);
										break;
									case "HEAD":
										api.setApiRequestType(4);
										break;
									case "OPTIONS":
										api.setApiRequestType(5);
										break;
									case "PATCH":
										api.setApiRequestType(6);
										break;
								}
								api.setGroupID(apiGroup.getGroupID());
								api.setProjectID(project.getProjectID());
								api.setUpdateUserID(userID);
								Integer result = apiMapper.addApi(api);
								if (result > 0)
								{
									List<Map<String, Object>> apiHeader = new ArrayList<>();
									JSONArray headers = request.getJSONArray("header");
									if (headers != null && !headers.isEmpty())
									{
										for (Iterator<Object> iterator1 = headers.iterator(); iterator1.hasNext();)
										{
											JSONObject headerData = (JSONObject) iterator1.next();
											Map<String, Object> map = new HashMap<>();
											map.put("headerName", headerData.getString("key"));
											map.put("headerValue", headerData.getString("value"));
											apiHeader.add(map);
											ApiHeader head = new ApiHeader();
											head.setHeaderName(headerData.getString("key"));
											head.setHeaderValue(headerData.getString("value"));
											head.setApiID(api.getApiID());
											if (apiMapper.addApiHeader(head) < 1)
												throw new RuntimeException("addApiHeader error");
										}
									}

									List<Map<String, Object>> apiRequestParam = new ArrayList<>();
									if (body != null && !body.isEmpty())
									{
										if (body.get("mode") != null && body.getString("mode").equals("formdata"))
										{
											JSONArray formdata = body.getJSONArray("formdata");
											if (formdata != null && !formdata.isEmpty())
											{
												for (Iterator<Object> iterator21 = formdata.iterator(); iterator21
														.hasNext();)
												{
													JSONObject object = (JSONObject) iterator21.next();
													Map<String, Object> map = new HashMap<>();
													ApiRequestParam requestParam = new ApiRequestParam();
													requestParam.setApiID(api.getApiID());
													requestParam.setParamName("");
													requestParam.setParamKey(object.getString("key"));
													requestParam.setParamValue(object.getString("value"));
													requestParam.setParamType(object.getString("type") != null ? 0 : 1);
													requestParam.setParamLimit("");
													requestParam.setParamNotNull(object.get("enabled") != null ? 0 : 1);
													if (apiMapper.addRequestParam(requestParam) < 1)
														throw new RuntimeException("addRequestParam error");
													map.put("paramName", "");
													map.put("paramKey", object.getString("key"));
													map.put("paramValue", object.getString("value"));
													map.put("paramType", object.getString("type") != null ? 0 : 1);
													map.put("paramLimit", "");
													map.put("paramNotNull", object.get("enabled") != null ? 0 : 1);
													map.put("paramValueList", new ArrayList<Map<String, Object>>());
													apiRequestParam.add(map);
												}
											}
										}
									}
									if (url != null && request.getString("method").equals("GET")
											&& url.get("raw") != null)
									{
										JSONArray formdata = url.getJSONArray("query");
										if (formdata != null && !formdata.isEmpty())
										{
											for (Iterator<Object> iterator21 = formdata.iterator(); iterator21
													.hasNext();)
											{
												JSONObject object = (JSONObject) iterator21.next();
												Map<String, Object> map = new HashMap<>();
												ApiRequestParam requestParam = new ApiRequestParam();
												requestParam.setApiID(api.getApiID());
												requestParam.setParamName("");
												requestParam.setParamKey(object.getString("key"));
												requestParam.setParamValue(object.getString("value"));
												requestParam.setParamType(0);
												requestParam.setParamLimit("");
												requestParam.setParamNotNull(object.get("equals") != null ? 0 : 1);
												if (apiMapper.addRequestParam(requestParam) < 1)
													throw new RuntimeException("addRequestParam error");
												map.put("paramName", "");
												map.put("paramKey", object.getString("key"));
												map.put("paramValue", object.getString("value"));
												map.put("paramType", 0);
												map.put("paramLimit", "");
												map.put("paramNotNull", object.get("equals") != null ? 0 : 1);
												map.put("paramValueList", new ArrayList<Map<String, Object>>());
												apiRequestParam.add(map);
											}
										}
									}

									Map<String, Object> cache = new HashMap<String, Object>();
									Map<String, Object> mockInfo = new HashMap<String, Object>();
									cache.put("baseInfo", api);
									cache.put("headerInfo", apiHeader);
									mockInfo.put("mockResult", api.getMockResult());
									mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
									mockInfo.put("mockConfig", api.getMockConfig());
									cache.put("mockInfo", mockInfo);
									cache.put("requestInfo", apiRequestParam);
									cache.put("resultInfo", new ArrayList<Map<String, Object>>());
									ApiCache apiCache = new ApiCache();
									apiCache.setApiID(api.getApiID());
									apiCache.setApiJson(JSON.toJSONString(cache));
									apiCache.setGroupID(api.getGroupID());
									apiCache.setProjectID(api.getProjectID());
									apiCache.setStarred(api.getStarred());
									apiCache.setUpdateUserID(api.getUpdateUserID());
									if (apiCacheMapper.addApiCache(apiCache) < 1)
										throw new RuntimeException("addApiCache error");
								}
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean importSwagger(String data, Integer userID)
	{
		// TODO Auto-generated method stub
		JSONObject swagger = JSONObject.parseObject(data);
		if (swagger != null && !swagger.isEmpty())
		{
			JSONObject projectInfo = swagger.getJSONObject("info");
			Project project = new Project();
			project.setProjectType(0);
			project.setProjectVersion(projectInfo.getString("version"));
			project.setProjectName(projectInfo.getString("title"));
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			project.setProjectUpdateTime(updateTime);
			if (projectMapper.addProject(project) < 1)
				throw new RuntimeException("addProject error");
			Partner partner = new Partner();
			partner.setProjectID(project.getProjectID());
			partner.setUserID(userID);
			partner.setUserType(0);
			if (partnerMapper.addPartner(partner) < 1)
				throw new RuntimeException("addPartner error");
			ApiGroup defaultapiGroup = new ApiGroup();
			defaultapiGroup.setGroupName("默认分组");
			defaultapiGroup.setIsChild(0);
			defaultapiGroup.setParentGroupID(0);
			defaultapiGroup.setProjectID(project.getProjectID());
			if (apiGroupMapper.addApiGroup(defaultapiGroup) < 1)
				throw new RuntimeException("addApiGroup error");
			JSONArray schemes = swagger.getJSONArray("schemes");
			Integer apiProtocol = 0;
			if (schemes != null && !schemes.isEmpty())
			{
				if (schemes.get(0).toString().equalsIgnoreCase("HTTPS"))
				{
					apiProtocol = 1;
				}
			}
			JSONObject paths = swagger.getJSONObject("paths");
			if (paths != null && !paths.isEmpty())
			{
				Map<String, Integer> groupInfo = new HashMap<>();
				for (String apiURI : paths.keySet())
				{
					JSONObject apiInfoList = paths.getJSONObject(apiURI);
					for (String requestType : apiInfoList.keySet())
					{
						JSONObject apiInfo = apiInfoList.getJSONObject(requestType);
						Api api = new Api();
						if (apiInfo.get("summary") == null)
						{
							api.setApiName(apiInfo.getString("operationId"));
						}
						else
						{
							api.setApiName(apiInfo.getString("summary"));
						}
						String groupName = apiInfo.getJSONArray("tags").getString(0);
						if (groupInfo.get(groupName) == null)
						{
							ApiGroup apiGroup = new ApiGroup();
							apiGroup.setGroupName(groupName);
							apiGroup.setIsChild(0);
							apiGroup.setParentGroupID(0);
							apiGroup.setProjectID(project.getProjectID());
							if (apiGroupMapper.addApiGroup(apiGroup) < 1)
								throw new RuntimeException("addApiGroup error");
							groupInfo.put(groupName, apiGroup.getGroupID());
						}
						api.setApiURI(apiURI);
						api.setApiStatus(0);
						api.setApiRequestParamType(0);
						api.setStarred(0);
						api.setApiNoteType(0);
						api.setApiProtocol(apiProtocol);
						api.setApiUpdateTime(updateTime);
						switch (requestType)
						{
							case "post":
								api.setApiRequestType(0);
								break;
							case "get":
								api.setApiRequestType(1);
								break;
							case "put":
								api.setApiRequestType(2);
								break;
							case "delete":
								api.setApiRequestType(3);
								break;
							case "head":
								api.setApiRequestType(4);
								break;
							case "options":
								api.setApiRequestType(5);
								break;
							case "patch":
								api.setApiRequestType(6);
								break;
						}
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

						api.setGroupID(groupInfo.get(groupName));
						api.setProjectID(project.getProjectID());
						api.setUpdateUserID(userID);
						Integer result = apiMapper.addApi(api);
						if (result > 0)
						{
							List<Map<String, Object>> apiHeader = new ArrayList<>();
							JSONArray consumes = apiInfo.getJSONArray("consumes");
							if (consumes != null && !consumes.isEmpty())
							{
								for (Object headerValue : consumes)
								{
									Map<String, Object> map = new HashMap<>();
									map.put("headerName", "Content-Type");
									map.put("headerValue", String.valueOf(headerValue));
									apiHeader.add(map);
									ApiHeader head = new ApiHeader();
									head.setHeaderName("Content-Type");
									head.setHeaderValue(String.valueOf(headerValue));
									head.setApiID(api.getApiID());
									if (apiMapper.addApiHeader(head) < 1)
										throw new RuntimeException("addApiHeader error");
								}
							}
							JSONArray produces = apiInfo.getJSONArray("produces");
							if (produces != null && !produces.isEmpty())
							{
								for (Object headerValue : produces)
								{
									Map<String, Object> map = new HashMap<>();
									map.put("headerName", "Accept");
									map.put("headerValue", String.valueOf(headerValue));
									apiHeader.add(map);
									ApiHeader head = new ApiHeader();
									head.setHeaderName("Accept");
									head.setHeaderValue(String.valueOf(headerValue));
									head.setApiID(api.getApiID());
									if (apiMapper.addApiHeader(head) < 1)
										throw new RuntimeException("addApiHeader error");
								}
							}
							List<Map<String, Object>> apiRequestParam = new ArrayList<>();
							JSONArray parameters = apiInfo.getJSONArray("parameters");
							if (parameters != null && !parameters.isEmpty())
							{
								for (Iterator<Object> iterator = parameters.iterator(); iterator.hasNext();)
								{
									JSONObject parameter = (JSONObject) iterator.next();
									Map<String, Object> map = new HashMap<>();
									ApiRequestParam requestParam = new ApiRequestParam();
									requestParam.setApiID(api.getApiID());
									requestParam.setParamName(parameter.getString("description"));
									requestParam.setParamKey(parameter.getString("name"));
									requestParam.setParamValue("");
									Integer paramType = 0;
									if (parameter.get("type") != null)
									{
										switch (parameter.getString("type"))
										{
											case "integer":
												paramType = 3;
												break;
											case "string":
												paramType = 0;
												break;
											case "long":
												paramType = 11;
												break;
											case "float":
												paramType = 4;
												break;
											case "double":
												paramType = 5;
												break;
											case "byte":
												paramType = 9;
												break;
											case "file":
												paramType = 1;
												break;
											case "date":
												paramType = 6;
												break;
											case "dateTime":
												paramType = 7;
												break;
											case "boolean":
												paramType = 8;
												break;
											case "array":
												paramType = 12;
												break;
											case "json":
												paramType = 2;
												break;
											case "object":
												paramType = 13;
												break;
											case "number":
												paramType = 14;
												break;
											default:
												paramType = 0;
										}
									}

									requestParam.setParamType(paramType);
									requestParam.setParamLimit("");
									requestParam.setParamNotNull(parameter.get("required") != null ? 0 : 1);
									if (apiMapper.addRequestParam(requestParam) < 1)
										throw new RuntimeException("addRequestParam error");
									map.put("paramName", parameter.getString("description"));
									map.put("paramKey", parameter.getString("name"));
									map.put("paramValue", "");
									map.put("paramType", paramType);
									map.put("paramLimit", "");
									map.put("paramNotNull", parameter.get("required") != null ? 0 : 1);
									map.put("paramValueList", new ArrayList<Map<String, Object>>());
									apiRequestParam.add(map);
								}

							}
							List<Map<String, Object>> apiResultParam = new ArrayList<>();
							JSONObject responses = apiInfo.getJSONObject("responses");
							if (responses != null && !responses.isEmpty())
							{
								for (String paramKey : responses.keySet())
								{
									JSONObject parameter = responses.getJSONObject(paramKey);
									Map<String, Object> map = new HashMap<>();
									ApiResultParam resultParam = new ApiResultParam();
									resultParam.setApiID(api.getApiID());
									resultParam.setParamName(parameter.getString("description"));
									resultParam.setParamKey(paramKey);
									Integer paramType = 0;
									JSONObject schema = parameter.getJSONObject("schema");
									if (schema != null && !schema.isEmpty())
									{
										if (schema.get("type") != null)
										{
											switch (schema.getString("type"))
											{
												case "integer":
													paramType = 3;
													break;
												case "string":
													paramType = 0;
													break;
												case "long":
													paramType = 11;
													break;
												case "float":
													paramType = 4;
													break;
												case "double":
													paramType = 5;
													break;
												case "byte":
													paramType = 9;
													break;
												case "file":
													paramType = 1;
													break;
												case "date":
													paramType = 6;
													break;
												case "dateTime":
													paramType = 7;
													break;
												case "boolean":
													paramType = 8;
													break;
												case "array":
													paramType = 12;
													break;
												case "json":
													paramType = 2;
													break;
												case "object":
													paramType = 13;
													break;
												case "number":
													paramType = 14;
													break;
												default:
													paramType = 0;
											}
										}

									}
									resultParam.setParamType(paramType);
									resultParam.setParamNotNull(parameter.get("required") != null ? 0 : 1);
									if (apiMapper.addResultParam(resultParam) < 1)
										throw new RuntimeException("addresultParam error");
									map.put("paramName", parameter.getString("description"));
									map.put("paramKey", paramKey);
									map.put("paramValue", "");
									map.put("paramType", paramType);
									map.put("paramLimit", "");
									map.put("paramNotNull", parameter.get("required") != null ? 0 : 1);
									map.put("paramValueList", new ArrayList<Map<String, Object>>());
									apiRequestParam.add(map);
								}
							}
							Map<String, Object> cache = new HashMap<String, Object>();
							Map<String, Object> mockInfo = new HashMap<String, Object>();
							cache.put("baseInfo", api);
							cache.put("headerInfo", apiHeader);
							mockInfo.put("mockResult", api.getMockResult());
							mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
							mockInfo.put("mockConfig", api.getMockConfig());
							cache.put("mockInfo", mockInfo);
							cache.put("requestInfo", apiRequestParam);
							cache.put("resultInfo", apiResultParam);
							ApiCache apiCache = new ApiCache();
							apiCache.setApiID(api.getApiID());
							apiCache.setApiJson(JSON.toJSONString(cache));
							apiCache.setGroupID(api.getGroupID());
							apiCache.setProjectID(api.getProjectID());
							apiCache.setStarred(api.getStarred());
							apiCache.setUpdateUserID(api.getUpdateUserID());
							if (apiCacheMapper.addApiCache(apiCache) < 1)
								throw new RuntimeException("addApiCache error");
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean importRAP(String data, Integer userID)
	{
		// TODO Auto-generated method stub
		JSONObject projectData = JSONObject.parseObject(data);
		if (projectData != null && !projectData.isEmpty())
		{
			Project project = new Project();
			project.setProjectType(0);
			project.setProjectVersion("1.0");
			project.setProjectName(projectData.getString("name"));
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			project.setProjectUpdateTime(updateTime);
			if (projectMapper.addProject(project) < 1)
				throw new RuntimeException("addProject error");
			Partner partner = new Partner();
			partner.setProjectID(project.getProjectID());
			partner.setUserID(userID);
			partner.setUserType(0);
			if (partnerMapper.addPartner(partner) < 1)
				throw new RuntimeException("addPartner error");
			ApiGroup defaultapiGroup = new ApiGroup();
			defaultapiGroup.setGroupName("默认分组");
			defaultapiGroup.setIsChild(0);
			defaultapiGroup.setParentGroupID(0);
			defaultapiGroup.setProjectID(project.getProjectID());
			if (apiGroupMapper.addApiGroup(defaultapiGroup) < 1)
				throw new RuntimeException("addApiGroup error");
			JSONArray moduleList = projectData.getJSONArray("moduleList");
			if (moduleList != null && !moduleList.isEmpty())
			{
				for (Iterator<Object> iterator = moduleList.iterator(); iterator.hasNext();)
				{
					JSONObject moduleData = (JSONObject) iterator.next();
					ApiGroup apiGroup = new ApiGroup();
					apiGroup.setGroupName(moduleData.getString("name"));
					apiGroup.setIsChild(0);
					apiGroup.setParentGroupID(0);
					apiGroup.setProjectID(project.getProjectID());
					if (apiGroupMapper.addApiGroup(apiGroup) < 1)
						throw new RuntimeException("addApiGroup error");
					JSONArray pageList = moduleData.getJSONArray("pageList");
					if (pageList != null && !pageList.isEmpty())
					{
						for (Iterator<Object> iterator1 = pageList.iterator(); iterator1.hasNext();)
						{
							JSONObject pageData = (JSONObject) iterator1.next();
							ApiGroup apiGroup1 = new ApiGroup();
							apiGroup1.setGroupName(pageData.getString("name"));
							apiGroup1.setIsChild(1);
							apiGroup1.setParentGroupID(apiGroup.getGroupID());
							apiGroup1.setProjectID(project.getProjectID());
							if (apiGroupMapper.addApiGroup(apiGroup1) < 1)
								throw new RuntimeException("addApiGroup error");
							JSONArray actionList = pageData.getJSONArray("actionList");
							if (actionList != null && !actionList.isEmpty())
							{
								for (Iterator<Object> iterator11 = actionList.iterator(); iterator11.hasNext();)
								{
									JSONObject actionData = (JSONObject) iterator11.next();
									Api api = new Api();
									if (api.getApiFailureMock() == null)
									{
										api.setApiFailureMock("");
									}
									if (api.getApiRequestRaw() == null)
									{
										api.setApiRequestRaw("");
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
									api.setApiUpdateTime(updateTime);
									api.setApiName(actionData.getString("name"));
									api.setApiURI(actionData.getString("requestUrl"));
									api.setApiSuccessMock(actionData.getString("responseTemplate"));
									int apiProtocol = 1;
									api.setApiProtocol(apiProtocol);
									api.setApiStatus(0);
									api.setApiNote("&lt;p&gt;" + actionData.getString("description") + "&lt;p&gt;");
									switch (actionData.getString("requestType"))
									{
										case "1":
											api.setApiRequestType(1);
											break;
										case "2":
											api.setApiRequestType(0);
											break;
										case "3":
											api.setApiRequestType(2);
											break;
										case "4":
											api.setApiRequestType(3);
											break;
										default:
											api.setApiRequestType(1);
											break;
									}
									api.setGroupID(apiGroup1.getGroupID());
									api.setProjectID(project.getProjectID());
									api.setUpdateUserID(userID);
									Integer result = apiMapper.addApi(api);
									if (result > 0)
									{
										List<Map<String, Object>> apiHeader = new ArrayList<>();

										List<Map<String, Object>> apiRequestParam = new ArrayList<>();

										List<Map<String, Object>> apiResultParam = new ArrayList<>();

										JSONArray requestParamList = actionData.getJSONArray("requestParameterList");
										if (requestParamList != null && !requestParamList.isEmpty())
										{
											for (Iterator<Object> iterator111 = requestParamList.iterator(); iterator111
													.hasNext();)
											{
												JSONObject object = (JSONObject) iterator111.next();
												Map<String, Object> map = new HashMap<>();
												ApiRequestParam requestParam = new ApiRequestParam();
												requestParam.setApiID(api.getApiID());
												requestParam.setParamName(object.getString("name"));
												requestParam.setParamKey(object.getString("identifier"));
												requestParam.setParamValue(object.getString("remark"));
												Integer paramType = 12;
												if (object.get("type") != null)
												{
													switch (object.getString("type"))
													{
														case "integer":
															paramType = 3;
															break;
														case "string":
															paramType = 0;
															break;
														case "long":
															paramType = 11;
															break;
														case "float":
															paramType = 4;
															break;
														case "double":
															paramType = 5;
															break;
														case "byte":
															paramType = 9;
															break;
														case "file":
															paramType = 1;
															break;
														case "date":
															paramType = 6;
															break;
														case "dateTime":
															paramType = 7;
															break;
														case "boolean":
															paramType = 8;
															break;
														case "array":
															paramType = 12;
															break;
														case "json":
															paramType = 2;
															break;
														case "object":
															paramType = 13;
															break;
														case "number":
															paramType = 14;
															break;
														default:
															paramType = 0;
													}
												}

												requestParam.setParamType(paramType);
												requestParam.setParamLimit(object.getString("dataType"));
												requestParam.setParamNotNull(0);
												if (apiMapper.addRequestParam(requestParam) < 1)
													throw new RuntimeException("addRequestParam error");
												map.put("paramName", object.getString("name"));
												map.put("paramKey", object.getString("identifier"));
												map.put("paramValue", object.getString("remark"));
												map.put("paramType", paramType);
												map.put("paramLimit", "");
												map.put("paramNotNull", 0);
												map.put("paramValueList", new ArrayList<Map<String, Object>>());
												apiRequestParam.add(map);
												JSONArray requestParamList1 = object.getJSONArray("parameterList");
												if (requestParamList1 != null && !requestParamList1.isEmpty())
												{
													for (Iterator<Object> iterator1111 = requestParamList1
															.iterator(); iterator1111.hasNext();)
													{
														JSONObject object1 = (JSONObject) iterator1111.next();
														Map<String, Object> map1 = new HashMap<>();
														ApiRequestParam requestParam1 = new ApiRequestParam();
														requestParam1.setApiID(api.getApiID());
														requestParam1.setParamName(object1.getString("name"));
														requestParam1.setParamKey(
																requestParam + ">>" + object1.getString("identifier"));
														requestParam1.setParamValue(object1.getString("remark"));
														if (object1.get("type") != null)
														{
															switch (object1.getString("type"))
															{
																case "integer":
																	paramType = 3;
																	break;
																case "string":
																	paramType = 0;
																	break;
																case "long":
																	paramType = 11;
																	break;
																case "float":
																	paramType = 4;
																	break;
																case "double":
																	paramType = 5;
																	break;
																case "byte":
																	paramType = 9;
																	break;
																case "file":
																	paramType = 1;
																	break;
																case "date":
																	paramType = 6;
																	break;
																case "dateTime":
																	paramType = 7;
																	break;
																case "boolean":
																	paramType = 8;
																	break;
																case "array":
																	paramType = 12;
																	break;
																case "json":
																	paramType = 2;
																	break;
																case "object":
																	paramType = 13;
																	break;
																case "number":
																	paramType = 14;
																	break;
																default:
																	paramType = 0;
															}
														}
														requestParam1.setParamType(paramType);
														requestParam1.setParamLimit(object1.getString("dataType"));
														requestParam1.setParamNotNull(0);
														if (apiMapper.addRequestParam(requestParam1) < 1)
															throw new RuntimeException("addRequestParam error");
														map1.put("paramName", object1.getString("name"));
														map1.put("paramKey",
																requestParam + ">>" + object1.getString("identifier"));
														map1.put("paramValue", object1.getString("remark"));
														map1.put("paramType", paramType);
														map1.put("paramLimit", "");
														map1.put("paramNotNull", 0);
														map1.put("paramValueList",
																new ArrayList<Map<String, Object>>());
														apiRequestParam.add(map1);
														JSONArray requestParamList11 = object1
																.getJSONArray("parameterList");
														if (requestParamList11 != null && !requestParamList11.isEmpty())
														{
															for (Iterator<Object> iterator11111 = requestParamList11
																	.iterator(); iterator11111.hasNext();)
															{
																JSONObject object11 = (JSONObject) iterator11111.next();
																Map<String, Object> map11 = new HashMap<>();
																ApiRequestParam requestParam11 = new ApiRequestParam();
																requestParam11.setApiID(api.getApiID());
																requestParam11.setParamName(object11.getString("name"));
																requestParam11.setParamKey(requestParam1 + ">>"
																		+ object11.getString("identifier"));
																requestParam11
																		.setParamValue(object11.getString("remark"));
																if (object11.get("type") != null)
																{
																	switch (object11.getString("type"))
																	{
																		case "integer":
																			paramType = 3;
																			break;
																		case "string":
																			paramType = 0;
																			break;
																		case "long":
																			paramType = 11;
																			break;
																		case "float":
																			paramType = 4;
																			break;
																		case "double":
																			paramType = 5;
																			break;
																		case "byte":
																			paramType = 9;
																			break;
																		case "file":
																			paramType = 1;
																			break;
																		case "date":
																			paramType = 6;
																			break;
																		case "dateTime":
																			paramType = 7;
																			break;
																		case "boolean":
																			paramType = 8;
																			break;
																		case "array":
																			paramType = 12;
																			break;
																		case "json":
																			paramType = 2;
																			break;
																		case "object":
																			paramType = 13;
																			break;
																		case "number":
																			paramType = 14;
																			break;
																		default:
																			paramType = 0;
																	}
																}
																requestParam11.setParamType(paramType);
																requestParam11
																		.setParamLimit(object11.getString("dataType"));
																requestParam11.setParamNotNull(0);
																if (apiMapper.addRequestParam(requestParam11) < 1)
																	throw new RuntimeException("addRequestParam error");
																map11.put("paramName", object11.getString("name"));
																map11.put("paramKey", requestParam1 + ">>"
																		+ object11.getString("identifier"));
																map11.put("paramValue", object11.getString("remark"));
																map11.put("paramType", paramType);
																map11.put("paramLimit", "");
																map11.put("paramNotNull", 0);
																map11.put("paramValueList",
																		new ArrayList<Map<String, Object>>());
																apiRequestParam.add(map11);
																JSONArray requestParamList111 = object11
																		.getJSONArray("parameterList");
																if (requestParamList111 != null
																		&& !requestParamList111.isEmpty())
																{
																	for (Iterator<Object> iterator111111 = requestParamList111
																			.iterator(); iterator111111.hasNext();)
																	{
																		JSONObject object111 = (JSONObject) iterator111111
																				.next();
																		Map<String, Object> map111 = new HashMap<>();
																		ApiRequestParam requestParam111 = new ApiRequestParam();
																		requestParam111.setApiID(api.getApiID());
																		requestParam111.setParamName(
																				object111.getString("name"));
																		requestParam111.setParamKey(requestParam11
																				+ ">>"
																				+ object111.getString("identifier"));
																		requestParam111.setParamValue(
																				object111.getString("remark"));
																		if (object111.get("type") != null)
																		{
																			switch (object111.getString("type"))
																			{
																				case "integer":
																					paramType = 3;
																					break;
																				case "string":
																					paramType = 0;
																					break;
																				case "long":
																					paramType = 11;
																					break;
																				case "float":
																					paramType = 4;
																					break;
																				case "double":
																					paramType = 5;
																					break;
																				case "byte":
																					paramType = 9;
																					break;
																				case "file":
																					paramType = 1;
																					break;
																				case "date":
																					paramType = 6;
																					break;
																				case "dateTime":
																					paramType = 7;
																					break;
																				case "boolean":
																					paramType = 8;
																					break;
																				case "array":
																					paramType = 12;
																					break;
																				case "json":
																					paramType = 2;
																					break;
																				case "object":
																					paramType = 13;
																					break;
																				case "number":
																					paramType = 14;
																					break;
																				default:
																					paramType = 0;
																			}
																		}
																		requestParam111.setParamType(paramType);
																		requestParam111.setParamLimit(
																				object111.getString("dataType"));
																		requestParam111.setParamNotNull(0);
																		if (apiMapper
																				.addRequestParam(requestParam111) < 1)
																			throw new RuntimeException(
																					"addRequestParam error");
																		map111.put("paramName",
																				object111.getString("name"));
																		map111.put("paramKey", requestParam11 + ">>"
																				+ object111.getString("identifier"));
																		map111.put("paramValue",
																				object111.getString("remark"));
																		map111.put("paramType", paramType);
																		map111.put("paramLimit", "");
																		map111.put("paramNotNull", 0);
																		map111.put("paramValueList",
																				new ArrayList<Map<String, Object>>());
																		apiRequestParam.add(map111);
																		JSONArray requestParamList1111 = object111
																				.getJSONArray("parameterList");
																		if (requestParamList1111 != null
																				&& !requestParamList1111.isEmpty())
																		{
																			for (Iterator<Object> iterator1111111 = requestParamList1111
																					.iterator(); iterator1111111
																							.hasNext();)
																			{
																				JSONObject object1111 = (JSONObject) iterator1111111
																						.next();
																				Map<String, Object> map1111 = new HashMap<>();
																				ApiRequestParam requestParam1111 = new ApiRequestParam();
																				requestParam1111
																						.setApiID(api.getApiID());
																				requestParam1111.setParamName(
																						object1111.getString("name"));
																				requestParam1111.setParamKey(
																						requestParam111 + ">>"
																								+ object1111.getString(
																										"identifier"));
																				requestParam1111.setParamValue(
																						object1111.getString("remark"));
																				if (object1111.get("type") != null)
																				{
																					switch (object1111
																							.getString("type"))
																					{
																						case "integer":
																							paramType = 3;
																							break;
																						case "string":
																							paramType = 0;
																							break;
																						case "long":
																							paramType = 11;
																							break;
																						case "float":
																							paramType = 4;
																							break;
																						case "double":
																							paramType = 5;
																							break;
																						case "byte":
																							paramType = 9;
																							break;
																						case "file":
																							paramType = 1;
																							break;
																						case "date":
																							paramType = 6;
																							break;
																						case "dateTime":
																							paramType = 7;
																							break;
																						case "boolean":
																							paramType = 8;
																							break;
																						case "array":
																							paramType = 12;
																							break;
																						case "json":
																							paramType = 2;
																							break;
																						case "object":
																							paramType = 13;
																							break;
																						case "number":
																							paramType = 14;
																							break;
																						default:
																							paramType = 0;
																					}
																				}
																				requestParam1111
																						.setParamType(paramType);
																				requestParam1111
																						.setParamLimit(object1111
																								.getString("dataType"));
																				requestParam1111.setParamNotNull(0);
																				if (apiMapper.addRequestParam(
																						requestParam1111) < 1)
																					throw new RuntimeException(
																							"addRequestParam error");
																				map1111.put("paramName",
																						object1111.getString("name"));
																				map1111.put("paramKey",
																						requestParam111 + ">>"
																								+ object1111.getString(
																										"identifier"));
																				map1111.put("paramValue",
																						object1111.getString("remark"));
																				map1111.put("paramType", paramType);
																				map1111.put("paramLimit", "");
																				map1111.put("paramNotNull", 0);
																				map1111.put("paramValueList",
																						new ArrayList<Map<String, Object>>());
																				apiRequestParam.add(map1111);
																				JSONArray requestParamList11111 = object1111
																						.getJSONArray("parameterList");
																				if (requestParamList11111 != null
																						&& !requestParamList11111
																								.isEmpty())
																				{
																					for (Iterator<Object> iterator11111111 = requestParamList11111
																							.iterator(); iterator11111111
																									.hasNext();)
																					{
																						JSONObject object11111 = (JSONObject) iterator11111111
																								.next();
																						Map<String, Object> map11111 = new HashMap<>();
																						ApiRequestParam requestParam11111 = new ApiRequestParam();
																						requestParam11111.setApiID(
																								api.getApiID());
																						requestParam11111.setParamName(
																								object11111.getString(
																										"name"));
																						requestParam11111.setParamKey(
																								requestParam1111 + ">>"
																										+ object11111
																												.getString(
																														"identifier"));
																						requestParam11111.setParamValue(
																								object11111.getString(
																										"remark"));
																						if (object11111
																								.get("type") != null)
																						{
																							switch (object11111
																									.getString("type"))
																							{
																								case "integer":
																									paramType = 3;
																									break;
																								case "string":
																									paramType = 0;
																									break;
																								case "long":
																									paramType = 11;
																									break;
																								case "float":
																									paramType = 4;
																									break;
																								case "double":
																									paramType = 5;
																									break;
																								case "byte":
																									paramType = 9;
																									break;
																								case "file":
																									paramType = 1;
																									break;
																								case "date":
																									paramType = 6;
																									break;
																								case "dateTime":
																									paramType = 7;
																									break;
																								case "boolean":
																									paramType = 8;
																									break;
																								case "array":
																									paramType = 12;
																									break;
																								case "json":
																									paramType = 2;
																									break;
																								case "object":
																									paramType = 13;
																									break;
																								case "number":
																									paramType = 14;
																									break;
																								default:
																									paramType = 0;
																							}
																						}
																						requestParam11111.setParamType(
																								paramType);
																						requestParam11111.setParamLimit(
																								object11111.getString(
																										"dataType"));
																						requestParam11111
																								.setParamNotNull(0);
																						if (apiMapper.addRequestParam(
																								requestParam11111) < 1)
																							throw new RuntimeException(
																									"addRequestParam error");
																						map11111.put("paramName",
																								object11111.getString(
																										"name"));
																						map11111.put("paramKey",
																								requestParam1111 + ">>"
																										+ object11111
																												.getString(
																														"identifier"));
																						map11111.put("paramValue",
																								object11111.getString(
																										"remark"));
																						map11111.put("paramType",
																								paramType);
																						map11111.put("paramLimit", "");
																						map11111.put("paramNotNull", 0);
																						map11111.put("paramValueList",
																								new ArrayList<Map<String, Object>>());
																						apiRequestParam.add(map11111);
																						JSONArray requestParamList111111 = object11111
																								.getJSONArray(
																										"parameterList");
																						if (requestParamList111111 != null
																								&& !requestParamList111111
																										.isEmpty())
																						{
																							for (Iterator<Object> iterator111111111 = requestParamList111111
																									.iterator(); iterator111111111
																											.hasNext();)
																							{
																								JSONObject object111111 = (JSONObject) iterator111111111
																										.next();
																								Map<String, Object> map111111 = new HashMap<>();
																								ApiRequestParam requestParam111111 = new ApiRequestParam();
																								requestParam111111
																										.setApiID(api
																												.getApiID());
																								requestParam111111
																										.setParamName(
																												object111111
																														.getString(
																																"name"));
																								requestParam111111
																										.setParamKey(
																												requestParam11111
																														+ ">>"
																														+ object111111
																																.getString(
																																		"identifier"));
																								requestParam111111
																										.setParamValue(
																												object111111
																														.getString(
																																"remark"));
																								if (object111111.get(
																										"type") != null)
																								{
																									switch (object111111
																											.getString(
																													"type"))
																									{
																										case "integer":
																											paramType = 3;
																											break;
																										case "string":
																											paramType = 0;
																											break;
																										case "long":
																											paramType = 11;
																											break;
																										case "float":
																											paramType = 4;
																											break;
																										case "double":
																											paramType = 5;
																											break;
																										case "byte":
																											paramType = 9;
																											break;
																										case "file":
																											paramType = 1;
																											break;
																										case "date":
																											paramType = 6;
																											break;
																										case "dateTime":
																											paramType = 7;
																											break;
																										case "boolean":
																											paramType = 8;
																											break;
																										case "array":
																											paramType = 12;
																											break;
																										case "json":
																											paramType = 2;
																											break;
																										case "object":
																											paramType = 13;
																											break;
																										case "number":
																											paramType = 14;
																											break;
																										default:
																											paramType = 0;
																									}
																								}
																								requestParam111111
																										.setParamType(
																												paramType);
																								requestParam111111
																										.setParamLimit(
																												object111111
																														.getString(
																																"dataType"));
																								requestParam111111
																										.setParamNotNull(
																												0);
																								if (apiMapper
																										.addRequestParam(
																												requestParam111111) < 1)
																									throw new RuntimeException(
																											"addRequestParam error");
																								map111111.put(
																										"paramName",
																										object111111
																												.getString(
																														"name"));
																								map111111.put(
																										"paramKey",
																										requestParam11111
																												+ ">>"
																												+ object111111
																														.getString(
																																"identifier"));
																								map111111.put(
																										"paramValue",
																										object111111
																												.getString(
																														"remark"));
																								map111111.put(
																										"paramType",
																										paramType);
																								map111111.put(
																										"paramLimit",
																										"");
																								map111111.put(
																										"paramNotNull",
																										0);
																								map111111.put(
																										"paramValueList",
																										new ArrayList<Map<String, Object>>());
																								apiRequestParam
																										.add(map111111);
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										JSONArray responseParameterList = actionData
												.getJSONArray("responseParameterList");
										if (responseParameterList != null && !responseParameterList.isEmpty())
										{
											for (Iterator<Object> iterator111 = responseParameterList
													.iterator(); iterator111.hasNext();)
											{
												JSONObject object = (JSONObject) iterator111.next();
												Map<String, Object> map = new HashMap<>();
												ApiResultParam resultParam = new ApiResultParam();
												resultParam.setApiID(api.getApiID());
												resultParam.setParamName(object.getString("name"));
												resultParam.setParamKey(object.getString("identifier"));
												resultParam.setParamValue(object.getString("remark"));
												Integer paramType = 12;
												if (object.get("type") != null)
												{
													switch (object.getString("type"))
													{
														case "integer":
															paramType = 3;
															break;
														case "string":
															paramType = 0;
															break;
														case "long":
															paramType = 11;
															break;
														case "float":
															paramType = 4;
															break;
														case "double":
															paramType = 5;
															break;
														case "byte":
															paramType = 9;
															break;
														case "file":
															paramType = 1;
															break;
														case "date":
															paramType = 6;
															break;
														case "dateTime":
															paramType = 7;
															break;
														case "boolean":
															paramType = 8;
															break;
														case "array":
															paramType = 12;
															break;
														case "json":
															paramType = 2;
															break;
														case "object":
															paramType = 13;
															break;
														case "number":
															paramType = 14;
															break;
														default:
															paramType = 0;
													}
												}
												resultParam.setParamType(paramType);
												resultParam.setParamNotNull(0);
												if (apiMapper.addResultParam(resultParam) < 1)
													throw new RuntimeException("addRequestParam error");
												map.put("paramName", object.getString("name"));
												map.put("paramKey", object.getString("identifier"));
												map.put("paramValue", object.getString("remark"));
												map.put("paramType", paramType);
												map.put("paramLimit", "");
												map.put("paramNotNull", 0);
												map.put("paramValueList", new ArrayList<Map<String, Object>>());
												apiResultParam.add(map);
												JSONArray requestParamList1 = object.getJSONArray("parameterList");
												if (requestParamList1 != null && !requestParamList1.isEmpty())
												{
													for (Iterator<Object> iterator1111 = requestParamList1
															.iterator(); iterator1111.hasNext();)
													{
														JSONObject object1 = (JSONObject) iterator1111.next();
														Map<String, Object> map1 = new HashMap<>();
														ApiResultParam requestParam1 = new ApiResultParam();
														requestParam1.setApiID(api.getApiID());
														requestParam1.setParamName(object1.getString("name"));
														requestParam1.setParamKey(
																resultParam + ">>" + object1.getString("identifier"));
														requestParam1.setParamValue(object1.getString("remark"));
														if (object1.get("type") != null)
														{
															switch (object1.getString("type"))
															{
																case "integer":
																	paramType = 3;
																	break;
																case "string":
																	paramType = 0;
																	break;
																case "long":
																	paramType = 11;
																	break;
																case "float":
																	paramType = 4;
																	break;
																case "double":
																	paramType = 5;
																	break;
																case "byte":
																	paramType = 9;
																	break;
																case "file":
																	paramType = 1;
																	break;
																case "date":
																	paramType = 6;
																	break;
																case "dateTime":
																	paramType = 7;
																	break;
																case "boolean":
																	paramType = 8;
																	break;
																case "array":
																	paramType = 12;
																	break;
																case "json":
																	paramType = 2;
																	break;
																case "object":
																	paramType = 13;
																	break;
																case "number":
																	paramType = 14;
																	break;
																default:
																	paramType = 0;
															}
														}
														requestParam1.setParamType(paramType);
														requestParam1.setParamNotNull(0);
														if (apiMapper.addResultParam(requestParam1) < 1)
															throw new RuntimeException("addRequestParam error");
														map1.put("paramName", object1.getString("name"));
														map1.put("paramKey",
																resultParam + ">>" + object1.getString("identifier"));
														map1.put("paramValue", object1.getString("remark"));
														map1.put("paramType", paramType);
														map1.put("paramLimit", "");
														map1.put("paramNotNull", 0);
														map1.put("paramValueList",
																new ArrayList<Map<String, Object>>());
														apiResultParam.add(map1);
														JSONArray requestParamList11 = object1
																.getJSONArray("parameterList");
														if (requestParamList11 != null && !requestParamList11.isEmpty())
														{
															for (Iterator<Object> iterator11111 = requestParamList11
																	.iterator(); iterator11111.hasNext();)
															{
																JSONObject object11 = (JSONObject) iterator11111.next();
																Map<String, Object> map11 = new HashMap<>();
																ApiResultParam requestParam11 = new ApiResultParam();
																requestParam11.setApiID(api.getApiID());
																requestParam11.setParamName(object11.getString("name"));
																requestParam11.setParamKey(requestParam1 + ">>"
																		+ object11.getString("identifier"));
																requestParam11
																		.setParamValue(object11.getString("remark"));
																if (object11.get("type") != null)
																{
																	switch (object11.getString("type"))
																	{
																		case "integer":
																			paramType = 3;
																			break;
																		case "string":
																			paramType = 0;
																			break;
																		case "long":
																			paramType = 11;
																			break;
																		case "float":
																			paramType = 4;
																			break;
																		case "double":
																			paramType = 5;
																			break;
																		case "byte":
																			paramType = 9;
																			break;
																		case "file":
																			paramType = 1;
																			break;
																		case "date":
																			paramType = 6;
																			break;
																		case "dateTime":
																			paramType = 7;
																			break;
																		case "boolean":
																			paramType = 8;
																			break;
																		case "array":
																			paramType = 12;
																			break;
																		case "json":
																			paramType = 2;
																			break;
																		case "object":
																			paramType = 13;
																			break;
																		case "number":
																			paramType = 14;
																			break;
																		default:
																			paramType = 0;
																	}
																}
																requestParam11.setParamType(paramType);
																requestParam11.setParamNotNull(0);
																if (apiMapper.addResultParam(requestParam11) < 1)
																	throw new RuntimeException("addRequestParam error");
																map11.put("paramName", object11.getString("name"));
																map11.put("paramKey", requestParam1 + ">>"
																		+ object11.getString("identifier"));
																map11.put("paramValue", object11.getString("remark"));
																map11.put("paramType", paramType);
																map11.put("paramLimit", "");
																map11.put("paramNotNull", 0);
																map11.put("paramValueList",
																		new ArrayList<Map<String, Object>>());
																apiResultParam.add(map11);
																JSONArray requestParamList111 = object11
																		.getJSONArray("parameterList");
																if (requestParamList111 != null
																		&& !requestParamList111.isEmpty())
																{
																	for (Iterator<Object> iterator111111 = requestParamList111
																			.iterator(); iterator111111.hasNext();)
																	{
																		JSONObject object111 = (JSONObject) iterator111111
																				.next();
																		Map<String, Object> map111 = new HashMap<>();
																		ApiResultParam requestParam111 = new ApiResultParam();
																		requestParam111.setApiID(api.getApiID());
																		requestParam111.setParamName(
																				object111.getString("name"));
																		requestParam111.setParamKey(requestParam11
																				+ ">>"
																				+ object111.getString("identifier"));
																		requestParam111.setParamValue(
																				object111.getString("remark"));
																		if (object111.get("type") != null)
																		{
																			switch (object111.getString("type"))
																			{
																				case "integer":
																					paramType = 3;
																					break;
																				case "string":
																					paramType = 0;
																					break;
																				case "long":
																					paramType = 11;
																					break;
																				case "float":
																					paramType = 4;
																					break;
																				case "double":
																					paramType = 5;
																					break;
																				case "byte":
																					paramType = 9;
																					break;
																				case "file":
																					paramType = 1;
																					break;
																				case "date":
																					paramType = 6;
																					break;
																				case "dateTime":
																					paramType = 7;
																					break;
																				case "boolean":
																					paramType = 8;
																					break;
																				case "array":
																					paramType = 12;
																					break;
																				case "json":
																					paramType = 2;
																					break;
																				case "object":
																					paramType = 13;
																					break;
																				case "number":
																					paramType = 14;
																					break;
																				default:
																					paramType = 0;
																			}
																		}
																		requestParam111.setParamType(paramType);
																		requestParam111.setParamNotNull(0);
																		if (apiMapper
																				.addResultParam(requestParam111) < 1)
																			throw new RuntimeException(
																					"addRequestParam error");
																		map111.put("paramName",
																				object111.getString("name"));
																		map111.put("paramKey", requestParam11 + ">>"
																				+ object111.getString("identifier"));
																		map111.put("paramValue",
																				object111.getString("remark"));
																		map111.put("paramType", paramType);
																		map111.put("paramLimit", "");
																		map111.put("paramNotNull", 0);
																		map111.put("paramValueList",
																				new ArrayList<Map<String, Object>>());
																		apiResultParam.add(map111);
																		JSONArray requestParamList1111 = object111
																				.getJSONArray("parameterList");
																		if (requestParamList1111 != null
																				&& !requestParamList1111.isEmpty())
																		{
																			for (Iterator<Object> iterator1111111 = requestParamList1111
																					.iterator(); iterator1111111
																							.hasNext();)
																			{
																				JSONObject object1111 = (JSONObject) iterator1111111
																						.next();
																				Map<String, Object> map1111 = new HashMap<>();
																				ApiResultParam requestParam1111 = new ApiResultParam();
																				requestParam1111
																						.setApiID(api.getApiID());
																				requestParam1111.setParamName(
																						object1111.getString("name"));
																				requestParam1111.setParamKey(
																						requestParam111 + ">>"
																								+ object1111.getString(
																										"identifier"));
																				requestParam1111.setParamValue(
																						object1111.getString("remark"));
																				if (object1111.get("type") != null)
																				{
																					switch (object1111
																							.getString("type"))
																					{
																						case "integer":
																							paramType = 3;
																							break;
																						case "string":
																							paramType = 0;
																							break;
																						case "long":
																							paramType = 11;
																							break;
																						case "float":
																							paramType = 4;
																							break;
																						case "double":
																							paramType = 5;
																							break;
																						case "byte":
																							paramType = 9;
																							break;
																						case "file":
																							paramType = 1;
																							break;
																						case "date":
																							paramType = 6;
																							break;
																						case "dateTime":
																							paramType = 7;
																							break;
																						case "boolean":
																							paramType = 8;
																							break;
																						case "array":
																							paramType = 12;
																							break;
																						case "json":
																							paramType = 2;
																							break;
																						case "object":
																							paramType = 13;
																							break;
																						case "number":
																							paramType = 14;
																							break;
																						default:
																							paramType = 0;
																					}
																				}
																				requestParam1111
																						.setParamType(paramType);
																				requestParam1111.setParamNotNull(0);
																				if (apiMapper.addResultParam(
																						requestParam1111) < 1)
																					throw new RuntimeException(
																							"addRequestParam error");
																				map1111.put("paramName",
																						object1111.getString("name"));
																				map1111.put("paramKey",
																						requestParam111 + ">>"
																								+ object1111.getString(
																										"identifier"));
																				map1111.put("paramValue",
																						object1111.getString("remark"));
																				map1111.put("paramType", paramType);
																				map1111.put("paramLimit", "");
																				map1111.put("paramNotNull", 0);
																				map1111.put("paramValueList",
																						new ArrayList<Map<String, Object>>());
																				apiResultParam.add(map1111);
																				JSONArray requestParamList11111 = object1111
																						.getJSONArray("parameterList");
																				if (requestParamList11111 != null
																						&& !requestParamList11111
																								.isEmpty())
																				{
																					for (Iterator<Object> iterator11111111 = requestParamList11111
																							.iterator(); iterator11111111
																									.hasNext();)
																					{
																						JSONObject object11111 = (JSONObject) iterator11111111
																								.next();
																						Map<String, Object> map11111 = new HashMap<>();
																						ApiResultParam requestParam11111 = new ApiResultParam();
																						requestParam11111.setApiID(
																								api.getApiID());
																						requestParam11111.setParamName(
																								object11111.getString(
																										"name"));
																						requestParam11111.setParamKey(
																								requestParam1111 + ">>"
																										+ object11111
																												.getString(
																														"identifier"));
																						requestParam11111.setParamValue(
																								object11111.getString(
																										"remark"));
																						if (object1111
																								.get("type") != null)
																						{
																							switch (object11111
																									.getString("type"))
																							{
																								case "integer":
																									paramType = 3;
																									break;
																								case "string":
																									paramType = 0;
																									break;
																								case "long":
																									paramType = 11;
																									break;
																								case "float":
																									paramType = 4;
																									break;
																								case "double":
																									paramType = 5;
																									break;
																								case "byte":
																									paramType = 9;
																									break;
																								case "file":
																									paramType = 1;
																									break;
																								case "date":
																									paramType = 6;
																									break;
																								case "dateTime":
																									paramType = 7;
																									break;
																								case "boolean":
																									paramType = 8;
																									break;
																								case "array":
																									paramType = 12;
																									break;
																								case "json":
																									paramType = 2;
																									break;
																								case "object":
																									paramType = 13;
																									break;
																								case "number":
																									paramType = 14;
																									break;
																								default:
																									paramType = 0;
																							}
																						}
																						requestParam11111.setParamType(
																								paramType);
																						requestParam11111
																								.setParamNotNull(0);
																						if (apiMapper.addResultParam(
																								requestParam11111) < 1)
																							throw new RuntimeException(
																									"addRequestParam error");
																						map11111.put("paramName",
																								object11111.getString(
																										"name"));
																						map11111.put("paramKey",
																								requestParam1111 + ">>"
																										+ object11111
																												.getString(
																														"identifier"));
																						map11111.put("paramValue",
																								object11111.getString(
																										"remark"));
																						map11111.put("paramType",
																								paramType);
																						map11111.put("paramLimit", "");
																						map11111.put("paramNotNull", 0);
																						map11111.put("paramValueList",
																								new ArrayList<Map<String, Object>>());
																						apiResultParam.add(map11111);
																						JSONArray requestParamList111111 = object11111
																								.getJSONArray(
																										"parameterList");
																						if (requestParamList111111 != null
																								&& !requestParamList111111
																										.isEmpty())
																						{
																							for (Iterator<Object> iterator111111111 = requestParamList111111
																									.iterator(); iterator111111111
																											.hasNext();)
																							{
																								JSONObject object111111 = (JSONObject) iterator111111111
																										.next();
																								Map<String, Object> map111111 = new HashMap<>();
																								ApiResultParam requestParam111111 = new ApiResultParam();
																								requestParam111111
																										.setApiID(api
																												.getApiID());
																								requestParam111111
																										.setParamName(
																												object111111
																														.getString(
																																"name"));
																								requestParam111111
																										.setParamKey(
																												requestParam11111
																														+ ">>"
																														+ object111111
																																.getString(
																																		"identifier"));
																								requestParam111111
																										.setParamValue(
																												object111111
																														.getString(
																																"remark"));
																								if (object111111.get(
																										"type") != null)
																								{
																									switch (object111111
																											.getString(
																													"type"))
																									{
																										case "integer":
																											paramType = 3;
																											break;
																										case "string":
																											paramType = 0;
																											break;
																										case "long":
																											paramType = 11;
																											break;
																										case "float":
																											paramType = 4;
																											break;
																										case "double":
																											paramType = 5;
																											break;
																										case "byte":
																											paramType = 9;
																											break;
																										case "file":
																											paramType = 1;
																											break;
																										case "date":
																											paramType = 6;
																											break;
																										case "dateTime":
																											paramType = 7;
																											break;
																										case "boolean":
																											paramType = 8;
																											break;
																										case "array":
																											paramType = 12;
																											break;
																										case "json":
																											paramType = 2;
																											break;
																										case "object":
																											paramType = 13;
																											break;
																										case "number":
																											paramType = 14;
																											break;
																										default:
																											paramType = 0;
																									}
																								}
																								requestParam111111
																										.setParamType(
																												paramType);
																								requestParam111111
																										.setParamNotNull(
																												0);
																								if (apiMapper
																										.addResultParam(
																												requestParam111111) < 1)
																									throw new RuntimeException(
																											"addRequestParam error");
																								map111111.put(
																										"paramName",
																										object111111
																												.getString(
																														"name"));
																								map111111.put(
																										"paramKey",
																										requestParam11111
																												+ ">>"
																												+ object111111
																														.getString(
																																"identifier"));
																								map111111.put(
																										"paramValue",
																										object111111
																												.getString(
																														"remark"));
																								map111111.put(
																										"paramType",
																										paramType);
																								map111111.put(
																										"paramLimit",
																										"");
																								map111111.put(
																										"paramNotNull",
																										0);
																								map111111.put(
																										"paramValueList",
																										new ArrayList<Map<String, Object>>());
																								apiResultParam
																										.add(map111111);
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										Map<String, Object> cache = new HashMap<String, Object>();
										Map<String, Object> mockInfo = new HashMap<String, Object>();
										cache.put("baseInfo", api);
										cache.put("headerInfo", apiHeader);
										mockInfo.put("mockResult", api.getMockResult());
										mockInfo.put("mockRule", JSONArray.parseArray(api.getMockRule()));
										mockInfo.put("mockConfig", api.getMockConfig());
										cache.put("mockInfo", mockInfo);
										cache.put("requestInfo", apiRequestParam);
										cache.put("resultInfo", new ArrayList<Map<String, Object>>());
										ApiCache apiCache = new ApiCache();
										apiCache.setApiID(api.getApiID());
										apiCache.setApiJson(JSON.toJSONString(cache));
										apiCache.setGroupID(api.getGroupID());
										apiCache.setProjectID(api.getProjectID());
										apiCache.setStarred(api.getStarred());
										apiCache.setUpdateUserID(api.getUpdateUserID());
										if (apiCacheMapper.addApiCache(apiCache) < 1)
											throw new RuntimeException("addApiCache error");
									}
								}
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

}
