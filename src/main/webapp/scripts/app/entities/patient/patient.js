'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('patient', {
                parent: 'entity',
                url: '/patients',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.patient.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patients.html',
                        controller: 'PatientController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        $translatePartialLoader.addPart('treatment');
                        $translatePartialLoader.addPart('attachment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-users" ></span>global.menu.entities.patient'// angular-breadcrumb's configuration
                }

            })
            .state('patient.detail', {
                parent: 'entity',
                url: '/patient/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.patient.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patient-detail.html',
                        controller: 'PatientDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Patient', function($stateParams, Patient) {
                        return Patient.get({id : $stateParams.id});
                    }]
                }
            })
            .state('patient.new', {
                parent: 'patient',
                url: '/new',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/patient/patient-dialog.html',
                        controller: 'PatientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {login: null, password: null, firstName: null, lastName: null, email: null, activated: true, blocked: false, picture: null,phoneNumber: '', langKey:'fr', id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('patient', null, { reload: true });
                    }, function() {
                        $state.go('patient');
                    })
                }]
            })
            .state('patient.patientChart', {
                parent: 'patient',
                url: '/treatments/{patientId}',
                data: {
                    roles: ['ROLE_ASSISTANT'],
                    pageTitle: 'windoctorApp.treatment.home.title'
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/patient/patientChart.html',
                        controller: 'PatientChartController',
                        size: 'lg',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('treatment');
                                $translatePartialLoader.addPart('attachment');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }],
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('patient', null, { reload: true });
                        }, function() {
                            $state.go('patient');
                        })
                }]
            })
            .state('patient.edit', {
                parent: 'patient',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/patient/patient-dialog.html',
                        controller: 'PatientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Patient', function(Patient) {
                                return Patient.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('patient', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
