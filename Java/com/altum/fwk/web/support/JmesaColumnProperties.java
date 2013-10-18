/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.fwk.web.support;

import org.jmesa.view.component.Column;
import org.jmesa.view.component.Row;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.DateCellEditor;
import org.jmesa.view.editor.NumberCellEditor;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.editor.DroplistFilterEditor;

public class JmesaColumnProperties {
	private String rowColumnProperty;
	private String title;
	private String filterEditor;
	private String cellFormat;
	
	/** Constructor */
	public JmesaColumnProperties(String rowColumnProperty, String title, String filterEditor, String cellFormat){
		this.rowColumnProperty=rowColumnProperty;
		this.title=title;
		this.filterEditor=filterEditor;
		this.cellFormat=cellFormat;
	}
/*
	public String getRowColumnProperty() {
		return rowColumnProperty;
	}
	public String getTitle() {
		return title;
	}
	public String getFilterEditor() {
		return filterEditor;
	}
	public String getCellFormat() {
		return cellFormat;
	}
*/	
	public void setJmesaColumnProperties(Row row){
		Column column;
		try{
			column = row.getColumn(rowColumnProperty);
		}catch (IllegalStateException ise){
			column = row.getColumn(rowColumnProperty.toUpperCase());
		}
		if(title!=null){
			  column.setTitle(title); 
		}	
		if(column instanceof HtmlColumn){
			HtmlColumn htmlColumn = (HtmlColumn)column;
			if("DroplistFilterEditor".equals(filterEditor)){
				htmlColumn.getFilterRenderer().setFilterEditor(new DroplistFilterEditor());
			}	 
		}
		if(cellFormat!=null){
			if(cellFormat.equals("DATE")){
				//Standard Date Format = "MM-dd-yyyy"
				column.getCellRenderer().setCellEditor(new DateCellEditor("MM-dd-yyyy"));
			}else{
				//Number format
				column.getCellRenderer().setCellEditor(new NumberCellEditor(cellFormat));
			}
		}else{
			column.getCellRenderer().setCellEditor(new BasicCellEditor());
		}
	}
}
