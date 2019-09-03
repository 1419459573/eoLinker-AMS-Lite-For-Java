package com.eolinker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.StatusCode;
import com.eolinker.pojo.StatusCodeGroup;
/**
 * 状态码分组[数据库操作]
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
public interface StatusCodeGroupMapper
{

	/**
	 * 添加状态码分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	public int addGroup(StatusCodeGroup statusCodeGroup);

	/**
	 * 添加子分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	public int addChildGroup(StatusCodeGroup statusCodeGroup);

	/**
	 * 判断用户和分组是否匹配
	 * 
	 * @param groupID
	 * @param userID
	 * @return
	 */
	public Integer checkStatusCodeGroupPermission(@Param("groupID") int groupID, @Param("userID") int userID);

	/**
	 * 删除分组
	 * 
	 * @param groupID
	 * @return
	 */
	public int deleteGroup(@Param("groupID") int groupID);

	/**
	 * 获取父分组列表
	 * 
	 * @param projectID
	 * @return
	 */
	public List<StatusCodeGroup> getParentList(@Param("projectID") int projectID);

	/**
	 * 获取子分组列表
	 * 
	 * @param projectID
	 * @param groupID
	 * @return
	 */
	public List<StatusCodeGroup> getChildList(@Param("projectID") int projectID, @Param("groupID") int groupID);

	/**
	 * 获取分组顺序
	 * 
	 * @param projectID
	 * @return
	 */
	public String getGroupOrder(@Param("projectID") int projectID);

	/**
	 * 修改父分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	public int editParentalGroup(StatusCodeGroup statusCodeGroup);

	/**
	 * 修改非父分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	public int editChildGroup(StatusCodeGroup statusCodeGroup);

	/**
	 * 更新分组排序
	 * 
	 * @return
	 */
	public int sortGroup(@Param("projectID") int projectID, @Param("orderList") String orderList);

	/**
	 * 获取分组名称
	 * 
	 * @param groupID
	 * @return
	 */
	public String getGroupName(@Param("groupID") int groupID);

	/**
	 * 获取全部分组信息
	 * 
	 * @param projectID
	 * @param groupID
	 * @return
	 */
	public StatusCodeGroup getGroupData(@Param("projectID") int projectID, @Param("groupID") int groupID);

	/**
	 * 获取子分组信息
	 * 
	 * @param groupID
	 * @return
	 */
	public List<StatusCodeGroup> getChildGroupData(@Param("parentGroupID") int parentGroupID,
			@Param("projectID") int projectID);

	/**
	 * 获取状态码信息
	 * 
	 * @param groupID
	 * @return
	 */
	public List<StatusCode> getStatusCodeData(@Param("groupID") int groupID);

	/**
	 * 新建状态码分组
	 * 
	 * @param statusCodeGroup
	 * @return
	 */
	public int addStatusCodeGroup(StatusCodeGroup statusCodeGroup);

	/**
	 * 获取父分组ID
	 * @param parentGroupID
	 * @return
	 */
	public Integer getParentGroupID(@Param("groupID")Integer groupID);
}
