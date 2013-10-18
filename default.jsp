<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="altum"	uri="/WEB-INF/tlds/altum.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="styles/report.css" />
    <link rel="stylesheet" href="styles/style.css" type="text/css" media="screen" />
    <script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
 	<script>
	  	function setSystemInfo(){
		//Use the Yahoo Connection Library to generate the excel file.
		var callback =
		{
			success:populateSystemParameters,
			failure:responseFailure
		}
		var url = '<c:url value="/report/cacheSystemParameters.grs" />';
		
		
		YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}
		function populateSystemParameters(obj){
			document.getElementById("systemInfoDiv").innerHTML = obj.responseText;
		}
		function responseFailure(obj){
			var msg = "There was a problem processing your request. Report the following error to your support staff: ";
			msg += "Status:" + obj.status + " Message:" + obj.statusText;
			alert(msg);
		}
  	</script>
  </head>

<body id="home" onLoad= "setSystemInfo();">
  	<%@include file="/jsp/include/header.jspf"%>
  	<div id="container">
    	<div id="content"><br></br>
    		<table height="500px" align="center" width="80%" cellpadding="0" cellspacing="0" border="0" >
    			<tr >
    				<td align="right" width="20%">
    					<img src="<c:url value="/images/logo.png"/>" alt="National Cancer Institute"/>
    				</td>
    				<td align="left">
    					<font color="black" size="3"><b>Scientific Managers Analysis and Reporting Tool (SMART)</b></font>
    				</td>
    			</tr>
    			<tr>
    				<td colspan="2">
    					<h3>SMART Main Menu</h3>			
    				</td>
    			</tr>
    			<tr>
    				<td colspan="2">
    					
						<table width="100%"  id="titleTable">
							<tr><td width="5%">&nbsp;</td><td>&nbsp;</td></tr>
							<tr><th width="5%">&nbsp;</th><th id="titleTd" align="left"><a href='<c:url value="/jsp/nci/reports/loadReport.jsp" />'>Run RCDC vs RAEB Comparison Report</a></th></tr>
							<tr><td width="5%">&nbsp;</td><td>&nbsp;</td></tr>
							<tr><th width="5%">&nbsp;</th><th id="titleTd" align="left"><a href='<c:url value="/jsp/nci/reports/projectCodingReportParameter.jsp" />'>Run Project Coding Report</a></th></tr>
							<tr><td width="5%">&nbsp;</td><td>&nbsp;</td></tr>
							<tr><th width="5%">&nbsp;</th><th id="titleTd" align="left"><a href='<c:url value="/jsp/nci/reports/loadReport.jsp" />?runQueryReport=true'>Run Query Report (SIC, SITE, PI, Institution)</a></th></tr>
							<tr><td width="5%">&nbsp;</td><td>&nbsp;</td></tr>
							<tr><th width="5%">&nbsp;</th><th id="titleTd" align="left"><a href='<c:url value="/jsp/nci/reports/loadReport.jsp" />?manageCustomMapping=true'>Manage Custom RCDC RAEB Code Mappings</a></th></tr>
							
							
						</table>			
    				</td>
    			</tr>
    			<tr>
    				<td colspan="2" height="30%">
    					&nbsp;			
    				</td>
    			</tr>
    		</table>
    	
		
		</div>
	<%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
