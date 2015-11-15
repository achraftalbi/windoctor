'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'entity',
                url: '/dashboards',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.dashboard.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dashboard/dashboards.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dashboard.detail', {
                parent: 'entity',
                url: '/dashboard/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.dashboard.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dashboard/dashboard-detail.html',
                        controller: 'DashboardDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Dashboard', function($stateParams, Dashboard) {
                        return Dashboard.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dashboard.new', {
                parent: 'dashboard',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dashboard/dashboard-dialog.html',
                        controller: 'DashboardDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {value: null, descriptionEn: null, descriptionFr: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dashboard', null, { reload: true });
                    }, function() {
                        $state.go('dashboard');
                    })
                }]
            })
            .state('dashboard.edit', {
                parent: 'dashboard',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dashboard/dashboard-dialog.html',
                        controller: 'DashboardDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Dashboard', function(Dashboard) {
                                return Dashboard.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dashboard', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
