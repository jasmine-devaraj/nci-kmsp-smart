<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@page language="java" session="true" 
		import="com.altum.jdbc.web.JdbcController,
				com.altum.jdbc.web.ReportController" %>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="altum"	uri="/tlds/altum.tld"%>
<%
session.removeAttribute(JdbcController.SESSION_RESULTSETDATA_LIST_TOKEN);
session.removeAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<%@include file="/jsp/baseHRef.jsp"%>
	<script src="<c:url value="/js/gui/mainpage.js" />" language="Javascript" ></script>
	<script src="js/reports/reportParameter.js" language="JavaScript"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
	<script>
	var bMultiples = false;
	function validate(){
		document.rptParmsForm.applIDOrSerial.value = document.rptParmsForm.applIDOrSerial.value.replace(/^\s+|\s+$/g,"");
		var val = document.rptParmsForm.applIDOrSerial.value; 
		var applIDs = val.split(",");
		bMultiples = (applIDs.length > 1);
		for(i = 0; i < applIDs.length; i++){
			val = applIDs[i];
			val = val.replace(/^\s+|\s+$/g,"");
			if((!val.match(/^[0-9]+$/)) || val.length==0 || val.length>8){
				if(applIDs.length == 1){
					if(document.rptParmsForm.runBy[0].checked){
						alert("Please enter a valid application ID");
					}else{
						alert("Please enter a valid serial number");
					}
				}else{
					if(document.rptParmsForm.runBy[0].checked){
						alert("Atleast one of the application IDs that you have entered is invalid");
					}else{
						alert("Atleast one of the serial numbers that you have entered is invalid");
					}
				}
					document.rptParmsForm.applIDOrSerial.focus();
					document.rptParmsForm.applIDOrSerial.select();
					return false;
			}
		}
				
		return true;
	}
	function runProjectCodingReport(){
		if(!bMultiples){
			var url = '<c:url value="/report/rcdc/runProjectCodingReport.grs" />?applIDOrSerial=' + document.forms[0].applIDOrSerial.value;
			if(document.rptParmsForm.runBy[0].checked){
				url += '&runBy=Application ID';
			}else{
				url += '&runBy=Serial Number';
			}
			document.location = url;
		}else{
			document.forms[0].submit();
		}
	}
	function updateApplOrSerialSpan(){
		document.getElementById("applOrSerial").innerHTML = document.rptParmsForm.runBy[0].checked?'Application ID':'Serial Number';
	}
	</script>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="styles/report.css" />
    <link rel="stylesheet" href="styles/style.css" type="text/css" media="screen" />
    <link rel="stylesheet" type="text/css" href="styles/chromemenu.css" />
  </head>

<body id="home" onunload = "if(spreadsheetWindow) spreadsheetWindow.close();">
  <%@include file="/jsp/include/header.jspf"%>
  <div id="container">
    <div id="content">
		<div id="ddtabs5" class="chromemenu">
			<ul>
				<li><a href="default.jsp" rel="cm1">HOME</a></li>
			</ul>
  		</div>
  		<br/><center><h1>Coding Report</h1>
  		<h3>Search Parameters</h3></center>
		<form name="rptParmsForm" method="POST" action="jsp/nci/reports/jmesaProjectCodingReport.jsp" >
		<div>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='2'>Run Report By</td>
						<td><img align='right' onClick="toggleGroup('Run_by_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='Run_by_ROW'>
					<td valign='top' width='20%'>
						<input type="radio" name="runBy" value="Application ID" checked onclick="updateApplOrSerialSpan();"> Application ID<br>
						<input type="radio" name="runBy" value="Serial Number" onclick="updateApplOrSerialSpan();"> Serial Number<br>
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
				<tr>
					<td colspan='2'><span id="applOrSerial">Application ID</span></td>
					<td><img align='right' onClick="toggleGroup('applID_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
				</tr>
				</thead>
				<tr id='applID_ROW'>
					<td valign='top' width='20%'>
						<br>
						<input type="text" name="applIDOrSerial" />*
					</td>
					<td valign='top'></td>
				</tr>
				<tr>
					<td colspan='3'><i>* Multiple application IDs or serial numbers seperated by commas can be entered in this field. <br/>&nbsp;&nbsp;&nbsp;Example: 7813582, 7813603, 7813184, 7813401.</i></td>
				</tr>
			</table>
			<br>
		</div>
		</form>
		<br>
		<div>
			<input type=button id="execute" onClick="if (validate()) runProjectCodingReport()" name="Execute" value="Execute" />
			<input type=button id="reset" onClick="document.location=document.location" name="Reset" value="Reset" />
		</div>
		<table style="height:150px"><tr>&nbsp;</tr></table>
    </div>
	<br>
	<br>
	<%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
