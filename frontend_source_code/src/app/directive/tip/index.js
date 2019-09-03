(function() {
    'use strict';
    /**
     * @Author   广州银云信息科技有限公司
     * @function [提示指令js]
     
     * @service  $filter [注入过滤器服务]
     * @service  $timeout [注入$timeout服务]
     */
    angular.module('eolinker.directive')

    .directive('tipDirective', ['$filter', '$timeout', function($filter, $timeout) {
        return {
            restrict: 'AE',
            transclude: true,
            template: '<span class="iconfont icon-yiwen1" ></span>' +
                '<div class="tips-message" style="margin-left: {{data.input.marginLeft}}px;margin-top:-{{data.element.clientHeight+5}}px"><ul><li class="message-li" id="tip-directive-js-{{data.info.uuid}}" ></li><li class="arrow-li"></li></ul></div>',
            scope: {
                input: '@'
            },
            link: function($scope, elem, attrs, ctrl) {
                $scope.data = {
                    input:{
                        marginLeft:attrs.marginLeft||-5
                    },
                    info: {
                        uuid: $filter('uuidFilter')(),
                        element: null
                    }
                }
                var data = {
                    info: {
                        timer: null
                    },
                    fun:{
                        $destroy:null,
                    }
                }
                
                /**
                 * @function [销毁功能函数]
                 */
                data.fun.$destroy = function() {
                    if (data.info.timer) {
                        $timeout.cancel(data.info.timer);
                    }
                }
                data.info.timer = $timeout(function() {
                    $scope.data.element = document.getElementById('tip-directive-js-' + $scope.data.info.uuid);
                    angular.element($scope.data.element).append($scope.input);
                })
                $scope.$on('$destroy', data.fun.$destroy);
            }
        };
    }]);
})();
