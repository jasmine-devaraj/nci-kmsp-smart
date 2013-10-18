/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */

function OptionSelection(aLHSOption,aRHSOption){
//----------------------------------------------------
// Class:      OptionSelection
//
// Definition: This client side object defines a gui component which
//				is composed of two option boxes, and buttons
//				which allow the user to move items from one
//				box to another.  The two boxes are referred to
//				as LHS (Left Hand Side) and RHS (Right Hand Side).
//----------------------------------------------------

// ATTRIBUTES
  this.lhsSortAsc = true;
  this.rhsSortAsc = null;
  this.LHSOption = aLHSOption; //Left hand options
  this.RHSOption = aRHSOption; //Right hand options
  this.oneRight = null; //Input Button
  this.oneLeft = null; //Input Button
  this.allRight = null; //Input Button
  this.allLeft = null; //Input Button
  this.moveUp = null; //Input Button
  this.moveDown = null; //Input Button
  this.LHSObject = new Object(); //Custom object array
  this.RHSObject = new Object(); //Custom object array

// OPTIONAL FUNCTIONS
  this.changeFunction = null;
  this.oneRightFunction = null;
  this.oneLeftFunction = null;
  this.allRightFunction = null;
  this.allLeftFunction = null;
  this.sortAscFunction = _OptionSelectionSortAscFunction;
  this.sortDescFunction = _OptionSelectionSortDescFunction;

// GETTER METHODS
  this.getLHSOption = new Function('return this.LHSOption;');
  this.getRHSOption = new Function('return this.RHSOption;');
  this.getLHSCount = new Function('return this.LHSOption.length;');
  this.getRHSCount = new Function('return this.RHSOption.length;');
  this.getLHSValues = _OptionSelectionGetLHSValues;
  this.getRHSValues = _OptionSelectionGetRHSValues;
  this.getLHSValuesArray = _OptionSelectionGetLHSValuesArray;
  this.getRHSValuesArray = _OptionSelectionGetRHSValuesArray;
  this.getLHSItem = _OptionSelectionGetLHSItem;
  this.getRHSItem = _OptionSelectionGetRHSItem;
  this.getLHSObject = _OptionSelectionGetLHSObject;
  this.getRHSObject = _OptionSelectionGetRHSObject;

// SETTER METHODS
  this.setSorted = _OptionSelectionSetSorted;
  this.setChangeFunction = _OptionSelectionSetChangeFunction; 
  this.setOneRightFunction = _OptionSelectionSetOneRightFunction; 
  this.setOneLeftFunction = _OptionSelectionSetOneLeftFunction; 
  this.setAllRightFunction = _OptionSelectionSetAllRightFunction; 
  this.setAllLeftFunction = _OptionSelectionSetAllLeftFunction; 
  this.setLHSOption = _OptionSelectionSetLHSOption;
  this.setRHSOption = _OptionSelectionSetRHSOption;
  this.setOneRight = _OptionSelectionSetOneRight;
  this.setOneLeft = _OptionSelectionSetOneLeft;
  this.setAllRight = _OptionSelectionSetAllRight;
  this.setAllLeft = _OptionSelectionSetAllLeft;
  this.setMoveUp = _OptionSelectionSetMoveUp;
  this.setMoveDown = _OptionSelectionSetMoveDown;
  this.addLHSObject = _OptionSelectionAddLHSObject;
  this.addRHSObject = _OptionSelectionAddRHSObject;
  this.setSortAscFunction = _OptionSelectionSetSortAscFunction; 
  this.setSortDescFunction = _OptionSelectionSetSortDescFunction; 

// MANIPULATION METHODS
  this.unselectLHSOption = _OptionSelectionUnselectLHSOption;
  this.unselectRHSOption = _OptionSelectionUnselectRHSOption;
  this.upOne = _OptionSelectionUpOne;
  this.downOne = _OptionSelectionDownOne;
  this.moveOneRight = _OptionSelectionMoveOneRight;
  this.moveOneLeft = _OptionSelectionMoveOneLeft;
  this.moveAllRight = _OptionSelectionMoveAllRight;
  this.moveAllLeft = _OptionSelectionMoveAllLeft;
  this.addOneRight = _OptionSelectionAddOneRight;
  this.addOneLeft = _OptionSelectionAddOneLeft;
  this.updateStatus = _OptionSelectionUpdateStatus;
  this.sortOption = _OptionSelectionSortOption;
  this.sortSelections = sortSelections;
  
// PRIVATE METHODS
  this._OptionSelectionUnselectOption = _OptionSelectionUnselectOption;
  this._OptionSelectionGetOptionValues = _OptionSelectionGetOptionValues;
  this._OptionSelectionGetOptionValuesArray = _OptionSelectionGetOptionValuesArray;
  this._OptionSelectionMoveOne = _OptionSelectionMoveOne;
  this._OptionSelectionMoveAll = _OptionSelectionMoveAll;
  this._OptionSelectionAddOne = _OptionSelectionAddOne;
}
function _OptionSelectionGetLHSValues(){
	return this._OptionSelectionGetOptionValues(this.LHSOption); 
}
function _OptionSelectionGetRHSValues(){
	return this._OptionSelectionGetOptionValues(this.RHSOption);
}
function _OptionSelectionGetOptionValues(aOption){
	var sValues = "";
	for (var i=0;i<aOption.length;i++)
		sValues += escape(aOption[i].value) + ',';
	sValues = sValues.substring(0,sValues.length-1);
	return sValues;
}
function _OptionSelectionGetLHSValuesArray(){
	return this._OptionSelectionGetOptionValuesArray(this.LHSOption); 
}
function _OptionSelectionGetRHSValuesArray(){
	return this._OptionSelectionGetOptionValuesArray(this.RHSOption);
}
function _OptionSelectionGetOptionValuesArray(aOption){
	var arr = new Array();
	for (var i=0;i<aOption.length;i++){
		arr[i] = aOption[i].value;
	}
	return arr;
}
function _OptionSelectionGetLHSItem(aIndex){
	return _OptionSelectionGetItem(aIndex,this.LHSOption);
}
function _OptionSelectionGetRHSItem(aIndex){
	return _OptionSelectionGetItem(aIndex,this.RHSOption);
}
function _OptionSelectionGetItem(aIndex,aOption){
	//return aOption[aIndex].text;
	return aOption[aIndex].value;
}
function _OptionSelectionGetLHSObject(aKey){
	return this.LHSObject[aKey];
}
function _OptionSelectionGetRHSObject(aKey){
	return this.RHSObject[aKey];
}

