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
     * @function [路由帮助服务] [Routing help service]
     
     * @service  APP_REQUIRES [注入加载应用常量] [inject Loading application constant service]
     * @return  data.fun [服务相关方法] [Service correlation method]
     */
    angular
        .module('eolinker')
        .provider('RouteHelpers', RouteHelpersProvider)

    RouteHelpersProvider.$inject = ['APP_REQUIRES'];

    function RouteHelpersProvider(APP_REQUIRES) {
        /* jshint validthis:true */
        var data = {
            info:{
                query:{
                    MODULES:APP_REQUIRES.MODULES,
                    SCRIPTS:{}
                }
            },
            fun: {
                basepath: null, // Set here the base of the relative path;for all app views
                resolveFor: null, // Generates a resolve object by passing script names;previously configured in constant.APP_REQUIRES
            }
        }

        data.fun.$get = function() {
            return {
                basepath: data.fun.basepath,
                resolveFor: data.fun.resolveFor
            };
        }

        /**
         * @function [配置基础路径功能模块] [Configuring the basic path function module]
         * @param    {[string]}   uri [原始路径 original path]
         * @return   {[string]}       [基础路径 Basic path]
         */
        data.fun.basepath = function(uri) {
            return 'app/' + uri;
        }

        /**
         * @function [按需加载功能模块] [Load function module on demand]
         * @return   {[function]}       [按需加载函数体 Loading functions on demand]
         */
        data.fun.resolveFor = function() {
            var _args = arguments;
            return {
                deps: ['$ocLazyLoad', '$q', function($ocLL, $q) {
                    // Creates a promise chain for each argument
                    var promise = $q.when(1); // empty promise
                    for (var i = 0, len = _args.length; i < len; i++) {
                        promise = andThen(_args[i]);
                    }
                    return promise;

                    // creates promise to chain dynamically
                    function andThen(_arg) {
                        // also support a function that returns a promise
                        if (typeof _arg === 'function')
                            return promise.then(_arg);
                        else
                            return promise.then(function() {
                                // if is a module, pass the name. If not, pass the array
                                var whatToLoad = getRequired(_arg);
                                // simple error check
                                if (!whatToLoad) return $.error('Route resolve: Bad resource name [' + _arg + ']');
                                // finally, return a promise
                                return $ocLL.load(whatToLoad);
                            });
                    }
                    // check and returns required data
                    // analyze module items with the form [name: '', files: []]
                    // and also simple array of script files (for not angular js)
                    function getRequired(name) {
                        if (data.info.query.MODULES)
                            for (var m in data.info.query.MODULES)
                                if (data.info.query.MODULES[m].name && data.info.query.MODULES[m].name === name)
                                    return data.info.query.MODULES[m];
                        return data.info.query.SCRIPTS && data.info.query.SCRIPTS[name];
                    }

                }]
            };
        }
        return data.fun;
    }


})();
