'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('medication', {
                parent: 'entity',
                url: '/medications',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.medication.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medication/medications.html',
                        controller: 'MedicationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medication');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('medication.detail', {
                parent: 'entity',
                url: '/medication/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.medication.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medication/medication-detail.html',
                        controller: 'MedicationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medication');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Medication', function($stateParams, Medication) {
                        return Medication.get({id : $stateParams.id});
                    }]
                }
            })
            .state('medication.new', {
                parent: 'medication',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medication/medication-dialog.html',
                        controller: 'MedicationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, man_description: null, woman_description: null, child_description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('medication', null, { reload: true });
                    }, function() {
                        $state.go('medication');
                    })
                }]
            })
            .state('medication.edit', {
                parent: 'medication',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medication/medication-dialog.html',
                        controller: 'MedicationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Medication', function(Medication) {
                                return Medication.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('medication', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
