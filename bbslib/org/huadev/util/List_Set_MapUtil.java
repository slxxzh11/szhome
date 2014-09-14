package org.huadev.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import java.util.Iterator;
import java.util.regex.Pattern;


public class List_Set_MapUtil {

	public static final boolean ASCE = false;

	public static final boolean DESC = true;

	public static TreeMap changeToTreeMap(HashMap map) {

		TreeMap result = new TreeMap();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = map.get(key);
			result.put(key, value);

		}

		return result;

	}
	
	
	public static void removeKeysFromMap(Map map,Set set){
		
		Iterator it = set.iterator();
		while(it.hasNext()){
			map.remove(it.next());
		}
		
	}
	
	public static ArrayList getMapValues(Map map){
		ArrayList result = new ArrayList();
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			result.add(map.get(it.next()));
		}
		return result;
	}
	

	public static Map putSetToMap(Set set, Map map) {
		Iterator it = set.iterator();
		while (it.hasNext()) {
			map.put(it.next(), "");
		}
		return map;

	}

	public static Map putListToMap(List list, Map map) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			map.put(it.next(), "");
		}
		return map;

	}

	public static Set putListToSet(List list, Set set) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			set.add(it.next());
		}
		return set;

	}

	public static Map putArrayToMap(Object[] objects, Map map) {
		for (int i = 0; i < objects.length; i++) {
			map.put(objects[i], "");
		}
		return map;

	}

	public static Set putArrayToSet(Object[] objects, Set set) {
		for (int i = 0; i < objects.length; i++) {
			set.add(objects[i]);
		}
		return set;

	}

	public static HashSet[] breakToPieces(Set set, int smallSize) {
		ArrayList setList = new ArrayList();
		Iterator it = set.iterator();
		int count = smallSize;
		HashSet temp = new HashSet();

		while (it.hasNext()) {
			temp.add(it.next());
			if (--count <= 0) {
				setList.add(temp);
				temp = new HashSet();
				count = smallSize;
			}

		}
		setList.add(temp);

		HashSet[] resultSet = new HashSet[setList.size()];

		it = setList.iterator();
		count = 0;
		while (it.hasNext()) {
			resultSet[count++] = (HashSet) it.next();

		}
		return resultSet;
	}

	public static HashSet changeArrayListToHashSet(ArrayList list) {

		HashSet set = new HashSet();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			set.add(it.next());
		}

		return set;

	}

	public static ArrayList fuzzyMatch(Set set, String keyPatternRegex) {
		ArrayList list = new ArrayList();
		Iterator it = set.iterator();

		while (it.hasNext()) {
			String temp = (String) it.next();
			if (temp.matches(keyPatternRegex)) {
				list.add(temp);
			}
		}

		if (list.size() == 0) {
			return null;
		} else {

			return list;
		}

	}

	public static ArrayList fuzzyMatchIgnoreCase(Set set, String keyPatternRegex) {
		ArrayList list = new ArrayList();
		Iterator it = set.iterator();

		while (it.hasNext()) {
			String temp = (String) it.next();
			if (Pattern.compile(keyPatternRegex, Pattern.CASE_INSENSITIVE)
					.matcher(temp).find()) {
				list.add(temp);
			}
		}
		if (list.size() == 0) {
			return null;
		} else {

			return list;
		}
	}

	public static TreeMap fuzzyMatch(Map map, String keyPatternRegex) {
		TreeMap resultMap = new TreeMap();
		Iterator it = map.keySet().iterator();

		while (it.hasNext()) {

			String key = (String) it.next();
		
			if (Pattern.compile(keyPatternRegex).matcher(key).find()) {
				resultMap.put(key, map.get(key));
			}
		}

		if (resultMap.size() == 0) {
			
			return null;
		} else {

			return resultMap;
		}

	}
	
	public static ArrayList[] breakToPieces(List list,int smallSize){
		ArrayList setList = new ArrayList();
		Iterator it = list.iterator();
		int count = smallSize;
		ArrayList temp = new ArrayList();

		while (it.hasNext()) {
			temp.add(it.next());
			if (--count <= 0) {
				setList.add(temp);
				temp = new ArrayList();
				count = smallSize;
			}

		}
		setList.add(temp);

		ArrayList[] resultSet = new ArrayList[setList.size()];

		it = setList.iterator();
		count = 0;
		while (it.hasNext()) {
			resultSet[count++] = (ArrayList) it.next();

		}
		return resultSet;
		
	}

	public static TreeMap fuzzyMatchIgnoreCase(Map map, String keyPatternRegex) {
		TreeMap resultMap = new TreeMap();
		Iterator it = map.keySet().iterator();

		while (it.hasNext()) {

			String key = (String) it.next();
			if (Pattern.compile(keyPatternRegex, Pattern.CASE_INSENSITIVE)
					.matcher(key).find()) {
				resultMap.put(key, map.get(key));
			}
		}
		if (resultMap.size() == 0) {
			return null;
		} else {
			return resultMap;
		}

	}

	public static int getRealSizeOfKeySetDespiteOfConcurrency(HashMap map) {
		Iterator it = map.keySet().iterator();
		int count = 0;
		try {
			while (it.hasNext()) {

				it.next();
				count++;

			}
		} catch (Exception e) {
			count = 0;
		}

		return count;

	}

	public static HashSet addSetWithoutCopy(HashSet mainSet, HashSet addend) {
		Iterator it = addend.iterator();
		while (it.hasNext()) {
			mainSet.add(it.next());
		}
		return mainSet;
	}

	public static ArrayList minusSet(Set minuend, Set subtrahend) {

		if (minuend == null) {
			return null;
		}
		Iterator it = sortSetAccordingToKeyLength(minuend, List_Set_MapUtil.DESC)
				.iterator();
		ArrayList result = new ArrayList();

		while (it.hasNext()) {
			Object obj = it.next();

			if (subtrahend == null || !subtrahend.contains(obj)) {
				result.add(obj);
			}
		}

		return result;

	}

	public static Map add(Map map, String key, Object value) {

		if (map.get(key) != null) {
			ArrayList list = (ArrayList) map.get(key);
			list.add(value);
			Iterator it = list.iterator();

		} else {
			ArrayList list = new ArrayList();
			list.add(value);
			map.put(key, list);
		}
		return map;

	}

	public static ArrayList sortSetAccordingToKeyLength(Set set,
			boolean asceOrDesc) {

		ArrayList list = new ArrayList();
		Iterator it = set.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			list.add(obj);
		}

		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {

				String pre = (String) list.get(j - 1);
				String post = (String) list.get(j);
				if (pre.length() < post.length() ? asceOrDesc : !asceOrDesc) {
					list.set(j - 1, post);
					list.set(j, pre);
				}

			}
		}

		return list;

	}

	public static ArrayList sortSetAccordingToValuesListLength(Map map,
			boolean asceOrDesc) {

		ArrayList list = new ArrayList();
		Iterator it = map.keySet().iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			list.add(obj);
		}

		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				String pre = (String) list.get(j - 1);
				String post = (String) list.get(j);
				int preSize = ((ArrayList) map.get(pre)).size();
				int postSize = ((ArrayList) map.get(post)).size();
				if (preSize < postSize ? asceOrDesc : !asceOrDesc) {
					list.set(j - 1, post);
					list.set(j, pre);
				}
			}
		}

		return list;

	}

	public static void main(String[] args) {
		String str = FileUtil.readText("D:\\a.txt");
		// System.out.println(str);
		String[] strs = str.split("\n");
		HashMap map = new HashMap();

		for (int i = 0; i < strs.length; i++) {
			String temp = strs[i];
			int index = temp.indexOf("======");
			if (index != -1) {
				String key = temp.substring(index + 6);
				String postfix = temp.substring(0, index);
				String[] a = postfix.split(" ");
				for (int m = 0; m < a.length; m++) {
					if (!a[m].trim().equals("")) {
						List_Set_MapUtil.add(map, key.trim(), a[m].trim());
					}
				}

			}
		}
		//
		// ;
		Iterator it3 = List_Set_MapUtil.sortSetAccordingToValuesListLength(map,
				List_Set_MapUtil.DESC).iterator();

		while (it3.hasNext()) {

			String key3 = (String) it3.next();
			ArrayList list7 = (ArrayList) map.get(key3);
			System.out.print("<header><content-type>" + key3
					+ "</content-type><postfix>" + list7.get(0));
			for (int u = 1; u < list7.size(); u++) {
				System.out.print("|" + list7.get(u));

			}

			System.out.println("</postfix></header>");

		}

		// String[] result = resultString.split("\n");
		// for(int i = 0; i < result.length-1;i++){
		// for(int j = result.length-1; j>0;j--){
		// if(result[j].length()>result[j-1].length()){
		// String temp = result[j];
		// result[j] = result[j-1];
		// result[j-1]=temp;
		// }
		// }
		// }
		// for(int i = 0; i < result.length; i++){
		// System.out.println(result[i]);
		// }

	}

}
