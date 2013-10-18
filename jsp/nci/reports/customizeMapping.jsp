<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
 <%@page language="java" session="true" 
		import="com.altum.beans.OptionSelection,
				com.altum.components.renderers.OptionSelectionRenderer,
				com.altum.jdbc.web.ReportController,
				com.altum.beans.Code,
				java.util.List" %>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="altum"	uri="/WEB-INF/tlds/altum.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
List<String> reportFy = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_FY);
List<String> reportDisease = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_DISEASE);
String oldCustomName = (String)request.getAttribute("oldCustomName");
String nihTerm = (String)request.getAttribute("nihTerm");
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>Customize Mapping</title>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" href="styles/style.css" type="text/css" media="screen" />
    <style>
    /* Option Selection Classes */
		.optionSelectionTable {
			border:1px inset; 
			padding-right: 5px; 
			padding-bottom: 5px;
			background-color: #ededed;	
		}
		.osMoveUpAndDownCell {display:none;}
		.osSortRow {display:none;}
		.txtBodyLabel{
			font-weight: bold;
			color: #000000;
			text-decoration:none;
			font-size:9pt;
		}
    </style>
    <script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/optionselection/OptionSelection.js" />"></script>
	<script>
	function nihTermSelected(){
		document.getElementById("nciTermsDiv").innerHTML = '<br><i>Loading mapped NCI terms .. </i><br><img src="<c:url value="/images/progress.gif"/>">';
		var callback =
		{
			success:loadNCITerms,
			failure:responseFailure
		}
		var url = '<c:url value="/report/rcdc/getMappedNciTermsForObName.grs" />';
		url += '?nih_name=' + document.custMapForm.nihTerms.value.replace(/&/g, "%26");
		YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	function loadNCITerms(obj){
		document.getElementById("nciTermsDiv").innerHTML = obj.responseText;
		nciTerms = new OptionSelection(document.all._nciTerms_LHS,document.all._nciTerms_RHS);
		nciTerms.setOneRight(document.all._nciTerms_oneRight);
		nciTerms.setOneLeft(document.all._nciTerms_oneLeft);
		nciTerms.lhsSortAsc=true;
		nciTerms.setAllRight(document.all._nciTerms_allRight);
		nciTerms.setAllLeft(document.all._nciTerms_allLeft);
		nciTerms.rhsSortAsc=true;
		nciTerms.setMoveUp(document.all._nciTerms_moveUp);
		nciTerms.setMoveDown(document.all._nciTerms_moveDown);
	}
	function saveMapping(){
		document.custMapForm.custName.value = document.custMapForm.custName.value.replace(/^\s+|\s+$/g,"");
		if(document.custMapForm.custName.value.length > 75){
			alert("The custom term name you have entered exceeds the allowed length of 75.\nPlease shorten the name and try again.");
			document.custMapForm.custName.focus();
			document.custMapForm.custName.select();
			return;
		}
		if(document.custMapForm.custName.value == ""){
			alert("Please enter a custom term name.");
			document.custMapForm.custName.focus();
			return;
		}
		if(document.custMapForm.nihTerms.value == ""){
			alert("Please pick one NIH term.");
			return;
		}
		if(nciTerms.getRHSCount()==0){
			alert("Please pick atleast one NCI term.");
			return;
		}
		var callback =
		{
			success:mappingSaved,
			failure:responseFailure
		}
		var url = '<c:url value="/report/rcdc/saveMapping.grs" />';
		url += '?nih_name=' + document.custMapForm.nihTerms.value.replace(/&/g, "%26");
		url += '&custName=' + document.custMapForm.custName.value.replace(/&/g, "%26");
		url += '&oldCustomName=' + document.custMapForm.oldCustomName.value.replace(/&/g, "%26");
		url += '&nci_names='; 
		for(var i=0, rhsCount=nciTerms.getRHSCount(); i<rhsCount; ++i){
			url += nciTerms.RHSOption[i].value;
		}
		YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	function mappingSaved(obj){
		if(obj.responseText == ''){
			alert("The custom term name you have entered is already used. \nPlease enter a new name.");
			document.custMapForm.custName.select();
			document.custMapForm.custName.focus();
		}else{
			var arg = window.dialogArguments;
			if(arg!=null && arg.diseaseObj!=null){
				arg.diseaseObj.innerHTML = obj.responseText;
			}else{
				//arg is parent window. refresh.
				arg.location=arg.location;
			}
			window.close();
		}
	}
	function responseFailure(obj){
		var msg = "There was a problem processing your request. Report the following error to your support staff: ";
		msg += "Status:" + obj.status + " Message:" + obj.statusText;
		alert(msg);
	}
	</script>
  </head>

<body id="home">
  	<div id="container">
    	<div id="content">
    	<form name="custMapForm">
    	<table>
    	<tr>
		<td colspan=2>&nbsp;</td>
		</tr>
		<tr>
		<td>
    	Custom term name
    	</td>
    	<td>
    	<input type="text" name="custName" size=75 value='<%= oldCustomName!=null?oldCustomName.substring(0, oldCustomName.length()-4):"" %>'/>
    	<input type="hidden" name="oldCustomName" size=75 value='<%= oldCustomName!=null?oldCustomName:"" %>'/>
    	</td>
    	</tr>
		<tr>
		<td colspan=2>&nbsp;</td>
		</tr>
		<tr>
		<td>
    	RCDC Category<br/><i>(Pick one)</i>
    	</td>
    	<td>
    	<select name='nihTerms' size='8' onchange='nihTermSelected();'>
		<%for(int i=0;i<reportDisease.size();i++){%>
			<%if(reportDisease.get(i).indexOf(" (C)")==-1){%>
				<option value="<%=reportDisease.get(i)%>" <%if(reportDisease.get(i).equals(nihTerm)){%>selected<%}%> >
					<%=reportDisease.get(i)%>
				</option>
			<%}%>
		<%}%>
		</select>
    	</td>
    	</tr>
    	<tr>
		<td colspan=2>&nbsp;</td>
		</tr>
    	<tr>
    	<td>
    	NCI Code
    	</td>
    	<td>
    	<div id="nciTermsDiv">
    	<%@ include file="includeNciTerms.jspf" %>
		</div>
		</td>
		</tr>
		<tr>
		<td colspan=2>&nbsp;</td>
		</tr>
		</table>
		<br/>
		<center>
		<input type=button onclick="saveMapping();" value="Save Mapping" />
		<input type=button onclick="window.close();" value="Cancel" />
		</center>
		</form>
		</div>
  	</div> <!-- container -->
 </body>
</html>
