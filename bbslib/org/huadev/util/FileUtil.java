package org.huadev.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil{


	public static String getUniqueFilePath(String strDir,String strExt)
	{
		strDir += File.separator;
		FileUtil.changeToStandardPath(strDir);
		
		strDir += StringUtil.getUniqueStr();
		if(strExt != "")
		{
			strDir += "." + strExt;
		}
		
		
		return strDir;
		
		
	}
	
	public static File rename(Object file_, String fileName) {
		File file = changeToFile(file_);
		File newFile = new File(file.getParentFile().getAbsolutePath()
				+ File.separator + fileName);
		if (file.exists()) {
			if (file.renameTo(newFile) == true) {
			
				return newFile;
			}
		}
	
		return null;
	}
	
	public static String getParentDir(String path)
	{
		path = changeToStandardPath(path);
		if(path.endsWith(File.separator))
		{
			path = path.substring(0,path.length()-1);			
		}
		
		int iEndPos = path.lastIndexOf(File.separator);
		String parentPath = null;		
	
		if(iEndPos != -1)
		{
			parentPath = path.substring(0,iEndPos);			
		}
		return parentPath;	
	}

	public static File createFileByForce(String path) {

		String parentDir = getParentDir(path);
		File fileParent = new File(parentDir);
		fileParent.mkdirs();
		
		File file = new File(path);
		FileUtil.delFileOrDir(file);
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}
	
	public static File CreateDir(String strPath)
	{
		File file = new File(strPath);
		if(file.isDirectory())
		{
			return file;
		}
		else
		{
			delFileOrDir(strPath);
			file.mkdirs();
			return file;
		}
		
	}

	public static boolean IsDir(String strPath)
	{
		File file = new File(strPath);
		boolean bFlag = false;
		if(file.isDirectory())
		{
			bFlag = true;			
		}
		
		return bFlag;
		
	}
	
	public static boolean IsFile(String strPath)
	{
		File file = new File(strPath);
		boolean bFlag = false;
		if(file.isFile())
		{
			bFlag = true;			
		}
		
		return bFlag;
		
		
	}
	
	public static File createFileByForce(File file)
	{
		
		if(!file.isFile())
		{
			String strPath = file.getParent();
			CreateDir(strPath);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
		
		
		return file;
		
	}

	public static String mergePathToStandardPath(String url, String postfix) {

		url = PageUtil.decode(url);
		postfix = PageUtil.decode(postfix);

		url = url.replace("\\", "/");
		postfix = postfix.replace("\\", "/");
		String fileURLAfterCorrection = "";

		if (!postfix.contains("://")) {

			String host = StringUtil.subString(url, "(http://.*?)/.*", 1);

			if (host.trim().equals("")) {
				if (url.toLowerCase().matches("http://.*")) {
					host = url;
				} else {
					if (url.indexOf(":") != -1) {
						host = url.substring(0, url.indexOf(":") + 1);
					}
				}
			}

			String preURLPostfix = url.substring(host.length());

			while (preURLPostfix.indexOf("//") != -1) {
				preURLPostfix = preURLPostfix.replace("//", "/");
			}

			if (!postfix.startsWith("/")) {
				if (!preURLPostfix.endsWith("/")) {
					if (preURLPostfix.lastIndexOf("/") != -1) {
						preURLPostfix = preURLPostfix.substring(0,
								preURLPostfix.lastIndexOf("/"));
					} else {
						preURLPostfix = "";
					}
				}
				postfix = preURLPostfix + "/" + postfix;

			}

			while (postfix.indexOf("//") != -1) {
				postfix = postfix.replace("//", "/");
			}

			while (postfix.indexOf("/./") != -1) {
				postfix = postfix.replace("/./", "/");
			}

			while (postfix.startsWith("./")) {
				postfix = postfix.substring(2);
			}

			while (postfix.indexOf("../") != -1) {
				int index = postfix.indexOf("../");
				String preHalf = postfix.substring(0, index);
				String postHalf = postfix.substring(index + 3);
				if (preHalf.endsWith("/")) {
					preHalf = preHalf.substring(0, preHalf.length() - 1);

					if (preHalf.indexOf("/") != -1) {
						preHalf = preHalf.substring(0,
								preHalf.lastIndexOf("/") + 1);
					} else {
						preHalf = "";
					}
				}
				postfix = preHalf + postHalf;

			}
			postfix = "/" + postfix;
			while (postfix.indexOf("//") != -1) {
				postfix = postfix.replace("//", "/");
			}

			fileURLAfterCorrection = host + postfix;

		} else {
			fileURLAfterCorrection = postfix;
		}

		while (fileURLAfterCorrection.indexOf("///") != -1) {
			fileURLAfterCorrection = fileURLAfterCorrection
					.replace("///", "//");
		}

		if (-1 != fileURLAfterCorrection.toLowerCase().indexOf("http:")) {
			fileURLAfterCorrection = fileURLAfterCorrection.replace(":::",
					"://");

		}

		// System.out.println(fileURLAfterCorrection);
		return fileURLAfterCorrection;

	}

	private static File changeToFile(Object file) {

		if (file instanceof File) {
			return (File) file;
		} else {
			String filePath = (String) file;
			File newFile = new File(filePath);
			return newFile;
		}
	}

	private static String changeToPath(Object file) {
		if (file instanceof File) {
			return ((File) file).getAbsolutePath();
		} else {

			return (String) file;
		}

	}

	public static String getRelativePath(Object mainFile, Object subFile) {
		String mainPath = changeToPath(mainFile);
		String subPath = changeToPath(subFile);
		mainPath = changeToStandardPath(mainPath).replace(
				File.separator, "/");
		subPath = changeToStandardPath(subPath).replace(File.separator,
				"/");

		String[] mainPathPieces = mainPath.split("/");
		String[] subPathPieces = subPath.split("/");
		int commonPathIndex = 0;
		for (; commonPathIndex < mainPathPieces.length
				&& commonPathIndex < subPathPieces.length;) {
			if (mainPathPieces[commonPathIndex]
					.equalsIgnoreCase(subPathPieces[commonPathIndex])) {
				commonPathIndex++;
			} else {
				break;
			}
		}

		if (commonPathIndex == 0) {
			return subPath;
		}

		String relativePath = "";

		for (int i = commonPathIndex + 1; i < mainPathPieces.length; i++) {
			relativePath += "../";
		}

		for (int i = commonPathIndex; i < subPathPieces.length; i++) {
			relativePath += "/" + subPathPieces[i];
		}
		relativePath = relativePath.replace("//", "/");

		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}
		if (relativePath.trim().equals("")) {
			return null;
		} else {
			return relativePath;
		}

	}

	public static String getWeb_InfPath() {
		String classPath = "";
		try {
			classPath = URLDecoder.decode(FileUtil.class.getResource("")
					.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		classPath = FileUtil.changeToStandardPath(classPath);
		classPath = classPath.substring(classPath.indexOf(File.separator) + 1);
		// get rid of the header
		classPath = classPath.substring(0, classPath.length() - 1);
		// get rid of last file separator

		String className = FileUtil.class.toString();// get the hierarchy of
		// the class

		while (className.indexOf(".") != -1) {
			classPath = classPath.substring(0, classPath
					.lastIndexOf(File.separator));
			className = className.substring(className.indexOf(".") + 1);
		}

		classPath = classPath.substring(0, classPath
				.lastIndexOf(File.separator));

		return classPath;
	}

	public static String changeToStandardPath(String path) {

		path = path.replace("/", File.separator).replace("\\", File.separator);
		while (path.indexOf(File.separator + File.separator) != -1) {
			path = path
					.replace(File.separator + File.separator, File.separator);
		}
		return path;
	}

	public static String getFileEncoding(Object path) {

		String charset = "GBK";
		try {
			File file = null;

			if (path instanceof String) {
				file = new File((String) path);
			} else {
				file = (File) path;
			}
			// if(1==1)return BytesEncodingDetect.getEncoding(file);

			byte[] first3Bytes = new byte[3];
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				// int len = 0;
				int loc = 0;

				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字�? (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码�?
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较�?
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
				// System.out.println( loc + " " + Integer.toHexString( read )
				// );
			}

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return charset;

	}

	public static String readText(String filePathAndName) {

		return readText(filePathAndName, "");
	}

	
	
	public static String readText(String filePathAndName, String encoding) {
		filePathAndName = filePathAndName.replace("%20", " ");
		try {
			File pageFile = new File(filePathAndName);

			if (pageFile.length() > 1024 * 1024 * 2) {
				System.out
						.println("The file is too large,Deny turning into a String:"+filePathAndName);
				return "";

			}
		} catch (Exception e) {
			try {
				Thread.sleep(15000);
				File pageFile = new File(filePathAndName);

				if (pageFile.length() > 1024 * 1024 * 2) {
					System.out
					.println("The file is too large,Deny turning into a String:"+filePathAndName);
					return "";

				}
			} catch (Exception e1) {

				e1.printStackTrace();
			}

		}

		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);

			InputStreamReader isr;
			String fileEncoding = getFileEncoding(filePathAndName);
			if (!fileEncoding.trim().equals("")) {
				if (encoding.equals("")) {
					encoding = fileEncoding;
				} else {
					System.out
							.println("The program obtain the file's encoding is"
									+ fileEncoding
									+ ""
									+ "not consistent with the given encoding: "
									+ FileUtil.class + " 89");
				}
			}

			if (encoding.equals("")) {

				isr = new InputStreamReader(fs);

			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);

			String data = "";
			// if (encoding.equals("")) {
			// System.out
			// .println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			//				
			// }
			while ((data = br.readLine()) != null) {
				str.append(data + "\n");
			}
			// if (encoding.equals("")) {
			// System.out
			// .println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			// String a = "国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国国";
			// }
			st = str.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return st;
	}

	public static void writeToFile(Object file_, Object obj, String pageEncoding) {
		try {
			
			File file = changeToFile(file_);
			if (file.exists() == false) {
				createFileByForce(file);
			}

			
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = null;

			if (pageEncoding.equals("")) {
				osw = new OutputStreamWriter(fos);
			} else {
				osw = new OutputStreamWriter(fos, pageEncoding);

			}

			PrintWriter bw = new PrintWriter(osw, true);

			String str = null;
			if (obj instanceof String) {
				str = (String) obj;
			} else {
				List list = (List) obj;
				Iterator it = list.iterator();
				while (it.hasNext()) {
					str += it.next() + "\n";
				}

			}

			bw.write(str);
			bw.close();
			osw.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeToFile(Object file_, Object obj) {

		File file = changeToFile(file_);
		String encoding = "";
		if (file.exists() == true) {
			encoding = getFileEncoding(file);
		}
		

		writeToFile(file_, obj, encoding);

	}

	public static void appendStringToFile(Object file_, String str) {
		try {
			if (str == null) {
				str = "";
			}

			File file = changeToFile(file_);
			if (file.exists() == false) {
				createFileByForce(file);
			}

			FileOutputStream fos = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, FileUtil
					.getFileEncoding(file));
			PrintWriter bw = new PrintWriter(osw, true);

			bw.println(str);

			bw.close();
			osw.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void appendStringToFile(Object file_, List list) {
		try {
			if (list.size() == 0) {
				return;
			}

			File file = changeToFile(file_);
			if (file.exists() == false) {
				createFileByForce(file);
			}

			FileOutputStream fos = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, FileUtil
					.getFileEncoding(file));
			PrintWriter bw = new PrintWriter(osw, true);
			
			Iterator it = list.iterator();
			while (it.hasNext()) {
				bw.println(it.next().toString());
			}
			
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ɾ��ָ���ļ����������ļ�
	 * 
	 * @param path
	 *            �ļ���������·��
	 * @return
	 * @return
	 */
	public static void delFileOrDir(Object file_) {
		File file = changeToFile(file_);

		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
		File[] fileList = file.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			File temp = fileList[i];
			delFileOrDir(temp);
		}

		file.delete();

	}

	/**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPathFile
	 *            ׼�����Ƶ��ļ�Դ
	 * @param newPathFile
	 *            �������¾��·�����ļ���?
	 * @return
	 */
	public static void copyFileToFile(Object oldPath, Object newPath) {
		try {
			File oldFile = changeToFile(oldPath);

			File newFile = changeToFile(newPath);

			int bytesum = 0;
			int byteread = 0;

			if (oldFile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldFile); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С

					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��������ļ��е�����?
	 * 
	 * @param oldPath
	 *            ׼��������Ŀ¼
	 * @param newPath
	 *            ָ�����·������Ŀ�?
	 * @return
	 */
	public static File createNewFileInDir(Object dir, Object file_) {
		File temp = null;

		String directory = "";

		if (dir instanceof File) {
			directory = ((File) dir).getAbsolutePath();
		} else {
			directory = (String) dir;
		}

		String file = "";

		if (file_ instanceof File) {
			file = ((File) file_).getName();
		}

		while (file.startsWith(File.separator)) {
			file = file.substring(1);
		}

		if (directory.endsWith(File.separator)) {
			temp = new File(directory + file);
		} else {
			temp = new File(directory + File.separator + file);
		}

		createFileByForce(temp);

		return temp;

	}

	public static File createDirInDir(Object dir, Object file_) {
		File temp = null;

		String directory = "";

		if (dir instanceof File) {
			directory = ((File) dir).getAbsolutePath();
		} else {
			directory = (String) dir;
		}

		String file = "";

		if (file_ instanceof File) {
			file = ((File) file_).getName();
		}

		while (file.startsWith(File.separator)) {
			file = file.substring(1);
		}

		if (directory.endsWith(File.separator)) {
			temp = new File(directory + file);
		} else {
			temp = new File(directory + File.separator + file);
		}

		try {
			temp.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;

	}

	public static void writeToFile(InputStream in, String saveFile) {

		byte[] b = new byte[1024 * 8];
		int bytesRead = 0;
		int i = 0;

		File file = FileUtil.createFileByForce(saveFile);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			while ((bytesRead = in.read(b, 0, b.length)) != -1) {
			//	System.out.println( new String(b));
				fos.write(b, 0, bytesRead);
				if ((++i) % 256 == 0) {
					System.out.println("------->" + saveFile + ":" + i / 256
							+ "M");
				}

			}
			fos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String getFilename(String strPath)
	{
		strPath = strPath.trim();
		strPath = changeToStandardPath(strPath);
		
		if(strPath.endsWith(File.separator))
		{
			strPath = strPath.substring(0,strPath.length()-1);			
		}
		
		
		
		
		
		String strFilename = strPath;
		
		int iPos = strPath.lastIndexOf(File.separator);
		if(iPos >= 0 )
		{
			strFilename = strPath.substring(iPos+1);			
		}
		
		return strFilename;
		
	}

	public static void copyFileToDir(Object oldPath, Object newPath) {
		try {
			File oldFile = changeToFile(oldPath);

			File newDir = changeToFile(newPath);
			if(oldFile.exists()==false||newDir.equals(oldFile.getParentFile())||oldPath.equals(newPath)) {
				return;
			}



			newDir.mkdirs();
			newDir.delete();
			newDir.mkdirs();

			if (oldFile.isFile()) {
				copyFileToFile(oldFile, createNewFileInDir(newDir, oldFile));
				return;
			} else {
				newDir = createDirInDir(newDir, oldFile);
			}

			File[] file = oldFile.listFiles();
			File temp = null;
			if (file != null) {
				for (int i = 0; i < file.length; i++) {

					temp = file[i];
					if (temp.isFile()) {
						copyFileToFile(temp, createNewFileInDir(newDir, temp));
					}
					if (temp.isDirectory()) {
						copyFileToDir(temp, newDir);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String file = "D:\\Program Files\\Tomcat 6.0\\webapps\\WebsitesConbination\\resource\\NEUNews\\5961531.files\\1437jpg45507796.jpg";
		String a1 = "D:\\b.mdb";
		String b1 = "D:\\c.mdb";
		FileUtil.copyFileToDir(a1, b1);
		// FileUtil.delFileOrDir(dest);

	}

}
