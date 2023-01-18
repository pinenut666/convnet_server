<%@ page session="false" pageEncoding="UTF-8" %>
<%@ include file="../common/includes.jsp" %>
<html>
<head>
    <title>组管理</title>
    <meta name="tab" content="group"/>
</head>
<body>

<table class="list-table" style="width: 100%;">
    <tr>
        <th>名称</th>
        <th>说明</th>
        <th>创建时间</th>
        <th style="width:180px;">操作</th>
    </tr>
    <c:forEach items="${groups}" var="group">
        <tr>
            <td>${group.name}</td>
            <td>${group.description}</td>
            <td><fmt:formatDate value="${group.createAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>
                <!--<a href="${ctx}/group/edit?id=${group.id}">编辑</a>&nbsp;-->
                <a href="${ctx}/group/delete?id=${group.id}" onclick="return confirm('确定要删除吗?')">删除</a>&nbsp;
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
