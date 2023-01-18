<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>500 - Error Page</title>
</head>

<body>
<div id="content">
    <%
        Throwable ex = null;
        if (exception != null)
            ex = exception;
        if (request.getAttribute("javax.servlet.error.exception") != null)
            ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        LogFactory.getLog(requestUri == null ? "500.jsp" : requestUri).error(ex.getMessage(), ex);
    %>

    <h3>System Runtime Error: <br><%=ex.getMessage()%></h3>
    <br>

    <button onclick="history.back();">Back</button>
    <br>

    <p><a href="#" onclick="document.getElementById('detail_error_msg').style.display = '';">Administrator click here to get the detail.</a></p>

    <div id="detail_error_msg" style="display:none">
        <pre><%ex.printStackTrace(new java.io.PrintWriter(out));%></pre>
    </div>
</div>
</body>
</html>