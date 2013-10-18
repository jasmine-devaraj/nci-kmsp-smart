/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.web;

import java.awt.Color;
import java.io.FileOutputStream; 
import java.io.IOException; 

import com.lowagie.text.Cell;
import com.lowagie.text.Document; 
import com.lowagie.text.DocumentException; 
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph; 
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.altum.beans.ProjectCoding;
import com.altum.coding.web.support.UtilityController;
import com.altum.jdbc.manager.JdbcManager;

public class ReportController extends UtilityController {
	
	/** Key to a SESSION Param which implies that the result is stored in session */
	public final static String SESSION_REPORT_FY = "SESSION_REPORT_FY";
	public final static String SESSION_REPORT_DISEASE = "SESSION_REPORT_DISEASE";
	public final static String SESSION_REPORT_MECHANISM = "SESSION_REPORT_MECHANISM";
	public final static String SESSION_CANCER_ACTIVITY = "SESSION_CANCER_ACTIVITY";
	public final static String SESSION_REPORT_DIVISION = "SESSION_REPORT_DIVISION";
	public final static String SESSION_PD_DESCRIPTION = "SESSION_PD_DESCRIPTION";
	public final static String SESSION_RFAPA_NUMBER = "SESSION_RFAPA_NUMBER";
	public final static String SESSION_REPORT_NCI_CODES = "SESSION_REPORT_NCI_CODES";
	public final static String SESSION_REPORT_ID = "SESSION_REPORT_ID";
	public final static String SESSION_PROJECT_CODING_LIST = "SESSION_PROJECT_CODING_LIST";
	public final static String SESSION_NIH_TERM = "SESSION_NIH_TERM";
	public final static String SESSION_NCI_TERMS = "SESSION_NCI_TERMS";
	public static final String DISEASE_SELECT_VIEW_NAME = "report.rcdcCompare";
	public static final String NCI_TERMS_VIEW_NAME = "report.includeNciTerms";
	public static final String MANAGE_CUSTOM_MAPPING_VIEW_NAME = "report.manageCustomMapping";
	public static final String CUSTOMIZE_MAPPING_VIEW_NAME = "report.customizeMapping";
	public static final String PROJECT_CODING_REPORT_VIEW_NAME = "report.projectCodingReport";
	public static final String PROJECT_CODING_EXPORT_VIEW_NAME = "exportProjectCodingToExcel";
	public static final String RCDC_COMPARISON_PARAMETER_VIEW_NAME = "report.rcdcCompareReportParameter";
	public static final String QUERY_REPORT_PARAMETER_VIEW_NAME = "report.queryReportParameter";

	/** Typically injected via Spring */
	private JdbcManager jdbcManager;
	
