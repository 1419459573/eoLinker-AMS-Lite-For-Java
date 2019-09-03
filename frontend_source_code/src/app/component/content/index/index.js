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
     * @function [登录页相关指令js] [Login page related instructions js]
     * @version  3.0.2
     * @service  $cookies [注入$cookies服务] [inject cookies service]
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $rootScope [注入根作用域服务] [inject rootScope service]
     * @service  CommonResource [注入通用接口服务] [inject common API service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  md5 [注入md5服务] [inject md5 service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     * @constant COOKIE_CONFIG [注入COOKIE_CONFIG常量] [inject COOKIE_CONFIG constant service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('index', {
                    url: '/index',
                    auth: true,
                    template: '<index></index>'
                });
        }])
        .component('index', {
            templateUrl: 'app/component/content/index/index.html',
            controller: loginController
        })

    loginController.$inject = ['$cookies', '$scope', '$rootScope', 'CommonResource', '$state', 'md5', 'NavbarService', '$filter', 'COOKIE_CONFIG', 'CODE'];

    function loginController($cookies, $scope, $rootScope, CommonResource, $state, md5, NavbarService, $filter, COOKIE_CONFIG, CODE) {

        var vm = this;
        vm.data = {
            service: NavbarService,
            info: {
                submitted: false,
                password: {
                    isShow: false
                },
                isRemember: false
            },
            interaction: {
                request: {
                    loginName: '',
                    loginPassword: '',
                }
            },
            fun: {
                init: null, 
                confirm: null, 
                changeView: null 
            }
        }
        
        /**
         * @function [确认是否已登录功能函数] [Confirm if you are logged in]
         */
        vm.data.fun.confirm = function() {
            var template = {
                storage: {
                    loginName: vm.data.interaction.request.loginName,
                    loginPassword: $filter('aesEncryptFilter')(vm.data.interaction.request.loginPassword)
                },
                request: {
                    loginName: vm.data.interaction.request.loginName,
                    loginPassword: md5.createHash(vm.data.interaction.request.loginPassword),
                }
            }
            if ($scope.loginForm.$valid) {
                vm.data.info.submitted = false;
                $cookies.put("verifyCode", template.request.verifyCode, COOKIE_CONFIG);
                CommonResource.Guest.Login(template.request).$promise.then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                if (vm.data.info.isRemember) {
                                    window.localStorage.setItem('LOGININFO', angular.toJson(template.storage));
                                } else {
                                    window.localStorage.removeItem('LOGININFO');
                                }
                                $state.go('home.project.api.default');
                                break;
                            }
                        default:
                            {
                                $rootScope.InfoModal($filter('translate')('0206'), 'error');
                                break;
                            }
                    }
                })
            } else {
                vm.data.info.submitted = true;
            }
        }

        /**
         * @function [密码是否显示功能函数] [Whether the password is displayed]
         */
        vm.data.fun.changeView = function() {
            if (vm.data.interaction.request.loginPassword) {
                vm.data.info.password.isShow = !vm.data.info.password.isShow;
            }
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = (function() {
            var template = {
                interaction: {
                    request: JSON.parse(window.localStorage['LOGININFO'] || '{}')
                }
            }
            CommonResource.Install.Config().$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('0203')] });
                            if (window.localStorage['LOGININFO']) {
                                try {
                                    vm.data.interaction.request.loginName = template.interaction.request.loginName;
                                    vm.data.interaction.request.loginPassword = $filter('aesDecryptFilter')(template.interaction.request.loginPassword);
                                    vm.data.info.isRemember = true;
                                } catch (e) {
                                    vm.data.info.isRemember = false;
                                }
                            }
                            break;
                        }
                    default:
                        {
                            $state.go('guide.first_step');
                            break;
                        }
                }
            })
        })()
    }
})();
