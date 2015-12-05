<%--
  Created by IntelliJ IDEA.
  User: guoqi
  Date: 2015/11/30
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miniarch</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <%--<!--加载requirejs-->--%>
        <%--<script src="${pageContext.request.contextPath}/plugins/require/require.js"  ></script>--%>
        <%--<script src="${pageContext.request.contextPath}/custom/global/js/require_conf.js"></script>--%>
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/bootstrap/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/font-awesome/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/ionicons/css/ionicons.min.css">
        <!-- Theme style -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin/css/AdminLTE.css">
        <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin/css/skins/_all-skins.min.css">
        <!-- iCheck -->
        <%--<link rel="stylesheet" href="plugins/iCheck/flat/blue.css">--%>
        <!-- Morris chart -->
        <!--<link rel="stylesheet" href="plugins/morris/morris.css">
        <!-- jvectormap -->
        <!--	<link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css">-->
        <!-- Date Picker -->
        <%--<link rel="stylesheet" href="plugins/datepicker/datepicker3.css">--%>
        <%--<!-- Daterange picker -->--%>
        <%--<link rel="stylesheet" href="plugins/daterangepicker/daterangepicker-bs3.css">--%>
        <%--<!-- bootstrap wysihtml5 - text editor -->--%>
        <%--<link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">--%>

        <%--<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->--%>
        <%--<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->--%>
        <%--<!--[if lt IE 9]>--%>
        <%--<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>--%>
        <%--<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>--%>
    </head>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- 头部 head-->
    <%@include file="comm/admin_head.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <!-- 左侧导航栏 left-->
    <%@include file="comm/admin_left.jsp" %>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Dashboard
                <small>Control panel</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Dashboard</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <%--<%@include file="comm/admin_left.jsp" %>--%>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 0.0.1 beta
        </div>
        <strong>Copyright &copy; 2015-2018 <a href="http://www.miniarch.com">Miniants Studio</a>.</strong> All rights
        reserved.
    </footer>
</div>
<!-- jQuery 2.1.4 -->
<script src="${pageContext.request.contextPath}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="${pageContext.request.contextPath}/static/plugins/fastclick/fastclick.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/static/admin/js/app.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<!-- Morris.js charts -->
<!--<script src="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>-->
<!--<script src="plugins/morris/morris.min.js"></script>-->
<!-- Sparkline -->
<!--<script src="plugins/sparkline/jquery.sparkline.min.js"></script>-->
<!-- jvectormap -->
<!--<script src="plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>-->
<!--<script src="plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>-->
<!-- jQuery Knob Chart -->
<!--<script src="plugins/knob/jquery.knob.js"></script>-->
<!-- daterangepicker -->
<!--<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js"></script>-->
<!--<script src="plugins/daterangepicker/daterangepicker.js"></script>-->
<!-- datepicker -->
<!--<script src="plugins/datepicker/bootstrap-datepicker.js"></script>-->
<!-- Bootstrap WYSIHTML5 -->
<!--<script src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>-->
<!-- Slimscroll -->
<!--<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>-->
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<!--<script src="dist/js/pages/dashboard.js"></script>-->
<!-- AdminLTE for demo purposes -->
<!--<script src="dist/js/demo.js"></script>-->
</body>
</html>
