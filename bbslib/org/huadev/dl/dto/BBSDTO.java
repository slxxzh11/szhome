package org.huadev.dl.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BBSDTO {
	
	private String m_strSubject;
	
	
	private List<BBSReplyDto> m_listReply;
	
	
	public BBSDTO()
	{
		m_listReply = new ArrayList<BBSReplyDto>();		
	}
	
	
	public void setSubject(String strSubject)
	{
		m_strSubject = strSubject;
	}
	
	
	public void AddReplyDto(BBSReplyDto dto)
	{
		if(dto != null)
		{
			m_listReply.add(dto);			
		}	
		
	}
	
	
	public String CreateHtml()
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

