<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<%@ include file="../common/includes.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="tab" content="user"/>
</head>
<body>
<form:form commandName="user" method="post">
    <table class="form-table" style="margin: 10px 5px;">
        <tr>
            <th>登录名</th>
            <td>
                <c:choose>
                    <c:when test="${empty user.id}"><form:input path="name" size="60" cssClass="validate[required]"/></c:when>
                    <c:otherwise>${user.name}</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>昵称</th>
            <td><form:input path="nickName" size="60" cssClass="validate[required]"/></td>
        </tr>
        <tr>
            <th>密码</th>
            <td><form:password path="password" size="60"/></td>
        </tr>
        <tr>
            <th>启用</th>
            <td><form:checkbox path="enabled"/>启用</td>
        </tr>
        <tr>
            <th>上行限制bps（不填写则为服务器默认）</th>
            <td><form:input path="sendLimit"/></td>
        </tr>
        <tr>
            <th>下行限制bps（不填写则为服务器默认）</th>
            <td><form:input path="reciveLimit"/></td>
        </tr>
        <tr>
            <th>是否允许创建组</th>
            <td><form:checkbox path="canCreateGroup"/></td>
        </tr>
        <tr>
            <th>是否允许加入\用户\组</th>
            <td><form:checkbox path="canAddfriend"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" class="a-btn" value="保存">
                <input type="button" class="a-btn" value="返回" onclick="location.href='${ctx}/user'">
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
