<%--
/**
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%
/* Standard Report Page Vars */
String sSessionId  = request.getParameter("jsessionid");

/******************************************************************************/
// select statement
StringBuffer codeSelect = new StringBuffer();
	codeSelect.append(" and (rownum<0 ");
boolean codeParameterPresent = false;
StringBuffer sbSelect = new StringBuffer();
	sbSelect.append("select distinct fy, appl_type, proj_num, subproject_id");
	sbSelect.append(", '<a href=\"javascript:openProjectCodingReport('||serial_num||',true);\" >'||serial_num||'</a> ' AS serial_num");
	sbSelect.append(", '<a href=\"javascript:openProjectCodingReport('||appl_id||',false);\" >'||appl_id||'</a> ' AS appl_id");
	sbSelect.append(", pi_name, org_name,arra_grant_eligible_flag, activity_code, proj_title, appl_status_code, ");
	sbSelect.append("appl_status_descrip, pd_division_abbreviation  as division, po_name, ");
	sbSelect.append("cancer_activity_description as cancer_act, rfa_pa_number ,coalesce(obligated_nci_amt,0) as total_dollars, ");
	sbSelect.append("type, code, code_name, code_percent_relevance as raeb_percent_rel, ");
	sbSelect.append("(coalesce(obligated_nci_amt,0) * (code_percent_relevance/100)) as RAEB_REL_DOLLARS ");
	sbSelect.append("from rcdc_appls_admin_info left outer join raeb_project_codes using (appl_id) ");
	sbSelect.append("left outer join rcdc_nci_appls_admin_info using (appl_id) ");
 	sbSelect.append("where fy in ("+ iFY + ")");
 	
 	StringTokenizer st3 = new StringTokenizer(runByVal, ",");
 		while (st3.hasMoreElements()) {
 			String runBy = st3.nextElement().toString();
			
 			if(runBy.equals("Institution")){
 		sbSelect.append(" and (");
 		StringTokenizer st2 = new StringTokenizer(institution, ",");
 
		while (st2.hasMoreElements()) {
			System.out.println();
			sbSelect.append(" upper(org_name) like upper("+ st2.nextElement() +")");
			if(st2.hasMoreElements()) {
				sbSelect.append(" or ");
			}
		}
 		
 		sbSelect.append(" ) ");
 	}else if(runBy.equals("Principal Investigator")){
 		sbSelect.append(" and (");
 		StringTokenizer st2 = new StringTokenizer(piName, ",");
 
		while (st2.hasMoreElements()) {
			System.out.println();
			sbSelect.append(" upper(pi_name) like upper("+ st2.nextElement() +")");
			if(st2.hasMoreElements()) {
				sbSelect.append(" or ");
			}
		}
		
 		sbSelect.append(" ) ");
 	}else if(runBy.equals("Serial Number")){
 		sbSelect.append(" and serial_num in ("+ serialNumber +")");
 	}else if(runBy.equals("SIC Code")){
 		codeParameterPresent = true;
 		codeSelect.append(" or ( type='SIC' and trim(code_name) in ("+ sicCodeName +")) ");
 	}else if(runBy.equals("SITE Code")){
 		codeParameterPresent = true;
 		codeSelect.append(" or ( type='SITE' and trim(code_name) in ("+ siteCodeName +")) ");
 	}else if(runBy.equals("CSO Code")){
 		codeParameterPresent = true;
 		codeSelect.append(" or ( type='CSO' and trim(code_name) in ("+ csoCodeName +")) ");
 	}else if(runBy.equals("Cancer Activity")){
 		sbSelect.append(" and trim(cancer_activity_description) in ("+ cancerActivity +")");
 	}else if(runBy.equals("Division")){
 		sbSelect.append(" and trim(pd_division_abbreviation) in ("+ division +")");
 	}else if(runBy.equals("PD Description")){
 		sbSelect.append(" and trim(pd_division_description) in ("+ pdDescription +")");
 	}else if(runBy.equals("RFAPA Number")){
 		sbSelect.append(" and trim(rfa_pa_number) in ("+ rfaPaNumber +")");
 	}
 	
		}
	codeSelect.append(" ) ");	
	if(codeParameterPresent){
 		sbSelect.append(codeSelect);
 	}
 	
 	
 	
 	sbSelect.append(" order by fy desc, appl_id, type desc, code_name asc ");
String sSelect = sbSelect.toString();
%>
<%= "<" + "!-- Report SQL:  " + sSelect + "-->" %>


