'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calendar', {
                parent: 'entity',
                url: '/calendar',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.calendar.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calendar/calendar.html',
                        controller: 'CalendarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calendar');
                        $translatePartialLoader.addPart('patient');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('calendar.detail', {
                parent: 'calendar',
                url: '/event_reason/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.event_reason.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/event_reason/event_reason-detail.html',
                        controller: 'Event_reasonDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('event_reason');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Event_reason', function($stateParams, Event_reason) {
                        return Event_reason.get({id : $stateParams.id});
                    }]
                }
            })
            .state('calendar.rows', {
                parent: 'calendar',
                url: '/calendar-events/{selectedDate}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal','$filter', function($stateParams, $state, $modal,$filter) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calendar/calendar-events.html',
                        controller: 'CalendarEventsController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                $stateParams.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                                console.log("selectedDate rows "+$stateParams.selectedDate);
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar', null, { reload: true });
                        }, function() {
                            $state.go('calendar');
                        })
                }]
            })
            .state('calendar.patientDetail', {
                parent: 'calendar',
                url: '/patient/{id}/{selectedDate}',
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
            .state('calendar.treatment', {
                parent: 'calendar',
                url: '/treatments/{eventId}/{selectedDate}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.treatment.home.title'
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calendar/calendar-treatments.html',
                        controller: 'CalendarTreatmentController',
                        size: 'lg',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('treatment');
                                $translatePartialLoader.addPart('attachment');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }],
                            entity: function () {
                                console.log("selectedDate 2 "+$stateParams.selectedDate);
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            })
            .state('calendar.newEventAppointment', {
                parent: 'calendar',
                url: '/calendar-dialog/{selectedDate}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calendar/calendar-dialog.html',
                        controller: 'CalendarDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                console.log("selectedDate 3-3 "+$stateParams.selectedDate);
                                return {event_date: new Date($stateParams.selectedDate), description: null, id: null,thisEventISAppointment:true};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            })
            .state('calendar.newEventVisit', {
                parent: 'calendar',
                url: '/calendar-dialog/{selectedDate}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calendar/calendar-dialog.html',
                        controller: 'CalendarDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                console.log("selectedDate 3-3 "+$stateParams.selectedDate);
                                return {event_date: new Date($stateParams.selectedDate), description: null, id: null,thisEventISAppointment:false};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            })
            .state('calendar.edit', {
                parent: 'calendar',
                url: '/{id}/edit/{selectedDate}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/calendar/calendar-dialog.html',
                        controller: 'CalendarDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Event', function(Event) {
                                return Event.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            });
    });

