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
     * @function [api版本管理模块相关js] [api history module related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  HomeProjectDefaultApi_Service [注入HomeProjectDefaultApi_Service服务] [Injection HomeProjectDefaultApi_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.api.history', {
                    url: '/history?groupID?childGroupID?grandSonGroupID?apiID',
                    template: '<home-project-inside-api-history power-object="$ctrl.powerObject"></home-project-inside-api-history>'
                });
        }])
        .component('homeProjectInsideApiHistory', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/api/history/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: indexController
        })

    indexController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', 'HomeProjectDefaultApi_Service', '$filter', 'CODE'];

    function indexController($scope, $rootScope, ApiManagementResource, $state, HomeProjectDefaultApi_Service, $filter, CODE) {
        var vm = this;
        vm.data = {
            service: {
                default: HomeProjectDefaultApi_Service
            },
            info: {
                apiName: ''
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID:$state.params.grandSonGroupID,
                    apiID: $state.params.apiID
                },
                response: {
                    query: null
                }
            },
            fun: {
                init: null,
                deleteHistory: null, 
                toggleHistory: null, 
            }
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: vm.data.interaction.request.apiID
                }
            }
            $rootScope.global.ajax.HistoryList_Api = ApiManagementResource.Api.HistoryList(template.request);
            $rootScope.global.ajax.HistoryList_Api.$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.interaction.response.query = response.apiHistoryList;
                            break;
                        }
                }
                vm.data.info.apiName = response.apiName;
                $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012100163') + response.apiName, $filter('translate')('012100164'), $state.params.projectName, 'API开发管理'] });
            })
            return $rootScope.global.ajax.HistoryList_Api.$promise;
        }

        /**
         * @function [删除历史记录] [Delete history]
         */
        vm.data.fun.deleteHistory = function(arg) {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiHistoryID: arg.item.historyID,
                    apiID: arg.item.apiID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100166'), false, $filter('translate')('012100167'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Api.DeleteHistory(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.data.interaction.response.query.splice(arg.$index, 1);
                                        $rootScope.InfoModal($filter('translate')('012100168'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100169'), 'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [切换历史记录] [Switch history]
         */
        vm.data.fun.toggleHistory = function(arg) {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiHistoryID: arg.item.historyID,
                    apiID: arg.item.apiID
                }
            }
            if ($rootScope.global.ajax.toggleHistory_Api) $rootScope.global.ajax.toggleHistory_Api.$cancelRequest();
            $rootScope.global.ajax.toggleHistory_Api = ApiManagementResource.Api.toggleHistory(template.request);
            $rootScope.global.ajax.toggleHistory_Api.$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            for (var i = 0; i < vm.data.interaction.response.query.length; i++) {
                                if (vm.data.interaction.response.query[i].isNow == 1) {
                                    vm.data.interaction.response.query[i].isNow = 0;
                                    break;
                                }
                            }
                            arg.item.isNow = 1;
                            break;
                        }
                }
            })
        }
    }
})();
