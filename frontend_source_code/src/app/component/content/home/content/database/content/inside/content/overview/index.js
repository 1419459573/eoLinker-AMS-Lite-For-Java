(function () {
    //author：广州银云信息科技有限公司
    'use strict';
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function ($stateProvider, helper) {
            $stateProvider
                .state('home.database.inside.overview', {
                    url: '/overview',
                    template: '<home-database-inside-overview></home-database-inside-overview>'
                });
        }])
        .component('homeDatabaseInsideOverview', {
            templateUrl: 'app/component/content/home/content/database/content/inside/content/overview/index.html',
            controller: indexController
        })

    indexController.$inject = ['$scope', 'DatabaseResource', '$state', 'CODE', '$rootScope'];

    function indexController($scope, DatabaseResource, $state, CODE, $rootScope) {

        var vm = this;
        vm.data = {
            info:{
                edit:parseInt($state.params.userType||'0') < 2
            },
            interaction: {
                request: {
                    databaseID: $state.params.databaseID
                },
                response: {
                    databaseInfo: null
                }
            },
            fun: {
                init: null, //初始化功能函数
                menu: null, //菜单功能函数
                edit: null, //编辑功能函数
            }
        }

        vm.data.fun.init = function () {
            var template = {
                promise: null,
                request: {
                    dbID: vm.data.interaction.request.databaseID
                }
            }
            $scope.$emit('$WindowTitleSet', {
                list: ['概括', '数据库结构管理']
            });
            template.promise = DatabaseResource.Database.Detail(template.request).$promise;
            template.promise.then(function (response) {
                switch (response.statusCode) {
                    case CODE.COMMON.SUCCESS:
                        {
                            vm.data.interaction.response.databaseInfo = response.databaseInfo;
                            vm.data.interaction.response.databaseInfo.tableCount=response.tableCount;
                            vm.data.interaction.response.databaseInfo.fieldCount=response.fieldCount;
                            break;
                        }
                }
            })
            return template.promise;
        }
        vm.data.fun.dump = function() {
            var template={
                modal:{
                    title:'导出数据字典',
                    dbID:vm.data.interaction.request.databaseID
                }
            }
            $rootScope.ExportDatabaseModal(template.modal, function(callback) {});
        }
        vm.data.fun.edit = function () {
            vm.data.interaction.response.databaseInfo.dbID = vm.data.interaction.request.databaseID;
            var template = {
                modal: {
                    title: '修改数据库' ,
                    interaction:{
                        request:vm.data.interaction.response.databaseInfo
                    }
                },
                request: {}
            }

            $rootScope.DatabaseModal(template.modal, function (callback) {
                if (callback) {
                    vm.data.interaction.response.databaseInfo.dbName = callback.dbName;
                    vm.data.interaction.response.databaseInfo.dbVersion = callback.dbVersion;
                    $rootScope.InfoModal('修改数据库成功', 'success');
                }
            });
        }
    }
})();