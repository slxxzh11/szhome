package org.huadev.dl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.huadev.util.*;
public class FileDownloading extends Thread {

	private String url;

	private InputStream in;

	private Socket socket;

	private PrintWriter out;

	private String header = "";

	private String contentType = "";// the content like "Content-Type:

	private boolean done = false;

	private String saveFile = null;

	private HashSet redirectionURLs = null;

	private boolean isParentFolder = false;



	public String getSaveFile() {

		if (done == false) {
			return null;
		} else {
			return saveFile;
		}
	}

	public FileDownloading(String url, String saveFile, boolean isParentFolder) {

		this.url = url;
		this.isParentFolder = isParentFolder;
		this.saveFile = saveFile;
		start();

	}
	
	
	public FileDownloading(String url)
	{
		this.url = url;      
	}
	
	public String GetContent()
	{
		
		Content strResult = null;
		String strContent = null;
		try {
			strResult = Request.Get(url)
			            .execute().returnContent();
			
			strContent = strResult.asString();
			strContent = PageUtil.ChangeUrlInContentToStandardURL(strContent, url);
			  
		} catch (Exception e) {
			e.printStackTrace();		
		}
		     
		 return strContent;
	}

	public void run() {

		try {
			if (isParentFolder) {
				
				createNewFileInFolder(url, saveFile);
			}else{
			
				FileUtil.createFileByForce(saveFile);
			}

			if (this.saveFile == null) {
				throw new Exception("FileDownloading 72:abort");
			}

			open();

			writeBodyToFile();
			normalEnd();
			System.out.println("FileDownloading 80:  " + this.url + "-->"
					+ this.saveFile + " completed");
		} catch (Exception e) {

			try {
				if (isParentFolder) {
					createNewFileInFolder(url, saveFile);
				}

				if (this.saveFile == null) {
					throw new Exception("FileDownloading 72:abort");
				}

				open();

				writeBodyToFile();
				normalEnd();
//				System.out.println("FileDownloading 80:  " + this.url + "-->"
//						+ this.saveFile + " completed");
			} catch (Exception e1) {

				System.out.println(this.url
						+ "-------------------------------- Error");
				e.printStackTrace();
				abort();
			}
		}

	}

	private void createNewFileInFolder(String url, String Folder)
			throws Exception {

		if (url == null || url.trim().equals("") || url.trim().length() < 6
				|| url.indexOf("://") == -1) {
			System.out.println(this.getClass()+"============"+url);
			throw new Exception("FileDownloading 110:abort");
		}

		if (new String(url).indexOf("/", 7) == -1) {
			url = new String(url) + "/";
		}

		String suffix = ".jpg";

		if (url.length() >= 5
				&& url.substring(url.length() - 5).indexOf(".") != -1) {
			suffix = url.substring(url.lastIndexOf(".")).toLowerCase();

			String regex = "\\.((jpg)|(jpeg)|(bmp)|(png)|(gif)|(wav)|(wma)|(mp3)|(rm)|(rmvb)|(ram)|(swf)|(flv)"
					+ "|(css)|(rar)|(zip)|(exe)|(bat)|(xls)|(doc)|(lrc)|(java)|(c)|(cpp)|(chm)|(js))";

			if (!suffix.matches(regex)) {
				suffix = ".jpg";
			}

		}

		String[] pieces = url.substring(url.indexOf("/", 7) + 1).split("/");

		String lastPiece = pieces[pieces.length - 1];
		if (lastPiece.indexOf("?") != -1) {
			lastPiece = lastPiece.substring(lastPiece.indexOf("?"));
		}
		if(lastPiece.lastIndexOf(".")!=-1){
			lastPiece = lastPiece.substring(0,lastPiece.lastIndexOf("."));
		}
		lastPiece = lastPiece.replaceAll("[\\W]", "");

		if (lastPiece.length() > 12) {
			lastPiece = lastPiece.substring(0, 6)
					+ lastPiece.substring(lastPiece.length() - 6);
		}
		
		
		this.saveFile = FileUtil.changeToStandardPath(Folder + "\\"
				+lastPiece+ StringUtil.getUniqueStr() + suffix);

	}

	private void open() throws Exception {

		sendHeader();
		try {
			in = socket.getInputStream();
		} catch (Exception e) {
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				in = socket.getInputStream();
			} catch (Exception e1) {
				throw new Exception("abort thread Error");
			}
		}

