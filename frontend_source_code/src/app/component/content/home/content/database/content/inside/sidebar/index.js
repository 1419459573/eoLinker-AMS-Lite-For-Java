(function() {
    'use strict';
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
     * @function [数据库内页侧边栏模块相关js] [Database inside the page sidebar module related js]
     * @version  3.2.1
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @service  DatabaseResource [注入数据库接口服务] [inject Database API service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  GroupService [注入GroupService服务] [inject GroupService service]
     * @service  $filter [注入GroupService服务] [inject filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .component('databaseSidebar', {
            templateUrl: 'app/component/content/home/content/database/content/inside/sidebar/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: databaseSidebarController
        })

    databaseSidebarController.$inject = ['$scope', '$rootScope', 'DatabaseResource', '$state', 'GroupService', '$filter', 'CODE'];

    function databaseSidebarController($scope, $rootScope, DatabaseResource, $state, GroupService, $filter, CODE) {
        var vm = this;
        vm.data = {
            component: {
                groupCommonObject: {}
            },
            info: {
                sidebarShow: null
            },
            interaction: {
                request: {
                    databaseID: $state.params.databaseID,
                    tableID: $state.params.tableID
                },
                response: {
                    query: []
                }
            },
            fun: {
                init: null, 
                click: null, 
                edit: null, 
                delete: null, 
                more: null
            }
        }

        /**
         * @function [table单击事件] [table click event]
         */
        vm.data.fun.click = function(status,arg) {
            vm.data.interaction.request.tableID = arg.item.tableID;
            $state.go('home.database.inside.table.list', { tableID: arg.item.tableID });
        }

        /**
         * @function [更多功能函数] [More functional functions]
         */
        vm.data.fun.more = function(arg) {
            arg.$event.stopPropagation();
            arg.item.listIsClick = true;
        }

        /**
         * @function [table编辑事件] [edit the event]
         */
        vm.data.fun.edit = function(arg) {
            arg = arg || {};
            var template = {
                modal: {
                    title: arg.item ? $filter('translate')('010129') : $filter('translate')('010120')
                },
                $index: null
            }
            $rootScope.TableModal(template.modal.title, arg.item, vm.data.interaction.request.databaseID, function(callback) {
                if (callback) {
                    if (arg.item) {
                        DatabaseResource.DatabaseTable.Edit(callback).$promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal(template.modal.title + $filter('translate')('0101210'), 'success');
                                        arg.item.tableName = callback.tableName;
                                        arg.item.tableDescription = callback.tableDescription;
                                        GroupService.set(vm.data.interaction.response.query);
                                        break;
                                    }
                            }
                        });
                    } else {
                        DatabaseResource.DatabaseTable.Add(callback).$promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal(template.modal.title + $filter('translate')('0101210'), 'success');
                                        if (vm.data.interaction.response.query == 0) {
                                            var newItem = { tableID: parseInt(response.tableID), tableName: callback.tableName, tableDescription: callback.tableDescription, isClick: true };
                                            vm.data.interaction.response.query.push(newItem);
                                            vm.data.fun.click('click',{ item: newItem });
                                        } else {
                                            vm.data.interaction.response.query.push({ tableID: parseInt(response.tableID), tableName: callback.tableName, tableDescription: callback.tableDescription });
                                        }
                                        GroupService.set(vm.data.interaction.response.query);
                                        break;
                                    }
                            }
                        });
                    }
                }
            });
        }

        /**
         * @function [table删除事件] [delete event]
         */
        vm.data.fun.delete = function(arg) {
            arg = arg || {};
            var template = {
                modal: {
                    title: $filter('translate')('0101211'),
                    message: $filter('translate')('0101212')
                }
            }
            $rootScope.EnsureModal(template.modal.title, false, template.modal.message, {}, function(callback) {
                if (callback) {
                    DatabaseResource.DatabaseTable.Delete({ dbID: vm.data.interaction.request.databaseID, tableID: arg.item.tableID }).$promise.then(function(response) {
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    vm.data.interaction.response.query.splice(arg.$index, 1);
                                    $rootScope.InfoModal($filter('translate')('0101213'), 'success');
                                    if (vm.data.interaction.request.tableID == arg.item.tableID) {
                                        if (vm.data.interaction.response.query.length > 0) {
                                            vm.data.fun.click('click',{ item: vm.data.interaction.response.query[0] });
                                        } else {
                                            $state.go('home.database.inside.table.list', { tableID: null });
                                        }
                                    }
                                    break;
                                }
                        }
                    })
                }
            });
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = (function() {
            var template = {
                request: {
                    dbID: vm.data.interaction.request.databaseID
                }
            }
            vm.data.info.sidebarShow = true;
            DatabaseResource.DatabaseTable.Query(template.request).$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.interaction.response.query = response.tableList;
                            GroupService.set(vm.data.interaction.response.query);
                            if (!vm.data.interaction.request.tableID) {
                                vm.data.interaction.request.tableID = vm.data.interaction.response.query[0].tableID;
                                $scope.$emit('$translateferStation', { state: '$LoadingInit', data: { tableID: vm.data.interaction.request.tableID } });
                            }
                        }
                }
            })
        })()
        vm.$onInit = function() {
            vm.data.component.groupCommonObject = {
                funObject: {
                    unTop:true,
                    btnGroupList: {
                        edit: {
                            key: $filter('translate')('010120'),
                            class: 'eo-button-success',
                            icon: 'tianjia',
                            fun: vm.data.fun.edit
                        }
                    }
                },
                mainObject: {
                    baseInfo: {
                        title: $filter('translate')('010123'),
                        name: 'tableName',
                        id: 'tableID',
                        current: vm.data.interaction.request
                    },
                    parentFun: {
                        edit: {
                            fun: vm.data.fun.edit,
                            key: $filter('translate')('010124'),
                            params: { item: null }
                        },
                        delete: {
                            fun: vm.data.fun.delete,
                            key: $filter('translate')('010125'),
                            params: { item: null, $index: null }
                        }
                    },
                    baseFun: {
                        click: vm.data.fun.click,
                    }
                }
            }
        }
    }

})();
