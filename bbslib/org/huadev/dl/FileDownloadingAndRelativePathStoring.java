package org.huadev.dl;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;






public class FileDownloadingAndRelativePathStoring extends Thread {

	private String fileURL;

	private String placeForStoring;

	private HashMap map;

	private ArrayList cpuList = new ArrayList();

	private boolean done = false;

	public String getFileURL() {
		return fileURL;
	}

	public boolean isDone() {
		return done;
	}

	public FileDownloadingAndRelativePathStoring(String fileURL, HashMap map,
			String placeForStoring) {
		this.fileURL = fileURL;
		this.placeForStoring = placeForStoring;
		this.map = map;

		start();
	}

	public void run() {

		try {

			FileDownloading d = new FileDownloading(fileURL, placeForStoring,
					true);
			cpuList.add(d);

			while (d.getSaveFile() == null && d.isDone() == false) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			if (d.getSaveFile() == null) {
				d = new FileDownloading(fileURL, placeForStoring, true);
				cpuList.add(d);
				while (d.getSaveFile() == null && d.isDone() == false) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (d.getSaveFile() == null || d.getSaveFile().trim().equals("")) {
				map.put(fileURL, " ");
			} else {
				String saveFile = d.getSaveFile();
				String relativePath = new File(placeForStoring).getName() + "/"
						+ new File(saveFile).getName();
				map.put(fileURL, relativePath);

			}

			normalEnd();
		} catch (Exception e) {
			abort();
		}

	}

	public void normalEnd() {

		try {
			if(done==true){
				return;
			}
			if (cpuList != null) {
				Iterator cpuListIterator = cpuList.iterator();

				while (cpuListIterator.hasNext()) {
					FileDownloading temp = (FileDownloading) cpuListIterator
							.next();
					temp.abort();
				}

			}

		
			if (map.get(fileURL) == null) {
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
		placeForStoring = null;
		map = null;
		
		cpuList = null;

	}



}
