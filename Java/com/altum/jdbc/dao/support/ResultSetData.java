/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.dao.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.CLOB;

public class ResultSetData {
	/** The column names, in order */
	private List<String> columnNames;
	/** Each row is a map of keys=columnNames, values=cellData */
	private List<Map<String,Object>> rows;

	public ResultSetData(){}
	public ResultSetData(List<String> columnNames, List<Map<String,Object>> rows){
		this.columnNames = columnNames;
		this.rows = rows;
	}
	public ResultSetData(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		columnNames = new ArrayList<String>(numColumns);
		rows = new ArrayList<Map<String,Object>>();
		// Get the column names; column indices start from 1
		for (int i=0; i<numColumns; ) {
			columnNames.add(rsmd.getColumnName(++i));
		}
		Map<String,Object> row;
		Object o;
		while (rs.next()){
			row = new HashMap<String,Object>();
			for(String columnName : columnNames){
				o=rs.getObject(columnName);
				row.put(columnName, (o instanceof CLOB ? rs.getString(columnName).trim() : o ));
			}
			if(row.size() != 0){
				rows.add(row);
			}
		}
	}
	public List<String> getColumnNames() {
		return columnNames;
	}
	public String[] getColumnNamesArray(){
		return columnNames.toArray(new String[columnNames.size()]);
	}
	public List<Map<String, Object>> getRows() {
		return rows;
	}
}
