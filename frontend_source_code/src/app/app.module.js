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
     * @function [全局定义app模块js] [Global definition app module js]
     */
    angular.module('eolinker', [
        //thrid part
        'ui.router',
        'oc.lazyLoad',
        'ngResource',
        'angular-md5',
        'pascalprecht.translate',
        'ngCookies',
        //custom part
        'eolinker.resource',
        'eolinker.modal',
        'eolinker.constant',
        'eolinker.filter',
        'eolinker.directive',
        'eolinker.service'
    ])

    .config(AppConfig)

    .run(AppRun);


    AppConfig.$inject = ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$logProvider', '$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', '$translateProvider', 'isDebug', 'CN'];


    function AppConfig($controllerProvider, $compileProvider, $filterProvider, $provide, $logProvider, $stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, $translateProvider, IsDebug, CN) {

        var data = {
            fun: {
                init: null, //初始化功能函数 initialization
                param: null, //解析请求参数格式功能函数 Parse the request parameter format
            }
        }
        data.fun.param = function(arg) {
            var query = '',
                name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in arg.object) {
                value = arg.object[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += data.fun.param({ object: innerObj }) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += data.fun.param({ object: innerObj }) + '&';
                    }
                } else if (value !== undefined && value !== null)
                    query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };
        data.fun.init = (function() {
            $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
            // Override $http service's default transformRequest
            $httpProvider.defaults.transformRequest = [function(callback) {
                return angular.isObject(callback) && String(callback) !== '[object File]' ? data.fun.param({ object: callback }) : callback;
            }];
            // Enable log
            $logProvider.debugEnabled(IsDebug);
            $urlRouterProvider.otherwise('/index');
            $translateProvider.translations('zh-cn', CN)
            $translateProvider.preferredLanguage('zh-cn');
        })();
    }

    AppRun.$inject = ['$rootScope', '$state', '$stateParams', '$translate', '$templateCache'];


    function AppRun($rootScope, $state, $stateParams, $translate, $templateCache) {

        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    }
})();
