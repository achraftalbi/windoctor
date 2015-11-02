'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testEntity2', {
                parent: 'entity',
                url: '/testEntity2s',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.testEntity2.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity2/testEntity2s.html',
                        controller: 'TestEntity2Controller'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity2');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testEntity2.detail', {
                parent: 'entity',
                url: '/testEntity2/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.testEntity2.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity2/testEntity2-detail.html',
                        controller: 'TestEntity2DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity2');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestEntity2', function($stateParams, TestEntity2) {
                        return TestEntity2.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testEntity2.new', {
                parent: 'testEntity2',
                url: '/new',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity2/testEntity2-dialog.html',
                        controller: 'TestEntity2DialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity2', null, { reload: true });
                    }, function() {
                        $state.go('testEntity2');
                    })
                }]
            })
            .state('testEntity2.edit', {
                parent: 'testEntity2',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity2/testEntity2-dialog.html',
                        controller: 'TestEntity2DialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestEntity2', function(TestEntity2) {
                                return TestEntity2.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity2', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
