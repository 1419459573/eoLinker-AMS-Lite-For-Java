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
     * @function [自动化测试页路由及子路由] [Automated test page routing and sub-routing]
     * @version  3.1.7
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.test', {
                    url: '/test',
                    template: '<home-project-inside-test power-object="$ctrl.data.info.powerObject"></home-project-inside-test>'
                })
                .state('home.project.inside.test.default', {
                    url: '/default?groupID?childGroupID?grandSonGroupID?search',
                    template: '<home-project-inside-test-default power-object="$ctrl.powerObject"></home-project-inside-test-default>'
                })
                .state('home.project.inside.test.api', {
                    url: '/api?groupID?childGroupID?grandSonGroupID?caseID',
                    template: '<home-project-inside-test-api power-object="$ctrl.powerObject"></home-project-inside-test-api>'
                })
                .state('home.project.inside.test.edit', {
                    url: '/operateApi/:status?groupID?childGroupID?grandSonGroupID?caseID?connID?orderNumber',
                    template: '<home-project-inside-test-edit-singal></home-project-inside-test-edit-singal>'
                });
        }])
})();