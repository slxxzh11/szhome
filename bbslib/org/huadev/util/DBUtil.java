package org.huadev.util;

import java.sql.*;




public class DBUtil {
	String m_strDBUrl;
	String m_strUserName;
	String m_strPassword;
	Connection m_conn;
	Statement m_stmt;
	public static final String s_breakerObject = "\tobjectBreaker\t";
	public static final String s_breakerLine = "\tlineBreaker\t";

	public  DBUtil(String strDBUrl, String strUserName, String strPassword) {
		// "jdbc:mysql://localhost:3306/phppal_app"
		if (strDBUrl.indexOf("/") == -1) {
			strDBUrl = "jdbc:mysql://localhost:3306/" + strDBUrl;
		}
		m_strDBUrl = strDBUrl;
		m_strUserName = strUserName;
		m_strPassword = strPassword;

		initConnection();

	}
	
	public boolean excute(String strExeSql) 
	{
		boolean bFlag = false;
		
		try {
			bFlag = m_stmt.execute(strExeSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bFlag;
	}

	
	public void excuteQuery(String strQuerySql,DBUtilProcesser objProcesser) 
	{
		
		try 
		{
			ResultSet rs = m_stmt.executeQuery(strQuerySql);
	
			
			ResultSetMetaData md = rs.getMetaData();
			int columnNum =  md.getColumnCount(); 
			

	
			while(rs.next())
			{
				String strOneLineInDB = "";
				for (int i=0; i< columnNum; i++)
				{ 
		              Object val= rs.getObject(md.getColumnName(i+1));
		              strOneLineInDB += val+s_breakerObject;		                       
		         } 
				objProcesser.processOneDataLine(strOneLineInDB);
			
				
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	public String excuteQuery(String strQuerySql) 
	{
		String strResult = "";
		try 
		{
			ResultSet rs = m_stmt.executeQuery(strQuerySql);
	
			
			ResultSetMetaData md = rs.getMetaData();
			int columnNum =  md.getColumnCount(); 
			

	
			while(rs.next())
			{
				for (int i=0; i< columnNum; i++)
				{ 
		              Object val= rs.getObject(md.getColumnName(i+1));
		              strResult += val+s_breakerObject;		                       
		         } 
				strResult += s_breakerLine;
				
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strResult;

	}

	private void initConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// 建立到MySQL的连接
			m_conn = DriverManager.getConnection(m_strDBUrl, m_strUserName,
					m_strPassword);
			// 执行SQL语句
			m_stmt = m_conn.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void CloseConnection() {
		try {
			if(!m_stmt.isClosed())
			{
				m_stmt.close();				
			}
			
			if(!m_conn.isClosed())
			{
				m_conn.close();				
			}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void finalize( )
	{
		CloseConnection();
	}



}
