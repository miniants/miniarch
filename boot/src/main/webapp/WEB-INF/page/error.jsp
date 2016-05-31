<!-- This Header is for Ajax to assert the request is ErrorPage!  -->
<%@page contentType="text/html; charset=utf-8" language="java"  isErrorPage="true" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  
    <title>服务器出现异常了</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  <meta http-equiv="Content-Type" content="text/html; charset=gbk"></head>
  
  <body>
  <p style="width: 500px"><pre>
<%

  Exception ex=(Exception)request.getAttribute("exception");
  if(null!=ex)out.println(ex);
  if(null!=exception)out.println(exception);

%>
  </pre></p>
  </body>
</html>
