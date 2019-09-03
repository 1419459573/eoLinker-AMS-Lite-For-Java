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
     * @function [用户权限常量集] [User authority constant set]
     * @version  3.0.2
     */
    angular
        .module('eolinker.constant')
        .constant('AUTH_EVENTS', {
            LOGIN_SUCCESS: 'auth-login-success',//登录成功 Login successful
            LOGIN_FAILED: 'auth-login-failed',//登录失败 Logon failure
            LOGOUT_SUCCESS: 'auth-logout-success',//退出成功 Quit successfully
            SESSION_TIMEOUT: 'auth-session-timeout',//认证超时 Authentication timeout
            UNAUTHENTICATED: 'auth-not-authenticated',//未认证权限 Unauthorized authority
            UNAUTHORIZED: 'auth-not-authorized',//未登录 Not logged in
            SYSTEM_ERROR: 'something-wrong-system'//服务器出错 Server error
        })
        .constant('USER_ROLES', {
            USER: 'guest'
        })
})();
