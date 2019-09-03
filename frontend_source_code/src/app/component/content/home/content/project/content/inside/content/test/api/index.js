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
     * @function [自动化测试单例页面] [Automated test singleton page]
     * @version  3.1.7
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  $window [注入window服务] [inject window service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject apiManagement API service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @service  Cache_CommonService [注入Cache_CommonService服务] [inject Cache_CommonService service]
     * @service  HomeProject_Common_Service [注入HomeProject_Common_Service服务] [inject HomeProject_Common_Service service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .component('homeProjectInsideTestApi', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/test/api/index.html',
            controller: indexController,
            bindings: {
                powerObject: '<'
            }
        })

    indexController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', 'CODE', '$state', 'Cache_CommonService', 'HomeProject_Common_Service', '$filter'];

    function indexController($scope, $rootScope, ApiManagementResource, CODE, $state, Cache_CommonService, HomeProject_Common_Service, $filter) {
        var vm = this;
        vm.data = {
            service: {
                home: HomeProject_Common_Service,
                cache: Cache_CommonService
            },
            info: {
                plugObject: {
                    needVersion: true
                },
                statusObject: {
                    testing: false
                },
                filter: {
                    stop: $filter('translate')('0121603'),
                    testAll: $filter('translate')('0121604'),
                    noResult: $filter('translate')('01216017'),
                    noReg: $filter('translate')('01216018'),
                    test: $filter('translate')('01216020')
                }
            },
            interaction: {
                batchTestObject: {
                    output: [],
                    statusObject: {
                        testing: false
                    },
                    flag: 0
                },
                reportObject: {
                    object: {},
                    show: false
                },
                request: {
                    projectID: $state.params.projectID,
                    caseID: $state.params.caseID,
                    groupID: $state.params.groupID,
                    grandSonGroupID:$state.params.grandSonGroupID,
                    childGroupID: $state.params.childGroupID
                },
                response: {
                    query: null
                }
            },
            fun: {
                edit: null
            }
        }
        var data = {
            assistantFun: {
                getQuery: null
            }
        }
        vm.data.fun.bind = function(status,arg) {
            var template = {
                modal: {
                    query: vm.data.interaction.response.singalQuery,
                    resource: ApiManagementResource,
                    request: {
                        projectID: vm.data.interaction.request.projectID,
                        groupID: -1
                    }
                },
                uri: {
                    status: status || 'add',
                    groupID: vm.data.interaction.request.groupID,
                    grandSonGroupID:vm.data.interaction.request.grandSonGroupID,
                    childGroupID: vm.data.interaction.request.childGroupID,
                    caseID: vm.data.interaction.request.caseID,
                    connID: arg ? arg.item.connID : null
                }
            }
            $rootScope.ApiManagement_AutomatedTest_QiuckAddSingalModal(template.modal, function (callback) {
                if (callback) {
                    vm.data.service.cache.set(callback, 'apiInfo');
                    switch (status) {
                        case 'insert':
                            {
                                template.uri.orderNumber = arg.$index;
                                break;
                            }
                    }
                    $state.go('home.project.inside.test.edit', template.uri);
                }
            })
        }
        vm.data.fun.detail = function(status,arg) {
            var template = {
                object: vm.data.interaction.batchTestObject.output[arg.$index]
            }
            vm.data.interaction.reportObject.object = {
                status:status,
                general: template.object.general,
                requestHeaders: JSON.stringify(template.object.requestHeaders) == '{}' ? null : template.object.requestHeaders,
                requestBody: {
                    requestType: template.object.requestBody.requestType,
                    body: JSON.stringify(template.object.requestBody.body) == '{}' ? null : template.object.requestBody.body
                },
                response: template.object.response,
                baseInfo: {
                    caseName: arg.item.apiName,
                    matchType: arg.item.matchType,
                    matchTypeMessage: arg.item.matchType == 0 ? $filter('translate')('01216024') : arg.item.matchType == 1 ? $filter('translate')('01216025') : arg.item.matchType == 2 ?$filter('translate')('01216026'):$filter('translate')('01216027'),
                    statusCode: arg.item.statusCode,
                    matchRule: arg.item.matchRule
                }
            }
            vm.data.interaction.reportObject.show = true;
        }
        vm.data.fun.test = function(status, arg) {
            switch (status) {
                case 'all':
                    {
                        if (vm.data.interaction.batchTestObject.statusObject.type == 'singal') { vm.data.interaction.batchTestObject.statusObject.testing = false; }
                        vm.data.info.statusObject.testing = !vm.data.info.statusObject.testing;
                        if (vm.data.info.statusObject.testing) {
                            vm.data.interaction.batchTestObject.output = [];
                        }
                        vm.data.interaction.batchTestObject.statusObject = vm.data.info.statusObject;
                        break;
                    }
                case 'singal':
                    {
                        arg.item.$index = arg.$index;
                        arg.item.testing = !arg.item.testing;
                        if (vm.data.interaction.batchTestObject.statusObject.type != 'singal') {
                            vm.data.interaction.batchTestObject.statusObject = {
                                caseList: []
                            };
                        }
                        if (arg.item.testing) {
                            arg.item.$count = vm.data.interaction.batchTestObject.statusObject.caseList.length;
                            vm.data.interaction.batchTestObject.statusObject.caseList.push(arg.item);
                        } else {
                            vm.data.interaction.batchTestObject.statusObject.caseList.splice(arg.item.$count, 1);
                        }
                        try {
                            if (arg.item.testing) {
                                vm.data.interaction.batchTestObject.output[arg.$index] = null;
                            }
                        } catch (e) {}
                        if (vm.data.interaction.batchTestObject.statusObject.caseList.length > 0) {
                            vm.data.interaction.batchTestObject.statusObject.testing = true;
                        } else {
                            vm.data.interaction.batchTestObject.statusObject.testing = false;
                        }
                        break;
                    }
            }
            vm.data.interaction.batchTestObject.statusObject.type = status;
        }

        /**
         * @function [编辑用例接口] [Edit the use case interface]
         * @param  {string} status [状态] [status]
         * @param  {object} arg    参数{item:单项列表项 Single item list}
         */
        vm.data.fun.edit = function(status, arg) {
            arg = arg || {
                item: {}
            };
            var template = {
                uri: {
                    groupID: vm.data.interaction.request.groupID,
                    childGroupID: vm.data.interaction.request.childGroupID,
                    caseID: vm.data.interaction.request.caseID,
                    grandSonGroupID:vm.data.interaction.request.grandSonGroupID,
                    connID: arg.item.connID,
                    status: status
                }
            }
            vm.data.service.cache.clear('apiInfo');
            switch (status) {
                case 'insert':
                    {
                        template.uri.orderNumber = arg.$index;
                        break;
                    }
            }
            $state.go('home.project.inside.test.edit', template.uri);
        }

        vm.data.fun.delete = function(arg) {
            var template = {
                modal: {
                    title: $filter('translate')('01216028'),
                    message: $filter('translate')('01216029')
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    caseID: arg.item.caseID,
                    connID: '[' + arg.item.connID + ']'
                }
            }
            $rootScope.EnsureModal(template.modal.title, false, template.modal.message, {}, function(callback) {
                if (callback) {
                    ApiManagementResource.AutomatedTestCaseSingle.Delete(template.request).$promise.then(function(response) {
                        vm.data.interaction.batchTestObject.flag++;
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    $rootScope.InfoModal($filter('translate')('01216030'), 'success');
                                    vm.data.interaction.response.query.splice(arg.$index, 1);
                                    try {
                                        vm.data.interaction.batchTestObject.output.splice(arg.$index, 1);
                                    } catch (e) {}
                                    break;
                                }
                        }
                    })
                }
            });
        }

        data.assistantFun.getQuery = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    caseID: vm.data.interaction.request.caseID
                }
            }
            $rootScope.global.ajax.Query_AutomatedTestCaseSingle = ApiManagementResource.AutomatedTestCaseSingle.Query(template.request);
            $rootScope.global.ajax.Query_AutomatedTestCaseSingle.$promise.then(function(response) {
                vm.data.interaction.response.query = response.singCaseList || [];
                vm.data.service.cache.set(vm.data.interaction.response.query, 'singCaseList');
                $scope.$emit('$translateferStation', { state: '$EnvInitReady', data: { status: 0 } });
            })
            return $rootScope.global.ajax.Query_AutomatedTestCaseSingle.$promise;
        }

        /**
         * @function [初始化功能函数] [Initialize the function]
         * @param  {object} arg 传参状态{status:请求加载状态 Request loading status}
         * 
         */
        vm.data.fun.init = function(arg) {
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
