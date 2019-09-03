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
     * @function [全局sidebar指令相关js] [Global sidebar instruction related js]
     * @version  3.1.5
     * @service  $scope [注入作用域服务] [inject scope service]
     * @service  $state [注入路由服务] [inject state service]
     * @service  NavbarService [注入NavbarService服务] [inject NavbarService service]
     * @service  $filter [注入过滤器服务] [inject filter service]
     */
    angular.module('eolinker')
        .component('eoSidebar', {
            templateUrl: 'app/component/sidebar/index.html',
            controller: sidebarController,
            bindings: {
                shrinkObject: '<'
            }
        })

    sidebarController.$inject = ['$scope', '$state', 'NavbarService', '$filter'];

    function sidebarController($scope, $state, NavbarService, $filter) {

        var vm = this;
        vm.data = {
            service: {
                default: NavbarService,
            },
            info: {
                current: null,
                menu: [{
                        name: 'API开发管理',
                        sref: 'home.project',
                        icon: 'icon-api',
                        childSref: 'home.project.api.default',
                        isShow: -1
                    }, {
                        name: '数据库结构管理',
                        sref: 'home.database',
                        icon: 'icon-ziliaoku',
                        childSref: 'home.database.list',
                        isShow: -1 
                    },{
                        name: '插件管理',
                        sref: 'home.plug',
                        icon: 'icon-kong',
                        power: -1,
                        childSref: 'home.plug.default',
                        isShow: -1 
                    },  {
                        name: $filter('translate')('22011'),
                        sref: 'home.user',
                        icon: 'icon-yonghu',
                        childSref: 'home.user.basic',
                        isShow: -1 
                    }, {
                        name: $filter('translate')('2101'),
                        href: 'https://www.eolinker.com/#/',
                        icon: 'icon-ONLINEkaifa',
                        isShow: -1,
                        divide: 1
                    }, {
                        name: '帮助中心',
                        href: 'http://help.eolinker.com',
                        icon: 'icon-bangzhu',
                        isShow: -1,
                    }
                ]
            },
            fun: {
                childMenu: null, 
                $Sidebar_ResetCurrent: null, 
                initMenu: null, 
                initChildMenu: null, 
                menu: null, 
                shrink: null, 
            }
        }

        /**
         * @function [子级菜单功能函数] [Submenu function function]
         * @param    {[obj]}   arg [{item:传值列表项 Pass the list item}]
         */
        vm.data.fun.childMenu = function(arg) {
            vm.data.service.default.info.navigation.current = arg.item.name;
            if (arg.item.childSref) {
                $state.go(arg.item.childSref, arg.item.params);
            } else if (arg.item.sref) {
                $state.go(arg.item.sref, arg.item.params);
            } else {
                window.open(arg.item.href);
            }
        }
        /**
         * @function [菜单功能函数] [menu]
         * @param    {[obj]}   arg [{item:传值列表项 Pass the list item}]
         */
        vm.data.fun.menu = function(arg) {
            if(arg.item.disable&&vm.data.service.pro.info.isExpire) return;
            var template = {
                storage: JSON.parse(window.localStorage['VERSIONINFO'] || '{}')
            }
            if (!arg.item.href) {
                vm.data.info.current = arg.item;
                vm.data.info.current.back = false;
                vm.shrinkObject.isShrink = false;
                if (arg.item.childList) {
                    vm.data.service.default.info.navigation = {
                        query: [{ name: arg.item.name }],
                        current: arg.item.childList[0].name
                    }
                } else {
                    vm.data.service.default.info.navigation = {
                        current: arg.item.name
                    }
                }
            }
            if (arg.item.childSref) {
                if (arg.item.otherChildSref && template.storage.companyHashKey) {
                    $state.go(arg.item.otherChildSref, { companyHashKey: template.storage.companyHashKey });
                } else {
                    $state.go(arg.item.childSref, { companyHashKey: template.storage.companyHashKey });
                }
            } else if (arg.item.sref) {
                $state.go(arg.item.sref, { companyHashKey: template.storage.companyHashKey });
            } else {
                window.open(arg.item.href);
            }
        }

        /**
         * @function [初始化菜单功能函数] [Initialize the menu]
         * @param    {[obj]}   arg [{item:传值列表项 Pass the list item}]
         */
        vm.data.fun.initMenu = function(arg) {
            if($state.current.name.toUpperCase().indexOf('INSIDE')>-1) return;
            if ($state.current.name.indexOf(arg.item.sref) > -1) {
                vm.data.info.current = arg.item;
                if (arg.item.childList) {
                    vm.data.service.default.info.navigation = {
                        query: [{ name: arg.item.name }]
                    }
                } else {
                    vm.data.service.default.info.navigation = {
                        current: arg.item.name
                    }
                }

            }
        }

        /**
         * @function [初始化子菜单功能函数] [Initialize submenu]
         * @param    {[obj]}   arg [{item:传值列表项 Pass the list item}]
         */
        vm.data.fun.initChildMenu = function(arg) {
            if ($state.current.name.indexOf(arg.item.sref) > -1) {
                vm.data.service.default.info.navigation.current = arg.item.name;
            }
        }

        /**
         * @function [广播重置当前状态功能函数] [The broadcast resets the current state]
         * @param    {[obj]}   _default [原生传参 Native parameter]
         */
        vm.data.fun.$Sidebar_ResetCurrent = function(_default) {
            vm.data.info.current = vm.data.info.menu[0];
            vm.data.service.default.info.navigation = {
                current: vm.data.info.menu[0].name
            }
            vm.data.service.pro.fun.init();
        }

        /**
         * @function [收缩功能函数] [shrink]
         */
        vm.data.fun.shrink = function() {
            vm.shrinkObject.isShrink = !vm.shrinkObject.isShrink;
        }

        /**
         * @function [初始化功能函数，默认sidebar不收缩，监听$Sidebar_ResetCurrent事件]
         * @function [Initialize, the default sidebar does not shrink, listen for the $ Sidebar_ResetCurrent event]
         */
        vm.$onInit=function(){
            $scope.$on('$Sidebar_ResetCurrent', vm.data.fun.$Sidebar_ResetCurrent)
            vm.shrinkObject.isShrink=false;
        }
    }

})();