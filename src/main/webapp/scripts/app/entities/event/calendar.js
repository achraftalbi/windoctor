'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calendar', {
                parent: 'entity',
                url: '/calendar',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.calendar.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/event/calendar.html',
                        controller: 'CalendarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('event_reason');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('calendar.detail', {
                parent: 'calendar',
                url: '/event_reason/{id}',
                data: {
                    roles: ['ROLE_USER'],
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
            .state('calendar.new', {
                parent: 'calendar',
                url: '/events',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/event/calendar-events.html',
                        controller: 'CalendarEventsController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('event_reason', null, { reload: true });
                        }, function() {
                            $state.go('event_reason');
                        })
                }]
            })
            .state('calendar.edit', {
                parent: 'calendar',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/event_reason/event_reason-dialog.html',
                        controller: 'Event_reasonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Event_reason', function(Event_reason) {
                                return Event_reason.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('event_reason', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        })
                }]
            });
    });

