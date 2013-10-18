/**
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */
package com.altum.fwk.tlds.utility;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.ecs.html.A;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

public class PrintQryParams extends TagSupport{
	private HashMap mhUserParams;

    /**
     * Set name property
     *
     * @param HashMap
     * @return void
     */
    public void setName(HashMap hMap) {
    	mhUserParams = hMap;
    }
	@Override
	public int doStartTag() throws JspException {
		try{
			JspWriter out = pageContext.getOut();

			Div userParamsDiv = new Div();
			userParamsDiv.setID("container");
			userParamsDiv.setClass("parms");

			Div showParams = new Div();
			showParams.setID("parmsShow");
			showParams.setClass("parmLabel");
			showParams.addElement(new A("javascript:toggleView(1)","Show Parameters"));
			userParamsDiv.addElement(showParams);

			Div hideParams = new Div();
			hideParams.setID("parmsHide");
			hideParams.setClass("parmLabel");
			hideParams.setStyle("display:none");
			hideParams.addElement(new A("javascript:toggleView(0)","Hide Parameters"));
			userParamsDiv.addElement(hideParams);

			Div params = new Div();
			params.setID("parms");
			params.setStyle("display:none");
			params.addElement(new Div("&nbsp;"));

			Table mainTbl = new Table();
			mainTbl.setCellSpacing(0);
			mainTbl.setCellPadding(0);
			mainTbl.setClass("tblMain");

//			Create Table Header Row
			mainTbl.addElement(
				new TR().setAlign("center")
				.addElement(
					new TD("User Selected Parameters").setColSpan(2)
					.setClass("tblHeaderCell")));

//			Create Detail Rows
			java.util.Iterator itParams = mhUserParams.keySet().iterator();
			while (itParams.hasNext()) {
				String key = (String) itParams.next();
				String val = (String) mhUserParams.get(key);
				mainTbl.addElement(new TR()
										.addElement(new TD(key).setAlign("right").setClass("tblHeaderCell"))
										.addElement(new TD(val).setClass("tblCell"))
										.setClass("tableRow")
								  );
			}


			params.addElement(mainTbl);
			userParamsDiv.addElement(params);

			out.println(userParamsDiv.toString());
		} catch (Exception e) {
			throw new JspException("Error generating PrintQryParams: " + e.getMessage());
		}
		return(SKIP_BODY);
	}
}
