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
     * @version  3.0.2
     */
    angular.module('eolinker.filter')
    .filter('currentTimeFilter', [function() {
        return function() {
            var data={
                fun:{
                    getTime:null
                }
            }
            /**
             * @function [获取当前时间功能函数] [Get the current time function function]
             * @return   {[string]}   [当前时间 current time]
             */
            data.fun.getTime = function() {
                var template={
                    info:{
                        date:new Date(),
                        time:{
                            year:null,
                            month:null,
                            day:null,
                            hour:null,
                            minute:null,
                            second:null
                        },
                        string:null//结果存储字符串 The result stores the string
                    }
                }
                template.info.time.year = template.info.date.getFullYear(); 
                template.info.time.month = template.info.date.getMonth() + 1; 
                template.info.time.day = template.info.date.getDate(); 

                template.info.time.hour = template.info.date.getHours(); 
                template.info.time.minute = template.info.date.getMinutes();
                template.info.time.second = template.info.date.getSeconds();

                template.info.string = template.info.time.year + "-";

                if (template.info.time.month < 10)
                    template.info.string += "0";

                template.info.string += template.info.time.month + "-";

                if (template.info.time.day < 10)
                    template.info.string += "0";

                template.info.string += template.info.time.day + " ";

                if (template.info.time.hour < 10)
                    template.info.string += "0";

                template.info.string += template.info.time.hour + ":";
                if (template.info.time.minute < 10) template.info.string += '0';
                template.info.string += template.info.time.minute + ":";
                if (template.info.time.second < 10) template.info.string += '0';
                template.info.string += template.info.time.second;
                return (template.info.string);
            }
            return data.fun.getTime();
        }
    }])

})();
