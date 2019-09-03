package com.eolinker.pojo;
/**
 * 接口缓存bean
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
public class ApiCache 
{
	private Integer cacheID;//缓存ID
	private Integer projectID;//项目ID
	private Integer groupID;//分组ID
	private Integer apiID;//接口ID
	private String apiJson;//接口缓存数据
	private Integer starred;//接口星标状态
	private Integer updateUserID;//接口更新成员ID
	public Integer getCacheID() {
		return cacheID;
	}
	public void setCacheID(Integer cacheID) {
		this.cacheID = cacheID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Integer getGroupID() {
		return groupID;
	}
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	public Integer getApiID() {
		return apiID;
	}
	public void setApiID(Integer apiID) {
		this.apiID = apiID;
	}
	public String getApiJson() {
		return apiJson;
	}
	public void setApiJson(String apiJson) {
		this.apiJson = apiJson;
	}
	public Integer getStarred() {
		return starred;
	}
	public void setStarred(Integer starred) {
		this.starred = starred;
	}
	public Integer getUpdateUserID() {
		return updateUserID;
	}
	public void setUpdateUserID(Integer updateUserID) {
		this.updateUserID = updateUserID;
	}
}
