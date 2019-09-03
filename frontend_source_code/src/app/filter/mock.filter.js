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
     * @function [mock数据生成过滤器（依赖mockjs文件）] [mock data generation filter (dependent mockjs file)]
     
     * @service  $filter [注入过滤器服务] [Inject filter service]
     */
    angular.module('eolinker.filter')

    .filter('mockFilter', ['$filter', function($filter) {
        var data = {
            fun: {
                switchType: null, //根据类型赋予不同mock随机值功能函数
                value: null, //输出mock值
            }
        }
        data.fun.switchType = function(arg) {
            var template = {
                callback: '', //返回对象
                val: arg.val //当前输入值
            }
            switch (arg.type) {
                case '0':
                    {
                        template.callback = Mock.Random.string();
                        break;
                    }
                case '1':
                    {
                        template.callback = Mock.Random.image();
                        break;
                    }
                case '2':
                case '13':
                    {
                        template.callback = {}; //{ 'key1': '测试json数据1', 'key2': '测试json数据2' };
                        break;
                    }
                case '3':
                case '10':
                case '11':
                case '14':
                    {
                        template.callback = Mock.Random.integer();
                        break;
                    }

                case '4':
                case '5':
                    {
                        template.callback = Mock.Random.float()
                        break;
                    }
                case '6':
                    {
                        template.callback = Mock.Random.date();
                        break;
                    }
                case '7':
                    {
                        template.callback = Mock.Random.datetime();
                        break;
                    }
                case '8':
                    {
                        template.callback = Mock.Random.boolean();
                        break;
                    }
                case '9':
                    {
                        template.callback = 96;
                        break;
                    }
                case '12':
                    {
                        template.callback = []; //Mock.Random.range(10);
                        break;
                    }
            }
            return template.callback;
        }
        data.fun.value = function(arg) {
            if ((arg.value || '').trim().substr(0, 6) == '@mock=') {

                try {
                    if (arg.value.trim().substr(6, 8) == 'function') {
                        return (new Function("return " + arg.value.trim().slice(6, arg.value.length)))();
                    } else if (arg.value.trim().substring(6, 7) == '@') {
                        return arg.value.trim().slice(6, arg.value.length);
                    } else {
                        if (/Mock/.test(arg.value.substring(6))) {
                            return arg.value.trim().slice(6, arg.value.length);
                        } else {
                            try {
                                return eval('(' + arg.value.trim().slice(6, arg.value.length) + ')');
                            } catch (e) {
                                return arg.value.trim().slice(6, arg.value.length);
                            }
                        }

                    }
                } catch (e) {
                    return data.fun.switchType({ val: arg.value, type: arg.type });
                }
            } else {
                return data.fun.switchType({ val: arg.value, type: arg.type });
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
                length: 0,
                $index: 0,
                item: [],
                loop: {
                    array: {
                        level: arg.array.level + 1,
                        totalItem: arg.array.totalItem,
                        item: arg.array.item.slice(1, arg.array.item.length), //参数名切割>>/::数组
                    },
                    parent: {
                        name: arg.parent.name, //父级参数名
                        object: arg.parent.object, //父级存储变量
                        rule: arg.parent.rule, //规则性父参名
                        array: arg.parent.array //父级数组：default(无保护规则),rule(包含规则)
                    },
                    key: arg.key
                }
            }
            if (arg.array.item.length > 0) {
                angular.copy(arg.array.item, template.item);
                template.item.splice(0, 1);
                if (arg.array.item[0] == arg.parent.name && arg.array.item.length <= 2) {
                    if (data.fun.typeof(template.loop.parent.object[template.loop.parent.rule]) == 'Array') {
                        template.length = template.loop.parent.object[template.loop.parent.rule].length >= 1 ? template.loop.parent.object[template.loop.parent.rule].length - 1 : 0;
                        if (data.fun.typeof(template.loop.parent.object[template.loop.parent.rule][template.length]) == 'Undefined') {
                            template.loop.parent.object[template.loop.parent.rule][template.length] = {};
                        } else if (data.fun.typeof(template.loop.parent.object[template.loop.parent.rule][template.length]) != 'Object') {
                            template.length++;
                            template.loop.parent.object[template.loop.parent.rule][template.length] = {};
                        }
                        template.loop.parent.object[template.loop.parent.rule][template.length][arg.key.name] = arg.key.value;
                    } else if (data.fun.typeof(template.loop.parent.object[template.loop.parent.rule]) == 'Object') {
                        template.loop.parent.object[template.loop.parent.rule][arg.key.name] = arg.key.value;
                    } else {
                        template.loop.parent.object[template.loop.parent.rule] = {};
                        template.loop.parent.object[template.loop.parent.rule][arg.key.name] = arg.key.value;
                    }
                } else {
                    template.$index = arg.parent.array.default.indexOf(template.loop.array.totalItem.slice(0, template.loop.array.level).join('>>'));
                    if (data.fun.typeof(template.loop.parent.object[template.loop.parent.array.rule[template.$index]]) == 'Array') {
                        template.loop.parent.object = template.loop.parent.object[template.loop.parent.array.rule[template.$index]][0];
                    } else {
                        if (data.fun.typeof(template.loop.parent.object[template.loop.parent.array.rule[template.$index]]) != 'Object') {
                            template.loop.parent.object[template.loop.parent.array.rule[template.$index]] = {};
                        }
                        template.loop.parent.object = template.loop.parent.object[template.loop.parent.array.rule[template.$index]];

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
                    templateParent: arg.templateParent || [],
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
            for (var key in arg.input) {
                var val = arg.input[key];
                template.array.item = (val.paramKey + '').replace(/(\s)*([:]{2}|[>]{2})(\s)*/g, '>>').split(/[:]{2}|[>]{2}/);
                template.loopVar.length = template.array.item.length;
                if (val.paramKey) {
                    switch (template.loopVar.length) {
                        case 1:
                            {
                                if (template.array.item[0]) {
                                    template.array.parent.push(template.array.item[0] + (val.rule ? ('|' + val.rule) : ''));
                                    template.array.templateParent.push(template.array.item[0]);
                                    template.result[template.array.item[0] + (val.rule ? ('|' + val.rule) : '')] = data.fun.value({ value: val.value, type: val.paramType });
                                    template.icon.parent = true;
                                }
                                break;
                            }
                        default:
                            {
                                template.loopVar.$index = template.array.templateParent.indexOf(template.array.item.slice(0, template.loopVar.length - 1, 1).join('>>'));
                                if (template.loopVar.$index > -1) {
                                    template.array.templateParent.push(template.array.item.join('>>'));
                                    template.array.parent.push(template.array.item[template.loopVar.length - 1] + (val.rule ? ('|' + val.rule) : ''));
                                    template.loopObject = {
                                        array: {
                                            item: template.array.item,
                                            level: 0,
                                            totalItem: template.array.item
                                        },
                                        parent: {
                                            name: template.array.item[template.loopVar.length - 2],
                                            object: template.result,
                                            rule: template.array.parent[template.loopVar.$index],
                                            array: {
                                                rule: template.array.parent,
                                                default: template.array.templateParent
                                            }
                                        },
                                        key: {
                                            name: template.array.item[template.loopVar.length - 1] + (val.rule ? ('|' + val.rule) : ''),
                                            value: data.fun.value({ value: val.value, type: val.paramType }),
                                            type: val.type,
                                            rule: val.rule
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
            }
            if (template.icon.parent && template.icon.child) {
                template.result = data.fun.main({ input: template.array.child, result: template.result, parent: template.array.parent, templateParent: template.array.templateParent })
            } else if (template.icon.child) {
                for (var key in template.array.child) {
                    var val = template.array.child[key];
                    template.result[val.paramKey + (val.rule ? ('|' + val.rule) : '')] = data.fun.value({ value: val.value, type: val.paramType });
                }
            }
            return template.result;
        }
        return function(input, config) {
            try {
                config = config || {};
                var template = {
                    origin: data.fun.main({ input: input }),
                    output: {}
                }
                console.log(template.origin)
                switch (config.type) {
                    case 'array':
                        {
                            template.output['@type' + (config.rule ? ('|' + config.rule) : '')] = [function() {
                                return Mock.mock(template.origin)
                            }];
                            break
                        }
                    default:
                        {
                            template.output['@type' + (config.rule ? ('|' + config.rule) : '')] = function() {
                                return Mock.mock(template.origin)
                            };
                        }
                }
                return JSON.stringify(Mock.mock(template.output)['@type']);
            } catch (error) {
                return JSON.stringify({ "tips": "mock生成数据出错" })
            }
        }
    }])

})();
