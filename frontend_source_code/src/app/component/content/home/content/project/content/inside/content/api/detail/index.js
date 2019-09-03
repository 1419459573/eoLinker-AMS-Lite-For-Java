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
     * @function [api详情模块相关js] [api details module related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $sce [注入$sce服务] [Injection sce service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @service  HomeProject_Common_Service [注入HomeProject_Service服务] [Injection HomeProject_Common_Service service]
     * @service  GroupService [注入GroupService服务] [Injection GroupService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.api.detail', {
                    url: '/detail?groupID?childGroupID?grandSonGroupID?apiID',
                    template: '<home-project-inside-api-detail power-object="$ctrl.powerObject" ></home-project-inside-api-detail>',
                    resolve: helper.resolveFor('MARKDOWN_CSS')
                });
        }])
        .component('homeProjectInsideApiDetail', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/api/detail/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: homeProjectInsideApiDetailController
        })

    homeProjectInsideApiDetailController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', '$sce', '$filter', 'HomeProject_Common_Service', 'CODE', 'HomeProjectDefaultApi_Service'];

    function homeProjectInsideApiDetailController($scope, $rootScope, ApiManagementResource, $state, $sce, $filter, HomeProject_Common_Service, CODE, HomeProjectDefaultApi_Service) {
        var vm = this;
        vm.data = {
            service: {
                home: HomeProject_Common_Service,
                default: HomeProjectDefaultApi_Service
            },
            info: {
                template: {
                    envModel: null
                },
                mock: {
                    isFailure: false
                },
                spreed:{
                    header:true,
                    request:true,
                    response:true,
                    example:true,
                    note:true
                },
                filter: {
                    shrink: $filter('translate')('012100010'),
                    open: $filter('translate')('012100011'),
                    yes: $filter('translate')('012100033'),
                }
            },
            interaction: {
                request: {
                    apiID: $state.params.apiID,
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID:$state.params.grandSonGroupID
                }
            },
            fun: {
                init: null, 
                storage: null, 
                show: {
                    request: null, 
                    response: null, 
                }
            }
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                promise: null,
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    groupID: vm.data.interaction.request.grandSonGroupID ||vm.data.interaction.request.childGroupID || vm.data.interaction.request.groupID,
                    apiID: vm.data.interaction.request.apiID
                }
            }

            template.promise = ApiManagementResource.Api.Detail(template.request).$promise;
            template.promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.service.home.envObject.object.model = response.apiInfo;
                            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012100035') + vm.data.service.home.envObject.object.model.baseInfo.apiName, $filter('translate')('012100036'), $state.params.projectName, 'API开发管理'] });
                            switch (response.apiInfo.baseInfo.apiProtocol) {
                                case 0:
                                    vm.data.service.home.envObject.object.model.baseInfo.protocol = 'HTTP';
                                    break;
                                case 1:
                                    vm.data.service.home.envObject.object.model.baseInfo.protocol = 'HTTPS';
                                    break;
                            }
                            switch (response.apiInfo.baseInfo.apiStatus) {
                                case 0:
                                    vm.data.service.home.envObject.object.model.baseInfo.status = $filter('translate')('012100038');
                                    break;
                                case 1:
                                    vm.data.service.home.envObject.object.model.baseInfo.status = $filter('translate')('012100039');
                                    break;
                                case 2:
                                    vm.data.service.home.envObject.object.model.baseInfo.status = $filter('translate')('012100040');
                                    break;
                            }
                            vm.data.service.home.envObject.object.model.resultInfo = $filter('paramLevelFilter')(vm.data.service.home.envObject.object.model.resultInfo);
                            vm.data.service.home.envObject.object.model.requestInfo = $filter('paramLevelFilter')(vm.data.service.home.envObject.object.model.requestInfo);
                            vm.data.service.home.envObject.object.model.baseInfo.apiNoteHtml = $sce.trustAsHtml($filter('XssFilter')(vm.data.service.home.envObject.object.model.baseInfo.apiNote, {
                                onIgnoreTagAttr: function(tag, name, value, isWhiteAttr) {
                                    if (/(class)|(id)|(name)/.test(name)) {
                                        return name + '="' + value + '"';
                                    }
                                }
                            }));
                            vm.data.service.home.envObject.object.model.baseInfo.successMockCode = vm.data.service.home.envObject.object.model.baseInfo.successMockURL;
                            vm.data.service.home.envObject.object.model.baseInfo.failureMockCode = vm.data.service.home.envObject.object.model.baseInfo.failureMockURL;
                            vm.data.service.home.envObject.object.model.headers = response.apiInfo.headerInfo;
                            vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
                            $scope.$emit('$translateferStation', { state: '$EnvInitReady', data: { status: 1, param: angular.toJson(vm.data.service.home.envObject.object.model) } });
                            break;
                        }
                }
            })
            return template.promise;
        }

        /**
         * @function [显示请求参数个例详情]
         */
        vm.data.fun.show.request = function(arg) {
            if (!(arg.item.paramLimit || (arg.item.paramValueList && arg.item.paramValueList.length > 0) || arg.item.paramValue)) return;
            var template = {
                modal: {
                    item: arg.item
                }
            }
            $rootScope.RequestParamDetailModal(template.modal, function(callback) {

            })
        }

        /**
         * @function [显示返回结果个例详情] [Show return results]
         */
        vm.data.fun.show.response = function(arg) {
            if (!(arg.item.paramValueList || arg.item.paramValueList.length > 0)) return;
            var template = {
                modal: {
                    item: arg.item
                }
            }
            $rootScope.ResponseParamDetailModal(template.modal, function(callback) {

            })
        }

        /**
         * @function [星标功能函数] [Switch star]
         */
        vm.data.fun.storage = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: vm.data.interaction.request.apiID
                }
            }
            switch (vm.data.service.home.envObject.object.model.baseInfo.starred) {
                case 0:
                    {
                        ApiManagementResource.Star.Add(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.data.service.home.envObject.object.model.baseInfo.starred = 1;
                                        break;
                                    }
                            }
                        });
                        break;
                    }
                case 1:
                    {
                        ApiManagementResource.Star.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.data.service.home.envObject.object.model.baseInfo.starred = 0;
                                        break;
                                    }
                            }
                        });
                        break;
                    }
            }
        }
    }
})();
