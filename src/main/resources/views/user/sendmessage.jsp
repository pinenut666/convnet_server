<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<%@ include file="../common/includes.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="tab" content="user"/>
</head>
<body>
<form:form commandName="user" method="post" action="sendmessagetouser">
    <table class="form-table" style="margin: 10px 5px;">
        <tr>
            <th>登录名</th>
            <td>
                <c:choose>
                    <c:when test="${empty user.id}">
                        <form:input path="name" size="60" cssClass="validate[required]"/>
                    </c:when>
                    <c:otherwise>${user.name}<input type="hidden" value="${user.id}" name="id"></a></c:otherwise>
                </c:choose>

            </td>
        </tr>
        <tr>
            <th>发送</th>
            <td><input name="message"></td>
            <td><input type="submit" class="a-btn" value="发送"></td>
        <c:if test="${!empty msg}"><tr><td style="color: red" colspan="2">${msg}</td></tr></c:if>
        </tr>
    </table>
</form:form>
</body>
</html>
