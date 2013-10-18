/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.coding.data;
/** 
 * As common tokens, these make for poor session/application keys, 
 * and should only be used for request keys (and other map keys). 
 * All tokens below are expected to have distinct but arbitrary values. 
*/
public class CommonTokens {
	
	public final static String DATA_TOKEN = "DATA_TOKEN";
	public final static String TABLES_TOKEN = "TABLES_TOKEN";
	/** key for EnumMap< JmesaKeys,Object> */
	public final static String JMESA_MAP_TOKEN = "JMESA_MAP_TOKEN";
	/** key for EnumMap< SqlCommandKeys,Object> */
	public final static String SQL_COMMAND_MAP_TOKEN = "SQL_COMMAND_MAP_TOKEN";

}
