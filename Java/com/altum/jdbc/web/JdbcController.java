/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.web;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.altum.coding.data.CommonTokens;
import com.altum.coding.enums.SqlCommandKeys;
import com.altum.coding.web.support.UtilityController;
import com.altum.jdbc.dao.support.ResultSetData;
import com.altum.jdbc.manager.JdbcManager;

public class JdbcController extends UtilityController {
	
	/** Key to a SESSION Param which implies that the result is stored in session */
	public final static String SESSION_PARAM_MAPS_TOKEN = "com.altum.jdbc.web.JdbcController.paramMaps";
	public final static String SESSION_RESULTSETDATA_LIST_TOKEN = "com.altum.jdbc.web.JdbcController.resultSetDataList";
	public final static String SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN = "com.altum.jdbc.web.JdbcController.exportResultSetDataList";
	/** Typically injected via Spring */
	private JdbcManager jdbcManager;
	
	/**
	 * This method runs (any) sql retrieved from the request and returns jmesa tables constructed using SESSION_PARAM_MAPS_TOKEN within a json object
	 * It is intended to drop tables (if they exist) and then create new tables, and optionally insert rows and/or update values.
	 * Beware, it will currently run any sql in the arguments in the order below regardless of what type of sql it actually is.
	 */
	public ModelAndView createAndFillTables(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EnumMap<SqlCommandKeys,Object> sqlCommandMap = getSqlCommandMap(request);
		jdbcManager.dropCreateAndFillTables(getSqlCommands(request, SqlCommandKeys.DROP, sqlCommandMap, true)
											,getSqlCommands(request, SqlCommandKeys.CREATE, sqlCommandMap, true)
											,getSqlCommands(request, SqlCommandKeys.INSERT, sqlCommandMap, false)
											,getSqlCommands(request, SqlCommandKeys.UPDATE, sqlCommandMap, false));
		return getResultSetData(request, response);
	}
	
	/**
	 * This method runs (any) sql retrieved from the request and returns jmesa tables constructed using SESSION_PARAM_MAPS_TOKEN within a json object
	 * It is intended to truncate tables, and then optionally inserts rows and/or update values.
	 * Beware, it will currently run any sql in the arguments in the order below regardless of what type of sql it actually is.
	 */
	public ModelAndView truncateAndFillTables(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EnumMap<SqlCommandKeys,Object> sqlCommandMap = getSqlCommandMap(request);
		jdbcManager.truncateAndFillTables(getSqlCommands(request, SqlCommandKeys.TRUNCATE, sqlCommandMap, true)
										 ,getSqlCommands(request, SqlCommandKeys.INSERT, sqlCommandMap, false)
										 ,getSqlCommands(request, SqlCommandKeys.UPDATE, sqlCommandMap, false));
		return getResultSetData(request, response);	
	}

	/**
	 * This method returns jmesa tables constructed using SESSION_PARAM_MAPS_TOKEN within a json object.
	 * The SESSION_RESULTSETDATA_LIST_TOKEN should be cleared prior to calling if the SqlCommandKeys.SELECT needs to be rerun and the resultsetdata list recreated.
	 */
	public ModelAndView getResultSetData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		List<Map<String,Object>> paramMaps = (List)session.getAttribute(SESSION_PARAM_MAPS_TOKEN);
		List<ResultSetData> results = (List)session.getAttribute(SESSION_RESULTSETDATA_LIST_TOKEN);
		ResultSetData rsd;
		String html;
		JSONObject jso;
		List<JSONObject> jsonObjs = new ArrayList<JSONObject>();
		if(results==null){
			results = new ArrayList<ResultSetData>();
			Map<String,Object> params;
			EnumMap<SqlCommandKeys,Object> sqlCommandMap;
			String select;
			for(int i=0; i<paramMaps.size(); ++i){
				params = paramMaps.get(i);
				sqlCommandMap = (EnumMap)params.get(CommonTokens.SQL_COMMAND_MAP_TOKEN);
				select = (String)sqlCommandMap.get(SqlCommandKeys.SELECT);
				rsd = jdbcManager.getResultSetData(select);
				results.add(rsd);
				// getJmesaTable renders the jmesa table
				html = getJmesaTable(request, response, (EnumMap)params.get(CommonTokens.JMESA_MAP_TOKEN), rsd.getColumnNamesArray(), rsd.getRows());
				jso = new JSONObject();
				jso.put(CommonTokens.DATA_TOKEN,html);
				jsonObjs.add(jso);
			}
			session.setAttribute(SESSION_RESULTSETDATA_LIST_TOKEN, results);
			session.setAttribute(SESSION_EXPORT_RESULTSETDATA_LIST_TOKEN, results);
		}else{
			for(int i=0; i<paramMaps.size(); ++i){
				// getJmesaTable renders the jmesa table
				rsd = results.get(i);
				html = getJmesaTable(request, response, (EnumMap)paramMaps.get(i).get(CommonTokens.JMESA_MAP_TOKEN), rsd.getColumnNamesArray(), rsd.getRows());
				jso = new JSONObject();
				jso.put(CommonTokens.DATA_TOKEN,html);
				jsonObjs.add(jso);
			}
		}
		jso = new JSONObject();
		jso.put(CommonTokens.TABLES_TOKEN, jsonObjs);
		response.getOutputStream().write(jso.toString().getBytes());
		response.getOutputStream().flush();
		return null;
	}
	
	private EnumMap<SqlCommandKeys,Object> getSqlCommandMap(HttpServletRequest request){
		EnumMap<SqlCommandKeys,Object> sqlCommandMap = (EnumMap)request.getAttribute(CommonTokens.SQL_COMMAND_MAP_TOKEN);
		if(sqlCommandMap==null){
			List<Map<String,Object>> paramMaps = (List)request.getSession().getAttribute(SESSION_PARAM_MAPS_TOKEN);
			if(paramMaps!=null){
				sqlCommandMap = (EnumMap)paramMaps.get(0).get(CommonTokens.SQL_COMMAND_MAP_TOKEN);
			}
		}
		return sqlCommandMap;
	}
	private String[] getSqlCommands(HttpServletRequest request, SqlCommandKeys key, EnumMap<SqlCommandKeys,Object> sqlCommandMap, boolean required) throws ServletRequestBindingException {
		try{
			return RequestUtils.getRequiredStringParameters(request, key.name());
		}catch (ServletRequestBindingException e){
			if(sqlCommandMap!=null){
				Object o = sqlCommandMap.get(key);
				if(o instanceof String[]){
					return (String[])o;
				}else if(o instanceof Object[]){
					Object[] a = (Object[])o;
					String[] ret = new String[a.length];
					for(int i=0; i<a.length; ++i){
						ret[i]=String.valueOf(a[i]);
					}
					return ret;
				}else if(o != null){
					return new String[]{o.toString()};
				}//else o==null, drop to choice below
			}
			if(!required){
				return new String[]{};
			}else{
				throw e;
			}
		}
	}
	/** Typically injected via Spring */
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}
}
