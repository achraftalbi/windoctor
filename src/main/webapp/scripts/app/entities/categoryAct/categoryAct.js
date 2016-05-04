'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('categoryAct', {
                parent: 'entity',
                url: '/categoryActs',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.categoryAct.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryAct/categoryActs.html',
                        controller: 'CategoryActController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoryAct');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-folder-open" ></span>global.menu.entities.categoryAct'// angular-breadcrumb's configuration
                }
            })
            .state('categoryAct.detail', {
                parent: 'entity',
                url: '/categoryAct/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.categoryAct.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoryAct/categoryAct-detail.html',
                        controller: 'CategoryActDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoryAct');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CategoryAct', function($stateParams, CategoryAct) {
                        return CategoryAct.get({id : $stateParams.id});
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-folder-open" ></span>global.menu.entities.categoryAct'// angular-breadcrumb's configuration
                }
            })
            .state('categoryAct.new', {
                parent: 'categoryAct',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoryAct/categoryAct-dialog.html',
                        controller: 'CategoryActDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('categoryAct', null, { reload: true });
                    }, function() {
                        $state.go('categoryAct');
                    })
                }]
            })
            .state('categoryAct.edit', {
                parent: 'categoryAct',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoryAct/categoryAct-dialog.html',
                        controller: 'CategoryActDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CategoryAct', function(CategoryAct) {
                                return CategoryAct.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('categoryAct', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
