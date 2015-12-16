<%--
  Created by IntelliJ IDEA.
  User: guoqi
  Date: 2015/11/30
  Time: 21:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<aside class="main-sidebar" ng-controller="menuCtrl">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="${pageContext.request.contextPath}/static/admin/img/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>Alexander Pierce</p>
                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header">Miniarch菜单{{Data.sysMenus[0].nodeName}}</li>


            <li ng-repeat="sysMenu in Data.sysMenus" class="{{sysMenu.checked | filter:{true:'active'} }} {{sysMenu.parentFlag | filter:{true:'treeview'} }}">
                <a href="{{sysMenu.nodeUri}}">
                    <i class="fa fa-dashboard"></i>
                    <span>{{sysMenu.nodeName}}</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li ng-repeat="s1menu in sysMenu.subMenus" class="{{s1menu.checked | filter:{true:'active'} }}"><a href="${mvcRoot}{{s1menu.nodeUri}}"><i class="fa {{s1menu.icon}}"></i>{{s1menu.nodeName}}</a></li>
                </ul>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>
