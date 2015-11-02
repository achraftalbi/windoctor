'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('structure', {
                parent: 'entity',
                url: '/structures',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.structure.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/structure/structures.html',
                        controller: 'StructureController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('structure');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('structure.detail', {
                parent: 'entity',
                url: '/structure/{id}',
                data: {
                    roles: ['ROLE_DOCTOR'],
                    pageTitle: 'windoctorApp.structure.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/structure/structure-detail.html',
                        controller: 'StructureDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('structure');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Structure', function($stateParams, Structure) {
                        return Structure.get({id : $stateParams.id});
                    }]
                }
            })
            .state('structure.new', {
                parent: 'structure',
                url: '/new',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/structure/structure-dialog.html',
                        controller: 'StructureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('structure', null, { reload: true });
                    }, function() {
                        $state.go('structure');
                    })
                }]
            })
            .state('structure.edit', {
                parent: 'structure',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_DOCTOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/structure/structure-dialog.html',
                        controller: 'StructureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Structure', function(Structure) {
                                return Structure.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('structure', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
