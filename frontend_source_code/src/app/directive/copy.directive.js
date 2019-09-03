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
     * @function [复制input值到剪贴板js] [Copy input value to clipboard JS]
     
     * @service  $compile [注入$compile服务] [inject $compile service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @param    copyModel [copy的内容] [The content of copy]
     */
    angular.module('eolinker.directive')

    .directive('copyDirective', ['$rootScope', '$compile', '$filter', function($rootScope, $compile, $filter) {
        return {
            restrict: 'A',
            transclude: true,
            scope: {
                copyModel: '=' 
            },
            link: function($scope, elem, attrs, ngModel) {
                var data = {
                    info: {
                        templet: {
                            button: '<button class="{{\'eo-button-default \'+data.info.timestamp}}" data-clipboard-text="{{copyModel}}"><span class=\"iconfont icon-copy\" ><\/span>{{data.info.clipboard.text}}</button>',
                            input: '<input type="text" id="{{data.info.timestamp}}" name="link" value="{{copyModel}}" class="{{\'eo-input \'+data.info.timestamp}}" data-clipboard-action="copy" data-clipboard-target="{{\'#\'+data.info.timestamp}}" ng-class="{\'eo-copy\':(data.info.clipboard.success)&&(data.info.clipboard.isClick)}" data-ng-click="data.fun.click()" readonly>' + '<label for="{{data.info.timestamp}}" class="pull-right copy-tips " ng-class="{\'copy-success\':(data.info.clipboard.success)&&(data.info.clipboard.isClick),\'copy-error\':(!data.info.clipboard.success)&&(data.info.clipboard.isClick)}">' + '{{data.info.clipboard.text}}' + '</label>',
                            textarea: '<textarea id="{{data.info.timestamp}}" readonly>{{copyModel}}</textarea><button data-clipboard-action="copy" data-clipboard-target="{{\'#\'+data.info.timestamp}}">{{data.info.clipboard.text}}</button>'
                        }
                    },
                    fun: {
                        init: null, 
                        reset: null, 
                        $destory: null, 
                    }
                }
                $scope.data = {
                    info: {
                        timestamp: 'copy-' + $filter('timestampFilter')(),
                        clipboard: {
                            isClick: false,
                            success: false,
                            fun: '',
                            text:attrs.buttonHtml || $filter('translate')('370')//显示button文本（默认文本'点击复制'）Display button text (default text 'click Copy')
                        }
                    },
                    fun: {
                        click: null, 
                    }
                }

                /**
                 * @function [重置功能函数] [Reset function]
                 * @param    {[obj]}   arg [{class:html相应class HTML相应类}]
                 */
                data.fun.reset = function(arg) {
                    $scope.data.info.clipboard.fun = new Clipboard(arg.class);
                    $scope.data.info.clipboard.fun.on('success', function(_default) {
                        $scope.data.info.clipboard.success = true;
                        $scope.data.info.clipboard.isClick = true;
                        console.info('Text:', _default.text);
                        if (attrs.isPopup) {//成功或者失败是否以弹窗形式提醒 Does the success or failure remind in popups
                            $rootScope.InfoModal($filter('translate')('371'), 'success');
                        } else {
                            $scope.data.info.clipboard.text = $filter('translate')('372');
                        }
                        $scope.$digest();
                        _default.clearSelection();
                    });

                    $scope.data.info.clipboard.fun.on('error', function(_default) {
                        $scope.data.info.clipboard.success = false;
                        $scope.data.info.clipboard.isClick = true;
                        console.info('Text:', _default.text);
                        if (attrs.isPopup) {
                            $rootScope.InfoModal($filter('translate')('373'), 'error');
                        } else {
                            $scope.data.info.clipboard.text = $filter('translate')('374');
                        }
                        $scope.$digest();
                    });
                }

                /**
                 * @function [单击功能函数] [Click function]
                 */
                $scope.data.fun.click = function() {
                    $scope.data.info.clipboard.isClick = false;
                }

                /**
                 * @function [页面销毁功能函数] [Page destruction function]
                 */
                data.fun.$destroy = function() {
                    $scope.data.info.clipboard.fun.destroy();
                }

                /**
                 * @function [初始化功能函数] [Initialization function]
                 */
                data.fun.init = (function() {
                    var template = {
                        html: ''
                    }
                    switch (attrs.switchTemplet) {//选择模板（0：button模板，1：input模板，2：textarea模板，默认input模板）Select templates (0:button templates, 1:input templates, 2:textarea templates, default input templates)
                        case '0':
                            {
                                template.html = data.info.templet.button;
                                break;
                            }
                        case '1':
                            {
                                template.html = data.info.templet.input;
                                break;
                            }
                        case '2':
                            {
                                template.html = data.info.templet.textarea;
                                break;
                            }
                        default:
                            {
                                template.html = data.info.templet.input;
                                break;
                            }
                    }
                    angular.element(elem).append($compile(template.html)($scope));
                    $scope.$on('$destroy', data.fun.$destroy);
                    data.fun.reset({ class: ('.' + $scope.data.info.timestamp) });
                })()
            }
        };
    }]);
})();
