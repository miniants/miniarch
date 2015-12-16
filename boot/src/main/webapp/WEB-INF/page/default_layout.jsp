<%--
  Created by IntelliJ IDEA.
  User: guoqi
  Date: 2015/11/30
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<c:set var="mvcRoot" value="${pageContext.request.contextPath}/smvc/"></c:set>
<!DOCTYPE html>
<html ng-app="indexApp">
    <head ng-controller="mainCtrl">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miniarch </title>
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin/css/AdminLTE.min.css">
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

        <!-- jQuery 2.1.4 -->
        <script src="${pageContext.request.contextPath}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/plugins/underscore/underscore-min.js"></script>
        <script src="${pageContext.request.contextPath}/static/plugins/angular-1.3.9/angular.js"></script>


    </head>
<body class="hold-transition skin-blue sidebar-mini fixed">
<div class="wrapper">
    <!-- 头部 head-->
    <%@include file="comm/admin_head.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <!-- 左侧导航栏 left-->
    <%@include file="comm/admin_left.jsp" %>
    <!-- Content Wrapper. Contains page content -->
    <%--<div class="content-wrapper">
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
        </section>
        <!-- /.content -->
    </div>--%>
    <div class="content-wrapper">
    <jsp:include page="${rp}.jsp"></jsp:include>
    </div><!-- /.content-wrapper -->
    <!-- /.content-wrapper -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 0.0.1 beta
        </div>
        <strong>Copyright &copy; 2015-2018 <a href="http://www.miniarch.com">Miniants Studio</a>.</strong> All rights
        reserved.
    </footer>
