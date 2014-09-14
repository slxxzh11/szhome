package org.huadev.dl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.huadev.util.FileUtil;
import org.huadev.util.PageUtil;

public class NewHrefExtraction extends Thread {

	private String page = "";

	private String subject = "";

	boolean done = false;

	public boolean isDone() {
		return done;
	}

	public NewHrefExtraction(String page, String subject) {
		this.page = page.trim();
		this.subject = subject;

		page = page.trim().toLowerCase();
		
		if(page.matches("(.*?)\\.((htm)|(html)|(css)|(txt)|(js))")){
			
			
			String pageContent = PageUtil.removeScriptAndStyleCode( FileUtil.readText(page));

			extractHrefExceptInFrame(pageContent);
			extractHrefInFrame(pageContent);
			System.out.println(page + " was Extracted");
		
		}else{
			System.out.println(page + "  is not A TXT銆�ile");
		}
		

	}

	public void extractHrefInFrame(String line) {

		HashSet pagesInFrame = PageUtil.extractPageInFrames(line);

		Iterator it = pagesInFrame.iterator();
		while (it.hasNext()) {
			String fileURL = (String) it.next();
			fileURL = fileURL.trim();

			String subPage = PageUtil.changeURLToStandardURL(this.page.replace(
					"\\", "/"), fileURL);
			subPage = subPage.replace("//", "\\").replace("/", "\\\\");
			new NewHrefExtraction(subPage, subject);
		}

	}

	public void extractHrefExceptInFrame(String str) {

		HashMap<String, String> map = new HashMap<String, String>();
		str = str.replace("\r", " ").replace("\n", " ");
		Pattern p = Pattern.compile("<\\s*a([^<]*?)>(.*?)<\\s*/a\\s*>",
				Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(str);

		while (m.find()) {

			try {

				String hyperLink = m.group();

				String href = "";
				Iterator<String> it = PageUtil.extractAllURLS(hyperLink)
						.iterator();

				while (it.hasNext()) {
					
					href = it.next();
					if(href.toLowerCase().indexOf("http:")!= -1){
						break;
					}
				}

				Matcher m1 = Pattern.compile("<.*?>").matcher(hyperLink);

				while (m1.find()) {
					String tag = m1.group();

					if (!tag.toLowerCase().matches(".*?<\\s*img.*")) {
						hyperLink = hyperLink.replace(tag, "");
					}

				}

				String innerText = hyperLink.replaceAll("\\s{2,}", " ").trim();

				href = href.trim();
				if (href.equals("") || innerText.equals("")) {
					continue;
				}

				if (map.get(href) != null) {
					innerText = map.get(href) + innerText;

				}

				map.put(href, innerText);
				// System.out.println(href);

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

		storeNewHrefInDatabaseBeforeDownloading(map);
		// downLoadNewHref(map.keySet());

	}

	private String rectifyImgSrcInInnerText(String innerText) {

		HashSet set = PageUtil.extractAllURLS(innerText);
		Iterator it1 = set.iterator();
		String parentFileOfThePage = page.replace("/", "\\").substring(0,
				page.lastIndexOf("\\"));

		while (it1.hasNext()) {
			String imgSrc = (String) it1.next();
			String rectifiedImgSrc = imgSrc;
			if (imgSrc.toLowerCase().indexOf(":") == -1) {
				rectifiedImgSrc = parentFileOfThePage + "\\"
						+ imgSrc.replace("/", "\\");
			}

			innerText = innerText.replace(imgSrc, FileUtil.changeToStandardPath(rectifiedImgSrc));

		}

		return innerText;

	}

	private void storeNewHrefInDatabaseBeforeDownloading(
			HashMap<String, String> map) {

		Iterator<String> it = map.keySet().iterator();
		
		while (it.hasNext()) {

			String href = it.next();

			String innerText = map.get(href);
			innerText = rectifyImgSrcInInnerText(innerText);

		}

	}


	public static void main(String args[]) {


	}

}
