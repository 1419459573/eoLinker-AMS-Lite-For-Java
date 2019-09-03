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
     * @description 侧边栏公用服务
     * @required GroupService 
     */
    angular.module('eolinker.service')
        .factory('Group_AmsCommonService', index);

    index.$inject = ['GroupService', 'CODE', '$rootScope','$filter']

    function index(GroupService, CODE, $rootScope,$filter) {
        var data = {
            service: GroupService,
            fun: {
                clear: null,
                spreed: null,
                operate: null
            },
            sort: {
                operate: null,
                init: null,
            }
        }


        /**
         * 分组操作
         * @param {string} status 操作状态
         * @param {object} arg 传参
         * @param {object} options 选项{callback:回调函数(选填),resource:请求资源,originGroupQuery:原始分组队列,status:状态（child），默认父分组}
         */
        data.fun.operate = function(status, arg, options) {
            var template = {
                modal: {},
                $index: null
            }
            switch (status) {
                case 'edit':
                    {
                        template.modal = {
                            title: (options.status=='edit' ? '修改' : '新增') + (arg.parentItem ? '子分组' : '分组'),
                            secondTitle: '分组名称',
                            group: !arg.item&&options.status=='edit' ?null:options.originGroupQuery,
                            data:options.status=='edit'?arg.item:null,
                            status:arg.grandParentItem||(options.status=='add-child'&&arg.parentItem)||!arg.item?'third-level':arg.parentItem||options.status=='add-child'?'second-level':'first-level',
                            parentObject:{
                                grandParentGroupID:'0',
                                parentGroupID:'0'
                            }
                        }
                        switch(template.modal.status){
                            case 'third-level':{
                                if(options.status=='add-child'){
                                    template.modal.parentObject={
                                        grandParentGroupID:arg.parentItem?arg.parentItem.groupID:'0',
                                        parentGroupID:arg.item?arg.item.groupID:'0'
                                    };
                                }else{
                                    template.modal.parentObject={
                                        grandParentGroupID:arg.grandParentItem?arg.grandParentItem.groupID:'0',
                                        parentGroupID:arg.parentItem?arg.parentItem.groupID:'0'
                                    };
                                }
                                break;
                            }
                            case 'second-level':{
                                if(options.status=='add-child'){
                                    template.modal.parentObject={
                                        grandParentGroupID:arg.item?arg.item.groupID:'0'
                                    };
                                }else{
                                    template.modal.parentObject={
                                        grandParentGroupID:arg.parentItem?arg.parentItem.groupID:'0'
                                    };
                                }
                                
                                break;
                            }
                        }
                        $rootScope.GroupModal(template.modal, function(callback) {
                            if (callback) {
                                if(callback.grandParentGroupID>0){
                                    if(callback.parentGroupID>0){
                                        callback.isChild='2';
                                        callback.parentGroupID=callback.parentGroupID;
                                    }else{
                                        callback.isChild='1';
                                        callback.parentGroupID=callback.grandParentGroupID;
                                    }
                                }else{
                                    callback.isChild='0';
                                    callback.parentGroupID=null;
                                }
                                angular.merge(callback, callback, options.baseRequest);
                                if (options.status=='edit') {
                                    options.resource.Edit(callback).$promise.then(function(response) {
                                        switch (response.statusCode) {
                                            case CODE.COMMON.SUCCESS:
                                                {
                                                    $rootScope.InfoModal(template.modal.title + '成功', 'success');
                                                    options.callback();
                                                    break;
                                                }
                                        }
                                    });
                                } else {
                                    options.resource.Add(callback).$promise.then(function(response) {
                                        switch (response.statusCode) {
                                            case CODE.COMMON.SUCCESS:
                                                {
                                                    $rootScope.InfoModal(template.modal.title + '成功', 'success');
                                                    options.callback();
                                                    break;
                                                }
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    }
                
            }
        }

        /**
         * 清空分组服务
         */
        data.fun.clear = function() {
            data.service.clear();
        };

        /**
         * 父分组展开功能函数
         * @param {object} arg 参数，eg:{$event:dom,item:单击所处父分组项}
         */
        data.fun.spreed = function(arg) {
            if (arg.$event) {
                arg.$event.stopPropagation();
            }
            arg.item.isSpreed = !arg.item.isSpreed;
        }

        /**
         * 排序操作集合
         * @param {string} status 操作状态 
         * @param {object} arg 操作对象{baseRequest,resource,callback}
         * @return {any} promise 返回对象
         */
        data.sort.operate = function(status, arg) {
            var template = {
                request: {},
            };
            switch (status) {
                case 'confirm':
                    {
                        angular.merge(template.request, arg.baseRequest);
                        angular.forEach(arg.originQuery, function(val, key) {
                            template.request.orderNumber[val.groupID] = key;
                            angular.forEach(val.childGroupList, function(childVal, childKey) {
                                template.request.orderNumber[childVal.groupID] = childKey;
                                angular.forEach(childVal.childGroupList, function(grandSonVal, grandSonKey) {
                                    template.request.orderNumber[grandSonVal.groupID] = grandSonKey;
                                })
                            })
                        })
                        template.request.orderNumber = JSON.stringify(template.request.orderNumber);
                        arg.resource(template.request).$promise.then(function(response) {
                            switch (response.statusCode) {
                                case CODE.COMMON.SUCCESS:
                                    {
                                        $rootScope.InfoModal('排序成功', 'success');
                                        GroupService.set(arg.originQuery);
                                        break;
                                    }
                                default:
                                    {
                                        $rootScope.InfoModal('排序失败，' + ERROR_WARNING.COMMON, 'error');
                                        break;
                                    }
                            }
                            arg.callback(response);
                        });
                        break;
                    }
            }
        }

        /**
         * 分组列表排序初始整理函数
         * @param  {object} input [传入分组列表]
         * @return {array}       [返回整理后分组列表]
         */
        data.sort.init = function(input) {
            var template = {
                output: {
                    _default: [],
                    array: [],
                    childArray: [],
                    grandSonArray:[]
                },
                loop: {
                    parent: 0,
                    child: 0,
                    grandson:0
                }
            }
            try {
                template.output._default = JSON.parse(input.groupOrder||false);
                angular.forEach(input.groupList, function(val, key) {
                    template.output.childArray = [];
                    angular.forEach(val.childGroupList, function(childVal, childKey) {
                        template.output.grandSonArray=[];
                        angular.forEach(childVal.childGroupList, function(grandSonVal, grandSonKey) {
                            grandSonVal.$order = template.output._default[grandSonVal.groupID];
                            template.loop.grandson = grandSonVal.$order > (template.output.grandSonArray.length - 1) ? (template.output.grandSonArray.length - 1) : grandSonVal.$order;
                            if (template.loop.grandson >= 0) {
                                for (; template.loop.grandson >= 0; template.loop.grandson--) {
                                    if (template.output.grandSonArray[template.loop.grandson].$order <= grandSonVal.$order) {
                                        break;
                                    }
                                }
                                template.output.grandSonArray.splice(template.loop.grandson + 1, 0, grandSonVal);
                            } else {
                                template.output.grandSonArray.push(grandSonVal);
                            }
                        })
                        childVal.isSpreed = true;
                        childVal.childGroupList = template.output.grandSonArray;
                        childVal.$order = template.output._default[childVal.groupID];
                        template.loop.child = childVal.$order > (template.output.childArray.length - 1) ? (template.output.childArray.length - 1) : childVal.$order;
                        if (template.loop.child >= 0) {
                            for (; template.loop.child >= 0; template.loop.child--) {
                                if (template.output.childArray[template.loop.child].$order <= childVal.$order) {
                                    break;
                                }
                            }
                            template.output.childArray.splice(template.loop.child + 1, 0, childVal);
                        } else {
                            template.output.childArray.push(childVal);
                        }
                        
                    })
                    val.isSpreed = true;
                    val.childGroupList = template.output.childArray;
                    val.$order = template.output._default[val.groupID];
                    template.loop.parent = val.$order > (template.output.array.length - 1) ? (template.output.array.length - 1) : val.$order;
                    if (template.loop.parent >= 0) {
                        for (; template.loop.parent >= 0; template.loop.parent--) {
                            if (template.output.array[template.loop.parent].$order <= val.$order) {
                                break;
                            }
                        }
                        template.output.array.splice(template.loop.parent + 1, 0, val);
                    } else {
                        template.output.array.push(val);
                    }
                })
            } catch (e) {
                template.output.array = input.groupList;
            }
            return template.output.array;
        }
        return data;
    }
})();
