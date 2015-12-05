'use strict';

angular.module('windoctorApp')
    .controller('Supply_typeDetailController', function ($scope, $rootScope, $stateParams, entity, Supply_type) {
        $scope.supply_type = entity;
        $scope.load = function (id) {
            Supply_type.get({id: id}, function(result) {
                $scope.supply_type = result;
            });
        };
        $rootScope.$on('windoctorApp:supply_typeUpdate', function(event, result) {
            $scope.supply_type = result;
        });
    });
