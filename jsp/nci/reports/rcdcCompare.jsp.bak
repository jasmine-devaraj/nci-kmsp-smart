<%--
/**
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@page import="java.util.*,
				org.jmesa.limit.ExportType,
				com.altum.coding.data.CommonTokens,
				com.altum.coding.enums.JmesaKeys,
				com.altum.coding.enums.SqlCommandKeys,
				com.altum.fwk.util.UtilityMethods,
				com.altum.fwk.web.support.JmesaColumnProperties,
				com.altum.jdbc.web.JdbcController,
				java.text.SimpleDateFormat" %>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="jmesa"	uri="/WEB-INF/tlds/jmesa.tld" %>
<%@taglib prefix="altum"	uri="/WEB-INF/tlds/altum.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%	//**************************** VARIABLES **********************************//
String runBy = request.getParameter("runBy");
boolean bRunByApplID = runBy.indexOf("Application")>-1; 
String iFY = request.getParameter("rcdcFY");
String sProjectTypeList = UtilityMethods.arrayToSqlList(request.getParameterValues("ProjectTypeSimple")).toString();
boolean bProjectTypeAll = sProjectTypeList.indexOf("All")>0;
boolean bProjectTypeGrants = sProjectTypeList.indexOf("Grants")>0;
boolean bProjectTypeIntramural = sProjectTypeList.indexOf("Intramural")>0;
boolean bProjectTypeContracts = sProjectTypeList.indexOf("Contracts")>0;
String source = request.getParameter("source");
String sDisease = request.getParameter("obDiseaseArea");
String sMechList = UtilityMethods.arrayToSqlList(request.getParameterValues("mechanismOptionMulti")).toString();
boolean bMech=sMechList.length()>0;
if(!bMech){
	sMechList="All";
}
// refresh is true so that subsequent refreshing of the page only does select
boolean refresh = session.getAttribute(JdbcController.SESSION_RESULTSETDATA_LIST_TOKEN)!=null;

// Map of params for the Show Parameter box
Map<String,String> params = new LinkedHashMap<String,String>();
params.put("Report Run By",runBy);
params.put("Fiscal Year",iFY);
params.put("Project Type",bProjectTypeAll?"All":sProjectTypeList.replaceAll("'",""));
params.put("Source",source);
params.put("Disease Area",(sDisease.indexOf(" (C)")>-1?sDisease.substring(0, sDisease.length()-3):sDisease));
params.put("Mechanism",sMechList.replaceAll("'",""));

//Treemap used to add hidden inputs at bottom to propogate all request params
TreeMap<String,String[]> tm = new TreeMap<String,String[]>();
Set<String> paramNameSet = request.getParameterMap().keySet();
for(String sName : paramNameSet){
	tm.put(sName,request.getParameterValues(sName));
}

// caption for the table
String caption = "National Cancer Institute - FY - "+iFY+" Disease - "+(sDisease.indexOf(" (C)")>-1?sDisease.substring(0, sDisease.length()-3):sDisease);
SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
String fileName = (sDisease.length()>24 ? sDisease.substring(0,24) : sDisease) + "_" + sdf.format(new Date());

