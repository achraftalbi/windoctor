'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testEntity4', {
                parent: 'entity',
                url: '/testEntity4s',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity4.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity4/testEntity4s.html',
                        controller: 'TestEntity4Controller'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity4');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testEntity4.detail', {
                parent: 'entity',
                url: '/testEntity4/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity4.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity4/testEntity4-detail.html',
                        controller: 'TestEntity4DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity4');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestEntity4', function($stateParams, TestEntity4) {
                        return TestEntity4.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testEntity4.new', {
                parent: 'testEntity4',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity4/testEntity4-dialog.html',
                        controller: 'TestEntity4DialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity4', null, { reload: true });
                    }, function() {
                        $state.go('testEntity4');
                    })
                }]
            })
            .state('testEntity4.edit', {
                parent: 'testEntity4',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity4/testEntity4-dialog.html',
                        controller: 'TestEntity4DialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestEntity4', function(TestEntity4) {
                                return TestEntity4.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity4', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
