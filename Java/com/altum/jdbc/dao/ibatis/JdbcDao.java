/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.dao.ibatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.altum.beans.Code;
import com.altum.beans.ProjectCoding;
import com.altum.jdbc.dao.support.ResultSetData;

public class JdbcDao extends SqlMapClientDaoSupport implements com.altum.jdbc.dao.JdbcDao {
	
	/* Non-Javadoc: see interface. */
	public ResultSetData getResultSetData(String sql) throws SQLException{
		Connection con = null;
		Statement stmt = null;
		try {
			con = getDataSource().getConnection();
			stmt = con.createStatement();
			return new ResultSetData(stmt.executeQuery(sql));
		} finally {
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		}
	}
	/* Non-Javadoc: see interface. */
	public void dropTable(String sql) throws SQLException{
		execute(sql,true);
	}
	/* Non-Javadoc: see interface. */
	public void createTable(String sql) throws SQLException{
		execute(sql,true);
	}
	/* Non-Javadoc: see interface. */
	public void insertTableData(String sql) throws SQLException{
		execute(sql,false);
	}
	/* Non-Javadoc: see interface. */	
	public void updateTableData(String sql) throws SQLException{
		execute(sql,false);
	}
	/* Non-Javadoc: see interface. */
	public void truncateTable(String sql) throws SQLException{
		execute(sql,false);
	}
	public List<String> getReportFY(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getReportFY", null);
	}
	public List<String> getReportDisease(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getReportDisease", null);
	}
	public List<String> getReportMechanism(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getReportMechanism", null);
	}
	public List<String> getCancerActivity(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getCancerActivity", null);
	}
	public List<String> getDivision(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getDivision", null);
	}
	public List<String> getPDDescription(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getPDDescription", null);
	}
	public List<String> getRFAPANumber(){
		return (List<String>)getSqlMapClientTemplate().queryForList("getRfaPaNumber", null);
	}
	public List<Code> getReportNCICodes(){
		return (List<Code>)getSqlMapClientTemplate().queryForList("getReportNCICodes", null);
	}
	public void insertOBMapping(Map<String,String> paramMap){
		getSqlMapClientTemplate().insert("insertOBMapping", paramMap);
	}
	public void deleteOBMapping(Map<String,String> paramMap){
		getSqlMapClientTemplate().insert("deleteOBMapping", paramMap);
	}
	public String isCustomNameTaken(Map<String,String> paramMap){
		return (String)getSqlMapClientTemplate().queryForObject("isCustomNameTaken", paramMap);
	}
	public List<ProjectCoding> getProjectCoding(List<Integer> applIDorSerial, boolean runBySerialNum){
		HashMap param = new HashMap();
		param.put("applIDorSerial", applIDorSerial);
		if(runBySerialNum){
			param.put("runBySerialNum", runBySerialNum);
		}
		return getSqlMapClientTemplate().queryForList("getProjectCoding", param);
	}
	public List<String> getMappedNciTermsForObName(Map<String,String> paramMap){
		return getSqlMapClientTemplate().queryForList("getMappedNciTermsForObName", paramMap);
	}
	public List<String> getCustomNihTerms(){
		return getSqlMapClientTemplate().queryForList("getCustomNihTerms", null);
	}
	public void deleteCustomTerm(String custName){
		getSqlMapClientTemplate().delete("deleteCustomTerm", custName);
	}
	public String getNihTermForCustomName(String custName){
		return (String)getSqlMapClientTemplate().queryForObject("getNihTermForCustomName", custName);
	}
	public List<String> getMappedNciTermsForCustomName(String custName){
		return getSqlMapClientTemplate().queryForList("getMappedNciTermsForCustomName", custName);
	}
	public 	String getLastRefreshDate(){
	    return (String)getSqlMapClientTemplate().queryForObject("getLastRefreshDate");
	}
	public String getLinkAcctDaysLeft(){
	    return (String)getSqlMapClientTemplate().queryForObject("getLinkAcctDaysLeft");
	}
	/**
	 * To run DML, set ddl arg to false
	 * @param sql
	 * @param ddl
	 * @throws SQLException
	 */
	private void execute(String sql, boolean ddl) throws SQLException{
		if(sql==null || sql.length()==0){
			throw new IllegalArgumentException("SQL argument must not be null or empty");
		}
		Connection con = null;
		Statement stmt = null;
		try{
			con = getDataSource().getConnection();
			stmt = con.createStatement();
			if(ddl){
				stmt.execute(sql);
			}else{
				stmt.executeUpdate(sql);
			}
		}finally{
			if(stmt!=null) stmt.close();
			if(con != null) con.close();
		}
	}
}
