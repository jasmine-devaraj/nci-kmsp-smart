/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.jdbc.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.altum.coding.web.support.UtilityController;
import com.altum.jdbc.manager.JdbcManager;

public class SystemController extends UtilityController {
	
	/** Key to a SESSION Param which implies that the result is stored in session */
	public final static String SESSION_LAST_REFRESH_DATE = "SESSION_LAST_REFRESH_DATE";
	public final static String SESSION_LINK_ACCT_DAYS_LEFT = "SESSION_LINK_ACCT_DAYS_LEFT";
	public static final String INCLUDE_SYSTEM_VALUES = "header.includeSytemValues";
	
	/** Typically injected via Spring */
	private JdbcManager jdbcManager;
	
	public ModelAndView cacheSystemParameters(HttpServletRequest request, HttpServletResponse response){
	    	HashMap<String, Object> model = new HashMap<String, Object>();
		if(request.getSession().getAttribute(SESSION_LAST_REFRESH_DATE) == null)
			request.getSession().setAttribute(SESSION_LAST_REFRESH_DATE, jdbcManager.getLastRefreshDate());
		if(request.getSession().getAttribute(SESSION_LINK_ACCT_DAYS_LEFT) == null)
			request.getSession().setAttribute(SESSION_LINK_ACCT_DAYS_LEFT, jdbcManager.getLinkAcctDaysLeft());
		return new ModelAndView(INCLUDE_SYSTEM_VALUES, model);
	}	/** Typically injected via Spring */
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}
}
