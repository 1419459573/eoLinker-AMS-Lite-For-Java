package com.eolinker.service;

import java.io.InputStream;
import java.util.List;

import com.eolinker.dto.CodeListDTO;
import com.eolinker.pojo.StatusCode;
/**
 * 状态码
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
public interface StatusCodeService
{

	/**
	 * 获取项目用户类型
	 * 
	 * @param codeID
	 * @return
	 */
	public int getUserType(int codeID) throws RuntimeException;

	/**
	 * 添加状态码
	 * 
	 * @param statusCode
	 * @return
	 * @throws RuntimeException
	 */
	public int addCode(StatusCode statusCode) throws RuntimeException;

	/**
	 * 删除状态码
	 * 
	 * @param codeID
	 * @return
	 * @throws RuntimeException
	 */
	public int deleteCode(int codeID) throws RuntimeException;

	/**
	 * 批量删除状态码
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	public int deleteBatchCode(List<Integer> codeIDs) throws RuntimeException;

	/**
	 * 获取某组状态码列表
	 * 
	 * @param groupID
	 * @return
	 * @throws RuntimeException
	 */
	public List<CodeListDTO> getCodeList(int groupID) throws RuntimeException;

	/**
	 * 获取所有状态码列表
	 * 
	 * @param projectID
	 * @return
	 * @throws RuntimeException
	 */
	public List<CodeListDTO> getAllCodeList(int projectID) throws RuntimeException;

	/**
	 * 修改状态码
	 * 
	 * @param statusCode
	 * @return
	 * @throws RuntimeException
	 */
	public int editCode(StatusCode statusCode) throws RuntimeException;

	/**
	 * 搜索状态码
	 * 
	 * @param projectID
	 * @param tips
	 * @return
	 * @throws RuntimeException
	 */
	public List<CodeListDTO> searchStatusCode(int projectID, String tips) throws RuntimeException;

	/**
	 * 获取状态码数量
	 * 
	 * @param projectID
	 * @return
	 */
	public int getStatusCodeNum(int projectID) throws RuntimeException;

	// 导入excel表
	public boolean addStatusCodeByExcel(Integer groupID, Integer userID, InputStream inputStream);

}
