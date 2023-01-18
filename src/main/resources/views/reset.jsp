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
    <h2 style="font-size: 16px; padding-top: 20px; color:#660000; padding-left: 17px;">Convnet 密码设置</h2>
    <form action="" method="post">
        <table class="form-table" style="margin: 10px 5px;">
            <table class="form-table" style="margin: 10px 5px;">
                <tr><input type="hidden" name="id" value="${user.id}"/>
                    <th>登录名</th>
                    <td>${user.name}</td>
                </tr>
                <tr>
                    <th>密码</th>
                    <td><input type="password" id="password" name="password" size="30" class="validate[required]"/></td>
                </tr>
                <tr>
                    <th>密码重复</th>
                    <td><input type="password" id="password1" name="password1" size="30" class="validate[required]"/></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input type="submit" class="a-btn" value="保存">
                    </td>
                </tr>
                <c:if test="${!empty msg}"><tr><td style="color: red" colspan="2">${msg}</td></tr></c:if>
            </table>
    </form>
</div>

</body>
</html>