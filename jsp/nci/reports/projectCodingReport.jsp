<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@taglib prefix="altum"	uri="/WEB-INF/tlds/altum.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/report.css"/>" />
    <link rel="stylesheet" href="<c:url value="/styles/style.css"/>" type="text/css" media="screen" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/styles/chromemenu.css"/>" />
    <script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
	<script>
	
	function exportReportToWord(){
		//Use the Yahoo Connection Library to generate the excel file.
		var callback =
		{
			success:generateDocFinished,
			failure:responseFailure
		}
		var url = '<c:url value="/report/rcdc/exportProjectCodingReportToWord.grs" />';
		YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	var wordWindow;
	function generateDocFinished(obj){
		if(wordWindow!=null){
			wordWindow.close();
		}
		wordWindow = window.open('<c:url value="/jsp/nci/reports/exportFrame.jsp" />'+'?fileName='+obj.responseText, '_blank');
		return false;
	}
	function responseFailure(obj){
		var msg = "There was a problem processing your request. Report the following error to your support staff: ";
		msg += "Status:" + obj.status + " Message:" + obj.statusText;
		alert(msg);
	}
	</script>
  </head>

<body id="home" onunload = "if(wordWindow) wordWindow.close();">
  	<%@include file="/jsp/include/header.jspf"%>
  	<div id="container">
    	<div id="content">
    	<div id="ddtabs5" class="chromemenu">
			<ul>
				<li><a href="<c:url value="/default.jsp"/>" rel="cm1">HOME</a></li>
				<li><a href="<c:url value="/jsp/nci/reports/projectCodingReportParameter.jsp"/>" rel="cm1">NEW SEARCH</a></li>
				<c:if test="${fn:length(projectCodingList)>0}">
				<li><a href="javascript:exportReportToWord();" rel="cm1">EXPORT TO WORD</a></li>
				</c:if>
			</ul>
  		</div>
		<div id="timer">
		<br>
			<c:choose>
			<c:when test="${fn:length(projectCodingList)==0}">
				<table width="100%" id="titleTable" >
					<tr><th id="titleTd" align="center" colspan=2><h1>National Cancer Institute</h1><br><h3>PROJECT CODING REPORT</h3></th></tr>
					<tr><td  colspan=2>&nbsp;</td></tr>
					<tr><td  colspan=2>&nbsp;</td></tr>
					<c:set var="appl">Application ID</c:set>
					<c:choose>
					<c:when test="${runBy == appl}">
					<tr><td  colspan=2>Application ID does not exist in the database.</td></tr>
					</c:when>
					<c:otherwise>
					<tr><td  colspan=2>Serial Number does not exist in the database.</td></tr>
					</c:otherwise>
					</c:choose>
					<tr><td  colspan=2>&nbsp;</td></tr>
					<tr><td  colspan=2>&nbsp;</td></tr>
				</table>			
			</c:when>
			<c:otherwise>
				<table width="100%" id="titleTable" >
					<tr><th id="titleTd" align="center" colspan=2><h1>National Cancer Institute</h1><br><h3>PROJECT CODING REPORT</h3></th></tr>
					<tr><td  colspan=2>&nbsp;</td></tr>
					<tr><td>Prepared by</td><td>KMSP/CSSI/NCI</td></tr>
					<jsp:useBean id="date" class="java.util.Date" />
					<tr><td>Date Created</td><td><fmt:formatDate value="${date}" pattern="MMMM d, yyyy"/></td></tr>
				</table>
				<table width="100%" id="infoTable" >
					<c:set var="currentApplID">0</c:set>
					<c:set var="currentTypeID">0</c:set>
					<c:set var="site">SITE</c:set>
					<c:set var="sic">SIC</c:set>
					<c:set var="cso">CSO</c:set>
					<c:set var="toReplace1">[</c:set>
					<c:set var="toReplace2">]</c:set>
					<c:set var="replaceWith"> </c:set>
					<c:forEach items="${projectCodingList}" var="code">
						<c:if test="${code.applID!=currentApplID}">
							<c:set var="currentApplID">${code.applID}</c:set>
							<c:if test="${currentRcdcCodeList!=null && fn:length(currentRcdcCodeList)>2}">
								<tr><td>&nbsp;</td><td><b>RCDC Code Name</b></td><td>&nbsp;</td></tr>
								<c:forEach items="${currentRcdcCodeList}" var="rcdccode">
								<tr><td>&nbsp;</td><td>${fn:replace(fn:replace(rcdccode,toReplace1,replaceWith),toReplace2,replaceWith)}</td><td>&nbsp;</td></tr>
								</c:forEach>
							</c:if>
							<c:set var="currentRcdcCodeList">${code.rcdcCodeList}</c:set>
							<tr><td  colspan=3><hr style="border: 2px solid #000;" /></td></tr>
							<tr><td>Project Number</td><td><b>${code.projNum}</b></td><td>&nbsp;</td></tr>
							<tr><td>Application ID</td><td>${code.applID}</td><td>&nbsp;</td></tr>
							<tr><td>FY</td><td>${code.fy}</td><td>&nbsp;</td></tr>
							<tr><td>Project Title</td><td>${code.projTitle}</td><td>&nbsp;</td></tr>
							<tr><td>Institution</td><td>${code.orgName}</td><td>&nbsp;</td></tr>
							<tr><td>City / State</td><td>${code.city} / ${code.state}</td><td>&nbsp;</td></tr>
							<tr><td>PI</td><td>${code.pi}</td><td>&nbsp;</td></tr>
							<tr><td>Program Director</td><td>${code.po}</td><td>&nbsp;</td></tr>
							<tr><td>Cancer Activity</td><td>${code.cancerActivity}</td><td>&nbsp;</td></tr>
							<tr><td>Division</td><td>${code.division}</td><td>&nbsp;</td></tr>
							<tr><td>Project Start Date</td><td><fmt:formatDate type="date" value="${code.projStartDate}" /></td><td>&nbsp;</td></tr>
							<tr><td>Project End Date</td><td><fmt:formatDate type="date" value="${code.projEndDate}" /></td><td>&nbsp;</td></tr>
							<tr><td>Total NCI Obligated Amount</td><td><fmt:formatNumber value="${code.amountOblig}" type="currency"/> </td><td>&nbsp;</td></tr>
							<tr><td  colspan=3><hr/></td></tr>
						</c:if>
						<c:if test="${code.typeId!=currentTypeID}">
							<c:set var="currentTypeID">${code.typeId}</c:set>
							<c:if test="${code.typeId == site}">
								<tr><td><b>Site Code Number</b></td><td><b>Site Code Name</b></td><td><b>Site Percent Relevance</b></td></tr>
							</c:if>
							<c:if test="${code.typeId == sic}">
								<tr><td><b>Sic Code Number</b></td><td><b>Sic Code Name</b></td><td><b>Sic Percent Relevance</b></td></tr>
							</c:if>
							<c:if test="${code.typeId == cso}">
								<tr><td><b>CSO Code Number</b></td><td><b>CSO Code Name</b></td><td></td></tr>
							</c:if>
						</c:if>
						<c:choose>
						<c:when test="${code.typeId == cso}">
							<tr><td>${code.code}</td><td>${code.codeName}</td><td></td></tr>
						</c:when>
						<c:otherwise>
							<c:if test="${code.code!=null}">
							<tr><td>${code.code}</td><td>${code.codeName}</td><td>${code.percentRel}%</td></tr>
							</c:if>
						</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${currentRcdcCodeList!=null && fn:length(currentRcdcCodeList)>2}">
						<tr><td>&nbsp;</td><td><b>RCDC Code Name</b></td><td>&nbsp;</td></tr>
						<c:forEach items="${currentRcdcCodeList}" var="rcdccode">
						<tr><td>&nbsp;</td><td>${fn:replace(fn:replace(rcdccode,toReplace1,replaceWith),toReplace2,replaceWith)}</td><td>&nbsp;</td></tr>
						</c:forEach>
					</c:if>
				</table>
			</c:otherwise>
			</c:choose>
		</div>
	</div><!-- content -->
	<%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
