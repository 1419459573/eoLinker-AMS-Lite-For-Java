package com.eolinker.service.impl;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.eolinker.config.Config;
import com.eolinker.service.UpdateService;
import com.eolinker.util.DbUtil;
/**
 * 更新[业务处理层]
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
public class UpdateServiceImpl implements UpdateService
{

	@Resource
	private Config config;
	
	@Override
	public boolean autoUpdate(String updateUrl)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean manualUpdate()
	{
		// TODO Auto-generated method stub
		DbUtil dbUtil = new DbUtil();
		if(dbUtil.updateDatabase(config.getDbUrl(), config.getDbUser(), config.getDbPassword()))
		{			
			try
			{
				String version = "v4.0";
				Properties properties = new Properties();
				FileOutputStream fileOutputStream;
				fileOutputStream = new FileOutputStream(
						System.getProperty("user.dir") + "/config/setting.properties", false);
				properties.setProperty("version", version);
				properties.setProperty("webSitename", config.getWebSitename());
				properties.setProperty("dbURL", config.getDbUrl());
				properties.setProperty("dbUser", config.getDbUser());
				properties.setProperty("dbPassword", config.getDbPassword());
				properties.setProperty("allowUpdate", "true");
				properties.setProperty("allowRegister", "true");
				properties.setProperty("port", config.getPort());
				properties.store(fileOutputStream, "配置文件");
				fileOutputStream.flush();
				fileOutputStream.close();
				config.setVersion(version);
				return true;
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
	}
	
}
