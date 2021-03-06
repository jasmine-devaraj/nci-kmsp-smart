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

/* Temp Table Definition */
String sSessionTable = "RPT_" + sSessionId;    //Name Temporary Table

//Create SQL specific vars
String sTblName1 = sSessionTable.substring(0,(sSessionTable.length())) + "FY1";
String sTblName2 = sSessionTable.substring(0,(sSessionTable.length())) + "FY2";

/******************************************************************************/
/* sCreateSQL creates the main report temporary */
StringBuffer sbCreateSQL1 = new StringBuffer();
sbCreateSQL1.append("CREATE TABLE ").append(sTblName1).append(" ( ");
sbCreateSQL1.append("  fy                  NUMBER(4),    ");
sbCreateSQL1.append("  appl_id             NUMBER(10),   ");
sbCreateSQL1.append("  appl_type           VARCHAR2(1),  ");
sbCreateSQL1.append("  activity_code       VARCHAR(3),   ");
sbCreateSQL1.append("  proj_num            VARCHAR2(30), ");
sbCreateSQL1.append("  subproject_id       VARCHAR2(4),  ");
sbCreateSQL1.append("  proj_title          VARCHAR2(81), ");
sbCreateSQL1.append("  total_dollars       NUMBER(10),   ");
sbCreateSQL1.append("  percent_rel         NUMBER(10),   ");
sbCreateSQL1.append("  serial_num          NUMBER(22),   ");
sbCreateSQL1.append("  appl_status_code    VARCHAR2(2),  ");
sbCreateSQL1.append("  appl_status_descrip VARCHAR2(60),  ");
sbCreateSQL1.append("  division			   VARCHAR2(10),  ");
sbCreateSQL1.append("  cancer_act	       VARCHAR2(80),  ");
sbCreateSQL1.append("  rfa_pa_number	   VARCHAR2(10),  ");
sbCreateSQL1.append("  po_name	   VARCHAR2(81),  ");
sbCreateSQL1.append("  arra_grant_eligible_flag	   VARCHAR2(1)  ");
sbCreateSQL1.append(")");
String sCreateSQL1 = sbCreateSQL1.toString();
%>
<%= "<" + "!-- Session Table Create SQL:  " + sCreateSQL1 + "-->" %>

<%
/******************************************************************************/
StringBuffer sbInsertSQL1 = new StringBuffer();
//Select 1 - RCDC Budget Data
sbInsertSQL1.append("INSERT INTO ").append(sTblName1);
sbInsertSQL1.append(" SELECT rph.fy");
sbInsertSQL1.append(",rph.appl_id");
sbInsertSQL1.append(",rph.appl_type");
sbInsertSQL1.append(",rph.activity_code");
sbInsertSQL1.append(",rph.proj_num AS full_proj_num");
sbInsertSQL1.append(",rph.subproject_id");
sbInsertSQL1.append(",rph.proj_title AS project_title");
sbInsertSQL1.append(",rph.amount_oblig AS total_dollars");
sbInsertSQL1.append(",NULL AS percent_rel");
sbInsertSQL1.append(",rph.serial_num AS serial_num");
sbInsertSQL1.append(",rph.appl_status_code AS appl_status_code");
sbInsertSQL1.append(",ast.appl_status_descrip");
sbInsertSQL1.append(",rnaai.pd_division_abbreviation  as division");
sbInsertSQL1.append(",rnaai.cancer_activity_description as cancer_act");
sbInsertSQL1.append(",rph.rfa_pa_number");
sbInsertSQL1.append(",rph.po_name");
sbInsertSQL1.append(",rph.arra_grant_eligible_flag");
sbInsertSQL1.append("  FROM rcdc_projlist_header rph");
sbInsertSQL1.append("  JOIN rcdc_projlist_coding rpc ON rph.fy=rpc.fy AND rph.appl_id=rpc.appl_id");
sbInsertSQL1.append("  JOIN disease_categories_t  dc ON rpc.dc_id = dc.dc_id AND rpc.fy = dc.dc_assigned_fy");
sbInsertSQL1.append("  LEFT OUTER JOIN rcdc_nci_appls_admin_info rnaai ON rph.appl_id=rnaai.appl_id  ");
sbInsertSQL1.append("  LEFT OUTER JOIN appl_statuses_t ast ON rph.fy = ast.fy AND rph.appl_status_code = ast.appl_status_code");
sbInsertSQL1.append(" WHERE rph.fy = ").append(iFY);
if (bMech) sbInsertSQL1.append(" AND rph.activity_code IN (").append(sMechList).append(")");
if(bProjectTypeAll || (bProjectTypeGrants && bProjectTypeContracts && bProjectTypeIntramural)){
	sbInsertSQL1.append(" AND (rph.APPL_CLASS_CODE in ('G', 'C') OR rph.activity_code like 'Z%')");
}else if(bProjectTypeGrants && bProjectTypeContracts){
	sbInsertSQL1.append(" AND rph.APPL_CLASS_CODE in ('G', 'C')");
}else if(bProjectTypeGrants && bProjectTypeIntramural){
	sbInsertSQL1.append(" AND (rph.APPL_CLASS_CODE in ('G') OR rph.activity_code like 'Z%')");
}else if(bProjectTypeContracts && bProjectTypeIntramural){
	sbInsertSQL1.append(" AND (rph.APPL_CLASS_CODE in ('C') OR rph.activity_code like 'Z%')");
}else if(bProjectTypeGrants){
   	sbInsertSQL1.append(" AND rph.APPL_CLASS_CODE = 'G' ");
}else if(bProjectTypeContracts){
   	sbInsertSQL1.append(" AND rph.APPL_CLASS_CODE = 'C' ");
}else if(bProjectTypeIntramural){
   	sbInsertSQL1.append(" AND rph.activity_code like 'Z%' ");
}
sbInsertSQL1.append("   AND dc.dc_name = ");
if(sDisease.indexOf(" (C)")>-1){
	sbInsertSQL1.append("(select distinct ob_name from ob_collection_code_mapping_t where custom_name = '").append(sDisease.replace("'","''")).append("')");
}else{
	sbInsertSQL1.append("'").append(sDisease.replace("'","''")).append("'");
}
sbInsertSQL1.append(" ORDER BY full_proj_num");
String sInsertSQL1 = sbInsertSQL1.toString();
%>
<%= "<" + "!-- Populate RCDC Data Temp Table SQL:  " + sInsertSQL1 + "-->" %>

