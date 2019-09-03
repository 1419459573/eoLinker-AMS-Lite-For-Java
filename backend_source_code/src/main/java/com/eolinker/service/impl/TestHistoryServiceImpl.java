package com.eolinker.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eolinker.mapper.TestHistoryMapper;
import com.eolinker.pojo.TestHistory;
import com.eolinker.service.TestHistoryService;
/**
 * 测试历史[业务处理层]
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
public class TestHistoryServiceImpl implements TestHistoryService
{

	@Autowired
	private TestHistoryMapper testHistoryMapper;
	
	/**
	 * 添加测试历史
	 */
	@Override
	public Integer addTestHistory(Integer projectID, Integer apiID, String requestInfo, String resultInfo,
			Timestamp testTime)
	{
		// TODO Auto-generated method stub
		TestHistory testHistory = new TestHistory();
		testHistory.setApiID(apiID);
		testHistory.setProjectID(projectID);
		testHistory.setRequestInfo(requestInfo);
		testHistory.setResultInfo(resultInfo);
		testHistory.setTestTime(testTime);
		if(testHistoryMapper.addTestHistory(testHistory) > 0)
			return testHistory.getTestID();
		return null;
	}
	
	/**
	 * 删除测试历史
	 */
	@Override
	public boolean deleteTestHistory(Integer projectID, Integer userID, Integer testID)
	{
		// TODO Auto-generated method stub
		if (testHistoryMapper.deleteTestHistory(projectID, testID) > 0)
			return true;
		else 
			return false;
	}
	
	/**
	 * 清空测试历史
	 */
	@Override
	public boolean deleteAllTestHistory(Integer projectID, Integer userID, Integer apiID)
	{
		// TODO Auto-generated method stub
		if (testHistoryMapper.deleteAllTestHistory(projectID, apiID) > 0)
			return true;
		else 
			return false;
	}

}
