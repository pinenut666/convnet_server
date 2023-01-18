<%@ page import="net.convnet.server.util.SitemeshHelper" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% SitemeshHelper.extractMeta(pageContext); %>
<!DOCTYPE html>
<html>
<head>
    <title>Convnet Console - ${_title}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <link rel="shortcut icon" href="${ctx}/static/img/forserver.ico" />
    <link href="${ctx}/static/css/main.css" type="text/css" media="screen" rel="stylesheet"/>
    <link href="${ctx}/static/css/console.css" type="text/css" media="screen" rel="stylesheet"/>
    <link href="${ctx}/static/css/jquery.validationEngine.css" type="text/css" media="screen" rel="stylesheet"/>
    <script src="${ctx}/static/js/jquery-min.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.validationEngine.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/console.js" type="text/javascript"></script>
    <script type="text/javascript">var _ctx='${ctx}';</script>
    ${_head}
</head>
<body>

<div id="container">

    <div class="tab">
        <div style="overflow: auto; ">
            <img src="${ctx}/static/img/z.png" style="margin: 20px;"/>
            <a href="${ctx}/logout" style="display: block;float: right;padding: 50px; padding-bottom: 0px;">注销</a>
        </div>
        <div style="clear: both">
            <ul class="clearfix">
                <li${_meta.tab=='user'?' class="active"':''}><a href="${ctx}/user">用户管理</a></li>
                <!--<li${_meta.tab=='group'?' class="active"':''}><a href="${ctx}/group">组管理</a></li>-->
            </ul>
        </div>
    </div>
    <div id="tbody">${_body}</div>
</div>
</body>
</html>