<%
/******************************************************************************/
//Build Flare SQL Statements
StringBuffer sbCreateSQL2 = new StringBuffer();
sbCreateSQL2.append("CREATE TABLE ").append(sTblName2).append(" ( ");
sbCreateSQL2.append("  fy                  NUMBER(4),    ");
sbCreateSQL2.append("  appl_id             NUMBER(10),   ");
sbCreateSQL2.append("  appl_type           VARCHAR2(1),  ");
sbCreateSQL2.append("  activity_code       VARCHAR(3),   ");
sbCreateSQL2.append("  proj_num            VARCHAR2(30), ");
sbCreateSQL2.append("  subproject_id       VARCHAR2(4),  ");
sbCreateSQL2.append("  proj_title          VARCHAR2(81), ");
sbCreateSQL2.append("  total_dollars       NUMBER(10),   ");
if(bIncludeCoding){
sbCreateSQL2.append("  code_name       	   VARCHAR2(81),   ");
}
sbCreateSQL2.append("  percent_rel         NUMBER(10),   ");
sbCreateSQL2.append("  serial_num          NUMBER(22),   ");
sbCreateSQL2.append("  appl_status_code    VARCHAR2(2),  ");
sbCreateSQL2.append("  arra_grant_eligible_flag    VARCHAR2(1),  ");
sbCreateSQL2.append("  appl_status_descrip VARCHAR2(60),  ");
sbCreateSQL2.append("  division			   VARCHAR2(10),  ");
sbCreateSQL2.append("  cancer_act	       VARCHAR2(80),  ");
sbCreateSQL2.append("  rfa_pa_number	   VARCHAR2(10),  ");
sbCreateSQL2.append("  po_name	   VARCHAR2(81)  ");
sbCreateSQL2.append(")");
String sCreateSQL2 = sbCreateSQL2.toString();
%>
<%= "<" + "!-- Session Table Create SQL:  " + sCreateSQL2 + "-->" %>

