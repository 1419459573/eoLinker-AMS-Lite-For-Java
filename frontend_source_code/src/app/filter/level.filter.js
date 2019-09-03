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
     * @function [参数显示过滤器] [Parameter display filter]
     * @version  3.2.2
     * @service  $sce [注入$sce服务] [Inject $sce service]
     * @service  $filter [注入过滤器服务] [Inject filter service]
     */
    angular.module('eolinker.filter')

    /**
     * @function [参数层级显示过滤器] [Parameter level display filter]
     * @param    {[array]}   input    [需过滤参数 Filter parameters are required] 
     * @return   {[array]}            [处理后的参数 Processed parameters]
     */
    .filter('paramLevelFilter', ['$sce', '$filter', function($sce, $filter) {
        var data = {
            info: {
                timer: {
                    start: null,
                    end: null
                }
            },
            fun: {
                main: null, 
            }
        }
        /**
         * @function [主操作功能函数] [Main operation]
         * @param    {[obj]}   arg [需过滤参数 Filter parameters are required]
         * @return   {[array]}       [处理后的参数 Processed parameters]
         */
        data.fun.main = function(arg) {
            var template = {
                origin: [],
                array: {
                    parent: arg.parent || [], //父存储位置数组（字符串）Parent storage location array (string)
                    child: [], //子存储位置数组（json）Sub storage location array (json)
                    item: [], //临时切割变量存放数组 Temporarily cut variables to store arrays
                },
                loopVar: {
                    $index: 0,
                    length: 0
                },
                oldLength: arg.oldLength || 0,
                $account: 0,
                result: arg.result || []
            }
            angular.copy(arg.input, template.origin);
            for (template.$account = template.origin.length - 1; template.$account >= 0; template.$account--) {
                var val = template.origin[template.$account];
                val.paramKeyHtml = val.paramKey;
                template.array.item = (val.paramKeyHtml + '').split(/[:]{2}|[>]{2}/);
                template.loopVar.length = template.array.item.length;
                val.childAccount = val.childAccount || 0;
                switch (template.loopVar.length) {
                    case 1:
                        {
                            template.array.parent.splice(0, 0, template.array.item[0]);
                            template.result.splice(0, 0, val);
                            break;
                        }
                    default:
                        {
                            template.loopVar.$index = template.array.parent.indexOf(template.array.item.slice(0,template.loopVar.length - 1).join('>>'));
                            if (template.loopVar.$index > -1) {
                                val.paramLevel = template.loopVar.length - 1;
                                val.paramKeyHtml = template.array.item[template.loopVar.length - 1];
                                template.result[template.loopVar.$index].childAccount++;
                                template.array.parent.splice(template.loopVar.$index + 1, 0, template.array.item.join('>>'));
                                template.result.splice(template.loopVar.$index + 1, 0, val);
                            } else {
                                template.array.child.splice(0, 0, val);
                            }
                            break;
                        }
                }
            }

            if (template.array.child.length > 0 && template.oldLength != template.array.child.length) {
                template.result = data.fun.main({ input: template.array.child, result: template.result, parent: template.array.parent, oldLength: template.array.child.length })
            } else if (template.array.child.length > 0) {
                template.result = template.result.concat(template.array.child);
            }
            return template.result;
        }
        return function(input) {
            return data.fun.main({ input: input });
        }
    }])

    .filter('paramLevelToNestFilter', ['$sce', '$filter', function($sce, $filter) {
        var data = {
            fun: {
                main: null, //主操作功能函数
                loop: null, //自循环嵌入子级功能函数
            }
        }
        data.fun.loop = function(arg) {
            var template = {
                item: [],
                loop: {
                    array: {
                        item: arg.array.item.slice(1, arg.array.item.length),
                    },
                    parent: {
                        name: arg.parent.name,
                        object: arg.parent.object,
                        array: arg.parent.array
                    },
                    key: arg.key
                }
            }
            if (arg.array.item.length > 0) {
                angular.copy(arg.array.item, template.item);
                template.item.splice(0, 1);
                if (arg.array.item[0] == arg.parent.name && template.item.indexOf(arg.parent.name) == -1) {
                    template.loop.parent.object.childList.push({ paramKey: arg.key.name, childList: [],parent:arg.key.parent });
                } else {
                    template.loop.parent.$index = arg.parent.array.slice(arg.parent.$index, arg.parent.array.length).indexOf(arg.array.item[1]) + arg.parent.$index;
                    try {
                        if (template.loop.parent.$index - arg.parent.$index > template.loop.parent.object.childList.length) {
                            template.loop.parent.object = template.loop.parent.object.childList[template.loop.parent.object.childList.length-1]||{ childList: [] };
                        } else {
                            template.loop.parent.object = template.loop.parent.object.childList[template.loop.parent.$index - arg.parent.$index - 1] || { childList: [] };
                        }
                    } catch (e) {
                        template.loop.parent.object = { childList: [] };
                    }
                    data.fun.loop(template.loop);
                }
            } else {
                template.loop.parent.object = { paramKey: arg.key.value, childList: [] };
            }
        }
        data.fun.main = function(arg) {
            var template = {
                loopObject: null,
                array: {
                    templateParent:arg.templateParent||[],
                    parent: arg.parent || [], //父存储位置数组（字符串）
                    child: [], //子存储位置数组（json）
                    item: [], //临时切割变量存放数组
                },
                loopVar: {
                    $index: 0,
                    length: 0,
                },
                icon: {
                    child: false,
                    parent: false
                },
                level: arg.level||{
                    object: {},
                    $index: 0
                },
                result: arg.result || []
            }
            angular.forEach(arg.input, function(val, key) {
                template.array.item = (val.paramKey + '').replace(/(\s)*([:]{2}|[>]{2})(\s)*/g, '>>').split(/[:]{2}|[>]{2}/);
                template.loopVar.length = template.array.item.length;
                switch (template.loopVar.length) {
                    case 1:
                        {
                            if (template.array.item[0]) {
                                template.array.parent.push(template.array.item[0]);
                                template.array.templateParent.push(template.array.item[0]);
                                template.result.push({ paramKey: template.array.item[0], childList: [],parent:'.' });
                                template.level.object[template.array.item[0]] = template.level.$index;
                                template.level.$index++;
                                template.icon.parent = true;
                            }
                            break;
                        }
                    default:
                        {
                            template.loopVar.$index = template.array.templateParent.indexOf(template.array.item.slice(0, template.loopVar.length - 1, 1).join('>>'));
                            template.loopVar.firstParent = template.array.parent.indexOf(template.array.item[0]);
                            if (template.loopVar.$index > -1) {
                                template.array.templateParent.push(template.array.item.join('>>'));
                                template.array.parent.push(template.array.item[template.loopVar.length - 1]);
                                template.loopObject = {
                                    array: {
                                        item: template.array.item,
                                    },
                                    parent: {
                                        name: template.array.item[template.loopVar.length - 2],
                                        object: template.result[template.level.object[template.array.item[0]]],
                                        $index: template.loopVar.firstParent,
                                        array: template.array.parent
                                    },
                                    key: {
                                        name: template.array.item[template.loopVar.length - 1],
                                        value: val.paramInfo || '',
                                        parent:'.'+template.array.item.slice(0,template.loopVar.length - 1).join('.')+'.'
                                    }
                                }
                                data.fun.loop(template.loopObject);
                                template.icon.parent = true;
                            } else {
                                template.array.child.push(val);
                                template.icon.child = true;
                            }
                            break;
                        }
                }
            })
            if (template.icon.parent && template.icon.child) {
                template.result = data.fun.main({ input: template.array.child, result: template.result, parent: template.array.parent,level:template.level, templateParent: template.array.templateParent })
            } else if (template.icon.child) {
                angular.forEach(template.array.child, function(val, key) {
                    template.result.push({ paramKey: val.paramKey, childList: [] });
                })
            }
            return template.result;
        }
        return function(input) {
            var template = {
                input: []
            }
            angular.copy(input, template.input);
            return data.fun.main({ input: template.input });
        }
    }])
    
    /**
     * @function [参数列表转换json过滤器] [Parameter list conversion json filter]
     * @param    {[array]}   input     [参数列表 parameter list]
     * @return   {[string]}            [json字符串 json string]
     */
    .filter('paramLevelToJsonFilter', ['$sce', '$filter', function($sce, $filter) {
        var data = {
            options: null,
            exist:false,
            fun: {
                main: null, //主操作功能函数
                loop: null, //自循环嵌入子级功能函数
                typeof: null, //判别类型功能函数
            }
        }
        data.fun.typeof = function(object) {
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
        data.fun.loop = function(arg) {
            var template = {
                item: [],
                loop: {
                    array: {
                        item: arg.array.item.slice(1, arg.array.item.length),
                    },
                    parent: {
                        name: arg.parent.name,
                        object: arg.parent.object
                    },
                    key: arg.key
                }
            }
            if (arg.array.item.length > 0) {
                angular.copy(arg.array.item, template.item);
                template.item.splice(0, 1);
                if (arg.array.item[0] == arg.parent.name && arg.array.item.length<=2) {
                    if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]]) == 'Array') {
                        var length = template.loop.parent.object[arg.array.item[0]].length >= 1 ? template.loop.parent.object[arg.array.item[0]].length - 1 : 0;
                        if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]][length]) == 'Undefined') {
                            template.loop.parent.object[arg.array.item[0]][length] = {};
                        } else if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]][length]) != 'Object') {
                            length++;
                            template.loop.parent.object[arg.array.item[0]][length] = {};
                        }
                        template.loop.parent.object[arg.array.item[0]][length][arg.key.name] = arg.key.value;
                    } else if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]]) == 'Object') {
                        template.loop.parent.object[arg.array.item[0]][arg.key.name] = arg.key.value;
                    } else {
                        template.loop.parent.object[arg.array.item[0]] = {};
                        template.loop.parent.object[arg.array.item[0]][arg.key.name] = arg.key.value;
                    }
                } else {
                    if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]]) == 'Array') {
                        template.loop.parent.object = template.loop.parent.object[arg.array.item[0]][0];
                    } else {
                        if (data.fun.typeof(template.loop.parent.object[arg.array.item[0]]) != 'Object') {
                            template.loop.parent.object[arg.array.item[0]] = {};
                        }
                        template.loop.parent.object = template.loop.parent.object[arg.array.item[0]];
                    }
                    data.fun.loop(template.loop);
                }
            } else {
                template.loop.parent.object = arg.key.value;
            }
        }
        data.fun.main = function(arg) {
            var template = {
                loopObject: null,
                array: {
                    parent: arg.parent || [], //父存储位置数组（字符串）
                    child: [], //子存储位置数组（json）
                    item: [], //临时切割变量存放数组
                },
                loopVar: {
                    $index: 0,
                    length: 0
                },
                icon: {
                    child: false,
                    parent: false
                },
                result: arg.result || {}
            }
            angular.forEach(arg.input, function(val, key) {
                template.array.item = (val.paramKey + '').replace(/(\s)*([:]{2}|[>]{2})(\s)*/g, '>>').split(/[:]{2}|[>]{2}/);
                template.loopVar.length = template.array.item.length;
                try {
                    switch (data.options.status) {
                        case 'automated-pro':
                            {
                                val.paramInfo = val.paramValue || (val.paramValueList && val.paramValueList.length > 0 ? val.paramValueList[val.default].value : '');
                                break;
                            }
                        default:
                            {
                                switch(parseInt(val.type||0)){
                                    case 0:
                                    case 1:
                                    case 9:{
                                        break;
                                    }
                                    default:{
                                        if(val.paramInfo){
                                            val.paramInfo='#'+val.paramInfo;
                                        }else{
                                            switch(parseInt(val.type||0)){
                                                case 2:
                                                case 13:{
                                                    val.paramInfo={};
                                                    break;
                                                }
                                                case 12:{
                                                    val.paramInfo=[];
                                                    break;
                                                }
                                            }
                                        }
                                        
                                        break;
                                    }
                                }
                                break;
                            }
                    }
                    if (/^#/.test(val.paramInfo) && !/^#(([1-9]\d*)|0)(\.\d*[1-9])?/.test(val.paramInfo)) {
                        val.paramInfo = JSON.parse(val.paramInfo.substring(1));
                    }
                } catch (e) {}
                if(data.options.check&&!data.exist){
                    data.exist=val[data.options.check.key]==data.options.check.value;
                }
                if (val[data.options.require.key || 'checkbox'] == (data.options.require.value || true)) {
                    switch (template.loopVar.length) {
                        case 1:
                            {
                                if (template.array.item[0]) {
                                    template.array.parent.push(template.array.item[0]);
                                    template.result[template.array.item[0]] = val.paramInfo;
                                    template.icon.parent = true;
                                }
                                break;
                            }
                        default:
                            {
                                template.loopVar.$index = template.array.parent.indexOf(template.array.item[template.loopVar.length - 2]);
                                if (template.loopVar.$index > -1) {
                                    template.array.parent.push(template.array.item[template.loopVar.length - 1]);
                                    template.loopObject = {
                                        array: {
                                            item: template.array.item,
                                        },
                                        parent: {
                                            name: template.array.item[template.loopVar.length - 2],
                                            object: template.result
                                        },
                                        key: {
                                            name: template.array.item[template.loopVar.length - 1],
                                            value: val.paramInfo || ''
                                        }
                                    }
                                    data.fun.loop(template.loopObject);
                                    template.icon.parent = true;
                                } else {
                                    template.array.child.push(val);
                                    template.icon.child = true;
                                }
                                break;
                            }
                    }
                }
            })
            if (template.icon.parent && template.icon.child) {
                template.result = data.fun.main({ input: template.array.child, result: template.result, parent: template.array.parent })
            } else if (template.icon.child) {
                angular.forEach(template.array.child, function(val, key) {
                    template.result[val.paramKey] = val.paramInfo || '';
                })
            }
            return template.result;
        }
        return function(input, options) {
            var template = {
                input: [],
                output: '',
                match: []
            }
            data.options = options || {
                require: {}
            };
            angular.copy(input, template.input);
            template.output = JSON.stringify(data.fun.main({ input: template.input }));
            template.match = template.output.match(/:"#(([1-9]\d*)|0)(\.\d*[1-9])?"/g) || [];
            angular.forEach(template.match, function(val, key) {
                if (data.fun.typeof(val) != 'String') return;
                template.output = template.output.replace(eval('/' + val + '/g'), val.replace(/"(#|)/g, ''));
            })
            console.log(template.output)
            if (data.options.check) {
                return JSON.stringify({
                    output: template.output,
                    exist:data.exist
                });
            } else {
                return template.output;
            }

        }
    }])
})();
