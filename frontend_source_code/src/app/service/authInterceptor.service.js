(function() {
    'use strict';
    /**
     * @name eolinker open source，eolinker开源版本
     * @link [图片]https://www.eolinker.com
     * @package eolinker
     * @author [图片]www.eolinker.com 广州银云信息科技有限公司 2015-2018

     * eolinker，业内领先的Api接口管理及测试平台，为您提供最专业便捷的在线接口管理、测试、维护以及各类性能测试方案，帮助您高效开发、安全协作。
     * 如在使用的过程中有任何问题，可通过[图片]http://help.eolinker.com寻求帮助
     *
     * 注意！eolinker开源版本遵循GPL V3开源协议，仅供用户下载试用，禁止“一切公开使用于商业用途”或者“以eoLinker开源版本为基础而开发的二次版本”在互联网上流通。
     * 注意！一经发现，我们将立刻启用法律程序进行维权。
     * 再次感谢您的使用，希望我们能够共同维护国内的互联网开源文明和正常商业秩序。
     *
     * @function [交互拦截相关服务js] [Interception related services js]
     
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @service  $q [注入$q服务] [inject $q service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @constant AUTH_EVENTS [注入权限事件常量] [inject authority event constant service]
     */
    angular.module('eolinker')
        .factory('AuthInterceptor', AuthInterceptor);

    AuthInterceptor.$inject = ['$rootScope', '$q', '$filter', 'AUTH_EVENTS']

    function AuthInterceptor($rootScope, $q, $filter, AUTH_EVENTS) {
        var Auth;
        var data = {
            info: {
                auth: null
            },
            fun: {
                request: null, 
                response: null, 
                responseError: null 
            }
        }
        /**
         * @function [交互请求功能函数] [Interactive request]
         * @param    {[obj]}   config [相关配置 Related configuration] 
         */
        data.fun.request = function(config) { 
            config.headers = config.headers || {};
            if (config.method == 'POST') {
            }
            return config;
        };

        /**
         * @function [交互响应功能函数] [Interactive response]
         * @param    {[obj]}   response [返回信息 returned messages]
         */
        data.fun.response = function(response) { 
            if (response.data) {
                $rootScope.$broadcast({
                    901: AUTH_EVENTS.UNAUTHENTICATED,
                    401: AUTH_EVENTS.UNAUTHORIZED
                }[response.data.code], response);
                try {
                    if (typeof response.data == 'object') {
                        response.data = JSON.parse($filter('HtmlFilter')(angular.toJson(response.data)));
                    }
                } catch (e) {
                    response.data = response.data;
                    $rootScope.$broadcast(AUTH_EVENTS.SYSTEM_ERROR);
                }
            }
            return $q.resolve(response);
        };

        /**
         * @function [交互响应出错功能函数] [Interaction response error]
         * @param    {[obj]}   rejection [拒绝信息 Reject information]
         */
        data.fun.responseError = function(rejection) { 
            $rootScope.$broadcast(AUTH_EVENTS.SYSTEM_ERROR);
            return rejection;
        };
        return data.fun;
    }
})();
