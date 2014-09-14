package org.huadev.util;

import java.util.ArrayList;
import java.util.Iterator;

public class XMLHierarchyBean {

	XMLElementBean presentElement;

	ArrayList<XMLElementBean> childNodes;

	public XMLHierarchyBean(XMLElementBean presentElement) {
		this.presentElement = presentElement;
		childNodes = new ArrayList<XMLElementBean>();

	}

	public void addChildNode(XMLElementBean childNode) {
		childNodes.add(childNode);
	}

	public Iterator<XMLElementBean> getChildNodesIterator() {
		return childNodes.iterator();
	}

	public XMLElementBean getPresentElement() {
		return presentElement;
	}

	public String toString() {

		String str = presentElement.toString();
		if (childNodes != null) {
			Iterator it = childNodes.iterator();
			while (it.hasNext()) {
				str += it.next();

			}
		}

		return str;
	}

}
