package com.eolinker.exception;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 全局异常类
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
@ControllerAdvice 
public class GlobalExceptionResolver {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, String> exceptionHandler(HttpServletRequest request, Exception e) {
		// 对捕获的异常进行处理并打印日志等，之后返回json数据，方式与Controller相同
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", "100003");
		return map;
	}

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public Map<String, String> runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
		// 对捕获的异常进行处理并打印日志等，之后返回json数据，方式与Controller相同
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", "100004");
		map.put("error", e.getMessage());
		return map;
	}

	@ExceptionHandler(value = SQLException.class)
	@ResponseBody
	public Map<String, String> sqlExceptionHandler(HttpServletRequest request, SQLException e) {
		// 对捕获的异常进行处理并打印日志等，之后返回json数据，方式与Controller相同
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", "100005");
		return map;
	}
	
	@ExceptionHandler(value = IOException.class)
	@ResponseBody
	public Map<String, String> ioExceptionHandler(HttpServletRequest request, IOException e) {
		// 对捕获的异常进行处理并打印日志等，之后返回json数据，方式与Controller相同
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", "100006");
		return map;
	}

}