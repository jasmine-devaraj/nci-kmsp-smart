<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--
/**
 * Developed by Altum, Inc.
 * 11718 Bowman Green Drive, Reston, VA 20190 USA
 * Copyright (c) 2000-2006 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@page import="java.util.*, java.io.*" %>
<%! boolean mbIncludeStyle = true; %>
<% StringBuffer sbHREF = new StringBuffer();
	sbHREF.append(request.getScheme());
	sbHREF.append("://");
//
// Need to use the server name from the client, not the server.
//
// 	sbHREF.append(request.getServerName ());
	sbHREF.append(request.getHeader("Host"));

String sServletBaseHREF = sbHREF.toString();
if (application.getAttribute("main.ServerBaseURL") != null){
	sServletBaseHREF += application.getAttribute("main.ServerBaseURL").toString();
}else{
	sServletBaseHREF += request.getContextPath() + "/";
	application.setAttribute("main.ServerBaseURL", request.getContextPath() + "/");
}

int miAppletPort = application.getAttribute("main.AppletPort") == null?80:Integer.valueOf(application.getAttribute("main.AppletPort").toString()).intValue();
%>
<script language="javascript">document.ServletBaseHREF = "<%= sServletBaseHREF %>";</script>
<base href="<%= sServletBaseHREF %>">
<% if (mbIncludeStyle) { %>
	<link rel="stylesheet" href="<c:url value="styles/main.css" />" type="text/css" />
	<c:if test="${applicationScope['main.CustomCSS'] != null}">
		<link rel="stylesheet" href="<c:url value="${applicationScope['main.CustomCSS']}" />" type="text/css" />
	</c:if>

<% } %>
