'use strict';

angular.module('windoctorApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Product) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        $rootScope.$on('windoctorApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
    });
