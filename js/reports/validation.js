/*

  filename:  validation.js
      date:  10/13/2003
written by:  BEA, GAG

      desc:  This file contains generic functions used to validate parameter
             page components.  Client specific components can be found in the
             {appName}\web\js\{client}\reports directory.
*//******************************************************************************/
function validateDetailParam(paramName,objName,desc){ //alert("BASE-->validateDetailParam");
/*
  desc: This function validates user input on every piece of a detail parameter.
params: paramName - The label describing the parameter
        objName - The parameter object name
        desc - A descriptor of the detail parameter to include in the error
return: boolean
*/
  var mainForm = document.forms[0].all;
  var options = mainForm[objName];
  //verify anything is selected
  if (!verifyGroupSelected(options)){
    alert("Please select a " +paramName);
    return false;
  }
  //verify children are selected
  for (var i=0;i<options.length;i++){
    var option = options[i];
    if ((option.checked)&&!(option.disabled)){
      var detailComp = mainForm["select_" + option.componentRef]; //verify the option has details
      if (detailComp){
        if (verifyItemsSelected(detailComp)){ //verify some detail is selected
          //if something is selected and this is a radio group then we can break.
          //There can be no more options with detail selected.
          if (option.type=="radio") break;
        }else{
          spanObj.className = "spanError";
          alert("Please select a " + option.value + " " + desc);
          return false;
        }
      }else{
        break; //break from loop if there is no detail to check
      }
    }
  }
  return true; //default for return validation script
}//end validateDetailParam function
/******************************************************************************/
function validateSelectList(paramName,objName){ //alert("validateSelectList");
/*
  desc: This function validates any option parameter select list
params: paramName - The label describing the parameter
        objName - The parameter object name
return: boolean
*/
  var mainForm = document.forms[0].all;
  if (!verifyItemsSelected(mainForm[objName])){
    alert("Please select a " + paramName);
    return false;
  }
  return true;
}//end validateSelectList function
/******************************************************************************/
/* end validation.js */
