/*
 q* Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.RequestUtils;

import jxl.write.WritableSheet;

import com.altum.fwk.web.support.AbstractJxlController;
import com.altum.jdbc.dao.support.ResultSetData;

public class ExportController extends AbstractJxlController {
	private boolean bProjectCoding;
	private boolean bQueryReport;
	@Override
	protected void init(HttpServletRequest request) throws Exception{
		if(request.getSession().getAttribute(ReportController.SESSION_REPORT_ID) == null)
			request.getSession().setAttribute(ReportController.SESSION_REPORT_ID, (int)( 1000000 + (Math.random() * 900000)));
		String sessionID = request.getSession().getAttribute(ReportController.SESSION_REPORT_ID).toString();
		String title = RequestUtils.getStringParameter(request,"title","");
		bProjectCoding = RequestUtils.getBooleanParameter(request, "projectCoding", false);
		bQueryReport = RequestUtils.getBooleanParameter(request, "queryReport", false);
		if(bProjectCoding){
			fileName = "ProjectCoding" + "("+sessionID+").xls";
		}else if(bQueryReport){
		    fileName = (title.substring(title.indexOf("FY")).replaceAll(",", " ").replaceAll("%", " ")).substring(0,Math.min(100, title.substring(title.indexOf("FY")).length()))+"("+sessionID+").xls";
		}else{
			fileName = title.substring(title.indexOf("FY")).replaceAll("&", "and").replaceAll("/", " ")+"("+sessionID+").xls";
		}
	}
	@Override
    protected void close(){
		bProjectCoding = false; //cleanup not actually necessary
    }	
	@Override
	protected void populateWorkbook(HttpServletRequest request) throws Exception {
		if(bProjectCoding){
			populateProjectCodingWorkbook(request);
		}else if(bQueryReport){
		    	populateQueryReportWorkbook(request);
		}else{
			populateComparisonReportWorkbook(request);
		}
		
	}	
	/**
	 * Populates RCDC Comparison report workbook
	 * @throws Exception
	 */
	public void populateQueryReportWorkbook(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		ResultSetData result = (ResultSetData)((List)session.getAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN)).get(0);
		List<Map<String,Object>> dataRows = result.getRows();
		String title = RequestUtils.getRequiredStringParameter(request,"title");
		WritableSheet sheet = createSheet("Query Report");
		String linkValue, linkUrl, linkDesc;
		int headerRow = -1;
		int[] columnDataTypes = new int[23];
		int[] columnWidths = new int[23];
		List<String> mappedNciTerms = (List<String>)session.getAttribute(ReportController.SESSION_NCI_TERMS); 
		//Generate title (query name)
		createMergedLabelCell(sheet, ++headerRow, 0, 21, title, CellFormatEnum.SHRINK_TITLE);
		//Blank Row
		++headerRow;
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Date Created", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, getFormattedDate(new Date()), CellFormatEnum.DATE, java.sql.Types.DATE);
		//User selected Parameters
		++headerRow;
		createMergedLabelCell(sheet, headerRow, 0, 1, "Search Parameters", CellFormatEnum.HEADER_SKY_BLUE);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Fiscal Year", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportFy"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		++headerRow;
		if(RequestUtils.getStringParameter(request, "reportInstitution")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Institution", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportInstitution"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportCA")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Cancer Activity", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportCA"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportSIC")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "SIC Code", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportSIC"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportCSO")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "CSO Code", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportCSO"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportPI")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Principal Investigator", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportPI"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportDivision")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Division", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportDivision"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportSITE")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "SITE Code", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportSITE"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportPDDesc")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "PD Description", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportPDDesc"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportSerial")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Serial Number", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportSerial"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		if(RequestUtils.getStringParameter(request, "reportRFAPA")!=null){
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "RFA/PA Number", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		    createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "reportRFAPA"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		    ++headerRow;
		}
		//createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Search Parameter", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		//createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "searchParameters"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		
		//Generate header row
		++headerRow;
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 0, "FY", java.sql.Types.NUMERIC, "Fiscal Year", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 1, "Application Type", java.sql.Types.VARCHAR, "Application Type", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 2, "Project Number", java.sql.Types.VARCHAR, "Project Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 3, "SubPrj ID", java.sql.Types.VARCHAR, "Subproject ID", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 4, "Serial Number", java.sql.Types.NUMERIC, "Serial Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 5, "Application Id", java.sql.Types.NUMERIC, "Application Id", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 6, "PI", java.sql.Types.VARCHAR, "Principal Investigator", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 7, "Institution", java.sql.Types.VARCHAR, "Institution", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 8, "ARRA", java.sql.Types.VARCHAR, "ARRA grant eligible flag", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 9, "Activity Code", java.sql.Types.VARCHAR, "Activity Code", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 10, "Project Title", java.sql.Types.VARCHAR, "Project Title", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 11, "Application Status Code", java.sql.Types.VARCHAR, "Application Status Code", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 12, "Application Status Description", java.sql.Types.VARCHAR, "Application Status Description", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 13, "Division", java.sql.Types.VARCHAR, "Division", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 14, "Program Director", java.sql.Types.VARCHAR, "Program Director", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 15, "Cancer Activity", java.sql.Types.VARCHAR, "Cancer Activity", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 16, "RFA-PA", java.sql.Types.VARCHAR, "RFA-PA", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 17, "Total NCI Obligated Amount", java.sql.Types.NUMERIC, "Total NCI Obligated Amount", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 18, "Code Type", java.sql.Types.VARCHAR, "Code Type", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 19, "Code Id", java.sql.Types.VARCHAR, "Code Id", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 20, "Code Name", java.sql.Types.VARCHAR, "Code Name", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 21, "% Rel", java.sql.Types.NUMERIC, "Percentage", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 22, "Related $", java.sql.Types.NUMERIC, "Related Dollars", 2, 1);
		
		//Generate data rows
		columnWidths = new int[]{10, 20, 15, 22, 12, 14, 25, 25, 12, 10, 70, 15, 30, 12, 30, 12, 12, 12, 7, 12, 12, 15, 20};
		for(int i=0; i<dataRows.size();i++){
			++headerRow;
			Map<String,Object> row = dataRows.get(i);
			List<String> colNames = result.getColumnNames();
			for(int j=0;j<colNames.size();j++){
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, j, row.get(colNames.get(j))!=null?row.get(colNames.get(j)).toString():"", colNames.get(j).indexOf("DOLLARS")>0?CellFormatEnum.DOLLARS:CellFormatEnum.NORMAL, colNames.get(j).indexOf("DOLLARS")>0?java.sql.Types.NUMERIC:java.sql.Types.VARCHAR);
			}
		}
		setVerticalFreezeAndColumnWidths(sheet, headerRow, columnWidths, true);
	}
	/**
	 * Populates RCDC Comparison report workbook
	 * @throws Exception
	 */
	public void populateComparisonReportWorkbook(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		ResultSetData result = (ResultSetData)((List)session.getAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN)).get(0);
		List<Map<String,Object>> dataRows = result.getRows();
		String title = RequestUtils.getRequiredStringParameter(request,"title");
		boolean bIncludeCoding = (result.getColumnNames().get(16).equals("CODE_NAME") || result.getColumnNames().get(17).equals("CODE_NAME"));
		boolean bBySerial = !result.getColumnNames().get(6).equals("APPL_ID");
		WritableSheet sheet = createSheet("RCDC Comparison Report");
		String linkValue, linkUrl, linkDesc;
		int headerRow = -1;
		int[] columnDataTypes = new int[23];
		int[] columnWidths = new int[23];
		List<String> mappedNciTerms = (List<String>)session.getAttribute(ReportController.SESSION_NCI_TERMS); 
		//Generate title (query name)
		createMergedLabelCell(sheet, ++headerRow, 0, bBySerial?bIncludeCoding?21:20:bIncludeCoding?22:21, title, CellFormatEnum.SHRINK_TITLE);
		//Blank Row
		++headerRow;
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Date Created", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, getFormattedDate(new Date()), CellFormatEnum.DATE, java.sql.Types.DATE);
		//User selected Parameters
		++headerRow;
		createMergedLabelCell(sheet, headerRow, 0, 1, "Search Parameters", CellFormatEnum.HEADER_SKY_BLUE);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Report Run By", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, bBySerial?"Serial Number":"Application ID", CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Fiscal Year", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, dataRows.size()>0?dataRows.get(0).get("FY").toString():"", CellFormatEnum.NUMBER, java.sql.Types.NUMERIC);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Project Type", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "projectType"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Source", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "source"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Disease Area", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, dataRows.size()>0?dataRows.get(0).get("DISEASE_CATEGORY").toString():"", CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Mechanism", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, RequestUtils.getRequiredStringParameter(request, "mechanism"), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		//Mapping Parameters
		++headerRow;
		createMergedLabelCell(sheet, headerRow, 0, 1, "Mapping Parameters", CellFormatEnum.HEADER_SKY_BLUE);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "NIH Term", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, "NCI Term", CellFormatEnum.BOLD, java.sql.Types.VARCHAR);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, (String)session.getAttribute(ReportController.SESSION_NIH_TERM), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
		for(int i=0; i<mappedNciTerms.size(); i++){
			createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, mappedNciTerms.get(i), CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
			++headerRow;
		}
		//Generate header row
		++headerRow;
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 0, "Disease Category", java.sql.Types.VARCHAR, "Disease Category", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 1, "FY", java.sql.Types.NUMERIC, "Fiscal Year", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 2, "Application Type", java.sql.Types.VARCHAR, "Application Type", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 3, "Project Number", java.sql.Types.VARCHAR, "Project Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 4, "SubPrj ID", java.sql.Types.VARCHAR, "Subproject ID", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 5, "Serial Number", java.sql.Types.NUMERIC, "Serial Number", 2, 1);
		if(!bBySerial){
			createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, 6, "Appl ID", java.sql.Types.NUMERIC, "Application ID", 2, 1);
		}
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?6:7, "ARRA", java.sql.Types.VARCHAR, "ARRA grant eligible flag", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?7:8, "Activity Code", java.sql.Types.VARCHAR, "Activity Code", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?8:9, "Project Title", java.sql.Types.VARCHAR, "Project Title", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?9:10, "Application Status Code", java.sql.Types.VARCHAR, "Application Status Code", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?10:11, "Application Status Description", java.sql.Types.VARCHAR, "Application Status Description", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?11:12, "Division", java.sql.Types.VARCHAR, "Division", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?12:13, "Cancer Activity", java.sql.Types.VARCHAR, "Cancer Activity", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?13:14, "RFA-PA", java.sql.Types.VARCHAR, "RFA-PA", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?14:15, "Program Director", java.sql.Types.VARCHAR, "Program Director", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?15:16, "Total NCI Obligated Amount", java.sql.Types.NUMERIC, "Total NCI Obligated Amount", 2, 1);
		if(bIncludeCoding){
		    createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?16:17, "Code Name", java.sql.Types.NUMERIC, "Code Name", 2, 1);
		}
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?(bIncludeCoding?17:16):(bIncludeCoding?18:17), "% Rel", java.sql.Types.NUMERIC, "Percentage", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?(bIncludeCoding?18:17):(bIncludeCoding?19:18), "Related $", java.sql.Types.NUMERIC, "Related Dollars", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?(bIncludeCoding?19:18):(bIncludeCoding?20:19), "Source", java.sql.Types.VARCHAR, "Source", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?(bIncludeCoding?20:19):(bIncludeCoding?21:20), "PDF Link", java.sql.Types.VARCHAR, "PDF Link", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_GREY, headerRow, columnDataTypes, columnWidths, bBySerial?(bIncludeCoding?21:20):(bIncludeCoding?22:21), "Extracted Text Link", java.sql.Types.VARCHAR, "Extracted Text Link", 2, 1);
		
		//Generate data rows
		++headerRow;
		if(bBySerial)
			columnWidths = new int[]{30, 10, 15, 22, 12, 12, 12, 10, 70, 15, 30, 12, 30, 12, 12, 12, 7, 12, 12, 15, 20, 20};
		else
			columnWidths = new int[]{30, 10, 15, 22, 12, 12, 12, 12, 10, 70, 15, 30, 12, 30, 12, 12, 12, 7, 12, 12, 15, 20, 20};
		for(int i=0; i<dataRows.size();i++){
			++headerRow;
			Map<String,Object> row = dataRows.get(i);
			List<String> colNames = result.getColumnNames();
			for(int j=0;j<colNames.size();j++){
				if(colNames.get(j).indexOf("LINK")>0){
					linkValue = row.get(colNames.get(j))!=null?row.get(colNames.get(j)).toString():"";
					linkUrl = linkValue.equals("")?"":linkValue.substring(linkValue.indexOf("\"")+1,linkValue.indexOf("\"", 15));
					linkDesc = linkValue.equals("")?"":linkValue.substring(linkValue.indexOf(">")+1,linkValue.indexOf("<", linkValue.indexOf(">")));
					//createHyperLinkCell(sheet, headerRow, columnWidths, j, linkUrl, linkDesc);
					//JAD050811 - Excel error with hyperlinks when a report has over 30000 rows
					createDataCell(sheet, headerRow, columnDataTypes, columnWidths, j, linkUrl, CellFormatEnum.NORMAL, java.sql.Types.VARCHAR);
					
				}else{
					createDataCell(sheet, headerRow, columnDataTypes, columnWidths, j, row.get(colNames.get(j))!=null?row.get(colNames.get(j)).toString():"", colNames.get(j).indexOf("DOLLARS")>0?CellFormatEnum.DOLLARS:CellFormatEnum.NORMAL, colNames.get(j).indexOf("DOLLARS")>0?java.sql.Types.NUMERIC:java.sql.Types.VARCHAR);
				}
			}
		}
		setVerticalFreezeAndColumnWidths(sheet, headerRow, columnWidths, true);
	}
	/**
	 * Populates Project Coding workbook
	 * @throws Exception
	 */
	public void populateProjectCodingWorkbook(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		ResultSetData result = (ResultSetData)((List)session.getAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN)).get(0);
		List<Map<String,Object>> dataRows = result.getRows();
		String title = RequestUtils.getRequiredStringParameter(request,"title");
		WritableSheet sheet = createSheet("Project Coding Report");
		int headerRow = -1;
		//Generate title (query name)
		createMergedLabelCell(sheet, ++headerRow, 0, 18, title, CellFormatEnum.SHRINK_TITLE);
		//Blank Row
		++headerRow;
		//Generate header row
		++headerRow;
		int[] columnDataTypes = new int[19];
		int[] columnWidths = new int[19];
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 0, "Application ID", java.sql.Types.NUMERIC, "Application ID", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 1, "Serial Number", java.sql.Types.NUMERIC, "Serial Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 2, "FY", java.sql.Types.NUMERIC, "Fiscal Year", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 3, "Project Number", java.sql.Types.VARCHAR, "Project Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 4, "Project Title", java.sql.Types.VARCHAR, "Project Title", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 5, "Institution", java.sql.Types.VARCHAR, "Institution", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 6, "City / State", java.sql.Types.VARCHAR, "City / State", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 7, "PI", java.sql.Types.VARCHAR, "Principal Investigator", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 8, "Program Director", java.sql.Types.VARCHAR, "Program Director", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 9, "Cancer Activity", java.sql.Types.VARCHAR, "Cancer Activity", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 10, "Division", java.sql.Types.VARCHAR, "Division", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 11, "Project Start Date", java.sql.Types.DATE, "Project Start Date", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 12, "Project End Date", java.sql.Types.DATE, "Project End Date", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 13, "Total NCI Obligated Amount", java.sql.Types.NUMERIC, "Total Obligated Amount", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 14, "Code Type", java.sql.Types.VARCHAR, "Code Type", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 15, "Code Number", java.sql.Types.VARCHAR, "Code Number", 2, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 16, "Code Name", java.sql.Types.VARCHAR, "Code Name", 1, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 17, "Percent Relevance", java.sql.Types.NUMERIC, "Percent Relevance", 1, 1);
		createHeaderCell(sheet, CellFormatEnum.HEADER_PALE_BLUE, headerRow, columnDataTypes, columnWidths, 18, "RCDC Codes", java.sql.Types.VARCHAR, "RCDC Codes", 2, 1);
		
		//Generate data rows
		++headerRow;
		columnWidths = new int[]{12, 10, 10, 20, 30, 30, 16, 18, 20, 15, 10, 15, 15, 15, 13, 12, 30, 14, 150};
		for(int i=0; i<dataRows.size();i++){
			++headerRow;
			Map<String,Object> row = dataRows.get(i);
			List<String> colNames = result.getColumnNames();
			for(int j=0;j<colNames.size();j++){
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, j, row.get(colNames.get(j))!=null?row.get(colNames.get(j)).toString():"", colNames.get(j).toUpperCase().indexOf("OBLIG")>0?CellFormatEnum.DOLLARS:CellFormatEnum.NORMAL, -1);
			}
		}
		setVerticalFreezeAndColumnWidths(sheet, headerRow, columnWidths, true);
	}
	/*public void populateProjectCodingWorkbook(HttpServletRequest request) throws Exception{
		List<ProjectCoding> projectCodingList = (List<ProjectCoding>)request.getAttribute("projectCodingList");
		WritableSheet sheet = createSheet("Coding Report");
		int headerRow = -1;
		//Generate title (query name)
		createMergedLabelCell(sheet, ++headerRow, 0, 2, "National Cancer Institute", CellFormatEnum.TITLE);
		createMergedLabelCell(sheet, ++headerRow, 0, 2, "Project Coding Report", CellFormatEnum.HEADER_PALE_BLUE);
		int[] columnDataTypes = new int[3];
		int[] columnWidths = new int[3];
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Prepared By", CellFormatEnum.NORMAL, -1);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, "KMSP/CSSI/NCI", CellFormatEnum.NORMAL, -1);
		++headerRow;
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Date Created", CellFormatEnum.NORMAL, -1);
		createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, getFormattedDate(new Date()), CellFormatEnum.NORMAL, -1);
		//Blank Row
		++headerRow;
		int currentApplID = 0;
		String currentType = "";
		List<String> rcdcCodeList = null;
		for(int i=0; i<projectCodingList.size(); i++){
			ProjectCoding pc = projectCodingList.get(i);
			if(currentApplID !=  pc.getApplID()){
				currentApplID = pc.getApplID();
				if(rcdcCodeList!=null && rcdcCodeList.size()>0){
					//Blank Row
					++headerRow;
					++headerRow;
					createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "RCDC Code Name", CellFormatEnum.BOLD, -1);
					for(int j=0;j<rcdcCodeList.size();j++){
						++headerRow;
						createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, rcdcCodeList.get(j), CellFormatEnum.WRAP, -1);
					}
				}
				rcdcCodeList = pc.getRcdcCodeList();
				++headerRow;
				//Blank Row
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "", CellFormatEnum.GRAY_BACKGROUND, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, "", CellFormatEnum.GRAY_BACKGROUND, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 2, "", CellFormatEnum.GRAY_BACKGROUND, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Project Number", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getProjNum(), CellFormatEnum.PALE_BLUE_BACKGROUND, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "FY", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getFy()+"", CellFormatEnum.NUMBER, java.sql.Types.NUMERIC);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Project Title", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getProjTitle(), CellFormatEnum.WRAP, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Institution", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getOrgName(), CellFormatEnum.WRAP, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "City / State", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, (pc.getCity()!=null?pc.getCity():"") + (((pc.getCity()!=null) && (pc.getState()!=null))?" / ":"") + (pc.getState()!=null?pc.getState():""), CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "PI", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getPi(), CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Program Director", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getPo(), CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Cancer Activity", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getCancerActivity(), CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Division", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getDivision(), CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Project Start Date", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getProjStartDate()!=null?getFormattedDate(pc.getProjStartDate()):"", CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Project End Date", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getProjEndDate()!=null?getFormattedDate(pc.getProjEndDate()):"", CellFormatEnum.NORMAL, -1);
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "Total Obligated Amount", CellFormatEnum.NORMAL, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getAmountOblig()+"", CellFormatEnum.DOLLARS, java.sql.Types.NUMERIC);
			}
			if(!currentType.equals( pc.getTypeId()) && pc.getTypeId()!=null){
				//Blank Row
				++headerRow;
				
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, (pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Code Number", CellFormatEnum.BOLD, -1);
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, (pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Code Name", CellFormatEnum.BOLD, -1);
				if(pc.getTypeId()!=null && !pc.getTypeId().equals("CSO"))createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 2, (pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Percent Relevance", CellFormatEnum.BOLD, -1);
				currentType = pc.getTypeId();
			}
			++headerRow;
			createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, pc.getCode(), CellFormatEnum.NORMAL, -1);
			createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 1, pc.getCodeName(), CellFormatEnum.WRAP, -1);
			if(pc.getTypeId()!=null && !pc.getTypeId().equals("CSO"))createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 2, (Float.valueOf(pc.getPercentRel()==null?"0":pc.getPercentRel()).floatValue()/100)+"", CellFormatEnum.PERCENT, java.sql.Types.NUMERIC);
			
		}
		if(rcdcCodeList!=null && rcdcCodeList.size()>0){
			//Blank Row
			++headerRow;
			++headerRow;
			createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, "RCDC Code Name", CellFormatEnum.BOLD, -1);
			for(int j=0;j<rcdcCodeList.size();j++){
				++headerRow;
				createDataCell(sheet, headerRow, columnDataTypes, columnWidths, 0, rcdcCodeList.get(j), CellFormatEnum.WRAP, -1);
			}
		}
		columnWidths = new int[]{40, 40, 40};
		setVerticalFreezeAndColumnWidths(sheet, headerRow, columnWidths, true);
	}*/
	 private String getFormattedDate(Date date) {
	        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
	        return dateFormat.format(date);
	    } 
}
