angular.module('CommControllers',['CommServices'])
    .controller('mainCtrl', function ($scope, $http, Data) {
        $scope.rolename = "";
        $scope.username = RMX.getUser().username;
        $scope.userlogo = "../plugins/adminlte/img/user2-160x160.jpg";
        $scope.tabPanel = {curTab: {nodeName: "首页"}};
        $scope.updateActiveTab = function (curTab, supTab) {
            $scope.tabPanel.curTab = curTab || $scope.tabPanel.curTab;
            $scope.tabPanel.supTab = supTab;
        };

    })
.controller('navCtrl', function ($scope, $http) {
})
.controller('menusCtrl', function ($scope, $http, Data) {
    $scope.sysMenus = Data.sysMenus;

})
.controller('contentCtrl', function ($scope, $rootScope, $http, Data) {
    $scope.sysMenus = Data.sysMenus;
})
.controller('rolesCtrl', function ($scope, $http, Data) {
})
.controller('usersCtrl', function() {

})
.controller('boardCtrl', function() {

})
.controller('sysconfigCtrl', ["$scope","$http",function($scope, $http) {
    $scope.resetDataBaseMsg = "暂无信息！";
    $scope.resetDataBase = function() {
        $scope.resetDataBaseMsg = "正在重置数据库表结构...";
        $http.post(mvcRoot+"/AdminBs/resetDb.json?resetDb=true")
            .success(function(resp) {
                $scope.resetDataBaseMsg = resp.msg;
            });
    };
        $scope.resetSysUrisMsg = "暂无信息！";
        $scope.resetSysUrisFilter = {uri:""};
        $scope.resetSysUris = function() {
            $scope.resetSysUrisMsg = "正在更新权限URI...";
            $http.post(mvcRoot+"/AdminBs/resetSysUris.json")
                .success(function(resp) {
                    $scope.resetSysUrisMsg = "更新的权限URI：";
                    $scope.sysUris = resp.body;
                });
    };

}]);