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
     * @function [项目文档详情相关js] [Project Document Details Related js]
     * @version  3.1.6
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $sce [注入$sce服务] [Injection $sce service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.doc.detail', {
                    url: '/detail?groupID?childGroupID?grandSonGroupID?documentID',
                    template: '<home-project-inside-doc-detail power-object="$ctrl.powerObject"></home-project-inside-doc-detail>',
                    resolve: helper.resolveFor('MARKDOWN_CSS')
                });
        }])
        .component('homeProjectInsideDocDetail', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/doc/detail/index.html',
            bindings: {
                powerObject: '<'
            },
            controller: indexController
        })

    indexController.$inject = ['$scope', '$sce', 'ApiManagementResource', '$state', 'CODE', '$rootScope', '$filter'];

    function indexController($scope, $sce, ApiManagementResource, $state, CODE, $rootScope, $filter) {
        var vm = this;
        vm.data = {
            interaction: {
                request: {
                    documentID: $state.params.documentID,
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID: $state.params.grandSonGroupID
                },
                response: {
                    documentInfo: {}
                }
            },
            fun: {
                init: null, 
                delete: null 
            }
        }
        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    groupID: vm.data.interaction.request.grandSonGroupID || vm.data.interaction.request.childGroupID || vm.data.interaction.request.groupID,
                    documentID: vm.data.interaction.request.documentID
                }
            }
            $rootScope.global.ajax.Detail_Doc = ApiManagementResource.Doc.Detail(template.request);
            $rootScope.global.ajax.Detail_Doc.$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012100075') + response.documentInfo.title, $filter('translate')('012100076'), $state.params.projectName, 'API开发管理'] });
                            response.documentInfo.docNoteHtml = $sce.trustAsHtml($filter('XssFilter')(response.documentInfo.content, {
                                onIgnoreTagAttr: function(tag, name, value, isWhiteAttr) {
                                    if (/(class)|(id)|(name)/.test(name)) {
                                        return name + '="' + value + '"';
                                    }
                                }
                            }));
                            vm.data.interaction.response.documentInfo = response.documentInfo;
                            break;
                        }
                }
            })
            return $rootScope.global.ajax.Detail_Doc.$promise;
        }
        /**
         * @function [删除功能函数] [delete]
         */
        vm.data.fun.delete = function(arg) {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    documentID: '['+vm.data.interaction.request.documentID+']'
                },
                uri:{ 
                    groupID: vm.data.interaction.request.groupID, 
                    childGroupID:vm.data.interaction.request.childGroupID,
                    grandSonGroupID: vm.data.interaction.request.grandSonGroupID }
            }
            $rootScope.EnsureModal($filter('translate')('012100078'), false, $filter('translate')('012100079'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Doc.Delete(template.request).$promise
                        .then(function(response) {
                            switch(response.statusCode){
                                case CODE.COMMON.SUCCESS:{
                                    $state.go('home.project.inside.doc.list',template.uri);
                                    $rootScope.InfoModal($filter('translate')('012100080'), 'success');
                                    break;
                                }
                            }
                        })
                }
            });
        }
    }
})();
