package com.eolinker.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.alibaba.fastjson.JSON;
/**
 * 登陆校验拦截器
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
public class LoginInterceptor extends HandlerInterceptorAdapter
{
	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 如果返回true
	 * 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		HttpSession session = request.getSession(true);
		if (session.getAttribute("userID") == null)
		{
			//登陆后门，避免前端跨域调用接口被拦截
//			Cookie[] cookie = request.getCookies();
//			for (int i = 0; i < cookie.length; i++)
//			{
//				Cookie cook = cookie[i];
//				if (cook.getName().equals("hackCode") && cook.getValue().equals("09c82nocr897smjyoy3487qrao87x"))
//				{ // 获取键
//					for(int j = 0; j < cookie.length; j++)
//					{
//						Cookie cook1 = cookie[j];
//						if(cook.getName().equals("userID") && cook1.getValue() != null && cook1.getValue().length() > 0)
//						{
//							session.setAttribute("userID", Integer.parseInt(cook1.getValue()));
//							return true;
//						}
//					}
//				}
//			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("statusCode", "100001");
			PrintWriter out = response.getWriter();
			out.println(JSON.toJSONString(map));
			return false;
		}
		return true;
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{

	}
}