//Column decoration details for dynamically formatting the JMESA table at the controller.
List<JmesaColumnProperties> colProperties = new ArrayList<JmesaColumnProperties>();
colProperties.add(new JmesaColumnProperties("DISEASE_CATEGORY","Disease Category",null,null));
colProperties.add(new JmesaColumnProperties("FY","FY",null,null));
colProperties.add(new JmesaColumnProperties("APPL_TYPE","Application Type",null,null));
colProperties.add(new JmesaColumnProperties("FULL_PROJ_NUM","Project Number","DroplistFilterEditor",null));
colProperties.add(new JmesaColumnProperties("SUBPROJECT_ID","SubPrj ID",null,null));
colProperties.add(new JmesaColumnProperties("SERIAL_NUM","Serial Number",null,null));
if(bRunByApplID) colProperties.add(new JmesaColumnProperties("APPL_ID","Appl ID",null,null));
colProperties.add(new JmesaColumnProperties("ARRA_GRANT_ELIGIBLE_FLAG","ARRA","DroplistFilterEditor",null));
colProperties.add(new JmesaColumnProperties("ACTIVITY_CODE","Activity Code","DroplistFilterEditor",null));
colProperties.add(new JmesaColumnProperties("PROJECT_TITLE","Project Title",null,null));
colProperties.add(new JmesaColumnProperties("APPL_STATUS_CODE","Application Status Code",null,null));
colProperties.add(new JmesaColumnProperties("APPL_STATUS_DESCRIP","Application Status Description",null,null));
colProperties.add(new JmesaColumnProperties("DIVISION","Division",null,null));
colProperties.add(new JmesaColumnProperties("CANCER_ACT","Cancer Activity",null,null));
colProperties.add(new JmesaColumnProperties("RFA_PA_NUMBER","RFA-PA",null,null));
colProperties.add(new JmesaColumnProperties("TOTAL_DOLLARS","Total $",null,"$###,###.###"));
colProperties.add(new JmesaColumnProperties("RAEB_PERCENT_REL"," % ",null,"###.##"));
colProperties.add(new JmesaColumnProperties("RAEB_REL_DOLLARS","Related $",null,"$###,###.###"));
colProperties.add(new JmesaColumnProperties("DATA_SOURCE","Source","DroplistFilterEditor",null));
colProperties.add(new JmesaColumnProperties("PDF_LINK","PDF Link",null,null));
colProperties.add(new JmesaColumnProperties("EXT_LINK","Extracted Text Link",null,null));
%>
<%@ include file="sqlInclude/rcdcCompare.sql" %>
<%
EnumMap<JmesaKeys,Object> jmesaMap = new EnumMap<JmesaKeys,Object>(JmesaKeys.class);
jmesaMap.put(JmesaKeys.TABLE_ID,"row");
jmesaMap.put(JmesaKeys.EXPORT_TYPES, new ExportType[]{ExportType.EXCEL});
jmesaMap.put(JmesaKeys.COLUMN_PROPERTIES,colProperties);
jmesaMap.put(JmesaKeys.CAPTION,caption);
jmesaMap.put(JmesaKeys.FILE_NAME,fileName);

EnumMap<SqlCommandKeys,Object> sqlCommandMap = new EnumMap<SqlCommandKeys,Object>(SqlCommandKeys.class);
sqlCommandMap.put(SqlCommandKeys.SELECT, sSelect);
sqlCommandMap.put(SqlCommandKeys.DROP, new String[]{sDropSQL1,sDropSQL2});
sqlCommandMap.put(SqlCommandKeys.CREATE, new String[]{sCreateSQL1,sCreateSQL2});
sqlCommandMap.put(SqlCommandKeys.INSERT, new String[]{sInsertSQL1,sInsertSQL2});

Map<String,Object> rowParams = new HashMap<String,Object>();
rowParams.put(CommonTokens.JMESA_MAP_TOKEN, jmesaMap);
rowParams.put(CommonTokens.SQL_COMMAND_MAP_TOKEN,sqlCommandMap);

List<Map<String,Object>> paramMaps = new ArrayList<Map<String,Object>>();
paramMaps.add(rowParams);
session.setAttribute(JdbcController.SESSION_PARAM_MAPS_TOKEN,paramMaps);
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<link rel="stylesheet" href="<c:url value="/styles/rptMain.css" />" />
<link rel="stylesheet" href="<c:url value="/styles/jmesa.css" />" />
<link rel="stylesheet" href="<c:url value="/styles/style.css" />" type="text/css" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/chromemenu.css"/>" />
<style>
a       { color: blue;}
a:hover { color: blue;}
</style>
<script language="JavaScript" src="<c:url value="/js/gui/mainpage.js" />"></script>
<script language="JavaScript" src="<c:url value="/js/jmesa/jmesa.js" />"></script>
<script language="JavaScript" src="<c:url value="/js/jmesa/jquery-1.2.6.min.js" />"></script>
<script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
<script language="JavaScript" src="<c:url value="/js/nci/reports/rptUtility.js" />"></script>
<title>NCI Knowledge Management and Special Projects Home</title>
<script>
//JMESA Functions
function onInvokeAction(id, action){
	setExportToLimit(id, '');
	if (action == 'sort'){
		var limit = TableFacadeManager.getTableFacade(id).limit;
		var sortSet = limit.getSortSet();
		if (sortSet){
			var sort = limit.sortSet[limit.sortSet.length - 1];
			removeAllSortsFromLimit(id)
			limit.sortSet[0] = sort;
		}
	}
	var parameterString = createParameterStringForLimit(id);
	$.post('${pageContext.request.contextPath}/report/jmesa/getReportData.grs' ,parameterString, function(data){
			$('#rows').html(data.<%= CommonTokens.TABLES_TOKEN %>[0].<%= CommonTokens.DATA_TOKEN %>);
		},"json");
}

function onInvokeExportAction(id){
	//Use the Yahoo Connection Library to generate the excel file.
	var callback =
	{
		success:exportReportToExcel,
		failure:responseFailure
	}
	var url = '<c:url value="/report/rcdc/getMappingParameters.grs" />?disease=' + '<%=sDisease.replaceAll("&","%26")%>';
	YAHOO.util.Connect.asyncRequest('POST', url, callback);
}

