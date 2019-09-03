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
     * @function [api详情相关服务js] [api details related services js]
     * @version  3.0.2
     */
    angular.module('eolinker')
        .factory('HomeProject_Common_Service', index);

    index.$inject = ['$rootScope']

    function index($rootScope) {
        var data = {
            apiTestObject: {
                testInfo: null,
                fun: {
                    set: null,
                    clear: null
                }
            },
            envObject: {
                object: {
                    model: {},
                    param: [],
                    fun: null
                },
                query: null,
                fun: {
                    resetObject: null, 
                    clear: null, 
                }
            },
            overviewObject:{//项目概况页相关类
                fun:{
                    autoGeneration:null
                }
            }
        }

        /**
         * @function [缓存测试数据功能函数] [Cache test data]
         * @param    {[arg]}   arg [数据 data]
         */
        data.apiTestObject.fun.set = function(arg) {
            var template = {
                object: {}
            }
            angular.copy(arg.object, template.object);
            data.apiTestObject.testInfo = template.object;
        }

        /**
         * @function [清空测试数据功能函数] [Empty test data]
         */
        data.apiTestObject.fun.clear = function() {
            data.apiTestObject.testInfo = null;
        }

        /**
         * @function [重置变量双向绑定数据功能函数] [Reset variable bidirectional binding data function function]
         * @param    {[obj]}   arg [重置内容 Reset the content]
         */
        data.envObject.fun.resetObject = function() {
            data.envObject.object = {
                model: {},
                param: [],
                fun: null
            };
        }

        /**
         * @function [清缓存功能函数] [Clear cache]
         */
        data.envObject.fun.clear = function() {
            data.envObject.query = null;
        }
        /**
         * @description 自动生成文档
         * @param arg {[object]} {传参对象}
         * @param arg.projectID {[string]} {项目标识}
         * 
         */
        data.overviewObject.fun.autoGeneration=function(arg){
            var template={
                modal:{
                    projectID:arg.projectID,
                    importURL:arg.importURL
                }
            }
            $rootScope.ApiManagement_AutoGenerationModal(template.modal);
        }
        return data;
    }
})();
