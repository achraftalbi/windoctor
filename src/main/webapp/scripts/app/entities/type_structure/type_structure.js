'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('type_structure', {
                parent: 'entity',
                url: '/type_structures',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.type_structure.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type_structure/type_structures.html',
                        controller: 'Type_structureController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('type_structure');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('type_structure.detail', {
                parent: 'entity',
                url: '/type_structure/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.type_structure.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/type_structure/type_structure-detail.html',
                        controller: 'Type_structureDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('type_structure');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Type_structure', function($stateParams, Type_structure) {
                        return Type_structure.get({id : $stateParams.id});
                    }]
                }
            })
            .state('type_structure.new', {
                parent: 'type_structure',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/type_structure/type_structure-dialog.html',
                        controller: 'Type_structureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description_en: null, description_fr: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('type_structure', null, { reload: true });
                    }, function() {
                        $state.go('type_structure');
                    })
                }]
            })
            .state('type_structure.edit', {
                parent: 'type_structure',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/type_structure/type_structure-dialog.html',
                        controller: 'Type_structureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Type_structure', function(Type_structure) {
                                return Type_structure.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('type_structure', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
