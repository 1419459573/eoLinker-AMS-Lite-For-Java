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
     * @function [mock模块相关js] [mock module related js]
     * @version  3.2.0
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootScope service]
     * @service  $sce [注入$sce服务] [Injection $sce service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  HomeProjectDefaultApi_Service [注入HomeProject_Service服务] [Injection HomeProjectDefaultApi_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.api.mock', {
                    url: '/mock?groupID?childGroupID?grandSonGroupID?apiID',
                    template: '<home-project-inside-api-mock power-object="$ctrl.powerObject"></home-project-inside-api-mock>'
                });
        }])
        .component('homeProjectInsideApiMock', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/api/mock/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: indexController
        })

    indexController.$inject = ['$scope', '$rootScope', '$sce', 'ApiManagementResource', '$state', 'HomeProjectDefaultApi_Service', '$filter', 'CODE'];

    function indexController($scope, $rootScope, $sce, ApiManagementResource, $state, HomeProjectDefaultApi_Service, $filter, CODE) {
        var vm = this;
        vm.data = {
            service: {
                default: HomeProjectDefaultApi_Service
            },
            info: {
                apiName: '',
                spreed: {
                    list: true,
                    review: true
                },
                filter: {
                    shrink: $filter('translate')('012100010'),
                    open: $filter('translate')('012100011'),
                }
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
                    mockInfo: null
                }
            },
            fun: {
                init: null, 
                filterMock: null, 
            }
        }

        /**
         * @function [过滤mock] [Filter mock]
         */
        vm.data.fun.filterMock = function(arg) {
            if (arg.paramKey == '') {
                return false;
            } else {
                return true;
            }
        }
        
        /**
         * @function [初始化功能函数] [Initialize the function]
         */
        vm.data.fun.init = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: vm.data.interaction.request.apiID
                }
            }
            $rootScope.global.ajax.Mock_Api = ApiManagementResource.Api.Mock(template.request);
            $rootScope.global.ajax.Mock_Api.$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.interaction.response.mockInfo = response;
                            vm.data.interaction.response.mockInfo.mockRule = $filter('paramLevelFilter')(vm.data.interaction.response.mockInfo.mockRule);
                            $scope.$emit('$WindowTitleSet', { list: ['[Mock]' + vm.data.interaction.response.mockInfo.apiName, $filter('translate')('012100164'), $state.params.projectName, 'API开发管理'] });
                            break;
                        }
                }
            })
            return $rootScope.global.ajax.Mock_Api.$promise;
        }
    }
})();
