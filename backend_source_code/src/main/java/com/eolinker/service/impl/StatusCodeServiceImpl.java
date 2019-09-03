package com.eolinker.service.impl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eolinker.dto.CodeListDTO;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.PartnerMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.mapper.StatusCodeGroupMapper;
import com.eolinker.mapper.StatusCodeMapper;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.pojo.StatusCode;
import com.eolinker.pojo.StatusCodeGroup;
import com.eolinker.service.StatusCodeService;
/**
 * 状态码[业务处理层]
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
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "java.lang.Exception")
public class StatusCodeServiceImpl implements StatusCodeService
{

	@Autowired
	private StatusCodeMapper statusCodeMapper;

	@Autowired
	private StatusCodeGroupMapper statusCodeGroupMapper;

	@Autowired
	private PartnerMapper partnerMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 获取用户权限
	 */
	@Override
	public int getUserType(int codeID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeMapper.checkStatusCodePermission(codeID, userID);
		if (projectID == null || projectID < 1)
			return -1;

		Partner projectUserType = this.partnerMapper.getProjectUserType(userID, projectID);
		if (projectUserType != null)
			return projectUserType.getUserType();
		else
			return -1;
	}

	/**
	 * 添加状态码
	 */
	@Override
	public int addCode(StatusCode statusCode) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(statusCode.getGroupID(), userID);
		if (projectID == null || projectID < 1)
			return -1;
		else
		{
			this.projectMapper.updateProjectUpdateTime(projectID, null);
			if (this.statusCodeMapper.addCode(statusCode) < 1)
				throw new RuntimeException("addCode error");
			else
			{
				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(projectID);
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE);
				projectOperationLog.setOpTargetID(statusCode.getCodeID());
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
				projectOperationLog.setOpDesc("添加状态码:" + statusCode.getCode());

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return statusCode.getCodeID();
			}
		}
	}

	/**
	 * 删除状态码
	 */
	@Override
	public int deleteCode(int codeID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeMapper.checkStatusCodePermission(codeID, userID);
		if (projectID == null || projectID < 1)
			return -1;
		else
		{
			if (this.statusCodeMapper.deleteCode(codeID) < 1)
				throw new RuntimeException("deleteCode error");
			else
			{
				this.projectMapper.updateProjectUpdateTime(projectID, null);

				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(projectID);
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE);
				projectOperationLog.setOpTargetID(codeID);
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
				projectOperationLog.setOpDesc("删除状态码:" + codeID);

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return 1;
			}
		}
	}

	/**
	 * 批量删除状态码
	 */
	@Override
	public int deleteBatchCode(List<Integer> codeIDs) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = null;
		for (int codeID : codeIDs)
		{
			projectID = this.statusCodeMapper.checkStatusCodePermission(codeID, userID);
			if (projectID == null || projectID < 1)
				return -1;
			else
				continue;
		}
		String statusCodes = this.statusCodeMapper.getStatusCodes(codeIDs);
		if (this.statusCodeMapper.deleteCodes(codeIDs) < 1)
			throw new RuntimeException("deleteCodes error");
		else
		{
			this.projectMapper.updateProjectUpdateTime(projectID, null);

			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpUerID(userID);
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE);
			projectOperationLog.setOpTargetID(0);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpDesc("删除状态码:" + statusCodes);

			this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return 1;
		}

	}

	/**
	 * 获取状态码列表
	 */
	@Override
	public List<CodeListDTO> getCodeList(int groupID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(groupID, userID);
		if (projectID == null || projectID < 1)
			return null;
		else
		{
			List<Integer> groupIDS = new ArrayList<Integer>();
			groupIDS.add(groupID);
			List<StatusCodeGroup> childGroupList = statusCodeGroupMapper.getChildList(projectID,groupID);
			if(childGroupList != null && !childGroupList.isEmpty())
			{
				for (StatusCodeGroup group : childGroupList)
				{
					groupIDS.add(group.getGroupID());
					List<StatusCodeGroup> childGroupList1 = statusCodeGroupMapper.getChildList(projectID, group.getGroupID());
					if(childGroupList1 != null && !childGroupList1.isEmpty())
					{
						for (StatusCodeGroup group1 : childGroupList1)
						{
							groupIDS.add(group1.getGroupID());
						}
					}
				}
			}
			List<CodeListDTO> codeList = this.statusCodeMapper.getCodeList(groupIDS);
			if(codeList != null && !codeList.isEmpty())
			{
				for(CodeListDTO codeListDTO : codeList)
				{
					Integer topParentGroupID = statusCodeGroupMapper.getParentGroupID(codeListDTO.getParentGroupID());
					topParentGroupID = topParentGroupID != null ? topParentGroupID : 0;
					codeListDTO.setTopParentGroupID(topParentGroupID);
				}
			}
			return (codeList == null || codeList.isEmpty()) ? null : codeList;
		}

	}

	/**
	 * 获取全部状态码
	 */
	@Override
	public List<CodeListDTO> getAllCodeList(int projectID) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return null;
		else
		{
			List<CodeListDTO> codeList = this.statusCodeMapper.getAllCodeList(projectID);
			if(codeList != null && !codeList.isEmpty())
			{
				for(CodeListDTO codeListDTO : codeList)
				{
					Integer topParentGroupID = statusCodeGroupMapper.getParentGroupID(codeListDTO.getParentGroupID());
					topParentGroupID = topParentGroupID != null ? topParentGroupID : 0;
					codeListDTO.setTopParentGroupID(topParentGroupID);
				}
			}
			return (codeList == null || codeList.isEmpty()) ? null : codeList;
		}
	}

	/**
	 * 修改状态码
	 */
	@Override
	public int editCode(StatusCode statusCode) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Integer projectID = this.statusCodeMapper.checkStatusCodePermission(statusCode.getCodeID(), userID);
		if (projectID == null || projectID < 1)
			return -1;
		else
		{
			this.projectMapper.updateProjectUpdateTime(statusCode.getGroupID(), null);
			int affectedRow = this.statusCodeMapper.editCode(statusCode);

			if (affectedRow < 1)
				throw new RuntimeException("editCode error");
			else
			{
				ProjectOperationLog projectOperationLog = new ProjectOperationLog();
				projectOperationLog.setOpProjectID(projectID);
				projectOperationLog.setOpUerID(userID);
				projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE);
				projectOperationLog.setOpTargetID(statusCode.getCodeID());
				projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
				projectOperationLog.setOpDesc("修改状态码:" + statusCode.getCodeID());

				this.projectOperationLogMapper.addProjectOperationLog(projectOperationLog);

				return 1;
			}
		}
	}

	/**
	 * 搜索状态码
	 */
	@Override
	public List<CodeListDTO> searchStatusCode(int projectID, String tips) throws RuntimeException
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return null;
		else
		{
			List<CodeListDTO> resultList = this.statusCodeMapper.searchStatusCode(projectID, tips);
			return (resultList == null || resultList.isEmpty()) ? null : resultList;
		}
	}

	/**
	 * 获取状态码数量
	 */
	@Override
	public int getStatusCodeNum(int projectID)
	{
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		int userID = (int) requestAttributes.getAttribute("userID", RequestAttributes.SCOPE_SESSION);

		Project project = this.projectMapper.getProject(userID, projectID);
		if (project == null)
			return -1;
		else
		{
			Integer count = this.statusCodeMapper.getStatusCodeCount(projectID);
			return (count != null) ? count : -1;
		}
	}

	/**
	 * 通过excel表导入状态码
	 */
	@Override
	public boolean addStatusCodeByExcel(Integer groupID, Integer userID, InputStream inputStream)
	{
		// TODO Auto-generated method stub
		try
		{
			@SuppressWarnings("resource")
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			StatusCode statusCode = new StatusCode();
			for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++)
			{
				HSSFRow hassfRow = hssfSheet.getRow(rowNum);
				HSSFCell cell1 = hassfRow.getCell(0);
				HSSFCell cell2 = hassfRow.getCell(1);
				if (cell1 == null || cell2 == null)
				{
					continue;
				}
				hassfRow.getCell(0).setCellType(CellType.STRING);
				hassfRow.getCell(1).setCellType(CellType.STRING);
				statusCode.setCode(cell1.getStringCellValue());
				statusCode.setCodeDescription(cell2.getStringCellValue());
				statusCode.setGroupID(groupID);
				if (statusCodeMapper.addCode(statusCode) < 1)
					throw new RuntimeException("addCode error");
			}
			Integer projectID = this.statusCodeGroupMapper.checkStatusCodeGroupPermission(groupID, userID);
			Date date = new Date();
			Timestamp updateTime = new Timestamp(date.getTime());
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("通过导入Excel添加状态码");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_STATUS_CODE);
			projectOperationLog.setOpTargetID(groupID);
			projectOperationLog.setOpTime(updateTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
	}

}