function create(){
	//Use the Yahoo Connection Library to post the data.
	var callback =
	{
		success:successJSON,
		failure:responseFailure
	}
	var postData = '';
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '<c:url value="/report/jmesa/createAndFillTables.grs" />', callback, postData);
}

function select(){
	var callback =
	{
		success:successJSON,
		failure:responseFailure
	}
	var postData ='';
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '<c:url value="/report/jmesa/getReportData.grs" />', callback, postData);
}

// This function replaces the common function 'success' in the rptUtility.js as this involves handling JSON data.
function successJSON(obj){
	// hides the timer Div and displays the jmesa table Div.
	document.getElementById('timer').style.display = 'none';
	document.getElementById('ddtabs5').style.display = 'inline';
	document.getElementById('toggle').style.display = 'inline';
	var jsonObj = eval('('+obj.responseText+')');
	document.getElementById('rows').innerHTML = jsonObj.<%= CommonTokens.TABLES_TOKEN %>[0].<%= CommonTokens.DATA_TOKEN %>;
	// calls to javascript functions for jmesa
	addTableFacadeToManager('row');
	setMaxRowsToLimit('row',15);
	setPageToLimit('row',1);
}
function exportReportToExcel(){
	//Use the Yahoo Connection Library to generate the excel file.
	var callback =
	{
		success:generateSpreadsheetFinished,
		failure:responseFailure
	}
	var url = '<c:url value="/report/generateSpreadsheet.grs?sessionID=" />'+'<%=sSessionId%>' + '&title=' + '<%=caption.replaceAll("&","%26").replaceAll("'", "\\\\\'")%>';
	url    += '&projectType=' + '<%=bProjectTypeAll?"All":sProjectTypeList.replaceAll("'","")%>';
	url    += '&source=' + '<%=source%>';
	url    += '&mechanism=' + '<%=sMechList.replaceAll("'","")%>';
	YAHOO.util.Connect.asyncRequest('POST', url, callback);
}
var spreadsheetWindow;
function generateSpreadsheetFinished(obj){
	if(spreadsheetWindow!=null){
		spreadsheetWindow.close();
	}
	spreadsheetWindow = window.open('<c:url value="exportFrame.jsp" />'+'?fileName='+obj.responseText, '_blank');
	return false;
}
function responseFailure(obj){
	var msg = "There was a problem processing your request. Report the following error to your support staff: ";
	msg += "Status:" + obj.status + " Message:" + obj.statusText;
	alert(msg);
}
function openProjectCodingReport(applID, runBySerial){
	window.open("loadReport.jsp?applIDOrSerial=" + applID + ((runBySerial==true)?"&runBySerial=true":"&runBySerial=false"));
}
</script>
</head>
<body onload="<%= refresh ? "select()" : "create()" %>" onunload = "if(spreadsheetWindow) spreadsheetWindow.close();">
<%@include file="/jsp/include/header.jspf"%>
<div id="container">
<div id="content">
<div id="ddtabs5" class="chromemenu" style="display:none;">
	<ul>
		<li><a href="<c:url value="/default.jsp"/>" rel="cm1">HOME</a></li>
		<li><a href="<c:url value="/jsp/nci/reports/rcdcCompareReportParameter.jsp"/>" rel="cm1">NEW SEARCH</a></li>
	</ul>
</div>
<%@ include file="showParams.jspf" %>
<form name="form" method="post">
	<div id="timer">
		<br>
		<br><br>
		<table width="100%" height="500px" >
			<tr><th align="center">Please wait while the page is loaded...</th></tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td align="center"><img src="<c:url value="/images/progress.gif"/>"></td></tr>
			<tr><td>&nbsp;</td></tr>
			<tr height="100%"><td align="center"><altum:Timer/></td></tr>
		</table>
	</div>
	<div id="rows" style="height:550px"></div>
	<input type="hidden" name="sTblName1" value="<%=sTblName1%>"/>
	<input type="hidden" name="sTblName2" value="<%=sTblName2%>"/>
<%
	String[] aValues;
	for(String sName : tm.keySet()){
		if(!sName.startsWith("row_")){
			aValues = request.getParameterValues(sName);
			for (String sValue : aValues){ %>
				<input type="hidden" name="<%=sName%>" value="<%=sValue%>">
			<%}
		}
	}%>
</form>
</div>
<%@include file="/jsp/include/footer.jspf"%>
</div> <!-- container -->
</body>
</html>
