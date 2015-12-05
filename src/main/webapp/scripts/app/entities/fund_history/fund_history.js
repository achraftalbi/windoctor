'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fund_history', {
                parent: 'entity',
                url: '/fund_historys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.fund_history.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fund_history/fund_historys.html',
                        controller: 'Fund_historyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fund_history');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('fund_history.detail', {
                parent: 'entity',
                url: '/fund_history/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.fund_history.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fund_history/fund_history-detail.html',
                        controller: 'Fund_historyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fund_history');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Fund_history', function($stateParams, Fund_history) {
                        return Fund_history.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fund_history.new', {
                parent: 'fund_history',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fund_history/fund_history-dialog.html',
                        controller: 'Fund_historyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {old_amount: null, new_amount: null, type_operation: null, amount_movement: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('fund_history', null, { reload: true });
                    }, function() {
                        $state.go('fund_history');
                    })
                }]
            })
            .state('fund_history.edit', {
                parent: 'fund_history',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fund_history/fund_history-dialog.html',
                        controller: 'Fund_historyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Fund_history', function(Fund_history) {
                                return Fund_history.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fund_history', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
