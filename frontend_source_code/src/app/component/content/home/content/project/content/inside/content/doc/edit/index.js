(function () {
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
     * @function [项目文档编辑相关js] [Project Document Edit Related js]
     * @version  3.1.6
     * @service  $scope [注入作用域服务] [Injection scope service]
     * @service  $rootScope [注入根作用域服务] [Injection rootscope service]
     * @service  ApiManagementResource [注入接口管理接口服务] [inject ApiManagement API service]
     * @service  $state [注入路由服务] [Injection state service]
     * @service  GroupService [注入GroupService服务] [Injection GroupService service]
     * @service  $filter [注入过滤器服务] [Injection filter service]
     * @constant CODE [注入状态码常量] [inject status code constant service]
     */
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function ($stateProvider, helper) {
            $stateProvider
                .state('home.project.inside.doc.edit', {
                    url: '/edit?groupID?childGroupID?grandSonGroupID?documentID?type',
                    template: '<home-project-inside-doc-edit></home-project-inside-doc-edit>',
                    resolve: helper.resolveFor('JQUERY', 'WANG_EDITOR', 'MARKDOWN', 'QINIU_UPLOAD')
                });
        }])
        .component('homeProjectInsideDocEdit', {
            templateUrl: 'app/component/content/home/content/project/content/inside/content/doc/edit/index.html',
            controller: homeProjectInsideDocEditController
        })

    homeProjectInsideDocEditController.$inject = ['$scope', 'ApiManagementResource', '$state', 'CODE', '$rootScope', 'GroupService', '$filter', 'HTML_LAZYLOAD'];

    function homeProjectInsideDocEditController($scope, ApiManagementResource, $state, CODE, $rootScope, GroupService, $filter, HTML_LAZYLOAD) {
        var vm = this;
        var code = CODE.COMMON.SUCCESS;
        vm.data = {
            constant: {
                lazyload: HTML_LAZYLOAD[1]
            },
            info: {
                input: {
                    disable: false,
                    submited: false
                },
                group: {
                    parent: [],
                    child: []
                },
                reset: {
                    projectID: $state.params.projectID,
                    groupID: $state.params.groupID,
                    childGroupID: $state.params.childGroupID,
                    grandSonGroupID: $state.params.grandSonGroupID,
                    documentID: $state.params.documentID,
                    type: $state.params.type
                }
            },
            interaction: {
                response: {
                    docInfo: {
                        projectID: $state.params.projectID,
                        groupID: $state.params.groupID,
                        childGroupID: $state.params.childGroupID,
                        grandSonGroupID: $state.params.grandSonGroupID,
                        documentID: $state.params.documentID,
                        title: '',
                        docRichNote: '',
                        docMarkdownNote: '',
                        contentRaw: '',
                        contentType: '0'
                    }
                }
            },
            fun: {
                init: null,
                load: null,
                requestProcessing: null,
                menu: null,
                change: {
                    group: null,
                    noteType: null,
                },
                back: null,
            },
            assistantFun: {
                init: null,
                confirm: null,
                keep: null,
                edit: null
            }
        }
        var data = {
            cache: {
                child: '[]',
                grandson: '[]'
            }
        }
        /**
         * @function [辅助初始化功能函数] [Auxiliary initialization function]
         */
        vm.data.assistantFun.init = function () {
            var docGroup = GroupService.get();
            vm.data.info.group.parent = docGroup;
            $scope.$emit('$WindowTitleSet', {
                list: [$filter('translate')('012100084'), $state.params.projectName, 'API开发管理']
            });
            if (vm.data.interaction.response.docInfo.groupID > 0) {
                for (var i = 0; i < vm.data.info.group.parent.length; i++) {
                    var val = vm.data.info.group.parent[i];
                    if (val.groupID == vm.data.interaction.response.docInfo.groupID) {
                        vm.data.info.group.child = [{
                            groupID: -1,
                            groupName: '可选[二级菜单]',
                            childGroupList: []
                        }].concat(val.childGroupList);
                        vm.data.info.group.grandson = [{
                            groupID: -1,
                            groupName: '可选[三级菜单]'
                        }];
                        if (vm.data.interaction.response.docInfo.childGroupID > 0) {
                            for (var childKey in val.childGroupList) {
                                var childVal = val.childGroupList[childKey];
                                if (childVal.groupID == vm.data.interaction.response.docInfo.childGroupID) {
                                    vm.data.info.group.grandson = vm.data.info.group.grandson.concat(childVal.childGroupList);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                vm.data.info.group.child = [{
                    groupID: -1,
                    groupName: '可选[二级菜单]',
                    childGroupList: []
                }].concat(vm.data.info.group.parent[0].childGroupList);
                vm.data.info.group.grandson = [{
                    groupID: -1,
                    groupName: '可选[三级菜单]'
                }].concat(vm.data.info.group.child[0].childGroupList);
            }
            if (vm.data.info.reset.pageID || vm.data.interaction.response.docInfo.groupID > 0) {
                vm.data.interaction.response.docInfo.groupID = parseInt(vm.data.interaction.response.docInfo.groupID);
                if (vm.data.interaction.response.docInfo.childGroupID) {
                    vm.data.interaction.response.docInfo.childGroupID = parseInt(vm.data.interaction.response.docInfo.childGroupID);
                } else {
                    vm.data.interaction.response.docInfo.childGroupID = -1;
                }
                if (vm.data.interaction.response.docInfo.grandSonGroupID) {
                    vm.data.interaction.response.docInfo.grandSonGroupID = parseInt(vm.data.interaction.response.docInfo.grandSonGroupID);
                } else {
                    vm.data.interaction.response.docInfo.grandSonGroupID = -1;
                }
            } else {
                vm.data.interaction.response.docInfo.groupID = vm.data.info.group.parent[0].groupID;
                vm.data.interaction.response.docInfo.childGroupID = -1;
                vm.data.interaction.response.docInfo.grandSonGroupID = -1;
            }
            data.cache.child = JSON.stringify(vm.data.info.group.child);
            data.cache.grandson = JSON.stringify(vm.data.info.group.grandson);
        }
        /**
         * @function [初始化功能函数] [initialization function]
         */
        vm.data.fun.init = function () {
            var template = {
                cache: {
                    group: GroupService.get()
                }
            }
            if (template.cache.group) {
                if (vm.data.info.reset.documentID) {
                    ApiManagementResource.Doc.Detail({
                        documentID: vm.data.info.reset.documentID,
                        groupID: vm.data.info.reset.grandSonGroupID || vm.data.info.reset.childGroupID || vm.data.info.reset.groupID,
                        projectID: vm.data.info.reset.projectID
                    }).$promise.then(function (response) {
                        if (code == response.statusCode) {
                            vm.data.interaction.response.docInfo = response.documentInfo;
                            vm.data.interaction.response.docInfo.contentType = "" + vm.data.interaction.response.docInfo.contentType;
                            vm.data.interaction.response.docInfo.docRichNote = vm.data.interaction.response.docInfo.contentType == '0' ? vm.data.interaction.response.docInfo.content : '';
                            vm.data.interaction.response.docInfo.docMarkdownNote = vm.data.interaction.response.docInfo.contentType == '1' ? vm.data.interaction.response.docInfo.content : '';
                            $scope.$emit('$windowTitle', {
                                apiName: (vm.data.info.reset.type == 2 ? $filter('translate')('012100086') : $filter('translate')('012100087')) + vm.data.interaction.response.docInfo.title
                            });
                            if (!vm.data.interaction.response.docInfo.topParentGroupID) {
                                if (vm.data.interaction.response.docInfo.parentGroupID) {
                                    vm.data.interaction.response.docInfo.childGroupID = response.documentInfo.groupID;
                                    vm.data.interaction.response.docInfo.groupID = response.documentInfo.parentGroupID;
                                } else {
                                    vm.data.interaction.response.docInfo.childGroupID = -1;
                                    vm.data.interaction.response.docInfo.grandSonGroupID = -1;
                                }
                            } else {
                                vm.data.interaction.response.docInfo.grandSonGroupID = response.documentInfo.groupID;
                                vm.data.interaction.response.docInfo.childGroupID = response.documentInfo.parentGroupID;
                                vm.data.interaction.response.docInfo.groupID = response.documentInfo.topParentGroupID;
                            }
                            if (vm.data.interaction.response.docInfo.contentType == '1') {
                                $scope.$broadcast('$changeNoteType');
                            }
                            vm.data.assistantFun.init();
                        }
                    });
                } else {
                    vm.data.assistantFun.init();
                    $scope.$emit('$windowTitle', {
                        apiName: $filter('translate')('012100088')
                    });
                    vm.data.interaction.response.docInfo.contentType = '0';
                    vm.data.interaction.response.docInfo.title = '';
                }
            }

        }
        vm.data.fun.init();
        /**
         * @function [更改父分组] [Change the parent group]
         */
        vm.data.fun.change.group = function (status) {
            switch (status) {
                case 'first-level':
                    {
                        for (var i = 0; i < vm.data.info.group.parent.length; i++) {
                            var val = vm.data.info.group.parent[i];
                            if (val.groupID == vm.data.interaction.response.docInfo.groupID) {
                                vm.data.info.group.child = [{
                                    groupID: -1,
                                    groupName: '可选[二级菜单]',
                                    childGroupList: []
                                }].concat(val.childGroupList);
                                vm.data.interaction.response.docInfo.childGroupID = -1;
                                vm.data.interaction.response.docInfo.grandSonGroupID = -1;
                                break;
                            }
                        }
                        break;
                    }
                case 'second-level':
                    {
                        for (var i = 0; i < vm.data.info.group.child.length; i++) {
                            var val = vm.data.info.group.child[i];
                            if (val.groupID == vm.data.interaction.response.docInfo.childGroupID) {
                                vm.data.info.group.grandson = [{
                                    groupID: -1,
                                    groupName: '可选[三级菜单]'
                                }].concat(val.childGroupList);
                                vm.data.interaction.response.docInfo.grandSonGroupID = -1;
                                break;
                            }
                        }
                        break;
                    }
            }

        }
        /**
         * @function [更改备注说明类型] [Change the note description type]
         */
        vm.data.fun.change.noteType = function () {
            $scope.$broadcast('$changeNoteType');
        }
        /**
         * @function [返回功能函数] [Back to function]
         */
        vm.data.fun.back = function () {
            if (vm.data.info.reset.documentID) {
                $state.go('home.project.inside.doc.detail', {
                    'groupID': vm.data.info.reset.groupID,
                    'childGroupID': vm.data.info.reset.childGroupID,
                    'grandSonGroupID': vm.data.info.reset.grandSonGroupID,
                    'documentID': vm.data.info.reset.documentID
                });
            } else {
                $state.go('home.project.inside.doc.list', {
                    'groupID': vm.data.info.reset.groupID,
                    'grandSonGroupID': vm.data.info.reset.grandSonGroupID,
                    'childGroupID': vm.data.info.reset.childGroupID
                });
            }
        }
        /**
         * @function [辅助确认功能函数] [Auxiliary confirmation function]
         */
        vm.data.assistantFun.confirm = function () {
            var info = {
                projectID: vm.data.info.reset.projectID,
                groupID: vm.data.interaction.response.docInfo.grandSonGroupID > 0 ? vm.data.interaction.response.docInfo.grandSonGroupID : vm.data.interaction.response.docInfo.childGroupID > 0 ? vm.data.interaction.response.docInfo.childGroupID : vm.data.interaction.response.docInfo.groupID,
                documentID: vm.data.info.reset.documentID,
                title: vm.data.interaction.response.docInfo.title,
                docHeader: vm.data.interaction.response.docInfo.docHeader,
                content: vm.data.interaction.response.docInfo.contentType == '1' ? vm.data.interaction.response.docInfo.docMarkdownNote : vm.data.interaction.response.docInfo.docRichNote,
                contentRaw: vm.data.interaction.response.docInfo.contentRaw,
                contentType: vm.data.interaction.response.docInfo.contentType
            }
            return info;
        }
        /**
         * @function [编辑相关系列按钮功能函数] [Edit related series button function]
         */
        vm.data.fun.load = function (arg) {
            $scope.$emit('$translateferStation', {
                state: '$LoadingInit',
                data: arg
            });
        }
        /**
         * @function [发送存储请求时预处理功能函数] [Preprocessing function when sending a store request]
         */
        vm.data.fun.requestProcessing = function (arg) { //arg status:（0：继续添加 1：快速保存，2：编辑（修改/新增））
            var template = {
                request: vm.data.assistantFun.confirm(),
                promise: null
            }
            if ($scope.editForm.$valid && !!template.request.content) {
                vm.data.info.input.disable = true;
                switch (arg.status) {
                    case 0:
                        {
                            template.promise = vm.data.assistantFun.keep({
                                request: template.request
                            });
                            break;
                        }
                    case 1:
                        {
                            template.promise = vm.data.assistantFun.edit({
                                request: template.request
                            });
                            break;
                        }
                }
            } else {
                $rootScope.InfoModal($filter('translate')('012100089'), 'error');
                vm.data.info.input.submited = true;
            }
            return template.promise;
        }
        /**
         * @function [辅助继续添加功能函数] [Assist to continue adding function functions]
         */
        vm.data.assistantFun.keep = function (arg) {
            var template = {
                promise: null
            }
            template.promise = ApiManagementResource.Doc.Add(arg.request).$promise;
            template.promise.then(function (response) {
                vm.data.info.input.disable = false;
                if (response.statusCode == code) {
                    $rootScope.InfoModal($filter('translate')('012100090'), 'success');
                    vm.data.interaction.response.docInfo = {
                        projectID: vm.data.info.reset.projectID,
                        groupID: vm.data.info.reset.groupID == '-1' ? vm.data.info.group.parent[0].groupID : parseInt(vm.data.info.reset.groupID),
                        title: '',
                        docMarkdownNote: '',
                        docRichNote: '',
                        contentType: '0',
                        contentRaw: ''
                    };
                    vm.data.info.group.child = JSON.parse(data.cache.child);
                    vm.data.info.group.grandson = JSON.parse(data.cache.grandson);
                    vm.data.interaction.response.docInfo.groupID = vm.data.info.reset.groupID == '-1' ? vm.data.info.group.parent[0].groupID : parseInt(vm.data.info.reset.groupID);
                    vm.data.interaction.response.docInfo.childGroupID = parseInt(vm.data.info.reset.childGroupID) || -1;
                    vm.data.interaction.response.docInfo.grandSonGroupID = parseInt(vm.data.info.reset.grandSonGroupID) || -1;
                    $scope.$broadcast('$resetWangEditor');
                    $scope.$broadcast('$resetMarkdown');
                    vm.data.info.input.submited = false;
                    window.scrollTo(0, 0);
                }
            })
            return template.promise;
        }
        /**
         * @function [编辑功能函数] [Edit]
         */
        vm.data.assistantFun.edit = function (arg) {
            var template = {
                promise: null
            }
            if (vm.data.info.reset.documentID && $state.params.type != 2) {
                vm.data.info.input.disable = true;
                template.promise = ApiManagementResource.Doc.Edit(arg.request).$promise;
                template.promise.then(function (response) {
                    vm.data.info.input.disable = false;
                    if (response.statusCode == code) {
                        $state.go('home.project.inside.doc.detail', {
                            'groupID': vm.data.info.reset.groupID,
                            'childGroupID': vm.data.info.reset.childGroupID,
                            'grandSonGroupID': vm.data.info.reset.grandSonGroupID,
                            'documentID': vm.data.info.reset.documentID
                        });
                        $rootScope.InfoModal($filter('translate')('012100091'), 'success');
                    }
                })
            } else {
                vm.data.info.input.disable = true;
                template.promise = ApiManagementResource.Doc.Add(arg.request).$promise;
                template.promise.then(function (response) {
                    vm.data.info.input.disable = false;
                    if (response.statusCode == code) {
                        $state.go('home.project.inside.doc.detail', {
                            'groupID': vm.data.info.reset.groupID,
                            'childGroupID': vm.data.info.reset.childGroupID,
                            'grandSonGroupID': vm.data.info.reset.grandSonGroupID,
                            'documentID': response.documentID
                        });
                        $rootScope.InfoModal($filter('translate')('012100090'), 'success');
                    }
                })
            }
            return template.promise;
        }

        $scope.$on('$SidebarFinish', function () {
            vm.data.fun.init();
        })
    }
})();