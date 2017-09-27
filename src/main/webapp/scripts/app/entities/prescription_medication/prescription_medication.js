'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prescription_medication', {
                parent: 'entity',
                url: '/prescription_medications',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.prescription_medication.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prescription_medication/prescription_medications.html',
                        controller: 'Prescription_medicationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prescription_medication');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prescription_medication.detail', {
                parent: 'entity',
                url: '/prescription_medication/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.prescription_medication.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prescription_medication/prescription_medication-detail.html',
                        controller: 'Prescription_medicationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prescription_medication');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Prescription_medication', function($stateParams, Prescription_medication) {
                        return Prescription_medication.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prescription_medication.new', {
                parent: 'prescription_medication',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prescription_medication/prescription_medication-dialog.html',
                        controller: 'Prescription_medicationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {medication_name: null, man_description: null, woman_description: null, child_description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prescription_medication', null, { reload: true });
                    }, function() {
                        $state.go('prescription_medication');
                    })
                }]
            })
            .state('prescription_medication.edit', {
                parent: 'prescription_medication',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prescription_medication/prescription_medication-dialog.html',
                        controller: 'Prescription_medicationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Prescription_medication', function(Prescription_medication) {
                                return Prescription_medication.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prescription_medication', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
