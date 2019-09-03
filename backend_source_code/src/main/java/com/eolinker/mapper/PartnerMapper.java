package com.eolinker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.eolinker.pojo.Partner;
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
public interface PartnerMapper
{
	// 新建项目成员
	public Integer addPartner(Partner partner);

	// 获取成员类型
	public Partner getProjectUserType(@Param("userID") Integer userID, @Param("projectID") Integer projectID);

	// 删除全部成员
	public Integer deleteAllMember(@Param("projectID") Integer projectID);

	// 获取成员数量
	public Integer getPartnerCount(@Param("projectID") Integer projectID);
	
	//检查是否已经被邀请
	public Integer checkIsInvited(@Param("projectID") Integer projectID, @Param("userName") String userName);

	//删除成员
	public int removePartner(@Param("projectID") Integer projectID, @Param("connID") Integer connID);

	//获取成员消息
	public Map<String, Object> getPartnerInfoByConnID(@Param("projectID") Integer projectID,
			@Param("connID") Integer connID);

	//获取成员列表
	public List<Map<String, Object>> getPartnerList(@Param("projectID") Integer projectID);

	//退出项目
	public int quitPartner(@Param("projectID") Integer projectID, @Param("userID") Integer userID);

	//修改成员备注
	public int editPartnerNickName(@Param("projectID") Integer projectID, @Param("connID") Integer connID,
			@Param("nickName") String nickName);

	//修改成员权限类型
	public int editPartnerType(@Param("projectID") Integer projectID, @Param("connID") Integer connID,
			@Param("userType") Integer userType);
}
