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
     * @function [自动补全指令js] [Automatic completion of instruction JS]
     
     * @service  $compile [注入$compile服务] [inject $compile service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @param    array [自定义数组填充数组] [Custom array fill array]
     * @param    model [输入框绑定] [Input box binding]
     * @param    inputChangeFun [输入框值改变绑定功能函数] [The input box value changes the binding function]
     */
    angular.module('eolinker.directive')

    .directive('autoComplete', ['$compile', '$rootScope', function($compile, $rootScope) {
        return {
            restrict: 'A',
            transclude: true,
            template: '<div ng-mouseover="mouseLeave=false" ng-mouseleave="mouseLeave=true">' +
                '<input placeholder="{{data.info.attr.placeholder || \'\'}}" id="{{data.info.hashCode}}-js"class="eo-input {{data.info.attr.addClass || \'\'}}" data-ng-model="model" data-ng-change="data.fun.modelChange()" data-ng-blur="data.fun.modelBlur()"  ng-focus="data.info.isFocus=true;">' +
                '<a ng-show="data.info.isFocus&&data.info.attr.expressionBuilder" style="margin-left: -70px;border-radius:0;border:none;"  class="iconfont icon-index-magicwand" data-ng-click="data.fun.expressionBuilder()"></a>' +
                '<label  for="{{data.info.hashCode}}-js" class="iconfont icon-xiangxia" ng-click="data.fun.changeSwitch()" ></label>' +
                '<div class="auto-complete-message" ng-show="data.info.view.isShow" style="z-index:10;height: 100px;background-color: #fff;border: 1px solid #e5e5e5;overflow-y: scroll; width:{{data.info.elem.clientWidth-data.info.attr.padding }}px">' +
                '<ul>' +
                '<li class="auto-complete-li" ng-repeat="item in data.info.array.filter track by $index" data-ng-click="data.fun.changeText(item)">{{item}}</li>' +
                '</ul>' +
                '</div>' +
                '<style>.auto-complete-message{position: absolute;}.auto-complete-li:hover{background-color:#f5f5f5;}</style></div>',
            scope: {
                array: '<', 
                model: '=', 
                inputChangeFun: '&', 
            },
            link: function($scope, elem, attrs, ngModel) { 
                $scope.data = {
                    info: {
                        attr:{
                            placeholder:attrs.placeholder,
                            expressionBuilder:attrs.expressionBuilder,
                            addClass:attrs.addClass,
                            padding:attrs.padding
                        },
                        hashCode: new Date().getTime(),
                        array: {
                            origin: $scope.array || [],
                            filter: []
                        },
                        input: {
                            isFocus: false,
                        },
                        view: {
                            isShow: false
                        },
                        isFocus: false,
                        expressionBuilderObject: {
                            request: {},
                            response: {}
                        },
                        elem: elem[0]
                    },
                    fun: {
                        modelBlur: null, 
                        changeText: null, 
                        changeSwitch: null, 
                        modelChange: null, 
                        expressionBuilder: null 
                    }
                }
                var data = {
                    info: {
                        html: '',
                        
                        keydown: {
                            preCount: -1,
                            count: -1,
                            elem: null,
                            originParent: null,
                            originElem: null,
                            originNextParent: null,
                            originNextElem: null
                        }
                    },
                    fun: {
                        init: null, 
                        reset: null, 
                        keydown: null 
                    }
                }

                /**
                 * @function [表达式构造功能函数] [Expression construction function]
                 */
                $scope.data.fun.expressionBuilder = function() {
                    $scope.data.info.expressionBuilderObject.request.constant = $scope.model;
                    $rootScope.ExpressionBuilderModal($scope.data.info.expressionBuilderObject, function(callback) {
                        $scope.model = callback.response.result || $scope.model;
                        $scope.data.info.expressionBuilderObject = callback;
                    });
                }

                /**
                 * @function [input框信息改变触发功能函数] [Input frame information change trigger function]
                 */
                $scope.data.fun.modelChange = function() {
                    $scope.data.info.view.isShow = true;
                    $scope.inputChangeFun();
                    if ($scope.model) {
                        $scope.data.info.array.filter = [];
                        var template = {
                            count: 0
                        }
                        angular.forEach($scope.data.info.array.origin, function(val, key) {
                            var pattern = '/^' + $scope.model.toLowerCase() + '/';
                            try {
                                if (eval(pattern).test(val.toLowerCase())) {
                                    $scope.data.info.array.filter.splice(template.count, 0, val);
                                    template.count++;
                                } else if (val.toLowerCase().indexOf($scope.model.toLowerCase()) > -1) {
                                    $scope.data.info.array.filter.push(val);
                                }
                            } catch (e) {
                                console.log(e)
                            }
                        })
                        if ($scope.data.info.array.filter.length <= 0) {
                            $scope.data.info.view.isShow = false;
                        }
                    } else {
                        $scope.data.info.array.filter = $scope.data.info.array.origin;
                    }
                }

                /**
                 * @function [单击下拉按钮显示下拉菜单函数] [Click the drop down button to display the drop-down menu function]
                 */
                $scope.data.fun.changeSwitch = function() {
                    $scope.data.info.view.isShow = !$scope.data.info.view.isShow;
                    if ($scope.data.info.view.isShow) {
                        $scope.data.info.array.filter = $scope.data.info.array.origin;
                    }
                }

                /**
                 * @function [选中下拉框单项内容执行函数] [Select the individual content of the drop-down box to execute the function]
                 * @param    {[string]}   info [选中内容 Selected content] 
                 */
                $scope.data.fun.changeText = function(info) {
                    $scope.model = info;
                    $scope.data.info.view.isShow = false;
                    $scope.inputChangeFun();
                    data.fun.reset();
                }

                /**
                 * @function [失去焦点执行函数] [Losing focus execution function]
                 */
                $scope.data.fun.modelBlur = function() {
                    setTimeout(function() { //进行延时处理，时间单位为千分之一秒 Delay processing, the time unit is 1/1000 seconds
                        $scope.data.info.isFocus = false;
                        $scope.$digest();
                    }, 500)
                    if ($scope.mouseLeave) {
                        $scope.data.info.view.isShow = false;
                        data.fun.reset();
                    }
                }

                /**
                 * @function [重置自动匹配指令] [Reset auto match instruction]
                 */
                data.fun.reset = function() {
                    data.info.keydown.originParent.scrollTop = 0;
                    data.info.keydown.originNextParent.scrollTop = 0;
                    data.info.keydown.count = -1;
                    if (data.info.keydown.elem) {
                        data.info.keydown.elem.style.backgroundColor = null;
                    }
                    try {
                        $scope.$digest();
                    } catch (e) {}

                }

                /**
                 * @function [键盘监听功能函数] [Keyboard monitor function]
                 * @param    {[type]}   _default [原生传参 native parameter]
                 */
                data.fun.keydown = function(_default) {
                    switch (_default.keyCode) {
                        case 38: // up
                        case 40: // down
                            {
                                _default.preventDefault();
                                if (!$scope.data.info.view.isShow) return;
                                var template = {
                                    parent: $scope.data.info.view.isShow ? data.info.keydown.originParent : data.info.keydown.originNextParent,
                                    origin: $scope.data.info.view.isShow ? data.info.keydown.originElem : data.info.keydown.originNextElem
                                };
                                data.info.keydown.preCount = data.info.keydown.count;
                                if (data.info.keydown.elem) {
                                    data.info.keydown.elem.style.backgroundColor = null;
                                }
                                switch (_default.keyCode) {
                                    case 38:
                                        {
                                            if (data.info.keydown.count == -1 || data.info.keydown.count == 0) {
                                                data.info.keydown.count = template.origin.childElementCount - 1;
                                            } else {
                                                data.info.keydown.count--;
                                            }
                                            data.info.keydown.elem = angular.element(template.origin.children[data.info.keydown.count])[0];
                                            data.info.keydown.elem.style.backgroundColor = '#f5f5f5';
                                            if (data.info.keydown.count < data.info.keydown.preCount) {
                                                template.parent.scrollTop = (data.info.keydown.count - 4) * data.info.keydown.elem.offsetHeight;
                                            } else {
                                                template.parent.scrollTop = data.info.keydown.count * data.info.keydown.elem.offsetHeight;
                                            }
                                            return false;
                                            break;
                                        }
                                    case 40:
                                        {
                                            if (data.info.keydown.count == (template.origin.childElementCount - 1)) {
                                                data.info.keydown.count = 0;
                                            } else {
                                                data.info.keydown.count++;
                                            }
                                            data.info.keydown.elem = angular.element(template.origin.children[data.info.keydown.count])[0];
                                            data.info.keydown.elem.style.backgroundColor = '#f5f5f5';
                                            if (data.info.keydown.count > 4) {
                                                template.parent.scrollTop = (data.info.keydown.count - 4) * data.info.keydown.elem.offsetHeight;
                                            } else if (data.info.keydown.count < data.info.keydown.preCount) {
                                                template.parent.scrollTop = 0;
                                            }
                                            return false;
                                            break;
                                        }
                                }
                                break;
                            }
                        case 13:
                            { //enter
                                _default.preventDefault();
                                if (data.info.keydown.elem) {
                                    $scope.data.fun.changeText(data.info.keydown.elem.innerText);
                                }
                                return false;
                                break;
                            }
                    }
                }

                /**
                 * @function [初始化功能函数] [Initialization]
                 */
                data.fun.init = function() {
                    elem.on('keydown', data.fun.keydown);
                    data.info.keydown.originParent = angular.element(angular.element(elem[0].children[0])[0].children[3])[0];
                    data.info.keydown.originElem = angular.element(angular.element(angular.element(elem[0].children[0])[0].children[3])[0].children[0])[0];
                    data.info.keydown.originNextParent = angular.element(angular.element(elem[0].children[0])[0].children[2])[0];
                    data.info.keydown.originNextElem = angular.element(angular.element(angular.element(elem[0].children[0])[0].children[2])[0].children[0])[0];
                }
                data.fun.init();
            }
        };
    }]);
})();
