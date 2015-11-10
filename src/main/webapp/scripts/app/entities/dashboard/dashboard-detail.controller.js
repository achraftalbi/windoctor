'use strict';

angular.module('windoctorApp')
    .controller('DashboardDetailController', function ($scope, $rootScope, $stateParams, entity, Dashboard) {
        $scope.dashboard = entity;
        $scope.load = function (id) {
            Dashboard.get({id: id}, function(result) {
                $scope.dashboard = result;
            });
        };
        $rootScope.$on('windoctorApp:dashboardUpdate', function(event, result) {
            $scope.dashboard = result;
        });
    });
