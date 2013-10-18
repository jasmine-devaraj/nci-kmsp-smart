/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.fwk.util;

/**
 * This is a static class which provides miscellaneous useful methods
 */
public class UtilityMethods {

	public static StringBuffer arrayToStringBuffer(Object[] a){
		return arrayToStringBuffer(a, ",", false, null);
	}
	public static StringBuffer arrayToSqlList(Object[] a){
		return arrayToStringBuffer(a, ",", true, "''");
	}
	public static StringBuffer arrayToStringBuffer(Object[] a, String delim, boolean bSingleQuoteValues, String replaceInnerSingleQuotes){
		StringBuffer sb = new StringBuffer();
		if(a!=null){
			for (int i=0; i<a.length; ++i){
				if(i>0){
					sb.append(delim);
				}
				if(bSingleQuoteValues){
					sb.append("'").append(a[i].toString().replace("'",replaceInnerSingleQuotes)).append("'");
				}else{
					sb.append(a[i]);
				}
			}
		}
		return sb;
	}
	/**
	 * This method will "escape" characters in a string that they may then be used in a url (for instance).
	 *
	 * @param	String	The string to be "escaped".
	 * @return	String  The escaped string
	 */
	public static String escapeString(String sVal) {
		if (sVal == null){
			return "";
		}
		StringBuffer sbVal = new StringBuffer(sVal);
		StringBuffer sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '\\': if ((sbVal.length()==(i+1))||((sbVal.charAt(i+1)!='n')&&(sbVal.charAt(i+1)!='r'))){
								sbReturn.append("\\\\");
								} else sbReturn.append("\\");
							break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '\'':sbReturn.append("\\\'");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
	/*	sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '\\':sbReturn.append("\\\\");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		} */
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '\"':sbReturn.append("\\\"");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '&':sbReturn.append("\\&");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '>':sbReturn.append("\\>");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			switch(sbVal.charAt(i)){
				case '<':sbReturn.append("\\<");break;
				default:sbReturn.append(sbVal.charAt(i));
			}
		}
		sbVal = sbReturn;
		sbReturn = new StringBuffer();
		for (int i=0;i<sbVal.length();i++){
			if (sbVal.substring(i,i+1).equals("\n")) {
				sbReturn.append("\\n");
			}else if (sbVal.substring(i,i+1).equals("\r")) {
				sbReturn.append("\\r");
			}else {
				sbReturn.append(sbVal.substring(i,i+1));
			}
		}
		return sbReturn.toString();
	}
}
