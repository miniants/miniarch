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
<div ng-controller="rolesCtrl">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            角色管理
            <small>角色新增|编辑|删除,角色权限维护 {{Data.name}}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li><a href="#">传统设置</a></li>
            <li class="active">角色</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="col-md-4">
                <div class="box box-info">
                    <div class="box-header">
                        <h3 class="box-title">角色列表</h3>
                        <!-- tools box -->
                        <div class="pull-right box-tools">
                            <button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="新增" ng-click="createRole();"><i class="fa fa-plus-square-o"></i></button>
                            <button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="编辑" ng-click="editType = 'baseForm'"><i class="fa fa-edit">基本信息</i></button>
                            <button class="btn btn-primary btn-sm pull-right" data-toggle="tooltip" title="编辑" ng-click="editType = 'uri'"><i class="fa fa-edit">权限</i></button>
                        </div><!-- /. tools -->
                    </div><!-- /.box-header -->
                    <div class="box-body no-padding" style="/*height: 500px;overflow-y: auto;*/">
                        <table class="table table-hover">
                            <%--<tr>
                                <th style="width: 10px">#</th>
                                <th style="width: 80px">角色</th>
                                <th>描述</th>
                            </tr>--%>
                            <tr ng-repeat="role in roles" ng-click="selectRole(role)"
                                ng-style="{'background-color':role._noSaved? '#dd4b39':role==curRole?'#f4f4f4':''}">
                                <td>{{$index}}.</td>
                                <td>{{role.name}}</td>
                                <td>{{role.note}}</td>
                            </tr>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->
            </div><!-- /.col -->
            <div class="col-md-8 ">
                <div class="box box-info" ng-show="editType == 'uri'">
                    <div class="box-header">
                        <h3 class="box-title">{{curRole.name || "[无名称]"}}的权限</h3>
                        <div class="box-tools">
                            <ul class="pagination pagination-sm no-margin pull-right">
                                <li><a href="#" ng-click="showUris(1)">&laquo;</a></li>
                                <li ng-repeat="up in urisVo.pages"><a href="#" ng-click="showUris(up)">{{up}}</a></li>
                                <li><a href="#" ng-click="showUris(urisVo.pageCount)">&raquo;</a></li>
                            </ul>
                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body no-padding">
                        <table class="table">
                            <%--<tr>
                                <th style="width: 10px">#</th>
                                <th>权限功能描述</th>
                            </tr>--%>
                            <tr ng-repeat="uri in uris">
                                <td style="width: 40px;"><input type="checkbox" ng-click="checkUri(curRole,uri,$event)" ng-checked="indexUri(curRole,uri)>-1"></td>
                                <td style="width: 50px">{{$index}}.</td>
                                <td style="width: 80px"><span ng-if="indexUri(curRole,uri)>-1" class="label label-success">Approved</span></td>
                                <td>{{uri.uriName}}</td>
                                <td>{{uri.uri}}</td>
                            </tr>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->
                <div class="box box-info" ng-show="editType == 'baseForm'">
                    <div class="box-header with-border">
                        <h3 class="box-title">{{curRole.name || "请选择/输入角色名称"}}</h3>
                    </div><!-- /.box-header -->
                    <!-- form start -->
                    <form class="form-horizontal">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="inputRoleName" class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="inputRoleName" placeholder="角色名称" ng-model="curRole.name" ng-change="curRole._noSaved=true">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputRoleDesc" class="col-sm-2 control-label">描述</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="inputRoleDesc" placeholder="角色描述" ng-model="curRole.note" ng-change="curRole._noSaved=true">
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <button type="button" class="btn btn-default" ng-click="resetRoleBase(curRole)">取消</button>
                            <button type="button" class="btn btn-default" ng-click="delRole(curRole)">删除</button>
                            <button type="button" class="btn btn-info pull-right" ng-click="saveRole(curRole)">保存</button>
                        </div><!-- /.box-footer -->
                    </form>
                </div>
            </div><!-- /.col -->

        </div><!-- /.row -->
    </section><!-- /.content -->
</div><!-- /inner .content-wrapper -->