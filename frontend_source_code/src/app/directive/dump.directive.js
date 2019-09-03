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
     * @function [导出文件指令js] [Export the file instructions js]
     
     * @param    interaction [交互参数] [Interactive parameters]
     * @param    dumpDirective [绑定设置回调函数] [Bind Set the callback function]
     */
    angular.module('eolinker.directive')

    .directive('dumpDirective', [function() {
        return {
            restrict: 'A',
            transclude: true,
            template: '<a class="eo-export" data-ng-click="data.fun.dump()"><p>{{interaction.request.text}}</p></a><div load-directive="dumpDirective(arg)" interaction="{request:{delay:true}}"></div>',
            scope: {
                interaction: '<',
                dumpDirective: '&'
            },
            link: function($scope, elem, attrs, ctrl) {
                $scope.data = {
                    info: {
                        elem: document.getElementById('dump-directive_js')
                    },
                    fun: {
                        dump: null, 
                    }
                }
                var data = {
                    info: {
                        broadcast: null
                    },
                    fun: {
                        init: null, 
                        $DumpDirective_Click: null, 
                        $Destory: null, 
                    }
                }

                /**
                 * @function [导出功能函数] [Export]
                 */
                $scope.data.fun.dump = function() {
                    $scope.$broadcast('$LoadingInit', { arg: { switch: $scope.interaction.request.switch } }); //switch导出结果存储方式（0：.export ，1：.html文件夹 2：.pdf）
                }

                /**
                 * @function [导出监听请求返回功能函数] [Export the monitor request to return]
                 * @param    {[obj]}   _default [原生传参] [Native parameters]
                 * @param    {[obj]}   arg      [{response:后台返回数据 Backstage returns data}]
                 */
                data.fun.$DumpDirective_Click = function(_default, arg) { 
                    $scope.data.info.elem.href = arg.response.fileName;
                    $scope.data.info.elem.download = arg.response.fileName;
                    $scope.data.info.elem.click();
                }

                /**
                 * @function [资源回收] [Recycle]
                 */
                data.fun.$Destory = function() {
                    data.info.broadcast();
                }

                /**
                 * @function [初始化功能函数] [初始化]
                 */
                data.fun.init = (function() {
                    $scope.interaction=$scope.interaction||{request:{}};
                    data.info.broadcast = $scope.$on('$DumpDirective_Click_' + ($scope.interaction.request.switch||''), data.fun.$DumpDirective_Click);
                    $scope.$on('$destroy', data.fun.$Destory);
                })()
            }
        };
    }]);
})();
