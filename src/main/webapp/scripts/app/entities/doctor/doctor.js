'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('doctor', {
                parent: 'entity',
                url: '/doctors',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.doctor.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/doctor/doctors.html',
                        controller: 'DoctorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('doctor');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('doctor.detail', {
                parent: 'entity',
                url: '/doctor/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.doctor.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/doctor/doctor-detail.html',
                        controller: 'DoctorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('doctor');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Doctor', function($stateParams, Doctor) {
                        return Doctor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('doctor.new', {
                parent: 'doctor',
                url: '/new',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/doctor/doctor-dialog.html',
                        controller: 'DoctorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {login: null, password: null, firstName: null, lastName: null, email: null, activated: null, blocked: null, picture: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('doctor', null, { reload: true });
                    }, function() {
                        $state.go('doctor');
                    })
                }]
            })
            .state('doctor.edit', {
                parent: 'doctor',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/doctor/doctor-dialog.html',
                        controller: 'DoctorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Doctor', function(Doctor) {
                                return Doctor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('doctor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
