'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logout', {
                parent: 'account',
                url: '/logout',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'LogoutController'
                    }
                },
                ncyBreadcrumb: {
                    label: 'global.menu.account.logout'// angular-breadcrumb's configuration
                }

            });
    });
