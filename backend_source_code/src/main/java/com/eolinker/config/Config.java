package com.eolinker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 * 项目配置类
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
@Component
@PropertySource("file:${user.dir}/config/setting.properties")
@ConfigurationProperties()
public class Config
{
	private String allowUpdate; //是否允许自动更新
	private String allowRegister; //字符允许注册
	private String language; //语言类型
	private String version; //版本
	private String webSitename; //网站名称
	private String dbUrl; //数据库路径
	private String dbUser; //数据库用户名称
	private String dbPassword; //数据库密码
	private String port;//项目启动端口
	private String path;

	public String getAllowUpdate()
	{
		return allowUpdate;
	}

	public void setAllowUpdate(String allowUpdate)
	{
		this.allowUpdate = allowUpdate;
	}

	public String getAllowRegister()
	{
		return allowRegister;
	}

	public void setAllowRegister(String allowRegister)
	{
		this.allowRegister = allowRegister;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getWebSitename()
	{
		return webSitename;
	}

	public void setWebSitename(String webSitename)
	{
		this.webSitename = webSitename;
	}

	public String getDbUrl()
	{
		return dbUrl;
	}

	public void setDbUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}

	public String getDbUser()
	{
		return dbUser;
	}

	public void setDbUser(String dbUser)
	{
		this.dbUser = dbUser;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}
}
