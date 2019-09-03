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
     * @function [核心配置模块] [Core configuration module]
     
     * @service  $rootScope [注入根作用域] [inject rootScope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  CommonResource [注入通用接口服务] [inject common API service]
     * @constant AUTH_EVENTS [注入权限事件常量] [inject authority event constant service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular
        .module('eolinker')
        .run(appRun);

    appRun.$inject = ['$rootScope', '$state', 'CommonResource', 'AUTH_EVENTS', 'CODE'];

    function appRun($rootScope, $state, CommonResource, AUTH_EVENTS, CODE) {
        var data = {
            info: {
                title: {
                    root: $rootScope.title
                },
                _hmt:[]
            },
            fun: {}
        };
        $rootScope.global = {
            ajax: {}
        }

        /**
         * 跳转页面时取消当前页面ajax请求
         */
        data.fun.cancelRequest = function() {
            for (var key in $rootScope.global.ajax) {
                var val = $rootScope.global.ajax[key];
                if (!val) return;
                val.$cancelRequest();
            }
            $rootScope.global.ajax = {};
        }

        /**
         * @function [监听路由改变功能函数] [watch route change]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         * @param    {[obj]}   arg [{auth:值为真时表示该页面在未登录状态下可以访问 When the value is true, it indicates that the page can be accessed without login}]
         */
        $rootScope.$on('$stateChangeStart', function(_default, arg) {
            window.scrollTo(0, 0);
            if (!arg.auth) {
                CommonResource.Guest.Check().$promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.UNLOGIN:
                            {
                                if ($state.current.name.indexOf('transaction') > -1) {
                                    $state.go('index');
                                } else {
                                    $rootScope.$broadcast(AUTH_EVENTS.UNAUTHENTICATED);
                                }
                                break;
                            }
                        case CODE.COMMON.UNAUTH:
                            {
                                $rootScope.$broadcast(AUTH_EVENTS.UNAUTHORIZED);
                                break;
                            }
                    }
                })
            }
        });

        /**
         * @function [转换交互功能函数] [Conversion interaction]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         * @param    {[obj]}   arg [自定义传参 customized parameter]
         */
        $rootScope.$on('$translateferStation', function(_default, arg) { 
            $rootScope.$broadcast(arg.state, arg.data);
        });

        /**
         * @function [设置title功能函数] [Setting title]
         * @param    {[obj]}   _default [原生传参] [原生传参 Native parameter]
         * @param    {[obj]}   arg [{list:title列表项 Title list item}]
         */
        $rootScope.$on('$WindowTitleSet', function(_default, arg) { 
            arg = arg || { list: [] };
            if (arg.list.length > 0) {
                window.document.title = arg.list.join('-') + (arg.list.length >= 1 ? '-' : '') +$rootScope.title;
            } else {
                window.document.title = $rootScope.title;
            }
        });

        /**
         * @function [监听服务器出错功能函数] [watch server error]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         */
        $rootScope.$on(AUTH_EVENTS.SYSTEM_ERROR, function(_default) {
            console.log("error");
        })

        /**
         * @function [监听未认证权限功能函数] [watch unauthorized authority]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         */
        $rootScope.$on(AUTH_EVENTS.UNAUTHENTICATED, function(_default) {
            $state.go('index');
        })

        /**
         * @function [监听未登录功能函数] [watch not logged in]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         */
        $rootScope.$on(AUTH_EVENTS.UNAUTHORIZED, function(_default) {
            $state.go('index');
        })
    }

})();
