package org.huadev.dl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.huadev.util.List_Set_MapUtil;


public class PageSetDownloadingAndRelativePathsStoring extends Thread {//
	private HashSet urls;
	
	private HashMap url_RelativePathMap = null;

	private String placeForStoring;
	
	private PageDownloading mainPage;

	private boolean done = false;

	private ArrayList cpuList = new ArrayList();
	private HashMap redownloadCount = new HashMap();

	public boolean isDone() {
		return done;
	}

	public PageSetDownloadingAndRelativePathsStoring(HashSet urls,
			HashMap url_RelativePathMap, String placeForStoring,PageDownloading mainPage) {

		this.urls = urls;
		this.url_RelativePathMap = url_RelativePathMap;
		this.placeForStoring = placeForStoring;
		this.mainPage = mainPage;

		start();
	}

	public void run() {

		try {

			int urlsStoringMapPreviousSize = 0;
			int appearTimes = 0;
			downloadingRemainPages();
			int realSize = 0;

			while (urls.size() > realSize) {

				realSize = List_Set_MapUtil
						.getRealSizeOfKeySetDespiteOfConcurrency(url_RelativePathMap);

				if (urlsStoringMapPreviousSize == realSize) {
					appearTimes++;
				} else {
					urlsStoringMapPreviousSize = realSize;
					appearTimes = 0;
				}

				if (appearTimes % 15 == 0) {
					showRemainPages();

				}
				if (appearTimes > 60) {
					downloadingRemainPages();
					appearTimes = 0;
				}

				try {
					sleep(2000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

			normalEnd();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void downloadingRemainPages() {

		ArrayList remainder = List_Set_MapUtil.minusSet(urls, url_RelativePathMap
				.keySet());
		Iterator remainderIterator = remainder.iterator();

		while (remainderIterator.hasNext()) {

			String urlNotStoreInMap = (String) remainderIterator.next();
			System.out.println("download Page again:" + urlNotStoreInMap);
			if(redownloadCount.get(urlNotStoreInMap)==null){
				redownloadCount.put(urlNotStoreInMap, "+");
			}else{
				String count = redownloadCount.get(urlNotStoreInMap)+"+";
				redownloadCount.put(urlNotStoreInMap, count);
				if(count.length()>=4){
					url_RelativePathMap.put(urlNotStoreInMap, " ");
					System.out.println("this file take too much time,Page downloading is canceled");
					continue;
				}
				if(count.length()>2){
					System.out.println("there are two thread downloading  page:" + urlNotStoreInMap);
					continue;					
				}				
				
				
			}
			PageDownloadingAndRelativePathStoring cpu = new PageDownloadingAndRelativePathStoring(
					urlNotStoreInMap, url_RelativePathMap, mainPage);
			cpuList.add(cpu);

		}

	}

	private void showRemainPages() {

		ArrayList remainder = List_Set_MapUtil.minusSet(urls, url_RelativePathMap
				.keySet());
		System.out.println(this.getName() + "\n" + remainder);

		Iterator remainderIterator = remainder.iterator();
		System.out.println("\n\n\n");
		int count = 0;

		while (remainderIterator.hasNext()) {

			String urlNotStoreInMap = (String) remainderIterator.next();
			System.out.println("page not finished:" + urlNotStoreInMap);
			count++;
		}

		System.out.println("subpages not finished Count:" + count);

	}

	private void normalEnd() {
		try {
			if(done==true){
				return;
			}
			Iterator cpuListIterator = cpuList.iterator();

			String mainPagePath = mainPage.getSaveFile();
			while (cpuListIterator.hasNext()) {
				PageDownloadingAndRelativePathStoring temp = (PageDownloadingAndRelativePathStoring) cpuListIterator
						.next();
				String fileURL = temp.getFileURL();
				
				if (url_RelativePathMap.get(fileURL) == null) {
					url_RelativePathMap.put(fileURL, "");
				}
				temp.abort();

			}

			done = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void abort() {
		normalEnd();
		urls = null;
		url_RelativePathMap = null;
		placeForStoring = null;
		cpuList = null;

	}

}
