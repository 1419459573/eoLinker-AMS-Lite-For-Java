package com.eolinker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.AutomatedTestCase;
/**
 * 自动化测试用例[数据库操作]
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
public interface AutomatedTestCaseMapper
{

	//删除分组下用例
	public void deleteTestCaseByGroupID(@Param("groupIDS") List<Integer> groupIDS);

	//添加自动化测试用例
	public int addTestCase(AutomatedTestCase automatedTestCase);

	//修改用例
	public int editTestCase(AutomatedTestCase automatedTestCase);

	//获取用例列表
	public List<Map<String, Object>> getTestCaseList(@Param("projectID") Integer projectID,
			@Param("groupIDS") List<Integer> groupIDS);

	//获取用例信息
	public Map<String, Object> getTestCaseInfo(@Param("projectID") Integer projectID, @Param("caseID") Integer caseID);

	//获取用例名称
	public String getCaseNameByIDs(@Param("caseIDs") List<Integer> caseIDs);

	//删除用例
	public Integer deleteTestCase(@Param("projectID") Integer projectID, @Param("caseIDs") List<Integer> caseIDs);

	//搜索用例
	public List<Map<String, Object>> searchTestCase(@Param("projectID") Integer projectID, @Param("tips") String tips);

	//获取用例数据
	public List<Map<String, Object>> getTestCaseDataList(@Param("projectID") Integer projectID,
			@Param("groupID") Integer groupID);

	//根据用例ID获取项目ID
	public Integer getProjectIDByCaseID(@Param("caseID") Integer caseID);

	//获取分组用例列表
	public List<Map<String, Object>> getTestCaseListByGroupID(@Param("projectID") Integer projectID,
			@Param("groupID") Integer groupID);

}
