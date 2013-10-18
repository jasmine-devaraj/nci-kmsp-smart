<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@page language="java" session="true" 
		import="com.altum.jdbc.web.JdbcController,
				com.altum.jdbc.web.ReportController,
				com.altum.beans.Code" %>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="altum"	uri="/tlds/altum.tld"%>
<%
List<String> reportFy, cancerActivity, division, pdDesc, rfaPaNumber;
cancerActivity = (List<String>)session.getAttribute(ReportController.SESSION_CANCER_ACTIVITY);
division = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_DIVISION);
pdDesc = (List<String>)session.getAttribute(ReportController.SESSION_PD_DESCRIPTION);
rfaPaNumber = (List<String>)session.getAttribute(ReportController.SESSION_RFAPA_NUMBER);
reportFy = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_FY);
List<Code> reportNCICodes = (List<Code>)session.getAttribute(ReportController.SESSION_REPORT_NCI_CODES);
session.removeAttribute(JdbcController.SESSION_RESULTSETDATA_LIST_TOKEN);
session.removeAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<%@include file="/jsp/baseHRef.jsp"%>
  	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>
	<script src="<c:url value="/js/gui/mainpage.js" />" language="Javascript" ></script>
	<script src="js/reports/reportParameter.js" language="JavaScript"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/yahoo/yahoo.js" />"></script>
	<script language="JavaScript" src="<c:url value="/js/gui/yahoo/connection/connection.js" />"></script>
	<script>
	function validate(){
		var newVal = "";
		$('#rcdcFY :selected').each(function(i, selected){ 
		  var val = $(selected).val();
		  newVal+=  $.trim(val) + ",";
		});
		if(newVal == "") {
			alert("Please enter at least one fiscal year.");
			return false;
		}
		newVal = newVal.substr(0, newVal.length-1);
		document.rptParmsForm.fyVal.value = newVal;
		var runByVal = "";
		var validationFailed = false;
	$(':checkbox:checked').each(function(i, selected){ 
		  var val = $(selected).val();
		  runByVal += val + ",";
		  if(val == "Institution"){
			var val2 = document.rptParmsForm.Institution.value; 
			var params2 = val2.split(",");
			var newVal = "";
			for(i2=0;i2<params2.length;i2++){
				if($.trim(params2[i2]).length > 0){
					newVal+= "'%" + $.trim(params2[i2]) + "%',";
				}
			}
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one institution.");
				document.rptParmsForm.Institution.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.institutionVal.value = newVal; 
		}else if(val == "Principal Investigator"){
			var val2 = document.rptParmsForm.PI.value; 
			var params2 = val2.split(",");
			var newVal = "";
			for(i2=0;i2<params2.length;i2++){
				if($.trim(params2[i2]).length > 0){
					newVal+= "'%" + $.trim(params2[i2]) + "%',";
				}
			}
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one Principal Investigator.");
				document.rptParmsForm.PI.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.piNameVal.value = newVal; 
		}else if(val == "Serial Number"){
			var val2 = document.rptParmsForm.SerialNumber.value; 
			var params2 = val2.split(",");
			var newVal = "";
			for(i2=0;i2<params2.length;i2++){
				if($.trim(params2[i2]).length > 0){
					newVal+= "" + $.trim(params2[i2]) + ",";
				}
			}
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one Serial number.");
				document.rptParmsForm.SerialNumber.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.serialNumberVal.value = newVal; 
		}else if(val == "SIC Code"){
			var newVal = "";
			$('#sicCodeName :selected').each(function(i, selected){ 
			  var val = $(selected).val();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one SIC Code.");
				document.rptParmsForm.sicCodeName.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.sicCodeNameVal.value = newVal; 
		}else if(val == "SITE Code"){
			var newVal = "";
			$('#siteCodeName :selected').each(function(i, selected){ 
			  var val = $(selected).val();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one SITE Code.");
				document.rptParmsForm.siteCodeName.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.siteCodeNameVal.value = newVal; 
		} else if(val == "CSO Code"){
			var newVal = "";
			$('#csoCodeName :selected').each(function(i, selected){ 
			  var val = $(selected).val();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one CSO Code.");
				document.rptParmsForm.csoCodeName.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.csoCodeNameVal.value = newVal; 
		} else if(val == "Cancer Activity"){
			var newVal = "";
			$('#cancerActivity :selected').each(function(i, selected){ 
			  var val = $(selected).text();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one Cancer Activity.");
				document.rptParmsForm.cancerActivity.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.cancerActivityVal.value = newVal; 
		} else if(val == "Division"){
			var newVal = "";
			$('#division :selected').each(function(i, selected){ 
			  var val = $(selected).text();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one Division.");
				document.rptParmsForm.division.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.divisionVal.value = newVal; 
		} else if(val == "PD Description"){
			var newVal = "";
			$('#pdDescription :selected').each(function(i, selected){ 
			  var val = $(selected).text();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one PD Description.");
				document.rptParmsForm.pdDescription.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.pdDescriptionVal.value = newVal; 
		} else if(val == "RFAPA Number"){
			var newVal = "";
			$('#rfaPaNumber :selected').each(function(i, selected){ 
			  var val = $(selected).text();
			  newVal+= "'" + $.trim(val) + "',";
			});
			if(newVal != "" && newVal != "'%%',") {
				newVal = newVal.substr(0, newVal.length-1);
			}
			else {
				alert("Please enter at least one RFA/PA Number.");
				document.rptParmsForm.rfaPaNumber.focus();
				validationFailed = true;
				return false;
			}
			document.rptParmsForm.rfaPaNumberVal.value = newVal; 
		}
		
		});	
		if(runByVal!="") runByVal = runByVal.substr(0, runByVal.length-1);
		document.rptParmsForm.runByVal.value = runByVal; 
		return !validationFailed;
				
		

	}
	function runProjectCodingReport(){
		var url = '<c:url value="/report/rcdc/runQueryReport.grs" />?searchParam=' + document.forms[0].searchParamVal.value;
		url += '&runBy=' + $("#runBy:checked").val();
		document.location = url;
	}
	
	function updateSearchParamSpan(obj){
		
		var val = "none";
		if(obj.checked) val = "inline";
		if(obj.value ==  "Institution"){
			$("#InstitutionDiv").css("display", val);
			if(val == "inline") document.rptParmsForm.Institution.focus(); 		}
		if(obj.value ==  "Principal Investigator"){
			$("#PIDiv").css("display", val);
			if(val == "inline") document.rptParmsForm.PI.focus(); 		}
		if(obj.value ==  "Serial Number"){
			$("#SerialNumberDiv").css("display", val);
			if(val == "inline") document.rptParmsForm.SerialNumber.focus(); 		}
		if(obj.value ==  "SIC Code"){
			$("#sicCodeNameSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.sicCodeName.focus(); 		}
		if(obj.value ==  "SITE Code"){
			$("#siteCodeNameSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.siteCodeName.focus(); 		}
		if(obj.value ==  "CSO Code"){
			$("#csoCodeNameSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.csoCodeName.focus(); 		}	
		if(obj.value ==  "Cancer Activity"){
			$("#cancerActivitySelect").css("display", val);
			if(val == "inline") document.rptParmsForm.cancerActivity.focus(); 		}
		if(obj.value ==  "Division"){
			$("#divisionSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.division.focus(); 		}	
		if(obj.value ==  "PD Description"){
			$("#pdDescriptionSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.pdDescription.focus(); 		}	
		if(obj.value ==  "RFAPA Number"){
			$("#rfaPaNumberSelect").css("display", val);
			if(val == "inline") document.rptParmsForm.rfaPaNumber.focus(); 		}	
		
		
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
  		<br/><center><h1>Query Report</h1>
  		<h3>Search Parameters</h3></center>
		<form name="rptParmsForm" id="rptParmsForm" method="POST" onSubmit="return validate();" action="jsp/nci/reports/queryReport.jsp" >
		<input type="hidden" name="sParameters" value="compareRCDC" >
		<input type="hidden" name="REPORT_PAGE" value="jsp/nci/reports/queryReport.jsp" >
		<input type="hidden" name="PAGE_TITLE" value="Query Report" >
		<input type="hidden" name="Report_Title" value="" >
		<input type="hidden" name="jsessionid" value="<%=((Integer)(session.getAttribute(ReportController.SESSION_REPORT_ID))).intValue() %>" >
		
		<div>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='10'>Run Report By</td>
						<td><img align='right' onClick="toggleGroup('Run_by_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='Run_by_ROW'>
					<td valign='top' width='20%'>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="Institution" onclick="updateSearchParamSpan(this);"> Institution<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="Principal Investigator" onclick="updateSearchParamSpan(this);"> Principal Investigator<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="Serial Number" onclick="updateSearchParamSpan(this);"> Serial Number<br>
					</td>
					<td valign='top'></td>
					<td valign='top' width='20%'>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="Cancer Activity" onclick="updateSearchParamSpan(this);"> Cancer Activity<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="Division" onclick="updateSearchParamSpan(this);"> Division<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="RFAPA Number" onclick="updateSearchParamSpan(this);"> RFA/PA Number<br>
					</td>
					<td valign='top'></td>
					<td valign='top' width='20%'>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="SIC Code" onclick="updateSearchParamSpan(this);"> SIC Code<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="SITE Code" onclick="updateSearchParamSpan(this);"> SITE Code<br>
					</td>
					<td valign='top'></td>
					<td valign='top' width='20%'>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="CSO Code" onclick="updateSearchParamSpan(this);"> CSO Code<br>
						<input type="checkbox" id="runBy" class="runByClass" name="runBy" value="PD Description" onclick="updateSearchParamSpan(this);"> PD Description<br>
					</td>
					<td valign='top'></td>
					<td valign='top' width='20%'>
						<select multiple name='rcdcFY' id ='rcdcFY' size='3'>
							<%for(int i=0;i<reportFy.size();i++){%>
								<option value='<%=reportFy.get(i)%>'><%=reportFy.get(i)%></option>
							<%}%>
						</select>
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
				<tr>
					<td colspan='2'><span id="searchParam">Search Parameters</span></td>
					<td><img align='right' onClick="toggleGroup('applID_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
				</tr>
				</thead>
				<tr id='applID_ROW'>
					<td valign='top' width='20%'>
						<br>
						<div id="textInput" style="display:none">
							<textarea rows="4" cols="50"  name="runByVal" ></textarea>
							<textarea rows="4" cols="50"  name="fyVal" ></textarea>
							<textarea rows="4" cols="50"  name="institutionVal" ></textarea>
							<textarea rows="4" cols="50"  name="cancerActivityVal" ></textarea>
							<textarea rows="4" cols="50"  name="sicCodeNameVal" ></textarea>
							<textarea rows="4" cols="50"  name="csoCodeNameVal" ></textarea>
							<textarea rows="4" cols="50"  name="piNameVal" ></textarea>
							<textarea rows="4" cols="50"  name="divisionVal" ></textarea>
							<textarea rows="4" cols="50"  name="siteCodeNameVal" ></textarea>
							<textarea rows="4" cols="50"  name="pdDescriptionVal" ></textarea>    
							<textarea rows="4" cols="50"  name="serialNumberVal" ></textarea>
							<textarea rows="4" cols="50"  name="rfaPaNumberVal" ></textarea>
						</div>
						
						<div id="InstitutionDiv" style="display:none">
							<b>Institution</b>
							<input type="text" name="Institution" id ="Institution" size=100>
							<i>* Multiple Institutions seperated by commas can be entered in this field. </i>
							<br>
						</div>
						
						<div id="cancerActivitySelect" style="display:none">
						<br><b>Cancer Activity</b><br>
						<select multiple id='cancerActivity' size='5'>
							<%for(int i=0;i<cancerActivity.size();i++){%>
								<option value='<%=cancerActivity.get(i)%>'><%=cancerActivity.get(i)%></option>
								<%}%>
						</select>
						<br>
						</div>
						
						<div id="sicCodeNameSelect" style="display:none">
						<br><b>SIC Code</b><br>
							<select multiple id='sicCodeName' size='5'>
							<%for(int i=0;i<reportNCICodes.size();i++){
									if(((Code)reportNCICodes.get(i)).getTypeId().equals("SIC")){
									%>
									<option value='<%=((Code)reportNCICodes.get(i)).getName().substring(6,((Code)reportNCICodes.get(i)).getName().length())%>'><%=((Code)reportNCICodes.get(i)).getId() + " - " + ((Code)reportNCICodes.get(i)).getName().substring(6,((Code)reportNCICodes.get(i)).getName().length())%></option>
									<%
									}	
								}%>
						</select>
						<br>
						</div>
						
						<div id="csoCodeNameSelect" style="display:none">
						<br><b>CSO Code</b><br>
							<select multiple id='csoCodeName' size='5'>
							<%for(int i=0;i<reportNCICodes.size();i++){
									if(((Code)reportNCICodes.get(i)).getTypeId().equals("CSO")){
									%>
									<option value='<%=((Code)reportNCICodes.get(i)).getName().substring(6,((Code)reportNCICodes.get(i)).getName().length())%>'><%=((Code)reportNCICodes.get(i)).getId() + " - " + ((Code)reportNCICodes.get(i)).getName().substring(6,((Code)reportNCICodes.get(i)).getName().length())%></option>
									<%
									}	
								}%>
						</select>
						<br>
						</div>
						
						<div id="PIDiv" style="display:none">
						<br><b>Principle Investigator</b>
							<input type="text" name="PI" id = "PI" size=100>
							<i>* Multiple PIs seperated by commas can be entered in this field. </i>
							<br>
						</div>
						
						<div id="divisionSelect" style="display:none">
						<br><b>Division</b><br>
							<select multiple id='division' size='5'>
							<%for(int i=0;i<division.size();i++){%>
								<option value='<%=division.get(i)%>'><%=division.get(i)%></option>
								<%}%>
						</select>
						<br>
						</div>
						
						<div id="siteCodeNameSelect" style="display:none">
						<br><b>SITE Code</b><br>
							<select multiple id='siteCodeName' size='5'>
							<%for(int i=0;i<reportNCICodes.size();i++){
									if(((Code)reportNCICodes.get(i)).getTypeId().equals("SITE")){
									%>
									<option value='<%=((Code)reportNCICodes.get(i)).getName().substring(7,((Code)reportNCICodes.get(i)).getName().length())%>'><%=((Code)reportNCICodes.get(i)).getId() + " - " + ((Code)reportNCICodes.get(i)).getName().substring(7,((Code)reportNCICodes.get(i)).getName().length())%></option>
									<%
									}	
								}%>
						</select>
						<br>
						</div>
						
						<div id="pdDescriptionSelect" style="display:none">
						<br><b>PD Org Description</b><br>
							<select multiple id='pdDescription' size='5'>
							<%for(int i=0;i<pdDesc.size();i++){%>
								<option value='<%=pdDesc.get(i)%>'><%=pdDesc.get(i)%></option>
								<%}%>
						</select>
						<br>
						</div>
						
						<div id="SerialNumberDiv" style="display:none">
						<br><b>Serial Number</b>
							<input type="text" name="SerialNumber" id = "SerialNumber" size=100>
							<i>* Multiple Serial Numbers seperated by commas can be entered in this field. </i>
							<br>
						</div>
						
						<div id="rfaPaNumberSelect" style="display:none">
						<br><b>RFA/PA Number</b><br>
							<select multiple id='rfaPaNumber' size='5'>
							<%for(int i=0;i<rfaPaNumber.size();i++){%>
								<option value='<%=rfaPaNumber.get(i)%>'><%=rfaPaNumber.get(i)%></option>
								<%}%>
						</select>
						<br>
						</div>
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
		</div>
		</form>
		<br>
		<div>
			<input type=button id="execute" onClick="if (validate()) document.forms[0].submit()" name="Execute" value="Execute" />
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
