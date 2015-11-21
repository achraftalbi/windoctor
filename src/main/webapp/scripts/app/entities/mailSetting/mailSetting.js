'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mailSetting', {
                parent: 'entity',
                url: '/mailSettings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.mailSetting.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailSetting/mailSettings.html',
                        controller: 'MailSettingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailSetting');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mailSetting.detail', {
                parent: 'entity',
                url: '/mailSetting/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.mailSetting.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailSetting/mailSetting-detail.html',
                        controller: 'MailSettingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailSetting');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MailSetting', function($stateParams, MailSetting) {
                        return MailSetting.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mailSetting.new', {
                parent: 'mailSetting',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mailSetting/mailSetting-dialog.html',
                        controller: 'MailSettingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    activated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mailSetting', null, { reload: true });
                    }, function() {
                        $state.go('mailSetting');
                    })
                }]
            })
            .state('mailSetting.edit', {
                parent: 'mailSetting',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mailSetting/mailSetting-dialog.html',
                        controller: 'MailSettingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MailSetting', function(MailSetting) {
                                return MailSetting.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mailSetting', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
