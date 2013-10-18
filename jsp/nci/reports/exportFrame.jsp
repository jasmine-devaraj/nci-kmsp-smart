<%--
/**
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Reston, VA 20190 USA
 * Copyright (c) 2000-2009 Altum, Inc.
 * All rights reserved.
 */ --%>
<%@taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<frameset  rows="99%, 1%">
	<frame src="<c:url value="/jsp/nci/reports/exportInclude.jspf" />">
	<frame src="<c:url value="/exports/${param.fileName}" />">
</frameset>
</html>
