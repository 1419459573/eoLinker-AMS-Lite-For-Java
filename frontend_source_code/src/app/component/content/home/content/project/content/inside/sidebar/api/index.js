(function() {
    /**
     * @name eolinker open source，eolinker开源版本
     * @link https://www.eolinker.com
     * @package eolinker
     * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018

     * eolinker，业内领先的Api接口管理及测试平台，为您提供最专业便捷的在线接口管理、测试、维护以及各类性能测试方案，帮助您高效开发、安全协作。
     * 如在使用的过程中有任何问题，可通过[图片]http://help.eolinker.com寻求帮助
     *
     * 注意！eolinker开源版本遵循GPL V3开源协议，仅供用户下载试用，禁止“一切公开使用于商业用途”或者“以eoLinker开源版本为基础而开发的二次版本”在互联网上流通。
     * 注意！一经发现，我们将立刻启用法律程序进行维权。
     * 再次感谢您的使用，希望我们能够共同维护国内的互联网开源文明和正常商业秩序。
     *
     * @function [api侧边栏相关js] [api sidebar related js]
     * @version  3.2.0
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  Group_AmsCommonService [注入Group_AmsCommonService服务] [Injection Group_AmsCommonService service]
     * @service  GroupService [注入GroupService服务] [Injection GroupService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .component('homeProjectInsideApiSidebar', {
            templateUrl: 'app/component/content/home/content/project/content/inside/sidebar/api/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: indexController
        })

        indexController.$inject = ['ERROR_WARNING', '$scope', 'ApiManagementResource', '$state', 'CODE', '$rootScope', 'GroupService', 'Group_AmsCommonService'];

        function indexController(ERROR_WARNING, $scope, ApiManagementResource, $state, CODE, $rootScope, GroupService, Group_AmsCommonService) {
            var vm = this;
            vm.data = {
                service: {
                    defaultCommon: Group_AmsCommonService
                },
                static: {
                    query: [{ groupID: -1, groupName: "所有接口", icon: 'sort' }, { groupID: -2, groupName: "接口回收站", icon: 'shanchu' }]
                },
                component: {
                    groupCommonObject: {}
                },
                info: {
                    sidebarShow: null,
                    sort: {
                        isDisable: false,
                        sortable: true,
                        originQuery: []
                    }
                },
                interaction: {
                    request: {
                        projectID: $state.params.projectID,
                        grandSonGroupID:$state.params.grandSonGroupID,
                        childGroupID:$state.params.childGroupID||-1,
                        groupID: $state.params.groupID || -1,
                        apiID: $state.params.apiID,
                        orderNumber: []
                    },
                    response: {
                        query: []
                    }
                },
                fun: {
                    init: null, //初始化功能函数
                    sort: {
                        copy: null, //复制相应原数组功能函数
                        confirm: null, //排序确认功能函数
                        cancle: null, //取消排序功能函数
                    }, //排序功能函数
                    click: {
                        parent: null, //父分组单击事件
                        child: null //子分组单击事件
                    },
                    edit: {
                        parent: null, //父分组编辑事件
                        child: null //子分组编辑事件
                    },
                    delete: {
                        parent: null, //父分组删除事件
                        child: null //子分组删除事件
                    }
                }
            }
    
            vm.data.fun.init = function() {
                var template = {
                    request: {
                        projectID: vm.data.interaction.request.projectID,
                        groupID: vm.data.interaction.request.groupID,
                        childGroupID: vm.data.interaction.request.childGroupID,
                        grandSonGroupID:vm.data.interaction.request.grandSonGroupID,
                        apiID: vm.data.interaction.request.apiID
                    },
                    query: [],
                    sort: {
                        array: []
                    }
                }
                vm.data.service.defaultCommon.fun.clear();
                if ($state.current.name.indexOf('edit') > -1) {
                    vm.data.info.sidebarShow = false;
                } else {
                    vm.data.info.sidebarShow = true;
                }
                ApiManagementResource.ApiGroup.Query(template.request).$promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                template.sort.array = vm.data.service.defaultCommon.sort.init(response);
                                vm.data.interaction.response.query = template.sort.array || [];
                                if ($state.current.name.indexOf('edit') > -1) {
                                    GroupService.set(template.sort.array, true);
                                } else {
                                    GroupService.set(template.sort.array);
                                }
                                break;
                            }
                    }
                })
            }
            vm.data.fun.init();
            vm.data.fun.sort.copy = function() {
                vm.data.info.sort.originQuery = [];
                angular.copy(vm.data.interaction.response.query, vm.data.info.sort.originQuery);
                if (vm.data.info.sort.originQuery.length > 0) {
                    vm.data.info.sort.isDisable = true;
                }
            }
            vm.data.fun.sort.cancle = function() {
                vm.data.info.sort.isDisable = false;
            }
            vm.data.fun.sort.confirm = function() {
                var template = {
                    input: {
                        baseRequest: {
                            projectID: vm.data.interaction.request.projectID,
                            orderNumber: {}
                        },
                        originQuery: vm.data.info.sort.originQuery,
                        resource: ApiManagementResource.ApiGroup.Sort,
                        callback: null
                    }
                }
                template.input.callback = function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                vm.data.interaction.response.query = vm.data.info.sort.originQuery;
                                vm.data.info.sort.isDisable = false;
                                break;
                            }
                    }
                }
                vm.data.service.defaultCommon.sort.operate('confirm', template.input);
            }
            vm.data.fun.click = function(status,arg) {
                var template={
                    uri:null
                }
                switch(status){
                    case 'first-level':{
                        template.uri={
                            groupID:arg.item.groupID || -1,
                            childGroupID:null,
                            grandSonGroupID:null
                        }
                        break;
                    }
                    case 'second-level':{
                        template.uri={
                            groupID:arg.parentItem.groupID,
                            childGroupID:arg.item.groupID,
                            grandSonGroupID:null
                        }
                        break;
                    }
                    case 'third-level':{
                        template.uri={
                            groupID:arg.grandParentItem.groupID,
                            childGroupID:arg.parentItem.groupID,
                            grandSonGroupID:arg.item.groupID
                        }
                        break;
                    }
                }
                angular.merge(vm.data.interaction.request,template.uri)
                arg.item.isSpreed = true;
                $state.go('home.project.inside.api.list', template.uri);
            }
            vm.data.fun.edit = function(status,arg) {
                arg=arg||{};
                var template = {
                    options:{
                        callback:vm.data.fun.init,
                        resource:ApiManagementResource.ApiGroup,
                        originGroupQuery:vm.data.interaction.response.query,
                        status:status,
                        baseRequest:{
                            projectID:vm.data.interaction.request.projectID
                        }
                    }
                }
                vm.data.service.defaultCommon.fun.operate('edit',arg,template.options);
            }
            vm.data.fun.delete = function(arg) {
                arg = arg || {};
                var template = {
                    modal: {
                        title: '删除分组',
                        message: '删除分组后，该分组下的api将全部移入接口回收站，该操作无法撤销，确认删除？'
                    }
                }
                $rootScope.EnsureModal(template.modal.title, false, template.modal.message, {}, function(callback) {
                    if (callback) {
                        ApiManagementResource.ApiGroup.Delete({  projectID: vm.data.interaction.request.projectID, groupID: arg.item.groupID }).$promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        if(arg.parentItem){
                                            arg.parentItem.childGroupList.splice(arg.$index, 1);
                                        }else{
                                            vm.data.interaction.response.query.splice(arg.$index, 1);
                                        }
                                        $rootScope.InfoModal('分组删除成功', 'success');
                                        switch((vm.data.interaction.request.groupID||0).toString()){
                                            case '-1':
                                            case '-2':{
                                                $state.reload('home.project.inside.api.list');
                                                break;
                                            }
                                            default:{
                                                if(arg.grandParentItem&&vm.data.interaction.request.grandSonGroupID == arg.item.groupID){
                                                    vm.data.fun.click('second-level',{parentItem:arg.grandParentItem, item: arg.parentItem});
                                                }else if(arg.parentItem&&vm.data.interaction.request.childGroupID == arg.item.groupID){
                                                    vm.data.fun.click('first-level',{item: arg.parentItem});
                                                }else if(vm.data.interaction.request.groupID == arg.item.groupID){
                                                    vm.data.fun.click('first-level',{item: vm.data.static.query[1]});
                                                }else if((arg.grandParentItem&&vm.data.interaction.request.childGroupID == arg.parentItem.groupID)||(arg.parentItem&&vm.data.interaction.request.groupID == arg.parentItem.groupID)){
                                                    $state.reload('home.project.inside.api.list');
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                            }
                        })
                    }
                });
            }
            vm.data.fun.export = function(arg) {
                var template = {
                    modal: {
                        status: 'group',
                        mark:'api',
                        title: '导出分组',
                        request: {
                            groupID: arg.item.groupID
                        },
                        resource:ApiManagementResource.ApiGroup
                    }
                }
                $rootScope.ExportModal(template.modal, function(callback) {});
            }
            vm.data.fun.import = function() {
                var template = {
                    modal: {
                        title: '导入分组',
                        status: 1,
                        request: {
                            projectID: vm.data.interaction.request.projectID
                        },
                        resource:ApiManagementResource.ApiGroup
                    }
                }
    
                $rootScope.ImportModal(template.modal, function(callback) {
                    if (callback) {
                        vm.data.fun.init();
                        switch (vm.data.interaction.request.groupID) {
                            case -1:
                            case '-1':
                                {
                                    $state.go('home.project.inside.api.list', { groupID: $state.params.groupID ? (null):-1  });
                                    break;
                                }
                        }
                    }
                });
            }
            $scope.$on('$stateChangeSuccess', function() { //路由更改函数
                vm.data.interaction.request.groupID = $state.params.groupID || -1;
                if ($state.current.name.indexOf('edit') > -1) {
                    vm.data.info.sidebarShow = false;
                } else {
                    vm.data.info.sidebarShow = true;
                }
            })
            vm.$onInit = function() {
                vm.data.component.groupCommonObject = {
                    sortObject: vm.data.info.sort,
                    funObject: {
                        showObject: vm.data.info.sort,
                        showVar: 'isDisable',
                        btnGroupList: {
                            edit: {
                                key: '新建分组',
                                class: 'eo-button-success',
                                icon: 'tianjia',
                                showable: false,
                                fun: vm.data.fun.edit,
                                params:'"add"'
                            },
                            export: {
                                key: '导入',
                                class: 'default-btn tab-first-btn',
                                icon: 'shangchuan',
                                showable: false,
                                fun: vm.data.fun.import
                            },
                            sortDefault: {
                                key: '排序',
                                class: 'default-btn tab-last-btn',
                                icon: 'paixu',
                                showable: false,
                                fun: vm.data.fun.sort.copy
                            },
                            sortConfirm: {
                                key: '确认',
                                class: 'default-btn tab-first-btn un-margin-left-btn',
                                icon: 'check',
                                showable: true,
                                fun: vm.data.fun.sort.confirm
                            },
                            sortCancel: {
                                key: '取消',
                                class: 'default-btn tab-last-btn',
                                icon: 'close',
                                showable: true,
                                fun: vm.data.fun.sort.cancle
                            }
                        }
                    },
                    mainObject: {
                        level: 2,
                        baseInfo: {
                            name: 'groupName',
                            id: 'groupID',
                            child: 'childGroupList',
                            secondLevelGroupID:'childGroupID',
                            thirdLevelGroupID:'grandSonGroupID',
                            current:vm.data.interaction.request
                        },
                        staticQuery: vm.data.static.query,
                        parentFun: {
                            addChild: {
                                fun: vm.data.fun.edit,
                                key: '添加子分组',
                                params: '"add-child",arg',
                                class: 'add-child-btn'
                            },
                            export: {
                                fun: vm.data.fun.export,
                                key: '导出分组'
                            },
                            edit: {
                                fun: vm.data.fun.edit,
                                key: '修改',
                                params: '"edit",arg'
                            },
                            delete: {
                                fun: vm.data.fun.delete,
                                key: '删除'
                            }
                        },
                        childFun: {
                            export: {
                                fun: vm.data.fun.export,
                                key: '导出分组'
                            },
                            edit: {
                                fun: vm.data.fun.edit,
                                key: '修改',
                                params: '"edit",arg',
                            },
                            delete: {
                                fun: vm.data.fun.delete,
                                key: '删除'
                            }
                        },
                        baseFun: {
                            click: vm.data.fun.click,
                            spreed: vm.data.service.defaultCommon.fun.spreed
                        }
                    }
                }
            }
        }

})();
