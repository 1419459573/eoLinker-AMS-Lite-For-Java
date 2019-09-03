package com.eolinker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.Env;
/**
 * 环境[数据库操作]
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
public interface EnvMapper
{

	/**
	 * 获取环境列表
	 * 
	 * @param projectID
	 * @return
	 */
	public List<Env> getEnvList(@Param("projectID") int projectID);

	/**
	 * 添加环境
	 * 
	 * @param env
	 * @return
	 */
	public int addEnv(Env env);

	/**
	 * 删除环境
	 * 
	 * @param envID
	 * @param projectID
	 * @return
	 */
	public int deleteEnv(@Param("envID") int envID, @Param("projectID") int projectID);

	/**
	 * 修改环境
	 * 
	 * @param env
	 * @return
	 */
	public int editEnv(Env env);

	/**
	 * 获取环境信息
	 * 
	 * @param envID
	 * @return
	 */
	public List<Env> getEnvInfo(@Param("envID") int envID);

	/**
	 * 获取环境名称
	 * 
	 * @param envID
	 * @return
	 */
	public String getEnvName(@Param("envID") int envID);

	/**
	 * 检查项目环境权限
	 * 
	 * @param envID
	 * @param userID
	 * @return
	 */
	public Integer checkEnvPermission(@Param("envID") int envID, @Param("userID") int userID);

}
