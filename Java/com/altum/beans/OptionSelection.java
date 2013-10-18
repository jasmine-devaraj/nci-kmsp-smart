/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.beans;

import java.util.ArrayList;
import java.util.List;

import com.altum.fwk.util.UtilityMethods;
/**
 * This object defines a gui component which is composed of two option boxes,
 * and buttons which allow the user to move items from one box to another.
 * The two boxes are referred to as LHS (Left Hand Side) and RHS (Right Hand Side).
 */
public class OptionSelection {
    public final static String BEAN_TOKEN = "BEAN_OPTION_SELECTION";
	
	//DataType Constants
	public final static String DATE = "Date";
	public final static String STRING = "Text";
	public final static String NUMBER = "Number";
	
    //Attributes
    private String msName;
    private String msDisplayName;
    private String msLHSTitle;
    private String msRHSTitle;
    private String msPath;
	private String msDataType;
    private boolean mbSorted;
    private boolean mbRHSAllowMultiple;
    private ArrayList malLHS;
    private ArrayList malRHS;

    //Custom (optional) functions;
    private String msChangeFunction;
    private String msOneRightFunction;
    private String msOneLeftFunction;
    private String msAllRightFunction;
    private String msAllLeftFunction;
    private String msSortAscFunction;
    private String msSortDescFunction;
    private String msLHSSelectFunction;
    private String msRHSSelectFunction;

    /**
     * Create a new OptionSelection bean with the <i>Name</i> parameter.
     * @param   String  Name for this bean
     */
    public OptionSelection(String sName) {
        msName = sName;
        malLHS = new ArrayList();
        malRHS = new ArrayList();
        mbSorted = false;
        mbRHSAllowMultiple = true;
    }

	public void fillOptionSelection(String sName,String sSelectFunction, String sChangeFunction){
		//Use the OptionSelection bean object with the sortable and sorted fields listings
		setName(sName);
		setLHSTitle("Available");
		setRHSTitle("Selected");
		setRHSSelectFunction(sSelectFunction);
		setLHSSelectFunction(sSelectFunction);
		setChangeFunction(sChangeFunction);
	}
	public StringBuffer buildDescriptionArray(String sArrayName){
		StringBuffer sbReturn = new StringBuffer();
		for (int i=0;i<getLHSCount();i++){
			appendOptionSelectionItemDesc(getLHSItem(i), sbReturn, sArrayName);
		}
		for (int i=0;i<getRHSCount();i++){
			appendOptionSelectionItemDesc(getRHSItem(i), sbReturn, sArrayName);
		}
		return sbReturn;
	}
	private void appendOptionSelectionItemDesc(OptionSelectionItem osi, StringBuffer sbReturn, String sArrayName){
		if ((osi.getDescription() != null)&&(!osi.getDescription().equals(""))){
			sbReturn.append(sArrayName).append("['").append(osi.getText()).append("'] = '");
			sbReturn.append(UtilityMethods.escapeString(osi.getDescription())).append("';\n");
		}
	}

	
	
