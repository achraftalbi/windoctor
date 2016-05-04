'use strict';

angular.module('windoctorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('purchase', {
                parent: 'entity',
                url: '/purchases',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.purchase.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchase/purchases.html',
                        controller: 'PurchaseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('purchase');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('purchase.detail', {
                parent: 'entity',
                url: '/purchase/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'windoctorApp.purchase.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchase/purchase-detail.html',
                        controller: 'PurchaseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('purchase');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Purchase', function($stateParams, Purchase) {
                        return Purchase.get({id : $stateParams.id});
                    }]
                }
            })
            .state('purchase.new', {
                parent: 'purchase',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchase/purchase-dialog.html',
                        controller: 'PurchaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {price: null, amount: null, creation_date: null, relative_date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('purchase', null, { reload: true });
                    }, function() {
                        $state.go('purchase');
                    })
                }]
            })
            .state('purchase.edit', {
                parent: 'purchase',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchase/purchase-dialog.html',
                        controller: 'PurchaseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Purchase', function(Purchase) {
                                return Purchase.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('purchase', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
