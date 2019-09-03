(function () {
    'use strict';
    /*
     * author：广州银云信息科技有限公司
     * 上传文件重置指令js
     */
    angular.module('eolinker.directive')

        .directive('checkPlugDirective', ['$compile', function ($compile) {
            return {
                restrict: 'AE',
                scope: {
                    input: '='
                },
                link: function ($scope, elem, attrs, ctrl) {
                    var data = {
                        fun: {
                            init: null //初始话检测功能函数
                        }
                    }
                    data.fun.init = (function () {
                        if (navigator.mimeTypes['application/eolinker'] || (window.plug && window.plug.type == "application/eolinker")) {
                            $scope.input.useStatus = 1;
                            if ($scope.input.needVersion) {
                                try {

                                    $scope.input.version = window.plug.version;
                                    if ($scope.input.matchVersion) $scope.input.useStatus = $scope.input.version >= $scope.input.matchVersion;
                                    $scope.input.versionString = JSON.stringify(window.plug.version).split('').join('.');
                                    return;
                                } catch (e) {}
                            } else {
                                return;
                            }
                        }
                        angular.element(document.getElementById('plug-inner-script')).bind('DOMNodeInserted', function (e) {
                            $scope.input.useStatus = 1;
                            $scope.input.version = window.plug.version;
                            if ($scope.input.matchVersion) $scope.input.useStatus = $scope.input.version >= $scope.input.matchVersion;
                            $scope.input.versionString = JSON.stringify(window.plug.version).split('').join('.');
                            $scope.$root && $scope.$root.$$phase || $scope.$apply();
                        });
                    })()
                }
            };
        }]);
})();