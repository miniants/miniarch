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
<div ng-controller="usersCtrl">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <myCustomer></myCustomer>
            用户管理
            <small>用户新增|编辑|删除,角色维护</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li><a href="#">系统设置</a></li>
            <li class="active">用户</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="row" style="margin-bottom: 5px;">
            <div class="col-md-6">
                <div class="btn-group">
                    <button type="button" class="btn btn-primary btn-sm" ng-click="saveBean(curBean)"><i class="fa fa-plus-square-o">保存</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="createBean(curBean);"><i class="fa fa-plus-square-o">新建</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="resetBaseBase(curBean);"><i class="fa fa-plus-square-o">重置</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="editType = 'base'"><i class="fa fa-edit">基本信息</i></button>
                    <button type="button" class="btn btn-primary btn-sm" ng-click="editType = 'props'"><i class="fa fa-edit">角色</i></button>
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
                        <h3 class="box-title">用户列表</h3>
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



                <%--<div tabs>
                    11111111111111
                    <pane title="First Tab">
                        <div>This is the content of the first tab.{{title}}</div>
                    </pane>
                    <pane title="Second Tab">
                        <div>This is the content of the second tab.</div>
                    </pane>
                </div>
                <div rmxgrid>
                    <div ng-click="testGrid()">sdfasdfasfasf</div>
                </div>--%>
            </div><!-- /.col -->
            <div class="col-md-8 ">
                <div class="box box-info" ng-show="editType == 'props'">
                    <div class="box-header">
                        <h3 class="box-title">{{curBean.name || "[无名称]"}}的角色</h3>
                        <div class="box-tools">
                            <ul class="pagination pagination-sm no-margin pull-right">
                                <li><a href="#" ng-click="showProps(1)">&laquo;</a></li>
                                <li ng-repeat="up in propsVo.pages"><a href="#" ng-click="showProps(up)">{{up}}</a></li>
                                <li><a href="#" ng-click="showProps(propsVo.pageCount)">&raquo;</a></li>
                            </ul>
                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body no-padding">
                        <table class="table">
                            <tr ng-repeat="role in props">
                                <td style="width: 40px;"><input type="checkbox" ng-click="checkUri(curBean,role,$event)" ng-checked="indexUri(curBean,role)>-1"></td>
                                <td style="width: 50px">{{$index}}.</td>
                                <td style="width: 80px"><span ng-if="indexUri(curBean,role)>-1" class="label label-success">Approved</span></td>
                                <td>{{role.name}}</td>
                                <td>{{role.note}}</td>
                            </tr>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->
                <div class="box box-info" ng-show="editType == 'base'">
                    <div class="box-header with-border">
                        <h3 class="box-title">{{curBean.username || "请选择/输入角色名称"}}</h3>
                    </div><!-- /.box-header -->
                    <!-- form start -->
                    <form class="form-horizontal">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="inputRoleName" class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="inputRoleName" placeholder="用户名称" ng-model="curBean.username" ng-change="curBean._noSaved=true">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputRoleDesc" class="col-sm-2 control-label">描述</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="inputRoleDesc" placeholder="用户描述" ng-model="curBean.note" ng-change="curBean._noSaved=true">
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                        <%--<div class="box-footer">--%>
                            <%--<button type="button" class="btn btn-default" ng-click="resetBeanBase(curBean)">取消</button>--%>
                            <%--<button type="button" class="btn btn-default" ng-click="delBean(curBean)">删除</button>--%>
                            <%--<button type="button" class="btn btn-info pull-right" ng-click="saveBean(curBean)">保存</button>--%>
                        <%--</div><!-- /.box-footer -->--%>
                    </form>
                </div>
            </div><!-- /.col -->

        </div><!-- /.row -->
    </section><!-- /.content -->
</div><!-- /inner .content-wrapper -->