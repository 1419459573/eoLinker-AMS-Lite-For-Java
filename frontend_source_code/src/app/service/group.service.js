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
     * @function [分组相关服务js] [Group related services js]
     * @version  3.0.2
     * @service  $rootScope [注入根作用域服务] [Inject rootscope service]
     */ 
    angular.module('eolinker')
        .factory('GroupService', GroupFactory);

    GroupFactory.$inject = ['$rootScope']

    function GroupFactory($rootScope) {
        var data = {
            info: {
                group: null //分组列表 Group list
            },
            fun: {
                get: null, 
                set: null, 
                clear:null,
            }
        }
        /**
         * @function [获取分组] [Get the grouping]
         */
        data.fun.get = function() {
            return data.info.group;
        }
        /**
         * @function [设置分组] [Separate Group]
         * @param    {[string]}   request [分组内容 Grouping content]
         * @param    {[boolean]}   boolean [是否需要初始化 Whether it needs to be initialized]
         */
        data.fun.set = function(request, boolean) {
            data.info.group = request;
            if (boolean) {
                $rootScope.$broadcast('$SidebarFinish');
            }
        }

        /**
         * @function [清空分组服务信息] [Empty the packet service information]
         */
        data.fun.clear = function() {
            data.info.group=null;
        }
        return data.fun;
    }
})();
