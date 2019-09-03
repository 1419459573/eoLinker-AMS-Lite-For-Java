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
     * @function [上传文件重置指令js] [Upload file reset command js]
     
     * @service  $compile [注入$compile服务] [inject $compile service]
     */
    angular.module('eolinker.directive')

    .directive('fileResetDirective', ['$compile', function($compile) {
        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            link: function($scope, elem, attrs, ctrl) {
                var data = {
                    fun: {
                        init: null, 
                        change: null 
                    }
                }
                /**
                 * @function [初始化功能函数] [initialization]
                 * @param    {[obj]}   _default [原生传参 Native parameters]
                 */
                data.fun.change = function(_default) {
                    elem[0].parentNode.replaceChild($compile(elem[0].outerHTML)($scope)[0], elem[0])
                    $scope.$apply();
                }

                /**
                 * @function [file按钮内容更改触发功能函数 file button contents change trigger]
                 */
                data.fun.init = (function() {
                    elem.bind(attrs.buttonFunction || 'click', data.fun.change);
                })()
            }
        };
    }]);
})();
