'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prescription', {
                parent: 'entity',
                url: '/prescriptions',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.prescription.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prescription/prescriptions.html',
                        controller: 'PrescriptionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prescription');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prescription.detail', {
                parent: 'entity',
                url: '/prescription/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.prescription.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prescription/prescription-detail.html',
                        controller: 'PrescriptionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prescription');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Prescription', function($stateParams, Prescription) {
                        return Prescription.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prescription.new', {
                parent: 'prescription',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prescription/prescription-dialog.html',
                        controller: 'PrescriptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, archived: null, medication_persist: null, creation_date: null, update_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prescription', null, { reload: true });
                    }, function() {
                        $state.go('prescription');
                    })
                }]
            })
            .state('prescription.edit', {
                parent: 'prescription',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prescription/prescription-dialog.html',
                        controller: 'PrescriptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Prescription', function(Prescription) {
                                return Prescription.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prescription', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
