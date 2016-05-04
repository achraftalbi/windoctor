'use strict';

angular.module('windoctorApp')
    .controller('ChargeDetailController', function ($scope, $rootScope, $stateParams, entity, Charge, Structure, CategoryCharge, Fund) {
        $scope.charge = entity;
        $scope.load = function (id) {
            Charge.get({id: id}, function(result) {
                $scope.charge = result;
            });
        };
        $rootScope.$on('windoctorApp:chargeUpdate', function(event, result) {
            $scope.charge = result;
        });
    });