function _OptionSelectionSetSorted(aSort){
	this.rhsSortAsc = aSort;
}
function _OptionSelectionSetChangeFunction(aFunction){
	this.changeFunction = aFunction;
}
function _OptionSelectionSetSortAscFunction(aFunction){
	this.sortAscFunction = aFunction;
}
function _OptionSelectionSetSortDescFunction(aFunction){
	this.sortDescFunction = aFunction;
}
function _OptionSelectionSetOneRightFunction(aFunction){
	this.oneRightFunction = aFunction;
}
function _OptionSelectionSetOneLeftFunction(aFunction){
	this.oneLeftFunction = aFunction;
}
function _OptionSelectionSetAllRightFunction(aFunction){
	this.allRightFunction = aFunction;
}
function _OptionSelectionSetAllLeftFunction(aFunction){
	this.allLeftFunction = aFunction;
}
function _OptionSelectionSetLHSOption(aOption){
	this.LHSOption = aOption;
}
function _OptionSelectionSetRHSOption(aOption){
	this.RHSOption = aOption;
}
function _OptionSelectionSetOneRight(aButton){
	this.oneRight = aButton;
}
function _OptionSelectionSetOneLeft(aButton){
	this.oneLeft = aButton;
}
function _OptionSelectionSetAllRight(aButton){
	this.allRight = aButton;
}
function _OptionSelectionSetAllLeft(aButton){
	this.allLeft = aButton;
}
function _OptionSelectionSetMoveUp(aButton){
	this.moveUp = aButton;
}
function _OptionSelectionSetMoveDown(aButton){
	this.moveDown = aButton;
}
function _OptionSelectionAddLHSObject(aKey){
	this.LHSObject[aKey] = new Object();
}
function _OptionSelectionAddRHSObject(aKey){
	this.RHSObject[aKey] = new Object();
}

