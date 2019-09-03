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
     * @function [上传文件指令js] [Upload file instruction js]
     
     */
    angular.module('eolinker.directive')

    .directive('uploadFileDirective', [function() {
        return {
            restrict: 'AE',
            template:'<input name="file" id="{{inputId}}" class="hidden" type="file" onChange="angular.element(this).scope().uploadFileDirective({arg:{$files:this.files}})" file-reset-directive button-function="change"/>',
            scope: {
                inputId:'@',
                uploadFileDirective: '&' //绑定设置回调函数
            },
            link: function($scope, elem, attrs, ctrl) {
                
            }
        };
    }]);
})();