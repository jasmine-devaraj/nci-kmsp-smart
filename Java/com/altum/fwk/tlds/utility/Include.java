/*
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.fwk.tlds.utility;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class will include a resource identified by a property stored in application
 * context, or in the context specified in the tag.
 */
public class Include extends TagSupport {
 
	private String msResource;
	private String msContext;

	/**
	 * Set name property
	 *
	 * @param String
	 * @return void
	 */
	public void setResource(String sVal) {
		msResource = sVal;
	}
	
	/**
	 * Set name property
	 *
	 * @param String
	 * @return void
	 */
	public void setContext(String sVal) {
		msContext = sVal;
	}

	@Override
	public int doStartTag() throws JspException {
		try
		{	
			JspWriter out = pageContext.getOut();   
			
			// Default context is application
			int iContextScope = PageContext.APPLICATION_SCOPE;			
			if ("SESSION".equalsIgnoreCase(msContext)) {
				iContextScope = PageContext.SESSION_SCOPE;
			} else if ("PAGE".equalsIgnoreCase(msContext)) {
				iContextScope = PageContext.PAGE_SCOPE;
			} else if ("REQUEST".equalsIgnoreCase(msContext)) {
				iContextScope = PageContext.REQUEST_SCOPE;
			} else if ("APPLICATION".equalsIgnoreCase(msContext)) {
				iContextScope = PageContext.APPLICATION_SCOPE;
			}
			 
			String sVal =  (String)pageContext.getAttribute(msResource, iContextScope);
			if ( sVal != null && !sVal.trim().equals("") ) {
				out.print(sVal);
			}
			 
		} catch (Exception e) {
			throw new JspException("Error generating Select Tag: " + e.getMessage());
		}
		return(SKIP_BODY);
	}
}


