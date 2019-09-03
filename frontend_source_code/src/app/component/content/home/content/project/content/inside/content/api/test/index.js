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
     * @function [api测试模块相关js] [api test module related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootScope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  HomeProject_Common_Service [注入HomeProject_Service服务] [Injection HomeProject_Common_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     * @constant HTTP_CONSTANT [注入HTTP相关常量集] [inject HTTP related constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.api.test', {
                    url: '/test?groupID?childGroupID?grandSonGroupID?apiID',
                    template: '<home-project-inside-api-test power-object="$ctrl.powerObject" ></home-project-inside-api-test>'
                });
        }])
        .component('homeProjectInsideApiTest', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/api/test/index.html',
            bindings: {
                powerObject: '<',
            },
            controller: homeProjectInsideApiTestController
        })

    homeProjectInsideApiTestController.$inject = ['$window', '$scope', '$rootScope', 'ApiManagementResource', '$state', 'Cache_CommonService', 'HomeProject_Common_Service', 'HomeProjectDefaultApi_Service', '$filter', 'CODE', 'HTTP_CONSTANT'];

    function homeProjectInsideApiTestController($window, $scope, $rootScope, ApiManagementResource, $state, Cache_CommonService, HomeProject_Common_Service, HomeProjectDefaultApi_Service, $filter, CODE, HTTP_CONSTANT) {
        var vm = this;
        vm.data = {
            service: {
                home: HomeProject_Common_Service,
                default: HomeProjectDefaultApi_Service,
                $window: $window
            },
            constant: {
                requestHeader: HTTP_CONSTANT.REQUEST_HEADER
            },
            info: {
                menuType:'body',
                template: {
                    envModel: []
                },
                uri: {
                    isFocus: false
                },
                header: {
                    type: '0'
                },
                format: {
                    isJson: true,
                    message: ''
                },
                response: {
                    menuType:'body',
                    httpCodeType: 2,
                    hadTest: false
                },
                toJson: {
                    checkbox: false,
                    raw: ''
                },
                auth: {
                    status: '0',
                    basicAuth: {
                        username: '',
                        password: ''
                    }
                },
                filter: {
                    shrink: $filter('translate')('012100010'),
                    open: $filter('translate')('012100011'),
                },
                script:{},
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID: $state.params.grandSonGroupID,
                    apiID: $state.params.apiID
                },
                response: {
                    apiInfo: {}
                }
            },
            fun: {
                addHistory: null,
                blurInput: null,
                expressionBuilder: null, 
                uriBlur: null, 
                delete: null, 
                recover: null, 
                deleteCompletely: null, 
                headerList: {
                    add: null, 
                    delete: null, 
                },
                requestList: {
                    add: null, 
                    delete: null, 
                },
                testList: {
                    enter: null, 
                    delete: null, 
                    clear: null, 
                },
                window: null, 
                changeType: null, 
                import: null, 
                last: null,
                json: null,  
            },
            assistantFun: {
                init: null 
            }
        }
        /**
         * @function [添加测试历史] [Add test history]
         */
        vm.data.fun.addHistory = function(arg) {
            var template = {
                request: {
                    // projectID: vm.data.interaction.request.projectID,
                    requestInfo: JSON.stringify(arg.history.requestInfo),
                    resultInfo: JSON.stringify(arg.history.resultInfo),
                    // testTime: arg.history.testTime,
                    apiID:vm.data.interaction.request.apiID,
                    beforeInject: vm.data.info.script.before,
                    afterInject: vm.data.info.script.after
                }
            }
            ApiManagementResource.Test.AddHistory(template.request).$promise.then(function(response) {
                arg.history.testID = response.testID;
            })
        }
        
        /**
         * @function [构造器失焦状态检测] [Constructor out of focus state detection]
         */
        vm.data.fun.json = function() {
            vm.data.info.toJson.checkbox = !vm.data.info.toJson.checkbox;
        }

        /**
         * @function [最后一个item 输入框内容改变功能函数] [The contents of the last header item are changed]
         */
        vm.data.fun.last = function(status, arg) {
            if (arg.$last) {
                switch (status) {
                    case 'header':
                        {
                            vm.data.fun.headerList.add();
                            break;
                        }
                    case 'request':
                        {
                            vm.data.fun.requestList.add();
                            break;
                        }
                }

            }
        }

        /**
         * @function [添加头部功能函数] [Add head]
         */
        vm.data.fun.headerList.add = function() {
            var info = {
                "headerName": '',
                "headerValue": '',
                "checkbox": true
            }
            vm.data.service.home.envObject.object.model.headers.push(info);
        }

        /**
         * @function [删除头部功能函数] [Remove the head]
         */
        vm.data.fun.headerList.delete = function(arg) {
            vm.data.service.home.envObject.object.model.headers.splice(arg.$index, 1);
        }

        /**
         * @function [添加请求参数功能函数] [Add the request parameters]
         */
        vm.data.fun.requestList.add = function() {
            var info = {
                "type": '0',
                "paramType": "0",
                "paramKey": "",
                "paramInfo": "",
                "checkbox": true,
                "paramValueQuery": []
            }
            vm.data.service.home.envObject.object.model.params.push(info);
            vm.submited = false;
        }

        /**
         * @function [删除请求参数功能函数] [Delete the request parameter]
         */
        vm.data.fun.requestList.delete = function(arg) {
            vm.data.service.home.envObject.object.model.params.splice(arg.$index, 1);
        }

        /**
         * @function [新开窗口] [New window]
         */
        vm.data.fun.window = function() {
            var template = {
                window: window.open()
            }
            if (vm.data.info.format.message) {
                template.window.document.open();
                template.window.document.write(vm.data.info.format.message);
                template.window.document.close();
            }
        }

        /**
         * @function [清空功能函数] [clear]
         */
        vm.data.fun.testList.clear = function(arg) {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: vm.data.interaction.request.apiID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100324'), false, $filter('translate')('012100231'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Test.DeleteAllHistory(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100051'), 'success');
                                        vm.data.interaction.response.apiInfo.testHistory = [];
                                        break;
                                    }
                            }
                        })

                }
            });
        }

        /**
         * @function [删除测试记录功能函数] [Delete the test record]
         */
        vm.data.fun.testList.delete = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    testID: arg.item.testID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100052'), false, $filter('translate')('012100042'), {}, function(callback) {
                if (callback) {
                    if (arg.item.testID) {
                        ApiManagementResource.Test.DeleteHistory(template.request).$promise
                            .then(function(response) {
                                switch (response.statusCode) {
                                    case CODE.COMMON.SUCCESS:
                                        {
                                            $rootScope.InfoModal($filter('translate')('012100053'), 'success');
                                            vm.data.interaction.response.apiInfo.testHistory.splice(arg.$index, 1);
                                            break;
                                        }
                                }
                            })
                    } else {
                        vm.data.interaction.response.apiInfo.testHistory.splice(arg.$index, 1);
                    }

                }
            });

        }

        /**
         * @function [进入测试记录功能函数] [Go to test record]
         */
        vm.data.fun.testList.enter = function(arg) {
            arg = arg || {};
            vm.data.service.home.envObject.object.model.URL = arg.item.requestInfo.URL;
            vm.data.service.home.envObject.object.model.headers = [];
            vm.data.service.home.envObject.object.model.params = [];
            vm.data.service.home.envObject.object.model.raw = '';
            vm.data.service.home.envObject.object.model.requestType = arg.item.requestInfo.requestType;
            vm.data.service.home.envObject.object.model.httpHeader = arg.item.requestInfo.apiProtocol;
            var info = {};
            var template = {
                img: {
                    html: null
                }
            }
            vm.data.info.response = {
                testHttpCode: arg.item.resultInfo.httpCode,
                testDeny: arg.item.resultInfo.testDeny,
                testResult: {
                    headers: arg.item.resultInfo.headers
                },
                httpCodeType: arg.item.httpCodeType,
                hadTest: true,
                menuType:'body'
            };
            vm.data.info.script={
                type:'0',
                before:arg.item.beforeInject,
                after:arg.item.afterInject
            }
            angular.forEach(arg.item.requestInfo.headers, function(val, key) {
                info = {
                    headerName: val.name,
                    headerValue: val.value
                };
                vm.data.service.home.envObject.object.model.headers.push(info);
            });
            if (/image\/(jpg|jpeg|png|gif)/ig.test(JSON.stringify(arg.item.resultInfo.headers))) {
                template.img.html = '<img style="width:100%;" author="eolinker-frontend" src="' + arg.item.resultInfo.body + '"/>';
            }
            if (vm.data.service.home.envObject.object.model.requestType != '1') {
                angular.forEach(arg.item.requestInfo.params, function(val, key) {
                    info = {
                        checkbox: true,
                        paramKey: val.key,
                        paramInfo: val.value,
                        paramValueQuery: []
                    };
                    vm.data.service.home.envObject.object.model.params.push(info);
                });
            } else {
                vm.data.service.home.envObject.object.model.params = [];
                vm.data.service.home.envObject.object.model.raw = arg.item.requestInfo.params;
            }
            vm.data.interaction.response.apiInfo.baseInfo.type = '' + arg.item.requestInfo.methodType;
            vm.data.info.format.message = template.img.html || arg.item.resultInfo.body;
            vm.data.fun.headerList.add();
            vm.data.fun.requestList.add();
            vm.data.info.header = {
                type: '0'
            }
            vm.data.info.auth = {
                status: '0',
                basicAuth: {
                    username: '',
                    password: ''
                }
            }
        }

        /**
         * @function [构造器失焦状态检测] [Constructor out of focus state detection]
         */
        vm.data.fun.blurInput = function(arg) {
            setTimeout(function() { //进行延时处理，时间单位为千分之一秒 Delay processing, the time unit is one thousandth of a second
                arg.focus.isFocus = false;
                $scope.$digest();
            }, 500)
        }

        /**
         * @function [构造器启动功能函数] [The constructor starts]
         */
        vm.data.fun.expressionBuilder = function(data) {
            data.item.expressionBuilderObject = data.item.expressionBuilderObject || {
                request: {},
                response: {}
            }
            switch (data.$index) {
                case 0:
                    {
                        data.item.expressionBuilderObject.request.constant = data.item.URL;
                        break;
                    }
                case 1:
                    {
                        data.item.expressionBuilderObject.request.constant = data.item.headerValue;
                        break;
                    }
                case 2:
                    {
                        data.item.expressionBuilderObject.request.constant = data.item.paramInfo;
                        break;
                    }
            }

            $rootScope.ExpressionBuilderModal(data.item.expressionBuilderObject, function(callback) {
                switch (data.$index) {
                    case 0:
                        {
                            data.item.URL = callback.response.result || data.item.URL;
                            break;
                        }
                    case 1:
                        {
                            data.item.headerValue = callback.response.result || data.item.headerValue;
                            break;
                        }
                    case 2:
                        {
                            data.item.paramInfo = callback.response.result || data.item.paramInfo;
                            break;
                        }
                }
                data.item.expressionBuilderObject = callback;
            });
        }

        /**
         * @function [测试地址input失焦触发功能函数] [Test address input]
         */
        vm.data.fun.uriBlur = function() {
            if (!vm.data.service.home.envObject.object.model) return;
            if (/(https:\/\/)/i.test(vm.data.service.home.envObject.object.model.URL)) {
                vm.data.service.home.envObject.object.model.httpHeader = '1';
            } else if (/(http:\/\/)/i.test(vm.data.service.home.envObject.object.model.URL)) {
                vm.data.service.home.envObject.object.model.httpHeader = '0';
            }
        }

        /**
         * @function [导入功能函数] [Import]
         */
        vm.data.fun.import = function(arg) {
            var template = {
                $index: this.$parent.$index,
                reader: null
            }
            vm.data.service.home.envObject.object.model.params[template.$index].paramInfo = '';
            for (var i = 0; i < arg.file.length; i++) {
                var val = arg.file[i];
                if (val.size > 2 * 1024 * 1024) {
                    vm.data.service.home.envObject.object.model.params[template.$index].paramInfo = '';
                    vm.data.service.home.envObject.object.model.params[template.$index.files] = [];
                    $rootScope.InfoModal($filter('translate')('012100054'), 'error');
                    break;
                } else {
                    vm.data.service.home.envObject.object.model.params[template.$index].paramInfo = val.name + ',' + vm.data.service.home.envObject.object.model.params[template.$index].paramInfo;
                    template.reader = new FileReader(); //new test
                    template.reader.readAsDataURL(val);
                    vm.data.service.home.envObject.object.model.params[template.$index].files = [];
                    template.reader.onload = function(_default) {
                        vm.data.service.home.envObject.object.model.params[template.$index].files.push(this.result);
                    }
                }

            }
            vm.data.service.home.envObject.object.model.params[template.$index].paramInfo = vm.data.service.home.envObject.object.model.params[template.$index].paramInfo.slice(0, vm.data.service.home.envObject.object.model.params[template.$index].paramInfo.length - 1);
            $scope.$digest();
        }

        /**
         * @function [辅助初始化功能函数] [Auxiliary initialization]
         */
        vm.data.assistantFun.init = function() {
            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012100055') + vm.data.interaction.response.apiInfo.baseInfo.apiName, $filter('translate')('012100164'), $state.params.projectName, 'API开发管理'] });
            vm.data.interaction.response.apiInfo.testHistory = vm.data.interaction.response.apiInfo.testHistory || [];
            vm.data.info.script = {
                type: '0',
                before: vm.data.interaction.response.apiInfo.baseInfo.beforeInject,
                after: vm.data.interaction.response.apiInfo.baseInfo.afterInject
            }
            angular.forEach(vm.data.interaction.response.apiInfo.testHistory, function(val, key) {
                try {
                    if (val.requestInfo.constructor != Object) {
                        val.requestInfo = {
                            apiProtocol: '0',
                            method: 'error',
                            URL: $filter('translate')('012100056'),
                            requestType: '0'
                        };
                    }
                    if (val.resultInfo == null) {
                        val.resultInfo = {
                            'body': 'error',
                            'headers': [],
                            'httpCode': 500,
                            'testDeny': 0
                        };
                    }
                    val.requestInfo.methodType = val.requestInfo.method == 'POST' ? 0 : val.requestInfo.method == 'GET' ? 1 : val.requestInfo.method == 'PUT' ? 2 : val.requestInfo.method == 'DELETE' ? 3 : val.requestInfo.method == 'HEAD' ? 4 : val.requestInfo.method == 'OPTIONS' ? 5 : 6;
                    val.httpCodeType = val.resultInfo.httpCode >= 100 && val.resultInfo.httpCode < 200 ? 1 : val.resultInfo.httpCode >= 200 && val.resultInfo.httpCode < 300 ? 2 : val.resultInfo.httpCode >= 300 && val.resultInfo.httpCode < 400 ? 3 : 4;
                    val.requestInfo.URL = (val.requestInfo.URL || '').replace('http://', '');
                } catch (e) {
                    console.log($filter('translate')('012100057'));
                }
            })
            vm.data.service.home.envObject.object.model.URL = vm.data.interaction.response.apiInfo.baseInfo.apiURI;
            vm.data.service.home.envObject.object.model.params = vm.data.interaction.response.apiInfo.requestInfo || [];
            vm.data.service.home.envObject.object.model.httpHeader = '' + vm.data.interaction.response.apiInfo.baseInfo.apiProtocol;
            vm.data.service.home.envObject.object.model.requestType = '' + vm.data.interaction.response.apiInfo.baseInfo.apiRequestParamType;
            vm.data.service.home.envObject.object.model.raw = '' + vm.data.interaction.response.apiInfo.baseInfo.apiRequestRaw;
            vm.data.interaction.response.apiInfo.baseInfo.type = '' + vm.data.interaction.response.apiInfo.baseInfo.apiRequestType;
            angular.forEach(vm.data.interaction.response.apiInfo.requestInfo, function(val, key) {
                val.paramValueQuery = [];
                val.paramInfo = '';
                val.type = val.paramType;
                switch (val.paramNotNull) {
                    case '0':
                    case 0:
                        {
                            val.checkbox = true;
                            break;
                        }
                    default:
                        {
                            val.checkbox = false;
                            break;
                        }
                }
                angular.forEach(val.paramValueList, function(value, key) {
                    val.paramValueQuery.push(value.value);
                })
            });
            vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
            vm.data.fun.headerList.add();
            vm.data.fun.requestList.add();
            $scope.$emit('$translateferStation', { state: '$EnvInitReady', data: { status: 2, param: angular.toJson(vm.data.service.home.envObject.object.model), header: 'headers',additionalParams:'params', uri: 'URL' } });
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function() {
            var template = {
                cache: {
                    apiInfo: Cache_CommonService.get(),
                    testInfo: vm.data.service.home.apiTestObject.testInfo
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    groupID: vm.data.interaction.request.grandSonGroupID || vm.data.interaction.request.childGroupID || vm.data.interaction.request.groupID,
                    apiID: vm.data.interaction.request.apiID
                }
            }
            if (template.cache.testInfo) {
                vm.data.interaction.response.apiInfo = template.cache.testInfo.apiInfo;
                vm.data.info.template.envModel = template.cache.testInfo.reset;
                vm.data.service.home.envObject.object.model = template.cache.testInfo.message;
                vm.data.info.response = template.cache.testInfo.result;
                vm.data.info.format = template.cache.testInfo.format;
                $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('012100055') + vm.data.interaction.response.apiInfo.baseInfo.apiName, $filter('translate')('012100036'), $state.params.projectName, 'API开发管理'] });
                $scope.$emit('$translateferStation', { state: '$EnvInitReady', data: { status: 2, reset: 1, resetInfo: vm.data.info.template.envModel, param: angular.toJson(vm.data.service.home.envObject.object.model), header: 'headers', uri: 'URL' } });
            } else {
                vm.data.interaction.response.apiInfo = template.cache.apiInfo;
                if (vm.data.interaction.response.apiInfo) {
                    vm.data.service.home.envObject.object.model.headers = vm.data.interaction.response.apiInfo.headers || [];
                    vm.data.assistantFun.init();
                } else {
                    ApiManagementResource.Api.Detail(template.request).$promise.then(function(response) {
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    vm.data.interaction.response.apiInfo = response.apiInfo;
                                    vm.data.service.home.envObject.object.model.headers = vm.data.interaction.response.apiInfo.headerInfo || [];
                                    vm.data.assistantFun.init();
                                    break;
                                }
                            default:
                                {
                                    vm.data.interaction.response.apiInfo = {};
                                }
                        }
                    })
                }
            }
            $scope.importFile = vm.data.fun.import;
        }
        vm.data.fun.init();
        vm.$onInit = function() {
            $scope.$watch('$ctrl.data.service.home.envObject.object.model.URL', function() {
                vm.data.fun.uriBlur();
            })
            $scope.$on('$stateChangeStart', function() {
                vm.data.info.template.envModel.params = vm.data.service.home.envObject.object.model.params;
                vm.data.service.home.apiTestObject.fun.set({ object: { reset: vm.data.info.template.envModel, apiInfo: vm.data.interaction.response.apiInfo, message: vm.data.service.home.envObject.object.model, result: vm.data.info.response, format: vm.data.info.format } });
                Cache_CommonService.set(null);
            });
        }

    }
})();