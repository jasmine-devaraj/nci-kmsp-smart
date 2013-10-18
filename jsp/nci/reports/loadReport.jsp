<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="altum"	uri="/WEB-INF/tlds/altum.tld" %>
<%
String applID = request.getParameter("applIDOrSerial");
boolean runBySerial = (request.getParameter("runBySerial")!=null) && (request.getParameter("runBySerial").equals("true")); 
String manageCustomMapping = request.getParameter("manageCustomMapping");
String runQueryReport = request.getParameter("runQueryReport");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/report.css"/>" />
    <link rel="stylesheet" href="<c:url value="/styles/style.css"/>" type="text/css" media="screen" />
    <script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
	<script>
	var applID = "<%=applID%>";
	var runBySerial = "<%=runBySerial%>";
	var manageCustomMapping = "<%=manageCustomMapping%>";
	var runQueryReport = "<%=runQueryReport%>";
	function bodyOnLoad(){
		if(applID!="null"){
			//project coding report
			document.location = '<c:url value="/report/rcdc/runProjectCodingReport.grs" />?applIDOrSerial=' + applID + ((runBySerial=='true')?"&runBy=Serial Number":"&runBy=Application ID");
		}else if (manageCustomMapping=="true"){
			document.location = '<c:url value="/report/rcdc/manageCustomMapping.grs" />';
		}else if (runQueryReport=="true"){
			document.location = '<c:url value="/report/rcdc/runQueryReport.grs" />';
		}else{
			//rcdc report comparison report
			document.location = '<c:url value="/report/rcdc/runRcdcComparisonReport.grs" />';
		}
	}
	</script>
  </head>

<body id="home" onload="bodyOnLoad();">
	<%@include file="/jsp/include/header.jspf"%>
  	<div id="container">
    	<div id="content">
		<div id="timer">
		<br>
		<br><br><br><br>
		<table width="100%" height="500px" id="titleTable">
			<tr><th id="titleTd" align="center">Please wait while the page is loaded...</th></tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td align="center"><img src="<c:url value="/images/progress.gif"/>"></td></tr>
			<tr><td>&nbsp;</td></tr>
			<tr height="100%"><td align="center"><altum:Timer/></td></tr>
		</table>
		</div>
		</div>
	<%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
