'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fund', {
                parent: 'entity',
                url: '/funds',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.fund.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fund/funds.html',
                        controller: 'FundController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fund');
                        $translatePartialLoader.addPart('fund_history');
                        $translatePartialLoader.addPart('product');
                        $translatePartialLoader.addPart('treatment');
                        $translatePartialLoader.addPart('charge');
                        $translatePartialLoader.addPart('supply_type');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                },
                ncyBreadcrumb: {
                    label: '<span class="fa fa-bank" ></span>global.menu.entities.fund'// angular-breadcrumb's configuration
                }

            })
            .state('fund.detail', {
                parent: 'entity',
                url: '/fund/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.fund.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fund/fund-detail.html',
                        controller: 'FundDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fund');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Fund', function($stateParams, Fund) {
                        return Fund.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fund.new', {
                parent: 'fund',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fund/fund-dialog.html',
                        controller: 'FundDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, amount: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('fund', null, { reload: true });
                    }, function() {
                        $state.go('fund');
                    })
                }]
            })
            .state('fund.edit', {
                parent: 'fund',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/fund/fund-dialog.html',
                        controller: 'FundDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Fund', function(Fund) {
                                return Fund.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fund', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
