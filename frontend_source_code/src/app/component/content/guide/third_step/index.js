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
     * @function [安装引导页step three] [Installation step three page]
     * @version  3.1.7
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  $window [注入window服务] [inject window service]
     * @service  CommonResource [注入通用接口服务] [inject common API service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider','RouteHelpersProvider', function($stateProvider,helper) {
            $stateProvider
                .state('guide.third_step', {
                    url: '/third_step',
                    template: '<third></third>',
                    auth: true // 页面权限，值为true时在未登录状态可以显示页面，默认为假 When the value is true, the page can be displayed without login. The default is false
                });
        }])
        .component('third', {
            templateUrl: 'app/component/content/guide/third_step/index.html',
            controller: thirdCtroller
        })

        thirdCtroller.$inject = ['$scope', '$state', '$window', 'CommonResource', '$filter', 'CODE'];

    function thirdCtroller($scope, $state, $window, CommonResource, $filter, CODE) {
        var vm = this;
        vm.data = {
            info: {
                installing: false,
                check: {
                    fileWrite: '',
                    db: ''
                },
            },
            interaction: {
                request: {},
                response: {
                    query: []
                }
            },
            fun: {
                checkConfig: null, 
                enterSecond: null, 
                install: null, 
                init: null 
            }
        }
        
        /**
         * @function [初始化功能函数，检测是否已安装，若已安装则跳转首页] [Initialize, check whether it is installed, if it is installed, jump home page]
         */
        vm.data.fun.init = function() {
            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('00410'),$filter('translate')('00212')] });
            CommonResource.Install.Config().$promise.then(function(data) {
                if (data.statusCode == CODE.COMMON.SUCCESS) {
                    $state.go('index');
                }
            });
            
            if (window.localStorage['INSTALLINFO']) {
                try {
                    var info = JSON.parse(window.localStorage['INSTALLINFO']);
                    vm.data.interaction.request.dbURL = info.master;
                    vm.data.interaction.request.dbName = info.name;
                    vm.data.interaction.request.dbUser = info.userName;
                    vm.data.interaction.request.dbPassword = info.password;
                    vm.data.interaction.request.websiteName = info.pageTitle;
                    vm.data.interaction.request.language = window.localStorage.lang;
                } catch (e) {
                    $state.go('guide.second_step');
                }
            } else {
                $state.go('guide.second_step');
            }
            vm.data.fun.checkConfig();
        }

        /**
         * @function [检测配置功能函数] [Detects configuration]
         */
        vm.data.fun.checkConfig = function() {
            CommonResource.Install.Check(vm.data.interaction.request).$promise.then(function(data) {
                if (data.statusCode == CODE.COMMON.SUCCESS) {
                    vm.data.interaction.response.query = data.envStatus;
                    if (data.envStatus.fileWrite == 1) {
                        vm.data.info.check.fileWrite = 'ok';
                    } else {
                        vm.data.info.check.fileWrite = 'error';
                    }
                    if (data.envStatus.db == 1) {
                        vm.data.info.check.db = 'ok';
                    } else {
                        vm.data.info.check.db = 'error';
                    }
                }
            });
        }

        vm.data.fun.init();
       
        /**
         * @function [返回上一步功能函数] [Return to the previous step]
         */
        vm.data.fun.enterSecond = function() {
            $state.go('guide.second_step');
        }

        /**
         * @function [安装功能函数] [installation]
         */
        vm.data.fun.install = function() {
            vm.data.info.installing = true;
            CommonResource.Install.Post(vm.data.interaction.request).$promise.then(function(data) {
                if (data.statusCode == CODE.COMMON.SUCCESS) {
                    $state.go('guide.finish');
                } else {
                    $state.go('guide.error');
                }
            })
        }
    }
})();