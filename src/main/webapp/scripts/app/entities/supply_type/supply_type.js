'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('supply_type', {
                parent: 'entity',
                url: '/supply_types',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.supply_type.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supply_type/supply_types.html',
                        controller: 'Supply_typeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supply_type');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supply_type.detail', {
                parent: 'entity',
                url: '/supply_type/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.supply_type.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supply_type/supply_type-detail.html',
                        controller: 'Supply_typeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supply_type');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Supply_type', function($stateParams, Supply_type) {
                        return Supply_type.get({id : $stateParams.id});
                    }]
                }
            })
            .state('supply_type.new', {
                parent: 'supply_type',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supply_type/supply_type-dialog.html',
                        controller: 'Supply_typeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('supply_type', null, { reload: true });
                    }, function() {
                        $state.go('supply_type');
                    })
                }]
            })
            .state('supply_type.edit', {
                parent: 'supply_type',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/supply_type/supply_type-dialog.html',
                        controller: 'Supply_typeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Supply_type', function(Supply_type) {
                                return Supply_type.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('supply_type', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