<%
/******************************************************************************/
//select 2 - FlareData
StringBuffer sbInsertSQL2 = new StringBuffer();
sbInsertSQL2.append("INSERT INTO ").append(sTblName2);
sbInsertSQL2.append(" SELECT raai.fy ");
sbInsertSQL2.append(",raai.appl_id ");
sbInsertSQL2.append(",raai.appl_type ");
sbInsertSQL2.append(",raai.activity_code ");
sbInsertSQL2.append(",raai.proj_num ");
sbInsertSQL2.append(",raai.subproject_id ");
sbInsertSQL2.append(",raai.proj_title ");
sbInsertSQL2.append(",rnaai.obligated_nci_amt ");
if(bIncludeCoding){
	sbInsertSQL2.append(",rpc.code_name ");
}
if(bIncludeCoding){	
	sbInsertSQL2.append(",rpc.code_percent_relevance AS percent_rel");
}else{
	sbInsertSQL2.append(",sum(rpc.code_percent_relevance) AS percent_rel");
}
sbInsertSQL2.append(",raai.serial_num ");
sbInsertSQL2.append(",raai.appl_status_code ");
sbInsertSQL2.append(",raai.arra_grant_eligible_flag ");
sbInsertSQL2.append(",ast.appl_status_descrip ");
sbInsertSQL2.append(",rnaai.pd_division_abbreviation  as division ");
sbInsertSQL2.append(",rnaai.cancer_activity_description as cancer_act ");
sbInsertSQL2.append(",raai.rfa_pa_number ");
sbInsertSQL2.append(",raai.po_name ");
sbInsertSQL2.append("  FROM rcdc_appls_admin_info raai");
sbInsertSQL2.append("  JOIN raeb_project_codes rpc ON (raai.appl_id = rpc.appl_id)");
sbInsertSQL2.append("  LEFT OUTER JOIN rcdc_nci_appls_admin_info rnaai ON raai.appl_id=rnaai.appl_id  ");
sbInsertSQL2.append("  LEFT OUTER JOIN appl_statuses_t ast ON raai.fy = ast.fy AND raai.appl_status_code = ast.appl_status_code");
sbInsertSQL2.append("  JOIN ob_collection_code_mapping_t ocm ON (rpc.type = ocm.type_id AND rpc.code = ocm.code_id)");
sbInsertSQL2.append(" WHERE raai.fy = ").append(iFY);
if (bMech) sbInsertSQL2.append(" AND raai.activity_code IN (").append(sMechList).append(")");
if(bProjectTypeAll || (bProjectTypeGrants && bProjectTypeContracts && bProjectTypeIntramural)){
	sbInsertSQL2.append(" AND (raai.APPL_CLASS_CODE in ('G', 'C') OR raai.activity_code like 'Z%')");
}else if(bProjectTypeGrants && bProjectTypeContracts){
	sbInsertSQL2.append(" AND raai.APPL_CLASS_CODE in ('G', 'C')");
}else if(bProjectTypeGrants && bProjectTypeIntramural){
	sbInsertSQL2.append(" AND (raai.APPL_CLASS_CODE in ('G') OR raai.activity_code like 'Z%')");
}else if(bProjectTypeContracts && bProjectTypeIntramural){
	sbInsertSQL2.append(" AND (raai.APPL_CLASS_CODE in ('C') OR raai.activity_code like 'Z%')");
}else if(bProjectTypeGrants){
   	sbInsertSQL2.append(" AND raai.APPL_CLASS_CODE = 'G' ");
}else if(bProjectTypeContracts){
   	sbInsertSQL2.append(" AND raai.APPL_CLASS_CODE = 'C' ");
}else if(bProjectTypeIntramural){
   	sbInsertSQL2.append(" AND raai.activity_code like 'Z%' ");
}
if(sDisease.indexOf(" (C)")>-1){
	sbInsertSQL2.append("    AND ocm.custom_name = ");
}else{
	sbInsertSQL2.append("    AND ocm.custom_name is null ");
	sbInsertSQL2.append("    AND ocm.ob_name = ");
}
sbInsertSQL2.append("'").append(sDisease.replace("'","''")).append("'");
sbInsertSQL2.append("    AND (raai.fy < 2009  or rpc.type = 'CSO'  or rpc.type = 'CANCERACT' or rpc.code_percent_relevance > 0)");
if(!bIncludeCoding){
	sbInsertSQL2.append("  GROUP BY raai.fy ,raai.appl_type ,raai.proj_num ,raai.subproject_id , raai.serial_num, raai.appl_id, raai.activity_code, raai.proj_title , raai.appl_status_code, raai.arra_grant_eligible_flag, ast.appl_status_descrip, rnaai.pd_division_abbreviation ,rnaai.cancer_activity_description,raai.rfa_pa_number, raai.po_name ,rnaai.obligated_nci_amt");
}
sbInsertSQL2.append("  ORDER BY raai.proj_num ");
String sInsertSQL2 = sbInsertSQL2.toString();
%>
<%= "<" + "!-- Populate Flare Data Temp Table SQL:  " + sInsertSQL2 + "-->" %>

