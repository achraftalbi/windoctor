'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('test6Entity', {
                parent: 'entity',
                url: '/test6Entitys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.test6Entity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/test6Entity/test6Entitys.html',
                        controller: 'Test6EntityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('test6Entity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('test6Entity.detail', {
                parent: 'entity',
                url: '/test6Entity/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.test6Entity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/test6Entity/test6Entity-detail.html',
                        controller: 'Test6EntityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('test6Entity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Test6Entity', function($stateParams, Test6Entity) {
                        return Test6Entity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('test6Entity.new', {
                parent: 'test6Entity',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/test6Entity/test6Entity-dialog.html',
                        controller: 'Test6EntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('test6Entity', null, { reload: true });
                    }, function() {
                        $state.go('test6Entity');
                    })
                }]
            })
            .state('test6Entity.edit', {
                parent: 'test6Entity',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/test6Entity/test6Entity-dialog.html',
                        controller: 'Test6EntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Test6Entity', function(Test6Entity) {
                                return Test6Entity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('test6Entity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
