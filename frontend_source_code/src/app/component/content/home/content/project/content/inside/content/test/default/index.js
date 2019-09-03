(function () {
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
     * @function [自动化测试默认页面] [Automated testing of the default page]
     * @version  3.1.7
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  $window [注入window服务] [inject window service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject apiManagement API service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @service  GroupService [注入GroupService服务] [inject GroupService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .component('homeProjectInsideTestDefault', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/test/default/index.html',
            controller: indexController,
            bindings: {
                powerObject: '<'
            }
        })

    indexController.$inject = ['$scope', '$rootScope', '$state', 'ApiManagementResource', 'GroupService', 'CODE', '$filter', 'HomeProject_Common_Service'];

    function indexController($scope, $rootScope, $state, ApiManagementResource, GroupService, CODE, $filter, HomeProject_Common_Service) {
        var vm = this;
        vm.data = {
            service: {
                group: GroupService,
                home: HomeProject_Common_Service
            },
            info: {
                plugObject: {
                    needVersion: true
                },
                statusObject: {
                    testing: false
                },
                batch: {
                    address: [],
                    disable: false
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID: $state.params.grandSonGroupID,
                    caseID: [],
                    tips: $state.params.search
                },
                response: {
                    query: null
                }
            },
            fun: {
                batch: {
                    sort: null,
                    delete: null,
                },
                test: null,
                delete: null,
                init: null
            }
        }
        vm.component = {
            batchTestObject: {
                testing: false,
                flag: 0,
                type: 'all'
            },
            reportObject: {
                show: false,
                testing: false
            }
        };
        var data = {
            assistantFun: {
                getQuery: null
            }
        }
        /**
         * @function [测试功能] [Test function]
         */
        vm.data.fun.test = function () {
            vm.component.reportObject.testing = vm.component.batchTestObject.testing = !vm.component.batchTestObject.testing;
            if (vm.data.info.plugObject.useStatus) {
                if (vm.component.batchTestObject.testing) {
                    vm.component.reportObject.object = {};
                    vm.component.reportObject.show = true;
                }
            }
        }
        vm.data.fun.enter = function (arg) {
            var template = {
                $index: vm.data.interaction.request.caseID.indexOf(arg.item.caseID)
            }
            if (vm.data.info.batch.disable) {
                if (vm.data.info.batch.disable) {
                    arg.item.isClick = !arg.item.isClick;
                    if (arg.item.isClick) {
                        vm.data.interaction.request.caseID.push(arg.item.caseID);
                        vm.data.info.batch.address.push(arg.$index);
                    } else {
                        vm.data.interaction.request.caseID.splice(template.$index, 1);
                        vm.data.info.batch.address.splice(template.$index, 1);
                    }
                }
            } else {
                $state.go('home.project.inside.test.api', {
                    caseID: arg.item.caseID,
                    grandSonGroupID: vm.data.interaction.request.grandSonGroupID,
                    groupID: vm.data.interaction.request.groupID,
                    childGroupID: vm.data.interaction.request.childGroupID
                });
            }
        }
        /**
         * @function [存储位置排序] [Sort storage location]
         */
        vm.data.fun.batch.sort = function (pre, next) {
            return pre - next;
        }
        vm.data.fun.batch.default = function () {
            if (vm.data.interaction.response.query && vm.data.interaction.response.query.length > 0) {
                vm.data.info.batch.disable = true;
                angular.forEach(vm.data.info.batch.address, function (val, key) {
                    vm.data.interaction.response.query[val].isClick = false;
                })
                vm.data.interaction.request.caseID = [];
                vm.data.info.batch.address = [];
                $rootScope.InfoModal($filter('translate')('01216110'), 'success');
            } else {
                $rootScope.InfoModal($filter('translate')('01216111'), 'error');
            }
        }
        /**
         * @function [删除测试用例] [delete]
         */
        vm.data.fun.delete = function (status, arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    caseID: (status == 'batch') ? JSON.stringify(vm.data.interaction.request.caseID) : '[' + arg.item.caseID + ']'
                },
                loop: {
                    num: 0
                }
            }
            $rootScope.EnsureModal($filter('translate')('01216112'), false, $filter('translate')('01216113'), {}, function (callback) {
                if (callback) {
                    ApiManagementResource.AutomatedTestCase.Delete(template.request).$promise
                        .then(function (response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.component.batchTestObject.flag--;
                                        switch (status) {
                                            case 'batch':
                                                {
                                                    angular.forEach(vm.data.info.batch.address.sort(vm.data.fun.batch.sort), function (val, key) {
                                                        val = val - template.loop.num++;
                                                        vm.data.interaction.response.query.splice(val, 1);
                                                    })
                                                    vm.data.info.batch.disable = false;
                                                    vm.data.interaction.request.caseID = [];
                                                    vm.data.info.batch.address = [];
                                                    break;
                                                }
                                            case 'singal':
                                                {
                                                    vm.data.interaction.response.query.splice(arg.$index, 1);
                                                    break;
                                                }
                                        }
                                        $rootScope.InfoModal($filter('translate')('01216114'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216115'), 'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [编辑用例接口] [Edit the use case interface]
         * @param  {string} status [状态] [status]
         * @param  {object} arg    参数{item:单项列表项 Single item list}
         */
        vm.data.fun.edit = function (status, arg) {
            arg = arg || {
                item: {}
            };
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    caseID: arg.item ? arg.item.caseID : null
                },
                modal: {
                    caseInfo: arg.item,
                    group: {
                        groupID: parseInt((arg.item.topParentGroupID || arg.item.parentGroupID || (arg.item.groupID || vm.data.interaction.request.groupID)) || -1),
                        childGroupID: parseInt((arg.item.parentGroupID && arg.item.topParentGroupID ? arg.item.parentGroupID : arg.item.parentGroupID ? arg.item.groupID : vm.data.interaction.request.childGroupID) || -1),
                        grandSonGroupID: parseInt((arg.item.topParentGroupID ? arg.item.groupID : vm.data.interaction.request.grandSonGroupID) || -1),
                        parent: vm.data.service.group.get()
                    },
                    status: status
                }
            }
            if ((!template.modal.group.parent) || (template.modal.group.parent == 0)) {
                $rootScope.InfoModal($filter('translate')('01216116'), 'error');
                return;
            }
            $rootScope.ApiManagement_AutomatedTest_EditCaseModal(template.modal, function (callback) {
                if (callback) {
                    angular.merge(template.request, callback);
                    switch (status) {
                        case 'add':
                            {
                                ApiManagementResource.AutomatedTestCase.Add(template.request)
                                .$promise.then(function (response) {
                                    switch (response.statusCode) {
                                        case CODE.COMMON.SUCCESS:
                                            {
                                                vm.component.batchTestObject.flag--;
                                                $rootScope.InfoModal($filter('translate')('01216117'), 'success');
                                                $scope.$broadcast('$LoadingInit');
                                                break;
                                            }
                                        default:
                                            {
                                                $rootScope.InfoModal($filter('translate')('01216118'), 'error');
                                                break;
                                            }
                                    }
                                })
                                break;
                            }
                        case 'edit':
                            {
                                ApiManagementResource.AutomatedTestCase.Edit(template.request)
                                .$promise.then(function (response) {
                                    switch (response.statusCode) {
                                        case CODE.COMMON.SUCCESS:
                                            {
                                                vm.component.batchTestObject.flag--;
                                                $rootScope.InfoModal($filter('translate')('01216119'), 'success');
                                                $scope.$broadcast('$LoadingInit');
                                                break;
                                            }
                                        default:
                                            {
                                                $rootScope.InfoModal($filter('translate')('01216120'), 'error');
                                                break;
                                            }
                                    }
                                })
                                break;
                            }
                    }
                }
            })
        }
        vm.data.fun.search = function () {
            var template = {
                uri: {
                    search: vm.data.interaction.request.tips
                }
            }
            $state.go('home.project.inside.test.default', template.uri);
        }
        data.assistantFun.getQuery = function () {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    tips: vm.data.interaction.request.tips,
                    groupID: vm.data.interaction.request.grandSonGroupID > 0 ? vm.data.interaction.request.grandSonGroupID : vm.data.interaction.request.childGroupID > 0 ? vm.data.interaction.request.childGroupID : vm.data.interaction.request.groupID
                }
            }
            template.request.groupID = template.request.groupID > 0 ? template.request.groupID : null;
            if (template.request.tips) {
                $rootScope.global.ajax.Query_AutomatedTestCase = ApiManagementResource.AutomatedTestCase.Search(template.request);
                $rootScope.global.ajax.Query_AutomatedTestCase.$promise.then(function (response) {
                    vm.data.interaction.response.query = response.caseList || [];
                    $scope.$emit('$translateferStation', {
                        state: '$EnvInitReady'
                    });
                })
            } else {
                $rootScope.global.ajax.Query_AutomatedTestCase = ApiManagementResource.AutomatedTestCase.Query(template.request);
                $rootScope.global.ajax.Query_AutomatedTestCase.$promise.then(function (response) {
                    vm.data.interaction.response.query = response.caseList || [];
                    $scope.$emit('$translateferStation', {
                        state: '$EnvInitReady'
                    });
                })
            }
            return $rootScope.global.ajax.Query_AutomatedTestCase.$promise;
        }

        /**
         * 初始化功能函数
         * @param  {object} arg 传参状态{status:请求加载状态}
         */
        vm.data.fun.init = function (arg) {
            arg = arg || {};
            switch (arg.status) {
                default: {
                    return data.assistantFun.getQuery();
                    break;
                }
            }
        }
    }
})();