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
     * @function [表达式过滤器指令js] [Expression filter instruction JS]
     
     */
    angular.module('eolinker.directive')
        .filter('timestampFilter', [function() {
            return function() {
                return new Date().getTime();
            }
        }])
        .filter('uuidFilter', [function() {
            var data = {
                fun: {
                    uuid: null 
                }
            }
            /**
             * @function [生成uuid功能函数] [Generating UUID function]
             * @return   {[string]} [uuid]
             */
            data.fun.uuid = function() {
                var template = {
                    array: [],
                    hexSingal: "0123456789abcdef"
                }
                for (var i = 0; i < 36; i++) {
                    template.array[i] = template.hexSingal.substr(Math.floor(Math.random() * 0x10), 1);
                }
                template.array[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
                template.array[19] = template.hexSingal.substr((template.array[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
                template.array[8] = template.array[13] = template.array[18] = template.array[23] = "-";
                return template.array.join("");
            }
            return function() {
                return data.fun.uuid();
            }
        }])
        .filter('randomFilter', [function() {
            return function(input, param1, param2) {
                return Math.ceil(Math.random() * ((parseInt(param2) || 100) - (parseInt(param1) || 0))) + (parseInt(param1) || 0);
            }
        }])
        .filter('constantFilter', [function() {
            return function(input, param) {
                return param;
            }
        }])
        .filter('base64Filter', [function() {
            var data = {
                fun: {
                    utf8Encode: null, 
                    encode: null 
                }
            }

            /**
             * @function [utf8编码功能函数] [Utf8 encoding function]
             * @param    {[obj]}   arg [{text:需过滤的文本 Text to be filtered}]
             * @return   {[string]}       [utf8Encode]
             */
            data.fun.utf8Encode = function(arg) {
                var template = {
                    result: '',
                    code: null
                }
                arg.text = arg.text.replace(/\r\n/g, "\n");
                for (var n = 0; n < arg.text.length; n++) {
                    template.code = arg.text.charCodeAt(n);
                    if (template.code < 128) {
                        template.result += String.fromCharCode(template.code);
                    } else if ((template.code > 127) && (template.code < 2048)) {
                        template.result += String.fromCharCode((template.code >> 6) | 192);
                        template.result += String.fromCharCode((template.code & 63) | 128);
                    } else {
                        template.result += String.fromCharCode((template.code >> 12) | 224);
                        template.result += String.fromCharCode(((template.code >> 6) & 63) | 128);
                        template.result += String.fromCharCode((template.code & 63) | 128);
                    }

                }
                return template.result;
            }
            /**
             * @function [编码功能函数] [Coded function]
             * @param    {[obj]}   arg [{text:需过滤的文本 Text to be filtered}]
             * @return   {[string]}       [encode]
             */
            data.fun.encode = function(arg) {
                var template = {
                    keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
                    result: '',
                    char: [], //单一字符数组 Single character array
                    enchar: [], //编码后字符数组 Coded character array
                    count: 0
                }
                arg.text = data.fun.utf8Encode({ text: arg.text.toString() });
                
                while (template.count < arg.text.length) {
                    template.char[0] = arg.text.charCodeAt(template.count++);
                    template.char[1] = arg.text.charCodeAt(template.count++);
                    template.char[2] = arg.text.charCodeAt(template.count++);
                    template.enchar[0] = template.char[0] >> 2;
                    template.enchar[1] = ((template.char[0] & 3) << 4) | (template.char[1] >> 4);
                    template.enchar[2] = ((template.char[1] & 15) << 2) | (template.char[2] >> 6);
                    template.enchar[3] = template.char[2] & 63;
                    if (isNaN(template.char[1])) {
                        template.enchar[2] = template.enchar[3] = 64;
                    } else if (isNaN(template.char[2])) {
                        template.enchar[3] = 64;
                    }
                    template.result = template.result +
                        template.keyStr.charAt(template.enchar[0]) + template.keyStr.charAt(template.enchar[1]) +
                        template.keyStr.charAt(template.enchar[2]) + template.keyStr.charAt(template.enchar[3]);
                }
                return template.result;
            }
            return function(input) {
                return data.fun.encode({text:input});
            }
        }])
        .filter('md5Filter', ['md5', function(md5) {
            return function(input) {
                return md5.createHash(input.toString());
            }
        }])
        .filter('hexFilter', ['CryptoJSService', function(CryptoJSService) {
            return function(input) {
                return CryptoJSService.enc.Hex.stringify(input);
            }
        }])
        .filter('upperFilter', [function() {
            return function(input) {
                return input.toString().toLocaleUpperCase();
            }
        }])
        .filter('lowerFilter', [function() {
            return function(input) {
                return input.toString().toLocaleLowerCase();
            }
        }])
        .filter('lengthFilter', [function() {
            return function(input) {
                return input.toString().length;
            }
        }])
        .filter('hmacFilter', ['CryptoJSService', '$filter', function(CryptoJSService, $filter) {
            return function(input, param1, param2, param3) {
                var template = {
                    result: null
                }
                switch (param3) {
                    case 'Base64':
                        {
                            template.result = $filter('base64Filter')(eval('CryptoJSService.Hmac' + param1 + '(input,param2||"")'));
                            break;
                        }
                    case 'Hex':
                        {
                            template.result = $filter('hexFilter')(eval('CryptoJSService.Hmac' + param1 + '(input,param2||"")'));
                            break;
                        }
                }
                return template.result;
            }
        }])
        .filter('shaFilter', ['CryptoJSService', '$filter', function(CryptoJSService, $filter) {
            return function(input, param1, param2) {
                var template = {
                    result: null
                }
                switch (param2) {
                    case 'Base64':
                        {
                            template.result = $filter('base64Filter')(eval('CryptoJSService.' + param1 + '(input)'));
                            break;
                        }
                    case 'Hex':
                        {
                            template.result = $filter('hexFilter')(eval('CryptoJSService.' + param1 + '(input)'));
                            break;
                        }
                }
                return template.result;
            }
        }])
        .filter('stringFilter', [function() {
            return function(input, param1) {
                return param1 + input + param1;
            }
        }])
        .filter('substringFilter', [function() {
            return function(input, param1, param2) {
                return input.toString().substring(parseInt(param1 || 0), parseInt(param2 || input.toString().length));
            }
        }])
        .filter('concatFilter', [function() {
            return function(input, param) {
                return input.toString().concat(param.toString());
            }
        }])

})();
