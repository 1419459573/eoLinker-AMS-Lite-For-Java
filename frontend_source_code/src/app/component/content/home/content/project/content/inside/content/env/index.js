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
     * @function [环境模块相关js] [Environment module related js]
     * @version  3.0.2
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  HomeProject_Common_Service [注入HomeProject_Service服务] [Injection HomeProject_Common_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     * @constant HTTP_CONSTANT [注入HTTP相关常量集] [inject HTTP related constant service]
     */
    'use strict';
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.env', {
                    url: '/env?envID',
                    template: '<home-project-inside-env power-object="$ctrl.data.info.powerObject"></home-project-inside-env>'
                });
        }])
        .component('homeProjectInsideEnv', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/env/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: homeProjectInsideEnv
        })

    homeProjectInsideEnv.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', 'HomeProject_Common_Service', '$filter', 'CODE', 'HTTP_CONSTANT'];

    function homeProjectInsideEnv($scope, $rootScope, ApiManagementResource, $state, HomeProject_Common_Service, $filter, CODE, HTTP_CONSTANT) {

        var vm = this;
        vm.data = {
            service: {
                container: HomeProject_Common_Service
            },
            constant: {
                headerArray: HTTP_CONSTANT.REQUEST_HEADER
            },
            info: {
                pre: {},
                current: {
                    envName: '',
                    frontURIList: [{
                        uri: ''
                    }],
                    headerList: [{
                        headerName: '',
                        headerValue: ''
                    }],
                    paramList: [{
                        paramKey: '',
                        paramValue: ''
                    }],
                    additionalParamList: [{
                        paramKey: '',
                        paramValue: ''
                    }]
                },
                reset: {
                    envID: -1,
                    envName: '',
                    frontURIList: [{
                        uri: ''
                    }],
                    headerList: [{
                        headerName: '',
                        headerValue: ''
                    }],
                    paramList: [{
                        paramKey: '',
                        paramValue: ''
                    }],
                    additionalParamList: [{
                        paramKey: '',
                        paramValue: ''
                    }]
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    envID: parseInt($state.params.envID),
                    uriID: -1,
                    frontURI: '',
                    headerName: '',
                    headerValue: '',
                    paramKey: '',
                    paramValue: ''
                },
                response: {
                    headerQuery: [],
                    paramQuery: []
                }
            },
            fun: {
                cancle: null,
                init: null,
                click: null,
                add: null,
                confirm: null,
                change: null,
                initQuery: null,
                delete: {
                    sidebar: null,
                    headerList: null,
                    paramList: null
                },
            }
        }

        /**
         * @function [菜单单击功能函数] [Menu click]
         */
        vm.data.fun.click = function(arg) {
            if ((arg.item.envID == vm.data.interaction.request.envID) && vm.data.info.current.envID == -1) {
                vm.data.info.current = vm.data.info.pre || arg.item;
            } else {
                $state.go('home.project.inside.env', {
                    envID: arg.item.envID
                })
            }

        }

        /**
         * @function [删除头部列表功能函数] [Delete the header list]
         */
        vm.data.fun.delete.headerList = function(arg) {
            vm.data.info.current.headerList.splice(arg.$index, 1);
        }

        vm.data.fun.delete.additionalParamList = function(arg) {
            vm.data.info.current.additionalParamList.splice(arg.$index, 1);
        }

        /**
         * @function [删除参数列表功能函数] [Delete the parameter list]
         */
        vm.data.fun.delete.paramList = function(arg) {
            vm.data.info.current.paramList.splice(arg.$index, 1);
        }

        /**
         * @function [删除侧边栏功能函数] [Delete the sidebar]
         */
        vm.data.fun.delete.sidebar = function(arg) {
            arg.$event.stopPropagation();
            var template = {
                modal: {
                    title: $filter('translate')('0121214'),
                    message: $filter('translate')('0121215')
                }
            }
            $rootScope.EnsureModal(template.modal.title, false, template.modal.message, {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Env.Delete({
                        projectID: vm.data.interaction.request.projectID,
                        envID: arg.item.envID
                    }).$promise.then(function(response) {
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    vm.data.interaction.response.query.splice(arg.$index, 1);
                                    $rootScope.InfoModal($filter('translate')('0121216'), 'success');
                                    if (vm.data.interaction.request.envID == arg.item.envID) {
                                        if (vm.data.interaction.response.query.length > 0) {
                                            vm.data.fun.click({
                                                item: vm.data.interaction.response.query[0]
                                            });
                                        } else {
                                            vm.data.interaction.request.envID = null;
                                            $state.go('home.project.inside.env', {
                                                'envID': null
                                            });
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
         * @function [编辑功能函数] [edit]
         */
        vm.data.fun.change = function(arg) {
            arg.item = arg.item || vm.data.info.current;
            var template = {
                reset: {},
                length: {
                    header: arg.item.headerList.length ? (arg.item.headerList.length - 1) : 0
                }
            }

            angular.copy(vm.data.info.reset, template.reset);
            if (arg.$last) {
                switch (arg.switch) {
                    case 0:
                        {
                            arg.item.headerList.push(template.reset.headerList[0]);
                            break;
                        }
                    case 1:
                        {
                            arg.item.paramList.push(template.reset.paramList[0]);
                            break;
                        }
                    case 2:
                        {
                            arg.item.additionalParamList.push(template.reset.additionalParamList[0]);
                            break;
                        }
                    default:
                        {
                            if (!vm.powerObject.edit) return;
                            if ((!arg.item.headerList[template.length.header]) || arg.item.headerList[template.length.header].headerName || arg.item.headerList[template.length.header].headerValue) {
                                arg.item.headerList.push(template.reset.headerList[0]);
                                arg.item.paramList.push(template.reset.paramList[0]);
                                arg.item.additionalParamList.push(template.reset.additionalParamList[0]);
                            }
                            break;
                        }
                }
            }

        }

        /**
         * @function [取消功能函数] [cancel]
         */
        vm.data.fun.cancle = function() {
            vm.data.interaction.response.query.splice(vm.data.interaction.response.query.length - 1, 1);
            vm.data.info.current = vm.data.info.pre;
        }

        /**
         * @function [添加菜单功能函数] [Add menu]
         */
        vm.data.fun.add = function() {
            if (vm.data.interaction.response.query[vm.data.interaction.response.query.length - 1] && vm.data.interaction.response.query[vm.data.interaction.response.query.length - 1].envID == -1) return;
            var template = {
                object: {}
            }
            vm.data.info.pre = vm.data.info.current;
            vm.data.info.current = {};
            angular.copy(vm.data.info.reset, vm.data.info.current);
            angular.copy(vm.data.info.reset, template.object);
            template.object.envName = $filter('translate')('0121217');
            vm.data.interaction.response.query.push(template.object);
        }

        /**
         * @function [初始化环境变量列表功能函数] [Initialize the list of environment variables]
         */
        vm.data.fun.initQuery = function(arg) {
            if (vm.data.interaction.request.envID == arg.item.envID) {
                arg.item.$index = arg.$index;
                angular.copy(arg.item, vm.data.info.current);
                vm.data.fun.change({
                    switch: -1,
                    $last: 1
                });
            }
        }

        /**
         * @function [确认功能函数] [Confirm]
         */
        vm.data.fun.confirm = function() {
            if ($scope.ConfirmForm.$invalid) 
                return;
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    envName: vm.data.info.current.envName,
                    frontURI: vm.data.info.current.frontURIList[0] ? vm.data.info.current.frontURIList[0].uri : '',
                    headers: {},
                    params: {},
                    additionalParams:{},
                    envID: vm.data.info.current.envID > -1 ? vm.data.info.current.envID : null,
                },
                object: {},
                promise: null
            }
            for (var key = 0; key < vm.data.info.current.headerList.length; key++) {
                var val = vm.data.info.current.headerList[key];
                if (val.headerName) {
                    template.request.headers[val.headerName] = val.headerValue;
                } else if (!val.headerName && val.headerValue) return;
            }
            for (var key = 0; key < vm.data.info.current.paramList.length; key++) {
                var val = vm.data.info.current.paramList[key];
                if (val.paramKey) {
                    template.request.params[val.paramKey] = val.paramValue;
                } else if (!val.paramKey && val.paramValue) return;
            }
            for (var key = 0; key < vm.data.info.current.additionalParamList.length; key++) {
                var val = vm.data.info.current.additionalParamList[key];
                if (val.paramKey) {
                    template.request.additionalParams[val.paramKey] = val.paramValue;
                } else if (!val.paramKey && val.paramValue) return;
            }
            template.request.headers = JSON.stringify(template.request.headers);
            template.request.params = JSON.stringify(template.request.params);
            template.request.additionalParams = JSON.stringify(template.request.additionalParams);
            if (template.request.envID) {
                template.promise = ApiManagementResource.Env.Edit(template.request).$promise;
                template.promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                vm.data.interaction.response.query[vm.data.info.current.$index] = {};
                                angular.copy(vm.data.info.current, vm.data.interaction.response.query[vm.data.info.current.$index]);
                                $rootScope.InfoModal($filter('translate')('0121218'), 'success');
                                break;
                            }
                        default:
                            {
                                $rootScope.InfoModal($filter('translate')('0121219'), 'error');
                                break;
                            }
                    }
                })
            } else {
                template.promise = ApiManagementResource.Env.Add(template.request).$promise;
                template.promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                $rootScope.InfoModal($filter('translate')('0121220'), 'success');
                                vm.data.info.current.envID = response.envID;
                                vm.data.interaction.response.query.splice(vm.data.interaction.response.query.length - 1, 1, vm.data.info.current);
                                vm.data.info.current = {};
                                angular.copy(vm.data.info.reset, vm.data.info.current);
                                angular.copy(vm.data.info.reset, template.object);
                                template.object.envName = $filter('translate')('0121217');
                                vm.data.interaction.response.query.push(template.object);
                                break;
                            }
                        default:
                            {
                                $rootScope.InfoModal($filter('translate')('0121221'), 'error');
                                break;
                            }
                    }
                })
            }
            return template.promise;
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = (function() {
            var template = {
                cache: vm.data.service.container.envObject.query,
                request: {
                    projectID: vm.data.interaction.request.projectID
                }
            }
            $scope.$emit('$WindowTitleSet', {
                list: [$filter('translate')('0121222'), $state.params.projectName, 'API开发管理']
            });
            if (template.cache) {
                vm.data.interaction.response.query = template.cache;
            } else {
                ApiManagementResource.Env.Query(template.request).$promise.then(function(response) {
                    vm.data.interaction.response.query = response.envList || [];
                }).finally(function() {
                    vm.data.service.container.envObject.query = vm.data.interaction.response.query;
                })
            }
        })()
        $scope.$on("$stateChangeStart", function(_default, arg) {
            if (!/home.project.inside.env/.test(arg.name)) {
                vm.data.service.container.envObject.fun.clear();
            }
        })
    }
})();