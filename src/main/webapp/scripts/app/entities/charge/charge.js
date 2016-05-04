'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('charge', {
                parent: 'entity',
                url: '/charges',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.charge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/charge/charges.html',
                        controller: 'ChargeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('entry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-cubes" ></span>global.menu.entities.charge'// angular-breadcrumb's configuration
                }

            })
            .state('charge.detail', {
                parent: 'entity',
                url: '/charge/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.charge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/charge/charge-detail.html',
                        controller: 'ChargeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('charge');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Charge', function($stateParams, Charge) {
                        return Charge.get({id : $stateParams.id});
                    }]
                }
            })
            .state('charge.new', {
                parent: 'charge',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/charge/charge-dialog.html',
                        controller: 'ChargeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, image: null, price: null, amount: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('charge', null, { reload: true });
                    }, function() {
                        $state.go('charge');
                    })
                }]
            })
            .state('charge.edit', {
                parent: 'charge',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/charge/charge-dialog.html',
                        controller: 'ChargeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Charge', function(Charge) {
                                return Charge.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('charge', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
