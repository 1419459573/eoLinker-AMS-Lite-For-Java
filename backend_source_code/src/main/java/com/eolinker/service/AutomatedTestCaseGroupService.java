package com.eolinker.service;

import java.util.List;
import java.util.Map;

import com.eolinker.pojo.AutomatedTestCaseGroup;
/**
 * 自动化测试分组
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
public interface AutomatedTestCaseGroupService
{

	//添加分组
	public boolean addGroup(AutomatedTestCaseGroup automatedTestCaseGroup, Integer userID);

	//删除分组
	public boolean deleteGroup(Integer projectID, Integer groupID, Integer userID);

	//获取分组列表
	public List<Map<String, Object>> getGroupList(Integer projectID);

	//获取分组排序
	public String getGroupOrderList(Integer projectID);

	//修改分组
	public boolean editGroup(AutomatedTestCaseGroup automatedTestCaseGroup, Integer userID);

	//对分组进行排序
	public boolean sortGroup(Integer projectID, Integer userID, String orderList);

	//导出分组
	public Map<String, Object> exportGroup(Integer projectID, Integer userID, Integer groupID);

	//导入分组
	public boolean importGroup(Integer projectID, Integer userID, String data);

}