function _OptionSelectionUnselectLHSOption(){
	this._OptionSelectionUnselectOption(this.LHSOption);
}
function _OptionSelectionUnselectRHSOption(){
	this._OptionSelectionUnselectOption(this.RHSOption);
}
function _OptionSelectionUnselectOption(aOption){
	aOption.selectedIndex = -1;
	this.updateStatus();
}

function _OptionSelectionUpOne(){
	var selectedIndex = this.RHSOption.selectedIndex;
	var destIndex = selectedIndex-1;
	var fromIndex = selectedIndex;
	
	// #GAG 4/1/2002 This check will make the destination index be the last index if 
	// the calculated destination is less than 0.
	if (destIndex < 0) {	
		destIndex = this.RHSOption.length -1;
		
		var tmpText = this.RHSOption[0].text;
		var tmpValue = this.RHSOption[0].value;		
		
		for (var i = 1; i < this.RHSOption.length; i++) {
			this.RHSOption[i-1].text = this.RHSOption[i].text;
			this.RHSOption[i-1].value = this.RHSOption[i].value;
		}		
		
		this.RHSOption[this.RHSOption.length -1].text = tmpText;
		this.RHSOption[this.RHSOption.length -1].value = tmpValue;
				
	} else {
	
			//myvar is the temp variable for the destination option.  	
		//	var myvar = new Option(this.RHSOption[destIndex].text, this.RHSOption[destIndex].value); 
			var mytext = this.RHSOption[destIndex].text;
			var myvalue = this.RHSOption[destIndex].value;
			
			//the destination option is set to the from option	
		//	this.RHSOption[destIndex] = new Option(this.RHSOption[selectedIndex].text, this.RHSOption[selectedIndex].value);
			this.RHSOption[destIndex].text = this.RHSOption[selectedIndex].text;
			this.RHSOption[destIndex].value = this.RHSOption[selectedIndex].value;
			
			//then the from option is set to the temp (or destination option)	
		//	this.RHSOption[fromIndex] = myvar;
			this.RHSOption[fromIndex].text = mytext;
			this.RHSOption[fromIndex].value = myvalue;
	}
		
	/*this statement makes the selected box follow one field throughout movement within the
	selection box*/	
		//#GAG 4/1/2002 Use the variable already calculated instead of running another calculation here.
		//this.RHSOption.selectedIndex=selectedIndex-1;
		this.RHSOption.selectedIndex=destIndex;
	
	this.updateStatus();
}
function _OptionSelectionDownOne(){
	var selectedIndex = this.RHSOption.selectedIndex;
	var destIndex = selectedIndex+1;
	var fromIndex = selectedIndex;
	
	// #GAG 4/1/2002 This check will make the destination index be the last index if 
	// the calculated destination is greater then the last index.
	if (destIndex > (this.RHSOption.length - 1)) {
		destIndex = 0;

		var tmpText = this.RHSOption[this.RHSOption.length - 1].text;
		var tmpValue = this.RHSOption[this.RHSOption.length - 1].value;		
		
		for (var i = this.RHSOption.length - 1; i > 0; i--) {
			this.RHSOption[i].text = this.RHSOption[i-1].text;
			this.RHSOption[i].value = this.RHSOption[i-1].value;
		}		
		
		this.RHSOption[0].text = tmpText;
		this.RHSOption[0].value = tmpValue;
		
	} else {		
			
		//myvar is the temp variable for the destination option.  
	//	var myvar = new Option(this.RHSOption[destIndex].text, this.RHSOption[destIndex].value); 
		var mytext = this.RHSOption[destIndex].text;
		var myvalue = this.RHSOption[destIndex].value;
		
		//the destination option is set to the from option
	//	this.RHSOption[destIndex] = new Option(this.RHSOption[selectedIndex].text, this.RHSOption[selectedIndex].value);
		this.RHSOption[destIndex].text = this.RHSOption[selectedIndex].text;
		this.RHSOption[destIndex].value = this.RHSOption[selectedIndex].value;
		
		//then the from option is set to the temp (or destination option)
	//	this.RHSOption[fromIndex] = myvar;
		this.RHSOption[fromIndex].text = mytext;
		this.RHSOption[fromIndex].value = myvalue;		
		
	}
	
	/*this statement makes the selected box follow one field throughout movement within the
	selection box*/
		//#GAG 4/1/2002 Use the variable already calculated instead of running another calculation here.
		//this.RHSOption.selectedIndex=selectedIndex-1;
		this.RHSOption.selectedIndex=destIndex;
	
	this.updateStatus();
}

