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
<%! String sReportPage;
	String sParameters;
	String sTitle;
	List<String> reportFy, reportMechanism;
%>
<%  sReportPage = "jsp/nci/reports/rcdcCompare.jsp";
	boolean bJsp = sReportPage.endsWith("jsp"); // specifies if the report page is a JSP or ASP.
	reportFy = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_FY);
	reportMechanism = (List<String>)session.getAttribute(ReportController.SESSION_REPORT_MECHANISM);
	sParameters = "compareRCDC";
	sTitle = "RCDC Report";
	if(bJsp){
		session.removeAttribute(JdbcController.SESSION_RESULTSETDATA_LIST_TOKEN);
		session.removeAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
  	<title>NCI Knowledge Management and Special Projects Home</title>
  	<%@include file="/jsp/baseHRef.jsp"%>
	<script src="<c:url value="/js/gui/mainpage.js" />" language="Javascript" ></script>
	<script src="js/reports/reportParameter.js" language="JavaScript"></script>
	<script src="js/reports/validation.js" language="JavaScript"></script>
	<script>
	function customizeMapping(){
		var obj = new Object;
		obj.diseaseObj = document.getElementById('selectDisease');
		window.showModalDialog("<c:url value="/jsp/nci/reports/customizeMapping.jsp" />",obj,"dialogWidth=1000px;dialogHeight=500px;resizable=no;help=no;status=no;");
	}
	</script>
	<altum:Include resource="jsp.reports.control.include"/>
	<altum:Include resource="jsp.reports.validation.include"/>
  	<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" type="text/css" href="styles/report.css" />
    <link rel="stylesheet" href="styles/style.css" type="text/css" media="screen" />
   	<link rel="stylesheet" type="text/css" href="styles/chromemenu.css" />
  </head>

<body id="home" onload="initializePage();">
  <%@include file="/jsp/include/header.jspf"%>
  <div id="container">
    <div id="content">
    <div id="ddtabs5" class="chromemenu">
		<ul>
			<li><a href="default.jsp" rel="cm1">HOME</a></li>
		</ul>
  	</div>
  	<br/><center><h1>RCDC vs RAEB Comparison Report</h1>
  	<h3>Search Parameters</h3></center>
		<form name="rptParmsForm" method="POST" action="jsp/nci/reports/rcdcCompare.jsp" >
			<input type="hidden" name="sParameters" value="compareRCDC" >
			<input type="hidden" name="REPORT_PAGE" value="jsp/nci/reports/rcdcCompare.jsp" >
			<input type="hidden" name="PAGE_TITLE" value="RCDC Report" >
			<input type="hidden" name="Report_Title" value="" >
			<input type="hidden" name="jsessionid" value="<%=((Integer)(session.getAttribute(ReportController.SESSION_REPORT_ID))).intValue() %>" >
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
						<input type="radio" name="runBy" value="Application ID" checked> Application ID<br>
						<input type="radio" name="runBy" value="Serial Number"> Serial Number<br>
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
				<tr>
					<td colspan='2'>FY</td>
					<td><img align='right' onClick="toggleGroup('Fiscal Year_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
				</tr>
				</thead>
				<tr id='Fiscal Year_ROW'>
					<td valign='top' width='20%'>
						<select name='rcdcFY' size='1'>
							<%for(int i=0;i<reportFy.size();i++){%>
								<option value='<%=reportFy.get(i)%>'><%=reportFy.get(i)%></option>
							<%}%>
						</select>
					</td>
					<td valign='top'></td>
				</tr>
				<tr id='rcdcFY_FOOT' class='selectedValues hiddenDetail'>
					<td colspan='3'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='2'>Project Type</td>
						<td><img align='right' onClick="toggleGroup('Project Type_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='Project Type_ROW'>
					<!--<td valign='top' width='20%'>
						<input id='ProjectTypeSimple_Grants' checked value='Grants' name='ProjectTypeSimple' componentRef='' type='RADIO'>&nbsp;<span id='ProjectTypeSimple_Grants_SPAN' class='spanNormal'>Grants</span><br>
						<input id='ProjectTypeSimple_Contracts' value='Contracts' name='ProjectTypeSimple' componentRef='' type='RADIO'>&nbsp;<span id='ProjectTypeSimple_Contracts_SPAN' class='spanNormal'>Contracts</span><br>
						<input id='ProjectTypeSimple_Both' value='Both' name='ProjectTypeSimple' componentRef='' type='RADIO'>&nbsp;<span id='ProjectTypeSimple_Both_SPAN' class='spanNormal'>Both</span><br>
					</td>-->
					<td valign='top' width='20%'>
						<input type="checkbox" name="ProjectTypeSimple" value="All" checked> All<br>
						<input type="checkbox" name="ProjectTypeSimple" value="Grants"> Grants<br>
						<input type="checkbox" name="ProjectTypeSimple" value="Intramural"> Intramural<br>
						<input type="checkbox" name="ProjectTypeSimple" value="Contracts"> Contracts<br>
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
				<tr>
					<td colspan='2'>Source</td>
					<td><img align='right' onClick="toggleGroup('Source_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
				</tr>
				</thead>
				<tr id='Source_ROW'>
					<td valign='top' width='20%'>
						<select name='source' size='1'>
							<option value='All'>All</option>
							<option value='NCI'>NCI</option>
							<option value='RCDC'>RCDC</option>
							<option value='Both'>Both</option>
						</select>
					</td>
					<td valign='top'></td>
				</tr>
				<tr id='source_FOOT' class='selectedValues hiddenDetail'>
					<td colspan='3'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='2'>Mechanism</td>
						<td><img align='right' onClick="toggleGroup('Mechanism_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='Mechanism_ROW'>
					<!--<td valign='top' width='20%'><select size='5' multiple name='mechanismOptionMulti' onChange="selectedValues('mechanismOptionMulti_FOOT',this,'Mechanism')"><option value='C06'>C06</option><option value='D43'>D43</option><option value='DP1'>DP1</option><option value='F30'>F30</option><option value='F31'>F31</option><option value='F32'>F32</option><option value='F33'>F33</option><option value='F34'>F34</option><option value='G08'>G08</option><option value='G11'>G11</option><option value='G13'>G13</option><option value='K01'>K01</option><option value='K02'>K02</option><option value='K04'>K04</option><option value='K05'>K05</option><option value='K07'>K07</option><option value='K08'>K08</option><option value='K11'>K11</option><option value='K12'>K12</option><option value='K14'>K14</option><option value='K18'>K18</option><option value='K22'>K22</option><option value='K23'>K23</option><option value='K24'>K24</option><option value='K25'>K25</option><option value='K30'>K30</option><option value='K99'>K99</option><option value='KL1'>KL1</option><option value='L30'>L30</option><option value='L40'>L40</option><option value='L50'>L50</option><option value='L60'>L60</option><option value='N01'>N01</option><option value='N02'>N02</option><option value='N03'>N03</option><option value='N43'>N43</option><option value='N44'>N44</option><option value='P01'>P01</option><option value='P20'>P20</option><option value='P30'>P30</option><option value='P40'>P40</option><option value='P41'>P41</option><option value='P50'>P50</option><option value='P60'>P60</option><option value='PL1'>PL1</option><option value='Q99'>Q99</option><option value='R00'>R00</option><option value='R01'>R01</option><option value='R03'>R03</option><option value='R09'>R09</option><option value='R10'>R10</option><option value='R13'>R13</option><option value='R15'>R15</option><option value='R18'>R18</option><option value='R21'>R21</option><option value='R23'>R23</option><option value='R24'>R24</option><option value='R25'>R25</option><option value='R26'>R26</option><option value='R29'>R29</option><option value='R33'>R33</option><option value='R34'>R34</option><option value='R35'>R35</option><option value='R37'>R37</option><option value='R41'>R41</option><option value='R42'>R42</option><option value='R43'>R43</option><option value='R44'>R44</option><option value='R55'>R55</option><option value='R56'>R56</option><option value='RC1'>RC1</option><option value='RC2'>RC2</option><option value='RC3'>RC3</option><option value='RC4'>RC4</option><option value='RL1'>RL1</option><option value='RL5'>RL5</option><option value='RL9'>RL9</option><option value='S06'>S06</option><option value='S07'>S07</option><option value='S10'>S10</option><option value='S15'>S15</option><option value='S21'>S21</option><option value='S22'>S22</option><option value='SC1'>SC1</option><option value='SC2'>SC2</option><option value='T15'>T15</option><option value='T32'>T32</option><option value='T34'>T34</option><option value='T35'>T35</option><option value='T36'>T36</option><option value='TL1'>TL1</option><option value='TU2'>TU2</option><option value='U01'>U01</option><option value='U09'>U09</option><option value='U10'>U10</option><option value='U13'>U13</option><option value='U18'>U18</option><option value='U19'>U19</option><option value='U24'>U24</option><option value='U26'>U26</option><option value='U2R'>U2R</option><option value='U41'>U41</option><option value='U42'>U42</option><option value='U43'>U43</option><option value='U44'>U44</option><option value='U54'>U54</option><option value='U56'>U56</option><option value='UH2'>UH2</option><option value='X02'>X02</option><option value='Y01'>Y01</option><option value='Y02'>Y02</option><option value='Z01'>Z01</option><option value='Z98'>Z98</option><option value='Z99'>Z99</option></select></td>-->
					<td valign='top' width='60%'>
						<table>
							<tr>
							<%for(int i=0;i<reportMechanism.size();i++){%>
								<td><input type="checkbox" name="mechanismOptionMulti" value="<%=reportMechanism.get(i)%>"> <%=reportMechanism.get(i)%></td>
								<%if(((i+1)%10) == 0){%>
									</tr><tr>
								<%}%>
							<%}%>
							</tr>
						</table>
					</td>
					<td valign='top'></td>
				</tr>
				<tr id='mechanismOptionMulti_FOOT' class='selectedValues hiddenDetail'>
					<td colspan='3'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='2'>Disease Category</td>
						<td><img align='right' onClick="toggleGroup('Disease Area_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='Disease Area_ROW'>
					<td valign='top' width='20%'>
					<div id="selectDisease">
					<%@ include file="selectDisease.jspf" %>
					</div>
					</td>
					<td valign='top'><a onclick="javascript:customizeMapping();">Customize Mapping</a></td>
				</tr>
				<tr id='obDiseaseArea_FOOT' class='selectedValues hiddenDetail'>
					<td colspan='3'></td>
				</tr>
			</table>
			<br>
			<table width='100%'>
				<thead>
					<tr>
						<td colspan='2'>Include Coding Detail</td>
						<td><img align='right' onClick="toggleGroup('INCLUDE_CODING_DETAIL_ROW',this);" id='toggleIcon' class='navigationImage' alt='Expand or collapse this parameter' src='images/minus.gif'></td>
					</tr>
				</thead>
				<tr id='INCLUDE_CODING_DETAIL_ROW'>
					<td valign='top' width='20%'>
						<input type="checkbox" name="IncludeCodingDetail" value="CodingDetail" checked> Include Coding Detail
					</td>
					<td valign='top'></td>
				</tr>
			</table>
			<br>
			<input name='sInputParams' type='HIDDEN' value='runBy,rcdcFY,ProjectTypeSimple,source,mechanismOptionMulti,obDiseaseArea'>
		</div>
		</form>
		<br>
		<div>
			<input type=button onClick="if (validate()) document.forms[0].submit()" name="Execute" value="Execute" />
			<input type=button onClick="document.location=document.location" name="Reset" value="Reset" />
		</div>
    </div>
	<br>
	<br>
    <%@include file="/jsp/include/footer.jspf"%>
  </div> <!-- container -->
 </body>
</html>
