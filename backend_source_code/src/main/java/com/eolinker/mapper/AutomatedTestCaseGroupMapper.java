package com.eolinker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.eolinker.pojo.AutomatedTestCaseGroup;

/**
 * 自动化测试用例分组[数据库操作]
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
public interface AutomatedTestCaseGroupMapper
{

	//添加分组
	public int addGroup(AutomatedTestCaseGroup automatedTestCaseGroup);

	//删除接口
	public int deleteGroup(@Param("groupIDS") List<Integer> groupIDS, @Param("projectID") Integer projectID);

	//获取接口
	public AutomatedTestCaseGroup getGroupByID(@Param("groupID") Integer groupID);

	//获取父分组列表
	public List<Map<String, Object>> getParentGroupList(@Param("projectID") Integer projectID);

	//获取子分组列表
	public List<Map<String, Object>> getChildGroupList(@Param("projectID") Integer projectID,
			@Param("parentGroupID") Integer parentGroupID);

	//获取分组排序列表
	public String getGroupOrderList(@Param("projectID") Integer projectID);

	//修改分组
	public int editGroup(AutomatedTestCaseGroup automatedTestCaseGroup);

	//对分组进行排序
	public int sortGroup(@Param("projectID") Integer projectID, @Param("orderList") String orderList);

	//检查用户是否对分组有操作权限
	public Integer checkGroupPermission(@Param("groupID") Integer groupID, @Param("userID") Integer userID);

	public Integer getParentGroupID(@Param("groupID")Integer groupID);

}
