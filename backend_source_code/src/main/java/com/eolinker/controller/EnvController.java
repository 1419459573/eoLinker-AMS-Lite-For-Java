package com.eolinker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eolinker.pojo.EnvHeader;
import com.eolinker.pojo.EnvParam;
import com.eolinker.pojo.EnvParamAdditional;
import com.eolinker.service.EnvService;
/**
 * 环境管理控制器
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
@Controller
@RequestMapping("/Env")
public class EnvController {

	@Autowired
	private EnvService envService;


	/**
	 * 获取项目环境列表
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getEnvList")
	public Map<String, Object> getEnvList(@RequestParam("projectID") String projectID) {
		Map<String,Object> map = new HashMap<String,Object>();

		try {
			if(projectID == null || !projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "170003");
			else {
				map = this.envService.getEnvList(Integer.parseInt(projectID));
				if(map == null) {
					map = new HashMap<String,Object>();
					map.put("statusCode", "170000");
				} else
					map.put("statusCode", "000000");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			map.put("statusCode", "170003");
			return map;
		}

		return map;
	}


	/**
	 * 添加项目环境
	 * @param envName
	 * @param frontURI
	 * @param headers
	 * @param params
	 * @param additionalParams
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addEnv")
	public Map<String, Object> addEnv(@RequestParam("projectID") String projectID, @RequestParam("envName") String envName, @RequestParam(value="frontURI",required=false) String frontURI,
										@RequestParam(value="headers",required=false) String headersString, @RequestParam(value="params",required=false) String paramsString,
										  @RequestParam(value="additionalParams",required=false) String additionalParamsString) {
		Map<String,Object> map = new HashMap<String,Object>();

		try {
			// create headers list
			List<EnvHeader> headers = new ArrayList<EnvHeader>();
			JSONObject headersObject = JSON.parseObject(headersString);
			Set<String> headersKey = headersObject.keySet();
			for(String key : headersKey) {
				EnvHeader envHeader = new EnvHeader();
				envHeader.setHeaderName(key);
				envHeader.setHeaderValue(headersObject.getString(key));
				headers.add(envHeader);
			}
			
			// create params list
			List<EnvParam> params = new ArrayList<EnvParam>();
			JSONObject paramsObject = JSON.parseObject(paramsString);
			Set<String> paramsKey = paramsObject.keySet();
			for(String key : paramsKey) {
				EnvParam envParam = new EnvParam();
				envParam.setParamKey(key);
				envParam.setParamValue(paramsObject.getString(key));
				params.add(envParam);
			}
			
			// create additionalParams list
			List<EnvParamAdditional> additionalParams = new ArrayList<EnvParamAdditional>();
			JSONObject additionalParamsObject = JSON.parseObject(additionalParamsString);
			Set<String> additionalParamsKey = additionalParamsObject.keySet();
			for(String key : additionalParamsKey) {
				EnvParamAdditional paramAdditional = new EnvParamAdditional();
				paramAdditional.setParamKey(key);
				paramAdditional.setParamValue(additionalParamsObject.getString(key));
				additionalParams.add(paramAdditional);
			}
			
			int applyProtocol = -1;
			if(projectID == null || !projectID.matches("^[0-9]{1,11}$"))				
				map.put("statusCode", "170003");
			else if(envName == null || envName.length() < 1 || envName.length() > 32)	//判断名称长度是否合法
				map.put("statusCode", "170001");
			else {
				int result = this.envService.addEnv(Integer.parseInt(projectID), envName, frontURI, applyProtocol, headers, params, additionalParams);

				if (result > 0) {
					map.put("statusCode", "000000");
					map.put("envID", result);
				} else 
					map.put("statusCode", "170000");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			map.put("statusCode", "170003");
			return map;
		} catch (RuntimeException e) {
			e.printStackTrace();
			map.put("statusCode", "170000");
			return map;
		}
		return map;
	}



	/**
	 * 删除项目环境
	 * @param envID
	 * @param projectID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteEnv")
	public Map<String, Object> deleteEnv(@RequestParam("envID") String envID, @RequestParam("projectID") String projectID) {
		Map<String,Object> map = new HashMap<String,Object>();

		try {
			if(!projectID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "170003");
			else if(!envID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "170002");
			else {
				int result = this.envService.deleteEnv(Integer.parseInt(projectID), Integer.parseInt(envID));
				map.put("statusCode", (result > 0) ? "000000" : "170000");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			map.put("statusCode", "170003");
			return map;
		} catch (RuntimeException e) {
			e.printStackTrace();	
			map.put("statusCode", "170000");
			return map;
		}

		return map;
	}


	/**
	 * 修改项目环境
	 * @param projectID
	 * @param envName
	 * @param frontURI
	 * @param headers
	 * @param params
	 * @param additionalParams
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editEnv")
	public Map<String, Object> editEnv(@RequestParam("envID") String envID, @RequestParam("envName") String envName, @RequestParam(value="frontURI",required=false) String frontURI,
										@RequestParam(value="headers",required=false) String headersString, @RequestParam(value="params",required=false) String paramsString,
										  @RequestParam(value="additionalParams",required=false) String additionalParamsString) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			// update headers list
			List<EnvHeader> headers = new ArrayList<EnvHeader>();
			JSONObject headersObject = JSON.parseObject(headersString);
			Set<String> headersKey = headersObject.keySet();
			for(String key : headersKey) {
				EnvHeader envHeader = new EnvHeader();
				envHeader.setHeaderName(key);
				envHeader.setHeaderValue(headersObject.getString(key));
				headers.add(envHeader);
			}
			
			// update params list
			List<EnvParam> params = new ArrayList<EnvParam>();
			JSONObject paramsObject = JSON.parseObject(paramsString);
			Set<String> paramsKey = paramsObject.keySet();
			for(String key : paramsKey) {
				EnvParam envParam = new EnvParam();
				envParam.setParamKey(key);
				envParam.setParamValue(paramsObject.getString(key));
				params.add(envParam);
			}
			
			// update additionalParams list
			List<EnvParamAdditional> additionalParams = new ArrayList<EnvParamAdditional>();
			JSONObject additionalParamsObject = JSON.parseObject(additionalParamsString);
			Set<String> additionalParamsKey = additionalParamsObject.keySet();
			for(String key : additionalParamsKey) {
				EnvParamAdditional paramAdditional = new EnvParamAdditional();
				paramAdditional.setParamKey(key);
				paramAdditional.setParamValue(additionalParamsObject.getString(key));
				additionalParams.add(paramAdditional);
			}
			
			int applyProtocol = -1;
			if(envName.length() < 1 || envName.length() > 32)
				map.put("statusCode", "170001");
			else if(!envID.matches("^[0-9]{1,11}$"))
				map.put("statusCode", "170002");
			else {
				int result = this.envService.editEnv(Integer.parseInt(envID), envName, frontURI, applyProtocol, headers, params, additionalParams);
				map.put("statusCode", (result > 0) ? "000000" : "170000");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			map.put("statusCode", "170002");
			return map;
		} catch (RuntimeException e) {
			e.printStackTrace();
			map.put("statusCode", "170000");
			return map;
		}
		
		return map;
	}




}
