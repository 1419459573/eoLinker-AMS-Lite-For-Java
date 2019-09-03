package com.eolinker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eolinker.dto.CodeListDTO;
import com.eolinker.pojo.StatusCode;
/**
 * 状态码[数据库操作]
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
public interface StatusCodeMapper
{

	/**
	 * 添加状态码
	 * 
	 * @param statusCode
	 * @return
	 */
	public int addCode(StatusCode statusCode);

	/**
	 * 删除状态码
	 * 
	 * @param codeID
	 * @return
	 */
	public int deleteCode(@Param("codeID") int codeID);

	/**
	 * 获取状态码列表
	 * 
	 * @param groupIDS
	 * @return
	 */
	public List<CodeListDTO> getCodeList(@Param("groupIDS") List<Integer> groupIDS);

	/**
	 * 获取所有状态码列表
	 * 
	 * @param projectID
	 * @return
	 */
	public List<CodeListDTO> getAllCodeList(@Param("projectID") int projectID);

	/**
	 * 修改状态码
	 * 
	 * @param statusCode
	 * @return
	 * @throws RuntimeException
	 */
	public int editCode(StatusCode statusCode) throws RuntimeException;

	/**
	 * 检查状态码与用户的联系
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	public Integer checkStatusCodePermission(@Param("codeID") int codeID, @Param("userID") int userID);

	/**
	 * 搜索状态码
	 * 
	 * @param projectID
	 * @param tips
	 * @return
	 */
	public List<CodeListDTO> searchStatusCode(@Param("projectID") int projectID, @Param("tips") String tips);

	/**
	 * 获取项目状态码数量
	 * 
	 * @param projectID
	 * @return
	 */
	public Integer getStatusCodeCount(Integer projectID);

	/**
	 * 批量删除状态码
	 * 
	 * @param codeIDs
	 * @return
	 */
	public Integer deleteCodes(@Param("codeIDs") List<Integer> codeIDs);

	/**
	 * 获取状态码名称
	 * 
	 * @param codeIDs
	 * @return
	 */
	public String getStatusCodes(@Param("codeIDs") List<Integer> codeIDs);

}
