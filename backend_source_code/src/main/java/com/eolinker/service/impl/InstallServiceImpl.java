package com.eolinker.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.eolinker.config.Config;
import com.eolinker.service.InstallService;
import com.eolinker.util.DbUtil;

@Service
public class InstallServiceImpl implements InstallService
{
	@Autowired
	private Config config;

	@Override
	public Map<String, Object> checkoutEnv(String dbURL, String dbName, String dbUser, String dbPassword)
	{
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		// 检查导出文件所在文件夹是否有读写权限
		try
		{
			File classPath = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!classPath.exists())
				classPath = new File("");
			File dir = new File(classPath.getAbsolutePath(), "dump");
			if (!dir.exists() || !dir.isDirectory())
				dir.mkdirs();
			if (!dir.canWrite())
			{
				result.put("fileWrite", 0);
			}
			result.put("fileWrite", 1);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			result.put("fileWrite", 0);
		}
		// 检查是否已经安装

		if ( config.getVersion() != null && !config.getVersion().equals(""))
		{
			result.put("isInstalled", 1);
		}
		else
		{
			result.put("isInstalled", 0);
		}

		// 检查数据库连接
		String[] uri = dbURL.split(":");
		dbURL = uri[0];
		String port = null;
		if (uri.length < 2 || uri[1] == null)
		{
			port = "3306";
		}
		else
		{
			port = uri[1];
		}
		DbUtil dbUtil = new DbUtil(dbURL, port, dbName, dbUser, dbPassword);
		boolean status = dbUtil.getConn();
		if (status)
			result.put("db", 1);
		else
			result.put("db", 0);
		return result;
	}

	@Override
	public boolean start(String dbURL, String dbName, String dbUser, String dbPassword, String websiteName,
			String language)
	{
		// TODO Auto-generated method stub
		if (language == null)
		{
			language = "zh-cn";
		}
		if (websiteName == null)
		{
			websiteName = "eolinker开源版";
		}
		String[] uri = dbURL.split(":");
		dbURL = uri[0];
		String port = null;
		if (uri.length < 2 || uri[1] == null)
		{
			port = "3306";
		}
		else
		{
			port = uri[1];
		}
		try
		{
			DbUtil dbUtil = new DbUtil(dbURL, port, dbName, dbUser, dbPassword);
			boolean result = dbUtil.installDatabase();
			if (!result)
				return false;
			String version = "v4.0";
			String dbUrl = "jdbc:mysql://" + dbURL + ":" + port + "/" + dbName + "?characterEncoding=UTF-8";
			Properties properties = new Properties();
			FileOutputStream fileOutputStream = new FileOutputStream(
					System.getProperty("user.dir") + "/config/setting.properties", false);
			properties.setProperty("language", language);
			properties.setProperty("version", version);
			properties.setProperty("webSitename", websiteName);
			properties.setProperty("dbURL", dbUrl);
			properties.setProperty("dbUser", dbUser);
			properties.setProperty("dbPassword", dbPassword);
			properties.setProperty("allowUpdate", "true");
			properties.setProperty("allowRegister", "true");
			properties.setProperty("port", config.getPort());
			properties.store(fileOutputStream, "配置文件");
			fileOutputStream.flush();
			fileOutputStream.close();
			config.setVersion(version);
			config.setLanguage(language);
			config.setDbUrl(dbUrl);
			config.setDbUser(dbUser);
			config.setDbPassword(dbPassword);
			return true;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
