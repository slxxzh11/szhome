package org.huadev.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	private static int fileCount = 0;
	
	public static String subString(String str,String regexPattern,int groupNumber){
		Matcher m = Pattern.compile(regexPattern,Pattern.CASE_INSENSITIVE).matcher(str);
		String result = "";
		while(m.find()){
		
			result += m.group(groupNumber)+":::";			
		}
		
		if(result.endsWith(":::")){
			result = result.substring(0,result.lastIndexOf(":::"));
		}
				
		return result;
		
	}
	
	public static String subString(String str,String regexPattern){

				
		return subString(str,regexPattern,0);
		
	}
	
	
	public static String replaceAll(String str,String regexPattern,String newSubString,int groupNumber ){
		Matcher m = Pattern.compile(regexPattern,Pattern.CASE_INSENSITIVE).matcher(str);
	
		while(m.find()){
		
			str = str.replace(m.group(groupNumber), newSubString);
		}
		
		return str;
	}
	
	
	public static String replaceAll(String str,String regexPattern,String newSubString){
		return replaceAll(str,regexPattern,newSubString,0);
	}
	
	public static String  getUniqueStr()
	{
		String time = (System.currentTimeMillis() + "");

		String strName =  time + fileCount;

		if (fileCount >= 10000) {
			fileCount = 0;
		}
		
		return strName;
	}

}
