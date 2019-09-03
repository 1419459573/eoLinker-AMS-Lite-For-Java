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
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @service  NavbarService [注入NavbarService服务] [Injection NavbarService service]
     */
    angular.module('eolinker')
        .component('homeProjectInsideNavbar', {
            templateUrl: 'app/component/content/home/content/project/content/inside/navbar/index.html',
            bindings: {
                shrinkObject: '<'
            },
            controller: indexController
        })

        indexController.$inject = ['$scope', '$state','$rootScope'];

        function indexController($scope, $state,$rootScope) {
            var vm = this;
            vm.data = {
                component: {
                    sidebarCommonObject: {}
                },
                info: {
                    menu: [{
                            name: '返回列表',
                            sref: 'home.project.api.default',
                            icon: 'icon-huidaodingbu-copy',
                            static: true,
                            power: -1
                        },
                        {
                            base: '/overview',
                            name: '项目概况',
                            sref: 'home.project.inside.overview',
                            icon: 'icon-tongjibaobiao',
                            power: -1
                        },
                        {
                            base: '/api/',
                            name: 'API接口',
                            sref: 'home.project.inside.api',
                            icon: 'icon-api',
                            childSref: 'home.project.inside.api.list',
                            params: {
                                groupID: null
                            },
                            power: -1
                        },
                        {
                            base: '/test',
                            name: '自动化测试',
                            sref: 'home.project.inside.test',
                            childSref: 'home.project.inside.test.default',
                            icon: 'icon-jiqirendaan',
                            key: 10,
                            power: -1,
                            status: 'un-spreed',
                            childList: [{
                                    name: '用例管理',
                                    sref: 'home.project.inside.test',
                                    childSref: 'home.project.inside.test.default',
                                    params: {
                                        groupID: null
                                    }
                                },
                                {
                                    name: '定时测试任务',
                                    class:'disable-menu-li',
                                    tip:'PRO',
                                    tipClass:'navbar-tip-item',
                                    click:function(){
                                        $rootScope.InfoModal('仅AMS专业版支持该功能','success');
                                    }
                                }
                            ]
                        },
                        {
                            base: '/code',
                            name: '状态码',
                            sref: 'home.project.inside.code',
                            childSref: 'home.project.inside.code.list',
                            icon: 'icon-icocode',
                            power: -1
                        },
                        {
                            base: '/doc',
                            name: '项目文档',
                            sref: 'home.project.inside.doc',
                            childSref: 'home.project.inside.doc.list',
                            icon: 'icon-renwuguanli',
                            power: -1
                        },
                        {
                            base: '/env',
                            name: '环境管理',
                            sref: 'home.project.inside.env',
                            icon: 'icon-waibuhuanjing',
                            params: {
                                envID: null
                            },
                            power: -1
                        },
                        {
                            base: '/team',
                            name: '协作管理',
                            sref: 'home.project.inside.team',
                            icon: 'icon-renyuanguanli',
                            power: -1
                        },
                        {
                            base: '/log',
                            name: '项目动态',
                            sref: 'home.project.inside.log',
                            icon: 'icon-gongzuojihua',
                            power: -1
                        }
                    ]
                },
                fun: {
                    menu: null, //菜单功能函数
                    shrink: null //收缩功能函数
                }
            }
            vm.data.fun.shrink = function () {
                $scope.$emit('$Home_ShrinkSidebar', {
                    shrink: vm.shrinkObject.isShrink
                });
            }
            vm.$onInit = function () {
                vm.data.component.sidebarCommonObject = {
                    mainObject: {
                        baseInfo: {
                            staticTop:true,
                            menu: vm.data.info.menu,
                            navigation: [{
                                name: '接口管理',
                                sref: 'home.project.api.default'
                            }, {
                                name: $state.params.projectName
                            }],
                        },
                        baseFun: {
                            shrink: vm.data.fun.shrink,
                        }
                    }
                }
            }
        }

})();