function _OptionSelectionMoveOneRight(){
	this._OptionSelectionMoveOne(this.LHSOption,this.RHSOption,this.rhsSortAsc);
	if (this.oneRightFunction != null) this.oneRightFunction(this);	
}
function _OptionSelectionMoveOneLeft(){
	this._OptionSelectionMoveOne(this.RHSOption,this.LHSOption,this.lhsSortAsc);
	if (this.oneLeftFunction != null) this.oneLeftFunction(this);	
}
function _OptionSelectionMoveOne(aSourceOption,aTargetOption,aSortOrder){
	var destIndex = aTargetOption.length;
	var sourceIndex = aSourceOption.selectedIndex;
	
	if (sourceIndex == -1) return;
	// Create a copy of the selected source option and place it in the target select box
	aTargetOption[destIndex] = new Option(aSourceOption[sourceIndex].text, aSourceOption[sourceIndex].value);
	
	// Move the source elements up from the selected element to the last element
	/*the for loop spins through the first selection box and 1) determines which option was selected,
	i=sourceIndex; 2) iterates the overall length minus one times; 3) increment i by 1 each 
	iteration*/
	
	for (var i=sourceIndex;i<aSourceOption.length-1;i++) {
//		aSourceOption[i] = new Option(aSourceOption[i+1].text, aSourceOption[i+1].value);
		aSourceOption[i].value = aSourceOption[i+1].value;
		aSourceOption[i].text = aSourceOption[i+1].text;
		}
	
	// Remove the last element on the source
	aSourceOption.length--;
	
	if (aSourceOption.length > 0)
		aSourceOption[sourceIndex==0?0:sourceIndex - 1].selected = true;
	if (aSortOrder!=null) this.sortOption(aTargetOption,aSortOrder);
	this.updateStatus();	
}
function _OptionSelectionMoveAllRight(){
	this._OptionSelectionMoveAll(this.LHSOption,this.RHSOption,this.rhsSortAsc);
	if (this.allRightFunction != null) this.allRightFunction(this);	
}
function _OptionSelectionMoveAllLeft(){
	this._OptionSelectionMoveAll(this.RHSOption,this.LHSOption,this.lhsSortAsc);
	if (this.allLeftFunction != null) this.allLeftFunction(this);	
}
function _OptionSelectionMoveAll(aSourceOption,aTargetOption,aSortOrder){
	var sourceLength = 0; 
	var i,j;	
	sourceLength = aSourceOption.length
	destIndex = aTargetOption.length;
	for (var i=0;i<sourceLength;i++)
		aTargetOption[destIndex+i] = new Option(aSourceOption[i].text, aSourceOption[i].value);
	aSourceOption.length = 0;
	if (aSortOrder != null) this.sortOption(aTargetOption,aSortOrder);
	this.updateStatus();
}
function _OptionSelectionAddOneRight(text, value){
	this._OptionSelectionAddOne(text, value, this.RHSOption, this.rhsSortAsc, this.LHSOption);
}
function _OptionSelectionAddOneLeft(text, value){
	this._OptionSelectionAddOne(text, value, this.LHSOption, this.lhsSortAsc, this.RHSOption);
}
function _OptionSelectionAddOne(text, value, aTargetOption, aSortOrder, aOtherOption){
	var destIndex = aTargetOption.length;
	aTargetOption[destIndex] = new Option(text, value);
	if(aTargetOption.selectedIndex >= 0){
		aTargetOption[aTargetOption.selectedIndex].selected=false;
		aTargetOption.selectedIndex = -1;
	}
	if(aOtherOption.selectedIndex >= 0){
		aOtherOption[aOtherOption.selectedIndex].selected=false;
		aOtherOption.selectedIndex = -1;
	}
	if(aSortOrder != null){
		this.sortOption(aTargetOption,aSortOrder);
		for(var i=0; i<aTargetOption.length; ++i){
			if(value==aTargetOption[i].value){
				destIndex=i;
				break;
			}
		}
	}
	aTargetOption[destIndex].selected=true;
	aTargetOption.selectedIndex = destIndex;
	this.updateStatus();
}

