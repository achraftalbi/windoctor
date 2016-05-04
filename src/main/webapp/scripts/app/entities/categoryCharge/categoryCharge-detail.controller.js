'use strict';

angular.module('windoctorApp')
    .controller('CategoryChargeDetailController', function ($scope, $rootScope, $stateParams, entity, CategoryCharge, Structure) {
        $scope.categoryCharge = entity;
        $scope.load = function (id) {
            CategoryCharge.get({id: id}, function(result) {
                $scope.categoryCharge = result;
            });
        };
        $rootScope.$on('windoctorApp:categoryChargeUpdate', function(event, result) {
            $scope.categoryCharge = result;
        });
    });
