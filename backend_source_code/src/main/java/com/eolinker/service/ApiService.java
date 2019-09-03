package com.eolinker.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eolinker.pojo.Api;
/**
 * 接口
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
public interface ApiService
{

	//添加接口
	public Integer addApi(Api api, String apiHeader, String apiRequestParam, String apiResultParam);

	//修改接口
	public boolean editApi(Api api, String apiHeader, String apiRequestParam, String apiResultParam, String updateDesc);

	//移除接口到回收站
	public boolean removeApi(Integer projectID, String apiID, Integer userID);

	//移除接口到回收站
	public boolean recoverApi(Integer projectID, Integer groupID, String apiID, Integer userID);

	//删除接口
	public boolean deleteApi(Integer projectID, String apiID, Integer userID);

	//清空回收站
	public boolean cleanRecyclingStation(Integer projectID, Integer userID);

	//获取回收站接口列表
	public List<Map<String,Object>> getRecyclingStationApiList(Integer projectID, Integer orderBy, Integer asc);

	//获取接口信息
	public Map<String, Object> getApi(Integer projectID, Integer apiID, HttpServletRequest request);

	//获取接口列表
	public List<Map<String, Object>> getApiList(Integer projectID,  Integer groupID, Integer orderBy, Integer asc);

	//搜索接口
	public List<Map<String, Object>> searchApi(Integer projectID, String tips);

	//修改接口星标状态
	public boolean updateStar(Integer projectID, Integer apiID, Integer userID, Integer starred);

	//获取接口历史列表
	public List<Map<String, Object>> getApiHistoryList(Integer projectID, Integer apiID);

	//删除接口历史
	public boolean deleteApiHistory(Integer projectID, Integer apiID, Integer userID, Integer apiHistoryID);

	//切换接口历史
	public boolean toggleApiHistory(Integer projectID, Integer apiID, Integer userID, Integer apiHistoryID);

	//修改接口分组
	public boolean changeApiGroup(Integer projectID, String apiID, Integer userID, Integer groupID);

	//导出接口
	public List<Map<String, Object>> exportApi(Integer projectID, String apiID, Integer userID);

	//导入接口
	public boolean importApi(Integer projectID, Integer groupID, Integer userID, String data);

	//获取接口mock数据
	public Map<String, Object> getApiMockData(Integer projectID, Integer apiID, HttpServletRequest request);

	//修改mock数据
	public boolean editApiMockData(Integer projectID, Integer apiID, String mockRule, String mockResult,
			String mockConfig);
	//获取项目ID
	public Integer getProjectID(Integer apiID);

}
