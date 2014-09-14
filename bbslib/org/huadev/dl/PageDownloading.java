package org.huadev.dl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.huadev.util.*;



public class PageDownloading extends Thread {//

	// private String relativePath = "";

	protected String url = "";

	protected String placeForPageStoring = "";

	protected String saveFile = null;

	protected boolean done = false;

	protected boolean isParentFolder = true;

	protected PageSetDownloadingAndRelativePathsStoring subPageAndCssFilesDownloading = null;

	protected FileSetDownloadingAndRelativePathsStoring pictureAndAudioAndJsFilesDownloading = null;

	protected SubPageAndCssFilesDownloadingLog log = null;

	protected FileDownloading mainPage;
	
	protected int hirerarchy = 0;
	
	public int getHirerarchy() {
		return hirerarchy;
	}

	public void setHirerarchy(int hirerarchy) {
		this.hirerarchy = hirerarchy;
	}

	public boolean isDone() {
		return done;
	}
	
	public SubPageAndCssFilesDownloadingLog getLog(){
		return log;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	public PageDownloading(String url, String placeForPageStoring,
			boolean isParentFolder) {
		
		this.url = url;
		this.isParentFolder = isParentFolder;
		
		this.placeForPageStoring = FileUtil.changeToStandardPath(placeForPageStoring);
		log = new SubPageAndCssFilesDownloadingLog();
	
		//this.start();
	}

	public PageDownloading(String url, String placeForPageStoring,
			boolean isParentFolder,PageDownloading mainPage) {
		this.url = url;
		this.isParentFolder = isParentFolder;
		this.placeForPageStoring = FileUtil.changeToStandardPath(placeForPageStoring);
		this.log = mainPage.getLog();
		this.hirerarchy = mainPage.getHirerarchy()+1;
		if(hirerarchy >= 2 ){
			normalEnd();
			return;
		}

		if (log.isDownloaded(url)) {
			this.saveFile = log.getSavePath(url);
			normalEnd();

		} else {
			//start();
		}

	}
	
	
	void DlPageFromServer()
	{
		
		System.out.println("begin download page:"+url);
		mainPage = new FileDownloading(url, placeForPageStoring,
				isParentFolder);

		saveFile = mainPage.getSaveFile();
		while (saveFile == null 
				&& mainPage.isDone() == false) {
			try {
				sleep(1000);
				saveFile = mainPage.getSaveFile();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}

	public void run() {

		try {

			DlPageFromServer();

			if (saveFile.toLowerCase().endsWith(".htm")
					|| saveFile.toLowerCase().endsWith(".html")
					|| saveFile.toLowerCase().endsWith(".css")) {
			
				downloadSubPageAndCssFiles(saveFile);
				downloadPictureAndAudioAndJsFilesRelatedToThePage(saveFile);

			}

			normalEnd();

		} catch (Exception e) {
			abort();
			e.printStackTrace();
		}
	}

	public void normalEnd() {
		try {
			//System.out.println(121+"PageDownloading");
			log.logSubpageAndCssFilesDownloaded(url, saveFile);
			
			if (mainPage != null) {
				mainPage.abort();
			}
			if (subPageAndCssFilesDownloading != null) {
				subPageAndCssFilesDownloading.abort();
			}
			if (pictureAndAudioAndJsFilesDownloading != null) {
				pictureAndAudioAndJsFilesDownloading.abort();
			}
			System.out.println("Page " + url + "   finished");
			done = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void abort() {
		normalEnd();
	}

	public void downloadSubPageAndCssFiles(String page) {

		File pageFile = new File(page);
		String pageFilePath = pageFile.getAbsolutePath();
		String placeForFrameStoring = pageFilePath.substring(0, pageFilePath
				.lastIndexOf("."))
				+ ".files\\";

		String line = FileUtil.readText(page);

		HashSet subPageAndCssFiles = List_Set_MapUtil.addSetWithoutCopy(PageUtil
				.extractPageInFrames(line), PageUtil.extractCssFiles(line));

		HashMap url_RelativePathMap = new HashMap();

		subPageAndCssFilesDownloading = new PageSetDownloadingAndRelativePathsStoring(
				subPageAndCssFiles, url_RelativePathMap, placeForFrameStoring,this);

		while (subPageAndCssFilesDownloading.isDone() == false) {

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println("PageDownloading 169");
		}

		line = replaceURLWithRelativePath(line, url_RelativePathMap);
		FileUtil.writeToFile(pageFile, line);

	}

	public void downloadPictureAndAudioAndJsFilesRelatedToThePage(String page) {

		try {

			File pageFile = new File(page);
			String pageFilePath = pageFile.getAbsolutePath();
			String placeForPictureAndAudioStoring = pageFilePath.substring(0,
					pageFilePath.lastIndexOf("."))
					+ ".files\\";

			String line = FileUtil.readText(page);
			Iterator originalURLIterator = PageUtil
					.extractPictureAndAudioAndJsFiles(line).iterator();

			HashSet validURLSet = new HashSet();
			while (originalURLIterator.hasNext()) {
				String fileURL = (String) originalURLIterator.next();
				if (fileURL.indexOf("://") != -1) {
					validURLSet.add(fileURL);
				}
			}

			HashMap url_RelativePathMap = new HashMap();

			pictureAndAudioAndJsFilesDownloading = new FileSetDownloadingAndRelativePathsStoring(
					validURLSet, url_RelativePathMap,
					placeForPictureAndAudioStoring);

			while (pictureAndAudioAndJsFilesDownloading.isDone() == false) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			line = replaceURLWithRelativePath(line, url_RelativePathMap);
			
			FileUtil.writeToFile(pageFile, line);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String replaceURLWithRelativePath(String line,
			HashMap url_RelativePathMap) {
		Iterator urlIterator = List_Set_MapUtil.sortSetAccordingToKeyLength(
				url_RelativePathMap.keySet(), List_Set_MapUtil.DESC).iterator();
		while (urlIterator.hasNext()) {

			String url = (String) urlIterator.next();

			String relativePath = (String) url_RelativePathMap.get(url);

			if (relativePath == null || relativePath.trim().equals("")) {
				continue;
			}

			line = line.replace(url, relativePath);
			System.out.println(url + "===[replace With]====>>" + relativePath);

		}
		return line;
	}

	public static void main(String[] args) {
 
		String str = " ";

		
		
		str = "http://www.javaeye.com/images/news/pdf.jpg";

		str = "http://www.windowsmarketplace.com/";
		
		str = "http://pic.news.sohu.com/911195/911297/group-151003.shtml#m=b&g=150724&p=1082776";
		str = "http://bbs.neu-pioneer.cn/images/default/printpost.gif";

		str = "http://hi.baidu.com/dinguangx/blog/item/e2eaa422e9b213a34723e899.html";

		str = "http://pk.arting365.com/view.php?action=ab&gid=30";
		str = "http://www.autohome.com.cn/index.html";

		

		str = "http://www.neu.edu.cn/indexsource/20081007.jpg";

		str = " http://www.neu.edu.cn/index_newsmain.htm";

		
		

		str = "http://photo.bitauto.com/sh2009/beauty.html";
		
		
		
		
		

		// str =
		// "http://www.neujob.com/Pages/Frt/FrontMoreNewsListPage.aspx?PlateId=3";
		// str =
		// "http://www.google.cn/search?complete=1&hl=zh-CN&inlang=zh-CN&newwindow=1&q=url%E7%89%B9%E6%AE%8A%E5%AD%97%E7%AC%A6&meta=&aq=f&oq=";
		//		
		// 
		// 
		// str = "http://www.neu-pioneer.cn/";
		// str = "http://www.javaeye.com/stylesheets/homepage.css?1240325119";
		// str = "http://www.javaeye.com/news";
		// str = "http://202.118.27.146/";
		// str = " http://shenyang.bitauto.com/";
		// str = "http://www.neu.edu.cn/";
		// str = "http://www.arting365.com/";
		// http://www.baa.com.cn/bitauto/index.shtml
		// str =
		// "http://image.bitauto.com/uploadpic/block/2009/313/2009313132941578.gif";
		// 
		str = "http://shenyang.bitauto.com/";
		
		
		
		str = "http://res.mail.qq.com/";
		
		
		
	
		str = "http://202.118.27.146/index.asp";
		
		str = "http://www.javaeye.com/upload/logo/group/49/a89f02b5-54b9-4c41-a8e7-f1332f74c62d.bmp";
		str = "http://www.google.cn/";
		String place = "D:\\Program Files\\Tomcat 6.0\\webapps\\WebsitesConbination";
		
		
		str = "http://www.neujob.com/Pages/Frt/FrontMoreNewsListPage.aspx?PlateId=3";
		
		
		
		str = " http://www.javaeye.com/stylesheets/news.css?1238554427";
		str = "http://news.sohu.com/";
		// http://www.autohome.com.cn/
		place = "C:\\Downloads\\";
		
		str= "http://www.arting365.com/bill/player2/player.html";
		
		str = "http://car.autohome.com.cn/compare/";
		str = "http://www.google.cn/ig/china?hl=zh-CN&gl=cn";
		str = "http://www.cnblogs.com/patrick/archive/2006/11/02/547462.html";
		//str = "http://www.autohome.com.cn/images/log.gif";
		
		
		str = "http://www.arting365.com/";
		str = "http://image.youdao.com/search?q=a&keyfrom=image.top&size=s";
		str = "D:\\";
		str = "http://www.phpv.net/html/1443.html";
		
		str = " http://news.sohu.com/";
	
		str = "http://www.zbintel.cn/product/buy.asp";
		str = "http://www.zbintel.com/demo/index.html";
		str = "http://www.zbintel.cn/product/buy.asp";
		str = "http://item.taobao.com/auction/item_detail.jhtml?item_id=a3102715b30f4cbb92d347d4e8f0a60c&x_id=0db2";
		//str = "";
		str = "http://indu.arting365.com/";
		place ="c:\\Downloads\\html";
		new PageDownloading(str, place, true);
		//		
		// System.out.println(RegexUtil.changeURLToStandardURL("http://www.javaeye.com/",
		// "/upload/logo/group/128/959ddc9f-1df6-4660-9b4a-3f8f9bb8fd35.gif"));
		// 
	}



}
