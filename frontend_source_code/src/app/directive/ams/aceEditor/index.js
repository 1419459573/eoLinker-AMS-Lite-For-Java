(function() {
    "use strict";
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
     * @function [编辑器指令js] [Editor instructions js]
     * @param {string} setVariable 设置绑定setModel的对象键，(optional)，需搭配setModel使用
     * @param {object} setModel 存储值位置，双绑
     * @param {string} type 语言类型{json,javascript}，默认json
     */
    angular.module('eolinker.directive')

        .directive("aceEditorAmsDirective", index);

    index.$inject = []

    function index() {
        return {
            restrict: 'AE',
            require: '?ngModel',
            scope: {
                setVariable: '<',
                setModel: '=',
                type: '@'
            },
            link: function($scope, element, attrs, ngModel) {
                var data = {
                    editor: null,
                    fun: {
                        init: null
                    }
                }
                data.fun.render = function() {
                    if ($scope.setVariable) {
                        data.editor.session.setValue($scope.setModel[$scope.setVariable] || '');
                    } else {
                        data.editor.session.setValue($scope.setModel || '');
                    }

                };
                /**
                 * 初始化编辑器
                 */
                data.fun.init = (function() {
                    data.editor = ace.edit(attrs.id);
                    element[0].style.fontSize = '14px';
                    element[0].style.lineHeight = '25px';
                    data.editor.setOptions({
                        minLines: 5,
                        maxLines: 15,
                        enableBasicAutocompletion: true,
                        enableLiveAutocompletion: false,
                        enableSnippets: true
                    });
                    if (ngModel) {
                        ngModel.$render = data.fun.render;
                    }
                    data.editor.getSession().on('change', function(e) {
                        if ($scope.setVariable) {
                            $scope.setModel[$scope.setVariable] = data.editor.getValue();
                        } else {
                            $scope.setModel = data.editor.getValue();
                        }
                    });
                    switch (attrs.type) {
                        case 'javascript':
                            {
                                data.editor.session.setMode("ace/mode/javascript");

                                data.editor.setAutoScrollEditorIntoView(true)
                                data.editor.resize()
                                break;
                            }
                        case 'json':
                        default:
                            {
                                data.editor.session.setMode("ace/mode/json");

                                break;
                            }
                    }

                })()

                $scope.$on('$ResetAceEditor_AmsEditor', function() {
                    data.editor.session.setValue('');
                })
                
                $scope.$on('$stateChangeStart', function() {
                    if (data.editor) data.editor.destroy();
                })

                $scope.$on('$InitAceEditor_AmsEditor', function() {
                    if ($scope.setVariable) {
                        data.editor.session.setValue($scope.setModel[$scope.setVariable] || '');
                    } else {
                        data.editor.session.setValue($scope.setModel || '');
                    }
                })
            }
        }

    };

})();