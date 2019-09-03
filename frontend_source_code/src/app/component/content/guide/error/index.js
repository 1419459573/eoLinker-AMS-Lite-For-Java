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
     * @function [安装引导失败页] [Installation failed page]
     * @version  3.0.2
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  CommonResource [注入通用接口服务] [inject common API service]
     * @service  $state [注入路由服务] [inject $state service]
     * @service  $window [注入window服务] [inject window service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider','RouteHelpersProvider', function($stateProvider,helper) {
            $stateProvider
                .state('guide.error', {
                    url: '/error', 
                    template: '<error></error>',
                    auth: true // 页面权限，值为true时在未登录状态可以显示页面，默认为假 When the value is true, the page can be displayed without login. The default is false
                });
        }])
        .component('error', {
            templateUrl: 'app/component/content/guide/error/index.html',
            controller: errorCtroller
        })

        errorCtroller.$inject = ['$scope', 'CommonResource', '$state', '$window', 'CODE', '$filter'];

    function errorCtroller($scope, CommonResource, $state, $window, CODE, $filter) {
        var vm = this;
        vm.data = {
            info: {},
            fun: {
                init: null
            }
        }

        /**
         * @function [初始化功能函数，检测是否已安装，若已安装则跳转首页] [Initialize, check whether it is installed, if it is installed, jump home page]
         */
        vm.data.fun.init = function() {
            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('00010'),$filter('translate')('00212')] });
            if (window.localStorage['INSTALLINFO']) {
                try {
                    var info = JSON.parse(window.localStorage['INSTALLINFO']);
                    vm.data.info.master = info.master;
                    vm.data.info.name = info.name;
                    vm.data.info.userName = info.userName;
                    vm.data.info.password = info.password;
                } catch (e) {
                    vm.data.info.master = '';
                    vm.data.info.name = '';
                    vm.data.info.userName = '';
                    vm.data.info.password = '';
                }
            }
            CommonResource.Install.Config().$promise.then(function(data) {
                if (data.statusCode == CODE.COMMON.SUCCESS) {
                    $state.go('index');
                }
            });
        }
        vm.data.fun.init();
    }
})();