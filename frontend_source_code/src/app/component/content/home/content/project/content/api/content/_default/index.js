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
     * @function [项目内页列表（list）模块相关js]
     * @version  3.0.2
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务][inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @service  NavbarService [注入NavbarService服务] [Injection NavbarService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.api.default', {
                    url: '/',
                    template: '<home-project-api-default></home-project-api-default>'
                });
        }])
        .component('homeProjectApiDefault', {
            templateUrl: 'app/component/content/home/content/project/content/api/content/_default/index.html',
            controller: indexController
        })

    indexController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', '$filter', 'NavbarService', 'CODE'];

    function indexController($scope, $rootScope, ApiManagementResource, $state, $filter, NavbarService, CODE) {
        var vm = this;
        vm.data = {
            service: {
                navbar: NavbarService,
            },
            storage: {},
            interaction: {
                request: {
                    projectType: -1
                },
                response: {
                    query: null
                }
            },
            fun: {
                import: null,
                dump: null,
                edit: null,
                delete: null,
                enter: null,
                init: null
            }
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                promise: null,
                request: {
                    projectType: vm.data.interaction.request.projectType
                }
            }
            vm.data.storage = JSON.parse(window.localStorage['ENV_DIRECTIVE_TABLE'] || '{}');
            $scope.$emit('$WindowTitleSet', {
                list: [$filter('translate')('012013')]
            });
            template.promise = ApiManagementResource.Project.Query(template.request).$promise;
            template.promise.then(function(response) {
                vm.data.interaction.response.query = response.projectList || [];
            })
            return template.promise;
        }

        /**
         * @function [导入项目] [Import the project]
         */
        vm.data.fun.import = function() {
            var template = {
                modal: {
                    title: $filter('translate')('01201')
                }
            }
            $rootScope.ImportModal(template.modal, function(callback) {
                if (callback) {
                    $scope.$broadcast('$LoadingInit');
                }
            });
        }

        /**
         * @function [编辑项目功能函数] [Edit the project]
         */
        vm.data.fun.edit = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                modal: {
                    title: arg.item ? $filter('translate')('012015') : $filter('translate')('01200'),
                    isAdd: !arg.item,
                    item: arg.item
                },
                request: {}
            }
            $rootScope.ProjectModal(template.modal, function(callback) {
                if (callback) {
                    template.request = {
                        projectDesc: callback.projectDesc,
                        projectID: callback.projectID,
                        projectName: callback.projectName,
                        projectType: callback.projectType,
                        projectUpdateTime: $filter('currentTimeFilter')(),
                        projectVersion: callback.projectVersion,
                        userType: callback.userType || 0
                    }
                    if (arg.item) {
                        vm.data.interaction.response.query.splice(arg.$index, 1);
                    }
                    vm.data.interaction.response.query.splice(0, 0, template.request);
                    $rootScope.InfoModal(template.modal.title + $filter('translate')('012016'), 'success');
                }
            });
        }

        /**
         * @function [删除功能函数] [delete]
         */
        vm.data.fun.delete = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: arg.item.projectID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012017'), true, $filter('translate')('012018'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Project.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.data.interaction.response.query.splice(arg.$index, 1);
                                        window.localStorage.setItem('ENV_DIRECTIVE_TABLE', JSON.stringify(vm.data.storage, function(key, val) {
                                            if (key === arg.item.projectID) {
                                                return undefined;
                                            }
                                            return val;
                                        }));
                                        $rootScope.InfoModal($filter('translate')('012019'), 'success');
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [进入项目功能函数] [Enter the project]
         */
        vm.data.fun.enter = function(arg) {
            var template = {
                uri: {
                    projectName: arg.item.projectName,
                    projectID: arg.item.projectID
                }
            }
            $state.go('home.project.inside.overview', template.uri);
        }
        vm.$onInit = function() {}
    }
})();