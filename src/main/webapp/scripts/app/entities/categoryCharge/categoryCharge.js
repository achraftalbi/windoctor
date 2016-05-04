'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('categoryCharge', {
                parent: 'entity',
                url: '/categoryCharges',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.categoryCharge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryCharge/categoryCharges.html',
                        controller: 'CategoryChargeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoryCharge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-folder-open" ></span>global.menu.entities.categoryCharge'// angular-breadcrumb's configuration
                }
            })
            .state('categoryCharge.detail', {
                parent: 'entity',
                url: '/categoryCharge/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.categoryCharge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryCharge/categoryCharge-detail.html',
                        controller: 'CategoryChargeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoryCharge');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CategoryCharge', function($stateParams, CategoryCharge) {
                        return CategoryCharge.get({id : $stateParams.id});
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-folder-open" ></span>global.menu.entities.categoryCharge'// angular-breadcrumb's configuration
                }
            })
            .state('categoryCharge.new', {
                parent: 'categoryCharge',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoryCharge/categoryCharge-dialog.html',
                        controller: 'CategoryChargeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('categoryCharge', null, { reload: true });
                    }, function() {
                        $state.go('categoryCharge');
                    })
                }]
            })
            .state('categoryCharge.edit', {
                parent: 'categoryCharge',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoryCharge/categoryCharge-dialog.html',
                        controller: 'CategoryChargeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CategoryCharge', function(CategoryCharge) {
                                return CategoryCharge.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('categoryCharge', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
