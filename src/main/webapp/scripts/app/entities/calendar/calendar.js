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
                                //$stateParams.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                                $stateParams.selectedDate = new Date($stateParams.selectedDate)+'';
                                console.log("selectedDate rows "+$stateParams.selectedDate);
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar', null, { reload: true });
                        }, function() {
                            $state.go('calendar',{ reload: true });
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
                                console.log("selectedDate 2treatment "+$stateParams.selectedDate);
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            console.log("selectedDate 2treatment3 "+$stateParams.selectedDate);
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            console.log("selectedDate 2treatment4 "+$stateParams.selectedDate);
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            })
            .state('calendar.newEventAppointment', {
                parent: 'calendar',
                url: '/calendar-dialog-event/{selectedDate}',
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
                                return {event_date: null, event_date_end: null, description: null, id: null,eventStatus:{id:1}};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate}, { reload: true });
                        }, function() {
                            $state.go('calendar.rows', { selectedDate: $stateParams.selectedDate});
                        })
                }]
            })
            .state('calendar.newEventRequest', {
                parent: 'calendar',
                url: '/calendar-dialog-request/{selectedDate}',
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
                                console.log("selectedDate 4-4 visite "+$stateParams.selectedDate);
                                return {event_date: null, event_date_end: null, description: null, id: null,eventStatus:{id:7}};
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
                url: '/calendar-dialog-visit/{selectedDate}',
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
                                console.log("selectedDate 4-4 visite "+$stateParams.selectedDate);
                                return {event_date: null, event_date_end: null, description: null, id: null,eventStatus:{id:8}};
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

