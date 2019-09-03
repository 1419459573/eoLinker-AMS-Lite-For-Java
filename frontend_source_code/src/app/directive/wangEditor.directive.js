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
     * @function [wangEditor相关函数js] [wangEditor Related Functions js]
     
     * @service  $timeout [注入$timeout服务] [Inject $timeout service]
     * @service  $filter [注入过滤器服务] [Inject filter service]
     */
    angular.module('eolinker.directive')

    .directive("wangEditor", editorFn);

    editorFn.$inject = ['$timeout', '$filter']

    function editorFn($timeout, $filter) {

        return {
            restrict: 'AE',
            require: '?ngModel', //ng-model绑定wangEditor初始化数据 ng-model binding wangEditor initializes the data
            link: function($scope, element, attrs, ngModel) {
                var token = {
                    uptoken: '',
                    key: ''
                };
                var timer = null;

                function printLog(title, info) {
                    window.console && console.log(title, info);
                }

                var textarea = !!attrs.editId ? document.getElementById(attrs.editId) : document.getElementById('editor-js');
                var editor = new wangEditor(textarea); //初始化wangEditor Initialize wangEditor
                editor.config.menus = [
                    'source',
                    '|',
                    'bold',
                    'underline',
                    'italic',
                    'strikethrough',
                    'eraser',
                    //'forecolor',
                    //'bgcolor',
                    '|',
                    'quote',
                    //'fontfamily',
                    'fontsize',
                    'head',
                    'unorderlist',
                    'orderlist',
                    'alignleft',
                    'aligncenter',
                    'alignright',
                    '|',
                    'link',
                    'unlink',
                    'table',
                    '|',
                    // 'img',
                    'insertcode',
                    '|',
                    'undo',
                    'redo'
                ];
                editor.config.menuFixed = false;
                editor.create();

                /**
                 * @function [页面更改消除wangEditor] [Page changes eliminate wangEditor]
                 */
                $scope.$on('$stateChangeStart', function() { 
                    editor.destroy();
                })
                if (ngModel) {

                    /**
                     * @function [ngModel.$render ng-model值发生变化时执行函数] [ngmodod. $ render ng-model The function is executed when the value changes]
                     */
                    ngModel.$render = function() { 
                        try {
                            if (!!ngModel.$viewValue) {
                                editor.$txt.html($filter('XssFilter')(ngModel.$viewValue));
                            } else {
                                ngModel.$setViewValue("");
                            }
                        } catch (e) {

                        }
                    };

                    /**
                     * @function [wangEditor 内容更改执行功能函数] [wangEditor content change execution function function]
                     */
                    editor.onchange = function() { 
                        timer = $timeout(function() {
                            ngModel.$setViewValue(editor.$txt.html());
                        }, 0, true)
                    };
                }

                /**
                 * @function [页面更改消除计时器] [The page changes the elimination timer]
                 */
                $scope.$on('$destroy', function() { 
                    if (timer) {
                        $timeout.cancel(timer);
                    }
                });

                /**
                 * @function [重置wangEditor] [Reset wangEditor]
                 */
                $scope.$on('$resetWangEditor', function() { 
                    if (editor) {
                        editor.$txt.html('');
                    }
                });
            }
        }

    };

})();
