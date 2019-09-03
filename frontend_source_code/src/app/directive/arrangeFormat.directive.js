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
     * @function [格式整理指令js] [Formatting instructions JS]
     
     * @service  $compile [注入$compile服务] [inject $compile service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @param    interaction [交互参数] [Interaction parameter]
     */
    angular.module('eolinker.directive')
        .directive('arrangeFormat', ['$compile', '$filter', '$rootScope', function($compile, $filter, $rootScope) {
            return {
                restrict: 'A',
                scope: {
                    interaction: '=' 
                },
                require: '?ngModel',
                link: function($scope, elem, attrs, ngModel) {
                    var data = {
                        info: {
                            //整理状态（0：字段错误，1：字段正确，整理前的字段结果,2:整理后的字段结果,3:暂无内容）
                            //Finishing state (0: field error, 1: field correct, field results before sorting, 2: collation of field results, 3: no content)
                            status: 0, 
                            text: {
                                origin: null,
                                result: null
                            },
                            originHtml: null
                        },
                        fun: {
                            init: null, 
                            render: null, 
                            click: null, 
                            hide: null, 
                            show: null, 
                            format: {
                                json: null, 
                                html: null 
                            }
                        },
                        interaction: $scope.interaction || { request: {}, response: {} }
                    }

                    /**
                     * @function [json格式整理功能函数] [JSON format function]
                     * @param    {[obj]}   arg [{text:需过滤的文本 Text to be filtered}]
                     */
                    data.fun.format.json = function(arg) {
                        try {
                            try {
                                return data.info.text.result = $compile($filter('JsonformatFilter')(arg.text, 4))($scope);
                            } catch (childE) {
                                return data.info.text.result = $compile($filter('JsonformatFilter')(JSON.stringify($filter('JsonLintFilter')(arg.text)), 4))($scope);
                            }
                        } catch (e) {
                            switch (arg.status) {
                                case 0:
                                    { //一次性格式整理 One time format arrangement
                                        return (arg.text || '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
                                        break;
                                    }
                                default:
                                    {
                                        return data.info.text.result = $compile('<span style="color: #f1592a;font-weight:bold;font-family: Menlo, Monaco, Consolas, Helvetica, 微软雅黑, monospace, Arial, sans-serif, 黑体;">' + e + '</span>')($scope);
                                        break;
                                    }
                            }

                        }
                    }
                    /**
                     * @function [html/xml格式整理功能函数] [Html/xml format function]
                     * @param    {[obj]}   arg [{text:需过滤的文本 Text to be filtered}]
                     */
                    data.fun.format.html = function(arg) {
                        try {
                            return $filter('HtmlformatFilter')(arg.text, 5).replace(/</g, '&lt;').replace(/>/g, '&gt;');
                        } catch (e) {
                            data.info.status = 0;
                            return arg.text.replace(/</g, '&lt;').replace(/>/g, '&gt;');
                        }
                    }

                    /**
                     * @function [ngModel.$render ng-model值发生变化时执行函数] [The execution function when the ngModel.$render ng-model value changes]
                     */
                    data.fun.render = function() {
                        data.info.text.origin = ngModel.$viewValue;
                        if (!ngModel.$viewValue) {
                            data.info.status = 3;
                            data.info.text.result = data.info.originHtml;
                        } else {
                            data.info.status = 2;
                            if (data.interaction.request.onlyOneTime) { //仅格式整理不相互转化 Only format arrangement does not transform each other
                                if (/^(<)(.*)(>)$/.test(data.info.text.origin.replace(/\s/g, ""))) {
                                    data.info.text.result = data.fun.format.html({ text: data.info.text.origin });
                                } else {
                                    data.info.text.result = data.fun.format.json({ text: data.info.text.origin, status: 0 });
                                }
                            } else if (data.interaction.request.type) { //自定义格式整理类型（0:json,1:xml,2:html）Custom formatting types (0:json, 1:xml, 2:html)
                                switch (data.interaction.request.type) {
                                    case 0:
                                        {
                                            data.info.text.result = data.fun.format.json({ text: data.info.text.origin, status: 1 });
                                            break;
                                        }
                                    case 1:
                                    case 2:
                                        {
                                            if (/^(<)(.*)(>)$/.test(data.info.text.origin.replace(/\s/g, ""))) {
                                                data.info.text.result = data.fun.format.html({ text: data.info.text.origin });
                                            } else {
                                                data.info.status = 0;
                                                data.info.text.result = (data.info.text.origin || '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
                                            }
                                            break;
                                        }
                                    default:
                                        {
                                            data.info.status = 0;
                                            data.info.text.result = (data.info.text.origin || '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
                                        }
                                }
                            } else { //主动判断格式是否为json或xml进行整理 Whether the active judgment format is JSON or XML is arranged
                                if (/^({|\[)(.*)(}|])$/.test(data.info.text.origin.replace(/\s/g, ""))) {
                                    data.info.text.result = data.fun.format.json({ text: data.info.text.origin, status: 1 });
                                } else if (/author="eolinker-frontend"/.test(data.info.text.origin.replace(/\s/g, ""))) {
                                    data.info.text.result = data.info.text.origin;
                                } else if (/^(<)(.*)(>)$/.test(data.info.text.origin.replace(/\s/g, ""))) {
                                    data.info.text.result = data.fun.format.html({ text: data.info.text.origin });
                                } else {
                                    data.info.status = 0;
                                    data.info.text.result = (data.info.text.origin || '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
                                }
                            }
                        }
                        angular.element(document.getElementById(attrs.arrangeFormat)).empty();
                        
                        angular.element(document.getElementById(attrs.arrangeFormat)).append(data.info.text.result);
                    };

                    /**
                     * @function [节点单击功能函数(格式前后转换)] [Node click function (format before and after conversion)]
                     */
                    data.fun.click = function() {
                        switch (data.info.status) {
                            case 0:
                                {
                                    $rootScope.InfoModal($filter('translate')('360'), 'error');
                                    break;
                                }
                            case 2:
                                {
                                    data.info.status = 1;
                                    angular.element(document.getElementById(attrs.arrangeFormat)).empty();
                                    angular.element(document.getElementById(attrs.arrangeFormat)).append((data.info.text.origin || '').replace(/</g, '&lt;').replace(/>/g, '&gt;'));
                                    break;
                                }
                            case 3:
                                {
                                    break;
                                }
                            default:
                                {
                                    data.info.status = 2;
                                    angular.element(document.getElementById(attrs.arrangeFormat)).empty();
                                    angular.element(document.getElementById(attrs.arrangeFormat)).append(data.info.text.result);
                                    break;
                                }
                        }
                    }

                    /**
                     * @function [json格式整理后收缩按钮功能函数] [Shrink button function after JSON format]
                     * @param    {[obj]}   arg [{target:该目标节点 The target node}] 
                     */
                    data.fun.hide = function(arg) {
                        var template = {
                            parent: arg.target.parentNode,
                            html: null,
                            type: null,
                            size: null
                        }
                        template.type = template.parent.getAttribute('data-type');
                        template.size = template.parent.getAttribute('data-size');
                        template.parent.setAttribute('data-inner', template.parent.innerHTML);
                        if (template.type === 'array') {
                            template.html = '<i  style="cursor:pointer;color: #3ab54a;font-size: 13px;padding: 0 5px;" class="iconfont icon-youjiantou-copy" ng-click="show($event)" ></i>Array[<span class="json_number">' + template.size + '</span>]';
                        } else {
                            template.html = '<i style="cursor:pointer;color: #3ab54a;font-size: 13px;padding: 0 5px;" class="iconfont icon-youjiantou-copy" ng-click="show($event)"></i>Object{...}';
                        }
                        angular.element(template.parent).empty();
                        angular.element(template.parent).append($compile(template.html)($scope));
                    }

                    /**
                     * @function [json格式整理后展示按钮功能函数] [Display button function after JSON format]
                     * @param    {[obj]}   arg [{target:该目标节点 The target node}]
                     */
                    data.fun.show = function(arg) {
                        var template = {
                            parent: arg.target.parentNode,
                            html: null
                        }
                        template.html = template.parent.getAttribute('data-inner');
                        angular.element(template.parent).empty();
                        angular.element(template.parent).append($compile(template.html)($scope));
                    }

                    /**
                     * @function [初始化功能函数] [Initialization]
                     */
                    data.fun.init = function() {
                        if (ngModel) {
                            ngModel.$render = data.fun.render;
                        }
                        if (!data.interaction.request.onlyOneTime) {
                            elem.bind('click', data.fun.click);
                        }
                        data.info.originHtml = document.getElementById(attrs.arrangeFormat).innerHTML;
                        $scope.hide = data.fun.hide;
                        $scope.show = data.fun.show;
                    }
                    data.fun.init();

                }
            };
        }]);
})();