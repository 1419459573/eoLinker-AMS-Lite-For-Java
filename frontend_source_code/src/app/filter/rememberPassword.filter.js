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
     * @function [计算当前时间过滤器] [Calculate the current time filter]
     * @version  3.1.7
     */
    angular.module('eolinker.filter')
    .filter('aesEncryptFilter', [function() {
        return function(word) {
            var data={
                fun:{
                    encrypt:null
                }
            }
            /**
             * @function [获取当前时间功能函数] [Get the current time function function]
             * @return   {[string]}   [当前时间 current time]
             */
            data.fun.encrypt = function(word) {
                var key = CryptoJS.enc.Utf8.parse("eolinker");
                var iv  = CryptoJS.enc.Utf8.parse("0102030405060708");
                var srcs = CryptoJS.enc.Utf8.parse(word);
                var encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv, mode:CryptoJS.mode.CBC});
                var encryptedStr = encrypted.ciphertext.toString();
                var encryptedHexStr = CryptoJS.enc.Hex.parse(encryptedStr);
                var encryptedBase64Str = CryptoJS.enc.Base64.stringify(encryptedHexStr);
                return encryptedBase64Str;
            }
            return data.fun.encrypt(word);
        }
    }])
    .filter('aesDecryptFilter', [function() {
        return function(word) {
            var data={
                fun:{
                    decrypt:null
                }
            }
            /**
             * @function [获取当前时间功能函数] [Get the current time function function]
             * @return   {[string]}   [当前时间 current time]
             */
            data.fun.decrypt = function(word) {
                var key = CryptoJS.enc.Utf8.parse("eolinker");
                var iv  = CryptoJS.enc.Utf8.parse('0102030405060708');
                var decrypted = CryptoJS.AES.decrypt(word, key, { iv: iv,mode:CryptoJS.mode.CBC});
                return CryptoJS.enc.Utf8.stringify(decrypted).toString();
            }
            return data.fun.decrypt(word);
        }
    }])

})();
