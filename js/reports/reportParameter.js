/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2007 Altum, Inc.
 * All rights reserved.
 */
var validationRoutines = new Array();
var toggleOnLoad = new Array();

function initializePage() {
  for (var j=0;j<toggleOnLoad.length;j++){
    eval(toggleOnLoad[j]);
  }
}//end initializePage

function validate() {
  clearHiddenValues();
  for (i=0; i<validationRoutines.length; i++) { //alert("validate["+i+"]="+validationRoutines[i]);
    bSuccess = eval(validationRoutines[i]);
    if (!bSuccess) return false;
  }
  return true;
}//end validate function

// ********************* DSIPLAY CONTROL FUNCTIONS *****************************
function toggleGroup(parmName,img,display) {
  var row;
  row = document.forms[0].all[parmName];
  display = display?display:(!row.style.display?"none":(row.style.display == "inline"?"none":"inline"));
  row.style.display = display;
  img.src = display == "inline"?"images/minus.gif":"images/plus.gif";
}//end toggleGroup function

function toggleChange(objName) {
  var mainForm = document.forms[0].all;
  var options = mainForm[objName];
  for (i=0;i<options.length;i++){
    var option = options[i];
    if (!mainForm["select_" + option.componentRef]) continue; //No display option is associated with this option
    mainForm["select_" + option.componentRef].style.display = option.checked?"inline":"none";
    if (!mainForm[option.componentRef + "_FOOT"]) continue; //No footer option is associated with this select
    mainForm[option.componentRef + "_FOOT"].style.display = option.checked?"inline":"none";
  }
}//end toggleChange function

function disableOption(obj,sExemptField){ //alert("inside disableOption()");
  var mainForm = document.forms[0].all;
  for (i=0;i<obj.length;i++){
    var option = obj[i];
    if (option.value!=sExemptField){
      option.disabled = true;
      mainForm["select_" + option.componentRef].style.display = "none";
      mainForm[option.componentRef + "_FOOT"].style.display="none";
    }
  }
}//end disableOption function

function enableOption(obj){ //alert("inside enableOption()");
  for (i=0;i<obj.length;i++) obj[i].disabled = false;
}//end disableOption function


// ************************* UTILITY FUNCTIONS *********************************
/* These functions are used internally to this file and the validation routines*/

function clearHiddenValues() {
  var selects = document.getElementsByTagName("select");
  for (i=0; i<selects.length; i++) {
    if (selects[i].style.display == "none") {
      selects[i].options.selectedIndex = -1; //Clears the listbox.
    }
  }
}//end clearHiddenValues function

function getGroupElementValue(element) {
  var sValue="";
  if (element.length){
    for(var i=0;i<element.length;i++){
      if (element[i].checked) sValue += element[i].value + ",";
    }
    return sValue.substr(0,sValue.length-1);
  }else{
    return '';
  }
} //end getGroupElementValue function

function verifyGroupSelected(element) {
  if (element.length){
    for(var i=0;i<element.length;i++){
      if(element[i].checked) return true;
    }
  }
  return false;
}//end verifyGroupSelected function

function getSelectedElements(element,delim) {
  var sValue="";
  if (element.options.length){
    for(var i=0;i<element.options.length;i++){
      if (element.options[i].selected) sValue += element.options[i].value + delim;
    }
    return sValue.substr(0,sValue.length-delim.length);
  }else{
    return '';
  }
} //end getSelectedElements function

function verifyItemsSelected(element) {
  if (element.selectedIndex >= 0) return true;
  return false;
}//end verifyItemsSelected function

function selectedValues(obj,ref,label){
  var elem = document.getElementById(obj);
  var child = elem.firstChild;
  elem.style.display = "inline";
  child.innerHTML = label + " - " + getSelectedElements(ref,";");
}//end  selectedValues function

function countSelectedValues(element){
 var count=0;
  if (element.options.length){
    for(var i=0;i<element.options.length;i++){
      if (element.options[i].selected) count = count+1;
    }
  }
  return count;
}//end countSelectedValues function