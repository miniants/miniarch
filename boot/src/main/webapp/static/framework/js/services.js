angular.module('CommServices', [])
.service('UserService',function($http) {
        var userService = {};

        userService.logon = function(username,password) {
            $http.post(mvcRoot + '/AdminBs/logon' + ".json", {username:username,password:password}).success(function (response) {
                RMX.setCookies('UID', username);
                RMX.setCookies('TKN', 'TKN');
                console.log(response);
            });
        };
        return userService;
})
.service('BeansService', function ($http) {
    var ctx = this;
    ctx.test = function(){
        alert(111);
    };
    //通用数据组件
    ctx.dataVos = {};
    ctx.fetch = function (serviceUri, pagination, callback, options) {
        var UUID = serviceUri + angular.toJson(options);
        var options = options || {};
        options.pagination = pagination;


        $http.post(mvcRoot + serviceUri + ".json", options).success(function (response) {
            var datasVo = response;
            datasVo.pages = [];//初始化分页
            for (var i = 1; i <= datasVo.pageCount; i++) {
                datasVo.pages.push(i);
            }
            callback.call(ctx, datasVo.datas, datasVo);
        });

    };

    ctx.createBeanDao = function (options) {
        var beanDao = _.extend({
            editType : "base", //[prop] | base
            beansUri:null,
            beanSaveUri:null,
            beanDelUri:null,
            beans : [],
            beansVo:{},
            lists:{},
            bckBeans : [],
            curBean : null,
            defBean : {_noSaved: true, _isNew: true}
            },options);


        var _removeBean = function (bean) {
            beanDao.beans.splice(beanDao.beans.indexOf(bean), 1);//删除
            beanDao.curBean == bean && (beanDao.curBean = (beanDao.beans && beanDao.beans[0]));//如果当前就是删除的,需要重新选中
            beanDao.newBean = undefined;
        }


        //获取当前页面的beans
        beanDao.fetchBeans = function (page, rowCount) {
            ctx.fetch(beanDao.beansUri, page || 1, function (beans, beansVo) {
                beanDao.beans = beans;
                beanDao.beansVo = beansVo;
                beans && beans.length > 0 && beanDao.selectBean(beans[0]);
            }, {rowCount: rowCount || 10});
        };
        //选择默认操作的bean，保存副本以备reset
        beanDao.selectBean = function (bean) {
            beanDao.bckBeans[bean.id] = angular.copy(bean);
            beanDao.curBean = bean;
            _.each(beanDao.lists,function(list,name) {
                if (!bean[name]) {//如果没有,则取后台取一下
                    $http.get(mvcRoot + list.listUri + (bean.id) + ".json").success(function (response) {
                        bean[name] = response.datas || [];
                    });
                }
            });
        };
        //创建一个默认的bean
        beanDao.createBean = function (bean) {
            bean = bean || beanDao.curBean;
            if (beanDao.newBean) {
                alert("新建对象尚未保存,请保存后再新建!");
                return;
            }
            beanDao.curBean = angular.extend({}, beanDao.defBean, bean, {id: -1});
            beanDao.newBean = beanDao.curBean;
            beanDao.beans.unshift(beanDao.curBean);
            beanDao.editType = 'base';
        };
        //重置当前编辑过的bean
        beanDao.resetBeanBase = function (bean) {
            bean = bean || beanDao.curBean;
            if (bean._isNew) {
                _removeBean(bean);
            } else {
                angular.extend(beanDao.curBean, beanDao.bckBeans[bean.id]);
                beanDao.curBean._noSaved = false;
            }
        };
        //保存当前的bean以及编辑好的属性
        beanDao.saveBean = function (bean) {
            bean = bean || beanDao.curBean;
            $http.post(mvcRoot + beanDao.beanSaveUri + (bean.id || -1) + ".json", bean).success(function (response) {
                bean._noSaved = !response.status;
                if (bean._noSaved) {
                    alert(response.msg);
                } else {
                    bean.id = response.body.id;
                    bean._isNew = undefined;
                    bean._noSaved = false;
                    (beanDao.newBean == bean) && (beanDao.newBean = undefined);//如果是新建对象保存了,则清空当前新建的对象,这样就允许继续新建了.
                }
            });
        };
        //从服务端删除bean
        beanDao.delBean = function (bean) {
            bean = bean || beanDao.curBean;
            if (bean._isNew) {
                _removeBean(bean);
            } else {
                $http.post(mvcRoot + beanDao.beanDelUri + (bean.id) + ".json", bean).success(function (response) {
                    if (!response.status) {
                        alert(response.msg);
                    } else {
                        _removeBean(bean);
                    }
                });
            }
        };
        //从服务器获得指定的list属性归属改bean的list集合
        beanDao.fetchSubList = function () {
            ctx.fetch(beanDao.propsUri, pagination, function (props, propsVo) {
                beanDao.props = props;
                beanDao.propsVo = propsVo;
            });
        };
        //从服务器获得指定list属性的所有对象
        beanDao.fetchSubListAll = function (name,page, rowCount) {
            ctx.fetch(beanDao.lists[name].listAllUri, page, function (datas, datasVo) {
                beanDao.lists[name].allDatas = datas;
                beanDao.lists[name].allDatasVo = datasVo;
            },{rowCount:rowCount});
        };

        //是否包含改list属性
        beanDao.containsSubListItem = function(subListName, subListBean){
            return !this.curBean?-1:_.findIndex(this.curBean[subListName], function (i) {
                return i.id == subListBean.id
            });
        };
        beanDao.toggleSubListItem = function(subListName, subListBean){
            var idx = this.containsSubListItem(subListName, subListBean);
            if (idx == -1) { //勾上了而且当前角色没有,则添加进去
                (this.curBean[subListName] || (this.curBean[subListName]=[])).push(subListBean);
                this.curBean._noSaved = true;
            } else if (idx > -1) {
                this.curBean[subListName].splice(idx, 1);
                this.curBean._noSaved = true;
            }
        };
        return beanDao;
    }
});