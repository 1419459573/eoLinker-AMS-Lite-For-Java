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
     * @function [协议页相关指令js] [Protocol page related instructions js]
     * @version  3.0.2
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $location [注入$location服务] [inject $location service]
     * @service  $anchorScroll [注入$anchorScroll服务] [inject $anchorScroll service]
     * @service  $window [注入$window服务] [inject window service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('register.protocol', {
                    url: '/protocol',
                    auth: true,
                    template: '<register-protocol></register-protocol>'
                });
        }])
        .component('registerProtocol', {
            templateUrl: 'app/component/content/register/content/protocol/index.html',
            controller: indexController
        })
        .run(['$anchorScroll', function($anchorScroll) {

            $anchorScroll.yOffset = 50; // 总是滚动额外的50像素 Always scrolling extra 50 pixels

        }])

    indexController.$inject = ['$scope', '$location', '$anchorScroll', '$window'];

    function indexController($scope, $location, $anchorScroll, $window) {
        var vm = this;
        vm.needFix = false;
        window.document.title = '用户服务协议 - eolinker 接口管理平台 | 业内领先的接口管理平台，让专业的接口管理变简单！';
        vm.goAnchor = function(info) {
            $location.hash(info);
            $anchorScroll();//移动到锚点 Move to anchor
        };
        vm.data = {}
        /**
         * @function [监听滚动条功能函数] [Listen to scroll]
         */
        $window.onscroll = function() {
            if ($window.scrollY > 246) {
                vm.needFix = true;
            } else {
                vm.needFix = false;
            }
            $scope.$digest();
        }
    }
})();