</div>

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
<script>
    var mvcRoot = "${mvcRoot}";

    var app = angular.module('indexApp', []);
    app.factory('Data', function() {
        return {
            name: "IndexData",
        }
    });
    app.controller('mainCtrl', function($scope, $http, Data) {
        $http.get(mvcRoot+"AdminBs/adminIndex.json")
                .success(function (response) {
                    Data.sysMenus = response.sysMenus;
                });
    }).controller('navCtrl', function($scope, $http) {
    }).controller('menuCtrl', function($scope, $http, Data) {
        $scope.Data=Data;
    });
    app.service('DataService',function($http){
        var ctx = this;
        //通用数据组件
        ctx.dataVos={};
        ctx.fetch=function(serviceUri,pagination,callback,options){
            var UUID = serviceUri+angular.toJson(options);
            var options = options || {};
            options.pagination=pagination;

            if(ctx.dataVos[UUID]&&ctx.dataVos[UUID][pagination]){//读缓存
                callback.call(ctx,ctx.dataVos[UUID][pagination],ctx.dataVos[UUID]);
            }else{//读服务器
                $http.post(mvcRoot+serviceUri+".json",options).success(function (response) {
                    (!ctx.dataVos[UUID]) && (ctx.dataVos[UUID]=response);
                    ctx.dataVos[UUID][pagination]=response.datas;
                    ctx.dataVos[UUID].pages=[];//初始化分页
                    for (var i = 1; i<=ctx.dataVos[UUID].pageCount; i++) {
                        ctx.dataVos[UUID].pages.push(i);
                    }
                    callback.call(ctx,ctx.dataVos[UUID][pagination],ctx.dataVos[UUID]);
                });
            }
        };

        //功能权限
        ctx.fetchUris=function(pagination,callback,options){
            ctx.fetch("AdminBs/uris",pagination,callback,options);
        }

        //角色
        ctx.roles={};
        ctx.rolesVo;
        ctx.initRoles=function(callback,options){
            ctx.fetch("AdminBs/roles",1,function(roles,rolesVo){
                ctx.roles=roles;
                ctx.rolesVo=rolesVo;
                callback(roles,rolesVo);
            },options);
        }

    });

    app.directive('rmxGrid',function($scope,DataService){
        var beans=[1];
//        DataService.fetch("AdminBs/uris",1,function(uris){
//            beans=uris;
//        });
        return {
            restrict: 'A',
            transclude: true,
            scope: {
                beans:'='
,                name:'111'
            },
            controller: function($scope) {
                var panes = $scope.panes = [];
                $scope.name = "controller set the name";

                $scope.select = function(pane) {
                    angular.forEach(panes, function(pane) {
                        pane.selected = false;
                    });
                    pane.selected = true;
                };
            },
            //templateUrl: 'my-tabs.html'
        };
    });
    app.controller('rolesCtrl', function($scope, $http, Data,DataService) {
        $scope.editType="uri"; //uri | baseForm
        $scope.roles={};
        $scope.uris={};
        $scope.curRole={};
        $scope.bckRoles={};
        $scope.newRole=undefined;
        $scope.urisVo=undefined;


        $scope.indexUri = function(role,uri){
            return _.findIndex(role.authUris,function(i){
                return i.id==uri.id
            });
        };
        $scope.checkUri = function(role,uri,checked){
            var idx = $scope.indexUri(role,uri);
            if(checked && idx==-1){ //勾上了而且当前角色没有,则添加进去
                role.authUris.push(uri);
                role._noSaved=true;
            }else if(idx>-1){
                role.authUris.splice(idx,1);
                role._noSaved=true;
            }
        };

        $scope.selectRole=function(role){
            $scope.bckRoles[role.id] = angular.copy(role);//保存副本用户reset
            $scope.curRole = role;
            if(!role.authUris){//如果没有权限,则取后台取一下
                $http.get(mvcRoot+"AdminBs/roleUris/"+(role.id)+".json").success(function (response) {
                    role.authUris=response.datas || [];
                });
            }
        };
        $scope.createRole=function(role){
            if($scope.newRole){
                alert("新建角色尚未保存,请保存后再新建.");
                return;
            }
            $scope.curRole = angular.extend({name:"unnamed"+(Math.random()*1000+10000).toFixed(0),authUris:[],_noSaved:true,_isNew:true},role);
            $scope.newRole = $scope.curRole;
            $scope.roles.unshift($scope.curRole);
            $scope.editType='baseForm';
        };
        $scope.resetRoleBase=function(role){
            if(role._isNew){
                $scope.roles.splice($scope.roles.indexOf($scope.curRole),1);
                $scope.curRole==role && ($scope.curRole=$scope.roles && $scope.roles[0]);//如果当前就是删除的,需要重新选中
                $scope.newRole=undefined;
            }else{
                angular.extend($scope.curRole,$scope.bckRoles[role.id]);
                $scope.curRole._noSaved=false;
            }
        };
        $scope.saveRole=function(role){
            $http.post(mvcRoot+"AdminBs/saveRole/"+(role.id||-1)+".json",role).success(function (response) {
                role._noSaved=!response.status;
                if(role._noSaved){
                    alert(response.msg);
                }else{
                    role.id=response.body.id;
                    role._isNew=undefined;
                    role._noSaved=false;
                    ($scope.newRole==role) && ($scope.newRole=undefined);//如果是新建对象保存了,则清空当前新建的对象,这样就允许继续新建了.
                }
            });
        };
        $scope.delRole=function(role){
            if(role._isNew){
                $scope.roles.splice($scope.roles.indexOf(role),1);//删除
                $scope.curRole==role && ($scope.curRole=$scope.roles && $scope.roles[0]);//如果当前就是删除的,需要重新选中
            }else{
                $http.post(mvcRoot+"AdminBs/delRole/"+(role.id)+".json",role).success(function (response) {
                    if(!response.status){
                        alert(response.msg);
                    }else{
                        $scope.roles.splice($scope.roles.indexOf(role),1);//删除
                        $scope.curRole==role && ($scope.curRole=$scope.roles && $scope.roles[0]);//如果当前就是删除的,需要重新选中
                    }
                });
            }
        };

        $scope.showUris=function(pagination){
            DataService.fetchUris(pagination,function(uris,urisVo){
                $scope.uris = uris;
                $scope.urisVo = urisVo;
            });
        };
        $scope.showUris(1);

        DataService.initRoles(function(){
            $scope.roles = DataService.roles;
            $scope.roles && $scope.selectRole($scope.roles[0]);
        });

    });

    var BeanEditWithListFieldCtrlFactory = function(options){
        return function($scope, $http, DataService){
            var     beansUri=options.beansUri,
                    delBeanUri=options.delBeanUri,
                    saveBeanUri=options.saveBeanUri,
                    propsOfBeanUri=options.propsOfBeanUri,
                    propsUri=options.propsUri;

            var beanName='',propName='roles';
            var newBeanFac=function(){
                var newBean={username:"unnamed"+(Math.random()*1000+10000).toFixed(0),_noSaved:true,_isNew:true};
                newBean[propName]=[];
                return newBean;
            };//新建bean的模板
            var _delBean = function(bean){
                $scope.beans.splice($scope.beans.indexOf(bean),1);//删除
                $scope.curBean==bean && ($scope.curBean=($scope.beans && $scope.beans[0]));//如果当前就是删除的,需要重新选中
                $scope.newBean=undefined;
            }

            $scope.editType="props"; //props | base
            $scope.beans={};
            $scope.props={};
            $scope.curBean={};
            $scope.bckBeans={};
            $scope.newBean=undefined;
            $scope.propsVo=undefined;


            $scope.indexUri = function(bean,prop){
                return _.findIndex(bean[propName],function(i){
                    return i.id==prop.id
                });
            };
            $scope.checkUri = function(bean,prop,checked){
                var idx = $scope.indexUri(bean,prop);
                if(checked && idx==-1){ //勾上了而且当前角色没有,则添加进去
                    bean[propName].push(prop);
                    bean._noSaved=true;
                }else if(idx>-1){
                    bean[propName].splice(idx,1);
                    bean._noSaved=true;
                }
            };
            $scope.selectBean=function(bean){
                $scope.bckBeans[bean.id] = angular.copy(bean);//保存副本用户reset
                $scope.curBean = bean;
                if(!bean[propName]){//如果没有权限,则取后台取一下
                    $http.get(mvcRoot+propsOfBeanUri+(bean.id)+".json").success(function (response) {
                        bean[propName]=response.datas || [];
                    });
                }
            };
            $scope.createBean=function(bean){
                if($scope.newBean){
                    alert("新建对象尚未保存,请保存后再新建!");
                    return;
                }
                $scope.curBean = angular.extend(newBeanFac(),bean);
                $scope.newBean = $scope.curBean;
                $scope.beans.unshift($scope.curBean);
                $scope.editType='base';
            };
            $scope.resetBeanBase=function(bean){
                if(bean._isNew){
                    _delBean(bean);
                }else{
                    angular.extend($scope.curBean,$scope.bckBeans[bean.id]);
                    $scope.curBean._noSaved=false;
                }
            };
            $scope.saveBean=function(bean){
                $http.post(mvcRoot+saveBeanUri+(bean.id||-1)+".json",bean).success(function (response) {
                    bean._noSaved=!response.status;
                    if(bean._noSaved){
                        alert(response.msg);
                    }else{
                        bean.id=response.body.id;
                        bean._isNew=undefined;
                        bean._noSaved=false;
                        ($scope.newBean==bean) && ($scope.newBean=undefined);//如果是新建对象保存了,则清空当前新建的对象,这样就允许继续新建了.
                    }
                });
            };
            $scope.delBean=function(bean){
                if(bean._isNew){
                    _delBean(bean);
                }else{
                    $http.post(mvcRoot+delBeanUri+(bean.id)+".json",bean).success(function (response) {
                        if(!response.status){
                            alert(response.msg);
                        }else{
                            _delBean(bean);
                        }
                    });
                }
            };

            $scope.showProps=function(pagination){
                DataService.fetch(propsUri,pagination,function(props,propsVo){
                    $scope.props = props;
                    $scope.propsVo = propsVo;
                });
            };
            $scope.showProps(1);

            DataService.fetch(beansUri,1,function(beans,beansVo){
                $scope.beans = beans;
                beans && beans.length>0 && $scope.selectBean(beans[0]);
            },{rowCount:100});
        }
    }
    app.controller('usersCtrl', BeanEditWithListFieldCtrlFactory({
        beansUri:'AdminBs/users',
        delBeanUri:'AdminBs/delUser/',
        saveBeanUri:'AdminBs/saveUser/',
        propsOfBeanUri:'AdminBs/rolesOfUser/',
        propsUri:'AdminBs/roles'})
    );


