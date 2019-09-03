package com.eolinker.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eolinker.mapper.MessageMapper;
import com.eolinker.mapper.UserMapper;
import com.eolinker.pojo.User;
import com.eolinker.service.UserService;
/**
 * 用户[业务处理层]
 * @name eolinker ams open source，eolinker开源版本
 * @link https://www.eolinker.com/
 * @package eolinker
 * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018
 * eoLinker是目前全球领先、国内最大的在线API接口管理平台，提供自动生成API文档、API自动化测试、Mock测试、团队协作等功能，旨在解决由于前后端分离导致的开发效率低下问题。
 * 如在使用的过程中有任何问题，欢迎加入用户讨论群进行反馈，我们将会以最快的速度，最好的服务态度为您解决问题。
 *
 * eoLinker AMS开源版的开源协议遵循Apache License2.0，如需获取最新的eolinker开源版以及相关资讯，请访问:https://www.eolinker.com/#/os/download
 *
 * 官方网站：https://www.eolinker.com/ 官方博客以及社区：http://blog.eolinker.com/
 * 使用教程以及帮助：http://help.eolinker.com/ 商务合作邮箱：market@eolinker.com
 * 用户讨论QQ群：707530721
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackForClassName="java.lang.Exception")
public class UserServiceImpl implements UserService
{
	@Autowired
	UserMapper userMapper;
	@Autowired
	MessageMapper messageMapper;

	/**
	 * 新建用户
	 */
	@Override
	public Integer addUser(User user)
	{
		// TODO Auto-generated method stub
		// 判断昵称是否为空，为空则自动生成
		if (user.getUserNickName() == null || user.getUserNickName().length() <= 0)
		{
			String[] nickName =
			{ "东方月初", "一叶知秋", "涂山红红", "王权富贵", "君莫笑", "独孤求败", "扫地僧", "东方不败", "风清扬", "金轮法王", "令狐冲", "南帝一灯", "无崖子", "灭绝师太",
					"天山童姥", "奇异种子", "比比鸟", "皮卡丘", "百变怪", "黑胡子", "战国", "鹰眼", "麦哲伦", "蒙其·D·路飞", "鼻屎兽", "触手百合", "七夕青鸟",
					"吼吼鲸", "巨沼怪", "妮可·罗宾", "白胡子", "八神太一", "圆陆鲨", "念力土偶", "毒蔷薇", "黑暗鸦", "小龙女", "杨过", "张三丰", "王重阳", "李莫愁",
					"喷火龙", "九尾", "六尾" };
			int index = (int) (Math.random() * nickName.length);
			user.setUserNickName(nickName[index]);
		}
		user.setUserPassword(getMD5(user.getUserPassword()));
		Integer userID = userMapper.addUser(user);
		// if(userID != 0)
		// {
		// Map<String, Object> demoProject = new HashMap<String, Object>();;
		// }
		return userID;
	}

	/**
	 * 根据用户名获取用户信息
	 */
	@Override
	public User getUserByUserName(String userName)
	{
		// TODO Auto-generated method stub
		return userMapper.getUserByUserName(userName);
	}

	/**
	 * 用户登录
	 */
	@Override
	public Map <String, Object> login(HttpServletRequest request, String userName, String userPassword)
	{
		// TODO Auto-generated method stub
		Map <String, Object> result = new HashMap<String, Object>();
		User user = userMapper.getUserByUserName(userName);
		if(user != null)
		{
			if(getMD5(userPassword).equals(user.getUserPassword()))
			{
				
				HttpSession session =  request.getSession(true);
				session.setAttribute("userID", user.getUserID());
				session.setAttribute("userName", user.getUserName());
				session.setAttribute("userNickName", user.getUserNickName());
				result.put("userID", user.getUserID());
				result.put("JSESSIONID", session.getId());
			}
		}
		return result;
	}

	/**
	 * md5加密密码
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String str)
	{
		try
		{
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String md5 = new BigInteger(1, md.digest()).toString(16);
			// BigInteger会把0省略掉，需补全至32位
			return fillMD5(md5);
		}
		catch(Exception e)
		{
			throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
		}
	}

	/**
	 * 填充0
	 * 
	 * @param md5
	 * @return
	 */
	public static String fillMD5(String md5)
	{
		return md5.length() == 32 ? md5 : fillMD5("0" + md5);
	}


	/**
	 * 修改密码
	 */
	@Override
	public boolean changePassword(String userName, String oldPassword, String newPassword)
	{
		// TODO Auto-generated method stub
		User user= userMapper.getUserByUserName(userName);
		if(getMD5(oldPassword).equals(user.getUserPassword()))
		{
			
			user.setUserPassword(getMD5(newPassword));
			Integer result = userMapper.updatePassword(user);
			if(result > 0)
				return true;
			else
				return false;
		}
		else
			return false;
	
	}

	/**
	 * 修改昵称
	 */
	@Override
	public boolean changeNickName(Integer userID, String nickName)
	{
		// TODO Auto-generated method stub
		User user = new User();
		user.setUserNickName(nickName);
		user.setUserID(userID);
		Integer result = userMapper.updateNickName(user);
		if(result > 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public Map<String, Object> getUserInfo(HttpSession session)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userID", (Integer)session.getAttribute("userID"));
		map.put("userName", (String)session.getAttribute("userName"));
		map.put("userNickName", (String)session.getAttribute("userNickName"));
		map.put("unreadMsgNum", messageMapper.getUnreadMessageNum((Integer)session.getAttribute("userID")));
		// TODO Auto-generated method stub
		return map;
	}

}
