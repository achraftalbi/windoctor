'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testEntity5', {
                parent: 'entity',
                url: '/testEntity5s',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity5.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity5/testEntity5s.html',
                        controller: 'TestEntity5Controller'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity5');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testEntity5.detail', {
                parent: 'entity',
                url: '/testEntity5/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.testEntity5.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testEntity5/testEntity5-detail.html',
                        controller: 'TestEntity5DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testEntity5');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestEntity5', function($stateParams, TestEntity5) {
                        return TestEntity5.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testEntity5.new', {
                parent: 'testEntity5',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity5/testEntity5-dialog.html',
                        controller: 'TestEntity5DialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description5: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity5', null, { reload: true });
                    }, function() {
                        $state.go('testEntity5');
                    })
                }]
            })
            .state('testEntity5.edit', {
                parent: 'testEntity5',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testEntity5/testEntity5-dialog.html',
                        controller: 'TestEntity5DialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestEntity5', function(TestEntity5) {
                                return TestEntity5.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testEntity5', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
