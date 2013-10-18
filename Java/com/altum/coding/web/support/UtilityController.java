/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.coding.web.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.limit.ExportType;
import org.jmesa.limit.Limit;
import org.jmesa.view.component.Row;
import org.jmesa.view.component.Table;
import org.jmesa.view.html.component.HtmlTable;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.altum.coding.enums.JmesaKeys;
import com.altum.fwk.web.support.JmesaColumnProperties;
import com.altum.jdbc.dao.support.ResultSetData;
import com.altum.jdbc.web.JdbcController;

/**
 * This class should handle requests that are related to the application
 * but not directly tied to its business process.  Also, helper methods that 
 * could be used in cross cutting scenarios could go here as well.
 */
public class UtilityController extends MultiActionController {

	/** Value matches a view in views.properties */
	public static final String JSON_VIEW = "jsonView";
	/** Value matches a view in views.properties */
	public static final String JSON_RESPONSE = "jsonResponse";
	/** Value matches a view in views.properties */
	public static final String STANDARD_SUCCESS_VIEW = "standardSuccess";

	//showStandardSuccessView map params:
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL. */
	public final static String SSV_AUTO_FORWARD_URL = "autoForwardUrl";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL, where the default value is 3000. */
	public final static String SSV_AUTO_FORWARD_MILLIS = "autoForwardMillis";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL, where the default value is "main". */
	public final static String SSV_AUTO_FORWARD_PAGE_DESCRIPTION = "autoForwardPageDescription";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL. */
	public final static String SSV_PAGE_TITLE = "pageTitle";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL. */
	public final static String SSV_PAGE_TITLE_DESCRIPTION = "pageTitleDescription";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL, where the default is "Your change was successful." */
	public final static String SSV_SUCCESS_MESSAGE = "successMessage";
	/** Used as a parameter key. Accessed via matching hardcoded string in JSTL. */
	public final static String SSV_PROCESS_COMPLETION_INFO = "processCompletionInfo";

	/**
	 * Map params:<ul>
	 * <li>SSV_AUTO_FORWARD_URL				- If not empty, page displays "You will momentarily be forwarded to the main page..."
	 * <li>SSV_AUTO_FORWARD_MILLIS			- Default is 3000. Only applies if SSV_AUTO_FORWARD_URL is not empty.
	 * <li>SSV_AUTO_FORWARD_PAGE_DESCRIPTION- Default is "main".
	 * <li>SSV_PAGE_TITLE
	 * <li>SSV_PAGE_TITLE_DESCRIPTION
	 * <li>SSV_SUCCESS_MESSAGE				- Default is "Your change was successful."
	 * <li>SSV_PROCESS_COMPLETION_INFO
	 */

	/**
	 * This method renders the jmesa table and decorates the columns.
	 */
	public static String getJmesaTable(HttpServletRequest request, HttpServletResponse response, EnumMap<JmesaKeys,Object> params, String[] columnNames, Collection<?> items){
		String tableId = (String)params.get(JmesaKeys.TABLE_ID);
		List<JmesaColumnProperties> columnProperties = (List)params.get(JmesaKeys.COLUMN_PROPERTIES);
		String caption = (String)params.get(JmesaKeys.CAPTION);
		List<ResultSetData> results = new ArrayList<ResultSetData>();
		TableFacade tableFacade = new TableFacadeImpl(tableId, request);
		tableFacade.setColumnProperties(columnNames);
		tableFacade.setItems(items);
		tableFacade.setExportTypes(response, (ExportType[])params.get(JmesaKeys.EXPORT_TYPES));
		Limit limit = tableFacade.getLimit();
		Table table = tableFacade.getTable();
		Row row = table.getRow();
		if (limit.isExported()) { 
			if(columnProperties != null){
				for(JmesaColumnProperties jcp : columnProperties){
					jcp.setJmesaColumnProperties(row);
				}
			}
			if(params.get(JmesaKeys.FILE_NAME) != null){
				table.setCaption((String)params.get(JmesaKeys.FILE_NAME));
			}
			//Will write the export data out to the response:
			tableFacade.render(); 
			return null;
		}else{
			if(columnProperties != null){
				for(JmesaColumnProperties jcp : columnProperties){
					jcp.setJmesaColumnProperties(row);
				}
			}
			List<String> columnNamesList = new ArrayList<String>();
			for(int i=0;i<columnNames.length;i++){
				columnNamesList.add(columnNames[i]);
			}
			results.add(new ResultSetData(columnNamesList, (List<Map<String, Object>>)tableFacade.getCoreContext().getFilteredItems()));
			request.getSession().setAttribute(JdbcController.SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN, results);
			String wholeTable = tableFacade.render();
			String toolbar = wholeTable.substring(wholeTable.indexOf("<tr class=\"toolbar\" >"), wholeTable.indexOf("<tr class=\"filter\" >"));
			String outputTable = "<table id=\"toolbar\" class=\"toolbar\" style=\"width:100%;\" ><caption>" + caption + "</caption>" + toolbar + "</table>" + wholeTable.substring(0, wholeTable.indexOf("<tr class=\"toolbar\" >"));
			outputTable		  += wholeTable.substring(wholeTable.indexOf("<tr class=\"filter\" >"));
			
			System.out.println(outputTable);
			
			return outputTable;
		}
	}
	
}
