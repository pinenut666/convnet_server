<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<%@ include file="common/includes.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <link href="${ctx}/static/css/main.css" type="text/css" media="screen" rel="stylesheet"/>
    <link href="${ctx}/static/css/console.css" type="text/css" media="screen" rel="stylesheet"/>
    <link href="${ctx}/static/css/jquery.validationEngine.css" type="text/css" media="screen" rel="stylesheet"/>
    <script src="${ctx}/static/js/jquery-min.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.validationEngine.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/console.js" type="text/javascript"></script>
    <link rel="shortcut icon" href="${ctx}/static/img/forserver.ico" />
    <title>登录</title>
</head>
<body>
<div style="background-color: #fafafa;width: 300px;padding: 10px 50px;margin: 100px auto;border: 1px solid #eee;border-radius:5px;;box-shadow:2px 2px 3px #aaa;">
    <img src="${ctx}/static/img/z.png" style="margin-top: 20px;"/>

    <form action="" method="post">
        <table class="form-table" style="margin: 10px 5px;">
            <tr>
                <th>登录名</th>
                <td><input type="text" id="name" name="name" size="30" value="${param.name}" class="validate[required]"/></td>
            </tr>
            <tr>
                <th>密码</th>
                <td><input type="password" id="password" name="password" size="30" class="validate[required]"/></td>
            </tr>
            <tr>
                <td colspan="3" style="text-align: center;">
                    <input type="submit" class="a-btn" value="登录">
                    <input type="button" class="a-btn" value="找回密码" onclick="location.href='${ctx}/forget';"/>
                    <input type="button" class="a-btn" value="注册" onclick="location.href='${ctx}/regist';"/>
                </td>
            </tr>

            <div style="padding-top: 10px; line-height: 22px; color: #666666">
                <div >服务启动于：822端口</div>
                <div>在线人数：${sessionCount}</div>

            </div>
            <c:if test="${!empty msg}"><tr><td style="color: red" colspan="2">${msg}</td></tr></c:if>
        </table>
    </form>
</div>
</body>
</html>