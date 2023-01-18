<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<%@ include file="../common/includes.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="tab" content="user"/>
</head>
<body>
<table class="list-table" style="width: 100%;">
    <caption>
        <div style="float: left;">
            <form id="user" action="${ctx}/user" method="get">
                过滤条件|
                用户名：<input id="name" type="text" value="${name}" name="name"  style="width: 120px;height: 22px;">
                在线：<input type="checkbox" <s:if test="${isonline}"> checked="true" </s:if>value="true" id="isonline" name="isonline"></input>
                <button>过滤</button>
            </form>
        </div>
        <div style="float: right; padding-right: 20px;"><a href="${ctx}/user/edit" class="a-btn">添加用户</a></div>
    </caption>
    <tr>
        <th>登录名</th>
        <th>用户名</th>
        <th>注册时间</th>
        <th>最后登录时间</th>
        <th>最后登录IP</th>
        <th>状态</th>
        <th>收(最后离线)</th>
        <th>发(最后离线)</th>


        <th style="width:180px;">操作</th>
    </tr>
    <c:forEach items="${page.content}" var="user">
        <tr>
            <td>${user.name}</td>
            <td>${user.nickName}</td>
            <td><fmt:formatDate value="${user.createAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${user.userEx.lastLoginAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${user.userEx.lastLoginIp}</td>
            <td>${user.userEx.userIsOnline?'<div style="color:green">在线</div>':'<div style="color:#6d6d6d">离线</div>'}</td>

            <td>${user.userEx.reciveFromServerString}</td>
            <td>${user.userEx.sendToServerString}</td>

            <td>
                <a href="${ctx}/user/edit?id=${user.id}">编辑</a>&nbsp;
                <a href="${ctx}/user/sendmessage?id=${user.id}">发送</a>
                <s:if test="${user.userEx.userIsOnline}">
                    <a href="${ctx}/user/serverban?id=${user.id}">断开</a>
                </s:if>
                <!--

                <a href="${ctx}/user/delete?id=${user.id}" onclick="return confirm('确定要删除吗?')">删除</a>&nbsp;
                -->
            </td>
        </tr>
    </c:forEach>
</table>


   <div style="padding-top: 12px;font-size: 14px; color: #062b4e; text-align: right; padding-right: 30px;">
    共${page.totalElements}条
    第 ${page.number+1}/${page.totalPages} 页
    <a href="?page.page=${page.number}&name=${name}&isonline=${isonline}">上一页</a>
    <c:forEach  var="pagenum" begin="1" end="${page.totalPages}" >
        <s:if test="${page.number-5<pagenum and pagenum < page.number+5}">
            <s:if test="${pagenum==page.number+1}">
                <a href="?page.page=${pagenum}&name=${name}&isonline=${isonline}" style="color:#8b0000; font-weight:bold;font-size: 18px;">${pagenum}</a>
            </s:if>

            <s:if test="${pagenum!=page.number+1}">
                <a href="?page.page=${pagenum}&name=${name}&isonline=${isonline}">${pagenum}</a>
            </s:if>
        </s:if>
    </c:forEach>
     <s:if test="${page.totalPages>page.number+1}">
        <a href="?page.page=${page.number+2}&name=${name}&isonline=${isonline}">下一页</a>
     </s:if>
    每页${page.size}条
   </div>

</body>
</html>
