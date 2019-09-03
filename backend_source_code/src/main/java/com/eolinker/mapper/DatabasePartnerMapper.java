package com.eolinker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eolinker.dto.PartnerListDTO;
import com.eolinker.pojo.ConnDatabase;
/**
 * 数据库成员[数据库操作]
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
public interface DatabasePartnerMapper
{

	/**
	 * 邀请协作人员
	 * 
	 * @param connDatabase
	 * @return
	 */
	public int invitePartner(ConnDatabase connDatabase);

	/**
	 * 移除协作人员
	 * 
	 * @param dbID
	 * @param connID
	 * @return
	 */
	public int removePartner(@Param("dbID") int dbID, @Param("connID") int connID);

	/**
	 * 获取协作人员列表
	 * 
	 * @param dbID
	 * @return
	 */
	public List<PartnerListDTO> getPartnerList(@Param("dbID") int dbID);

	/**
	 * 退出协作项目
	 * 
	 * @param dbID
	 * @param userID
	 * @return
	 */
	public int quitPartner(@Param("dbID") int dbID, @Param("userID") int userID);

	/**
	 * 查询是否已经加入过项目
	 * 
	 * @param dbID
	 * @param userName
	 * @return
	 */
	public Integer checkIsInvited(@Param("dbID") int dbID, @Param("userName") String userName);

	/**
	 * 获取用户ID
	 * 
	 * @param connID
	 * @return
	 */
	public Integer getUserID(@Param("connID") int connID);

	/**
	 * 修改协作成员的昵称
	 * 
	 * @param connDatabase
	 * @return
	 */
	public int editPartnerNickName(ConnDatabase connDatabase);

	/**
	 * 修改协作成员的类型
	 * 
	 * @param connDatabase
	 * @return
	 */
	public int editPartnerType(ConnDatabase connDatabase);

}
