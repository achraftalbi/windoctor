'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('consumption', {
                parent: 'entity',
                url: '/consumptions',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.consumption.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/consumption/consumptions.html',
                        controller: 'ConsumptionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('consumption');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('consumption.detail', {
                parent: 'entity',
                url: '/consumption/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.consumption.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/consumption/consumption-detail.html',
                        controller: 'ConsumptionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('consumption');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Consumption', function($stateParams, Consumption) {
                        return Consumption.get({id : $stateParams.id});
                    }]
                }
            })
            .state('consumption.new', {
                parent: 'consumption',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/consumption/consumption-dialog.html',
                        controller: 'ConsumptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {amount: null, creation_date: null, relative_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('consumption', null, { reload: true });
                    }, function() {
                        $state.go('consumption');
                    })
                }]
            })
            .state('consumption.edit', {
                parent: 'consumption',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/consumption/consumption-dialog.html',
                        controller: 'ConsumptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Consumption', function(Consumption) {
                                return Consumption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('consumption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
