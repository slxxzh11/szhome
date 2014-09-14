package org.huadev.dl;

import java.util.HashMap;
import java.util.HashSet;

public class SubPageAndCssFilesDownloadingLog{
	
	private HashMap subPageAndCssFiles = new HashMap();
	
	public boolean isDownloaded(String url){
		
		return subPageAndCssFiles.containsKey(url);
	
	}
	
	public String getSavePath(String url){
		return (String)subPageAndCssFiles.get(url);
		
	}
	
	public void logSubpageAndCssFilesDownloaded(String url ,String absolutePath){
		if(subPageAndCssFiles.containsKey(url)){
			System.out.println("this url has been downloaded");
		}else{
		    subPageAndCssFiles.put(url,absolutePath);
		}		
	}
	

}