<%
/******************************************************************************/
// select statement
StringBuffer sbSelect = new StringBuffer();
String selectDisease = sDisease.replace("'","''");
if(bCustomMapping){
	selectDisease = selectDisease.substring(0, selectDisease.length()-3);
}
if(bRunByApplID){
	sbSelect.append("SELECT '").append(selectDisease).append("' AS disease_category");
	sbSelect.append(",COALESCE(rcdc.fy,flare.fy) AS fy");
	sbSelect.append(",COALESCE(rcdc.appl_type,flare.appl_type) AS appl_type");
	sbSelect.append(",COALESCE(rcdc.proj_num,flare.proj_num) AS full_proj_num");
	sbSelect.append(",COALESCE(rcdc.subproject_id,flare.subproject_id) AS subproject_id");
	sbSelect.append(", '<a href=\"javascript:openProjectCodingReport('||COALESCE(rcdc.serial_num,flare.serial_num)||',true);\" >'||COALESCE(rcdc.serial_num,flare.serial_num)||'</a> ' AS serial_num");
	sbSelect.append(", '<a href=\"javascript:openProjectCodingReport('||COALESCE(rcdc.appl_id,flare.appl_id)||',false);\" >'||COALESCE(rcdc.appl_id,flare.appl_id)||'</a> ' AS appl_id");
	sbSelect.append(",CASE WHEN COALESCE(rcdc.arra_grant_eligible_flag,flare.arra_grant_eligible_flag) = 'Y' THEN 'Yes' ELSE 'No' END AS arra_grant_eligible_flag");
	sbSelect.append(",COALESCE(rcdc.activity_code,flare.activity_code) AS activity_code");
	sbSelect.append(",COALESCE(rcdc.proj_title,flare.proj_title) AS project_title");
	sbSelect.append(",COALESCE(rcdc.appl_status_code,flare.appl_status_code) AS appl_status_code");
	sbSelect.append(",COALESCE(rcdc.appl_status_descrip,flare.appl_status_descrip) AS appl_status_descrip");
	sbSelect.append(",COALESCE(rcdc.division,flare.division) AS division");
	sbSelect.append(",COALESCE(rcdc.cancer_act,flare.cancer_act) AS cancer_act");
	sbSelect.append(",COALESCE(rcdc.rfa_pa_number,flare.rfa_pa_number) AS rfa_pa_number");
	sbSelect.append(",COALESCE(rcdc.po_name,flare.po_name) AS po_name");
	sbSelect.append(",CASE WHEN flare.fy IS NULL THEN rcdc.total_dollars ELSE flare.total_dollars END AS total_dollars");
	if(bIncludeCoding){
	sbSelect.append(",flare.code_name AS code_name");
	}
	sbSelect.append(",flare.percent_rel AS raeb_percent_rel");
	sbSelect.append(",ROUND(flare.total_dollars * flare.percent_rel / 100) AS raeb_rel_dollars");
	sbSelect.append(",CASE");
	sbSelect.append(" WHEN rcdc.fy IS NULL THEN 'FLARE'");
	sbSelect.append(" WHEN flare.fy IS NULL THEN 'RCDC'");
	sbSelect.append(" ELSE 'BOTH'");
	sbSelect.append(" END AS data_source");
	sbSelect.append(", '<a href=\"https://apps.era.nih.gov/rcdc/jsp/common/viewExportedProject.jsp?action=VIEW_PDF&applicationID='||COALESCE(COALESCE(rcdc.appl_id,flare.appl_id),0)||'\" target=\"_blank\" >PDF Link</a> ' AS pdf_link");
	sbSelect.append(", '<a href=\"https://apps.era.nih.gov/rcdc/jsp/common/viewExportedProject.jsp?action=VIEW_EXTRACTED_TEXT&applicationID='||COALESCE(COALESCE(rcdc.appl_id,flare.appl_id),0)||'\" target=\"_blank\" >Extracted Text Link</a> ' AS ext_link");
	sbSelect.append(" FROM ").append(sTblName1).append(" rcdc");
	sbSelect.append(" FULL OUTER JOIN   ").append(sTblName2).append(" flare ON rcdc.fy=flare.fy AND rcdc.appl_id=flare.appl_id");
	if(source.equals("Both")){
		sbSelect.append(" where flare.fy IS NOT NULL and rcdc.fy IS NOT NULL ");
	}else if (source.equals("NCI")){
		sbSelect.append(" where rcdc.fy IS NULL ");
	}else if (source.equals("RCDC")){
		sbSelect.append(" where flare.fy IS NULL ");
	}
	sbSelect.append(" ORDER BY 2, 4, 5");
}else{
	sbSelect.append("SELECT '").append(selectDisease).append("' AS disease_category");
	sbSelect.append(",COALESCE(rcdc.fy,flare.fy) AS fy");
	sbSelect.append(",MAX(COALESCE(rcdc.appl_type,flare.appl_type)) AS appl_type");
	sbSelect.append(",MAX(COALESCE(rcdc.proj_num,flare.proj_num)) AS full_proj_num");
	sbSelect.append(",COALESCE(rcdc.subproject_id,flare.subproject_id) AS subproject_id");
	sbSelect.append(", '<a href=\"javascript:openProjectCodingReport('||COALESCE(rcdc.serial_num,flare.serial_num)||',true);\" >'||COALESCE(rcdc.serial_num,flare.serial_num)||'</a> ' AS serial_num");
	sbSelect.append(",MAX(CASE WHEN COALESCE(rcdc.arra_grant_eligible_flag,flare.arra_grant_eligible_flag) = 'Y' THEN 'Yes' ELSE 'No' END) AS arra_grant_eligible_flag");
	sbSelect.append(",MAX(COALESCE(rcdc.activity_code,flare.activity_code)) AS activity_code");
	sbSelect.append(",MAX(COALESCE(rcdc.proj_title,flare.proj_title)) AS project_title");
	sbSelect.append(",MAX(COALESCE(rcdc.appl_status_code,flare.appl_status_code)) AS appl_status_code");
	sbSelect.append(",MAX(COALESCE(rcdc.appl_status_descrip,flare.appl_status_descrip)) AS appl_status_descrip");
	sbSelect.append(",MAX(COALESCE(rcdc.division,flare.division)) AS division");
	sbSelect.append(",MAX(COALESCE(rcdc.cancer_act,flare.cancer_act)) AS cancer_act");
	sbSelect.append(",MAX(COALESCE(rcdc.rfa_pa_number,flare.rfa_pa_number)) AS rfa_pa_number");	
	sbSelect.append(",MAX(COALESCE(rcdc.po_name,flare.po_name)) AS po_name");	
	sbSelect.append(",CASE WHEN flare.fy IS NULL THEN MAX(rcdc.total_dollars) ELSE MAX(flare.total_dollars) END AS total_dollars");
	if(bIncludeCoding){
	sbSelect.append(",MAX(flare.code_name) AS code_name");
	}
	sbSelect.append(",MAX(flare.percent_rel) AS raeb_percent_rel");
	sbSelect.append(",ROUND(MAX(flare.total_dollars) * MAX(flare.percent_rel) / 100) AS raeb_rel_dollars");
	sbSelect.append(",CASE");
	sbSelect.append(" WHEN rcdc.fy IS NULL THEN 'FLARE'");
	sbSelect.append(" WHEN flare.fy IS NULL THEN 'RCDC'");
	sbSelect.append(" ELSE 'BOTH'");
	sbSelect.append(" END AS data_source");
	sbSelect.append(", '<a href=\"https://apps.era.nih.gov/rcdc/jsp/common/viewExportedProject.jsp?action=VIEW_PDF&applicationID='||MAX(COALESCE(rcdc.appl_id,flare.appl_id))||'\" target=\"_blank\" >PDF Link</a> ' AS pdf_link");
	sbSelect.append(", '<a href=\"https://apps.era.nih.gov/rcdc/jsp/common/viewExportedProject.jsp?action=VIEW_EXTRACTED_TEXT&applicationID='||MAX(COALESCE(rcdc.appl_id,flare.appl_id))||'\" target=\"_blank\" >Extracted Text Link</a> ' AS ext_link");	
	sbSelect.append(" FROM ").append(sTblName1).append(" rcdc");
	sbSelect.append(" FULL OUTER JOIN   ").append(sTblName2).append(" flare ON rcdc.fy=flare.fy AND rcdc.appl_id=flare.appl_id");
	if(source.equals("Both")){
		sbSelect.append(" where flare.fy IS NOT NULL and rcdc.fy IS NOT NULL ");
	}else if (source.equals("NCI")){
		sbSelect.append(" where rcdc.fy IS NULL ");
	}else if (source.equals("RCDC")){
		sbSelect.append(" where flare.fy IS NULL ");
	}
	sbSelect.append(" GROUP BY rcdc.fy,flare.fy,rcdc.serial_num,flare.serial_num,rcdc.subproject_id,flare.subproject_id");
	sbSelect.append(" ORDER BY 2, 4, 5");
}
String sSelect = sbSelect.toString();
%>
<%= "<" + "!-- Report SQL:  " + sSelect + "-->" %>
<%
//  **********************************************************************************************
String sDropSQL1 = "DROP TABLE " + sTblName1 + " purge";
String sDropSQL2 = "DROP TABLE " + sTblName2 + " purge";
%>
<%= "<" + "!-- sDropSQL1:  " + sDropSQL1 + "-->" %>
<%= "<" + "!-- sDropSQL2:  " + sDropSQL2 + "-->" %>
