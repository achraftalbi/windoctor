'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('treatment', {
                parent: 'entity',
                url: '/treatments',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.treatment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/treatment/treatments.html',
                        controller: 'TreatmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('treatment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('treatment.detail', {
                parent: 'entity',
                url: '/treatment/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.treatment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/treatment/treatment-detail.html',
                        controller: 'TreatmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('treatment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Treatment', function($stateParams, Treatment) {
                        return Treatment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('treatment.new', {
                parent: 'treatment',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/treatment/treatment-dialog.html',
                        controller: 'TreatmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {treatment_date: null, description: null, price: null, paid_price: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('treatment', null, { reload: true });
                    }, function() {
                        $state.go('treatment');
                    })
                }]
            })
            .state('treatment.edit', {
                parent: 'treatment',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/treatment/treatment-dialog.html',
                        controller: 'TreatmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Treatment', function(Treatment) {
                                return Treatment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('treatment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
