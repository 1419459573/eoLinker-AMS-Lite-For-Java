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
     * @function [api接口相关公用服务js] [api interface related public service js]
     * @version  3.1.5
     * @service  $state [注入作用域服务] [Injection state service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  Cache_CommonService [注入HomeProject_Service服务] [Injection Cache_CommonService service]
     * @service  GroupService [注入GroupService服务] [Injection GroupService service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .service('HomeProjectDefaultApi_Service', index);

    index.$inject = ['$state', '$rootScope', 'ApiManagementResource', 'Cache_CommonService','GroupService' ,'CODE', '$filter']

    function index($state, $rootScope, ApiManagementResource, Cache_CommonService, GroupService ,CODE, $filter) {
        var data = {
            service: {
                detail: Cache_CommonService
            },
            navbar: {
                menu: null, 
                delete: null, 
                recover: null, 
                deleteCompletely: null, 
            }
        }
        /**
         * @function [菜单功能] [Menu function]
         */
        data.navbar.menu = function(status, uri, cache) {
            var template = {
                uri: {
                    groupID: uri.groupID,
                    childGroupID: uri.childGroupID,
                    apiID: uri.apiID
                }
            }
            switch (status) {
                case 'list':
                    {
                        $state.go('home.project.inside.api.list', template.uri);
                        break;
                    }
                case 'detail':
                    {
                        $state.go('home.project.inside.api.detail', template.uri);
                        break;
                    }
                case 'test':
                    {
                        data.service.detail.set(cache);
                        $state.go('home.project.inside.api.test', template.uri);
                        break;
                    }
                case 'mock':
                    {
                        $state.go('home.project.inside.api.mock', template.uri);
                        break;
                    }
                case 'history':
                    {
                        $state.go('home.project.inside.api.history', template.uri);
                        break;
                    }
                case 'edit':
                    {
                        $state.go('home.project.inside.api.edit', template.uri);
                        break;
                    }
                case 'copy':
                    {
                        template.uri.type = 2;
                        $state.go('home.project.inside.api.edit', template.uri);
                        break;
                    }
            }
        }
        /**
         * @function [移入回收站] [Move into the Recycle Bin]
         */
        data.navbar.delete = function(arg) {
            var template = {
                request: {
                    projectID: arg.projectID,
                    apiID: '[' + arg.apiID + ']'
                },
                uri: {
                    groupID: arg.groupID,
                    childGroupID: arg.childGroupID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100230'), false, $filter('translate')('012100231'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Api.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $state.go('home.project.inside.api.list', template.uri);
                                        $rootScope.InfoModal($filter('translate')('012100232'), 'success');
                                        break;
                                    }
                            }
                        })
                }
            });
        }
        /**
         * @function [恢复功能函数] [Restore function function]
         */
        data.navbar.recover = function(arg) {
            var template = {
                modal: {
                    group: {
                        parent: GroupService.get(),
                        title: $filter('translate')('012100237')
                    }
                },
                request: {
                    projectID: arg.projectID,
                    apiID: '[' + arg.apiID + ']',
                    groupID: ''
                },
                uri: {
                    groupID: arg.groupID,
                    childGroupID: arg.childGroupID
                }
            }
            if (!template.modal.group.parent) {
                $rootScope.InfoModal($filter('translate')('012100238'), 'error');
                return;
            }
            $rootScope.ApiRecoverModal(template.modal, function(callback) {
                if (callback) {
                    template.request.groupID = callback.groupID;
                    ApiManagementResource.Trash.Recover(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100239'), 'success');
                                        $state.go('home.project.inside.api.list', template.uri);
                                        break;
                                    }
                            }
                        })
                }
            });
        }
         /**
         * @function [彻底删除功能函数] [Completely remove the function function]
         */
        data.navbar.deleteCompletely = function(arg) {
            var template = {
                request: {
                    projectID: arg.projectID,
                    apiID: '[' + arg.apiID + ']'
                },
                uri: {
                    groupID: arg.groupID,
                    childGroupID: arg.childGroupID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100047'), false, $filter('translate')('012100048'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Trash.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $state.go('home.project.inside.api.list', template.uri);
                                        $rootScope.InfoModal($filter('translate')('012100049'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100050') ,'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }
        return data;
    }
})();
