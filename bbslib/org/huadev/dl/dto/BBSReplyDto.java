package org.huadev.dl.dto;



public class BBSReplyDto
{
	private String m_strTime;
	private String m_strUserName;
	private String m_strUserLink;
	private String m_strUserImg;
	private String m_strContent;
	
	public void SetUserName(String strUserName)
	{
		m_strUserName = strUserName;
	}
	
	public void SetUserLink(String strUserLink)
	{
		m_strUserLink = strUserLink;		
	}
	
	public void SetUserImg(String strUserImg)
	{
		m_strUserImg = strUserImg;
		
	}
	
	public void SetContent(String strContent)
	{
		m_strContent = strContent;
	}
	
	public void SetTime(String strTime)
	{
		m_strTime = strTime;
	}
	
	
	public  String GetTime()
	{
		return m_strTime;
	}
	
	public String GetUserName()
	{
		return m_strUserName;
	}
	
	public String GetUserLink()
	{
		return m_strUserLink;
	}
	
	public String GetContent()
	{
		return m_strContent;
	}
	
	public String GetUserImg()
	{
		return m_strUserImg;
	}
}