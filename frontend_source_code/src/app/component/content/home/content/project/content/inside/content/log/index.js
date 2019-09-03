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
     * @function [项目动态模块相关js] [Project dynamics module related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.log', {
                    url: '/log',
                    template: '<home-project-inside-log></home-project-inside-log>'
                });
        }])
        .component('homeProjectInsideLog', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/log/index.html',
            controller: indexController
        })

    indexController.$inject = ['$scope','$rootScope', 'ApiManagementResource', '$state', '$filter', 'CODE'];

    function indexController($scope,$rootScope, ApiManagementResource, $state, $filter, CODE) {
        var vm = this;
        vm.data = {
            info: {
                pagination: {
                    maxSize: 5,
                    logCount: 0
                },
                filter: {
                    dynamics: $filter('translate')('012135'),
                    apiManagement: 'API开发管理',
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    page: 1,
                    pageSize: 15
                },
                response: {
                    query: null
                }
            },
            fun: {
                init: null, 
                pageChange:null
            }
        }

        /**
         * @function [辅助初始化功能函数] [Auxiliary initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    page: vm.data.interaction.request.page,
                    pageSize: vm.data.interaction.request.pageSize
                }
            }
            $scope.$emit('$WindowTitleSet', { list: [vm.data.info.filter.dynamics, $state.params.projectName, vm.data.info.filter.apiManagement] });
            template.promise = ApiManagementResource.Project.GetProjectLogList(template.request).$promise;
            template.promise.then(function(response) {
                vm.data.interaction.response.query=response.logList||[];
                vm.data.info.pagination.logCount = response.logCount||0;
            })
            return template.promise;
        }

        /**
         * @function [页面更改功能函数] [Page change function]
         */
        vm.data.fun.pageChange = function() {
            $scope.$broadcast('$LoadingInit');
        }
    }
})();
