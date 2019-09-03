(function() {
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
     * @function [环境模块相关js] [Environment module related js]
     * @version  3.1.1
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection tootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    'use strict';
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.team', {
                    url: '/team',
                    template: '<home-project-inside-team></home-project-inside-team>'
                });
        }])
        .component('homeProjectInsideTeam', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/team/index.html',
            controller: homeProjectInsideTeam
        })

    homeProjectInsideTeam.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', '$filter', 'CODE'];

    function homeProjectInsideTeam($scope, $rootScope, ApiManagementResource, $state, $filter, CODE) {

        var vm = this;
        vm.data = {
            info: {
                search: {
                    submited: false,
                    leave: true,
                    isDisable: false
                },
                power: 2, //0：管理员，1：协作管理员，2：普通成员 0: admin, 1: collaboration manager, 2: regular member
                timer: {
                    fun: null
                },
                filter: {
                    unknown: $filter('translate')('0101012'),
                    unknownNickName: $filter('translate')('0101013'),
                    haveJoined: $filter('translate')('0101014'),
                    add: $filter('translate')('0101015'),
                    administrators: $filter('translate')('0101017'),
                    kick: $filter('translate')('01010111'),
                    quit: $filter('translate')('01010112'),
                    read: $filter('translate')('01010126'),
                    readAndWrite: $filter('translate')('01010127'),
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    userName: ''
                },
                response: {
                    userInfo: null,
                    adminQuery: [],
                    query: []
                }
            },
            fun: {
                init: null, 
                check: null, 
                closeSearch: null, 
                setNickName: null, 
                add: null, 
                setType: null, 
                delete: null, 
                search: null, 
                invite: null 
            }
        }

        /**
         * @function [隐藏搜索框功能函数] [Hide the search box]
         */
        vm.data.fun.closeSearch = function() {
            if (vm.data.info.search.leave) {
                vm.data.info.search.submited = false;
                vm.data.interaction.response.userInfo = null;
            }
        }

        /**
         * @function [设置备注名] [Hide the search box]
         */
        vm.data.fun.setNickName = function(arg) {
            var template={};
            arg.item.groupName = arg.item.partnerNickName;
            arg.item.required = true;
            template.modal = {
                title: $filter('translate')('01010114'),
                secondTitle: $filter('translate')('01010115'),
                data:arg.item
            }
            $rootScope.GroupModal(template.modal, function(callback) {
                if (callback) {
                    ApiManagementResource.Partner.SetNickName({ projectID: vm.data.interaction.request.projectID, nickName: callback.groupName, connID: arg.item.connID }).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('01010116'), 'success');
                                        arg.item.partnerNickName = callback.groupName;
                                        break;
                                    }
                            }
                        });
                }
            });
        }

        /**
         * @function [设置用户权限] [Set user permissions]
         */
        vm.data.fun.setType = function(arg) {
            arg.item.listIsClick = false;
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    connID: arg.item.connID,
                    userType: arg.userType
                }
            }
            ApiManagementResource.Partner.SetType(template.request).$promise
                .then(function(response) {
                    switch (response.statusCode) {
                        case CODE.COMMON.SUCCESS:
                            {
                                $rootScope.InfoModal($filter('translate')('01010117'), 'success');
                                arg.item.listIsClick = false;
                                switch (arg.userType - 0) {
                                    case 1:
                                        {
                                            vm.data.interaction.response.adminQuery.push(arg.item);
                                            vm.data.interaction.response.query.splice(arg.$index, 1);
                                            break;
                                        }
                                    case 2:
                                    case 3:
                                        {
                                            if (arg.item.userType < 2) {
                                                vm.data.interaction.response.query.push(arg.item);
                                                vm.data.interaction.response.adminQuery.splice(arg.$index, 1);
                                            }
                                            break;
                                        }
                                }
                                arg.item.userType = arg.userType;
                                break;
                            }
                    }
                });
        }

        /**
         * @function [添加用户] [Add user]
         */
        vm.data.fun.add = function() {
            if (!vm.data.info.search.isDisable) {
                vm.data.info.search.isDisable = true;
                ApiManagementResource.Partner.Add(vm.data.interaction.request).$promise
                    .then(function(response) {
                        vm.data.info.search.isDisable = false;
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    var info = vm.data.interaction.response.userInfo;
                                    vm.data.info.search.submited = false;
                                    vm.data.interaction.request.userName = '';
                                    info.isNow = 0;
                                    info.userType = 2;
                                    info.inviteCall = info.userName;
                                    info.connID = response.connID;
                                    vm.data.interaction.response.query.push(info);
                                    vm.data.interaction.response.userInfo = null;
                                    break;
                                }
                        }
                    });
            }

        }

        /**
         * @function [移除、退出项目] [Remove, exit item]
         */
        vm.data.fun.delete = function(arg) {
            var bol = arg.item.isNow == 1 ? true : false;
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID
                }
            }
            if (bol) {
                $rootScope.EnsureModal($filter('translate')('01010118'), false, $filter('translate')('01010119'), {}, function(callback) {
                    if (callback) {
                        ApiManagementResource.Partner.Quit(template.request).$promise
                            .then(function(response) {
                                switch (response.statusCode) {
                                    case CODE.COMMON.SUCCESS:
                                        {
                                            $state.go('home.project.api.default');
                                            break;
                                        }
                                }
                            });
                    }
                });

            } else {
                $rootScope.EnsureModal($filter('translate')('01010120'), false, $filter('translate')('01010121'), {}, function(callback) {
                    if (callback) {
                        ApiManagementResource.Partner.Delete({ projectID: vm.data.interaction.request.projectID, connID: arg.item.connID }).$promise
                            .then(function(response) {
                                switch (response.statusCode) {
                                    case CODE.COMMON.SUCCESS:
                                        {
                                            if (arg.isAdmin) {
                                                vm.data.interaction.response.adminQuery.splice(arg.$index, 1);
                                            } else {
                                                vm.data.interaction.response.query.splice(arg.$index, 1);
                                            }
                                            $rootScope.InfoModal($filter('translate')('01010122'), 'success');
                                            break;
                                        }
                                }
                            });
                    }
                });
            }
        }

        /**
         * @function [搜索用户] [searchUse]
         */
        vm.data.fun.search = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    userName: vm.data.interaction.request.userName
                }
            }
            if (vm.data.info.timer.fun) {
                clearInterval(vm.data.info.timer.fun);
            }
            if (!!vm.data.interaction.request.userName) {
                ApiManagementResource.Partner.Search(template.request).$promise
                    .then(function(response) {
                        vm.data.info.search.submited = true;
                        switch (response.statusCode) {
                            case CODE.COMMON.SUCCESS:
                                {
                                    vm.data.interaction.response.userInfo = response.userInfo;
                                    if (vm.data.interaction.response.userInfo.isInvited == 1) {
                                        vm.data.info.search.isDisable = true;
                                    } else {
                                        vm.data.info.search.isDisable = false;
                                    }
                                    break;
                                }
                            default:
                                {
                                    vm.data.interaction.response.userInfo = null;
                                    break;
                                }
                        }
                    })
            }
        };

        /**
         * @function [文本改变功能函数] [text change]
         */
        vm.data.fun.check = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    userName: vm.data.interaction.request.userName
                }
            }
            if (vm.data.info.timer.fun) {
                clearInterval(vm.data.info.timer.fun);
            }
            vm.data.info.search.submited = false;
            vm.data.info.timer.fun = setInterval(function() {
                if ($scope.sureForm.$valid) {
                    ApiManagementResource.Partner.Search(template.request).$promise
                        .then(function(response) {
                            vm.data.info.search.submited = true;
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        vm.data.interaction.response.userInfo = response.userInfo;
                                        break;
                                    }
                                default:
                                    {
                                        vm.data.interaction.response.userInfo = null;
                                        break;
                                    }
                            }
                        })
                }
                clearInterval(vm.data.info.timer.fun);
            }, 1000);
        };

        /**
         * @function [获取邀请码链接功能函数] [Get the invitation code link]
         */
        vm.data.fun.invite = function() {
            var template = {
                modal: {
                    request:{projectID: vm.data.interaction.request.projectID}
                }
            }
            $rootScope.InviteModal(template.modal, function(callback) {

            })
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = (function() {
            var template = {
                request: { projectID: vm.data.interaction.request.projectID }
            }
            $scope.$emit('$WindowTitleSet', { list: [$filter('translate')('01010123'), $state.params.projectName, $filter('translate')('01010124')] });
            ApiManagementResource.Partner.Query(template.request).$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.interaction.response.query = response.partnerList;
                            for (var i = 0; i < vm.data.interaction.response.query.length; i++) {
                                switch (vm.data.interaction.response.query[i].userType - 0) { /*0 项目管理员；1 协助管理员；2 普通成员[读写]；3 普通成员[只读]*/
                                    case 0:
                                        {
                                            vm.data.interaction.response.adminQuery.push(vm.data.interaction.response.query[i]);
                                            if (vm.data.interaction.response.query[i].isNow == 1) {
                                                vm.data.info.power = 0;
                                            }
                                            vm.data.interaction.response.query.splice(i, 1);
                                            i--;
                                            break;
                                        }
                                    case 1:
                                        {
                                            vm.data.interaction.response.adminQuery.push(vm.data.interaction.response.query[i]);
                                            if (vm.data.interaction.response.query[i].isNow == 1) {
                                                vm.data.info.power = 1;
                                            }
                                            vm.data.interaction.response.query.splice(i, 1);
                                            i--;
                                            break;
                                        }
                                    default:
                                        {
                                            console.log(vm.data.interaction.response.query[i])
                                            if (vm.data.interaction.response.query[i].isNow == 1) {
                                                vm.data.info.power = 2;
                                            }
                                        }
                                }
                            }
                            break;
                        }
                    default:
                        {
                            vm.data.interaction.response.query = [];
                            break;
                        }
                }
            });
        })()
    }
})();
