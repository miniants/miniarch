<%@ page import="cn.remex.core.exception.ServiceCode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String state_redirect = (String)request.getAttribute("body");
    String code = (String) request.getAttribute("code");
    String msg = (String) request.getAttribute("msg");
%>
<html>
<head>
    <% if(ServiceCode.ERROR.equals(code)){ //验证没有通过 %>
    <!-- 没有 bs code 则不跳转 -->
    <%}else { //登录成功后 %>
    <meta http-equiv="Refresh" content="0; url=../..<%= state_redirect.trim()%>" />
    <%}%>
</head>
<body>
<% if(ServiceCode.ERROR.equals(code) ){ %>
微信菜单回调信息：<%= msg %>
<% }%>
</body>
</html>
