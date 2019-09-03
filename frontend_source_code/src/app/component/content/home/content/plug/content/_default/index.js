(function() {
    'use strict';
    angular.module('eolinker')
        .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
            $stateProvider
                .state('home.plug.default', {
                    url: '/?status',
                    template: '<home-plug-default></home-plug-default>'
                });
        }])
        .component('homePlugDefault', {
            templateUrl: 'app/component/content/home/content/plug/content/_default/index.html',
            controller: indexController
        })

    indexController.$inject = ['$window', '$scope', '$state', 'CODE', '$rootScope','PLUG_CONSTANT'];

    function indexController($window, $scope, $state, CODE, $rootScope,PLUG_CONSTANT) {
        var vm = this;
        vm.data = {
            service: {
                $window: $window
            },
            info: {
                query: PLUG_CONSTANT.QUERY,
                status: $state.params.status || '-1'
            }
        }
        vm.$onInit = function() {
            $scope.$emit('$WindowTitleSet', { list: ['插件管理'] });
        }
    }
})();
