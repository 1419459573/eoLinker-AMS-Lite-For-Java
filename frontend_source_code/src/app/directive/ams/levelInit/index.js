(function () {
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
     * @function [json层级预处理指令] [json level preprocessing directive]
     * @param {string} level 限制层级
     */
    angular.module('eolinker')

        .directive('levelInitDirective', ['$compile', function ($compile) {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    level: '<',
                    limitLevel: '<',
                    status: '@'
                },
                link: function ($scope, elem, attrs, ctrl) {
                    var data = {
                        fun: {
                            init: null,
                        }
                    }



                    /**
                     * @function [自启动初始化功能函数] [Self-start initialization function]
                     */
                    data.fun.init = (function () {
                        var template = {
                            level: {
                                parent: $scope.level || '',
                                default: ($scope.level || 0) + 1,
                                limit: $scope.limitLevel || 5
                            },
                            html: ''
                        }
                        if (template.level.default > template.level.limit) {
                            return;
                        }
                        switch (attrs.status) {
                            case 'automatedTest_jsonMatch':
                                {
                                    template.html = '<ul><li ng-repeat="item' + template.level.default+' in item' + template.level.parent + '.childList" level-init-directive level=' + template.level.default+' limit-level=' + $scope.limitLevel + ' status=' + attrs.status + '>' +
                                    '<table>' +
                                    '<tbody>' +
                                    '<tr><td class="name-td">' +
                                    '<input style="margin-left:' + 15 * (template.level.default) + 'px;width:' + (308 - 15 * (template.level.default)) + 'px;" class="eo-input" ng-model="item' + template.level.default+'.paramKey" ng-class="{\'eo-input-error\':($ctrl.data.info.submited&&!item' + template.level.default+'.paramKey)}" maxlength="255" placeholder="参数名" ng-change="$ctrl.data.fun.last(\'responseParam\',{$last:$last,item:item' + template.level.parent + '})">' +
                                    '</td>' +
                                    '<td class="match-rule-td">' +
                                    '<select class="eo-input" ng-model="item' + template.level.default+'.matchRule" ng-init="item' + template.level.default+'.matchRule=item' + template.level.default+'.matchRule||\'0\'">' +
                                    '<option value="0">无</option>' +
                                    '<option value="1">等于 [ = ]</option>' +
                                    '<option value="2">不等于 [ != ]</option>' +
                                    '<option value="3">大于 [ &gt; ]</option>' +
                                    '<option value="4">小于 [ &lt; ]</option>' +
                                    '<option value="5">正则 [ Reg= ]</option>' +
                                    '</select>' +
                                    '</td>' +
                                    '<td>' +
                                    '<input class="eo-input pull-left" ng-model="item' + template.level.default+'.paramInfo"  placeholder="匹配值" >' +
                                    '</td>' +
                                    '<td class="operation-td">' +
                                    (template.level.default < $scope.limitLevel ? ('<a class=" number-label add-child-a" ng-click="$ctrl.data.fun.edit(\'addChild\',{item:item' + template.level.default+'})"><span class="iconfont icon-tianjia"></span><span>子字段</span></a>') : '') +
                                    '<a class="iconfont icon-shanchu number-label" ng-click="$ctrl.data.fun.delete(\'responseChild\',{$index:$index,item:item' + template.level.parent + '})"></a>' +
                                    '</td>' +
                                    '</tr></tbody>' +
                                    '</table>' +
                                    '</li>' +
                                    '</ul>';
                                    break;
                                }
                            case 'automatedTest_bindRule':
                                {
                                    template.level.argumentString = 'item '
                                    for (var key = 1; key <= template.level.default; key++) {
                                        template.level.argumentString += ', item' + key + ' ';
                                    }

                                    template.html = '<ul>' +
                                    '<li ng-repeat="item' + template.level.default+' in item' + template.level.parent + '.childList |filter:data.fun.filter" level-init-directive level=' + template.level.default+' limit-level=' + $scope.limitLevel + ' status=' + attrs.status + ' ng-class="{\'elem-active\':data.info.current.activeArray[' + template.level.default+']==(item' + template.level.default+'.parent+item' + template.level.default+'.paramKey)}" >' +
                                    '<p ng-click="data.fun.click(' + template.level.argumentString + ')"><span class="iconfont icon-xiangyou" style="font-size: 12px;color:#' + template.level.default+'0d' + template.level.default+'d4;margin-left:' + (template.level.default * 10) + 'px; " ></span>' +
                                    '{{item' + template.level.default+'.paramKey}}</p>' +
                                    '</li>' +
                                    '</ul>'
                                    break;
                                }
                            case 'report_jsonMatch':
                                {
                                    template.html = '<ul>' +
                                    '<li ng-repeat="item' + template.level.default+' in item' + template.level.parent + '.childList  | filter:$ctrl.data.fun.filter" level-init-directive level=' + template.level.default+' limit-level=' + $scope.limitLevel + ' status=' + attrs.status + ' >' +
                                    '<table>' +
                                    '<tbody><tr>' +
                                    '<td class="name-td"><span class="iconfont icon-xiangyou" style="font-size: 12px;color:#' + template.level.default+'0d' + template.level.default+'d4;margin-left:' + (template.level.default * 10) + 'px; " ></span>{{item' + template.level.default+'.paramKey}}</td>' +
                                    '<td class="rule-td" ng-switch="item' + template.level.default+'.matchRule">' +
                                    '<span ng-switch-when="0">无</span>' +
                                    '<span ng-switch-when="1">等于 [ = ]</span>' +
                                    '<span ng-switch-when="2">不等于 [ != ]</span>' +
                                    '<span ng-switch-when="3">大于 [ > ]</span>' +
                                    '<span ng-switch-when="4">小于 [ < ]</span>' +
                                    '<span ng-switch-when="5">正则 [ Reg= ]</span>' +
                                    '</td>' +
                                    '<td>{{item' + template.level.default+'.paramInfo}}</td>' +
                                    '</tr></tbody>' +
                                    '</table>' +
                                    '</li>' +
                                    '</ul>'
                                    break;
                                }
                        }
                        elem.append($compile(template.html)($scope.$parent));
                    })()
                }
            };
        }]);
})();