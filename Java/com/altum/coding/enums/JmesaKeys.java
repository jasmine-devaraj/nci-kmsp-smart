/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.coding.enums;

public enum JmesaKeys {
	/** String */
	TABLE_ID,
	/** List< JmesaColumnProperties> - can be null */
	COLUMN_PROPERTIES,
	/** String - can be null */
	CAPTION,
	/** ExportType... exportTypes */
	EXPORT_TYPES,
	/** File Name & Sheet Name(Used in export) - cannot exceed 31 characters */
	FILE_NAME
}
