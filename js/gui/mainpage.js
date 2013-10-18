/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */

function _mainPageActions () {
  this.executeJS = new Function('js','return eval(js);');
  this._windowUnload = window.unload;
  this._unload = _Navigation_mainPageUnload;
}

function _Navigation_mainPageUnload() {
  top.navigation.setMain(null);
}

_mpa = new _mainPageActions();
window.unload = new Function('_mpa._unload(); if (_mpa._windowUnload != null) _mpa._windowUnload();');
if(top.navigation){
	top.navigation.setMain(_mpa);
}

function openOnlineHelp(namedDest,helpFileName){
	var url = document.ServletBaseHREF+'jsp/onlineHelpFrame.jsp';
	url += "?helpFileName=" + (helpFileName?helpFileName:'UserBinder.pdf');
	url += '&namedDest='+(namedDest?namedDest:'');
	if (top.onlineHelpWindow){
		top.onlineHelpWindow.close();
	}
	top.onlineHelpWindow = window.open();
	top.onlineHelpWindow.location=url;
}

//Used for the dataview section
var comingFromADataViewPage = false;
function lastDataviewUsed(){
	var url = "ProxyServlet?";
	url+= "objectHandle=DataView";
	url+= "&actionHandle=lastDataviewUsed";
	url+= "&nextPage=jsp/dataview/dataView.jsp";
	if(comingFromADataViewPage){
		url+= "&usePriorDV=true";
	}
	top.navigation.mainLoadURL(url);
}

/** Report Writer methods **/
var reportWindow;
function executeReport(id){
	//Pop out the report so the users can more easily print/save the reports.
	reportWindow = window.open(document.ServletBaseHREF+"qm/report/reportWriterPdfFrame.jsp?reportId=" + id, "reportWindow");
	reportWindow.focus();
}

function loadNewReport(){
	//Do a close because a disable can prevent the fv from loading, causing the "deploy" page to fail in the wizard.
	if (top.navigation){
		top.navigation.tbLoad();
		top.navigation.fvLoad("Reports");
		top.navigation.fvClose();
	}
	top.navigation.mainLoadURL("report/create.grs"); 
}
/**************/

//NOTE: this function belongs in the folderViewScript.jsp
/* MOD-BEA20030417_01
Added function to call loadingStatus.html page
*/
function openStatus(){
  var w=screen.width; var x=((w/2)-200);
  var h=screen.height; var y=((h/2)-150);
  top.waitWin = window.open("loadingStatus.html","expwin","height=180,width=200,resizable=no,left="+x+",top="+y); //file is relative to calling page
}
/* end MOD-BEA20030417_01  */
