package com.cattsoft.framework.util;

import java.io.Serializable;
import java.util.List;

public class SerializableList implements Serializable{

	private List list;
	
	public List getList(){
		
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
}
