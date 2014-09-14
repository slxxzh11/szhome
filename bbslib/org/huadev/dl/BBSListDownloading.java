package org.huadev.dl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.huadev.dl.dto.BBSDetailDTO;
import org.huadev.dl.dto.BBSDetailListDTO;
import org.huadev.dl.dto.BBSReplyDto;
import org.huadev.util.FileUtil;
import org.huadev.util.PageUtil;



public class BBSListDownloading implements Runnable{
	
	List<String> m_listStrBBSHtml;
	HashSet<String> m_setBBSPageNumUrl;
	int m_iMaxPageNum;
	BBSDetailListDTO m_bbsListDto;
	
	
	
	protected String m_strUrl = null;
	protected String m_strPlaceForPageStoring = null;
	protected String m_strSaveFile = null;

	
	
	

	public BBSListDownloading(String strUrl, String strPlaceForPageStoring,int iMaxPageNum) {
	
		m_strUrl = strUrl;
		m_strPlaceForPageStoring = strPlaceForPageStoring;
		m_listStrBBSHtml = new ArrayList<String>();
		m_setBBSPageNumUrl = new HashSet<String>();
		m_iMaxPageNum = iMaxPageNum;
		m_bbsListDto = new BBSDetailListDTO();
	}
	
	
	

	
	public void DownloadUrl(String strUrl)
	{
	
		FileDownloading dl = new FileDownloading(strUrl);	
		String strContent = dl.GetContent();
		
		
		String strBBSHtml = GetBBSList(strContent);
		String strPageNumHtml = GetPageNum(strContent);
		m_setBBSPageNumUrl.add(strUrl);	
		m_listStrBBSHtml.add(strBBSHtml);	
		System.out.println(strUrl);
		
		HashSet<String> setBBSPageNumTemp = new HashSet<String>();		
		PageUtil.extractAllURLS(strPageNumHtml,setBBSPageNumTemp);
		
		Iterator<String> it = setBBSPageNumTemp.iterator();
		
		while (it.hasNext()) {
			
			String strUrlTemp = it.next();	
			if(m_setBBSPageNumUrl.size() > m_iMaxPageNum)
			{
				break;
			}
			
			if(!m_setBBSPageNumUrl.contains(strUrlTemp)){
				DownloadUrl(strUrlTemp);				
			}			
		}
	}
	
	void DlPageFromServer()
	{
		
		DownloadUrl(m_strUrl);	

		m_strSaveFile = m_strPlaceForPageStoring;
		m_strSaveFile = FileUtil.changeToStandardPath(m_strPlaceForPageStoring)+"/index.html";		
	
		FileUtil.writeToFile(m_strSaveFile, m_listStrBBSHtml);
		
		SimplifyHtml(m_strSaveFile);
		
		String strHtml = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
		strHtml += m_bbsListDto.CreateHtml();
		
		FileUtil.writeToFile(m_strSaveFile,strHtml);
		
		DownloadDetails(m_strPlaceForPageStoring);
	}
	
	
	void DownloadDetails(String strFolder)
	{
		m_bbsListDto.DownloadDetails(strFolder);		
	}
	
	
	void SimplifyHtml(String strPath)
	{
		

		String strHtml = FileUtil.readText(strPath);
		
		
		int iPreFound = 0;
		
		
		int iDebugTest = 0;
		do
		{
			int iBegin = strHtml.indexOf("<tr>",iPreFound);
			int iEnd = strHtml.indexOf("</tr>",iBegin);
			
			if(iBegin >= 0 && iEnd >= 0)
			{
				iPreFound = iEnd;
				String strOneNews = strHtml.substring(iBegin,iEnd);

				String strTrLink = "<td(.*?)>(.*?)</td>(.*?)<td(.*?)>(.*?)</td>(.*?)<td(.*?)>(.*?)</td>(.*?)<td(.*?)>(.*?)</td>(.*?)<td(.*?)>(.*?)</td>(.*?)";
				Pattern p = Pattern.compile(strTrLink, Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

				Matcher m = p.matcher(strOneNews);

				if (m.find()) {
					
					String strTitle = m.group(2);
					String[] strTitleHref = PageUtil.extractOneHref(strTitle);
					
					if(strTitleHref != null)
					{
						System.out.println(strTitleHref[1]);
					}
					String strName  = m.group(8);
					String[] strNameHref = PageUtil.extractOneHref(strName);
					if(strNameHref != null)
					{
						System.out.println(strNameHref[1]);
					}
					
					String strReivewCount = m.group(11);
					String strTime = m.group(14);
					
					if(iDebugTest < 10)
					{
					     m_bbsListDto.AddBBSRecord(strTitleHref[0],strTitleHref[1],strName, strReivewCount, strTime);
					}
					iDebugTest++;
					
				}
				

			}
			else
			{
				break;
			}
			
		}
		while(true);
		
	}

	
	String GetPageNum(String strContent)	
	{
		String strResult = null;
		int iPos = strContent.indexOf(" id=\"AspNetPager2\"");
		
		int iEndPos = strContent.indexOf("class=\"re_write",iPos);
		
		if(iPos >= 0 && iEndPos > iPos)
		{
		    strResult = strContent.substring(iPos, iEndPos);
		}
		
		return strResult;		
	}
	
	
	
	String GetBBSList(String strContent)
	{

		String strResult = null;
		int iPos = strContent.indexOf(" id=\"DivADPlace3");
		
		int iEndPos = strContent.indexOf("class=\"page",iPos);
		
		if(iPos >= 0 && iEndPos > iPos)
		{
		    strResult = strContent.substring(iPos, iEndPos);
		    int iPos1 = strResult.indexOf("<tbody");
		    int iEndPos1 = strResult.lastIndexOf("</tbody");
		    
		    if(iPos1 >= 0 && iEndPos1 > iPos1)
		    {
		    	strResult = strResult.substring(iPos1,iEndPos1);
		    	
		    }
		    
		}
		
		return strResult;		
	}
	  

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DlPageFromServer();
	}
	
	

	public static void main(String[]args)
	{
	
		String strUrl = "http://bbs.szhome.com/0.html";
		String strPlace ="c:\\Downloads\\html";
		BBSListDownloading bbs = new BBSListDownloading(strUrl,strPlace,1);
		bbs.run();
	}






}
