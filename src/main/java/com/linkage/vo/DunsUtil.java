package com.linkage.vo;

public abstract class DunsUtil {

	private final static int DUNS_PAD_VALUE = (int) Math.pow(10,9);
	public static String padDuns(String dunsNbr) {
		//The GLR extract has no 0's prefixed
		//ALR extract has 0's prefixed
		//We prefer to have both with 0's prefixed!
		//so we pad out to 9 places with 0's		
		if(null == dunsNbr || dunsNbr.equals("null")) {
			return null;
		} else {
			// Atlas file is having 11 digits
			// ensuring that dunsNumber  having 11 digits and starting with numerals
			// 1,2 etc are ignored, to avoid number format exception
			// we are considering only dunsnumber with 11 digits that starts with 00
			if((dunsNbr.length() == 11 && dunsNbr.startsWith("00")) || dunsNbr.length() < 9) {
				int dunsVal = Integer.valueOf(dunsNbr);
				return String.valueOf(dunsVal + DUNS_PAD_VALUE).substring(1);
			} else {
				return dunsNbr;
			}
			
		}
	}
	
}
