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

import org.apache.ecs.html.Script;
import org.apache.ecs.html.Span;

/**
 * This class will write a JavaScript timer to the screen.
 */
public class Timer extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		try{
			JspWriter out = pageContext.getOut();
	
			String sVal = (String)pageContext.getAttribute("report.Timer", PageContext.APPLICATION_SCOPE);
			
			if ("true".equalsIgnoreCase(sVal)){
				Span timerSpan = new Span();
				timerSpan.setID("elapsedTime");
				
				Script script = new Script();
				script.addElement(getScript());
				
				out.write(timerSpan.toString());
				out.write(script.toString());
			}else{
				out.write("");
			}
		} catch (Exception e) {
			throw new JspException("Error generating JavaScript Timer: " + e.getMessage());
		}
		return(SKIP_BODY);
	}

	private String getScript() {
		
		StringBuffer sb = new StringBuffer();
	
		sb.append("var startDate = new Date();\n");
		sb.append("var startTime = startDate.getTime();\n");
		sb.append("var obj = document.all[\"elapsedTime\"];\n");

		//calculates the seconds elapsed since the page was loaded	
		sb.append("function seconds_elapsed (){\n");
		sb.append("  var date_now = new Date ();\n");
		sb.append("  var time_now = date_now.getTime ();\n");
		sb.append("var time_diff = time_now - startTime;\n");
		sb.append("var seconds_elapsed = Math.floor (time_diff/1000);\n");
		sb.append("return (seconds_elapsed);\n");
		sb.append("}\n");
		
		//take the seconds elapsed and converts them for output
		sb.append("function time_spent(){\n"); 
		sb.append("var secs = seconds_elapsed ();\n"); 
		sb.append("var mins = Math.floor ( secs/60 );\n"); 
		sb.append("secs -= mins * 60;\n");
		sb.append("var hour = Math.floor ( mins/60 );\n"); 
		sb.append("mins -= hour * 60;\n");
		sb.append("obj.innerHTML = \"<DIV>\"+pad(hour) + \":\" + pad(mins) + \":\" + pad(secs)+\"<DIV>\"; \n");
		sb.append("setTimeout( \"time_spent ()\", 1000 );\n");
		sb.append("}\n");
		
		sb.append("function pad(num){\n");
		sb.append("  return ((num>9)?num:\"0\"+num);\n");
		sb.append("};\n");
		
		sb.append("time_spent();\n");
		
		return sb.toString();
	}
}
