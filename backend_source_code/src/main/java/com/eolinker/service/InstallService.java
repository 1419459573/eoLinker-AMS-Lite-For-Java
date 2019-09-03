package com.eolinker.service;

import java.util.Map;

public interface InstallService
{

	public Map<String, Object> checkoutEnv(String dbURL, String dbName, String dbUser, String dbPassword);

	public boolean start(String dbURL, String dbName, String dbUser, String dbPassword, String websiteName,
			String language);

}
