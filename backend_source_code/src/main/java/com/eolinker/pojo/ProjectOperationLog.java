package com.eolinker.pojo;

import java.sql.Timestamp;
/**
 * 项目操作日志
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
public class ProjectOperationLog
{
	private Integer opID;// 日志ID
	private Integer opType;// 操作类型
	private Integer opUserID;// 操作用户ID
	private String opDesc;// 操作描述
	private Timestamp opTime;// 操作时间
	private Integer opProjectID;// 操作项目ID
	private Integer opTarget;// 操作目标
	private Integer opTargetID;// 操作目标ID
	private String partnerNickName;//成员备注
	private String userNickName;//成员昵称

	public static Integer OP_TYPE_ADD = 0; //添加操作
	public static Integer OP_TYPE_UPDATE = 1;//更新操作
	public static Integer OP_TYPE_DELETE = 2;//删除操作
	public static Integer OP_TYPE_OTHERS = 3;//其他操作
	public static Integer OP_TARGET_PROJECT = 0;//操作项目
	public static Integer OP_TARGET_API = 1;//操作接口
	public static Integer OP_TARGET_API_GROUP = 2;//操作接口分组
	public static Integer OP_TARGET_STATUS_CODE = 3;//操作状态码
	public static Integer OP_TARGET_STATUS_CODE_GROUP = 4;//操作状态码分组
	public static Integer OP_TARGET_ENVIRONMENT = 5;//操作环境
	public static Integer OP_TARGET_PARTNER = 6;//操作协作成员
	public static Integer OP_TARGET_PROJECT_DOCUMENT_GROUP = 7;//操作文档分组
	public static Integer OP_TARGET_PROJECT_DOCUMENT = 8;//操作文档
	public static Integer OP_TARGET_AUTOMATED_TEST_CASE_GROUP = 9;//操作用例分组
	public static Integer OP_TARGET_AUTOMATED_TEST_CASE = 10;//操作用例

	public Integer getOpID()
	{
		return opID;
	}

	public void setOpID(Integer opID)
	{
		this.opID = opID;
	}

	public Integer getOpType()
	{
		return opType;
	}

	public void setOpType(Integer opType)
	{
		this.opType = opType;
	}

	public Integer getOpUserID()
	{
		return opUserID;
	}

	public void setOpUerID(Integer opUserID)
	{
		this.opUserID = opUserID;
	}

	public String getOpDesc()
	{
		return opDesc;
	}

	public void setOpDesc(String openDesc)
	{
		this.opDesc = openDesc;
	}

	public Timestamp getOpTime()
	{
		return opTime;
	}

	public void setOpTime(Timestamp opTime)
	{
		this.opTime = opTime;
	}

	public Integer getOpProjectID()
	{
		return opProjectID;
	}

	public void setOpProjectID(Integer opProjectID)
	{
		this.opProjectID = opProjectID;
	}

	public Integer getOpTarget()
	{
		return opTarget;
	}

	public void setOpTarget(Integer opTarget)
	{
		this.opTarget = opTarget;
	}

	public Integer getOpTargetID()
	{
		return opTargetID;
	}

	public void setOpTargetID(Integer opTargetID)
	{
		this.opTargetID = opTargetID;
	}

	public String getPartnerNickName() {
		return partnerNickName;
	}

	public void setPartnerNickName(String partnerNickName) {
		this.partnerNickName = partnerNickName;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

}
