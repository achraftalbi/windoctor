'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('status', {
                parent: 'entity',
                url: '/statuss',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.status.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/status/statuss.html',
                        controller: 'StatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('status.detail', {
                parent: 'entity',
                url: '/status/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.status.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/status/status-detail.html',
                        controller: 'StatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Status', function($stateParams, Status) {
                        return Status.get({id : $stateParams.id});
                    }]
                }
            })
            .state('status.new', {
                parent: 'status',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/status/status-dialog.html',
                        controller: 'StatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('status', null, { reload: true });
                    }, function() {
                        $state.go('status');
                    })
                }]
            })
            .state('status.edit', {
                parent: 'status',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/status/status-dialog.html',
                        controller: 'StatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Status', function(Status) {
                                return Status.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('status', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
