package com.eolinker.service;

import java.util.List;

import com.eolinker.pojo.DatabaseTable;
/**
 * 数据库表
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
public interface DatabaseTableService
{

	//新增数据库表
	public int addTable(Integer dbID, String tableName, String tableDesc);

	//检查权限
	public Integer checkTablePermission(int tableID, int userID);

	//删除表
	public int deleteTable(int tableID);

	//获取用户类型
	public int getUserType(int tableID);

	//获取表
	public List<DatabaseTable> getTable(int dbID);

	//修改表
	public int editTable(int tableID, String tableName, String tableDesc);

}
