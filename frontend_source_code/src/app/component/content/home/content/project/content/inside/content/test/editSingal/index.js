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
     * @function [自动化测试编辑单例页面] [Automated test editing singleton page]
     * @version  3.2.2
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  $window [注入window服务] [inject window service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject apiManagement API service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @service  Cache_CommonService [注入Cache_CommonService服务] [inject Cache_CommonService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .component('homeProjectInsideTestEditSingal', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/test/editSingal/index.html',
            controller: indexController
        })

    indexController.$inject = ['HTTP_CONSTANT', '$scope', '$rootScope', 'ApiManagementResource', 'CODE', '$state', 'Cache_CommonService', '$filter'];

    function indexController(HTTP_CONSTANT, $scope, $rootScope, ApiManagementResource, CODE, $state, Cache_CommonService, $filter) {
        var vm = this;
        vm.data = {
            constant: {
                requestHeader: HTTP_CONSTANT.REQUEST_HEADER
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    caseID: $state.params.caseID,
                    connID: $state.params.connID,
                    matchTextarea: '',
                    groupID:$state.params.groupID,
                    childGroupID:$state.params.childGroupID,
                    grandSonGroupID: $state.params.grandSonGroupID,
                    orderNumber:$state.params.orderNumber,
                    responseJson: []
                },
                response: {
                    caseInfo: null,
                    singalQuery: null
                }
            },
            info: {
                status: $state.params.status,
                header: {
                    type: '0'
                }
            },
            fun: {
                delete: null, 
                close: null, 
                changeType: null, 
                edit: null, 
                last: null
            }
        }
        var data = {
            service: {
                cache: Cache_CommonService
            },
            interaction: {
                response: {

                }
            },
            fun: {
                init: null, 
            }
        }

        /**
         * @function [编辑功能函数] [edit]
         */
        vm.data.fun.edit = function(status, arg) {
            switch (status) {
                case 'addChild':
                    {
                        arg.item.childList.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "childList": [],
                            "matchRule": '0',
                            "parent": arg.item.parent + arg.item.paramKey + '.'
                        })
                        break;
                    }
            }
        }
        /**
         * @function [删除功能函数] [delete]
         */
        vm.data.fun.delete = function(status, arg) {
            switch (status) {
                case 'header':
                    {
                        vm.data.interaction.response.caseInfo.caseData.headers.splice(arg.$index, 1);
                        break;
                    }
                case 'param':
                    {
                        vm.data.interaction.response.caseInfo.caseData.params.splice(arg.$index, 1);
                        break;
                    }
                case 'response':
                    {
                        vm.data.interaction.request.responseJson.splice(arg.$index, 1);
                        break;
                    }
                case 'responseChild':
                    {
                        arg.item.childList.splice(arg.$index, 1);
                        break;
                    }
            }
        }
        /**
         * @function [绑定功能函数] [bind]
         */
        vm.data.fun.bind = function(status, arg) {
            var template = {
                modal: {
                    query: []
                }
            }
            if (vm.data.interaction.response.singalQuery.length <= 0) {
                $rootScope.InfoModal($filter('translate')('01216236'), 'error');
                return;
            }
            if (vm.data.interaction.response.singalQuery[0].connID == parseInt(vm.data.interaction.request.connID)) {
                $rootScope.InfoModal($filter('translate')('01216236'), 'error');
                return;
            }
            for(var key in vm.data.interaction.response.singalQuery){
                var val=vm.data.interaction.response.singalQuery[key];
                if(val.connID == parseInt(vm.data.interaction.request.connID)){
                    break;
                }else{
                   template.modal.query.push(val); 
                }
            }
            $rootScope.ApiManagement_AutomatedTest_BindModal(template.modal, function(callback) {
                if (callback) {
                    if(/^\./.test(callback.bind))callback.bind=callback.bind.slice(1,callback.bind.length);
                    switch (status) {
                        case 'header':
                            {
                                arg.item.headerValue = (arg.item.headerValue||'') + '<' + 'response[' + callback.connID + '].' + callback.bind + '>.';
                                break;
                            }
                        case 'param':
                            {
                                arg.item.paramInfo = (arg.item.paramInfo||'') + '<' + 'response[' + callback.connID + '].' + callback.bind + '>.';
                                break;
                            }
                    }
                }

            })
        }
        vm.data.fun.last = function(status, arg) {
            if (!arg.$last) return;
            switch (status) {
                case 'header':
                    {
                        vm.data.interaction.response.caseInfo.caseData.headers.push({
                            "headerName": '',
                            "headerValue": '',
                            "checkbox": true
                        });
                        break;
                    }
                case 'param':
                    {
                        vm.data.interaction.response.caseInfo.caseData.params.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "checkbox": true,
                        });
                        break;
                    }
                case 'response':
                    {
                        vm.data.interaction.request.responseJson.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "childList": [],
                            "matchRule": '0',
                            "parent": '.'
                        });
                        break;
                    }
                case 'responseParam':
                    {
                        arg.item.childList.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "childList": [],
                            "matchRule": '0',
                            "parent": arg.item.parent + arg.item.paramKey + '.'
                        })
                        break;
                    }
                case 'all':
                    {

                        vm.data.interaction.response.caseInfo.caseData.headers = vm.data.interaction.response.caseInfo.caseData.headers || [];
                        vm.data.interaction.response.caseInfo.caseData.params = vm.data.interaction.response.caseInfo.caseData.params || [];
                        vm.data.interaction.response.caseInfo.caseData.headers.push({
                            "headerName": '',
                            "headerValue": '',
                            "checkbox": true
                        });
                        vm.data.interaction.response.caseInfo.caseData.params.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "checkbox": true,
                        });
                        vm.data.interaction.request.responseJson.push({
                            "paramKey": "",
                            "paramInfo": "",
                            "childList": [],
                            "matchRule": '0',
                            "parent": '.'
                        });
                        break;
                    }
            }
        }
        vm.data.fun.confirm = function() {
            if ($scope.ConfirmForm.$invalid) {
                vm.data.info.submited = true;
                $rootScope.InfoModal($filter('translate')('01216237'), 'error');
                return;
            }
            var template = {
                promise: null,
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    connID: vm.data.interaction.response.caseInfo.connID,
                    caseID: vm.data.interaction.request.caseID,
                    caseData: JSON.stringify(vm.data.interaction.response.caseInfo.caseData, function(key, val) {
                        if (/(\$index)|(default)|(paramNote)|(\$\$hashKey)|(isFocus)|(headerID)|(paramID)|(paramName)|(paramType)|(paramLimit)|(paramValue)|(paramNotNull)|(paramNotNull)/.test(key)) {
                            return undefined;
                        }
                        return val;
                    }),
                    statusCode: '',
                    orderNumber:vm.data.interaction.request.orderNumber,
                    matchType: vm.data.interaction.response.caseInfo.matchType,
                    matchRule: vm.data.interaction.response.caseInfo.matchType == 2 ? JSON.stringify(vm.data.interaction.request.responseJson, function(key, val) {
                        if (/(\$\$hashKey)/.test(key)) {
                            return undefined;
                        }
                        return val;
                    }) : vm.data.interaction.request.matchTextarea,
                    apiURI: vm.data.interaction.response.caseInfo.caseData.URL,
                    apiName: vm.data.interaction.response.caseInfo.apiName,
                    apiRequestType: vm.data.interaction.response.caseInfo.caseData.apiRequestType
                }
            }
            switch (vm.data.info.status) {
                case 'insert':
                case 'add':
                    {
                        template.request.statusCode = vm.data.interaction.response.caseInfo.statusCode != '0' ? vm.data.interaction.response.caseInfo.statusCode : vm.data.interaction.response.caseInfo.code;
                        template.promise = ApiManagementResource.AutomatedTestCaseSingle.Add(template.request).$promise;
                        template.promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216238'), 'success');
                                        vm.data.fun.back();
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216239'), 'error');
                                        break;
                                    }
                            }
                        });
                        break;
                    }
                case 'edit':
                    {
                        template.request.statusCode = vm.data.interaction.response.caseInfo.code;
                        template.promise = ApiManagementResource.AutomatedTestCaseSingle.Edit(template.request).$promise;
                        template.promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216240'), 'success');
                                        vm.data.fun.back();
                                        break;
                                    }
                                case '870000':
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216240'), 'success');
                                        vm.data.fun.back();
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01216239'), 'error');
                                        break;
                                    }
                            }
                        });
                        break;
                    }
            }
            return template.promise;
        };
        vm.data.fun.back = function() {
            var template = {
                uri: {
                    groupID: vm.data.interaction.request.groupID,
                    childGroupID: vm.data.interaction.request.childGroupID,
                    grandSonGroupID: vm.data.interaction.request.grandSonGroupID,
                    caseID: vm.data.interaction.request.caseID
                }
            }
            $state.go('home.project.inside.test.api', template.uri);
        };
        data.fun.init = (function() {
            var template = {
                cache: {
                    singCaseList: data.service.cache.get('singCaseList'),
                    apiInfo: data.service.cache.get('apiInfo'),
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    caseID: vm.data.interaction.request.caseID,
                    connID: vm.data.interaction.request.connID
                }
            }
            console.log(template.cache.apiInfo)
            if (template.cache.singCaseList) {
                vm.data.interaction.response.singalQuery = template.cache.singCaseList;
            } else {
                $rootScope.global.ajax.Query_AutomatedTestCaseSingle = ApiManagementResource.AutomatedTestCaseSingle.Query(template.request);
                $rootScope.global.ajax.Query_AutomatedTestCaseSingle.$promise.then(function(response) {
                    vm.data.interaction.response.singalQuery = response.singCaseList || [];
                })
            }
            switch (vm.data.info.status) {
                case 'insert':
                case 'add':
                    {
                        vm.data.interaction.response.caseInfo = {
                            caseData: { "auth": { "status": "0" }, "headers": [], "URL": "", "params": [], "httpHeader": "0", "requestType": "0", "methodType": "0", "apiRequestType": "0" },
                            statusCode: '200',
                            matchType: 3
                        };
                        if (template.cache.apiInfo) {
                            template.request.apiID = template.cache.apiInfo.apiID;
                            vm.data.interaction.response.caseInfo.apiName = template.cache.apiInfo.apiName;
                            vm.data.interaction.response.caseInfo.caseData.URL = template.cache.apiInfo.apiURI;
                            vm.data.interaction.response.caseInfo.caseData.apiRequestType=(template.cache.apiInfo.apiRequestType||0).toString();
                            ApiManagementResource.Api.Detail(template.request).$promise.then(function(response) {
                                vm.data.interaction.response.caseInfo.caseData.params = response.apiInfo.requestInfo;
                                vm.data.interaction.response.caseInfo.caseData.headers = response.apiInfo.headerInfo;
                                try {
                                    vm.data.interaction.response.caseInfo.matchType=response.apiInfo.resultInfo.length>0?2:3;
                                    vm.data.interaction.request.responseJson = $filter('paramLevelToNestFilter')(response.apiInfo.resultInfo);
                                } catch (e) {}
                                vm.data.fun.last('all', { $last: true });
                            })
                        } else {
                            vm.data.fun.last('all', { $last: true });
                        }
                        break;
                    }
                case 'edit':
                    {
                        $rootScope.global.ajax.Info_AutomatedTestCaseSingle = ApiManagementResource.AutomatedTestCaseSingle.Info(template.request);
                        $rootScope.global.ajax.Info_AutomatedTestCaseSingle.$promise.then(function(response) {
                            vm.data.interaction.response.caseInfo = response.singleCaseInfo || {
                                caseData: '{"auth":{"status":"0"},"headers": [],"URL": "","params": [],"httpHeader": "0","requestType": "0","methodType": "0","apiRequestType": "0"}',
                                statusCode: '200',
                                matchType: 3
                            }
                            switch (vm.data.interaction.response.caseInfo.matchType) {
                                case 2:
                                case '2':
                                    {
                                        vm.data.interaction.request.responseJson = vm.data.interaction.response.caseInfo.matchRule || [];
                                        break;
                                    }
                                default:
                                    {
                                        vm.data.interaction.request.matchTextarea = vm.data.interaction.response.caseInfo.matchRule;
                                        vm.data.fun.last('response', { $last: true });
                                    }
                            }
                            try {
                                vm.data.interaction.response.caseInfo.caseData = JSON.parse(vm.data.interaction.response.caseInfo.caseData);
                            } catch (e) {
                                vm.data.interaction.response.caseInfo.caseData = { "auth": { "status": "0" }, "headers": [], "URL": "", "params": [], "httpHeader": "0", "requestType": "0", "methodType": "0", "apiRequestType": "0" };
                            }
                            vm.data.interaction.response.caseInfo.code = vm.data.interaction.response.caseInfo.statusCode;
                        })
                        break;
                    }
            }
        })()
    }
})();
