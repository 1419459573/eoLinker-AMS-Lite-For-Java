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
     * @function [匹配Json转换为Param指令js] [Match Json to param command js]
     
     * @service  $compile [注入$compile服务] [inject $compile service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @param    importMethod [导入文本方式，默认json，1：get请求参数形式,2:请求头部形式] [Import text mode, default json, 1: get request parameter form, 2: request header form]
     * @param    item [设置初始object类集（可选）] [Set the initial object class set (optional)]
     * @param    resetResult [插入结果位置集（必选）] [Insert result set (required)]
     * @param    valueItem [值可能性object类集（可选）] [Value Possibility object Class Set (optional)]
     */
    angular.module('eolinker.directive')
        .directive('setJsonToParams', ['$compile', '$rootScope', '$filter', function($compile, $rootScope, $filter) {
            return {
                restrict: 'A',
                scope: {
                    importMethod: '@', 
                    item: '@', 
                    resetResult: '=', 
                    valueItem: '@' 
                },
                replace: true,
                template: '<button class="eo-button-info add-param-btn import-btn" data-ng-click="data.fun.confirm()">{{importMethod=="1"?data.info.get:(importMethod=="2"?data.info.header:data.info.json)}}</button>',
                link: function($scope, elem, attrs, ngModel) {
                    var data = {
                        input: {
                            key: attrs.setJsonToParams || 'key', //数组参数个例变量名 Array parameter case variable name
                            valueKey: attrs.setValueKey || 'key', //数组参数个例值变量结果（如果value存储为array及object时存在）Array parameter case value variable result (if value is stored as array and object exists)
                            value: attrs.setValue || 'value' //数组参数个例值变量名 Array parameter case value variable name
                        },
                        fun: {
                            format: {
                                getHeaderDefault: null, 
                                default: null, 
                                value: null, 
                                array: null, 
                                object: null, 
                                typeof: null, 
                            }
                        },
                        output: []
                    }
                    $scope.data = {
                        info: {
                            get: $filter('translate')('340'),
                            header: $filter('translate')('341'),
                            json: $filter('translate')('342'),
                        },
                        fun: {
                            confirm: null, //确认导入功能函数 Confirm the import
                        }
                    }

                    /**
                     * @function [json字段值设置功能函数] [json field value set]
                     * @param    {[obj]}   object [需过滤对象] [Need to filter the object]
                     */
                    data.fun.format.value = function(object) {
                        if (!object) return;
                        switch (data.fun.format.typeof(data.output[data.output.length - 1][data.input.value])) {
                            case 'Array':
                                try {
                                    var newItem = JSON.parse($scope.valueItem);
                                    newItem[data.input.valueKey] = object;
                                    data.output[data.output.length - 1][data.input.value].push(newItem);
                                } catch (e) {
                                    data.output[data.output.length - 1][data.input.value][0] = object;
                                }
                                break;
                            default:
                                try {
                                    var newItem = JSON.parse($scope.valueItem);
                                    newItem[data.input.valueKey] = object;
                                    data.output[data.output.length - 1][data.input.value].push(newItem);
                                } catch (e) {
                                    data.output[data.output.length - 1][data.input.value] = object;
                                }
                                break;
                        }
                    }

                    /**
                     * @function [json值字段为array] [The json value field is array]
                     * @param    {[obj]}   object       [需过滤对象] [Need to filter the object]
                     * @param    {[number]}   indent_count [缩进长度] [Indentation length]
                     * @param    {[obj]}   parent       [该对象的父对象] [The parent of the object]
                     */
                    data.fun.format.array = function(object, indent_count, parent) {
                        if (object.length > 0) {
                            data.fun.format.default(object[0], indent_count + 1, parent)
                        }
                    }

                    /**
                     * @function [json值字段为object] [The json value field is object]
                     * @param    {[obj]}   object       [需过滤对象] [Need to filter the object]
                     * @param    {[number]}   indent_count [缩进长度] [Indentation length]
                     * @param    {[obj]}   parent       [该对象的父对象] [The parent of the object]
                     */
                    data.fun.format.object = function(object, indent_count, parent) {
                        var template = {
                            preItem: {}
                        }
                        var newItem={};
                        for (var key in object) {
                            if (object[key] !== 'author-riverLethe-double-slash-note') {
                                try {
                                    newItem = JSON.parse($scope.item);
                                    
                                } catch (e) {
                                    newItem = {};
                                }
                                switch (data.fun.format.typeof(object[key])) {
                                    case 'Object':
                                        {
                                            newItem.paramType='13';
                                            for (var childKey in object[key]) {
                                                if (object[key][childKey] == 'author-riverLethe-double-slash-note') {
                                                    newItem.paramName = childKey;
                                                }
                                                break;
                                            }
                                            break;
                                        }
                                    case 'Array':
                                        {
                                            newItem.paramType='12';
                                            if (object[key].length > 0) {
                                                for (var childKey in object[key][0]) {
                                                    if (object[key][0][childKey] == 'author-riverLethe-double-slash-note') {
                                                        newItem.paramName = childKey;
                                                        object[key].splice(0,1);
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                }
                                template.preItem = newItem;
                                newItem[data.input.key] = parent ? (parent + '>>' + key) : key;
                                data.output.push(newItem);
                                data.fun.format.default(object[key], indent_count + 1, parent ? (parent + '>>' + key) : key)
                            } else {
                                template.preItem.paramName = key;
                            }
                        }
                    }

                    /**
                     * @function [判别类型功能函数] [Discriminant type]
                     * @param    {[obj]}   object       [需过滤对象 Need to filter the object]
                     */
                    data.fun.format.typeof = function(object) {
                        var tf = typeof object,
                            ts = Object.prototype.toString.call(object);
                        return null === object ? 'Null' :
                            'undefined' == tf ? 'Undefined' :
                            'boolean' == tf ? 'Boolean' :
                            'number' == tf ? 'Number' :
                            'string' == tf ? 'String' :
                            '[object Function]' == ts ? 'Function' :
                            '[object Array]' == ts ? 'Array' :
                            '[object Date]' == ts ? 'Date' : 'Object';
                    }

                    /**
                     * @function [判断类型执行相应格式处理函数] [The judgment type performs the corresponding format processing function]
                     * @param    {[obj]}   object       [需过滤对象] [Need to filter the object]
                     * @param    {[number]}   indent_count [缩进长度] [Indentation length]
                     * @param    {[obj]}   parent       [该对象的父对象] [The parent of the object]
                     */
                    data.fun.format.default = function(object, indent_count, parent) {
                        switch (data.fun.format.typeof(object)) {
                            case 'Boolean':
                            case 'Number':
                            case 'String':
                                object = '' + object;
                                data.fun.format.value(object);
                                break;
                            case 'Array':
                                data.fun.format.array(object, indent_count, parent);
                                break;
                            case 'Object':
                                data.fun.format.object(object, indent_count, parent);
                                break;
                        }
                    }

                    /**
                     * @function [导入头部主函数] [Import the head main function]
                     * @param    {[string]}   string       [JSON]
                     */
                    data.fun.format.getHeaderDefault = function(string) {
                        var template = {
                            query: string.replace(/\"/g, '\\\"').split('\n'),
                            output: '{"'
                        }
                        angular.forEach(template.query, function(val, key) {
                            if (template.query.length - 1 != key) {
                                template.output = template.output + val.replace(/:/, '":"') + '","';
                            } else {
                                template.output = template.output + val.replace(/:/, '":"') + '"}';
                            }
                        })
                        console.log(JSON.parse(template.output))

                        return template.output;
                    }

                    /**
                     * @function [导入参数主函数] [Import the parameter main function]
                     * @param    {[string]}   string       [JSON]
                     */
                    data.fun.format.getParamDefault = function(string) {
                        var template = {
                            $index: string.indexOf('?'),
                            output: ''
                        }
                        switch (template.$index) {
                            case -1:
                                {
                                    template.output = '{"' + string.replace(/&/g, '","').replace(/=/g, '":"') + '"}';
                                    break;
                                }
                            default:
                                {
                                    template.output = '{"' + string.substring(template.$index + 1).replace(/&/g, '","').replace(/=/g, '":"') + '"}';
                                    break;
                                }
                        }
                        return template.output;
                    }

                    /**
                     * @function [插入返回参数集] [Insert the return parameter set]
                     */
                    $scope.data.fun.confirm = function() { 
                        var template = {
                            input: '',
                            modal: {
                                method: $scope.importMethod
                            },
                            jsonToParamObject:{}
                        }
                        $rootScope.JsonToParamInputModal(template.modal, function(callback) {
                            if (callback) {
                                switch ($scope.importMethod) {
                                    case '1':
                                        {
                                            try {
                                                template.input = JSON.parse(data.fun.format.getParamDefault(callback.desc));
                                            } catch (e) {
                                                $rootScope.InfoModal($filter('translate')('380'), 'error');
                                            }

                                            break;
                                        }
                                    case '2':
                                        {
                                            try {
                                                template.input = JSON.parse(data.fun.format.getHeaderDefault(callback.desc));
                                            } catch (e) {
                                                $rootScope.InfoModal($filter('translate')('381'), 'error');
                                            }
                                            break;
                                        }
                                    default:
                                        {
                                            try {

                                                template.jsonToParamObject = {
                                                    origin: callback.desc.replace(/\/\/((?!").)*\n/g, ',"author-lethe":"author-riverLethe-double-slash-note",').replace(/(\s)*,(\s)*,/g, ',').replace(/(\s)*,(\s)*}/g, '}').replace(/(\s)*,(\s)*\]/g, ']').replace(/(\s)*\[(\s)*,"author-lethe":"author-riverLethe-double-slash-note"/g, '[{"author-lethe":"author-riverLethe-double-slash-note"}').replace(/(\s)*{(\s)*,/g, '{'),
                                                    matchList: [],
                                                    splitList: [],
                                                    result: ''
                                                }
                                                template.jsonToParamObject.matchList = callback.desc.match(/\/\/((?!").)*\n/g);
                                                template.jsonToParamObject.splitList = template.jsonToParamObject.origin.split('author-lethe');
                                                angular.forEach(template.jsonToParamObject.splitList, function(val, key) {
                                                    if (key == 0) {
                                                        template.jsonToParamObject.result = val;
                                                    } else {
                                                        template.jsonToParamObject.result = template.jsonToParamObject.result + template.jsonToParamObject.matchList[key - 1].replace(/\n/g, '').replace(/\/\//g, '') + val;
                                                    }
                                                })
                                                template.input = eval('(' + template.jsonToParamObject.result + ')');

                                            } catch (e) {
                                                $rootScope.InfoModal($filter('translate')('382'), 'error');
                                            }
                                            break;
                                        }
                                }
                                data.fun.format.default(template.input, 1);
                                data.output.push(JSON.parse($scope.item));
                                switch (callback.which) {
                                    case 0:
                                        { //插入
                                            $scope.resetResult.splice($scope.resetResult.length - 1, 1);
                                            $scope.resetResult = $scope.resetResult.concat(data.output);
                                            break;
                                        }
                                    case 1:
                                        { //替换

                                            $scope.resetResult = data.output;
                                            break;
                                        }
                                }
                                data.output = [];
                            }
                        });
                    }
                }
            };
        }]);
})();
