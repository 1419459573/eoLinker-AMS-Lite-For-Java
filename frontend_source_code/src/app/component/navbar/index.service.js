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
     * @function [顶部栏（navbar）相关服务js] [Top bar (navbar) related services js]
     * @version  3.0.2
     * @service  $state [注入路由服务] [inject state service]
     * @service  CommonResource [注入通用接口服务] [inject common API service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     * @return   data [服务相关对象] [Service related object]
     */
    angular.module('eolinker')
        .factory('NavbarService', NavbarService);

    NavbarService.$inject = ['$state', 'CommonResource', 'CODE']

    function NavbarService($state, CommonResource, CODE) {
        var data = {
            info: {
                status: 0, //登录状态 Login status 0：没登录 Not logged in ，1：已登录 Has logged
                userInfo: {
                    unreadMsgNum: null
                },
                navigation: {
                    query: [],
                    current: ''
                }
            },
            fun: {
                logout: null, 
                $router: null 
            }
        }
        /**
         * @function [退出登录功能函数] [Exit the login function function]
         */
        data.fun.logout = function() {
            var template = {
                promise: null
            }
            template.promise = CommonResource.User.LoginOut().$promise;
            template.promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            if (/(project)|(home)|(database)|(gateway)/.test($state.current.name)) { //window.location.href
                                $state.go('index');
                            } else {
                                $state.reload();
                            }
                            data.info.status = 0;
                            break;
                        }
                }
            })
            return template.promise;
        }

        /**
         * @function [路由更换功能函数] [Routing replacement function function]
         */
        data.fun.$router = function() {
            var template = {
                promise: null
            }
            if (data.info.status == 1) {
                template.promise = CommonResource.Message.UnReadNum().$promise
                template.promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                if (data.info.userInfo.unreadMsgNum != response.unreadMsgNum) {
                                    data.info.userInfo.unreadMsgNum = response.unreadMsgNum;
                                }
                                break;
                            }
                        case CODE.COMMON.UNLOGIN:
                            {
                                data.info.status = 0;
                                break;
                            }
                        default:
                            {
                                data.info.unreadMsgNum = 0;
                                break;
                            }
                    }
                });
            } else {
                template.promise = CommonResource.User.Info().$promise;
                template.promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                data.info.userInfo = response.userInfo;
                                data.info.status = 1;
                                break;
                            }
                        default:
                            {
                                data.info.status = 0;
                            }
                    }
                });
            }
            return template.promise;
        }
        
        return data;
    }
})();