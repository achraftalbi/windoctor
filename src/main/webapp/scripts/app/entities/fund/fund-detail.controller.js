'use strict';

angular.module('windoctorApp')
    .controller('FundDetailController', function ($scope, $rootScope, $stateParams, entity, Fund, Fund_history) {
        $scope.fund = entity;
        $scope.load = function (id) {
            Fund.get({id: id}, function(result) {
                $scope.fund = result;
            });
        };
        $rootScope.$on('windoctorApp:fundUpdate', function(event, result) {
            $scope.fund = result;
        });
    });
