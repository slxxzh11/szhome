package org.huadev.util;

import java.util.HashMap;
import java.util.Iterator;

public class XMLElementBean {

	private HashMap<String,String> attributesList;

	private String text;

	public HashMap<String,String> getAttributesList() {		
		return attributesList;
	}
	
	public String getAttribute(String attributeName){
		return attributesList.get(attributeName);
		
	}
	
	public String[] getID_OtherAttributesString(){
		Iterator<String> it = attributesList.keySet().iterator();
		String[]IDAndOtherAttributesString = new String[2];
		IDAndOtherAttributesString[0] = "";
		IDAndOtherAttributesString[1] = "";
		while(it.hasNext()){
			String key = it.next();
			String value = attributesList.get(key);
			if(key.trim().equals("id")){
				IDAndOtherAttributesString[0] = value;
			}else{
				IDAndOtherAttributesString[1] += value+"::::";
			}
		}
		if(IDAndOtherAttributesString[1].endsWith("::::")){
			IDAndOtherAttributesString[1] = IDAndOtherAttributesString[1].substring(0,IDAndOtherAttributesString[1].length()-4);
		}
		return IDAndOtherAttributesString;
	}

	public XMLElementBean() {

		this.attributesList = new HashMap<String,String>(1);
		this.text = null;
	}

	public void addAttribute(String name,String value) {
		attributesList.put(name, value);

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		String str = "Attributes==>";
		if (attributesList != null) {
			Iterator<String> it = attributesList.keySet().iterator();
			while (it.hasNext()) {
				String name = it.next();
				String value = attributesList.get(name);
				
				str += name+"::::"+value + "    ";
			}
		}
		if(text!=null){
		    str += "\n" + "Text:" + text ;
		}
		str += "\n\n";
		return str;
	}

}
