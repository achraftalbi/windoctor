'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entityTest1', {
                parent: 'entity',
                url: '/entityTest1s',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.entityTest1.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entityTest1/entityTest1s.html',
                        controller: 'EntityTest1Controller'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entityTest1');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('entityTest1.detail', {
                parent: 'entity',
                url: '/entityTest1/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.entityTest1.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entityTest1/entityTest1-detail.html',
                        controller: 'EntityTest1DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entityTest1');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'EntityTest1', function($stateParams, EntityTest1) {
                        return EntityTest1.get({id : $stateParams.id});
                    }]
                }
            })
            .state('entityTest1.new', {
                parent: 'entityTest1',
                url: '/new',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/entityTest1/entityTest1-dialog.html',
                        controller: 'EntityTest1DialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('entityTest1', null, { reload: true });
                    }, function() {
                        $state.go('entityTest1');
                    })
                }]
            })
            .state('entityTest1.edit', {
                parent: 'entityTest1',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/entityTest1/entityTest1-dialog.html',
                        controller: 'EntityTest1DialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EntityTest1', function(EntityTest1) {
                                return EntityTest1.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('entityTest1', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
