'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mailType', {
                parent: 'entity',
                url: '/mailTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.mailType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailType/mailTypes.html',
                        controller: 'MailTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mailType.detail', {
                parent: 'entity',
                url: '/mailType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.mailType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailType/mailType-detail.html',
                        controller: 'MailTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MailType', function($stateParams, MailType) {
                        return MailType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mailType.new', {
                parent: 'mailType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mailType/mailType-dialog.html',
                        controller: 'MailTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    label: null,
                                    description: null,
                                    content: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mailType', null, { reload: true });
                    }, function() {
                        $state.go('mailType');
                    })
                }]
            })
            .state('mailType.edit', {
                parent: 'mailType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mailType/mailType-dialog.html',
                        controller: 'MailTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MailType', function(MailType) {
                                return MailType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mailType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