//    app.directive('myCustomer', function() {
//        return {
//            template: 'Name: {{customer.name}} Address: {{customer.address}}'
//        };
//    }).directive('tabs', function() {
//        return {
//            restrict: 'AE',
//            transclude: true,
//            scope: {},
//            controller: [ "$scope", function($scope) {
//                var panes = $scope.panes = [];
//
//                $scope.testa = function(){
//                    alert();
//                }
//                $scope.select = function(pane) {
//                    angular.forEach(panes, function(pane) {
//                        pane.selected = false;
//                    });
//                    pane.selected = true;
//                }
//
//                this.addPane = function(pane) {
//                    if (panes.length == 0) $scope.select(pane);
//                    panes.push(pane);
//                }
//            }],
//            template:
//            '<div class="tabbable">' +
//            '<ul class="nav nav-tabs">' +
//            '<li ng-repeat="pane in panes" ng-class="{active:pane.selected}">'+
//            '<a href="" ng-click="select(pane)">{{pane.title}}{{test}}</a>' +
//            '</li>' +
//            '</ul>' +
//            '<div class="tab-content" ng-transclude></div>' +
//            '</div>',
//            replace: false
//        };
//    }).directive('pane', function() {
//        return {
//            require: '^tabs',
//            restrict: 'AE',
//            transclude: true,
//            scope: { title: '@' },
//            link: function(scope, element, attrs, tabsCtrl) {
//                tabsCtrl.addPane(scope);
//            },
//            template:
//            '<div class="tab-pane" ng-class="{active: selected}" ng-transclude>' +
//            '</div>',
//            replace: true
//        };
//    }).directive('rmxgrid', function() {
//        return {
//            restrict: 'AE',
//            transclude: true,
//            scope: false,
//            controller: [ "$scope", function($scope) {
//                var beans = $scope.beans=Math.random();
//                var test = $scope.test="test"
//                $scope.testGrid = function(pane) {
//                    $scope.test=$scope.test+1;
//                }
//            }],
//            link: function(scope, element, attrs, tabsCtrl) {
//            },
//            template:
//            '<div class="tab-content" ng-transclude></div>' +
//            '</div>',
//            replace: false
//        };
//    })
</script>
</body>
</html>
