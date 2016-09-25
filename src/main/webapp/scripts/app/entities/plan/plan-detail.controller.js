'use strict';

angular.module('windoctorApp')
    .controller('PlanDetailController', function ($scope, $rootScope, $stateParams, entity, Plan, Structure) {
        $scope.plan = entity;
        $scope.load = function (id) {
            Plan.get({id: id}, function(result) {
                $scope.plan = result;
            });
        };
        $rootScope.$on('windoctorApp:planUpdate', function(event, result) {
            $scope.plan = result;
        });
    });
