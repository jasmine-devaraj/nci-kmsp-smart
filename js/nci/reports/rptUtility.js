/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2007 Altum, Inc.
 * All rights reserved.
 */
function toggleView(bShow){
	/*
	desc: This function is used to display/hide the user parameters on the report.
	params: bShow - a boolean that determines if the parameters are displayed or hidden.
	return: none
	*/
	var divHide	= document.all.parmsHide;
	var divShow	= document.all.parmsShow;
	var divID	= document.all.parms;
	if (bShow){
		divShow.style.display='none';
		divHide.style.display='inline';
		divID.style.display='inline';
	}else{
		divID.style.display='none';
		divHide.style.display='none';
		divShow.style.display='inline';
	}
}
// functions used by Yahoo Conenction Library when making AJAX calls.
// called when AJAX connection succeeds

function success(obj){
	var msg = 'success';
	// hides the timer Div and displays the jmesa table Div.
	var div = document.getElementById('timer');
	div.style.display = 'none';
	var div1 = document.getElementById('toggle');
	div1.style.display = 'inline';
	var div2 = document.getElementById('rows');
	div2.innerHTML = obj.responseText;
	// calls to javascript functions for jmesa
	addTableFacadeToManager('row');
	setMaxRowsToLimit('row',25);
	setPageToLimit('row',1);
	
}

// called when AJAX connection fails
function responseFailure(obj){
	var msg = 'There was a problem ';
	msg += 'Status:' + obj.status + ' Message:' + obj.statusText;
	alert(msg);
	var div = document.getElementById('timer');
	div.style.display = 'none';
	var div1 = document.getElementById('rows');
	if(obj.responseText != undefined){
		div1.innerHTML = '<li>Transaction id: ' + obj.tId + '</li>';
		div1.innerHTML += '<li>HTTP status: ' + obj.status + '</li>';
		div1.innerHTML += '<li>Status code message: ' + obj.statusText + '</li>';
		div1.innerHTML += obj.responseText;
	}
	else{
		div1.innerHTML += '<li>Status code message: ' + obj.statusText + '</li>';
	}
}