	public ModelAndView runRcdcComparisonReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		cacheReportParameters(request);
		return new ModelAndView(RCDC_COMPARISON_PARAMETER_VIEW_NAME, new HashMap());
	}
	public ModelAndView runQueryReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		cacheReportParameters(request);
		return new ModelAndView(QUERY_REPORT_PARAMETER_VIEW_NAME, new HashMap());
	}
	private void cacheReportParameters(HttpServletRequest request){
		if(request.getSession().getAttribute(SESSION_REPORT_FY) == null)
			request.getSession().setAttribute(SESSION_REPORT_FY, jdbcManager.getReportFY());
		if(request.getSession().getAttribute(SESSION_REPORT_DISEASE) == null)
			request.getSession().setAttribute(SESSION_REPORT_DISEASE, jdbcManager.getReportDisease());
		if(request.getSession().getAttribute(SESSION_REPORT_MECHANISM) == null)
			request.getSession().setAttribute(SESSION_REPORT_MECHANISM, jdbcManager.getReportMechanism());
		if(request.getSession().getAttribute(SESSION_CANCER_ACTIVITY) == null)
			request.getSession().setAttribute(SESSION_CANCER_ACTIVITY, jdbcManager.getCancerActivity());
		if(request.getSession().getAttribute(SESSION_REPORT_DIVISION) == null)
			request.getSession().setAttribute(SESSION_REPORT_DIVISION, jdbcManager.getDivision());
		if(request.getSession().getAttribute(SESSION_PD_DESCRIPTION) == null)
			request.getSession().setAttribute(SESSION_PD_DESCRIPTION, jdbcManager.getPDDescription());
		if(request.getSession().getAttribute(SESSION_RFAPA_NUMBER) == null)
			request.getSession().setAttribute(SESSION_RFAPA_NUMBER, jdbcManager.getRFAPANumber());
		if(request.getSession().getAttribute(SESSION_REPORT_NCI_CODES) == null)
			request.getSession().setAttribute(SESSION_REPORT_NCI_CODES, jdbcManager.getReportNCICodes());
		if(request.getSession().getAttribute(SESSION_REPORT_ID) == null)
			request.getSession().setAttribute(SESSION_REPORT_ID, (int)( 1000000 + (Math.random() * 900000)));
	}
	public ModelAndView saveMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Map<String,String> paramMap = new HashMap();
		String custName = request.getParameter("custName");
		String ob_name = request.getParameter("nih_name");
		String oldCustomName = request.getParameter("oldCustomName");
		paramMap.put("custName", custName + " (C)");
		paramMap.put("ob_name", ob_name);
		if(jdbcManager.isCustomNameTaken(paramMap).equals("Y")) return null;
		if(oldCustomName.trim()!=""){
			jdbcManager.deleteCustomTerm(oldCustomName);
		}
		String codes = request.getParameter("nci_names");
		StringTokenizer st = new StringTokenizer(codes, ";");
		while(st.hasMoreTokens()){
			String code = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(code, ",");
			String s = st2.nextToken();
			paramMap.put("org_id", s.substring(s.indexOf(":")+1));
			s = st2.nextToken();
			paramMap.put("type_id", s.substring(s.indexOf(":")+1));
			s = st2.nextToken();
			paramMap.put("code_id", s.substring(s.indexOf(":")+1));
			jdbcManager.insertOBMapping(paramMap);
		}
		request.getSession().setAttribute(SESSION_REPORT_DISEASE, jdbcManager.getReportDisease());
		//model.put("customDisease", ob_name.indexOf("(C)")>-1?ob_name:ob_name + " (C)");
		model.put("customDisease", custName + " (C)");
		return new ModelAndView(DISEASE_SELECT_VIEW_NAME, model);
	}
	public ModelAndView getMappedNciTermsForObName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Map<String,String> paramMap = new HashMap();
		String ob_name = request.getParameter("nih_name");
		paramMap.put("ob_name", ob_name);
		List<String> mappedNciTerms = jdbcManager.getMappedNciTermsForObName(paramMap);
		model.put("mappedNciTerms", mappedNciTerms);
		return new ModelAndView(NCI_TERMS_VIEW_NAME, model);
	}
	public ModelAndView manageCustomMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		cacheReportParameters(request);
		List<String> customNihTerms = jdbcManager.getCustomNihTerms();
		model.put("customNihTerms", customNihTerms);
		return new ModelAndView(MANAGE_CUSTOM_MAPPING_VIEW_NAME, model);
	}
	public ModelAndView deleteCustomTerm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String custName = request.getParameter("custName");
		jdbcManager.deleteCustomTerm(custName);
		request.getSession().setAttribute(SESSION_REPORT_DISEASE, jdbcManager.getReportDisease());
		return manageCustomMapping(request, response);
	}
	public ModelAndView editCustomTerm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		cacheReportParameters(request);
		String custName = request.getParameter("custName");
		String nihTerm = jdbcManager.getNihTermForCustomName(custName);
		List<String> mappedNciTerms = jdbcManager.getMappedNciTermsForCustomName(custName);
		model.put("oldCustomName", custName);
		model.put("nihTerm", nihTerm);
		model.put("mappedNciTerms", mappedNciTerms);
		return new ModelAndView(CUSTOMIZE_MAPPING_VIEW_NAME, model);
	}
	public ModelAndView getMappingParameters(HttpServletRequest request, HttpServletResponse response) throws Exception {
		cacheReportParameters(request);
		String disease = request.getParameter("disease");
		if(disease.indexOf("(C)")>-1){
			request.getSession().setAttribute(SESSION_NIH_TERM, jdbcManager.getNihTermForCustomName(disease));
			request.getSession().setAttribute(SESSION_NCI_TERMS, jdbcManager.getMappedNciTermsForCustomName(disease));
		}else{
			request.getSession().setAttribute(SESSION_NIH_TERM, disease);
			Map<String,String> paramMap = new HashMap();
			paramMap.put("ob_name", disease);
			request.getSession().setAttribute(SESSION_NCI_TERMS, jdbcManager.getMappedNciTermsForObName(paramMap));
		}
		return null;
	}
	public ModelAndView runProjectCodingReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		cacheReportParameters(request);
		String applIDOrSerial = request.getParameter("applIDOrSerial");
		String runBy = request.getParameter("runBy");
		boolean bRunBySerial = (runBy!=null)&&(runBy.indexOf("Serial")>-1);
		StringTokenizer st = new StringTokenizer(applIDOrSerial, ",");
		List<Integer> applIDOrSerialList = new ArrayList();
	    while (st.hasMoreTokens()) {
	    	applIDOrSerialList.add(Integer.valueOf(st.nextToken().trim()));
	    }
		List<ProjectCoding> projectCodingList = jdbcManager.getProjectCoding(applIDOrSerialList, bRunBySerial);
		model.put("projectCodingList", projectCodingList);
		request.getSession().setAttribute(SESSION_PROJECT_CODING_LIST, projectCodingList);
		if(applIDOrSerialList.size()>1){
			return new ModelAndView(PROJECT_CODING_EXPORT_VIEW_NAME, model);
		}else{
			model.put("runBy", runBy);
			return new ModelAndView(PROJECT_CODING_REPORT_VIEW_NAME, model);
		}
	}
	public ModelAndView exportProjectCodingReportToWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> model = new HashMap<String, Object>();
		
		if(request.getSession().getAttribute(ReportController.SESSION_REPORT_ID) == null)
			request.getSession().setAttribute(ReportController.SESSION_REPORT_ID, (int)( 1000000 + (Math.random() * 900000)));
		String sessionID = request.getSession().getAttribute(ReportController.SESSION_REPORT_ID).toString();
		String fileName = "ProjectCoding" + "("+sessionID+").rtf"; 
		List<ProjectCoding> projectCodingList = (List<ProjectCoding>)request.getSession().getAttribute(SESSION_PROJECT_CODING_LIST);
		
		StringBuffer sbDocWriteFile = new StringBuffer();
		String sDir = getServletContext().getRealPath("/");
		char cTestVal = sDir.charAt(sDir.length()-1);
        if ('\\' != cTestVal) {
            sDir += "/";
        }
		sbDocWriteFile.append(sDir);
		sbDocWriteFile.append("exports/");
		sbDocWriteFile.append(fileName);
		
		Document document = new Document(); 
        try { 
        		
        		RtfWriter2.getInstance(document, new FileOutputStream(sbDocWriteFile.toString())); 
                document.open(); 
                // Register, create and set fonts
                FontFactory.register(sDir + "fonts/" + "verdana.ttf");
                FontFactory.register(sDir + "fonts/" + "arial.ttf");
                RtfParagraphStyle.STYLE_NORMAL.setFontName("verdana");
                RtfParagraphStyle.STYLE_NORMAL.setSize(10);
                Font arial8 = new Font(FontFactory.getFont("arial"));
                arial8.setSize(10);
                arial8.setColor(Color.BLACK);
                
                Font arialBold10 = new Font(FontFactory.getFont("arial"));
                arialBold10.setSize(10);
                arialBold10.setStyle(Font.BOLD);
                arialBold10.setColor(Color.BLACK);
                
                Font arialRedBold14 = new Font(FontFactory.getFont("arial"));
                arialRedBold14.setSize(14);
                arialRedBold14.setColor(new Color(0x99, 0x00, 0x00));
                arialRedBold14.setStyle(Font.BOLD);
                
                Font arialBold14 = new Font(FontFactory.getFont("arial"));
                arialBold14.setSize(14);
                arialBold14.setStyle(Font.BOLD);

                Paragraph paragraph;
                Cell cell;
               
               
                paragraph = new Paragraph("National Cancer Institute", arialBold14);
                paragraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(paragraph);
                paragraph = new Paragraph("");
                document.add(paragraph);
                paragraph = new Paragraph("PROJECT CODING REPORT", arialRedBold14);
                paragraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(paragraph);
                
                int[] cellWidths1 = new int[]{33, 33, 34};

                Table t1 = new Table(3);
                t1.setBorderWidth(0);
                t1.setWidth(100);
                t1.setWidths(cellWidths1);
                t1.setSpacing(4);
                
                paragraph = new Paragraph("Prepared by", arial8);
                cell = new Cell(paragraph);
                cell.setBorder(0);
                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
                t1.addCell(cell);
                
                paragraph = new Paragraph("KMSP/CSSI/NCI", arial8);
                cell = new Cell(paragraph);
                cell.setBorder(0);
                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
                cell.setColspan(2);
                t1.addCell(cell);
                
                paragraph = new Paragraph("Date Created", arial8);
                cell = new Cell(paragraph);
                cell.setBorder(0);
                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
                t1.addCell(cell);
                
                paragraph = new Paragraph(getFormattedDate(new Date()), arial8);
                cell = new Cell(paragraph);
                cell.setBorder(0);
                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
                cell.setColspan(2);
                t1.addCell(cell);
                
                document.add(t1);
                
                int[] cellWidths2 = new int[]{33, 33, 34};

                Table t2 = new Table(3);
                t2.setBorderWidth(0);
                t2.setWidth(100);
                t2.setWidths(cellWidths2);
                t2.setSpacing(4);
                
                int currentApplID = 0;
        		String currentType = "";
        		List<String> rcdcCodeList = null;
        		for(int i=0; i<projectCodingList.size(); i++){
        			ProjectCoding pc = projectCodingList.get(i);
        			if(currentApplID !=  pc.getApplID()){
        				currentApplID = pc.getApplID();
        				if(rcdcCodeList!=null && rcdcCodeList.size()>0){
        					//Blank cell
            				paragraph = new Paragraph("", arial8);
        	                cell = new Cell(paragraph);
        	                cell.setBorder(0);
        	                t2.addCell(cell);
        	                
        					paragraph = new Paragraph("RCDC Code Name", arialBold10);
        	                cell = new Cell(paragraph);
        	                cell.setBorder(0);
        	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
        	                cell.setColspan(2);
        	                t2.addCell(cell);
        	                
        					for(int j=0;j<rcdcCodeList.size();j++){
        						//Blank cell
                				paragraph = new Paragraph("", arial8);
            	                cell = new Cell(paragraph);
            	                cell.setBorder(0);
            	                t2.addCell(cell);
            	                
        						paragraph = new Paragraph(rcdcCodeList.get(j), arial8);
            	                cell = new Cell(paragraph);
            	                cell.setBorder(0);
            	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
            	                cell.setColspan(2);
            	                t2.addCell(cell);
        					}
        				}
        				rcdcCodeList = pc.getRcdcCodeList();

        				//Blank Row
        				paragraph = new Paragraph("", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setColspan(3);
    	                cell.setBackgroundColor(Color.LIGHT_GRAY);
    	                t2.addCell(cell);
    	                
        				paragraph = new Paragraph("Project Number", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getProjNum(), arialBold10);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph("Application ID", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getApplID()+"", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("FY", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getFy()+"", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Project Title", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_TOP);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getProjTitle(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Institution", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getOrgName(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("City / State", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph((pc.getCity()!=null?pc.getCity():"") + (((pc.getCity()!=null) && (pc.getState()!=null))?" / ":"") + (pc.getState()!=null?pc.getState():""), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("PI", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getPi(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Program Director", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getPo(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Cancer Activity", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getCancerActivity(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Division", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getDivision(), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Project Start Date", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getProjStartDate()!=null?getFormattedDate(pc.getProjStartDate()):"", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        				
        				paragraph = new Paragraph("Project End Date", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getProjEndDate()!=null?getFormattedDate(pc.getProjEndDate()):"", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
    	                
        				paragraph = new Paragraph("Total NCI Obligated Amount", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph(pc.getAmountOblig()+"", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
        			}
        			if(!currentType.equals( pc.getTypeId()) && pc.getTypeId()!=null){
        				paragraph = new Paragraph((pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Code Number", arialBold10);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                t2.addCell(cell);
    	                
    	                paragraph = new Paragraph((pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Code Name", arialBold10);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                if(pc.getTypeId()!=null && pc.getTypeId().equals("CSO")) cell.setColspan(2);
    	                t2.addCell(cell);
    	                
        				if(pc.getTypeId()!=null && !pc.getTypeId().equals("CSO")){
        					paragraph = new Paragraph((pc.getTypeId().equals("SITE")?"Site":(pc.getTypeId().equals("SIC")?"Sic":pc.getTypeId())) + " Percent Relevance", arialBold10);
        	                cell = new Cell(paragraph);
        	                cell.setBorder(0);
        	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
        	                t2.addCell(cell);
        				}
        				currentType = pc.getTypeId();
        			}
        			if(pc.getCode()!=null){
	        			paragraph = new Paragraph(pc.getCode(), arial8);
		                cell = new Cell(paragraph);
		                cell.setBorder(0);
		                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
		                t2.addCell(cell);
		                
	        			paragraph = new Paragraph(pc.getCodeName(), arial8);
		                cell = new Cell(paragraph);
		                cell.setBorder(0);
		                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
		                if(pc.getTypeId()!=null && pc.getTypeId().equals("CSO")) cell.setColspan(2);
		                t2.addCell(cell);
		                
	        			if(pc.getTypeId()!=null && !pc.getTypeId().equals("CSO")) {
	        				paragraph = new Paragraph(pc.getPercentRel()+"", arial8);
	    	                cell = new Cell(paragraph);
	    	                cell.setBorder(0);
	    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
	    	                t2.addCell(cell);
	        			}
        			}
        		}
        		if(rcdcCodeList!=null && rcdcCodeList.size()>0){
        			//Blank cell
    				paragraph = new Paragraph("", arial8);
	                cell = new Cell(paragraph);
	                cell.setBorder(0);
	                t2.addCell(cell);
	                
        			paragraph = new Paragraph("RCDC Code Name", arialBold10);
	                cell = new Cell(paragraph);
	                cell.setBorder(0);
	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
	                cell.setColspan(2);
	                t2.addCell(cell);
                
					for(int j=0;j<rcdcCodeList.size();j++){
						//Blank cell
        				paragraph = new Paragraph("", arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                t2.addCell(cell);
    	                
						paragraph = new Paragraph(rcdcCodeList.get(j), arial8);
    	                cell = new Cell(paragraph);
    	                cell.setBorder(0);
    	                cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
    	                cell.setColspan(2);
    	                t2.addCell(cell);
					}
        		}
        		document.add(t2);
	
        } catch (DocumentException e) { 
                System.err.println(e.getMessage()); 
        } catch (IOException ex) { 
                System.err.println(ex.getMessage()); 
        } 
        document.close();
		

		model.put("fileName", fileName);
		return new ModelAndView(JSON_VIEW, model);
	}
	private String getFormattedDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(date);
    }
	/** Typically injected via Spring */
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}
}
