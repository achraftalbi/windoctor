'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('attachment', {
                parent: 'entity',
                url: '/attachments',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.attachment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attachment/attachments.html',
                        controller: 'AttachmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('attachment.detail', {
                parent: 'entity',
                url: '/attachment/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.attachment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attachment/attachment-detail.html',
                        controller: 'AttachmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Attachment', function($stateParams, Attachment) {
                        return Attachment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('attachment.new', {
                parent: 'attachment',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, image: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('attachment', null, { reload: true });
                    }, function() {
                        $state.go('attachment');
                    })
                }]
            })
            .state('attachment.edit', {
                parent: 'attachment',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Attachment', function(Attachment) {
                                return Attachment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('attachment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