function _OptionSelectionUpdateStatus(){
	this.oneRight.disabled = this.LHSOption.selectedIndex == -1;
	this.oneLeft.disabled = this.RHSOption.selectedIndex == -1;
	if(this.allLeft)	this.allLeft.disabled = this.RHSOption.length < 1;
	if(this.allRight)	this.allRight.disabled = this.LHSOption.length < 1;
	
	//moveUp logic if target option should be sorted
	if (this.rhsSortAsc==null){
		// # GAG 
		//this.moveUp.disabled = this.RHSOption.selectedIndex < 1;
		//this.moveDown.disabled = (this.RHSOption.selectedIndex == -1) || (this.RHSOption.selectedIndex == (this.RHSOption.length -1));
		if(this.moveUp)		this.moveUp.disabled = (this.RHSOption.selectedIndex == -1);
		if(this.moveDown)	this.moveDown.disabled = (this.RHSOption.selectedIndex == -1);
	}		

	//document.all.apply.disabled = options2.length < 1;	
	if (this.changeFunction != null) this.changeFunction(this);
	if(this.LHSOption.selectedIndex > -1){
		if(this.LHSOption.onchange)	this.LHSOption.onchange(this.LHSOption);
	}else if(this.RHSOption.selectedIndex > -1){
		if(this.RHSOption.onchange)	this.RHSOption.onchange(this.RHSOption);
	}else{
		if(this.LHSOption.onchange)	this.LHSOption.onchange(this.LHSOption);
		if(this.RHSOption.onchange)	this.RHSOption.onchange(this.RHSOption);
	}	
}

function sortSelections(){
	this.sortOption(this.LHSOption,this.lhsSortAsc);
	this.sortOption(this.RHSOption,this.rhsSortAsc);
}
function _OptionSelectionSortOption(aSortedOption, aSortAsc){
	var sortArray = new Array();		
	for (var i=0; i<aSortedOption.length; ++i){
		sortArray[i] = new Option (aSortedOption[i].text, aSortedOption[i].value);
	}
	sortArray.sort(aSortAsc ? this.sortAscFunction : this.sortDescFunction);
	for (var i=0; i<sortArray.length; ++i){
		aSortedOption[i].text = sortArray[i].text;
		aSortedOption[i].value = sortArray[i].value;
	}
	sortArray = null;
}
//Overridden in filterValuesScript.jsp
function _OptionSelectionSortAscFunction(aV1,aV2){
	if (aV1.text > aV2.text) return 1;
	if (aV1.text == aV2.text) return 0;
	return -1;
}
function _OptionSelectionSortDescFunction(aV1,aV2){
	return _OptionSelectionSortAscFunction(aV2,aV1);
}

/* LAD - Added these methods here for common location
	The sortDate methods will allow for displaying the date
	in mm/dd/yyyy format while being sorted first by year then month and day
*/
function sortDate(a,b){
	if (a.text == '-empty-') return -1;
	if (b.text == '-empty-') return 1;

	var x = Date.parse(a.text);
	var y = Date.parse(b.text);
	
	if (x > y) return 1;
	if (x < y) return -1;
	return 0;
}
function sortDateDesc(a,b){
	return sortDate(b,a);
}
/* End LAD - 8/24/00 */
