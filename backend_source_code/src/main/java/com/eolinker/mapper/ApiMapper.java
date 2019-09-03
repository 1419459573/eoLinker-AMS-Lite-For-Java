package com.eolinker.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.Api;
import com.eolinker.pojo.ApiHeader;
import com.eolinker.pojo.ApiRequestParam;
import com.eolinker.pojo.ApiRequestValue;
import com.eolinker.pojo.ApiResultParam;
import com.eolinker.pojo.ApiResultValue;

/**
 * 接口[数据库操作]
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
public interface ApiMapper
{
	//获取项目接口数量
	public Integer getApiCount(@Param("projectID") Integer projectID);

	//添加接口
	public Integer addApi(Api api);

	//删除分组下的接口
	public Integer deleteApiByGroupID(@Param("groupIDS") List<Integer> groupIDS, @Param("removeTime") Timestamp removeTime);

	//添加接口请求头部
	public Integer addApiHeader(ApiHeader header);

	//添加接口请求参数
	public Integer addRequestParam(ApiRequestParam requestParam);

	//添加接口请求参数值
	public Integer addRequestValue(ApiRequestValue apiRequestValue);

	//添加接口返回结果参数
	public Integer addResultParam(ApiResultParam resultParam);

	//添加接口返回结果参数值
	public Integer addResultValue(ApiResultValue apiResultValue);

	//更新接口
	public Integer updateApi(Api api);

	//删除接口请求头部
	public void deleteApiHeader(@Param("apiID") Integer apiID);

	//删除接口请求参数
	public void deleteRequestParam(@Param("apiID") Integer apiID);

	//删除接口返回参数
	public void deleteResultParam(@Param("apiID") Integer apiID);

	//移除接口到回收站
	public Integer removeApi(@Param("projectID") Integer projectID, @Param("apiIDs") List<Integer> apiIDs,
			@Param("updateTime") Timestamp updateTime);

	//获取接口名称
	public String getApiNameByIDs(@Param("apiIDs") List<Integer> apiIDs);

	//恢复接口
	public Integer recoverApi(@Param("projectID") Integer projectID, @Param("groupID") Integer groupID,
			@Param("apiIDs") List<Integer> apiIDs);

	//彻底删除接口
	public Integer deleteApi(@Param("projectID") Integer projectID, @Param("apiIDs") List<Integer> apiIDs);

	//批量删除接口请求头部
	public void batchDeleteApiHeader(@Param("apiIDs") List<Integer> apiIDs);

	//批量删除接口请求头部参数
	public void batchDeleteRequestParam(@Param("apiIDs") List<Integer> apiIDs);

	//批量删除接口返回参数
	public void batchDeleteResultParam(@Param("apiIDs") List<Integer> apiIDs);

	//清空回收站
	public Integer cleanRecyclingStation(@Param("projectID") Integer projectID);

	//获取回收站接口列表
	public List<Map<String, Object>> getRecyclingStationApiList(@Param("projectID") Integer projectID,
			@Param("orderBy") String orderBy);

	//获取接口
	public Map<String, Object> getApi(@Param("projectID") Integer projectID, @Param("apiID") Integer apiID);

	//获取接口信息
	public Api getApiInfo(@Param("projectID") Integer projectID, @Param("apiID") Integer apiID);

	//获取接口列表
	public List<Map<String, Object>> getApiList(@Param("projectID") Integer projectID,
			@Param("groupIDS") List<Integer> groupIDS, @Param("orderBy") String orderBy);

	//搜索接口
	public List<Map<String, Object>> searchApi(@Param("projectID") Integer projectID, @Param("tips") String tips);

	//修改接口星标状态
	public Integer updateStar(@Param("projectID") Integer projectID, @Param("apiID") Integer apiID,
			@Param("starred") Integer starred);

	//修改接口分组
	public int changeApiGroup(@Param("projectID") Integer projectID, @Param("apiIDs") List<Integer> apiIDs,
			@Param("groupID") Integer groupID);

	//获取接口数据
	public List<Map<String, Object>> getApiData(@Param("projectID") Integer projectID,
			@Param("apiIDs") List<Integer> apiIDs);

	//获取项目全部接口
	public List<Map<String, Object>> getAllApi(@Param("projectID") Integer projectID);

	//获取接口mock数据
	public Map<String, Object> getApiMockData(@Param("projectID") Integer projectID, @Param("apiID") Integer apiID);

	//修改接口mock数据
	public int editApiMockData(@Param("projectID") Integer projectID, @Param("apiID") Integer apiID,
			@Param("mockRule") String mockRule, @Param("mockResult") String mockResult,
			@Param("mockConfig") String mockConfig);

	//根据接口ID获取项目ID
	public Integer getProjectID(@Param("apiID") Integer apiID);
}
