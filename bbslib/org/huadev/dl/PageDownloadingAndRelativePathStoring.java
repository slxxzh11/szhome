package org.huadev.dl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.huadev.util.FileUtil;





public class PageDownloadingAndRelativePathStoring extends Thread {//
	private String fileURL;

	private HashMap map;

	private String placeForStoring;
	
	private PageDownloading mainPage;
	
	private ArrayList cpuList = new ArrayList();

	private boolean done = false;
	
	
	
	

	public boolean isDone() {
		return done;
	}



	 public String getFileURL() {
		return fileURL;
	}

	public PageDownloadingAndRelativePathStoring(String fileURL, HashMap map,
			PageDownloading mainPage) {
		this.fileURL = fileURL;
		this.mainPage = mainPage;
		String mainPagePath = mainPage.getSaveFile();
		this.placeForStoring = mainPagePath.substring(0, mainPagePath
				.lastIndexOf("."))
				+ ".files\\";
		this.map = map;
		start();

	}

	public void run() {

		try {
			PageDownloading d = new PageDownloading(fileURL, placeForStoring,
					true,mainPage);
			cpuList.add(d);
			while (d.isDone() == false) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			if (d.getSaveFile() == null) {
				d = new PageDownloading(fileURL, placeForStoring, true,mainPage);
				cpuList.add(d);
				while (d.isDone() == false) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (d.getSaveFile() == null) {
				map.put(fileURL, "");
			} else {
				String saveFile = d.getSaveFile();
				String relativePath = FileUtil.getRelativePath(mainPage.getSaveFile(), saveFile);
				map.put(fileURL, relativePath);
			}

			normalEnd();
		} catch (Exception e) {
			abort();
			e.printStackTrace();
		}

	}

	private void normalEnd() {

		try {
			if(done==true){
				return;
			}
			Iterator cpuListIterator = cpuList.iterator();

			while (cpuListIterator.hasNext()) {
				PageDownloading temp = (PageDownloading) cpuListIterator.next();
				temp.abort();

			}

			if (map!=null&&map.get(fileURL) == null) {
				map.put(fileURL, "");
			}

			done = true;

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void abort() {

		normalEnd();
		fileURL = null;
		map = null;
		placeForStoring = null;

	}

}
