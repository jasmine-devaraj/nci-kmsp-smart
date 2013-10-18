/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.components.renderers;

import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import com.altum.beans.OptionSelection;

public class OptionSelectionRenderer {


	/**
	 * --Perhaps this and all private methods below should be in OptionSelection.java - I'm ambivalent...
	 * 
	 * Default generic version called by JSPs (replaces the jsp inclusion, except that
	 * 		pages must now separately include js/gui/optionselection/OptionSelection.js)
	 * @param os
	 * @return
	 */
	public static Span getOptionSelectionElement(OptionSelection os){
		return getOptionSelectionElement(os,"center",8);
	}
	/**
	 * --Perhaps this and all private methods below should be in OptionSelection.java - I'm ambivalent...
	 *
	 * Customizable version, could be called by JSPs.
	 * @param os
	 * @param sAlign
	 * @param iSize
	 * @return
	 */
	public static Span getOptionSelectionElement(OptionSelection os, String sAlign, int iSize){
		Span span = new Span();
		Table table = new Table();
		table.setID("_"+os.getName()+"_table");
		table.setAlign(sAlign);
		table.setClass("optionSelectionTable");
		TR tr = new TR();
		tr.addElement(getLHSElement(os, iSize));
		tr.addElement(getBetweenSelectsElement(os));
		tr.addElement(getRHSElement(os, iSize));
		if (os.isRHSAllowMultiple() && !os.isSorted()){
			tr.addElement(getUpOrDownElement(os));
		}
		table.addElement(tr);
		span.addElement(table);
		span.addElement(getScriptElement(os));
		return span;
	}

