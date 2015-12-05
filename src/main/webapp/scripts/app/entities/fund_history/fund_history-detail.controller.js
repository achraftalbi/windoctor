'use strict';

angular.module('windoctorApp')
    .controller('Fund_historyDetailController', function ($scope, $rootScope, $stateParams, entity, Fund_history, Fund, Treatment, Product) {
        $scope.fund_history = entity;
        $scope.load = function (id) {
            Fund_history.get({id: id}, function(result) {
                $scope.fund_history = result;
            });
        };
        $rootScope.$on('windoctorApp:fund_historyUpdate', function(event, result) {
            $scope.fund_history = result;
        });
    });
