'use strict';

angular.module('windoctorApp')
    .controller('ConsumptionDetailController', function ($scope, $rootScope, $stateParams, entity, Consumption, Product) {
        $scope.consumption = entity;
        $scope.load = function (id) {
            Consumption.get({id: id}, function(result) {
                $scope.consumption = result;
            });
        };
        $rootScope.$on('windoctorApp:consumptionUpdate', function(event, result) {
            $scope.consumption = result;
        });
    });
