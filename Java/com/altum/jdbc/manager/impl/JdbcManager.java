/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.manager.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.altum.beans.Code;
import com.altum.beans.ProjectCoding;
import com.altum.jdbc.dao.JdbcDao;
import com.altum.jdbc.dao.support.ResultSetData;

public class JdbcManager implements com.altum.jdbc.manager.JdbcManager {
	/** Typically injected via Spring */
	private JdbcDao jdbcDao;
	
	/* Non-Javadoc: see interface. */
	public ResultSetData getResultSetData(String sql) throws SQLException{
		return jdbcDao.getResultSetData(sql);
	}

	/* Non-Javadoc: see interface. */
	public void dropCreateAndFillTables(String[] dropCommands, String[] createCommands, String[] insertCommands, String[] updateCommands) throws SQLException{
		dropCreateTables(dropCommands, createCommands);
		insertTablesData(insertCommands);
		updateTablesData(updateCommands);
	}

	/* Non-Javadoc: see interface. */
	public void truncateAndFillTables(String[] truncateCommands, String[] insertCommands, String[] updateCommands) throws SQLException{
		truncateTables(truncateCommands);
		insertTablesData(insertCommands);
		updateTablesData(updateCommands);
	}

	/* Non-Javadoc: see interface. */
	public void dropCreateTables(String[] dropCommands, String[] createCommands) throws SQLException{
		if(dropCommands!=null){
			for(String sql : dropCommands){
				try {
					jdbcDao.dropTable(sql);
				} catch (SQLException se) {}// We don't care if the table didn't already exist.
			}
		}
		if(createCommands!=null){
			for(String sql : createCommands){
				jdbcDao.createTable(sql);
			}
		}
	}
	
	/* Non-Javadoc: see interface. */
	public void insertTablesData(String[] insertCommands) throws SQLException{
		if(insertCommands!=null){
			for(String sql : insertCommands){
				jdbcDao.insertTableData(sql);
			}
		}
	}
	
	/* Non-Javadoc: see interface. */
	public void updateTablesData(String[] updateCommands) throws SQLException{
		if(updateCommands!=null){
			for(String sql : updateCommands){
				jdbcDao.updateTableData(sql);
			}
		}
	}
	
	/* Non-Javadoc: see interface. */
	public void truncateTables(String[] truncateCommands) throws SQLException{
		if(truncateCommands!=null){
			for(String sql : truncateCommands){
				jdbcDao.truncateTable(sql);
			}
		}
	}
	
	public List<String> getReportFY(){
		return jdbcDao.getReportFY();
	}
	public List<String> getReportDisease(){
		return jdbcDao.getReportDisease();
	}
	public List<String> getReportMechanism(){
		return jdbcDao.getReportMechanism();
	}
	public List<String> getCancerActivity(){
		return jdbcDao.getCancerActivity();
	}
	public List<String> getDivision(){
		return jdbcDao.getDivision();
	}
	public List<String> getPDDescription(){
		return jdbcDao.getPDDescription();
	}
	public List<String> getRFAPANumber(){
		return jdbcDao.getRFAPANumber();
	}
	public List<Code> getReportNCICodes(){
		return jdbcDao.getReportNCICodes();
	}
	public void insertOBMapping(Map<String,String> paramMap){
		jdbcDao.insertOBMapping(paramMap);
	}
	public void deleteOBMapping(Map<String,String> paramMap){
		jdbcDao.deleteOBMapping(paramMap);
	}
	public String isCustomNameTaken(Map<String,String> paramMap){
		return jdbcDao.isCustomNameTaken(paramMap);
	}
	public List<ProjectCoding> getProjectCoding(List<Integer> applIDorSerial, boolean runBySerialNum){
		return jdbcDao.getProjectCoding(applIDorSerial, runBySerialNum);
	}
	public List<String> getMappedNciTermsForObName(Map<String,String> paramMap){
		return jdbcDao.getMappedNciTermsForObName(paramMap);
	}
	public List<String> getCustomNihTerms(){
		return jdbcDao.getCustomNihTerms();
	}
	public void deleteCustomTerm(String custName){
		jdbcDao.deleteCustomTerm(custName);
	}
	public String getNihTermForCustomName(String custName){
		return jdbcDao.getNihTermForCustomName(custName);
	}
	public String getLastRefreshDate(){
	    return jdbcDao.getLastRefreshDate();
	}
	public String getLinkAcctDaysLeft(){
	    return jdbcDao.getLinkAcctDaysLeft();
	}
	public List<String> getMappedNciTermsForCustomName(String custName){
		return jdbcDao.getMappedNciTermsForCustomName(custName);
	}
	/** Typically injected via Spring */
	public void setJdbcDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
}
