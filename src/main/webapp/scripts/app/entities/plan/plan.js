'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('plan', {
                parent: 'entity',
                url: '/plans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.plan.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plan/plans.html',
                        controller: 'PlanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plan');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('plan.detail', {
                parent: 'entity',
                url: '/plan/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.plan.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plan/plan-detail.html',
                        controller: 'PlanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plan');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Plan', function($stateParams, Plan) {
                        return Plan.get({id : $stateParams.id});
                    }]
                }
            })
            .state('plan.new', {
                parent: 'plan',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/plan/plan-dialog.html',
                        controller: 'PlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, number: null, archive: null, creation_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('plan', null, { reload: true });
                    }, function() {
                        $state.go('plan');
                    })
                }]
            })
            .state('plan.edit', {
                parent: 'plan',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/plan/plan-dialog.html',
                        controller: 'PlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Plan', function(Plan) {
                                return Plan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('plan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
