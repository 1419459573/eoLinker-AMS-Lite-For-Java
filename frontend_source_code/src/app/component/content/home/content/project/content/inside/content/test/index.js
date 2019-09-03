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
     * @function [自动化测试侧边栏组件] [Automated test sidebar components]
     * @version  3.1.7
     */
    angular.module('eolinker')
        .component('homeProjectInsideTest', {
            template: '<div>' +
                '<home-project-inside-test-sidebar power-object="$ctrl.powerObject"></home-project-inside-test-sidebar>' +
                '<header ng-show="$ctrl.data.info.status!=-1">' +
                '<ul> ' +
                '<li class="env-li pull-right" style="margin:9px 5px 9px 0;">' +
                '<env-ams-component env-model="$ctrl.data.service.home.envObject.object.model" env-query-init="$ctrl.data.service.home.envObject.object.fun" env-param="$ctrl.data.service.home.envObject.object.param" total-env="$ctrl.data.service.home.envObject.object.total"></env-ams-component>' +
                '</li></ul>' +
                '</header>' +
                '    <div ui-view>' +
                '    </div>' +
                '</div>',
            bindings: {
                powerObject: '<'
            },
            controller: indexController
        })

    indexController.$inject = ['$scope', '$state', 'HomeProject_Common_Service', 'Cache_CommonService', '$filter'];

    function indexController($scope, $state, HomeProject_Common_Service, Cache_CommonService, $filter) {
        var vm = this;
        vm.data = {
            service: {
                home: HomeProject_Common_Service,
                cache: Cache_CommonService
            },
            info: {
                status: 0,
            },
            fun: {
                init: null //初始化功能函数
            },
            assistantFun: {
                init: null //辅助初始化功能函数
            }
        }
        vm.data.assistantFun.init = function() {
            vm.data.service.home.envObject.fun.resetObject();
            if ((/(default)|(api)/).test($state.current.name)) {
                vm.data.info.status = 0;
            } else {
                vm.data.info.status = -1;
            }
        }
        $scope.$on('$stateChangeSuccess', function() { //路由转换函数，检测是否该显示环境变量
            vm.data.assistantFun.init();
        });
        vm.data.fun.init = (function() {
            vm.data.service.cache.set(null, 'apiList');
            vm.data.service.cache.set(null, 'apiGroup');
            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012136')] });
            vm.data.assistantFun.init();
        })()
    }
})();
