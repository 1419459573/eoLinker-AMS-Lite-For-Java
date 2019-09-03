package com.eolinker.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.eolinker.pojo.Project;
import com.eolinker.pojo.Partner;
/**
 * 自动化测试用例
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
public interface ProjectService
{

	// 新增项目
	public Map<String, Object> addProject(Project project, HttpSession session);

	// 获取用户类型
	public Partner getProjectUserType(Integer userID, Integer projectID);

	// 删除项目
	public int deleteProject(Integer projectID);

	// 获取项目列表
	public List<Map<String, Object>> getProjectList(Integer userID, Integer projectType);

	// 修改项目
	public boolean editProject(Project project);

	//获取项目详情
	public Map<String, Object> getProject(Integer userID, Integer projectID);

	//获取项目日志列表
	public List<Map<String, Object>> getProjectLogList(Integer projectID, Integer page, Integer pageSize);

	//获取项目日志条数
	public int getProjectLogCount(Integer projectID, int dayOffset);

	//获取接口数量
	public int getApiNum(Integer projectID);

	//获取导出项目数据
	public Map<String, Object> exportProjectData(Integer projectID, Integer userID);

}
