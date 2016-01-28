angular.module('commDirectives',['CommServices'])
.directive('hello',function() {
    return {
        template:'../framework/partials/sbtable.html'
    }
})
.directive('resize', function ($window) {
    return function (scope, element) {
        var w = element;//angular.element($window);
        scope.getDimensions = function () {
            return {
                'h': w.height(),
                'w': w.width()
            };
        };
        scope.$watch(scope.getDimensions, function (newValue, oldValue) {
            scope.height = newValue.h;
            scope.width = newValue.w;

            scope.style = function () {
                return {
                    'height': (newValue.h - 100) + 'px',
                    'width': (newValue.w - 100) + 'px'
                };
            };

        }, true);

        angular.element($window).bind('resize',function () {
            scope.$apply();
        });
    }
})
.directive('rmxAutosize', function ($window) {
    return {
        replace:true,
        scope:{},
        link: function(scope, elem, attrs, controllerInstance) {
            var settings = RMX.evalWholeJson(attrs.rmxAutosize);
            var resizefn = function(event,wc) {
                settings.offset.w && $(elem).width(wc.width+settings.offset.w);
                settings.offset.h && $(elem).height(wc.height+settings.offset.h);
            };
            $(window).on('contentResize',resizefn);
            resizefn(null, window.contentSize);
        }
    }
})
.directive('myDialog', function() {
    return {
        restrict: 'EA',
        transclude: 'element',
        scope: {
            name:"@"
        },
        //templateUrl: '../framework/partials/my-dialog.html',
        link: function (scope, element) {
            scope.name = 'Jeff';
            scope.test = "ttttttt"
        }
    };
})
.directive('jqgrid',function() {
    var defalutSettings = {
        //url: mvcRoot + "/DataTableService/TeacherInfo/list.json",
        //postData: {extColumn: "academy.orgName;department.orgName;professionInfo.professionName;"},
        prmNames: {
            rows: "rowCount",
            page: "pagination"
        },
        styleUI: 'Bootstrap',
        datatype: "json",
        colNames: [],
        colModel: [],
        jsonReader: {
            root: "datas",
            page: "pagination",
            total: "pageCount",
            records: "recordCount",
            repeatitems: true,
            id: "id",
            //subgrid: {
            //    root:"rows",
            //    repeatitems: true,
            //    cell:"cell"
            //}
        },
        sortname: 'id',
        sortorder: "desc",
        viewrecords: true,
        multiselect: false,
        //autowidth: true,
        //autoHeight:true,
        height: "500px",
        rowNum: 20,
        rownumbers: true, // 显示行号
        rownumWidth: 35, // the width of the row numbers columns
        pager: "#jqGridPager",//分页控件的id
        subGrid: false//是否启用子表格
    };
    return {
        scope:{
            datavo:'=',
            name:'=',
            jqgrid:'='
        },
        templateUrl:'../framework/partials/jqgrid.html',
        replace:true,
        controller:function($scope, $element) {
            this.curRow = null;

            this.selectRow = function() {
                alert("aaa")
            };
        },
        link: function(scope, elem, attr, controllerInstance) {
            var attrSettings = eval("(" + ("{" + attr.settings + "}") + ")");
            var jqSettings = $.extend(true, {}, defalutSettings, {height:window.contentSize.height-220,width:window.contentSize.width-70},attrSettings);
            jqSettings.url && (jqSettings.url = mvcRoot +jqSettings.url);
            var dataTable = $("#jqGridTable",$(elem)).jqGrid(jqSettings);
            scope.number = scope.number + "33333 ";

            $(window).on('contentResize',function(event,wc) {
                jqSettings.width || dataTable.setGridWidth(wc.width-70); // 如果显式指定了宽度则不用自动调整
                dataTable.setGridHeight(wc.height-220);
                console.log(wc);
            })

            scope.jqgrid = dataTable;
        }
    }
})
.directive('rmxTable',function() {
        return {
            scope:true,//继承父且创建自己的作用于
            //scope:{
            //    datavo:'=',
            //    name:'=',
            //    jqgrid:'=',
            //    beansuri:'@',
            //    beansaveuri:'@',
            //    beandeluri: '@'
            //},
            //templateUrl:'../framework/partials/sbtable.html',
            //template:'<div ng-transclude></div>',
            //replace:true,
            //transclude: true,
            link: function(scope, elem, attrs, controllerInstance) {
                var opt = RMX.evalWholeJson(attrs.rmxTable);
                _.extend(scope, attrs);
                scope.dao = scope.BeansService.createBeanDao(opt);
                scope.dao.fetchBeans();

                _.each(opt.lists, function (item,name) {
                    scope.dao.fetchSubListAll(name);
                });
            },
            controller:function($scope, $element,BeansService) {
                $scope.BeansService = BeansService;
            }
        }
})
.directive('rmxTableProp',function() {
    return {
        scope:false,//共享父作用域
        require: ['^rmxTable'],
        link: function(scope, elem, attrs, controllerInstance) {
            //scope.subListAll = scope.dao.fetchSubListAll(attrs);
        }
    }
})
.directive('rmxTreeView',function() {
        return {
            scope:true,//继承父且创建自己的作用于
            //scope:{
            //    datavo:'=',
            //    name:'=',
            //    jqgrid:'=',
            //    beansuri:'@',
            //    beansaveuri:'@',
            //    beandeluri: '@'
            //},
            //templateUrl:'../framework/partials/sbtable.html',
            //template:'<div ng-transclude></div>',
            //replace:true,
            //transclude: true,
            link: function(scope, elem, attrs, controllerInstance) {
                var opt = RMX.evalWholeJson(attrs.rmxTreeView);
                opt.data= [
                    {
                        text: "Node 1",
                        icon: "glyphicon glyphicon-stop",
                        selectedIcon: "glyphicon glyphicon-stop",
                        color: "#000000",
                        backColor: "#FFFFFF",
                        href: "#node-1",
                        selectable: true,
                        state: {
                            checked: true,
                            disabled: false,
                            expanded: true,
                            selected: true
                        },
                        tags: ['available'],
                        nodes: [
                            {
                                text: "Child 1",
                                nodes: [
                                    {
                                        text: "Grandchild 1"
                                    },
                                    {
                                        text: "Grandchild 2"
                                    }
                                ]
                            },
                            {
                                text: "Child 2"
                            }
                        ]
                    },
                    {
                        text: "Parent 2"
                    },
                    {
                        text: "Parent 3"
                    },
                    {
                        text: "Parent 4"
                    },
                    {
                        text: "Parent 5"
                    }
                ];
                $(elem).treeview(opt);
            },
            controller:function($scope, $element,BeansService) {
                $scope.BeansService = BeansService;
            }
        }
})
.directive('rmxZtree',function() {
        return {
            scope:true,//继承父且创建自己的作用于
            //scope:{
            //    datavo:'=',
            //    name:'=',
            //    jqgrid:'=',
            //    beansuri:'@',
            //    beansaveuri:'@',
            //    beandeluri: '@'
            //},
            //templateUrl:'../framework/partials/sbtable.html',
            //template:'<div ng-transclude></div>',
            //replace:true,
            //transclude: true,
            link: function(scope, elem, attrs, controllerInstance) {
                var setting = {
                    view:{showIcon:true},
                    edit:{enable:true}
                };

                var zNodes =[
                    { name:"父节点1 - 展开", open:true,/*iconSkin:" fa fa-user ",*/
                        children: [
                            { name:"父节点11 - 折叠",
                                children: [
                                    { name:"叶子节点111"},
                                    { name:"叶子节点112"},
                                    { name:"叶子节点113"},
                                    { name:"叶子节点114"}
                                ]},
                            { name:"父节点12 - 折叠",
                                children: [
                                    { name:"叶子节点121"},
                                    { name:"叶子节点122"},
                                    { name:"叶子节点123"},
                                    { name:"叶子节点124"}
                                ]},
                            { name:"父节点13 - 没有子节点", isParent:true}
                        ]},
                    { name:"父节点2 - 折叠",
                        children: [
                            { name:"父节点21 - 展开", open:true,
                                children: [
                                    { name:"叶子节点211"},
                                    { name:"叶子节点212"},
                                    { name:"叶子节点213"},
                                    { name:"叶子节点214"}
                                ]},
                            { name:"父节点22 - 折叠",
                                children: [
                                    { name:"叶子节点221"},
                                    { name:"叶子节点222"},
                                    { name:"叶子节点223"},
                                    { name:"叶子节点224"}
                                ]},
                            { name:"父节点23 - 折叠",
                                children: [
                                    { name:"叶子节点231"},
                                    { name:"叶子节点232"},
                                    { name:"叶子节点233"},
                                    { name:"叶子节点234"}
                                ]}
                        ]},
                    { name:"父节点3 - 没有子节点", isParent:true}

                ];
                scope.zNodes = zNodes;
                scope.zTree = $.fn.zTree.init($(elem), setting, zNodes);
            },
            controller:function($scope, $element,BeansService) {
                $scope.BeansService = BeansService;
            }
        }
})