	public void setName (String sVal){
    	msName = sVal;
  	}
  	public String getName (){
    	return msName;
  	}
  	public void setDisplayName (String sVal){
    	msDisplayName = sVal;
  	}
  	public String getDisplayName (){
    	return msDisplayName;
  	}
  	public void setPath (String sVal){
    	msPath = sVal;
  	}
  	public String getPath (){
    	return msPath;
  	}
    public void setDataType (String sVal){
    	msDataType = sVal;
    }
    public String getDataType (){
    	return msDataType;
    }
    /**
     * This will determine if the items in the select boxes will be sorted.
     */
  	public void setSorted (boolean bVal){
    	mbSorted = bVal;
  	}
  	public boolean isSorted (){
    	return mbSorted;
  	}
  	public void setRHSAllowMultiple (boolean bVal){
  		mbRHSAllowMultiple = bVal;
  	}
  	public boolean isRHSAllowMultiple (){
    	return mbRHSAllowMultiple;
  	}
  	public void setRHSTitle  (String sVal){
    	msRHSTitle  = sVal;
  	}
  	public String getRHSTitle  (){
    	return msRHSTitle ;
  	}
  	public void setLHSTitle(String sVal){
    	msLHSTitle = sVal;
  	}
  	public String getLHSTitle (){
    	return msLHSTitle;
  	}
  	public void setAllLeftFunction (String sVal){
    	msAllLeftFunction = sVal;
  	}
  	public String getAllLeftFunction (){
    	return msAllLeftFunction;
  	}
  	public void setAllRightFunction (String sVal){
    	msAllRightFunction = sVal;
  	}
  	public String getAllRightFunction (){
    	return msAllRightFunction;
  	}
    /**
     * This function will be called when a single item is moved to the LHS.
     */
  	public void setOneLeftFunction (String sVal){
    	msOneLeftFunction = sVal;
  	}
  	public String getOneLeftFunction (){
    	return msOneLeftFunction;
  	}
    /**
     * This function will be called when a single item is moved to the RHS.
     */
  	public void setOneRightFunction (String sVal){
    	msOneRightFunction = sVal;
  	}
  	public String getOneRightFunction (){
    	return msOneRightFunction;
  	}
  	public void setChangeFunction (String sVal){
    	msChangeFunction = sVal;
  	}
  	public String getChangeFunction (){
    	return msChangeFunction;
  	}
    /**
     * This function will be called when an item on the left hand side is selected.
     */
  	public void setLHSSelectFunction (String sVal){
    	msLHSSelectFunction = sVal;
  	}
  	public String getLHSSelectFunction (){
    	return msLHSSelectFunction;
  	}
    /**
     * This function will be called when an item on the right hand side is selected.
     */
  	public void setRHSSelectFunction (String sVal){
    	msRHSSelectFunction = sVal;
  	}
  	public String getRHSSelectFunction (){
    	return msRHSSelectFunction;
  	}
    /**
     * This method will be called <u>INSTEAD</u> of the standard sort function associated with javascript arrays.
     */
  	public void setSortAscFunction (String sVal){
    	msSortAscFunction = sVal;
  	}
  	public String getSortAscFunction (){
    	return msSortAscFunction;
  	}
    /**
     * This method will be called <u>INSTEAD</u> of the standard sort function associated with javascript arrays.
     */
  	public void setSortDescFunction (String sVal){
    	msSortDescFunction = sVal;
  	}
  	public String getSortDescFunction (){
    	return msSortDescFunction;
  	}
    /**
     * Returns the number of items in the left hand side
     */
  	public int getLHSCount(){
    	return this.malLHS.size();
  	}
    /**
     * Sets the capacity for items in the left hand side
     */
    public void setLHSCapacity(int iCapacity){
    	this.malLHS.ensureCapacity(iCapacity);
    }
    /**
     * Returns the number of items in the right hand side
     */
  	public int getRHSCount(){
    	return this.malRHS.size();
  	}
    /**
     * Sets the capacity for items in the right hand side
     */
    public void setRHSCapacity(int iCapacity){
    	this.malRHS.ensureCapacity(iCapacity);
    }
    /**
     * Returns the List of left hand side item
     */
    public List getLHSItems(){
    	return this.malLHS;
    }

    /**
     * Returns the List of right hand side item
     */
    public List getRHSItems(){
    	return this.malRHS;
    }

    /**
     * Returns the item in the left hand side at the specified position
     *
     * @param   int The index at which the desired item exists.
     * @return  OptionSelectionItem  A value at the specified location
     */
  	public OptionSelectionItem getLHSItem(int iIndex){
    	return this.getItem(this.malLHS, iIndex);
  	}

    /**
     * Returns the item in the right hand side at the specified position
     *
     * @param   int The index at which the desired item exists.
     * @return  OptionSelectionItem  A value at the specified location
     */
  	public OptionSelectionItem getRHSItem(int iIndex){
    	return this.getItem(this.malRHS, iIndex);
  	}

    /**
     * Returns the item in the specified array at the specified position.
     *
     * @param   ArrayList   The collection of items
     * @param   int The index at which the desired item exists.
     * @return  OptionSelectionItem  A value at the specified location, or a new OptionSelectionItem
     * if the index is out of bounds or -1
     */
  	private OptionSelectionItem getItem(ArrayList alCol, int iIndex){
        if ((iIndex == -1)||(iIndex >= alCol.size()))
            return new OptionSelectionItem("","","");
    	return (OptionSelectionItem)alCol.get(iIndex);
  	}

