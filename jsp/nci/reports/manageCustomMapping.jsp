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
<%@taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@taglib prefix="altum"	uri="/tlds/altum.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<%@include file="/jsp/baseHRef.jsp"%>
	<script src="<c:url value="/js/gui/mainpage.js" />" language="Javascript" ></script>
	<script>
	function addCustomTerm(){
		window.showModalDialog("<c:url value="/jsp/nci/reports/customizeMapping.jsp" />",window,"dialogWidth=1000px;dialogHeight=500px;resizable=no;help=no;status=no;");
	}
	function editCustomTerm(customTerm){
		window.showModalDialog('<c:url value="/report/rcdc/editCustomTerm.grs" />?custName='+customTerm.replace(/&/g, "%26"), window, "dialogWidth=1000px;dialogHeight=500px;resizable=no;help=no;status=no;");
	}
	function deleteCustomTerm(customTerm){
		if(!confirm('Are you sure you want to delete the custom term "'+customTerm.substring(0, customTerm.length-4)+'" ?')) return;
		document.location = '<c:url value="/report/rcdc/deleteCustomTerm.grs" />?custName=' + customTerm.replace(/&/g, "%26");
	}
	</script>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="styles/report.css" />
    <link rel="stylesheet" href="styles/style.css" type="text/css" media="screen" />
   	<link rel="stylesheet" type="text/css" href="styles/toolbarButtons.cssx"/>
   	<link rel="stylesheet" type="text/css" href="styles/chromemenu.css" />
  </head>

<body id="home">
  <%@include file="/jsp/include/header.jspf"%>
  <div id="container">
    <div id="content">
    	<div id="ddtabs5" class="chromemenu">
			<ul>
				<li><a href="default.jsp" rel="cm1">HOME</a></li>
			</ul>
  		</div>
    	<br/><br/>
		<center><h1>Manage Custom NIH Terms</h1></center>
		<br/><br/><br/>
		<div style="width:70%; margin:0 auto;"><input type="button" value=" Add New " onclick="addCustomTerm();" title="Add new custom NIH term"/></div>
		<hr width="70%"/>
		<div style="width:70%; height: 500px; margin:0 auto; text-align:center; overflow: auto">
		<table id="titleTable" >
			<tr>
				<th>Custom Term</th>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th>Edit</th>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th>Delete</th>
			</tr>
			<tr><td  colspan=5>&nbsp;</td></tr>
			<c:set var="toBeReplaced" value="'"/>
			<c:set var="replaceWith" value="\\'"/>
			<c:forEach items="${customNihTerms}" var="term">
			<tr>
				<td align="left">${fn:substring(term, 0, fn:length(term)-3)}</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td ><button id="edit" onclick="editCustomTerm('${fn:replace(term,toBeReplaced,replaceWith)}');" title="Edit custom NIH term"></button></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td ><button id="delete" onclick="deleteCustomTerm('${fn:replace(term,toBeReplaced,replaceWith)}');" title="Delete custom NIH term"></button></td>
			</tr>
			</c:forEach>
		</table>
		</div>
		<hr width="70%"/>
		<br/>
	<br/>
	<br/>
	</div> <!-- content -->
	<%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
