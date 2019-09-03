package com.eolinker.pojo;
/**
 * 环境URI
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
public class EnvFrontUri {

	private Integer envID;//环境ID
	private String uri;//环境路径
	private Integer uriID;//环境路径ID
	private Integer applyProtocol;//请求协议
	
	public Integer getEnvID() {
		return envID;
	}
	public void setEnvID(Integer envID) {
		this.envID = envID;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Integer getUriID() {
		return uriID;
	}
	public void setUriID(Integer uriID) {
		this.uriID = uriID;
	}
	public Integer getApplyProtocol() {
		return applyProtocol;
	}
	public void setApplyProtocol(Integer applyProtocol) {
		this.applyProtocol = applyProtocol;
	}
	
	
}
