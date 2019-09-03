package com.eolinker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.AutomatedTestCaseSingle;
/**
 * 自动化测试用例单例[数据库操作]
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
public interface AutomatedTestCaseSingleMapper
{
	//删除用例分组下面的单例
	public int deleteTestCaseSingleByGroupID(@Param("groupIDS") List<Integer> groupIDS);

	//根据用例ID删除单例
	public void batchDeleteSingle(@Param("caseIDs") List<Integer> caseIDs);

	//获取单例列表
	public List<Map<String, Object>> getSingleCaseList(@Param("caseID") Integer caseID);

	//添加单例
	public int addSingleTestCase(AutomatedTestCaseSingle automatedTestCaseSingle);

	//修改单例
	public int editSingleTestCase(AutomatedTestCaseSingle automatedTestCaseSingle);

	//获取全部单例
	public List<Map<String, Object>> getAllSingleCase(@Param("projectID") Integer projectID);

	//获取单例信息
	public Map<String, Object> getSingleTestCaseInfo(@Param("projectID") Integer projectID,
			@Param("connID") Integer connID);

	//获取接口名称
	public String getApiNameByIDs(@Param("connIDs") List<Integer> connIDs);

	//删除单例
	public int deleteSingleTestCase(@Param("projectID") Integer projectID, @Param("connIDs") List<Integer> connIDs);

	//获取关联ID
	public Integer getConnID(@Param("apiName") String apiName, @Param("apiURI") String apiURI,
			@Param("caseID") Integer caseID);

	//更新排序
	public void updateOrderNumber(@Param("caseID")Integer caseID, @Param("orderNumber")Integer orderNumber);
	
	//获取用例最大排序序号
	public Integer getMaxOrderNumber(@Param("caseID")Integer caseID);
}