	//Changes here should also be made to getRHSElement. Basically the same code but with a ton of L vs R differences.
	private static TD getLHSElement(OptionSelection os, int iSize){
		TD td = new TD();
		td.setAlign("center");
		Div div = new Div();
		div.setClass("txtBodyLabel");
		div.addElement(os.getLHSTitle()+":");
		td.addElement(div);
		Select select = new Select();
		select.setID("_"+os.getName()+"_LHS");
		select.setName("_"+os.getName()+"_LHS");
		select.setSize(iSize);
		select.setOnKeyUp(os.getName()+".updateStatus();");
		select.setOnDblClick(os.getName()+".moveOneRight();");
		if(os.getLHSSelectFunction()!=null){
			select.setOnChange(os.getLHSSelectFunction() + "(this);");
		}
		select.setOnClick(os.getName()+".unselectRHSOption();");
//		if(getFont()!=null){			select.setStyle("FONT-FAMILY: '"+getFont()+"';");		}
		Option option;
		for(int i=0; i < os.getLHSCount(); i++){
			option = new Option();
			option.setValue(os.getLHSItem(i).getValue());
			option.addElement(os.getLHSItem(i).getText());
			select.addElement(option);
		}
		td.addElement(select);
		div = new Div();
		div.setClass("osSortRow");
		Input input = new Input();
		input.setType("checkbox");
		input.setName("lhsSortOrder");
		input.setOnClick(os.getName()+".sortOption("+os.getName()+".getLHSOption(),this.checked);"+os.getName()+".lhsSortAsc=this.checked;");
		input.setChecked(true);
		div.addElement(input);
		div.addElement("Ascending");
		td.addElement(div);
		return td;
	}
	private static TD getBetweenSelectsElement(OptionSelection os){
		TD td = new TD();
		td.setAlign("center");
		td.setVAlign("middle");
		td.setWidth(110);
		if(os.isRHSAllowMultiple()){
			Input input = getInputElement(os,"allRight",">>","moveAllRight");
			input.setDisabled(os.getLHSCount()<=0);
			td.addElement(input);
			td.addElement(new BR());
		}
		td.addElement(getInputElement(os,"oneRight",">","moveOneRight"));
		td.addElement(new BR());
		td.addElement(getInputElement(os,"oneLeft","<","moveOneLeft"));
		if(os.isRHSAllowMultiple()){
			td.addElement(new BR());
			Input input = getInputElement(os,"allLeft","<<","moveAllLeft");
			input.setDisabled(os.getRHSCount()<=0);
			td.addElement(input);
		}
		return td;
	}
	//Changes here should probably also be made to getLHSElement. Basically the same code but with a ton of L vs R differences.
	private static TD getRHSElement(OptionSelection os, int iSize){
		TD td = new TD();
		td.setAlign("center");
		Div div = new Div();
		div.setClass("txtBodyLabel");
		div.addElement(os.getRHSTitle()+":");
		td.addElement(div);
		Select select = new Select();
		select.setID("_"+os.getName()+"_RHS");
		select.setName("_"+os.getName()+"_RHS");
		select.setSize(iSize);
		select.setOnKeyUp(os.getName()+".updateStatus();");
		select.setOnDblClick(os.getName()+".moveOneLeft();");
		if(os.getRHSSelectFunction()!=null){
			select.setOnChange(os.getRHSSelectFunction() + "(this);");
		}
		select.setOnClick(os.getName()+".unselectLHSOption();");
//		if(getFont()!=null){			select.setStyle("FONT-FAMILY: '"+getFont()+"';");		}
		Option option;
		for(int i=0; i < os.getRHSCount(); i++){
			option = new Option();
			option.setValue(os.getRHSItem(i).getValue());
			option.addElement(os.getRHSItem(i).getText());
			select.addElement(option);
		}
		td.addElement(select);
		div = new Div();
		div.setClass("osSortRow");
		if(os.isRHSAllowMultiple() && os.isSorted()){
			Input input = new Input();
			input.setType("checkbox");
			input.setName("rhsSortOrder");
			input.setOnClick(os.getName()+".sortOption("+os.getName()+".getRHSOption(),this.checked);"+os.getName()+".rhsSortAsc=this.checked;");
			input.setChecked(true);
			div.addElement(input);
			div.addElement("Ascending");
		}else{
			div.addElement("&nbsp;");
		}
		td.addElement(div);
		return td;
	}
	private static TD getUpOrDownElement(OptionSelection os){
		TD td = new TD();
		td.setAlign("center");
		td.setClass("osMoveUpAndDownCell");
		td.addElement(getInputElement(os,"moveUp","up","upOne"));
		td.addElement(new BR());
		td.addElement("&nbsp;");
		td.addElement(new BR());
		td.addElement(getInputElement(os,"moveDown","dn","downOne"));
		return td;
	}
	private static Input getInputElement(OptionSelection os, String sNameAppend, String sValue, String sOnClickFunctionName){
		Input input = new Input();
		input.setType("button");
		input.setName("_"+os.getName()+"_"+sNameAppend);
		input.setValue(sValue);
		input.setStyle("HEIGHT: 28px; WIDTH: 34px");
		input.setOnClick(os.getName()+"."+sOnClickFunctionName+"();");
		input.setDisabled(true);
		return input;
	}
	private static Script getScriptElement(OptionSelection os){
		Script script = new Script();
		script.addElement("var "+os.getName()+" = new OptionSelection(document.all._"+os.getName()+"_LHS,document.all._"+os.getName()+"_RHS);");
												script.addElement(os.getName()+".setOneRight(document.all._"+os.getName()+"_oneRight);");
												script.addElement(os.getName()+".setOneLeft(document.all._"+os.getName()+"_oneLeft);");
												script.addElement(os.getName()+".lhsSortAsc=true;");
		if(os.getChangeFunction()!=null)		script.addElement(os.getName()+".setChangeFunction("+os.getChangeFunction()+");");
		if(os.getOneRightFunction()!=null)		script.addElement(os.getName()+".setOneRightFunction("+os.getOneRightFunction()+");");
		if(os.getOneLeftFunction()!=null)		script.addElement(os.getName()+".setOneLeftFunction("+os.getOneLeftFunction()+");");
		if(os.getSortAscFunction()!=null)		script.addElement(os.getName()+".setSortAscFunction("+os.getSortAscFunction()+");");
		if(os.getSortDescFunction()!=null)		script.addElement(os.getName()+".setSortDescFunction("+os.getSortDescFunction()+");");
		if(os.isRHSAllowMultiple()){
												script.addElement(os.getName()+".setAllRight(document.all._"+os.getName()+"_allRight);");
												script.addElement(os.getName()+".setAllLeft(document.all._"+os.getName()+"_allLeft);");
			if(os.getAllRightFunction()!=null)	script.addElement(os.getName()+".setAllRightFunction("+os.getAllRightFunction()+");");
			if(os.getAllLeftFunction()!=null)	script.addElement(os.getName()+".setAllLeftFunction("+os.getAllLeftFunction()+");");
			if(os.isSorted()){
												script.addElement(os.getName()+".rhsSortAsc=true;");
												script.addElement(os.getName()+".sortSelections();");
			}else{
												script.addElement(os.getName()+".rhsSortAsc=true;");
												script.addElement(os.getName()+".setMoveUp(document.all._"+os.getName()+"_moveUp);");
												script.addElement(os.getName()+".setMoveDown(document.all._"+os.getName()+"_moveDown);");
			}
		}
		return script;
	}
	
}
