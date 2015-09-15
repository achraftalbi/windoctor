'use strict';

angular.module('windoctorApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, Category) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        $rootScope.$on('windoctorApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
    });
