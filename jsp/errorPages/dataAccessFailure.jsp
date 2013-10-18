<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@ page import="org.springframework.dao.DataAccessException"%>

<html>
<body>
<%
Exception ex = (Exception) request.getAttribute("exception");
response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
%>

<h2>Data access failure:</h2>
<h3> <%= ex.getMessage() %> </h3>

<hr>
<% if (application.getAttribute("main.AdministratorEmail") != null){ %>
<a href="mailto:<%= application.getAttribute("main.AdministratorEmail")%> ">Contact your administrator about this error.</a>
<% } %>


</body>
</html>