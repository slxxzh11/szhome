package org.huadev.dl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.huadev.dl.dto.BBSDetailDTO;
import org.huadev.dl.dto.BBSReplyDto;
import org.huadev.util.FileUtil;
import org.huadev.util.PageUtil;



public class BBSDownloading  implements Runnable{
	
	List<String> m_listStrBBSHtml;
	HashSet<String> m_setBBSPageNumUrl;
	
	BBSDetailDTO m_detailDto;
	protected String m_strPlaceForPageStoring = "";
	protected String m_strSaveFile = null;
	protected boolean m_bIsParentFolder = true;

	

	public BBSDownloading(BBSDetailDTO detailDto, String strPlaceForPageStoring,
			boolean bIsParentFolder) {	
		
		m_detailDto = detailDto;
		m_strPlaceForPageStoring = strPlaceForPageStoring;
		m_bIsParentFolder = bIsParentFolder;
		m_listStrBBSHtml = new ArrayList<String>();
		m_setBBSPageNumUrl = new HashSet<String>();
	
		
	}
	
	
	@Override
	public void run() {
		DlPageFromServer();				
	}
	
	
	
	String GetSaveFile()
	{
		return m_strSaveFile;
	}
	
	
	public void DownloadUrl(String strUrl)
	{
	
		FileDownloading dl = new FileDownloading(strUrl);	
		String strContent = dl.GetContent();
		
		
		String strBBSHtml = GetBBS(strContent);
		String strPageNumHtml = GetPageNum(strContent);
		
		m_setBBSPageNumUrl.add(strUrl);
		m_listStrBBSHtml.add(strBBSHtml);	
		HashSet<String> setBBSPageNumTemp = new HashSet<String>();		
		PageUtil.extractAllURLS(strPageNumHtml,setBBSPageNumTemp);
		
		Iterator<String> it = setBBSPageNumTemp.iterator();
		
		while (it.hasNext()) {
			
			String strUrlTemp = it.next();			
			if(!m_setBBSPageNumUrl.contains(strUrlTemp)){
				DownloadUrl(strUrlTemp);				
			}			
		}
	}
	
	void DlPageFromServer()
	{
		
		DownloadUrl(m_detailDto.getHref());	

		m_strSaveFile = m_strPlaceForPageStoring;
		if(m_bIsParentFolder)
		{
			m_strSaveFile = FileUtil.getUniqueFilePath(m_strPlaceForPageStoring,"html");			
		}
	
		FileUtil.appendStringToFile(m_strSaveFile, m_listStrBBSHtml);
		
		SimplifyHtml(m_strSaveFile);
	}
	
	void SimplifyHtml(String strPath)
	{
		String strHtml = FileUtil.readText(strPath);
		
		
		String[] strPieces = strHtml.split("class=\"userInfo\"");
		
		//第一个里面没有信息，不需要
		for(int i = 1; i < strPieces.length; i++)
		{
			String strTemp = strPieces[i];
			BBSReplyDto dto = new BBSReplyDto();
			
			int iUserNameBegin = strTemp.indexOf("class=\"userName");
			int iUserNameEnd = strTemp.indexOf("class=\"head140",iUserNameBegin);
			
			String strUserName = strTemp.substring(iUserNameBegin, iUserNameEnd);
			
			String[] strUserLink = PageUtil.extractOneHref(strUserName);		
		
			if(strUserLink != null)
			{
				/*
				int iHrefPos = strUserName.indexOf(strUserLink);
				int iNameTextBegin = strUserName.indexOf(">",iHrefPos);
				int iNameTextEnd = strUserName.indexOf("<",iNameTextBegin);
				
				String strUserNameText = strUserName.substring(iNameTextBegin+1,iNameTextEnd);
				*/
				dto.SetUserName(strUserLink[0]);
				dto.SetUserLink(strUserLink[1]);
			}
			
			
			
			
			int iUserImgBegin = strTemp.indexOf("<img",iUserNameEnd); 
			int iUserImgEnd = strTemp.indexOf("class=\"identify",iUserImgBegin); 
			String strUserImg = strTemp.substring(iUserImgBegin,iUserImgEnd);
			String strImgUrl = PageUtil.extractOneUrl(strUserImg);
			
			
			dto.SetUserImg(strImgUrl);
			
			
			
			
			int iWriteTimeBegin= strTemp.indexOf("class=\"spanWriteTime"); 
			iWriteTimeBegin = strTemp.indexOf(">",iWriteTimeBegin); 
			int iWriteTimeEnd= strTemp.indexOf("</span");
			
			String strWriteTime = strTemp.substring(iWriteTimeBegin+1, iWriteTimeEnd);
			
			dto.SetTime(strWriteTime);
			
			if(i == 1)
			{
				int iSubjectBegin = strTemp.indexOf("id=\"Subject");
				iSubjectBegin = strTemp.indexOf(">",iSubjectBegin);
				int iSubjectEnd = strTemp.indexOf("</",iSubjectBegin);
				String strSubjectTitle = strTemp.substring(iSubjectBegin+1,iSubjectEnd);	
				
				m_detailDto.setSubject(strSubjectTitle);
				
			}
			
			int iContentBegin= strTemp.indexOf("class=\"tzContent");
			iContentBegin = strTemp.indexOf(">",iContentBegin);			
			int iContentEnd= strTemp.indexOf("</div",iContentBegin);
			
			String strContent = strTemp.substring(iContentBegin+1,iContentEnd);
			dto.SetContent(strContent);
			m_detailDto.AddReplyDto(dto);
			
		}
		
		String strHtmlSimple = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
		strHtmlSimple +=  m_detailDto.CreateDetailHtml();
		FileUtil.writeToFile(strPath,strHtmlSimple,"UTF-8");
		m_detailDto.setSaveFile(strPath);
		
		
	}

	
	String GetPageNum(String strContent)	
	{
		String strResult = null;
		int iPos = strContent.indexOf(" class=\"pageNum");
		
		int iEndPos = strContent.indexOf("class=\"re_write",iPos);
		
		if(iPos >= 0 && iEndPos > iPos)
		{
		    strResult = strContent.substring(iPos, iEndPos);
		}
		
		return strResult;		
	}
	
	
	
	String GetBBS(String strContent)
	{

		String strResult = null;
		int iPos = strContent.indexOf(" class=\"main");
		
		int iEndPos = strContent.indexOf("class=\"nxck",iPos);
		
		if(iPos >= 0 && iEndPos > iPos)
		{
		    strResult = strContent.substring(iPos, iEndPos);
		    int iPos1 = strResult.indexOf("<div");
		    int iEndPos1 = strResult.lastIndexOf("</div");
		    
		    if(iPos1 >= 0 && iEndPos1 > iPos1)
		    {
		    	strResult = strResult.substring(iPos1,iEndPos1);
		    	
		    }
		    
		}
		
		return strResult;		
	}
	  
	
	public static void main(String[]args)
	{
		
		String strUrl = "http://bbs.szhome.com/commentdetail.aspx?id=160894631&projectid=330010";
		String strPlace ="c:\\Downloads\\html";
		//BBSDownloading bbs = new BBSDownloading(strUrl,strPlace,true);
	}


	

}
