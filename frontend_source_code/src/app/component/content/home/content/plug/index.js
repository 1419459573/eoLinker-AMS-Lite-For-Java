(function() {
    'use strict';
    angular.module('eolinker')

    .config(['$stateProvider', 'RouteHelpersProvider', function($stateProvider, helper) {
        $stateProvider
            .state('home.plug', {
                url: '/plug',
                template: '<div class="home-plug"><home-plug-sidebar ></home-plug-sidebar>' +
                '<div ui-view></div></div>'
            });
    }])
})();
