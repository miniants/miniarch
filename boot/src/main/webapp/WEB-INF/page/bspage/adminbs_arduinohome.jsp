<%--
  Created by IntelliJ IDEA.
  User: guoqi
  Date: 2015/11/30
  Time: 21:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!-- Content Wrapper. Contains page content -->
<div ng-controller="pbyCtrl">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <myCustomer></myCustomer>
            考场屏蔽仪设置
            <small>设备维护管理</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li><a href="#">系统设置</a></li>
            <li class="active">设备维护管理</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="row" style="margin-bottom: 5px;">
            <div class="col-md-6">
                <div class="btn-group">
                    <button type="button" class="btn btn-primary btn-sm" ng-click="turnonAll()"><i class="fa fa-plus-square-o">全部打开</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="turnoffAll();"><i class="fa fa-plus-square-o">全部关闭</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="turnon(curBean);"><i class="fa fa-plus-square-o">关闭选择</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="turnoff(curBean);"><i class="fa fa-edit">打开选择</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="editType = 'props'"><i class="fa fa-edit">明细</i></button>
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary btn-sm" ng-click="delBean(curBean)">删除</button>
                        <button type="button" class="btn btn-primary btn-sm  dropdown-toggle" data-toggle="dropdown">
                            <span class="caret">…</span>
                            <span class="sr-only"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#">刷新</a></li>
                            <li class="divider"></li>
                            <li><a href="#">帮助</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="box box-info">
                    <div class="box-header">
                        <h3 class="box-title">教室列表</h3>
                        <!-- tools box -->
                        <%--<div class="pull-right box-tools">--%>
                            <%--<button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="新增" ><i class="fa fa-plus-square-o"></i></button>--%>
                            <%--<button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="编辑" ><i class="fa fa-edit">基本信息</i></button>--%>
                            <%--<button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="编辑" ><i class="fa fa-edit">角色</i></button>--%>
                        <%--</div><!-- /. tools -->--%>
                    </div><!-- /.box-header -->
                    <div class="box-body no-padding" style="/*height: 500px;overflow-y: auto;*/">
                        <table class="table table-hover">
                            <tr ng-repeat="user in beans" ng-click="selectBean(user)"
                                ng-style="{'background-color':user._noSaved? '#dd4b39':user==curBean?'#f4f4f4':''}">
                                <td>{{$index}}.</td>
                                <td>{{user.username}}</td>
                                <td>{{user.note}}</td>
                            </tr>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->


        </div><!-- /.row -->
    </section><!-- /.content -->
</div><!-- /inner .content-wrapper -->