    /**
     * Add a OptionSelectionItem to the left hand side at the specified position.
     *
     * @param   OptionSelectionItem  An object to be added.
     * @param   int The index at which the object should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addLHSItem(OptionSelectionItem osi, int iIndex){
        this.addItem(this.malLHS,osi,iIndex);
  	}

    /**
     * Add an item to the left hand side at the end..
     *
     * @param   String  A text value to be added.
     * be added to the end.
     * @return  none
     */
  	public void addLHSItem(String sItem){
        this.addItem(this.malLHS,new OptionSelectionItem(sItem,"",sItem),this.malLHS.size());
  	}
  	/**
     * Add an item to the left hand side at the end..
     *
     * @param   String  A text value to be added.
     * @param   String  A description for the text value.
     * @param   String  A value (if different from text).
     * @return  none
     */
  	public void addLHSItem(String sItem, String sDesc, String sValue){
        this.addItem(this.malLHS,new OptionSelectionItem(sItem,sDesc,sValue),this.malLHS.size());
  	}
    /**
     * Add an item to the left hand side at the specified position.
     *
     * @param   String  A text value to be added.
     * @param   int The index at which the string should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addLHSItem(String sItem, int iIndex){
        this.addItem(this.malLHS,new OptionSelectionItem(sItem,"",sItem),iIndex);
  	}

    /**
     * Add an item to the left hand side at the specified position
     *
     * @param   String  A text value to be added.
     * @param   String  A description for the text value
     * @param   int The index at which the string should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addLHSItem(String sItem, String sDesc, int iIndex){
        this.addItem(this.malLHS,new OptionSelectionItem(sItem,sDesc,sItem),iIndex);
  	}

    /**
     * Add a OptionSelectionItem to the right hand side at the specified position.
     *
     * @param   OptionSelectionItem  An object to be added.
     * @param   int The index at which the object should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addRHSItem(OptionSelectionItem osi, int iIndex){
        this.addItem(this.malRHS,osi,iIndex);
  	}

    /**
     * Add an item to the right hand side at the end..
     *
     * @param   String  A text value to be added.
     * be added to the end.
     * @return  none
     */
  	public void addRHSItem(String sItem){
        this.addItem(this.malRHS,new OptionSelectionItem(sItem,"",sItem),this.malRHS.size());
  	}
  	/**
     * Add an item to the right hand side at the end..
     *
     * @param   String  A text value to be added.
     * @param   String  A description for the text value.
     * @param   String  A value (if different from text).
     * @return  none
     */
  	public void addRHSItem(String sItem, String sDesc, String sValue){
        this.addItem(this.malRHS,new OptionSelectionItem(sItem,sDesc,sValue),this.malRHS.size());
  	}
    /**
     * Add an item to the right hand side at the specified position
     *
     * @param   String  A text value to be added.
     * @param   int The index at which the string should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addRHSItem(String sItem,int iIndex){
        this.addItem(this.malRHS,new OptionSelectionItem(sItem,"",sItem),iIndex);
  	}

    /**
     * Add an item to the right hand side at the specified position
     *
     * @param   String  A text value to be added.
     * @param   String  A description for the text value
     * @param   int The index at which the string should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	public void addRHSItem(String sItem, String sDesc,int iIndex){
        this.addItem(this.malRHS,new OptionSelectionItem(sItem,sDesc,sItem),iIndex);
  	}

    /**
     * Add an item to the specified ArrayList at the specified position
     *
     * @param   ArrayList   The collection of items.
     * @param   OptionSelectionItem  An item to be added to the ArrayList.
     * @param   int The index at which the string should be added, or -1 if the item should
     * be added to the end.
     * @return  none
     */
  	private void addItem(ArrayList malCol, OptionSelectionItem osi, int iIndex){
        if ((iIndex == -1)||(iIndex >= malCol.size())) {
			iIndex = malCol.size();
        }
   		malCol.add(iIndex,osi);
  	}

    /**
     * Remove an item from the left hand side at the specified position.
     * The removed item is returned by this method
     *
     * @param   int The index at which the value should be removed
     * @return  OptionSelectionItem The item that was removed.
     */
  	public OptionSelectionItem removeLHSItem(int iIndex){
        return (OptionSelectionItem)this.malLHS.remove(iIndex);
  	}

    /**
     * Remove an item from the right hand side at the specified position.
     * The removed item is returned by this method
     *
     * @param   int The index at which the value should be removed
     * @return  OptionSelectionItem The item that was removed.
     */
  	public OptionSelectionItem removeRHSItem(int iIndex){
        return (OptionSelectionItem)this.malRHS.remove(iIndex);
  	}

    /**
     * Class describing an item which can be added to an OptionsSelection side.
     *
     */
    public class OptionSelectionItem extends Object{
        private String msText;
        private String msDescription;
        private String msValue;
        /**
         * Returns a new OptionSelectionItem with the text and description values
         *
         * @param   String  The text as it will appear in a display
         * @param   String  The text description of the item
         * @return  none
         */
        public OptionSelectionItem(String sText, String sDescription, String sValue){
            this.msText = sText;
            this.msDescription = sDescription;
            this.msValue = sValue;
        }

        /**
         * Returns the text of this object
         *
         * @return  String  OptionSelectionItem text value
         */
      	public String getText(){
        	return this.msText;
      	}

        /**
         * Returns the description of this object
         *
         * @return  String  OptionSelectionItem description
         */
      	public String getDescription(){
        	return this.msDescription;
      	}
      	
      	/**
         * Returns the value of this object
         *
         * @return  String  OptionSelectionItem description
         */
      	public String getValue(){
        	return this.msValue;
      	}

    }
}
