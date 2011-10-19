package com.linkage.vo;

public class Util {

	public static String cleanString(String rawString) {
		if(null == rawString)
			return null;
		
		String s = rawString.trim();
		
		if( s.equals("") || s.equals("null"))
			return null;
		
		return s;
		
	}
}
