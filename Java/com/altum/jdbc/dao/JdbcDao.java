/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.altum.beans.Code;
import com.altum.beans.ProjectCoding;
import com.altum.jdbc.dao.support.ResultSetData;

public interface JdbcDao {
	
	/**
	 * Executes the sql and returns the ResultSetData with columnNames and row data.
	 * @param sql
	 * @return ResultSetData
	 */
	ResultSetData getResultSetData(String sql) throws SQLException;

	/**
	 * creates temp tables
	 * @param sql
	 * @return boolean
	 */
	void createTable(String sql) throws SQLException;
	
	/**
	 * inserts data into the temp table created
	 * @param sql
	 * @throws SQLException
	 */
	void insertTableData(String sql) throws SQLException;
	
	/**
	 * updates table data
	 * @param sql
	 * @throws SQLException
	 */	
	void updateTableData(String sql) throws SQLException;
	
	/**
	 * drops the temp tables
	 * @param sql
	 * throws SQLException
	 */
	void dropTable(String sql) throws SQLException;
	
	/**
	 * truncates the session table
	 * @param sql
	 * throws SQLException
	 */
	void truncateTable(String sql) throws SQLException;
	/**
	 * returns list of FY used by the RCDC report
	 */
	List<String> getReportFY();
	/**
	 * returns list of Disease Categories used by the RCDC report
	 */
	List<String> getReportDisease();
	/**
	 * returns list of Mechanisms used by the RCDC report
	 */
	List<String> getReportMechanism();
	/**
	 * returns list of Cancer Activities used by the RCDC report
	 */
	List<String> getCancerActivity();
	/**
	 * returns list of PD descriptions used by the RCDC report
	 */
	List<String> getPDDescription();
	/**
	 * returns list of RFA/PA numbers used by the RCDC report
	 */
	List<String> getRFAPANumber();
	/**
	 * returns list of Divisions used by the RCDC report
	 */
	List<String> getDivision();
	/**
	 * returns list of NCI codes used by the RCDC report
	 */
	List<Code> getReportNCICodes();
	/**
	 * inserts custom OB mapping
	 */
	void insertOBMapping(Map<String,String> paramMap);
	/**
	 * delete custom OB mapping
	 */
	void deleteOBMapping(Map<String,String> paramMap);
	/**
	 * checks if the custom name is already used
	 */
	String isCustomNameTaken(Map<String,String> paramMap);
	/**
	 * returns project coding list 
	 */
	List<ProjectCoding> getProjectCoding(List<Integer> applIDorSerial, boolean runBySerialNum);
	/**
	 * returns list of nci terms for given ob name
	 */
	List<String> getMappedNciTermsForObName(Map<String,String> paramMap);
	/**
	 * returns list of custom NIH terms
	 */
	List<String> getCustomNihTerms();
	/**
	 * deletes the custom NIH term passed as a parameter
	 */
	void deleteCustomTerm(String custName);
	/**
	 * get the NIH term for custom name
	 */
	String getNihTermForCustomName(String custName);
	/**
	 * returns list of nci terms for given custom name
	 */
	List<String> getMappedNciTermsForCustomName(String custName);
	/**
	 * get the last refresh date
	 */
	String getLastRefreshDate();
	/**
	 * get the link acct days left
	 */
	String getLinkAcctDaysLeft();
}
