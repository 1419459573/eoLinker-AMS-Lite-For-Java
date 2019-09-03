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
     * @function [测试指令js] [Test instructions js]
     * @version  3.1.5
     * @service  $filter [注入过滤器服务] [Inject filter service]
     * @service  $timeout [注入$timeout服务] [Inject filter service]
     * @service  $rootScope [注入根作用域服务] [Inject rootScope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     * @param    version [版本：0个人 1企业] [Version: 0 Personal 1 Business]
     * @param    auth [验证类型] [Authentication type]
     * @param    json [转json参数] [Turn json parameters]
     * @param    message [值可能性object类集（可选）] [Value Possibility object Class Set (optional)]
     * @param    result [双向绑定测试后返回结果] [Return the result after bidirectional binding test]
     * @param    detail [双向绑定测试初始化getApi内容] [Bidirectional binding test initializes getApi content]
     * @param    format [双向绑定格式整理内容] [Two-way binding format]
     * @param    testForm [双向绑定基本表单信息是否填写完整] [Bind the basic form information is complete]
     * @param    info [双向绑定基本的路由信息] [Bind the basic routing information]
     * @param    isPlug [双向绑定是否为插件（用于对界面有无插件的差异性显示）] [Whether the two-way binding for the plug-in (for the interface with or without plug-in differences show)]
     * @param    envParam [环境变量全局参数数组] [Environment variable global parameter array]
     */
    angular.module('eolinker.directive')

        .directive('testDirective', ['$window', '$filter', '$timeout', '$rootScope', 'ApiManagementResource', 'CODE', function($window, $filter, $timeout, $rootScope, ApiManagementResource, CODE) {
            return {
                restrict: 'A',
                transclude: true,
                replace: true,
                template: '<div>' + '<button class="eo-button-info " data-ng-click="test()" >' + ' <span class="iconfont icon-fasong" ng-class="{\'hidden\':send.disable}"></span> {{send.disable?data.info.stop&nbsp;+(send.countdown>0?send.countdown:""):data.info.send}}' + '</button>' + '<div class="hidden" id="plug-in-result-js"></div>' + '<div class="hidden" id="plug-in-js">{"method":{{detail.baseInfo.type}},"requestInfo":{{message}},"env":{{envParam}},"formDataToJson":{"checkbox":{{json.checkbox}},"raw":{{message.params|paramLevelToJsonFilter}}},"auth":{{auth}},"script":{{script}}}</div>' + '</div>',
                scope: {
                    version: '@',
                    auth: '=',
                    json: '=',
                    message: '=',
                    result: '=',
                    detail: '=',
                    format: '=',
                    testForm: '=',
                    info: '=',
                    isPlug: '=',
                    envParam: '=',
                    addHistory: '&',
                    script:'<'
                },
                link: function($scope, elem, attrs, ctrl) {
                    var countdown = null;
                    var templateCountdown = null;
                    var timer = null;
                    $scope.send = {
                        countdown: '',
                        disable: false
                    }
                    $scope.data = {
                        info: {
                            stop: $filter('translate')('350'),
                            send: $filter('translate')('351'),
                        },
                    }
                    var data = {
                        fun: {
                            restfulSet: null
                        }
                    }

                    /**
                     * @function [环境变量全局参数重构功能函数] [Global Parameter Reconstruction of Environment Variables]
                     * @param    {[obj]}   origin [原始值 Original value]
                     * @return   {[obj]}          [重构后的值 Reconstructed value]
                     */
                    var envSet = function(origin) {
                        if ($scope.envParam.length > 0) {
                            var templateResult = {};
                            angular.copy(origin, templateResult);
                            angular.forEach($scope.envParam, function(val, key) {
                                templateResult.URL = templateResult.URL.replace(eval('/({{' + val.paramKey + '}})/g'), val.paramValue);
                                angular.forEach(templateResult.headers, function(childVal, childKey) {
                                    childVal.headerValue = childVal.headerValue.replace(eval('/({{' + val.paramKey + '}})/g'), val.paramValue);
                                    childVal.headerName = childVal.headerName.replace(eval('/({{' + val.paramKey + '}})/g'), val.paramValue);
                                })
                                angular.forEach(templateResult.params, function(childVal, childKey) {
                                    childVal.paramKey = childVal.paramKey.replace(eval('/({{' + val.paramKey + '}})/g'), val.paramValue);
                                    childVal.paramInfo = (childVal.paramInfo || '').replace(eval('/({{' + val.paramKey + '}})/g'), val.paramValue);
                                })
                            })
                            return templateResult;
                        } else {
                            return origin;
                        }
                    }

                    /**
                     * @function [显示测试结果调用功能函数] [Show test result call]
                     * @param    {[obj]}   testHistory [测试历史] [Test history]
                     * @param    {[obj]}   data        [测试数据] [Test Data]
                     */
                    var showTestResult = function(testHistory, data) {
                        if ($scope.send.disable) {
                            if (data.statusCode == CODE.COMMON.SUCCESS) {
                                $scope.result = {
                                    menuType:'body',
                                    testHttpCode: data.testHttpCode,
                                    testDeny: data.testDeny,
                                    testResult: data.testResult,
                                    httpCodeType: data.testHttpCode >= 100 && data.testHttpCode < 200 ? 1 : data.testHttpCode >= 200 && data.testHttpCode < 300 ? 2 : data.testHttpCode >= 300 && data.testHttpCode < 400 ? 3 : 4
                                };
                                var result = $scope.result.testResult.body;
                                testHistory.resultInfo = {
                                    headers: data.testResult.headers,
                                    body: data.testResult.body,
                                    httpCode: data.testHttpCode,
                                    testDeny: data.testDeny
                                };
                                testHistory.testID = data.testID;
                                testHistory.httpCodeType = data.testHttpCode >= 100 && data.testHttpCode < 200 ? 1 : data.testHttpCode >= 200 && data.testHttpCode < 300 ? 2 : data.testHttpCode >= 300 && data.testHttpCode < 400 ? 3 : 4;
                                var array = [];
                                array.push(testHistory);
                                $scope.detail.testHistory = array.concat($scope.detail.testHistory);
                                $scope.format.message = result;
                            } else {
                                $scope.result = {
                                    httpCodeType: 5,
                                    menuType:'body'
                                };
                                $scope.format.message = '';
                            }
                            $scope.result.hadTest = true;
                            clearInterval(countdown);
                            $scope.send.countdown = null;
                            $scope.send.disable = false;
                        }
                    }

                    /**
                     * @function [插件测试调用功能函数] [The plugin test calls the function function]
                     * @param    {[obj]}   testHistory [测试历史 Test history]
                     */
                    var plugTest = function(testHistory) {
                        var data = {};
                        var template = {
                            img: {
                                html: ''
                            }
                        }
                        try {
                            data = JSON.parse($filter('HtmlFilter')(document.getElementById('plug-in-result-js').innerText));
                        } catch (e) {
                            data = {
                                statusCode: '2xxxxx'
                            }
                        }
                        if (data.statusCode == CODE.COMMON.SUCCESS) {
                            $scope.result = {
                                menuType:'body',
                                testHttpCode: data.testHttpCode,
                                testDeny: data.testDeny,
                                testResult: data.testResult,
                                httpCodeType: data.testHttpCode >= 100 && data.testHttpCode < 200 ? 1 : data.testHttpCode >= 200 && data.testHttpCode < 300 ? 2 : data.testHttpCode >= 300 && data.testHttpCode < 400 ? 3 : 4
                            };
                            if (/image\/(jpg|jpeg|png|gif)/ig.test(JSON.stringify(data.testResult.headers))) {
                                template.img.html = '<img style="max-width:100%;" author="eolinker-frontend" src="' + data.testResult.body + '"/>';
                            }
                            var result = $scope.result.testResult.body;
                            testHistory.resultInfo = {
                                headers: data.testResult.headers,
                                body: template.img.html || ((typeof result == 'object') ? angular.toJson(data.testResult.body) : data.testResult.body),
                                httpCode: data.testHttpCode,
                                testDeny: data.testDeny
                            };
                            testHistory.testID = data.testID;
                            testHistory.httpCodeType = data.testHttpCode >= 100 && data.testHttpCode < 200 ? 1 : data.testHttpCode >= 200 && data.testHttpCode < 300 ? 2 : data.testHttpCode >= 300 && data.testHttpCode < 400 ? 3 : 4;
                            var array = [];
                            array.push(testHistory);
                            $scope.detail.testHistory = array.concat($scope.detail.testHistory);
                            if (typeof result == 'object') {
                                $scope.format.message = angular.toJson(result);
                            } else {
                                $scope.format.message = template.img.html || result;
                            }
                            $scope.addHistory({
                                arg: {
                                    history: testHistory
                                }
                            });
                        } else {
                            $scope.result = {
                                menuType:'body',
                                httpCodeType: 5
                            };
                            if (data.errorText) {
                                $scope.format.message = data.errorText;
                            } else {
                                $scope.format.message = '';
                            }
                        }
                        $scope.result.hadTest = true;
                        clearInterval(templateCountdown);
                        clearInterval(countdown);
                        $scope.send.countdown = null;
                        $scope.send.disable = false;
                        $scope.$apply();
                    }

                    /**
                     * @function [服务器测试调用功能函数] [Server test call]
                     */
                    var serverTest = function() {
                        var template = {
                            env: envSet($scope.message),
                            restfulObject: {
                                hadFilterParams: []
                            }
                        }
                        if (!$scope.send.disable) {
                            var info = {
                                apiProtocol: $scope.message.httpHeader,
                                URL: $scope.message.URL,
                                headers: {},
                                params: {},
                            }
                            if (/(http:\/\/)/.test(info.URL.substring(0, 7))) {
                                info.URL = info.URL.substring(7);
                            } else if (/(https:\/\/)/.test(info.URL.substring(0, 8))) {
                                info.URL = info.URL.substring(8);
                            }
                            info = envSet(info);
                            var testHistory = {
                                requestInfo: {
                                    apiProtocol: info.apiProtocol,
                                    URL: info.URL,
                                    headers: [],
                                    params: [],
                                    method: $scope.detail.baseInfo.type == '0' ? 'POST' : $scope.detail.baseInfo.type == '1' ? 'GET' : $scope.detail.baseInfo.type == '2' ? 'PUT' : $scope.detail.baseInfo.type == '3' ? 'DELETE' : $scope.detail.baseInfo.type == '4' ? 'HEAD' : $scope.detail.baseInfo.type == '5' ? 'OPTIONS' : 'PATCH',
                                    methodType: $scope.detail.baseInfo.type,
                                    requestType: ($scope.json.checkbox && $scope.message.requestType != '1' && /0|2|6/.test($scope.detail.baseInfo.type)) ? 1 : $scope.message.requestType
                                }
                            };
                            if ($scope.testForm.$valid) {
                                angular.forEach(template.env.headers, function(val, key) {
                                    if (val.checkbox) {
                                        if (!!val.headerName) {
                                            info.headers[val.headerName] = val.headerValue;
                                            var history = {
                                                name: val.headerName,
                                                value: val.headerValue
                                            }
                                            testHistory.requestInfo.headers.push(history);
                                        }
                                    }
                                });
                                switch ($scope.auth.status) {
                                    case '1':
                                        {
                                            info.headers['Authorization'] = $filter('base64Filter')($scope.auth.basicAuth.username + ':' + $scope.auth.basicAuth.password);
                                            testHistory.requestInfo.headers.push({
                                                name: 'Authorization',
                                                value: $filter('base64Filter')($scope.auth.basicAuth.username + ':' + $scope.auth.basicAuth.password)
                                            });
                                            break;
                                        }
                                }
                                switch ($scope.message.requestType) {
                                    case '0':
                                        {
                                            if ($scope.json.checkbox && /0|2|6/.test($scope.detail.baseInfo.type)) {
                                                info.params = testHistory.requestInfo.params = $filter('paramLevelToJsonFilter')(template.env.params);
                                            } else {
                                                angular.forEach(template.env.params, function(val, key) {
                                                    if (val.checkbox) {
                                                        if (!!val.paramKey) {
                                                            info.params[val.paramKey] = val.paramInfo;
                                                            var history = {
                                                                key: val.paramKey,
                                                                value: val.paramInfo
                                                            }
                                                            testHistory.requestInfo.params.push(history);
                                                        }
                                                    }
                                                });
                                                info.params = angular.toJson(info.params);
                                            }
                                            break;
                                        }
                                    case '1':
                                        {
                                            info.params = testHistory.requestInfo.params = $scope.message.raw;
                                            break;
                                        }
                                    case '2':
                                        {
                                            angular.forEach(template.env.params, function(val, key) {
                                                if (val.checkbox) {
                                                    if (val.paramKey) {
                                                        if (info.URL.trim().indexOf('{' + val.paramKey + '}') > -1) {
                                                            info.URL = info.URL.replace(eval('/(\\\{' + val.paramKey + '\\\})/g'), val.paramInfo);
                                                        } else {
                                                            template.restfulObject.hadFilterParams.push(val);
                                                            info.params[val.paramKey] = val.paramInfo;
                                                            var history = {
                                                                key: val.paramKey,
                                                                value: val.paramInfo
                                                            }
                                                            testHistory.requestInfo.params.push(history);
                                                        }
                                                    }
                                                }
                                            });
                                            if ($scope.json.checkbox && /0|2|6/.test($scope.detail.baseInfo.type)) {
                                                info.params = testHistory.requestInfo.params = $filter('paramLevelToJsonFilter')(template.restfulObject.hadFilterParams);
                                            } else {
                                                info.params = angular.toJson(info.params);
                                            }
                                            testHistory.requestInfo.URL = info.URL;
                                            break;
                                        }
                                }
                                var message = {
                                    apiProtocol: info.apiProtocol,
                                    URL: info.URL,
                                    headers: angular.toJson(info.headers),
                                    params: info.params,
                                    apiID: $scope.info.apiID,
                                    projectID: $scope.info.projectID,
                                    requestType: testHistory.requestInfo.requestType
                                }
                                var type = $scope.detail.baseInfo.type;
                                testHistory.testTime = $filter('currentTimeFilter')();
                                var result = {};
                                $scope.send.countdown = 0;
                                $scope.send.disable = true;
                                countdown = setInterval(function() {
                                    $scope.send.countdown++;
                                    $scope.$digest(); // 通知视图模型的变化 Notification changes to the view model
                                }, 1000);
                                switch ($scope.detail.baseInfo.type) {
                                    case '0':
                                        ApiManagementResource.Test.Post(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '1':
                                        ApiManagementResource.Test.Get(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '2':
                                        ApiManagementResource.Test.Put(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '3':
                                        ApiManagementResource.Test.Delete(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '4':
                                        ApiManagementResource.Test.Head(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '5':
                                        ApiManagementResource.Test.Options(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                    case '6':
                                        ApiManagementResource.Test.Patch(message).$promise.then(function(data) {
                                            showTestResult(testHistory, data);
                                        })
                                        break;
                                }
                            }
                        } else {
                            clearInterval(countdown);
                            $scope.send.countdown = null;
                            $scope.send.disable = false;
                        }
                    }

                    /**
                     * @function [绑定click，执行测试功能函数] [Bind the click, perform the test]
                     */
                    $scope.test = function() {
                        var template = {
                            env: envSet($scope.message),
                            modal: {
                                html: ''
                            },
                            restfulObject: {
                                hadFilterParams: []
                            }
                        }
                        if (!$scope.send.disable) {
                            if ($window.plug && $window.plug.type == "application/eolinker") {
                                document.getElementById('plug-in-result-js').innerText = '';
                                var info = {
                                    apiProtocol: $scope.message.httpHeader,
                                    URL: $scope.message.URL,
                                    headers: {},
                                    params: {},
                                }
                                if (/(http:\/\/)/.test(info.URL.substring(0, 7))) {
                                    info.URL = info.URL.substring(7);
                                } else if (/(https:\/\/)/.test(info.URL.substring(0, 8))) {
                                    info.URL = info.URL.substring(8);
                                }
                                var testHistory = {
                                    requestInfo: {
                                        apiProtocol: info.apiProtocol,
                                        URL: template.env.URL,
                                        headers: [],
                                        params: [],
                                        method: $scope.detail.baseInfo.type == '0' ? 'POST' : $scope.detail.baseInfo.type == '1' ? 'GET' : $scope.detail.baseInfo.type == '2' ? 'PUT' : $scope.detail.baseInfo.type == '3' ? 'DELETE' : $scope.detail.baseInfo.type == '4' ? 'HEAD' : $scope.detail.baseInfo.type == '5' ? 'OPTIONS' : 'PATCH',
                                        methodType: $scope.detail.baseInfo.type,
                                        requestType: ($scope.json.checkbox && $scope.message.requestType != '1' && /0|2|6/.test($scope.detail.baseInfo.type)) ? 1 : $scope.message.requestType
                                    }
                                };
                                if ($scope.testForm.$valid) {
                                    angular.forEach(template.env.headers, function(val, key) {
                                        if (val.checkbox) {
                                            if (!!val.headerName) {
                                                info.headers[val.headerName] = val.headerValue;
                                                var history = {
                                                    name: val.headerName,
                                                    value: val.headerValue
                                                }
                                                testHistory.requestInfo.headers.push(history);
                                            }
                                        }
                                    });
                                    switch ($scope.auth.status) {
                                        case '1':
                                            {
                                                testHistory.requestInfo.headers.push({
                                                    name: 'Authorization',
                                                    value: $filter('base64Filter')($scope.auth.basicAuth.username + ':' + $scope.auth.basicAuth.password)
                                                });
                                                break;
                                            }
                                    }
                                    switch ($scope.message.requestType) {
                                        case '0':
                                            {
                                                if ($scope.json.checkbox && /0|2|6/.test($scope.detail.baseInfo.type)) {
                                                    testHistory.requestInfo.params = $filter('paramLevelToJsonFilter')(template.env.params);
                                                } else {
                                                    angular.forEach(template.env.params, function(val, key) {
                                                        if (val.checkbox) {
                                                            if (!!val.paramKey) {
                                                                var history = {
                                                                    key: val.paramKey,
                                                                    value: val.paramInfo
                                                                }
                                                                testHistory.requestInfo.params.push(history);
                                                            }
                                                        }
                                                    });
                                                }
                                                break;
                                            }
                                        case '1':
                                            {
                                                testHistory.requestInfo.params = $scope.message.raw;
                                                break;
                                            }
                                        case '2':
                                            {
                                                angular.forEach(template.env.params, function(val, key) {
                                                    if (val.checkbox) {
                                                        if (val.paramKey) {
                                                            if (info.URL.trim().indexOf('{' + val.paramKey + '}') > -1) {
                                                                info.URL = info.URL.replace(eval('/(\\\{' + val.paramKey + '\\\})/g'), val.paramInfo);
                                                            } else {
                                                                template.restfulObject.hadFilterParams.push(val);
                                                                var history = {
                                                                    key: val.paramKey,
                                                                    value: val.paramInfo
                                                                }
                                                                testHistory.requestInfo.params.push(history);
                                                            }
                                                        }
                                                    }
                                                });
                                                if ($scope.json.checkbox && /0|2|6/.test($scope.detail.baseInfo.type)) {
                                                    testHistory.requestInfo.params = $filter('paramLevelToJsonFilter')(template.restfulObject.hadFilterParams);
                                                }
                                                testHistory.requestInfo.URL = info.URL;
                                                break;
                                            }
                                    }
                                    var type = $scope.detail.baseInfo.type;
                                    testHistory.testTime = $filter('currentTimeFilter')();
                                    var result = {};
                                    $scope.send.countdown = 0;
                                    $scope.send.disable = true;
                                    templateCountdown = setInterval(function() {
                                        if (!!document.getElementById('plug-in-result-js').innerText) {
                                            plugTest(testHistory);
                                        }
                                    }, 10);
                                    countdown = setInterval(function() {
                                        $scope.send.countdown++;
                                        $scope.$digest(); // 通知视图模型的变化
                                        if ($scope.send.countdown == 60) {
                                            $scope.result = {
                                                httpCodeType: 5,
                                                menuType:'body'
                                            };
                                            $scope.format.message = '';
                                            $scope.isJson = false;
                                            $scope.result.hadTest = true;
                                            clearInterval(countdown);
                                            clearInterval(templateCountdown);
                                            $scope.send.countdown = null;
                                            $scope.send.disable = false;
                                            $scope.$digest();
                                        }
                                    }, 1000);
                                }
                            } else {
                                serverTest();
                            }
                        } else {
                            clearInterval(templateCountdown);
                            clearInterval(countdown);
                            $scope.send.countdown = null;
                            $scope.send.disable = false;
                        }
                    }

                    /**
                     * @function [销毁页面时销毁计时器] [Destroy the timer when the page is destroyed]
                     */
                    $scope.$on('$destroy', function() {
                        if (timer) {
                            $timeout.cancel(timer);
                        }
                    });

                    /**
                     * @function [路由开始转换时清除计时器] [The timer clears the timer when the route starts to transition]
                     */
                    $scope.$on('$stateChangeStart', function() {
                        if (!!templateCountdown) {
                            clearInterval(templateCountdown);
                        }
                        if (!!countdown) {
                            clearInterval(countdown);
                        }
                    })
                }
            };
        }]);
})();