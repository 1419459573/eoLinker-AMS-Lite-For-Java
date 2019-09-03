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
     * @function [api列表模块相关js] [api list module related js]
     * @version  3.2.2
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootScope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  GroupService [注入GroupService服务] [Injection GroupService service]
     * @service  HomeProject_Common_Service [注入HomeProject_Service服务] [Injection HomeProject_Common_Service service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.api.list', {
                    url: '/list?groupID?childGroupID?grandSonGroupID?search',
                    template: '<home-project-inside-api-list power-object="$ctrl.powerObject"></home-project-inside-api-list>'
                });
        }])
        .component('homeProjectInsideApiList', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/api/list/index.html',
            bindings: {
                powerObject: '<',
            },
            controller: homeProjectInsideApiListController
        })

    homeProjectInsideApiListController.$inject = ['$scope', '$rootScope', 'ApiManagementResource', '$state', 'GroupService', 'HomeProject_Common_Service', '$filter', 'CODE'];

    function homeProjectInsideApiListController($scope, $rootScope, ApiManagementResource, $state, GroupService, HomeProject_Common_Service, $filter, CODE) {
        var vm = this;
        vm.data = {
            service: {
                home: HomeProject_Common_Service,
            },
            info: {
                more: parseInt(window.localStorage['PROJECT_MORETYPE']) || 1,
                template: {
                    envModel: []
                },
                sort: {
                    query: [{
                        name: $filter('translate')('012100224'),
                        asc: 0,
                        orderBy: 3
                    }, {
                        name: $filter('translate')('012100225'),
                        asc: 0,
                        orderBy: 1
                    }, {
                        name: $filter('translate')('012100226'),
                        asc: 0,
                        orderBy: 0
                    }, {
                        name: $filter('translate')('012100227'),
                        asc: 0,
                        orderBy: 2
                    }],
                    current: JSON.parse(window.localStorage['PROJECT_SORTTYPE'] || '{"orderBy":3,"asc":0}')
                },
                batch: {
                    address: [],
                    disable: false
                },
                filter: {
                    ascending: $filter('translate')('012100210'),
                    descending: $filter('translate')('012100211'),
                    updated: $filter('translate')('012100214'),
                    grouped: $filter('translate')('012100215'),
                    updatedTime: $filter('translate')('012100216'),
                    deleteTime: $filter('translate')('012100217'),
                }
            },
            interaction: {
                request: {
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID || -1,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID:$state.params.grandSonGroupID,
                    tips: $state.params.search,
                    apiID: []
                }
            },
            fun: {
                init: null,
                search: null,
                sort: null,
                import: null,
                setMore: null,
                recover: null,
                clean: null,
                enter: null,
                batch: {
                    sort: null,
                    delete: null,
                    remove: null,
                    recover: null,
                    default: null,
                }
            },
            assistantFun: {
                init: null
            }
        }
        /**
         * @function [切换缩略和详细功能函数] [Toggle thumbnails and details]
         */
        vm.data.fun.setMore = function(arg) {
            vm.data.info.more = arg.switch;
            window.localStorage.setItem('PROJECT_MORETYPE', arg.switch);
        }

        /**
         * 导出接口
         */
        vm.data.fun.export = function() {
            var template = {
                modal: {
                    status: 'api',
                    title: $filter('translate')('012100258'),
                    request: {
                        projectID: vm.data.interaction.request.projectID,
                        apiID: JSON.stringify(vm.data.interaction.request.apiID),
                    }
                }
            }
            $rootScope.ExportModal(template.modal, function(callback) {
                if (callback) {
                    vm.data.info.batch.disable = false;
                    vm.data.interaction.request.apiID = [];
                }
            });
        }

        /**
         * @function [导入文档] [Import the document]
         */
        vm.data.fun.import = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID
                },
                reader: null,
                modal: {
                    title: $filter('translate')('012100259'),
                    group: {
                        parent: GroupService.get(),
                        groupID: parseInt(vm.data.interaction.request.groupID),
                        childGroupID: parseInt(vm.data.interaction.request.childGroupID),
                        grandSonGroupID: parseInt(vm.data.interaction.request.grandSonGroupID)
                    },
                    inputObject: {
                        type: 'file'
                    },
                    secondTitle: $filter('translate')('012100260')
                }
            }
            if ((!template.modal.group.parent) || (template.modal.group.parent == 0)) {
                $rootScope.InfoModal($filter('translate')('012100229'), 'error');
                return;
            }
            $rootScope.Common_UploadFile(template.modal, function(callback) {
                if (callback) {
                    template.request.groupID = callback.groupID;
                    template.reader = new FileReader();
                    template.reader.readAsText(callback.file);
                    template.reader.onloadend = function(evt) {
                        template.request.data = this.result;
                        $scope.$broadcast('$LoadingInit', {
                            status: 'import',
                            request: template.request
                        });
                    }

                }
            });
        }

        /**
         * 加载状态，发送请求
         * @param  {object} arg 请求
         * @return {promise}     请求promise
         */
        vm.data.fun.load = function(arg) {
            var template = {
                promise: null,
                request: arg.request
            }
            template.promise = ApiManagementResource.Api.Import(template.request).$promise.then(function(response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.assistantFun.init();
                        }
                    case '510000':
                        {
                            $rootScope.InfoModal($filter('translate')('012100261'), 'success');
                            break;
                        }
                    default:
                        {
                            $rootScope.InfoModal($filter('translate')('012100262'), 'error');
                            break;
                        }
                }
            })
            return template.promise;
        }

        /**
         * @function [搜索功能函数] [search]
         */
        vm.data.fun.search = function() {
            if ($scope.searchForm.$valid) {
                $state.go('home.project.inside.api.list', {
                    search: vm.data.interaction.request.tips
                });
            }
        }

        /**
         * @function [编辑函数] [edit]
         */
        vm.data.fun.edit = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                cache: GroupService.get()
            }
            if ((!template.cache) || (template.cache.length == 0)) {
                $rootScope.InfoModal($filter('translate')('012100229'), 'error');
            } else {
                if (!arg.item) {
                    $state.go('home.project.inside.api.edit', {
                        groupID: vm.data.interaction.request.groupID,
                        childGroupID: vm.data.interaction.request.childGroupID,
                        grandSonGroupID:vm.data.interaction.request.grandSonGroupID
                    });
                } else {
                    $state.go('home.project.inside.api.edit', {
                        groupID: vm.data.interaction.request.groupID,
                        childGroupID: vm.data.interaction.request.childGroupID,
                        grandSonGroupID:vm.data.interaction.request.grandSonGroupID,
                        apiID: arg.item.apiID
                    })
                }
            }
        }

        /**
         * @function [排序功能函数] [sort]
         */
        vm.data.fun.sort = function(arg) {
            arg.item.asc = arg.item.asc == 0 ? 1 : 0;
            vm.data.info.sort.current = arg.item;
            window.localStorage.setItem('PROJECT_SORTTYPE', angular.toJson(arg.item));
            $scope.$broadcast('$LoadingInit', {
                boolean: true
            });
        }

        /**
         * @function [切换星标状态功能函数] [Switch the star status]
         */
        vm.data.fun.storage = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: arg.item.apiID
                }
            }
            switch (arg.item.starred - 0) {
                case 0:
                    {
                        ApiManagementResource.Star.Add(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        arg.item.starred = 1;
                                        break;
                                    }
                            }
                        });
                        break;
                    }
                case 1:
                    {
                        ApiManagementResource.Star.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        arg.item.starred = 0;
                                        break;
                                    }
                            }
                        });
                        break;
                    }
            }
        }

        /**
         * @function [删除功能函数] [delete]
         */
        vm.data.fun.delete = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: '[' + arg.item.apiID + ']'
                }
            }
            switch (arg.switch) {
                case 0:
                    {
                        $rootScope.EnsureModal($filter('translate')('012100230'), false, $filter('translate')('012100231'), {}, function(callback) {
                            if (callback) {
                                ApiManagementResource.Api.Delete(template.request).$promise
                                    .then(function(response) {
                                        switch (response.statusCode) {
                                            case CODE.COMMON.SUCCESS:
                                                {
                                                    vm.data.service.home.envObject.object.model.splice(arg.$index, 1);
                                                    $rootScope.InfoModal($filter('translate')('012100232'), 'success');
                                                    break;
                                                }
                                            default:
                                                {
                                                    $rootScope.InfoModal($filter('translate')('012100233'), 'error');
                                                    break;
                                                }
                                        }
                                    })
                            }
                        });
                        break;
                    }
                case 1:
                    {
                        $rootScope.EnsureModal($filter('translate')('012100234'), false, $filter('translate')('012100235'), {}, function(callback) {
                            if (callback) {
                                ApiManagementResource.Trash.Delete(template.request).$promise
                                    .then(function(response) {
                                        switch (response.statusCode) {
                                            case CODE.COMMON.SUCCESS:
                                                {
                                                    vm.data.service.home.envObject.object.model.splice(arg.$index, 1);
                                                    $rootScope.InfoModal($filter('translate')('012100236'), 'success');
                                                    break;
                                                }
                                            default:
                                                {
                                                    $rootScope.InfoModal($filter('translate')('012100233'), 'error');
                                                    break;
                                                }
                                        }
                                    })
                            }
                        });
                        break;
                    }
            }
        }

        /**
         * @function [恢复功能函数] [recover]
         */
        vm.data.fun.recover = function(arg) {
            arg = arg || {};
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            var template = {
                modal: {
                    group: {
                        parent: GroupService.get(),
                        title: $filter('translate')('012100237')
                    }
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: '[' + arg.item.apiID + ']',
                    groupID: ''
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
                                        vm.data.service.home.envObject.object.model.splice(arg.$index, 1);
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [清空功能函数] [clean]
         */
        vm.data.fun.clean = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100240'), false, $filter('translate')('012100241'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Trash.Clean(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100242'), 'success');
                                        vm.data.service.home.envObject.object.model = [];
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [查看详情功能函数] [see details]
         */
        vm.data.fun.enter = function(arg) {
            var template = {
                uri: {
                    groupID: vm.data.interaction.request.groupID,
                    childGroupID: vm.data.interaction.request.childGroupID,
                    grandSonGroupID: vm.data.interaction.request.grandSonGroupID,
                    apiID: arg.item.apiID
                },
                $index: vm.data.interaction.request.apiID.indexOf(arg.item.apiID)
            }
            if (vm.data.info.batch.disable) {
                arg.item.isClick = !arg.item.isClick;
                if (arg.item.isClick) {
                    vm.data.interaction.request.apiID.push(arg.item.apiID);
                    vm.data.info.batch.address.push(arg.$index);
                } else {
                    vm.data.interaction.request.apiID.splice(template.$index, 1);
                    vm.data.info.batch.address.splice(template.$index, 1);
                }
            } else {
                $state.go('home.project.inside.api.detail', template.uri);
            }
        }

        /**
         * @function [存储位置排序] [Storage location sort]
         */
        vm.data.fun.batch.sort = function(pre, next) {
            return pre - next;
        }

        /**
         * @function [默认切换函数] [Default switch function]
         */
        vm.data.fun.batch.default = function() {
            if (vm.data.service.home.envObject.object.model && vm.data.service.home.envObject.object.model.length > 0) {
                vm.data.info.batch.disable = true;
                angular.forEach(vm.data.info.batch.address, function(val, key) {
                    vm.data.service.home.envObject.object.model[val].isClick = false;
                })
                vm.data.info.batch.address = [];
                vm.data.interaction.request.apiID = [];
                $rootScope.InfoModal($filter('translate')('012100243'), 'success');
            } else {
                $rootScope.InfoModal($filter('translate')('012100244'), 'error');
            }
        }

        /**
         * @function [批量修改分组函数] [Batch modify grouping function]
         */
        vm.data.fun.batch.moveGroup = function() {
            var template = {
                modal: {
                    group: {
                        parent: GroupService.get(),
                        title: $filter('translate')('012100253')
                    },
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: JSON.stringify(vm.data.interaction.request.apiID),
                    groupID: ''
                },
                loop: {
                    num: 0
                }
            }
            if (!template.modal.group.parent) {
                $rootScope.InfoModal($filter('translate')('012100254'), 'error');
                return;
            }
            $rootScope.ApiRecoverModal(template.modal, function(callback) {
                if (callback) {
                    template.request.groupID = callback.groupID;
                    ApiManagementResource.Api.Move(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        angular.forEach(vm.data.info.batch.address.sort(vm.data.fun.batch.sort), function(val, key) {
                                            val = val - template.loop.num++;
                                            vm.data.service.home.envObject.object.model.splice(val, 1);
                                        })
                                        vm.data.info.batch.disable = false;
                                        vm.data.interaction.request.apiID = [];
                                        vm.data.info.batch.address = [];
                                        $rootScope.InfoModal($filter('translate')('012100255'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100256'), 'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }
        /**
         * @function [批量删除功能函数] [batch deletion]
         */
        vm.data.fun.batch.delete = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: JSON.stringify(vm.data.interaction.request.apiID)
                },
                loop: {
                    num: 0
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100234'), false, $filter('translate')('012100235'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Trash.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        angular.forEach(vm.data.info.batch.address.sort(vm.data.fun.batch.sort), function(val, key) {
                                            val = val - template.loop.num++;
                                            vm.data.service.home.envObject.object.model.splice(val, 1);
                                        })
                                        vm.data.interaction.request.apiID = [];
                                        vm.data.info.batch.address = [];
                                        vm.data.info.batch.disable = false;
                                        $rootScope.InfoModal($filter('translate')('012100236'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100233'), 'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [批量恢复功能函数] [Batch recovery]
         */
        vm.data.fun.batch.recover = function() {
            var template = {
                modal: {
                    group: {
                        parent: GroupService.get(),
                        title: $filter('translate')('012100237')
                    }
                },
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: JSON.stringify(vm.data.interaction.request.apiID),
                    groupID: ''
                },
                loop: {
                    num: 0
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
                                        angular.forEach(vm.data.info.batch.address.sort(vm.data.fun.batch.sort), function(val, key) {
                                            val = val - template.loop.num++;
                                            vm.data.service.home.envObject.object.model.splice(val, 1);
                                        })
                                        vm.data.info.batch.disable = false;
                                        vm.data.interaction.request.apiID = [];
                                        vm.data.info.batch.address = [];
                                        $rootScope.InfoModal($filter('translate')('012100245'), 'success');
                                        break;
                                    }
                            }
                        })
                }
            });
        }

        /**
         * @function [批量移入回收站功能函数] [Move in bulk to the Recycle Bin]
         */
        vm.data.fun.batch.remove = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    apiID: JSON.stringify(vm.data.interaction.request.apiID)
                },
                loop: {
                    num: 0
                }
            }
            $rootScope.EnsureModal($filter('translate')('012100230'), false, $filter('translate')('012100231'), {}, function(callback) {
                if (callback) {
                    ApiManagementResource.Api.Delete(template.request).$promise
                        .then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        angular.forEach(vm.data.info.batch.address.sort(vm.data.fun.batch.sort), function(val, key) {
                                            val = val - template.loop.num++;
                                            vm.data.service.home.envObject.object.model.splice(val, 1);
                                        })
                                        vm.data.info.batch.disable = false;
                                        vm.data.interaction.request.apiID = [];
                                        vm.data.info.batch.address = [];
                                        $rootScope.InfoModal($filter('translate')('012100232'), 'success');
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal($filter('translate')('012100233'), 'error');
                                        break;
                                    }
                            }
                        })
                }
            });
        }


        /**
         * @function [辅助初始化功能函数] [Auxiliary initialization]
         */
        vm.data.assistantFun.init = function() {
            var template = {
                request: {
                    projectID: vm.data.interaction.request.projectID,
                    groupID: vm.data.interaction.request.grandSonGroupID||vm.data.interaction.request.childGroupID || vm.data.interaction.request.groupID,
                    orderBy: vm.data.info.sort.current.orderBy,
                    asc: vm.data.info.sort.current.asc,
                    tips: vm.data.interaction.request.tips
                }
            }

            if (vm.data.interaction.request.groupID == -2) {
                $rootScope.global.ajax.Query_Api = ApiManagementResource.Trash.Query(template.request);
                $rootScope.global.ajax.Query_Api.$promise.then(function(response) {
                    vm.data.service.home.envObject.object.model = response.apiList || [];
                    vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
                    $scope.$emit('$translateferStation', {
                        state: '$EnvInitReady',
                        data: {
                            status: 0
                        }
                    });
                })
            } else {
                if (vm.data.interaction.request.tips) {
                    $rootScope.global.ajax.Query_Api = ApiManagementResource.Api.Search(template.request);
                    $rootScope.global.ajax.Query_Api.$promise.then(function(response) {
                        vm.data.service.home.envObject.object.model = response.apiList || [];
                        vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
                        $scope.$emit('$translateferStation', {
                            state: '$EnvInitReady',
                            data: {
                                status: 0
                            }
                        });
                    })
                } else if (vm.data.interaction.request.groupID == -1) {
                    $rootScope.global.ajax.Query_Api = ApiManagementResource.Api.All(template.request);
                    $rootScope.global.ajax.Query_Api.$promise.then(function(response) {
                        vm.data.service.home.envObject.object.model = response.apiList || [];
                        vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
                        $scope.$emit('$translateferStation', {
                            state: '$EnvInitReady',
                            data: {
                                status: 0
                            }
                        });
                    })
                } else {
                    $rootScope.global.ajax.Query_Api = ApiManagementResource.Api.Query(template.request);
                    $rootScope.global.ajax.Query_Api.$promise.then(function(response) {
                        vm.data.service.home.envObject.object.model = response.apiList || [];
                        vm.data.info.template.envModel = vm.data.service.home.envObject.object.model;
                        $scope.$emit('$translateferStation', {
                            state: '$EnvInitReady',
                            data: {
                                status: 0
                            }
                        });
                    })
                }
            }
            return $rootScope.global.ajax.Query_Api.$promise;
        }

        /**
         * @function [初始化功能函数] [initialization]
         */
        vm.data.fun.init = function(arg) {
            arg = arg || {};
            switch (arg.status) {
                case 'import':
                    {
                        return vm.data.fun.load(arg)
                        break;
                    }
                default:
                    {
                        return vm.data.assistantFun.init();
                        break;
                    }
            }
        }

        vm.$onInit = function() {
            $scope.$emit('$WindowTitleSet', {
                list: [$filter('translate')('012100247'), $state.params.projectName, 'API开发管理']
            });
        }
    }
})();