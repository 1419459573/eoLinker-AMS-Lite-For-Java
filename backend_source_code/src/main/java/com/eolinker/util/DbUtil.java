package com.eolinker.util;
/**
 * 数据库操作工具
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DbUtil
{

	private String className;
	private String dbURL;
	private String dbUser;
	private String dbPassword;
	private Connection connection;

	/**
	 * 初始化参数
	 * 
	 * @param dbURL
	 * @param port
	 * @param dbName
	 * @param dbUser
	 * @param dbPassword
	 */
	public DbUtil(String dbURL, String port, String dbName, String dbUser, String dbPassword)
	{
		this.className = "com.mysql.jdbc.Driver";
		this.dbURL = "jdbc:mysql://" + dbURL + ":" + port + "/" + dbName + "?characterEncoding=UTF-8";
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	public DbUtil()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public boolean getConn()
	{
		try
		{
			Class.forName(this.className);
			this.connection = DriverManager.getConnection(this.dbURL, this.dbUser, this.dbPassword);
			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
	}

	public boolean installDatabase()
	{
		try
		{
			Class.forName(this.className);
			this.connection = DriverManager.getConnection(this.dbURL, this.dbUser, this.dbPassword);
			ScriptRunner runner = new ScriptRunner(this.connection);
			runner.setAutoCommit(true);// 自动提交
			runner.setFullLineDelimiter(false);
			runner.setDelimiter(";");//// 每条命令间的分隔符
			runner.setSendFullScript(false);
			runner.setStopOnError(false);
			// runner.setLogWriter(null);//设置是否输出日志
			// 如果又多个sql文件，可以写多个runner.runScript(xxx),
			InputStream stream = getClass().getClassLoader().getResourceAsStream("db/eolinker_os.sql");
			runner.runScript(new InputStreamReader(stream));
			connection.close();
			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 读取sql文件
	 * 
	 * @return
	 */
	public String readSqlFile()
	{
		String encoding = "UTF-8";
		try
		{
			File file = new File(
					Thread.currentThread().getContextClassLoader().getResource("").getPath() + "db/eolinker_os.sql");
			Long filelength = file.length();
			byte[] filecontent = new byte[filelength.intValue()];
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
			return new String(filecontent, encoding);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 更新数据库
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean updateDatabase(String dbURL, String dbUser, String dbPassword)
	{
		try
		{
			this.className = "com.mysql.jdbc.Driver";
			Class.forName(this.className);
			this.connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			List<String> tables = new ArrayList<String>();
			PreparedStatement pst = null;
			ResultSet rs = null;
			DatabaseMetaData dbMetaData = null;
			dbMetaData = connection.getMetaData();
			rs = dbMetaData.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next())
			{
				tables.add(rs.getString("TABLE_NAME"));
			}
			List<Map<String, Object>> oldTables = new ArrayList<>();
			if (tables != null && !tables.isEmpty())
			{
				for (String tableName : tables)
				{
					Map<String, Object> oldTable = new HashMap<>();
					oldTable.put("tableName", tableName);
					String sql = "select * from " + tableName;
					pst = connection.prepareStatement(sql);
					rs = pst.executeQuery();
					ResultSetMetaData meta = rs.getMetaData();
					int columeCount = meta.getColumnCount();
					List<String> columns = new ArrayList<>();
					for (int i = 1; i < columeCount + 1; i++)
					{
						columns.add(meta.getColumnName(i));
					}
					oldTable.put("columns", columns);
					oldTables.add(oldTable);
				}
				if (oldTables != null && !oldTables.isEmpty())
				{
					for (Map<String, Object> oldTable : oldTables)
					{
						pst = connection.prepareStatement("RENAME TABLE " + oldTable.get("tableName") + " TO " + "old_"
								+ oldTable.get("tableName"));
						pst.executeUpdate();
						oldTable.put("tableName", "old_" + oldTable.get("tableName"));
					}
				}
			}
			ScriptRunner runner = new ScriptRunner(this.connection);
			runner.setAutoCommit(true);// 自动提交
			runner.setFullLineDelimiter(false);
			runner.setDelimiter(";");//// 每条命令间的分隔符
			runner.setSendFullScript(false);
			runner.setStopOnError(false);
			// runner.setLogWriter(null);//设置是否输出日志
			// 如果又多个sql文件，可以写多个runner.runScript(xxx),
			InputStream stream = getClass().getClassLoader().getResourceAsStream("db/eolinker_os.sql");
			runner.runScript(new InputStreamReader(stream));
			List<String> tables1 = new ArrayList<String>();
			dbMetaData = connection.getMetaData();
			rs = dbMetaData.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next())
			{
				tables1.add(rs.getString("TABLE_NAME"));
			}
			if (tables1 != null && !tables1.isEmpty())
			{
				List<Map<String, Object>> newTables = new ArrayList<>();
				for (String tableName : tables1)
				{
					if (tableName.contains("old"))
					{
						continue;
					}
					Map<String, Object> newTable = new HashMap<>();
					newTable.put("tableName", tableName);
					String sql = "select * from " + tableName;
					pst = connection.prepareStatement(sql);
					rs = pst.executeQuery();
					ResultSetMetaData meta = rs.getMetaData();
					int columeCount = meta.getColumnCount();
					List<String> columns = new ArrayList<>();
					for (int i = 1; i < columeCount + 1; i++)
					{
						columns.add(meta.getColumnName(i));
					}
					newTable.put("columns", columns);
					newTables.add(newTable);
				}
				if (newTables != null && !newTables.isEmpty())
				{
					int oldTablesCount = oldTables.size() - 1;
					int newTablesCount = newTables.size() - 1;
					for (int i = 0, j = 0; i <= oldTablesCount; i++, j = 0)
					{
						for (; j <= newTablesCount; j++)
						{
							if (oldTables.get(i).get("tableName").equals("old_" + newTables.get(j).get("tableName")))
							{
								String columnSQL = "";
								for (String column : (List<String>) oldTables.get(i).get("columns"))
								{
									columnSQL += "`" + column + "`,";
								}
								if (columnSQL.equals(""))
									continue;
								columnSQL = columnSQL.substring(0, columnSQL.length() - 1);
								System.out.println("INSERT INTO " + newTables.get(j).get("tableName") + " (" + columnSQL
										+ ") SELECT " + columnSQL + " FROM " + oldTables.get(i).get("tableName"));
								pst = connection.prepareStatement("INSERT INTO " + newTables.get(j).get("tableName")
										+ " (" + columnSQL + ") SELECT " + columnSQL + " FROM "
										+ oldTables.get(i).get("tableName"));
								pst.executeUpdate();
							}
						}
					}
				}
			}

			for (Map<String, Object> oldTable : oldTables)
			{
				pst = connection.prepareStatement("DROP TABLE IF EXISTS " + oldTable.get("tableName"));
				pst.executeUpdate();
			}
			connection.close();
			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
