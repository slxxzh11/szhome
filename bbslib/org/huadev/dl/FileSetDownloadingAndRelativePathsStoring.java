package org.huadev.dl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.huadev.util.List_Set_MapUtil;


public class FileSetDownloadingAndRelativePathsStoring extends Thread {

	private HashSet urls;

	private HashMap url_RelativePathMap;

	private String placeForStoring;

	private boolean done = false;

	private ArrayList cpuList = new ArrayList();
	
	private HashMap redownloadCount = new HashMap();

	public boolean isDone() {
		return done;
	}

	public FileSetDownloadingAndRelativePathsStoring(HashSet urls,
			HashMap url_RelativePathMap, String placeForStoring) {


		this.urls = urls;
		this.url_RelativePathMap = url_RelativePathMap;
		this.placeForStoring = placeForStoring;

		start();
	}

	public void run() {

		try {

			int urlsStoringMapPreviousSize = 0;
			int appearTimes = 0;
			int realSize = 0;

			downloadingRemainFiles();

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
					showRemainFiles();

				}

				if (appearTimes > 60) {
					downloadingRemainFiles();
					appearTimes = 0;
				}

				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			normalEnd();

		} catch (Exception e) {
			abort();
			e.printStackTrace();
		}

	}

	private void downloadingRemainFiles() {

		ArrayList remainder = List_Set_MapUtil.minusSet(urls, url_RelativePathMap
				.keySet());


		Iterator remainderIterator = remainder.iterator();
		System.out.println("FileSetDownloading 94:the number of all Files "
				+ urls.size());
		while (remainderIterator.hasNext()) {

			String urlNotStoreInMap = (String) remainderIterator.next();
			
			if(redownloadCount.get(urlNotStoreInMap)==null){
				redownloadCount.put(urlNotStoreInMap, "+");
			}else{
				String count = redownloadCount.get(urlNotStoreInMap)+"+";
				redownloadCount.put(urlNotStoreInMap, count);
				if(count.length()>=4){
					url_RelativePathMap.put(urlNotStoreInMap, "");
					System.out.println("this file take too much time,downloading is canceled");
					continue;
				}
				if(count.length()>2){
					System.out.println("there are two thread downloading :" + urlNotStoreInMap);
					continue;					
				}				
				
				
			}
			//System.out.println("download File again:" + urlNotStoreInMap);
			FileDownloadingAndRelativePathStoring cpu = new FileDownloadingAndRelativePathStoring(
					urlNotStoreInMap, url_RelativePathMap, placeForStoring);

			cpuList.add(cpu);

		}

	}

	private void showRemainFiles() {

		ArrayList remainder = List_Set_MapUtil.minusSet(urls, url_RelativePathMap
				.keySet());

		Iterator remainderIterator = remainder.iterator();
		System.out.println("\n\n\n\n\n");
		int count = 0;
		while (remainderIterator.hasNext()) {

			String urlNotStoreInMap = (String) remainderIterator.next();
			//System.out.println("File not finished:" + urlNotStoreInMap);
			count++;

		}

		System.out.println("Files not finished Count:" + count);

	}

	private void normalEnd() {

		try {
			if(done==true){
				return;
			}
			Iterator cpuListIterator = cpuList.iterator();

			while (cpuListIterator.hasNext()) {

				FileDownloadingAndRelativePathStoring temp = (FileDownloadingAndRelativePathStoring) cpuListIterator
						.next();
				String fileURL = temp.getFileURL();
				if (url_RelativePathMap.get(fileURL) == null) {
					url_RelativePathMap.put(fileURL, " ");
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

	public static void main(String[] args) {
		String str = "    ";
		HashSet urls = new HashSet();
		urls.add("http://202.118.27.146/images/03.jpg");
		urls.add("http://202.118.27.146/images/news.swf");
		HashMap map = new HashMap();
		FileSetDownloadingAndRelativePathsStoring a = new FileSetDownloadingAndRelativePathsStoring(
				urls, map, "D:\\22");
		if (a.isDone()) {
//			_.p(map);
		}

	}

}
