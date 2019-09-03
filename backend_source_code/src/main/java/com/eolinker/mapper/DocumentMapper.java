package com.eolinker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.eolinker.dto.DocumentDTO;
import com.eolinker.pojo.Document;
import com.eolinker.pojo.DocumentGroup;
/**
 * 项目文档[数据库操作]
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
public interface DocumentMapper
{

	/**
	 * 添加文档
	 * 
	 * @param document
	 * @return
	 */
	public int addDocument(Document document);

	/**
	 * 删除文档
	 * 
	 * @param documentId
	 * @return
	 */
	public int deleteDocument(@Param("documentID") int documentID);

	/**
	 * 根据分组获取文档列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<DocumentDTO> getDocumentList(@Param("groupIDS") List<Integer> groupIDS);

	/**
	 * 获取所有文档列表
	 * 
	 * @param projectId
	 * @return
	 */
	public List<DocumentDTO> getAllDocumentList(@Param("projectID") int projectID);

	/**
	 * 修改文档
	 * 
	 * @param document
	 * @return
	 */
	public int editDocument(Document document);

	/**
	 * 搜索文档
	 * 
	 * @param projectId
	 * @param tips
	 * @return
	 */
	public List<DocumentDTO> searchDocument(@Param("projectID") int projectID, @Param("tips") String tips);

	/**
	 * 获取文档详情
	 * 
	 * @param documentId
	 * @return
	 */
	public Map<String, Object> getDocument(@Param("documentID") int documentID);

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public DocumentGroup getParentGroupInfo(@Param("groupID") Integer groupID);

	/**
	 * 批量删除文档
	 * 
	 * @param documentIDList
	 * @param projectID
	 * @return
	 */
	public int deleteDocuments(@Param("documentIDList") List<Integer> documentIDList,
			@Param("projectID") int projectID);

	/**
	 * 检查文档权限
	 * 
	 * @param documentID
	 * @param userID
	 * @return
	 */
	public Integer checkDocumentPermission(@Param("documentID") int documentID, @Param("userID") int userID);

	/**
	 * 获取文档标题
	 * 
	 * @param documentIDList
	 * @return
	 */
	public String getDocumentTitle(@Param("documentIDList") List<Integer> documentIDList);
}
