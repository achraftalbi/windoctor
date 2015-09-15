'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('event_reason', {
                parent: 'entity',
                url: '/event_reasons',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.event_reason.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/event_reason/event_reasons.html',
                        controller: 'Event_reasonController'
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
            .state('event_reason.detail', {
                parent: 'entity',
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
            .state('event_reason.new', {
                parent: 'event_reason',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/event_reason/event_reason-dialog.html',
                        controller: 'Event_reasonDialogController',
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
            .state('event_reason.edit', {
                parent: 'event_reason',
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
