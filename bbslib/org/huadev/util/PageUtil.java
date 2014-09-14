package org.huadev.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageUtil {

	/**
	 * @param line
	 * @return
	 */

	public static String encode(String url) {

		url = decode(url);

		try {
			url = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url = url.replace("%3A", ":");
		url = url.replace("+", "%20");
		url = url.replace("%23", "#");
		url = url.replace("%26", "&");
		url = url.replace("%2F", "/");
		url = url.replace("%3F", "?");
		url = url.replace("%3D", "=");
		url = url.replace("%2B", "+");
		url = url.replace("%25", "%");

		return url;
	}

	public static String decode(String url) {

		url = url.replace("%3A", ":");
		url = url.replace("%20", " ");
		url = url.replace("%23", "#");
		url = url.replace("%26", "&");
		url = url.replace("%2F", "/");
		url = url.replace("%3F", "?");
		url = url.replace("%3D", "=");
		url = url.replace("%2B", "+");
		url = url.replace("%25", "%");
		url = url.replace("&amp;", "&");

		return url;
	}

	public static String changeURLToStandardURL(String url, String postfix) {

		// postfix = encode(decode(postfix));
		return FileUtil.mergePathToStandardPath(url, postfix);

		// url = url.trim().replace("\\", "/");
		// postfix = postfix.trim().replace("\\", "/");
		// String host = null;
		// if (url.indexOf("/", 7) != -1) {
		// host = url.substring(0, url.indexOf("/", 7));
		// } else {
		// host = url.substring(0, url.indexOf(":") + 1);
		// }
		//
		// String parentPath = url.substring(0, url.lastIndexOf("/") + 1);
		//
		// String fileURLAfterCorrection = "";
		// if (!postfix.contains("://")) {
		//
		// if (postfix.startsWith("/")) {
		// fileURLAfterCorrection = host + postfix;
		// } else {
		//
		// String prefix = parentPath;
		// while (postfix.startsWith("../")) {
		// if (prefix.toLowerCase().matches("http://.*?/.+")) {
		// prefix = prefix.substring(0, prefix.length() - 1);
		// prefix = prefix.substring(0,
		// prefix.lastIndexOf("/") + 1);
		// }
		// postfix = postfix.substring(3);
		//
		// }
		//
		// while (postfix.startsWith("./")) {
		// postfix = postfix.substring(2);
		// }
		// if (postfix.toLowerCase().startsWith("javascript")) {
		// fileURLAfterCorrection = postfix;
		// } else {
		// fileURLAfterCorrection = prefix + postfix;
		// }
		//
		// }
		//
		// } else {
		// fileURLAfterCorrection = postfix;
		// }
		//
		// fileURLAfterCorrection = fileURLAfterCorrection.replace("://",
		// ":::");
		//
		// while (fileURLAfterCorrection.indexOf("//") != -1) {
		// fileURLAfterCorrection = fileURLAfterCorrection.replace("//", "/");
		// }
		//
		// fileURLAfterCorrection = fileURLAfterCorrection.replace(":::", "://")
		// .replace(" ", "%20");
		//
		// return fileURLAfterCorrection;

	}
	
	
	public static String ChangeUrlInContentToStandardURL(String strContent,String strPageUrl)
	{
		
		HashSet URLS = extractAllURLS(strContent);

		ArrayList list = List_Set_MapUtil.sortSetAccordingToKeyLength(URLS,
				List_Set_MapUtil.DESC);

		
		Iterator it = list.iterator();
		

		HashMap<String, String> map = new HashMap<String, String>();
		int id = 10;

		
		while (it.hasNext()) {

			
			String fileURL = (String) it.next();
			String postfix = fileURL;
		
			String fileURLAfterCorrection = changeURLToStandardURL(strPageUrl,
					postfix);
			String key = "__" + (id++) + "__" + System.currentTimeMillis();
			map.put(key, fileURLAfterCorrection);
			strContent = strContent.replace(fileURL, key);

		

		}

		
		Iterator it1 = map.keySet().iterator();
		while (it1.hasNext()) {
			String key1 = (String) it1.next();
			strContent = strContent.replace(key1, (String) map.get(key1));

		}
		
		return strContent;

		
	}

	public static void changeURLInPageToStandardURL(String page, String strPageURL) {

		try {
			System.out.println("please wait:url is being rectified,may take several minute...");
			
			String line = FileUtil.readText(page);
			line = ChangeUrlInContentToStandardURL(line,strPageURL);
			FileUtil.writeToFile(page, line);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static HashSet<String> extractAllURLS(String line) {

		HashSet<String> URLS = new HashSet<String>();
		return extractAllURLS(line, URLS);

	}
	
	public static String[] extractOneHref(String strLine)
	{
		LinkedList<String[]> listUrl = extractHrefUrl(strLine,1);
		if(listUrl.size() > 0)
		{
			return listUrl.get(0);
		}
		else
		{
			return null;
		}

	}
	
	public static LinkedList<String[]> extractHrefUrl(String strLine,int iExtractCount)
	{
		
		
		String href_Src_Link = "<a(.*?)>(.*)</a";

		Pattern p = Pattern.compile(href_Src_Link, Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(strLine);
		
		LinkedList<String[]> listHrefs = new LinkedList<String[]>();

		while (iExtractCount > 0 && m.find()) {

			String[] strHref = new String[2];
			String strfileURL = m.group(1);
			String strContent = m.group(2);
			
			HashSet<String> setUrls = extractAllURLS(strfileURL);
			if(setUrls.size()>0)
			{
				strHref[0] = strContent;
				strHref[1] = setUrls.iterator().next();
			}
			
			
			iExtractCount--;
			listHrefs.add(strHref);
		
		}

		
		return listHrefs;
	}
	
	
	
	
	
	
//	public static HashSet<String> extractAllURLSPrecisely(String line) {
//
//		HashSet<String> URLS = new HashSet<String>();
//		return extractAllURLS(line, URLS,true);
//
//	}

	public static HashSet<String> extractAllURLS(String line,
			HashSet<String> URLS) {

		// if (line.length() > 160000) {
		// System.out.println(" PageUtil 148: this may take 2 or 3 minute ");
		// }

		String href_Src_Link = "((action|href|src|link)(\\s*+)=(([\\s'\"]*+)(.{1,}?))(?=([><'\"]|$)))";

		Pattern p = Pattern.compile(href_Src_Link, Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(line);

		while (m.find()) {

			String fileURL = m.group(4).trim();
			
			
			

			if (fileURL.matches(".*?([\"'])\\s*\\1.*")
					|| fileURL.startsWith("+")
					|| fileURL.toLowerCase().indexOf("mailto") != -1
					|| fileURL.toLowerCase().indexOf("javascript") != -1) {
				continue;
			}

			fileURL = fileURL.replaceAll("[\"'\\s]*(.*?)[\"'\\s]*$", "$1")
					.trim();

			if (m.group(5) != null && m.group(5).trim().equals("")) {

				Matcher m1 = Pattern.compile("(.{1,})(?=(\\s|$))",
						Pattern.CASE_INSENSITIVE).matcher(fileURL);
				if (m1.find()) {
					fileURL = m1.group();
				}

			}
			
			 fileURL = trimIncorrectPostfixOfFileURLPrecisely(fileURL.trim());
			
			if (fileURL.length() > 3) {

				line = line.replace(m.group(), "");
				
				URLS.add(fileURL);
			}

		}

		String audio_Picture_html = "([\\S&&[^\"]&&[^\\']&&[^\\|]&&[^\\(]&&[^\\)]]+?(\\.)((jpg)|(jpeg)|(bmp)|(png)|(gif)|(wav)|(wma)|(mp3)|(rm)|(rmvb)|(ram)|(swf)|(flv)|(css)|(js))(?=\\W|$))";
		p = Pattern.compile(audio_Picture_html, Pattern.CASE_INSENSITIVE);

		m = p.matcher(line);

		while (m.find()) {
			String fileURL = m.group();
			
			fileURL = fileURL.replaceAll("[\"'\\s]*(.*?)[\"'\\s]*$", "$1")
					.trim();

			
			Matcher m1 = Pattern.compile(
					"((action|href|src|link)(\\s*+)=(([\\s'\"]*+)(.{1,})))",
					Pattern.CASE_INSENSITIVE).matcher(fileURL);
			if (m1.find()) {
				fileURL = m1.group(4);
			}

			 fileURL = trimIncorrectPostfixOfFileURLPrecisely(fileURL.trim());
			
			if (fileURL.length() > 3) {

				line = line.replace(m.group(), "");
				URLS.add(fileURL);
			}

			continue;

		}

		
		return URLS;
	}

	public static String trimIncorrectPostfixOfFileURLPrecisely(String fileURL) {
		fileURL = fileURL.trim();
		if(fileURL.lastIndexOf("/") == -1){
			return fileURL;
		}
		
		String postfix = fileURL.substring(fileURL.lastIndexOf("/"));
		
		if(postfix.indexOf("=")==-1||postfix.indexOf(" ")==-1){
			return fileURL;
		}
		
		String prefix =  fileURL.substring(0,fileURL.lastIndexOf("/"));

		int equalQuoteIndex = postfix.indexOf("=");
		int spaceQuoteIndex = postfix.indexOf(" ");
		
		if(equalQuoteIndex < spaceQuoteIndex ){
			postfix = postfix.substring(0,spaceQuoteIndex);
		}else{
			postfix = postfix.substring(0,equalQuoteIndex);
			postfix = postfix.substring(0,postfix.lastIndexOf(" "));
		}
		
//	    postfix = postfix.substring(0,postfix.indexOf("="));
//	    if(postfix.lastIndexOf(" ")!=-1){
//	        postfix = postfix.substring(0,postfix.lastIndexOf(" "));
//	    }
//	    System.out.println(postfix);
	    return prefix+postfix;

	}
	


	public static HashSet extractPictureAndAudioAndJsFiles(String line) {

		Pattern p = Pattern
				.compile(
						"((src)(\\s*+)=(([\\s'\"]*+)(.{1,}?))(?=([><'\"]|$)))"
								+ "|([\\S&&[^\"]&&[^\\']&&[^\\(]&&[^\\|]]+?(\\.)((jpg)|(jpeg)|(bmp)|(png)|(gif)|(wav)|(wma)|(mp3)|(rm)|(rmvb)|(ram)|(swf)|(flv)|(js))(?=\\W|$))",
						Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(line);

		HashSet<String> URLS = new HashSet<String>();

		while (m.find()) {
			extractAllURLS(m.group(), URLS);
		}

		Iterator frameAndCSSIterator = List_Set_MapUtil.addSetWithoutCopy(
				extractPageInFrames(line), extractCssFiles(line)).iterator();

		while (frameAndCSSIterator.hasNext()) {
			URLS.remove(frameAndCSSIterator.next());
		}

		return URLS;

	}

	public static HashSet extractPageInFrames(String line) {
		HashSet<String> URLS = new HashSet<String>();
		Matcher m1 = Pattern.compile("(<iframe [^<]{1,}?>)|(<frame [^<]{1,}?>)",
				Pattern.CASE_INSENSITIVE).matcher(line);
		while (m1.find()) {
			extractAllURLS(m1.group(), URLS);
		}
		return URLS;
	}

	public static HashSet extractCssFiles(String line) {
		HashSet<String> URLS = new HashSet<String>();
		Matcher m0 = Pattern
				.compile(
						"(<link([^<]*?)href(\\s*+)=(\\s*+)([^<]{5,}?)([><'\"]|$))"
								+ "|([\\S&&[^\"]&&[^\\']&&[^\\(]&&[^\\|]]+?(\\.)(css)(?=\\W|$))",
						Pattern.CASE_INSENSITIVE).matcher(line);
		while (m0.find()) {
			extractAllURLS(m0.group(), URLS);

		}

		return URLS;

	}

	public static void rectifyPageEncodingAndRemoveRedudantWord(String pagePath) {

		String realPageEncoding = FileUtil.getFileEncoding(pagePath);

		String content = FileUtil.readText(pagePath);
		Pattern p = Pattern.compile("charset\\s*=\\s*(\\S+?)(\\s|\\\"|'|$)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);

		if (m.find()) {
			String charsetInPageCode = m.group();
			String encodingInPageCode = m.group(1);
			if (!encodingInPageCode.equals(realPageEncoding)) {
				content = content.replace(charsetInPageCode, charsetInPageCode
						.replace(encodingInPageCode, realPageEncoding));
			}
		} else {
			String newContentTypeCodeInPage = "<META http-equiv=Content-Type content=\"text/html; charset="
					+ realPageEncoding + "\">";
			int index = -1;

			if ((index = content.toLowerCase().indexOf("<head>")) != -1) {
				content = content.substring(0, index)
						+ newContentTypeCodeInPage
						+ content.substring(index + 6);
			} else if ((index = content.toLowerCase().indexOf("<html>")) != -1) {
				content = content.substring(0, index)
						+ newContentTypeCodeInPage
						+ content.substring(index + 6);
			} else {
				content = newContentTypeCodeInPage + content;
			}

		}

		int index1 = -1;
		if ((index1 = content.toLowerCase().indexOf("<!doctype")) != -1) {
			content = content.substring(index1);
		} else if ((index1 = content.toLowerCase().indexOf("<html")) != -1) {
			content = content.substring(index1);
		}

		FileUtil.writeToFile(pagePath, content, realPageEncoding);

	}

	public static String reserveChineseChar(String str) {

		str = removeTag(str);
		str = str.replace("\r", ":::");
		str = str.replace("\n", ":::");
		String chinese = "";
		char chineseBeginning = 19968;
		char chineseEnd = 40869;
		String format = "[" + chineseBeginning + "-" + chineseEnd
				+ ",\\.\\d \\s]{10,}";

		Pattern p = Pattern.compile(format);
		Matcher m = p.matcher(str);

		while (m.find()) {
			chinese += m.group();
		}

		chinese = chinese.replaceAll("[\\.,\\d]{12,}+", " ");
		chinese = chinese.replaceAll("[,\\s]{3,}+", "<br>");
		str = str.replace(":::", "\n");
		chinese = "<br>" + chinese + "<br>";

		return chinese;
	}

	public static String getTheBiggestNews(String str, int bigNewsAmount) {

		str = removeTag(str);
		str = str.replace("\r", "");
		str = str.replace("\n", "");
		str = StringUtil.replaceAll(str, "<br>", "<BR>");
		String[] allNews = str.split("<BR>");

		TreeMap<String, Integer> newsMap = new TreeMap<String, Integer>();

		for (int i = 0; i < allNews.length; i++) {

			newsMap.put(allNews[i], i);
		}

		ArrayList<String> newsMapKey = List_Set_MapUtil
				.sortSetAccordingToKeyLength(newsMap.keySet(),
						List_Set_MapUtil.DESC);
		// TreeMap<Integer,String> result = new TreeMap<Integer,String> ();
		String resultString = "";
		// System.out.println(newsMapKey);
		for (int i = 0; i < newsMapKey.size() && i < bigNewsAmount; i++) {
			resultString += newsMapKey.get(i).trim() + ":::";

		}

		if (resultString.endsWith(":::")) {
			resultString = resultString.substring(0,
					resultString.lastIndexOf(":::")).trim();
		}

		// System.out.println(resultString);

		return resultString;

	}
	


	public static String removeTag(String str) {

		str = removeScriptAndStyleCode(str);
		str = str.replace("\r", "");
		str = str.replace("\n", "");
		str = str.replaceAll("</[^<]*?>", "");
		str = str.replaceAll("(<[^<]*?>)", "");
		str = str.replaceAll("(\\s){2,}", "");
		str = str.replaceAll("&#(\\w|;)*", "");
		str = str.replaceAll("&(\\w|;)*", "");
		str = str.replaceAll("#(\\w|;)*", "");

		return str;
	}
	

	public static String removeScriptAndStyleCode(String str) {
		str = str.replace("\r", ":::");
		str = str.replace("\n", ":::");
		Matcher m = Pattern
				.compile(
						"(<script[^<]*?>.*?</script>)|<script[^<]*?/>|(<style[^<]*?>.*?</style>)",
						Pattern.CASE_INSENSITIVE).matcher(str);

		while (m.find()) {
			str = str.replace(m.group(), "");
		}

		str = str.replace(":::", "\n");

		return str;

	}
	
	
	public static String extractOneUrl(String line)
	{
		LinkedList<String> list = new LinkedList<String>();
		
		extractAllURLS(line,list,1);
		if(list.size() > 0)
		{
			return list.iterator().next();
		}
		else
		{
			return null;
		}
		
		
	}
	
	public static LinkedList<String> extractAllURLS(String line,
			LinkedList<String> URLS,int iMaxUrlCount) {

	
		String href_Src_Link = "((action|href|src|link)(\\s*+)=(([\\s'\"]*+)(.{1,}?))(?=([><'\"]|$)))";

		Pattern p = Pattern.compile(href_Src_Link, Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(line);
	    int iLinkCount = 0;

		while (m.find()) {
			
		

			String fileURL = m.group(4).trim();
			
			
			

			if (fileURL.matches(".*?([\"'])\\s*\\1.*")
					|| fileURL.startsWith("+")
					|| fileURL.toLowerCase().indexOf("mailto") != -1
					|| fileURL.toLowerCase().indexOf("javascript") != -1) {
				continue;
			}

			fileURL = fileURL.replaceAll("[\"'\\s]*(.*?)[\"'\\s]*$", "$1")
					.trim();

			if (m.group(5) != null && m.group(5).trim().equals("")) {

				Matcher m1 = Pattern.compile("(.{1,})(?=(\\s|$))",
						Pattern.CASE_INSENSITIVE).matcher(fileURL);
				if (m1.find()) {
					fileURL = m1.group();
				}

			}
			
			 fileURL = trimIncorrectPostfixOfFileURLPrecisely(fileURL.trim());
			
			if (fileURL.length() > 3) {

				line = line.replace(m.group(), "");
				
				URLS.add(fileURL);
			}
			
			
			if(iMaxUrlCount > 0)
			{
				iLinkCount++;
				if(iLinkCount>= iMaxUrlCount)
				{
					break;
				}
			}

		}
		
		return URLS;
	}

	public static void main(String[] args) {
		String a = "<img src=\"D:\\Program Files\\Tomcat 6.0\\webapps\\WebsitesConbination\\resource\\NEUNews\\6037671.files\\baijiangzhan9763546.jpg\" width=\"154\" height=\"90\" border=\"0\">";
		String b = "<img src=\"D:\\Program Files\\Tomcat 6.0\\webapps\\WebsitesConbination\\resource\\NEUNews\\11485187.files\\renminwang15108781.gif\"";
		System.out.println(List_Set_MapUtil.sortSetAccordingToKeyLength(PageUtil.extractAllURLS(a), List_Set_MapUtil.DESC));
		// String line = FileUtil
		// .readText("C:\\Downloads\\html\\n264002999092453.htm");
		//
		// System.out.println(PageUtil.changeURLToStandardURL("a",
		// "http://image.youdao.com/http%3A%2F%2Fimg3.fengniao.com%2Fforum%2Fsecpics%2F46%2F37%2F1807383.jpg"));

		// FileUtil.writeToFile("C:\\Downloads\\html\\test.htm", result);
		// String str = "(http://www.bai.com/a.jpg";
		// System.out.println(PageUtil.extractAllURLS(str));
	}

}
