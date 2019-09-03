package com.eolinker.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eolinker.mapper.MessageMapper;
import com.eolinker.mapper.PartnerMapper;
import com.eolinker.mapper.ProjectMapper;
import com.eolinker.mapper.ProjectOperationLogMapper;
import com.eolinker.pojo.Message;
import com.eolinker.pojo.Partner;
import com.eolinker.pojo.Project;
import com.eolinker.pojo.ProjectOperationLog;
import com.eolinker.service.PartnerService;
/**
 * 项目成员[业务处理层]
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
@Transactional(propagation=Propagation.REQUIRED,rollbackForClassName="java.lang.Exception")
public class PartnerServiceImpl implements PartnerService
{

	@Autowired
	private PartnerMapper partnerMapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private ProjectOperationLogMapper projectOperationLogMapper;

	/**
	 * 检查成员是否已经存在[业务处理层]
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
	@Override
	public int checkIsInvited(Integer projectID, String userName)
	{
		// TODO Auto-generated method stub
		Integer connID = partnerMapper.checkIsInvited(projectID, userName);
		if (connID != null && connID > 0)
			return connID;
		else
			return -1;
	}

	/**
	 * 邀请成员[业务处理层]
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
	@Override
	public int invitePartner(Integer projectID, Integer inviteUserID, Integer userID, String userName)
	{
		// TODO Auto-generated method stub
		Project project = projectMapper.getProject(inviteUserID, projectID);

		String summary = "您已被邀请加入项目：" + project.getProjectName() + "，开始您的高效协作之旅吧！";
		String msg = "<p>您好！亲爱的用户：</p><p>您已经被加入项目：<b style='color:#4caf50'>" + project.getProjectName()
				+ "</b>，现在你可以参与项目的开发协作工作。</p>"
				+ "<p>如果您在使用的过程中遇到任何问题，欢迎前往<a href='http://blog.eolinker.com/#/bbs/'><b style='color:#4caf50'>交流社区</b></a>反馈意见，谢谢！。</p>";

		Partner partner = new Partner();
		partner.setInviteUserID(inviteUserID);
		partner.setProjectID(projectID);
		partner.setUserID(userID);
		partner.setUserType(2);

		// 邀请协作人员
		int result = partnerMapper.addPartner(partner);
		if (result > 0 && partner.getConnID() > 0)
		{

			Message message = new Message();
			message.setFromUserID(inviteUserID);
			message.setToUserID(userID);
			message.setMsgType(1);
			message.setSummary(summary);
			message.setMsg(msg);
			if (messageMapper.sendMessage(message) < 1)
				throw new RuntimeException("sendMessage error");
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("邀请新成员'" + userName + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PARTNER);
			projectOperationLog.setOpTargetID(userID);
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_ADD);
			projectOperationLog.setOpUerID(inviteUserID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return partner.getConnID();
		}
		else
			return 0;
	}

	/**
	 * 移除成员[业务处理层]
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
	@Override
	public boolean removePartner(Integer projectID, Integer connID, Integer userID)
	{
		// TODO Auto-generated method stub
		Map<String, Object> partnerInfo = partnerMapper.getPartnerInfoByConnID(projectID, connID);
		if (partnerMapper.removePartner(projectID, connID) > 0)
		{
			Project project = projectMapper.getProject(userID, projectID);

			String summary = "您已被移除出项目：" + project.getProjectName();
			String msg = "<p>您好！亲爱的用户：</p><p>您已经被移除出项目：<b style='color:#4caf50'>" + project.getProjectName()
					+ "</b>。</p>"
					+ "<p>如果您在使用的过程中遇到任何问题，欢迎前往<a href='http://blog.eolinker.com/#/bbs/'><b style='color:#4caf50'>交流社区</b></a>反馈意见，谢谢！。</p>";

			Message message = new Message();
			message.setFromUserID(0);
			message.setToUserID(new Integer(partnerInfo.get("userID").toString()));
			message.setMsgType(1);
			message.setSummary(summary);
			message.setMsg(msg);
			if (messageMapper.sendMessage(message) < 1)
				throw new RuntimeException("sendMessage error");
			String name = "";
			if (partnerInfo.get("partnerNickName") == null)
			{
				name = (String) partnerInfo.get("userName");
			}
			else
			{
				name = (String) partnerInfo.get("partnerNickName");
			}
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("移除成员'" + name + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PARTNER);
			projectOperationLog.setOpTargetID(new Integer(partnerInfo.get("userID").toString()));
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取成员列表[业务处理层]
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
	@Override
	public List<Map<String, Object>> getPartnerList(Integer projectID, Integer userID)
	{
		// TODO Auto-generated method stub
		List<Map<String, Object>> partnerList = partnerMapper.getPartnerList(projectID);
		for (Map<String, Object> partner : partnerList)
		{
			if (new Integer(partner.get("userID").toString()).equals(userID))
				partner.put("isNow", 1);
			else
				partner.put("isNow", 0);
			partner.remove("userID");
		}
		return partnerList;
	}

	/**
	 * 退出项目[业务处理层]
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
	@Override
	public boolean quitPartner(Integer projectID, Integer userID, String userName)
	{
		// TODO Auto-generated method stub
		if (partnerMapper.quitPartner(projectID, userID) > 0)
		{
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("'" + userName + "'退出项目协作");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PARTNER);
			projectOperationLog.setOpTargetID(userID);
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_DELETE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		return false;
	}

	/**
	 * 修改成员备注
	 */
	@Override
	public boolean editPartnerNickName(Integer projectID, Integer connID, Integer userID, String nickName)
	{
		// TODO Auto-generated method stub
		Map<String, Object> partnerInfo = partnerMapper.getPartnerInfoByConnID(projectID, connID);
		if (partnerMapper.editPartnerNickName(projectID, connID, nickName) > 0)
		{
			String name = "";
			if (partnerInfo.get("partnerNickName") == null)
			{
				name = (String) partnerInfo.get("userName");
			}
			else
			{
				name = (String) partnerInfo.get("partnerNickName");
			}
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("修改成员'" + name + "'备注为'" + nickName + "'");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PARTNER);
			projectOperationLog.setOpTargetID(Integer.valueOf(partnerInfo.get("userID").toString()));
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

	/**
	 * 修改成员权限类型
	 */
	@Override
	public boolean editPartnerType(Integer projectID, Integer connID, Integer userID, Integer userType)
	{
		// TODO Auto-generated method stub
		Map<String, Object> partnerInfo = partnerMapper.getPartnerInfoByConnID(projectID, connID);
		if (partnerMapper.editPartnerType(projectID, connID, userType) > 0)
		{
			String name = "";
			if (partnerInfo.get("partnerNickName") == null)
			{
				name = (String) partnerInfo.get("userName");
			}
			else
			{
				name = (String) partnerInfo.get("partnerNickName");
			}
			String type = "";
			switch (userType)
			{
				case 1:
					type = "管理员";
					break;
				case 2:
					type = "普通成员（读写）";
					break;
				case 3:
					type = "普通成员（只读）";
					break;
				default:
					break;
			}
			System.out.println(type);
			Date date = new Date();
			Timestamp nowTime = new Timestamp(date.getTime());
			projectMapper.updateProjectUpdateTime(projectID, nowTime);
			ProjectOperationLog projectOperationLog = new ProjectOperationLog();
			projectOperationLog.setOpProjectID(projectID);
			projectOperationLog.setOpDesc("修改成员'" + name + "'为" + type + "");
			projectOperationLog.setOpTarget(ProjectOperationLog.OP_TARGET_PARTNER);
			projectOperationLog.setOpTargetID(Integer.valueOf(partnerInfo.get("userID").toString()));
			projectOperationLog.setOpTime(nowTime);
			projectOperationLog.setOpType(ProjectOperationLog.OP_TYPE_UPDATE);
			projectOperationLog.setOpUerID(userID);
			projectOperationLogMapper.addProjectOperationLog(projectOperationLog);
			return true;
		}
		else
			return false;
	}

}
