'use strict';

angular.module('windoctorApp')
    .controller('PurchaseDetailController', function ($scope, $rootScope, $stateParams, entity, Purchase, Product, Fund) {
        $scope.purchase = entity;
        $scope.load = function (id) {
            Purchase.get({id: id}, function(result) {
                $scope.purchase = result;
            });
        };
        $rootScope.$on('windoctorApp:purchaseUpdate', function(event, result) {
            $scope.purchase = result;
        });
    });
