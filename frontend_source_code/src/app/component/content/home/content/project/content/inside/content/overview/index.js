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
     * @function [项目概况模块相关js] [Project Overview module related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  ProjectService [注入HomeProject_Service服务] [Injection ProjectService service]
     * @service  HomeProject_Common_Service [注入HomeProject_Service服务] [Injection HomeProject_Common_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    'use strict';
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.overview', {
                    url: '/overview',
                    template: '<home-project-inside-overview></home-project-inside-overview>',
                    resolve: helper.resolveFor('IMG_CROP', 'QINIU_UPLOAD'),
                });
        }])
        .component('homeProjectInsideOverview', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/overview/index.html',
            controller: indexController
        })

    indexController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', 'ProjectService', 'HomeProject_Common_Service', '$filter', 'CODE'];

    function indexController($scope, $rootScope, ApiManagementResource, $state, ProjectService, HomeProject_Common_Service, $filter, CODE) {

        var vm = this;
        vm.data = {
            service: {
                common: HomeProject_Common_Service
            },
            info: {
                filter: {
                    noDesc: $filter('translate')('012140'),
                    overview: $filter('translate')('0121420'),
                    apiManagement: 'API开发管理',
                    edit: $filter('translate')('0121422'),
                    editSuccess: $filter('translate')('0121423'),
                    dump: $filter('translate')('0121424'),
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID
                },
                response: {
                    projectInfo: null
                }
            },
            fun: {
                init: null,
                menu: null,
                dump: null,
                edit: null,
                backups: null
            }
        }

        /**
         * @function [辅助初始化功能函数] [Auxiliary initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                cache: ProjectService.detail.get(),
                promise: null,
                request: {
                    projectID: vm.data.interaction.request.projectID
                }
            }
            $scope.$emit('$WindowTitleSet', {
                list: [vm.data.info.filter.overview, $state.params.projectName, vm.data.info.filter.apiManagement]
            });
            vm.data.interaction.response.projectInfo = template.cache;
            if (template.cache) {
                if (vm.data.interaction.response.projectInfo.reset) {
                    template.promise = ApiManagementResource.Project.Detail(template.request).$promise;
                    template.promise.then(function(response) {
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    vm.data.interaction.response.projectInfo = response;
                                    ProjectService.detail.set(response);
                                    break;
                                }
                        }
                    })
                }
            }
            return template.promise;
        }

        /**
         * @function [菜单功能函数] [Menu function]
         */
        vm.data.fun.menu = function(arg) {
            switch (arg.switch) {
                case 0:
                    {
                        $state.go('home.project.inside.api.list', {
                            groupID: -1
                        });
                        break;
                    }
                case 1:
                    {
                        $state.go('home.project.inside.code.list', {
                            groupID: -1
                        });
                        break;
                    }
                case 3:
                    {
                        $state.go('home.project.inside.test.default');
                        break;
                    }
            }
        }

        /**
         * @function [编辑功能函数] [Edit function]
         */
        vm.data.fun.edit = function() {
            vm.data.interaction.response.projectInfo.projectID = vm.data.interaction.request.projectID;
            var template = {
                modal: {
                    title: vm.data.info.filter.edit,
                    isAdd: false,
                    item: vm.data.interaction.response.projectInfo
                },
                request: {}
            }

            $rootScope.ProjectModal(template.modal, function(callback) {
                if (callback) {
                    vm.data.interaction.response.projectInfo.projectName = callback.projectName;
                    vm.data.interaction.response.projectInfo.projectDesc = callback.projectDesc;
                    vm.data.interaction.response.projectInfo.projectType = callback.projectType;
                    vm.data.interaction.response.projectInfo.projectVersion = callback.projectVersion;
                    $rootScope.InfoModal(vm.data.info.filter.editSuccess, 'success');
                }
            });
        }

        /**
         * @function [导出功能函数] [Export function]
         */
        vm.data.fun.dump = function() {
                var template = {
                    modal: {
                        title: vm.data.info.filter.dump,
                        projectID: vm.data.interaction.request.projectID
                    }
                }
                $rootScope.ExportModal(template.modal, function(callback) {});
            }
            /**
             * @function [备份项目函数] [Export function]
             */
        vm.data.fun.backups = function() {
            var template = {
                modal: {
                    title: vm.data.info.filter.dump,
                    projectID: vm.data.interaction.request.projectID
                }
            }
            $rootScope.ApiManagement_BackupsModal(template.modal, function(callback) {});
        }
        $scope.$on('$initProjectInfo', function(data, attr) {
            vm.data.fun.init();
        });
    }
})();