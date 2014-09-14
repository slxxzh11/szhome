package org.huadev.dl.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BBSDetailDTO {
	
	private String m_strSubject;
	private String m_strUserName;
	private String m_strHref;
	private String m_strReviewCount;
	private String m_strTime;
	private String m_strSaveFile;
	
	
	private List<BBSReplyDto> m_listReply;
	
	public void setSaveFile(String strSaveFile)
	{
		m_strSaveFile = strSaveFile;
	}
	
	public String getSaveFile()
	{
		return m_strSaveFile;
	}
	
	
	public BBSDetailDTO()
	{
		m_listReply = new LinkedList<BBSReplyDto>();		
	}
	
	public String  getHref()
	{
		return m_strHref;
	}
	
	
	public void setSubject(String strSubject)
	{
		m_strSubject = strSubject;
	}
	
	
	
	public void AddBBSRecord(String strTitle,String strHref,String strUserName,String strReviewCount,String strTime)
    {
		
    	m_strHref = strHref;
    	m_strSubject = strTitle;
    	m_strUserName = strUserName;
    	m_strTime = strTime;
    	m_strReviewCount = strReviewCount;
    	
    	
    }
	
	
	
	
	public void AddReplyDto(BBSReplyDto dto)
	{
		if(dto != null)
		{
			m_listReply.add(dto);			
		}	
		
	}
	
	public String CreateBiefHtml()
	{
		String strHtml = "";
		strHtml += "<tr>";
		strHtml += "<td>";
		strHtml += "<div>";
		strHtml += "<div class=\"leftIcon\">";
		strHtml += "<a href=\"";
		strHtml += m_strHref;
		strHtml += "\">";
		strHtml += "<img src=\"\">";
		strHtml += "</a>";
		strHtml += "</div>";
		strHtml += "<div class=\"rightContent\">";
		strHtml += "<ul>";
		strHtml += "<li class=\"name\">";
		strHtml += "<span class=\"leftName\"><a href=\"";
		strHtml += m_strHref;
		strHtml += "\">";
		strHtml += m_strUserName;
		strHtml += "</a><span>";
		strHtml += "<span class=\"rightTime\">2013-04-01</span>";
		strHtml += "</li>";
		strHtml += "<li class=\"content\">";
		strHtml += "<div class=\"title\">";
		
		strHtml += m_strSubject;
		strHtml += "</div>";
		strHtml += "<div class=\"content\">aaaaaa</div>";
		strHtml += "<div class=\"imgs\">";
		strHtml += "<img src=\"1.jpg\"/>";
		strHtml += "</div>";
		strHtml += "</li>";

		strHtml += "<li>";
		strHtml += "<div class=\"Reply\">";
		strHtml += "<span class=\"replyname\">我</span>:";
		strHtml += "<span class=\"replycontent\">你是平江的？</span>";
		strHtml += "</div>";
		strHtml += "</li>";

		strHtml += "</ul>";
		strHtml += "</td>";
		strHtml += "</tr>";
		return strHtml;
	}
	
	public String CreateDetailHtml()
	{
		String strHtml = "\r\n";
		strHtml += "<table>\r\n";
		strHtml += "<tr>\r\n";
		strHtml += "<td>\r\n";
		strHtml += "<div class=\"bbstitle\">\r\n";
		strHtml += m_strSubject;
		strHtml += "</div>\r\n";
		strHtml += "</td>\r\n";
		strHtml += "</tr>\r\n";
		
		Iterator<BBSReplyDto> it = m_listReply.iterator();
		int i = 0;
		while(it.hasNext())
		{
			BBSReplyDto dto = it.next();
			strHtml += "<tr>\r\n";
			strHtml += "<td>\r\n";
			strHtml += "<div>\r\n";
			strHtml += "	<div class=\"bbs\">\r\n";
			strHtml += "	<ul>\r\n";
			strHtml += "	<li>\r\n";
			strHtml += "	<div class=\"authorImg\">\r\n";
			strHtml += "	    <a href=\"\r\n";
			strHtml += dto.GetUserLink();
			strHtml += "\" target=\"_blank\" >\r\n";
			strHtml += "							<img src=\"\r\n";
			strHtml += dto.GetUserImg();
			strHtml += "\" ></a>\r\n";
			strHtml += "	</div>\r\n";
			strHtml += "	</li>\r\n";
			strHtml += "	<li>\r\n";
			strHtml += "	<div class=\"authortext\">\r\n";
			strHtml += "	  <div class=\"author\"><a href=\"\r\n";
			strHtml += dto.GetUserLink();
			strHtml += "\">\r\n";
			strHtml += dto.GetUserName();
			strHtml += "</a></div>\r\n";
			strHtml += "	  <div class=\"time\">\r\n";
			strHtml += dto.GetTime();
			strHtml += "</div>\r\n";
			strHtml += "	<div>\r\n";
			strHtml += "	</li>\r\n";
			strHtml += "	<li>\r\n";
			strHtml += "	   <div class=\"floor\"> \r\n";
			if(i == 0)
			{
				strHtml += "楼\r\n";
				
			}
			else
			{
				strHtml += i;
				strHtml += "楼\r\n";
			}
			
			strHtml += "</div>\r\n";
			strHtml += "	</li>\r\n";
			strHtml += "	</ul>\r\n";
		
			strHtml += "	</div>\r\n";
			strHtml += "	\r\n";
			strHtml += "	<div class=\"content\">\r\n";
			strHtml += dto.GetContent();
		
			strHtml += "	</div>\r\n";
		
			strHtml += "</div>\r\n";
			strHtml += "</td>\r\n";
			
			strHtml += "</tr>\r\n";
			
		}
	

		
		strHtml += "</table>\r\n";
		return strHtml;
	}
	
	
	
	

}

