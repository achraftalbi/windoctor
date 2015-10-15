'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testEntity3', {
                parent: 'entity',
                url: '/testEntity3s',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity3.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity3/testEntity3s.html',
                        controller: 'TestEntity3Controller'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity3');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testEntity3.detail', {
                parent: 'entity',
                url: '/testEntity3/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity3.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity3/testEntity3-detail.html',
                        controller: 'TestEntity3DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity3');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestEntity3', function($stateParams, TestEntity3) {
                        return TestEntity3.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testEntity3.new', {
                parent: 'testEntity3',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity3/testEntity3-dialog.html',
                        controller: 'TestEntity3DialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity3', null, { reload: true });
                    }, function() {
                        $state.go('testEntity3');
                    })
                }]
            })
            .state('testEntity3.edit', {
                parent: 'testEntity3',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity3/testEntity3-dialog.html',
                        controller: 'TestEntity3DialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestEntity3', function(TestEntity3) {
                                return TestEntity3.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity3', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
