package org.huadev.dl.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.huadev.dl.BBSDownloading;
import org.huadev.util.TaskList;

public class BBSDetailListDTO {

	private List<BBSDetailDTO> m_listBBS;
	
	
	
	public BBSDetailListDTO()
	{
		m_listBBS = new LinkedList<BBSDetailDTO>();
	}

	
	
	public void AddBBSRecord(String strTitle,String strHref,String strUserName,String strReviewCount,String strTime)
	{
		BBSDetailDTO bbsDetailDTO = new BBSDetailDTO();
		bbsDetailDTO.AddBBSRecord(strTitle, strHref, strUserName, strReviewCount, strTime);
		m_listBBS.add(bbsDetailDTO);
	}
	
	public String CreateHtml()
	{
		String strHtml = "";
		
		if(m_listBBS.size() > 0)
		{
			strHtml += "<table>";
			Iterator<BBSDetailDTO> it = m_listBBS.iterator();
			while(it.hasNext())
			{
				BBSDetailDTO dto = it.next();
				strHtml += dto.CreateBiefHtml();
			}

			strHtml += "</table>";
			
		}
		
		
		return strHtml;
	}
	
	
	public void DownloadDetails(String strDir)
	{
		TaskList taskList = new TaskList(5);
		Iterator<BBSDetailDTO> it =  m_listBBS.iterator();
		while(it.hasNext())
		{
			BBSDownloading bbsDl = new BBSDownloading(it.next(),strDir,true);
			taskList.AddTask(bbsDl);
		}
		
		taskList.run();
		
	}
	
	

}

