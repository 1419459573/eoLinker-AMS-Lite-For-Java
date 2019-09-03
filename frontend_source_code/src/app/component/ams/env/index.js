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
     * @function [自动匹配组件] [自动匹配组件]
     * @version  3.1.9
     * @extends {string} authorityObject 权限
     * @extends {string} version 版本管理
     * @extends {string} envModel model页面相应内容
     * @extends {array} envQueryInit env list页相应返回显示apiURI函数
     * @extends {string} envParam 环境变量全局参数绑定数组
     * @extends {function} totalEnv 完整当前环境变量
     */
    angular.module('eolinker')
        .component('envAmsComponent', {
            templateUrl: 'app/component/ams/env/index.html',
            controller: indexController,
            bindings: {
                authorityObject: '<', 
                version: '@', 
                envModel: '=', 
                envQueryInit: '=', 
                envParam: '=', 
                totalEnv: '=' 
            }
        })

    indexController.$inject = ['$scope', 'Cache_CommonService', 'ApiManagementResource', '$sce', '$state', '$filter', '$timeout'];

    function indexController($scope, Cache_CommonService, ApiManagementResource, $sce, $state, $filter, $timeout) {
        var vm = this;
        vm.data = {
            info: {
                model: {},
                itemStatus: 'hidden'
            },
            interaction: {
                response: {
                    query: null
                }
            },
            fun: {
                click: null
            }
        }
        var data = {
            storage: {},
            service: {
                cache: Cache_CommonService
            },
            input: {
                status: 0,
                param: '',
                result: '',
                header: '',
                additionalParams: '',
                uri: ''
            },
            info: {
                timer: null,
                reset: false
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    companyID: $state.params.companyID
                }
            },
            fun: {
                init: null
            },
            assistantFun: {
                init: null
            }
        }

        vm.envQueryInit = function(attr) {
            return attr;
        };

        /**
         * @function [辅助初始化] [Auxiliary initialization]
         */
        data.assistantFun.init = function() {
            var envItem = null;
            var template = {
                cache: null
            }
            template.cache = data.storage[data.interaction.request.projectID];
            if (template.cache) {
                for (var key = 0; key < vm.data.interaction.response.query.length; key++) {
                    var val = vm.data.interaction.response.query[key];
                    if (val.envID == template.cache) {
                        envItem = val;
                        break;
                    }
                }
            }
            try {
                vm.totalEnv = envItem || {};
            } catch (e) {}
            vm.envParam = envItem ? envItem.paramList : [];
            if (envItem) {
                vm.data.info.model = envItem;
                switch (data.input.status) {
                    case 0:
                        {

                            if (envItem.frontURIList.length > 0) {
                                vm.envQueryInit = function(attr) {
                                    var result = attr;
                                    angular.forEach(envItem.paramList, function(val, key) {
                                        result = result.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue)
                                    })
                                    return envItem.frontURIList[0].uri + result;
                                };
                            } else {
                                vm.envQueryInit = function(attr) {
                                    var result = attr;
                                    angular.forEach(envItem.paramList, function(val, key) {
                                        result = result.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue)
                                    })
                                    return result;
                                };
                            }
                            break;
                        }
                    case 1:
                        {
                            var result = null;
                            data.input.result = data.input.param;
                            if (envItem.paramList.length > 0) {
                                var templateResult = {};
                                angular.copy(angular.fromJson(data.input.param), templateResult);
                                angular.forEach(envItem.paramList, function(val, key) {
                                    templateResult.baseInfo.apiURI = templateResult.baseInfo.apiURI.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                    angular.forEach(templateResult.headerInfo, function(val1, key1) {
                                        val1.headerName = val1.headerName.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                    })
                                    angular.forEach(templateResult.requestInfo, function(val1, key1) {
                                        val1.paramKey = val1.paramKey.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                        val1.paramKeyHtml = val1.paramKeyHtml.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                    })
                                    result = angular.toJson(templateResult);
                                })
                                data.input.result = result;
                            }
                            if (envItem.frontURIList.length > 0) {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.uri + '\":\"){1}/g'), '\"' + data.input.uri + '\":\"' + vm.data.info.model.frontURIList[0].uri);
                            } else {
                                data.input.result = data.input.result;
                            }
                            if (envItem.headerList.length > 0) {
                                var headerString = angular.toJson(envItem.headerList);
                                if (data.input.result.indexOf('\"' + data.input.header + '\":[]') > -1) {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1));
                                } else {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1) + ',');
                                }
                            }
                            vm.envModel = angular.fromJson(data.input.result);
                            vm.envModel.baseInfo.apiNoteHtml = $sce.trustAsHtml($filter('XssFilter')(vm.envModel.baseInfo.apiNote, {
                                onIgnoreTagAttr: function(tag, name, value, isWhiteAttr) {
                                    if (/(class)|(id)|(name)/.test(name)) {
                                        return name + '="' + value + '"';
                                    }
                                }
                            }));
                            break;
                        }
                    case 2:
                        {
                            if (envItem.frontURIList.length > 0) {
                                data.input.result = data.input.param.replace(eval('/(\"' + data.input.uri + '\":\"){1}/g'), '\"' + data.input.uri + '\":\"' + vm.data.info.model.frontURIList[0].uri);
                            } else {
                                data.input.result = data.input.param;
                            }
                            if (envItem.headerList.length > 0) {
                                var headerString = angular.toJson(envItem.headerList);
                                if (data.input.result.indexOf('\"' + data.input.header + '\":[]') > -1) {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1));
                                } else {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1) + ',');
                                }
                            }
                            if (envItem.additionalParamList.length > 0) {
                                var paramString = angular.toJson(envItem.additionalParamList);
                                if (data.input.result.indexOf('\"' + data.input.additionalParams + '\":[]') > -1) {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.additionalParams + '\":\\\[)/'), '\"' + data.input.additionalParams + '\":\[' + paramString.slice(1, paramString.length - 1));
                                } else {
                                    data.input.result = data.input.result.replace(eval('/(\"' + data.input.additionalParams + '\":\\\[)/'), '\"' + data.input.additionalParams + '\":\[' + paramString.slice(1, paramString.length - 1) + ',');
                                }
                            }
                            vm.envModel = angular.fromJson(data.input.result);
                            break;
                        }
                }
            } else {
                vm.data.info.model = {
                    envName: '',
                    frontURIList: [],
                    headerList: [],
                    paramList: []
                }
                switch (data.input.status) {
                    case 0:
                        {
                            vm.envQueryInit = function(attr) {
                                return attr;
                            };
                            break;
                        }
                    case 1:
                        {
                            data.input.result = data.input.param;
                            break;
                        }
                    case 2:
                        {
                            data.input.result = data.input.param;
                            vm.envModel = angular.fromJson(data.input.result);
                            break;
                        }
                }
            }
        }
        data.fun.init = function() {
            var template = {
                cache: null
            }
            data.storage = JSON.parse(window.localStorage['ENV_DIRECTIVE_TABLE'] || '{}');
            if (vm.data.interaction.response.query) {
                data.assistantFun.init();
            } else {
                ApiManagementResource.Env.Query({
                    projectID: data.interaction.request.projectID
                }).$promise.then(function(response) {
                    vm.data.interaction.response.query = response.envList || [];
                    data.assistantFun.init();
                })
            }
        }

        /**
         * @function [下拉按钮单击功能函数] [Click the drop-down button to click the function]
         */
        vm.data.fun.click = function(query) { 
            var template = {
                output: null
            }
            if (query == null) {
                query = {
                    envName: '',
                    frontURIList: [],
                    headerList: [],
                    paramList: [],
                    additionalParamList: []
                }
            } else {
                query.changed = true;
            }
            data.input.status ? data.info.reset = 1 : '';
            vm.data.info.model = query;
            vm.envParam = query.paramList;
            vm.data.info.itemStatus = 'hidden';
            data.storage[data.interaction.request.projectID] = query.envID;
            window.localStorage.setItem('ENV_DIRECTIVE_TABLE', angular.toJson(data.storage));

            try {
                vm.totalEnv = query || {};
            } catch (e) {}
            switch (data.input.status) {
                case 0:
                    {
                        if (query.frontURIList.length > 0) {
                            vm.envQueryInit = function(attr) {
                                var result = attr;
                                angular.forEach(vm.data.info.model.paramList, function(val, key) {
                                    result = result.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                })
                                return vm.data.info.model.frontURIList[0].uri + result;
                            };
                        } else {
                            vm.envQueryInit = function(attr) {
                                var result = attr;
                                angular.forEach(vm.data.info.model.paramList, function(val, key) {
                                    result = result.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                })
                                return result;
                            };
                        }
                        break;
                    }
                case 1:
                    {
                        var result = null;
                        data.input.result = data.input.param;
                        if (query.paramList.length > 0) {
                            var templateResult = {};
                            angular.copy(angular.fromJson(data.input.param), templateResult);
                            angular.forEach(query.paramList, function(val, key) {
                                templateResult.baseInfo.apiURI = templateResult.baseInfo.apiURI.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                angular.forEach(templateResult.headerInfo, function(val1, key1) {
                                    val1.headerName = val1.headerName.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                })
                                angular.forEach(templateResult.requestInfo, function(val1, key1) {
                                    val1.paramKey = val1.paramKey.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                    val1.paramKeyHtml = val1.paramKeyHtml.replace(eval('/(\\\{\\\{' + val.paramKey + '\\\}\\\})/g'), val.paramValue);
                                })
                                result = angular.toJson(templateResult);
                            })
                            data.input.result = result;
                        }
                        if (query.frontURIList.length > 0) {
                            data.input.result = data.input.result.replace(eval('/(\"' + data.input.uri + '\":\"){1}/g'), '\"' + data.input.uri + '\":\"' + vm.data.info.model.frontURIList[0].uri);
                        } else {
                            data.input.result = data.input.result;
                        }
                        if (query.headerList.length > 0) {
                            var headerString = angular.toJson(query.headerList);
                            if (data.input.result.indexOf('\"' + data.input.header + '\":[]') > -1) {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1));
                            } else {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1) + ',');
                            }
                        }
                        vm.envModel = angular.fromJson(data.input.result);
                        vm.envModel.baseInfo.apiNoteHtml = $sce.trustAsHtml($filter('XssFilter')(vm.envModel.baseInfo.apiNote, {
                            onIgnoreTagAttr: function(tag, name, value, isWhiteAttr) {
                                if (/(class)|(id)|(name)/.test(name)) {
                                    return name + '="' + value + '"';
                                }
                            }
                        }));
                        break;
                    }
                case 2:
                    {

                        if (query.frontURIList.length > 0) {
                            data.input.result = data.input.param.replace(eval('/(\"' + data.input.uri + '\":\"){1}/g'), '\"' + data.input.uri + '\":\"' + vm.data.info.model.frontURIList[0].uri);
                        } else {
                            data.input.result = data.input.param;
                        }
                        if (query.headerList.length > 0) {
                            var headerString = angular.toJson(query.headerList);
                            if (data.input.result.indexOf('\"' + data.input.header + '\":[]') > -1) {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1));
                            } else {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.header + '\":\\\[)/'), '\"' + data.input.header + '\":\[' + headerString.slice(1, headerString.length - 1) + ',');
                            }
                        }
                        if (query.additionalParamList.length > 0) {
                            var paramString = angular.toJson(query.additionalParamList);
                            if (data.input.result.indexOf('\"' + data.input.additionalParams + '\":[]') > -1) {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.additionalParams + '\":\\\[)/'), '\"' + data.input.additionalParams + '\":\[' + paramString.slice(1, paramString.length - 1));
                            } else {
                                data.input.result = data.input.result.replace(eval('/(\"' + data.input.additionalParams + '\":\\\[)/'), '\"' + data.input.additionalParams + '\":\[' + paramString.slice(1, paramString.length - 1) + ',');
                            }
                        }
                        template.output = JSON.parse(data.input.result);
                        vm.envModel = template.output;
                        break;
                    }
            }
        }

        /**
         * @function [监听初始化] [Listener initialization]
         */
        $scope.$on('$EnvInitReady', function(_default, attr) { 
            attr = attr || {};
            if (attr.reset && data.info.reset) {
                data.input.param = angular.toJson(attr.resetInfo);
                data.info.reset = false;
            } else if (attr.reset) {
                data.input.param = angular.toJson(attr.resetInfo);
            } else {
                data.input.param = attr.param;
            }
            data.input.status = attr.status;
            data.input.header = attr.header ? attr.header : 'headerInfo';
            data.input.additionalParams = attr.additionalParams ? attr.additionalParams : 'requestInfo';
            data.input.uri = attr.uri ? attr.uri : 'apiURI';
            data.info.timer = $timeout(function() {
                data.fun.init();
            });
        });

        /**
         * @function [页面销毁功能函数] [Page destruction function]
         */
        $scope.$on('$destroy', function() { 
            if (data.info.timer) {
                $timeout.cancel(data.info.timer);
            }
        });
    }
})();