		receiveHeader();

	}

	private void sendHeader() throws IOException {

		if(url.indexOf(".club.sohu.com/HJOIN?c")!=-1){
			abort();
		}
	
		String urlWithoutTreaty = "";
		url = url.trim();// abundant space character will affect the result;
		if (url.indexOf("//") != -1) {
			urlWithoutTreaty = url.substring(url.indexOf("//") + 2);
		}

		String host = "";
		int port = 80;
		String strPage = "/";

		if (urlWithoutTreaty.indexOf("/") != -1) {
			host = urlWithoutTreaty.substring(0, urlWithoutTreaty.indexOf("/"));
			strPage = urlWithoutTreaty.substring(urlWithoutTreaty.indexOf("/"));
		} else {
			host = urlWithoutTreaty;
			strPage = "/";
		}

		if (host.indexOf(":") != -1) {
			port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
			host = host.substring(0, host.indexOf(":"));
		}
		InetAddress addr = InetAddress.getByName(host);
		socket = new Socket(addr, port);

		// socket.setSoTimeout(60000);
		boolean autoflush = true;
		out = new PrintWriter(socket.getOutputStream(), autoflush);

		strPage = PageUtil.encode(strPage);

		out.println("GET " + strPage + " HTTP/1.1");
		out.println("Host: " + host);
		out.println("Connection: Close");
		out.println("\r\n");
		out.flush();

	}

	private void receiveHeader() throws Exception {

		byte[] buffer = new byte[1];
		header = "";

		while (header.indexOf("\r\n\r\n") == -1) {
			in.read(buffer);
			header += new String(buffer);
		}
		
		//System.out.println(header);
		String firstLineParts[] = header.substring(0, header.indexOf("\n"))
				.split(" ");
		int code = Integer.parseInt(firstLineParts[1]);
		if (code > 400) {
			abort();
		}

		setContentType();
		return;

	}

	private void setContentType() throws Exception {

		String[] headerContent = header.split("\n");

		for (int i = 0; i < headerContent.length; i++) {
			int index = headerContent[i].toLowerCase().indexOf("content-type");
			// System.out.println(headerContent[i]);
			if (index != -1) {
				contentType = headerContent[i].substring(
						headerContent[i].indexOf(":") + 1).trim().toLowerCase();

			}
			int index1 = headerContent[i].toLowerCase().indexOf("location:");
			if (index1 != -1) {
				String location = headerContent[i].substring(
						headerContent[i].indexOf(":") + 1).trim();

				if (redirectionURLs == null) {
					redirectionURLs = new HashSet();
					redirectionURLs.add(url);

				} else {
					redirectionURLs.add(url);
				}

				url = PageUtil.changeURLToStandardURL(url, location);

				if (!redirectionURLs.contains(url)) {

					open();

					return;
				}

			}

		}

	}

	private void writeBodyToFile() throws Exception {

		saveFile = rectifyOutputFilePostfix(saveFile);


		FileUtil.writeToFile(in, saveFile);

		
		if (saveFile.toLowerCase().endsWith(".htm")
				|| saveFile.toLowerCase().endsWith(".html")) {
			
			PageUtil.rectifyPageEncodingAndRemoveRedudantWord(saveFile);
			
			PageUtil.changeURLInPageToStandardURL(saveFile, url);
		}else if(saveFile.toLowerCase().endsWith(".css")){
			PageUtil.changeURLInPageToStandardURL(saveFile, url);
		}

	}

	private String rectifyOutputFilePostfix(String outputFile) {

		
		if (contentType == null) {
			return outputFile;
		}
	
		String contentTypeHeader = contentType;
		if (contentTypeHeader.indexOf(";") != -1) {
			contentTypeHeader = contentTypeHeader.substring(0,
					contentTypeHeader.indexOf(";"));
		}
		
		String contentTypeValue =  new ContentTypeXML().getPostfixMap().get(contentTypeHeader);
		
		if (contentTypeValue == null) {
		
			return outputFile;
		}
		
		String postfix = null;
		if (outputFile.lastIndexOf(".") != -1) {
			postfix = outputFile.substring(outputFile.lastIndexOf(".")).trim();
		}
	

		if (postfix != null) {
			String[] allPostfixs = contentTypeValue.split("|");
			for (int i = 0; i < allPostfixs.length; i++) {
				if (allPostfixs[i].trim().equals(postfix)) {
					
					return outputFile;
				}
			}

		}
		//System.out.println(359+""+this.getClass());
		if (contentTypeValue.indexOf("|") != -1) {
			postfix = contentTypeValue.substring(0, contentTypeValue
					.indexOf("|"));
		} else {
			postfix = contentTypeValue;
		}
		
		if (outputFile.lastIndexOf(".") != -1) {
			outputFile = outputFile.substring(0, outputFile.lastIndexOf("."))
					+ postfix;
		} else {
			outputFile += postfix;
		}

		return outputFile;

	}

	public void normalEnd() {
		try {

			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (socket != null) {
				socket.close();

			}

			done = true;

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void abort() {
		normalEnd();
		url = null;
		in = null;
		socket = null;
		out = null;
		header = null;
		contentType = null;
		saveFile = null;
		redirectionURLs = null;

	}

	public boolean isDone() {
		return done;
	}

	public static void main(String[] args) {
		// String str = "http://www.windowsmarketplace.com/";
		// String str = " ";
		// 

		// String str = "http://www.neu.edu.cn/index_info.htm";
		// String str = "http://202.118.27.146/";
		String str = "http://www.google.cn/";

		// str = "http://www.google.cn/intl/zh-CN/images/logo_cn.gif";
		// String str = "http://www.javaeye.com/news";
		// String str = "
		// http://www.javaeye.com/stylesheets/news.css?1238554427";
		// String str = "http://www.google.com/intl/en_ALL/images/logo.gif";;
		// str = "http://www.neujob.com/images/default/dian%20+++++++ --.jpg";
		// str =
		// "http://image.bitauto.com/uploadpic/Block/2009/318/200931817247546.swf";
		// str =
		// "http://image.bitauto.com/Bitauto/index/20080328/images/button_search.gif";
		// str = "http://image.bitauto.com/Bitauto/08index/images/repeatbg.jpg";
		// str = "http://www.bitauto.com/city/beijing/images/zz13.jpg";
		// str = "http://image.bitauto.com/bitauto/index/zhizao.jpg";
		//
		// str = "http://swc.neu.edu.cn/";
		// str = "http://202.118.31.197/images/in_16.jpg";
		// str = "http://www.neu.edu.cn/index_newsmain.htm";
		// str = "http://photo.bitauto.com/sh2009/images/navi_sp.jpg";
		// str =
		// "http://css.bitauto.com/autoalbum/forecommon/sh2009/common/basic.css";
		// str =
		// "http://img6.bitauto.com/autoalbum/Files/20090422/200904220032205156_262818_1.JPG";
		//
		// str =
		// "http://image.bitauto.com/bitauto/news/2009-4-22/2009422133853234.jpg";
		// str =
		// "http://image.bitauto.com/bitauto/news/2009-4-22/2009422131049390.jpg";
		// str =
		// "http://js.bitauto.com/autoalbum/forecommon/js/BitCommonNew.js";
		// str = "http://photo.bitauto.com/sh2009/images/navi_xc.jpg";

		// str =
		// "http://image.bitauto.com/uploadpic/Block/AD/7174/200871143126453.swf";
		// str = "http://image.bitauto.com/Price/images/search02.gif";

		// str = "http://bbs.neu-pioneer.cn/space.php?uid=151560";
		// str = "http://photo.bitauto.com/v1.0/sh2009/images/name_mnp.jpg";
		// "http://home.neu-pioneer.cn/link.php?url=http://big5.sznews.com/photo/images/00142246e980088f68ee06.jpg";
		str = "http://localhost:8080";
		str = "http://www.baidu.com/";
		

		str = "http://www.google.cn/intl/zh-CN/images/logo_cn.gif";
		str = "http://www.autohome.com.cn/images/log.gif";
		str = "http://car.autohome.com.cn/spec_compare.aspx?id=3489";
		str = "http://www.arting365.com/";
		str = "http://swc.neu.edu.cn/";
		str = "http://www.google.cn/search?complete=1&hl=zh-CN&ie=GB2312&q=%D6%D0%BB%AF%B9%FA%BC%CA%28%BF%D8%B9%C9%29%B9%C9%B7%DD%D3%D0%CF%DE%B9%AB%CB%BE&btnG=Google+%CB%D1%CB%F7&meta=&aq=null";
		str = "http://sports.sina.com.cn/index.shtmls";
		//str = "http://y1.youdao.com/thumb?doc=9e6b6250f268b6c0";
		str = "http://news.sohu.com/";
		str = "http://www.51voa.com/VOA_Standard_English/VOA_Standard_English_26129.html";
		String storePlace = "C:\\Downloads\\MyHTML.htm";
		FileDownloading a = new FileDownloading(str, storePlace, false);
	

		// PageUtil.changeURLInPageToStandardURL(storePlace, str);

		// System.out.println(a.getHeader());
		// String b = null;
		// String c = new String(b );
		// String temp = "aa";
		// System.out.println(temp.indexOf(null));
	}

}
